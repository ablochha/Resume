/*
 * hmdp_server.c
 *
 * Computer Science 3357a - Fall 2015
 * Author: Andrew Bloch-Hansen
 *
 * Handle's a client's connection according to the Hooli Metadata Protocol.
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <syslog.h>

#include "hmdp_server.h"
#include "../common/client_structs.h"
#include "../common/hmdp.h"
#include "../common/linkedlist.h"
#include "../hdb/hdb.h"

#define FILENAMES 0
#define CHECKSUMS 1
#define COMMAND_AUTH "Auth"
#define COMMAND_LIST "List"

// Tries to authenticate a client by checking them against the Hooli database
// @param connectionfd the socket descriptor of the client
// @param server the database server
// @param command the command of the request
// @param username the client's username
// @param password the client's password
// @return 1 if the client was authenticated, -1 otherwise
static int handle_auth(int connectionfd, char *server, char **command,
                       char **username, char **password) {
    
    syslog(LOG_INFO, "Authenticating user '%s'", *username);
    
    // Connect to the Hooli database
    hdb_connection *con = hdb_connect(server);
    char *hooli_token = hdb_authenticate(con, *username, *password);
    hdb_disconnect(con);
    
    // The client was authenticated successfully
    if (hooli_token != NULL) {
        
        syslog(LOG_INFO, "Authentication successful");
        response_200(connectionfd, hooli_token);
        
        free(*command);
        free(*username);
        free(*password);
        free(hooli_token);
        
        *command = NULL;
        *username = NULL;
        *password = NULL;
        hooli_token = NULL;
        
        return 1;
        
    } //end if
    
    // The client was not authenticated successfully
    else {
        
        syslog(LOG_INFO, "Unauthorized");
        response_401(connectionfd);
        
        free(*command);
        free(*username);
        free(*password);
        
        *command = NULL;
        *username = NULL;
        *password = NULL;
        
        return -1;
        
    } //end else
    
} //end handle_auth

// Tries to tell the client which files to upload to the Hooli database
// @param connectionfd the socket descriptor of the client
// @param server the database server
// @param the command of the request
// @param token the client's token
// @param response_list the list of files to request from the client
// @param request_list the list of files received from the client
// @param database_list the list of files stores in the Hooli database
// @return -1 to terminate the connection with the client
static int handle_list(int connectionfd, char *server, char **command,
                       char **token, csv_record **response_list,
                       csv_record *request_list) {
    
    syslog(LOG_INFO, "Receiving file list");
    to_string_files(request_list, CHECKSUMS);
    
    // Connect to the Hooli database
    hdb_connection *con = hdb_connect(server);
    char *hooli_username = hdb_verify_token(con, *token);
    hdb_disconnect(con);
    
    // The client gave a valid token
    if (hooli_username != NULL) {
        
        // Connect to the Hooli database
        hdb_connection *con = hdb_connect(server);
        hdb_record *database_list = hdb_user_files(con, hooli_username);
        hdb_disconnect(con);
        
        get_updated_files(response_list, request_list, database_list);
        
        // The client needs to sync some files
        if (*response_list != NULL) {
            
            syslog(LOG_INFO, "Requesting files:");
            to_string_files(*response_list, FILENAMES);
            response_302(connectionfd, *response_list);
            
            free(*command);
            free(hooli_username);
            free(*token);
            
            *command = NULL;
            hooli_username = NULL;
            *token = NULL;
            
            csv_free_result(request_list);
            csv_free_result(*response_list);
            hdb_free_result(database_list);
            
            return -1;
            
        } //end if
        
        // The client does not need to sync any files
        else {
            
            syslog(LOG_INFO, "No files requested");
            response_204(connectionfd);
            
            free(*command);
            free(hooli_username);
            free(*token);
            
            *command = NULL;
            hooli_username = NULL;
            *token = NULL;
            
            csv_free_result(request_list);
            hdb_free_result(database_list);
            
            return -1;
            
        } //end else
        
    } //end if
    
    // The client gave an invalid token
    else {
        
        syslog(LOG_INFO, "Unauthorized");
        response_401(connectionfd);
        
        free(*command);
        free(*token);
        
        *command = NULL;
        *token = NULL;
        
        csv_free_result(request_list);
        
        return -1;
        
    } //end else
    
} //end handle_list

// Handle's a client's request
// @param connectionfd the descriptor for the connection
// @param server the database server
// @param command the type of request
// @param client the client's information
// @return 1 if the server should handle another request, -1 otherwise
int handle_request(int connectionfd, char *server, char **command, client_info **client) {
    
    // Check if the client used the correct protocol
    if (read_request(connectionfd, &(*client)->username, &(*client)->password, &(*client)->token, &(*client)->list, command) == 1) {
        
        // The client sent an 'Auth' request
        if (strcmp(*command, COMMAND_AUTH) == 0) {
            
            // Attempt to authenticate the client with the Hooli database
            if (handle_auth(connectionfd, server, command, &(*client)->username, &(*client)->password) == -1) {
                
                return -1;
                
            } //end if
            
        } //end if
        
        // The client sent a 'List' request
        else if (strcmp(*command, COMMAND_LIST) == 0) {
            
            // Attempt to sync the client with the Hooli database
            if (handle_list(connectionfd, server, command, &(*client)->token,
                            &(*client)->response_list, (*client)->list) == -1) {
                
                return -1;
                
            } //end if
            
        } //end else if
        
        // The client gave an invalid command
        else {
            
            syslog(LOG_DEBUG, "Expected valid command, got '%s'", *command);
            response_401(connectionfd);
            return -1;
            
        } //end else
        
    } //end if
    
    // The client's message did not follow the protocol
    else {
        
        syslog(LOG_DEBUG, "Received message failed to follow protocol");
        response_401(connectionfd);
        return -1;
        
    } //end else
    
    return 1;
    
} //end handle_request
