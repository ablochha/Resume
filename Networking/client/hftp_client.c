/*
 * hftp_client.c
 *
 * Computer Science 3357a - Fall 2015
 * Author: Andrew Bloch-Hansen
 *
 * Communicates with the Hooli File Transfer Server according to the Hooli File
 * Transfer Protocol.
 */

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <poll.h>
#include <syslog.h>
#include <unistd.h>

#include "../common/checksum.h"
#include "../common/client_structs.h"
#include "../common/filereader.h"
#include "../common/hftp.h"
#include "../common/linkedlist.h"
#include "../common/udp_sockets.h"
#include "hftp_client.h"
#include "udp_client.h"

#define INITIALIZE 1
#define TERMINATE 2
#define DATA 3
#define MAX_DATA_LENGTH 1468
#define TRANSFER_THRESHOLD_PACKETS 5
#define TRANSFER_THRESHOLD_PERCENT 0.1

// Creates a new file variable
// @return a pointer to the file variable
static file_info* create_file() {
    
    file_info *file = (file_info*)malloc(sizeof(file_info));
    
    file->file_transferred = false;
    
    file->path = NULL;
    file->file_buffer = NULL;
    file->pos = NULL;
    
    file->file_count = 0;
    file->file_length = 0;
    file->bytes_sent = 0;
    file->file_buffer_size = 0;
    file->buffer_length = 0;
    file->packets_in_buffer = 0;
    file->transfer_threshold = 0;
    file->percent = 0.0;
    
    return file;

} //end create_file

// Frees the file variable
// @param file the file variable
static void free_file_info(file_info *file) {
    
    free(file->path);
    free(file->file_buffer);
    
    file->path = NULL;
    file->file_buffer = NULL;
    
    file->file_count = 0;
    file->file_length = 0;
    file->bytes_sent = 0;
    file->file_buffer_size = 0;
    file->buffer_length = 0;
    file->packets_in_buffer = 0;
    file->transfer_threshold = 0;
    file->percent = 0.0;
    
    free(file);
    file = NULL;
    
} //end free_file_info

// Builds the correct path to the file
// @param record a linked list node with the file
// @param dir the input directory
// @param the pathname to the file
static char* build_path(csv_record *record, char *dir) {
    
    // Convert a file to a char array
    char *path = malloc(strlen(dir) + strlen(record->filename) + 1);
    strcpy(path, dir);
    strcat(path, record->filename);
    return path;
    
} //end build_path

// Initializes values for a file
// @param file a pointer to the file variable
// @param client a pointer to the client variable
// @param p a linked list node with the file
static void initialize_file(file_info *file, client_info *client, csv_record *p) {
    
    file->path = build_path(p, client->dir);
    file->file_length = get_file_length(file->path);
    file->transfer_threshold = file->file_length * TRANSFER_THRESHOLD_PERCENT;
    file->file_count = 0;
    file->packets_in_buffer = 0;
    file->bytes_sent = 0;
    file->file_buffer_size = 0;
    
} //end initialize_file

// Computes the size of the chunk to read from the file
// @param file a pointer to the file variable
// @return the size of the chunk to read from the file
static int get_buffer_size(file_info *file) {
    
    // There is not enough data left to fill the standard chunk size
    if ((MAX_DATA_LENGTH * TRANSFER_THRESHOLD_PACKETS) > (file->file_length - file->bytes_sent)) {
        
        return file->file_length - file->bytes_sent;
        
    } //end if
    
    // Standard chunk size fits the TRANSFER_THRESHOLD_PACKETS amount
    else {
        
        return MAX_DATA_LENGTH * TRANSFER_THRESHOLD_PACKETS;
        
    } //end else
    
} //end if

// Computes the size of the data going into a packet
// @param file a pointer to the file variable
// @return the size of the data going into a packet
static int get_data_length(file_info *file) {
    
    // There is enough data to fill a packet
    if ((file->file_length - file->bytes_sent) >= MAX_DATA_LENGTH) {
        
        return MAX_DATA_LENGTH;
        
    } //end if

    // There is not enough data to fill a data packet
    else {
        
        return file->file_length - file->bytes_sent;
        
    } //end else
    
} //end get_data_length

