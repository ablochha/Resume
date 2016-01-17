/*
 * hftp_server.c
 *
 * Computer Science 3357a - Fall 2015
 * Author: Andrew Bloch-Hansen
 *
 * Handle's a client's connection according to the Hooli File Transfer Protocol.
 */

#include <stdbool.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <poll.h>
#include <syslog.h>

#include "../common/checksum.h"
#include "../common/filereader.h"
#include "../common/hftp.h"
#include "../common/puts3.h"
#include "../common/udp_sockets.h"
#include "../hdb/hdb.h"
#include "hftp_server.h"

#define RESPONSE_TYPE 255
#define ERROR 1
#define NO_ERROR 0
#define HFTP_MSS 1472
#define TRANSFER_THRESHOLD_PACKETS 5
#define TRANSFER_THRESHOLD_PERCENT 0.1
#define SLASH "/"

// Creates a new file variable
// @return a pointer to the file variable
static file_info* create_file() {
    
    file_info *file = (file_info*)malloc(sizeof(file_info));
    
    file->username = NULL;
    file->filename = NULL;
    file->path = NULL;
    file->checksum = NULL;
    file->file_buffer = NULL;
    
    file->filename_length = 0;
    file->file_size = 0;
    file->file_received_size = 0;
    file->file_buffer_size = 0;
    file->transfer_threshold = 0;
    file->packets_in_buffer = 0;
    file->percent = 0.0;
    
    return file;
    
} //end create_file

// Frees the file variable
// @param file the file variable
static void free_file_info(file_info **file) {
    
    free((*file)->username);
    free((*file)->filename);
    free((*file)->path);
    free((*file)->checksum);
    free((*file)->file_buffer);
    
    (*file)->username = NULL;
    (*file)->filename = NULL;
    (*file)->path = NULL;
    (*file)->checksum = NULL;
    (*file)->file_buffer = NULL;
    
    (*file)->filename_length = 0;
    (*file)->file_size = 0;
    (*file)->file_received_size = 0;
    (*file)->file_buffer_size = 0;
    (*file)->transfer_threshold = 0;
    (*file)->packets_in_buffer = 0;
    (*file)->percent = 0.0;
    
    free(*file);
    *file = NULL;
    
} //end free_file_info

// Compares the re-calculated checksum to the control message
// @param file a pointer to the file variable
// @return true if the checksums are the same, false otherwise
static bool verify_file(file_info *file) {
    
    char *calculated_checksum = compute_crc(file->path);
    
    // The file was correctly received
    if (strcmp(calculated_checksum, file->checksum) == 0) {
        
        free(calculated_checksum);
        return true;
        
    } //end if
    
    // The file has an error
    else {
        
        syslog(LOG_INFO, "'%s' could not be added, incorrect checksum", file->filename);
        free(calculated_checksum);
        return false;
        
    } //end else
    
} //end verify_file

// Verifies the client's token with the Hooli database
// @param request the client's request
// @param server the hooli database server
// @return the username associated with the token
static char* verify_token(hftp_control_message *request, char *server) {
    
    char token[16];
    char *username = NULL;
    
    memcpy(token, request->token, 16);
    token[16] = '\0';
    
    hdb_connection *con = hdb_connect(server);
    username = hdb_verify_token(con, token);
    hdb_disconnect(con);
    
    return username;
    
} //end verify_token

// Adds a file to the Hooli database
// @param file a pointer to a file variable
// @param server the hooli database server
static void update_file_metadata(file_info **file, char *server) {
    
    hdb_connection *con = hdb_connect(server);
    
    hdb_record rec = {
        .username = (*file)->username,
        .filename = (*file)->filename,
        .checksum = (*file)->checksum
    };
    
    hdb_remove_file(con, (*file)->username, (*file)->filename);
    hdb_store_file(con, &rec);
    
    hdb_disconnect(con);
    
} //end update_file_metadata

