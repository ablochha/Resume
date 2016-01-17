/*
 * hmdp_client.c
 *
 * Computer Science 3357a - Fall 2015
 * Author: Andrew Bloch-Hansen
 *
 * Communicates with the Hooli Metadata Server according to the Hooli Metadata Protocol.
 */

#include <stdio.h>
#include <stdlib.h>
#include <syslog.h>
#include <unistd.h>

#include "../common/client_structs.h"
#include "../common/hmdp.h"
#include "../common/linkedlist.h"
#include "hmdp_client.h"
#include "tcp_client.h"

#define STATUS_200 200
#define STATUS_204 204
#define STATUS_302 302
#define STATUS_401 401
#define FILENAMES 0

// Syncs the contents of the client's hooli directory with the Hooli Database
// @param server the name of the server to connect to
// @param port the port number to open a socket on
// @param username the username entered by the user
// @param password the password entered by the user
// @param token the client's token
// @param list the linked list of files and checksums from the hooli directory
// @return 1 if we need to transfer files, -1 otherwise
int hooli_metadata_sync(server_info *hmds, client_info **client) {
    
    int sockfd;                         // Socket descriptor of the opened addressed
    
    char *status = NULL;                // The server's response code
    char *message = NULL;               // The server's response message
    
    int retval = -1;                    // Whether to transfer files or not
    
    // Connect to the server
    syslog(LOG_INFO, "Connecting to server");
    sockfd = create_tcp_client_socket(hmds->server, hmds->port);
    
    // Request authentication
    syslog(LOG_DEBUG, "Sending credentials");
    request_auth(sockfd, (*client)->username, (*client)->password);
    
    // Check if the server used the correct protocol
    if (read_response(sockfd, &(*client)->token, &status, &message, &(*client)->response_list) == 1) {
        
        // Succesfully authenticated, send the list of files
        if (atoi(status) == STATUS_200) {
            
            syslog(LOG_INFO, "Authentication successful");
            syslog(LOG_INFO, "Uploading file list");
            request_list(sockfd, (*client)->token, (*client)->list);
            
            free(status);
            free(message);
            
            status = NULL;
            message = NULL;
            
            // Check if the server used the correct protocol
            if (read_response(sockfd, &(*client)->token, &status, &message, &(*client)->response_list) == 1) {
                
                // Files are requested, print out the list of files
                if (atoi(status) == STATUS_302) {
                    
                    syslog(LOG_INFO, "Server requesting the following files:");
                    to_string_files((*client)->response_list, FILENAMES);
                    retval = 1;
                    
                } //end if
                
                // No files are requested
                else if (atoi(status) == STATUS_204) {
                    
                    syslog(LOG_INFO, "%s", message);
                    
                } //end else if
                
                // Incorrect token was sent to the server
                else if (atoi(status) == STATUS_401) {
                    
                    syslog(LOG_INFO, "%s", message);
                    
                } //end else if
                
                // Recieved an unexpected status code
                else {
                    
                    syslog(LOG_DEBUG, "Expected valid status code, got '%s'", status);
                    
                } //end else
                
            } //end if
            
            // The response from the server did not parse according to the protocol
            else {
                
                syslog(LOG_ERR, "Recieved message failed to follow protocol");
                
            } //end else
            
        } //end if
        
        // Incorrect password was sent to the server
        else if (atoi(status) == STATUS_401) {
            
            syslog(LOG_INFO, "%s", message);
            
        } //end else if
        
        // Received an unexpected status code
        else {
            
            syslog(LOG_DEBUG, "Expected valid status code, got '%s'", status);
            
        } //end else
        
    } //end if
    
    // The response from the server did not parse according to the protocol
    else {
        
        syslog(LOG_ERR, "Recieved message failed to follow protocol");
        
    } //end else
    
    free(status);
    free(message);
    
    // Close the connection
    close(sockfd);
    
    return retval;
    
} //end hooli_metadata_sync