// Reads another chunk of a file to a buffer
// @param file a pointer to a file variable
static void manage_file_buffer(file_info *file) {
    
    // Read another chunk of the file to the file buffer
    if (file->file_count % TRANSFER_THRESHOLD_PACKETS == 0) {
        
        free(file->file_buffer);
        file->file_buffer = NULL;
        
        file->file_buffer = read_from_file(file->path, file->bytes_sent, get_buffer_size(file), &file->buffer_length);
        file->pos = file->file_buffer;
        
    } //end if
    
} //end managa_file_buffer

// Tracks progress of a file, displays update output
// @param file a pointer to a file variable
static void update_progress(file_info *file) {
    
    file->file_count++;
    file->packets_in_buffer++;
    file->file_buffer_size += get_data_length(file);
    file->bytes_sent += get_data_length(file);
    file->pos += MAX_DATA_LENGTH;
    
    // Display progress if its more than X && X%
    if ((file->file_buffer_size >= file->transfer_threshold) &&
        (file->packets_in_buffer >= TRANSFER_THRESHOLD_PACKETS)) {
        
        file->percent = ((double)file->bytes_sent / (double)file->file_length) * 100;
        syslog(LOG_INFO, "...Bytes sent: %d of %d (%.2f%%)", file->bytes_sent, file->file_length, file->percent);
        
        file->file_buffer_size = 0;
        file->packets_in_buffer = 0;
        
    } //end if
    
    // Update the file transferred
    if (file->bytes_sent == file->file_length) {
        
        file->file_transferred = true;
        
    } //end if
    
} //end update_progress

// Displays 100% file transferred
// @param file a pointer to a file variable
static void final_progress_update(file_info *file) {
    
    // Write the rest of the file buffer to file
    if (file->file_buffer_size > 0) {
        
        file->percent = ((double)file->bytes_sent / (double)file->file_length) * 100;
        syslog(LOG_INFO, "...Bytes sent: %d of %d (%.2f%%)", file->bytes_sent, file->file_length, file->percent);
        
    } //end if
    
    // Display empty files as successfully transferred
    if (file->file_length == 0) {
        
        syslog(LOG_INFO, "...Bytes sent: %d of %d (100.00%%)", file->bytes_sent, file->file_length);
        
    } //end if
    
} //end final_progress_update

// Repeatedly sends a message until the server ACK's it
// @param sockfd the socket file descriptor
// @param request the message being sent
// @param server the server being sent to
// @param sequence the sequence number of the message
// @return -1 if there was an error from the server, 1 otherwise
static int transmit(int sockfd, message **request, host *server, int *sequence) {
    
    hftp_response_message *response;   // Server's response message
    
    // We will poll sockfd for the POLLIN event
    struct pollfd fd = {
        
        .fd = sockfd,
        .events = POLLIN
        
    };
    
    // Re-transmit until a packet is acknowledged
    while (1) {
        
        send_message(sockfd, *request, server);
        
        // Poll the socket for 1 seconds
        int retval = poll(&fd, 1, 1000);
        
        if (retval == 1 && fd.revents == POLLIN) {
            
            // Read the response from the server
            response = (hftp_response_message*)receive_message(sockfd, server);
            response->error_code = ntohl(response->error_code);
            
            syslog(LOG_DEBUG, "Received Ack, Sequence: %d, Error code: %d", response->sequence, response->error_code);
            
            // The correct sequence number is acked and no error
            if (response->sequence == *sequence && response->error_code != 1) {
                
                *sequence = (*sequence + 1) % 2;
                
                free(response);
                free(*request);
                
                response = NULL;
                *request = NULL;
                
                return 1;
                
            } //end if
            
            // There was an error
            else if (response->error_code == 1) {
                
                free(response);
                free(*request);
                
                response = NULL;
                *request = NULL;
                
                return -1;
                
            } //end else if
            
        } //end if
        
    } //end while
    
} //end trasmit_control