// Build the appropriate pathname for a file
// @param file a pointer to the file variable
// @param root the input root directory
static void build_path(file_info **file, char *root) {
    
    // Build the pathname to transfer the file to
    int dir_length = strlen(root) + strlen((*file)->username) + 1;
    char *dir = malloc(dir_length + 1);
    
    strcpy(dir, root);
    strcat(dir, (*file)->username);
    strcat(dir, SLASH);
    dir[dir_length] = '\0';
    
    int path_length = strlen(dir) + (*file)->filename_length;
    (*file)->path = malloc(path_length + 1);
    
    strcpy((*file)->path, dir);
    strcat((*file)->path, (*file)->filename);
    (*file)->path[path_length] = '\0';
    
    make_all_directories((*file)->path);
    
    free(dir);
    
} //end build_path

// Calculates the size of the file buffer
// @param file a pointer to the file variable
static int get_buffer_size(file_info **file) {
    
    // File buffer will write every x amount of data packets
    if (TRANSFER_THRESHOLD_PACKETS * HFTP_MSS >= (*file)->transfer_threshold) {
        
        return ((TRANSFER_THRESHOLD_PACKETS + 1) * HFTP_MSS);
        
    } //end if
    
    // File buffer will write every x% of file transferred
    else {
        
        return ((*file)->transfer_threshold + HFTP_MSS); // File buffer
        
    } //end else
    
} //end get_buffer_size

// Acknowledges a request from a client
// @param sockfd the socket file descriptor
// @param client the client's information
// @param sequence the sequence number of the response
// @param error_code the error code of the resposne
// @return -1 if the message failed to send
static int ack(int sockfd, host *client, int sequence, int error_code) {
    
    hftp_response_message *response = (hftp_response_message*)create_response_message(RESPONSE_TYPE, sequence, error_code);
    syslog(LOG_DEBUG, "Ack'ed, Sequence: %d, Error code: %d", response->sequence, response->error_code);
    
    if (send_message(sockfd, (message*)response, client) == -1) {
        
        free(response);
        return -1;
        
    } //end if
    
    free(response);
    return 1;
    
} //end ack

// Reads the information in a control message
// @param request the client's request message
// @param file a pointer to the file variable
// @param root the input root directory
// @param expected_sequence the expected sequence number for the request
static void read_control(hftp_control_message *request, file_info **file, char *root, int *expected_sequence) {
    
    (*file)->filename_length = ntohs(request->filename_length);             // Filename length
    (*file)->filename = malloc((*file)->filename_length + 1);               // Filename buffer
    memcpy((*file)->filename, request->filename, (*file)->filename_length); // Copy filename
    (*file)->filename[(*file)->filename_length] = '\0';                     // Null byte
    
    (*file)->file_size = ntohl(request->file_size);                                 // File size
    (*file)->transfer_threshold = (*file)->file_size * TRANSFER_THRESHOLD_PERCENT;  // Update every X%
    (*file)->file_buffer = malloc(get_buffer_size(file));                           // Make file buffer
    
    (*file)->checksum = checksum_to_char(ntohl(request->checksum));                 // Copy checksum
    
    build_path(file, root);
    write_new_file((*file)->path);
    
    // Increment the expected sequence number
    *expected_sequence = (*expected_sequence + 1) % 2;
    
} //end read_control

// Reads the information in a data message
// @param request the client's request message
// @param file a pointer to the file variable
// @param expected_sequence the expected sequence number for the request
static void read_data(hftp_data_message *request, file_info **file, int *expected_sequence) {
    
    memcpy((*file)->file_buffer + (*file)->file_buffer_size, request->data, ntohs(request->data_length));
    (*file)->file_received_size += ntohs(request->data_length);
    (*file)->file_buffer_size += ntohs(request->data_length);
    (*file)->packets_in_buffer++;
    
    // Increment the expected sequence number
    *expected_sequence = (*expected_sequence + 1) % 2;
    
} //end read_data