// Sends an intialize message to the server
// @param sockfd the socket file descriptor
// @param server the server being sent to
// @param seq the sequence number of the message
// @param file a pointer to the file variable
// @param client a pointer to the client variable
// @param hftpd a pointer to the server information
// @param p a pointer to the filename
// @param count the position in the list of files
static void send_initialize(int sockfd, host *server, int *seq, file_info *file,
                         client_info *client, server_info *hftpd, csv_record *p, int count) {
    
    hftp_control_message *control = (hftp_control_message*)create_control_message(INITIALIZE, *seq, p, client->token, file->file_length);
    
    syslog(LOG_INFO, "Transferring '%s' (%d of %d)", p->filename, count, get_list_length(client->response_list));
    syslog(LOG_DEBUG, "Type: Initialization, Sequence: %d, Filename length: %d", control->sequence, ntohs(control->filename_length));
    
    // Transmit a control request
    if (transmit(sockfd, (message**)&control, server, seq) == -1) {
        
        syslog(LOG_INFO, "Authentication with Hooli file transfer server failed");
        free_file_info(file);
        close(sockfd);
        
        free_server_info(hftpd);
        free_client_info(client);
        
        exit(EXIT_FAILURE);
        
    } //end if
    
} //end send_control

// Sends a termination message to the server
// @param sockfd the socket file descriptor
// @param server the server being sent to
// @param seq the sequence number of the message
// @param p a pointer to the filename
// @param client a pointer to the client variable
static void send_termination(int sockfd, host *server, int *seq, csv_record *p, client_info *client) {
    
    hftp_control_message *control = (hftp_control_message*)create_control_message(TERMINATE, *seq, p, client->token, 0);
    
    syslog(LOG_DEBUG, "Type: Termination, Sequence: %d", control->sequence);
    transmit(sockfd, (message**)&control, server, seq);
    
} //end send_termination

// Sends a data message to the server
// @param sockfd the socket file descriptor
// @param server the server being sent to
// @param seq the sequence number of the message
// @param file a pointer to the file variable
static void send_data(int sockfd, host *server, int *seq, file_info *file) {
    
    char data_buffer[MAX_DATA_LENGTH];  // A data segment
    
    memcpy(data_buffer, file->pos, get_data_length(file));
    hftp_data_message *data = (hftp_data_message*)create_data_message(DATA, *seq, data_buffer, get_data_length(file));
    
    syslog(LOG_DEBUG, "Type: Data, Sequence: %d, Data length: %d", data->sequence, ntohs(data->data_length));
    transmit(sockfd, (message**)&data, server, seq);
    
} //end send data

// Sends the requested files to the Hooli file transfer server
// @param hftpd a pointer to the server information
// @param client a pointer to the client information
// @return -1 if there was an error, 1 otherwise
int hooli_file_sync(server_info *hftpd, client_info *client) {
    
    int sockfd;                         // The socket for communication with the server
    int seq = 0;                        // The sequence number
    int count = 0;
    
    file_info *file;
    host server;                        // Server address
    
    sockfd = create_udp_client_socket(hftpd->server, hftpd->port, &server);
    csv_record *p = client->response_list;
     
    // Loop through all the requested files
    while (p != NULL) {
        
        count++;
        
        file = create_file();
        initialize_file(file, client, p);
        send_initialize(sockfd, &server, &seq, file, client, hftpd, p, count);
    
        // Keep sending data messages until a file is transferred
        while (!file->file_transferred) {
            
            manage_file_buffer(file);
            send_data(sockfd, &server, &seq, file);
            update_progress(file);
            
        } //end while
     
        final_progress_update(file);
        free_file_info(file);
        p = p->next;
     
    } //end while
     
    send_termination(sockfd, &server, &seq, p, client);
     
    // Close the socket
    close(sockfd);
    
    return 1;
    
} //end hooli_file_sync