// Closes previous file (if applicable) and opens a new file
// @param sockfd the socket file descriptor
// @param server the Hooli server
// @param client the client's information
// @param request the client's request message
// @param expected_sequence the expected sequence number for the request
// @param file a pointer to the file variable
// @param root the input root directory
// @return -1 if there was an error
static int handle_control(int sockfd, char *server, host *client, hftp_control_message *request,
                          int *expected_sequence, file_info **file, char *root) {
    
    syslog(LOG_DEBUG, "Received Type: %d, Sequence: %d, Filename length: %d",
           request->type, request->sequence, ntohs(request->filename_length));
    
    // The request has the expected sequence
    if (request->sequence == *expected_sequence) {
        
        // Close and transfer the previous file
        if (*file != NULL) {
            
            // Write the rest of the file buffer to file
            if ((*file)->file_buffer_size > 0) {
                
                (*file)->percent = ((double)(*file)->file_received_size / (double)(*file)->file_size) * 100;
                syslog(LOG_INFO, "...Bytes received: %d of %d (%.2f%%)",
                       (*file)->file_received_size, (*file)->file_size, (*file)->percent);
                
                (*file)->file_buffer[(*file)->file_buffer_size] = '\0';
                write_buffer_to_file((*file)->file_buffer, (*file)->path, (*file)->file_buffer_size);
                
            } //end if
            
            if (verify_file(*file)) {
                
                update_file_metadata(file, server);
                //putFileIntoS3((*file)->path, (*file)->path);
                //remove_file((*file)->path);
                
            } //end if
            
            free_file_info(file);
            
        } //end if
        
        *file = create_file();
        
        (*file)->username = verify_token(request, server);
        
        // The client's token is invalid
        if ((*file)->username == NULL) {
            
            syslog(LOG_INFO, "Invalid token");
            ack(sockfd, client, request->sequence, ERROR);
        
            free(request);
            
            free_file_info(file);
            
            return -1;
            
        } //end if
        
        // The client's token is valid
        else {
            
            // Ack the request
            ack(sockfd, client, request->sequence, NO_ERROR);
            read_control(request, file, root, expected_sequence);
            
            syslog(LOG_INFO, "Receiving '%s'", (*file)->filename);
        
            free(request);
            
            return 1;
            
        } //end else
        
    } //end if
    
    // The request has the incorrect sequence
    else {
        
        // Ack the received sequence number
        ack(sockfd, client, request->sequence, NO_ERROR);
        free(request);
        
        return 1;
        
    } //end else
    
} //end handle_control

// Closes previous file and exits
// @param sockfd the socket file descriptor
// @param server the Hooli server
// @param timewait the length of the timewait state
// @param request the client's request message
// @param expected_sequence the expected sequence number for the request
// @param file a pointer to the file variable
// @param root the input root directory
// @return -1 if there was an error
static int handle_termination(int sockfd, char *server, host *client, int timewait,
                              hftp_control_message *request, int *expected_sequence,
                              file_info **file, char *root) {
    
    syslog(LOG_DEBUG, "Received Type: %d, Sequence: %d", request->type, request->sequence);
    
    // The request has the expected sequence
    if (request->sequence == *expected_sequence) {
    
        // The client did not send a type 1 control message first
        if (*file == NULL) {
        
            syslog(LOG_INFO, "Expected a type 1 control message first. Invalid protocol");
            ack(sockfd, client, request->sequence, ERROR);
        
            free(request);
            return -1;
        
        } //end if
    
        // Write the rest of the file buffer to file
        if ((*file)->file_buffer_size > 0) {
    
            (*file)->percent = ((double)(*file)->file_received_size / (double)(*file)->file_size) * 100;
            syslog(LOG_INFO, "...Bytes received: %d of %d (%.2f%%)", (*file)->file_received_size, (*file)->file_size, (*file)->percent);
    
            (*file)->file_buffer[(*file)->file_buffer_size] = '\0';
            write_buffer_to_file((*file)->file_buffer, (*file)->path, (*file)->file_buffer_size);
        
        } //end if
    
        if (verify_file(*file)) {
    
            update_file_metadata(file, server);
            //putFileIntoS3((*file)->path, (*file)->path);
            //remove_file((*file)->path);
    
        } //end if
    
        free_file_info(file);
    
        // We will poll sockfd for the POLLIN event
        struct pollfd fd = {
        
            .fd = sockfd,
            .events = POLLIN
        
        };
    
        // Acknowledge any incoming type 2 control requests
        while (1) {
        
            ack(sockfd, client, request->sequence, NO_ERROR);
        
            // Poll the socket for 10 seconds
            int retval = poll(&fd, 1, timewait);
        
            // Received a request during timewait
            if (retval == 1 && fd.revents == POLLIN) {
            
                free(request);
                request = (hftp_control_message*)receive_message(sockfd, client);
            
                // The request was a duplicate termination message
                if (request->type == 2) {
                
                    continue;
                
                } //end if
            
                // The request was not a termination message
                else {
                
                    free(request);
                    *expected_sequence = 0;
                    
                    return 1;
                
                } //end else
            
            } //end if
        
            // No request received during timewait
            else {
            
                free(request);
                *expected_sequence = 0;
                
                return 1;
            
            } //end else
        
        } //end while
        
    } //end if
    
    // The request has the incorrect sequence
    else {
        
        // Ack the received sequence number
        ack(sockfd, client, request->sequence, NO_ERROR);
        free(request);
        
        return 1;
        
    } //end else
    
} //end handle_termination

// Reads data from a data message to a file buffer
// @param sockfd the socket file descriptor
// @param client the client's information
// @param request the client's request message
// @param expected_sequence the expected sequence number for the request
// @param file a pointer to the file variable
// @return -1 if there was an error
static int handle_data(int sockfd, host *client, hftp_data_message *request,
                       int *expected_sequence, file_info **file) {
    
    syslog(LOG_DEBUG, "Received Type: %d, Sequence: %d, Data length: %d", request->type, request->sequence, ntohs(request->data_length));
    
    // The client did not send a type 1 control message first
    if (*file == NULL) {
        
        syslog(LOG_INFO, "Expected a type 1 control message first. Invalid protocol");
        ack(sockfd, client, request->sequence, ERROR);
        
        free(request);
        return -1;
        
    } //end if
    
    // The request has the expected sequence
    if (request->sequence == *expected_sequence) {
        
        // Ack the request
        ack(sockfd, client, request->sequence, NO_ERROR);
        read_data(request, file, expected_sequence);
        
        // Write the buffer to file every 5 percent of the file size
        if (((*file)->file_buffer_size >= (*file)->transfer_threshold) &&
            ((*file)->packets_in_buffer >= TRANSFER_THRESHOLD_PACKETS)) {
            
            (*file)->percent = ((double)(*file)->file_received_size / (double)(*file)->file_size) * 100;
            syslog(LOG_INFO, "...Bytes received: %d of %d (%.2f%%)", (*file)->file_received_size, (*file)->file_size, (*file)->percent);
            
            (*file)->file_buffer[(*file)->file_buffer_size] = '\0';
            write_buffer_to_file((*file)->file_buffer, (*file)->path, (*file)->file_buffer_size);
            
            free((*file)->file_buffer);
            (*file)->file_buffer = NULL;
            
            (*file)->file_buffer = malloc(get_buffer_size(file));
            
            (*file)->file_buffer_size = 0;
            (*file)->packets_in_buffer = 0;
            
        } //end if
        
        free(request);
        
        return 1;
        
    } //end if
    
    // The request has the incorrect sequence
    else {
        
        // Ack the received sequence number
        ack(sockfd, client, request->sequence, NO_ERROR);
        
        free(request);
        
        return 1;
        
    } //end else
    
} //end handle_data

// Reads a request from a client
// @param sockfd the socket file descriptor
// @param server the Hooli server
// @param root the input root directory
// @param timewait the length of the timewait state
// @param expected_sequence the expected sequence number for the request
// @param file a pointer to the file variable
// @return -1 if there was an error
int handle_request(int sockfd, char *server, char *root, int timewait, int *expected_sequence, file_info **file) {
    
    host client;    // Client's address
    
    // Read the request message and generate the response
    hftp_control_message *request = (hftp_control_message*)receive_message(sockfd, &client);
    
    if (request == NULL) {
        
        return -1;
        
    } //end if
    
    switch (request->type) {
            
        // Received a type 1 control message
        case 1:
            
            return handle_control(sockfd, server, &client, request, expected_sequence, file, root);
        
        // Received a type 2 control message
        case 2:
            
            return handle_termination(sockfd, server, &client, timewait, request, expected_sequence, file, root);
            
        // Received a data message
        case 3:
            
            return handle_data(sockfd, &client, (hftp_data_message*)request, expected_sequence, file);
            
        // Received an unknown message type
        default:
            
            return -1;
            
    } //end switch
    
} //end handle_request
