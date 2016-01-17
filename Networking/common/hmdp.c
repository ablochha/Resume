/*
 * hmdp.c
 *
 * Computer Science 3357a - Fall 2015
 * Author: Andrew Bloch-Hansen
 *
 * Builds and reads network messages according to the Hooli Metadata
 * Protocol.
 */

#include <err.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <syslog.h>
#include <unistd.h>

#include "hmdp.h"
#include "hmdpbuilder.h"
#include "tcp_sockets.h"

#define MAX_BUFFER_SIZE 131072
#define COMMAND_AUTH "Auth"
#define COMMAND_LIST "List"
#define KEY_USERNAME "Username"
#define KEY_PASSWORD "Password"
#define KEY_TOKEN "Token"
#define KEY_LENGTH "Length"
#define STATUS_200 "200"
#define STATUS_204 "204"
#define STATUS_302 "302"
#define STATUS_401 "401"
#define MESSAGE_200 "Authentication successful"
#define MESSAGE_204 "No files requested"
#define MESSAGE_302 "Files requested"
#define MESSAGE_401 "Unauthorized"

// Sends the HMDP request 'Auth'
// @param sockfd the socket descriptor of the server
// @param username the username of the client
// @param password the password of the client
void request_auth(int sockfd, char *username, char *password) {
    
    char *request;                          // The 'Auth' message
    
    int length;                             // The length of the 'Auth' message
    
    add_command(&request, COMMAND_AUTH);    //  |-------------------|
    add_key(&request, KEY_USERNAME);        //  |Auth\n             |
    add_value(&request, username);          //  |-------------------|
    add_key(&request, KEY_PASSWORD);        //  |Username:value\n   |
    add_value(&request, password);          //  |-------------------|
    delimit_header(&request);               //  |Password:value\n\n |
                                            //  |-------------------|
    length = strlen(request);
    
    // Check to see if the message sent successfully
    if (send_all(sockfd, request, &length) == -1) {
        
        syslog(LOG_ERR, "Only sent %d bytes\n", length);
        
    } //end if
    
    free(request);
    request = NULL;
    
} //end request_auth

// Sends the HMDP request 'List'
// @param sockfd the socket descriptor of the server
// @param token the token used for verification
// @param list the list of files found in the clients hooli directory
void request_list(int sockfd, char *token, csv_record *list) {
    
    char *request;                          // The 'List' message
    char *list_size;                        // The size of the list of files and checksums
    
    int length;                             // The length of the 'List' message
    
    asprintf(&list_size, "%d", get_list_size(list));
    
    add_command(&request, COMMAND_LIST);    //  |-----------------|
    add_key(&request, KEY_TOKEN);           //  |List\n           |
    add_value(&request, token);             //  |-----------------|
    add_key(&request, KEY_LENGTH);          //  |Token:value\n    |
    add_value(&request, list_size);         //  |-----------------|
    delimit_header(&request);               //  |Length:value\n\n |
    add_body(&request, list);               //  |-----------------|
                                            //  |Body:            |
                                            //  |-----------------|
    length = strlen(request);
    
    // Check to see if the message sent successfully
    if (send_all(sockfd, request, &length) == -1) {
        
        syslog(LOG_ERR, "Only sent %d bytes\n", length);
        
    } //end if
    
    free(list_size);
    free(request);
    
    list_size = NULL;
    request = NULL;
    
} //end request_list

// Sends the HMDP response '200'
// @param connectionfd the socket descriptor of the client
// @param token the verification token for the client
void response_200(int connectionfd, char *token) {
    
    char *response;                         // The '200' message
    
    int length;                             // The length of the message
    
    add_status(&response, STATUS_200);      //  |--------------------------------|
    add_message(&response, MESSAGE_200);    //  |200 Authentication Successful\n |
    add_key(&response, KEY_TOKEN);          //  |--------------------------------|
    add_value(&response, token);            //  |Token:value\n\n                 |
    delimit_header(&response);              //  |--------------------------------|
    
    length = strlen(response);
    
    // Check to see if the message sent successfully
    if (send_all(connectionfd, response, &length) == -1) {
        
        syslog(LOG_ERR, "Only sent %d bytes\n", length);
        
    } //end if
    
    free(response);
    response = NULL;
    
} //end response_200

// Sends the HMDP response '204'
// @param connectionfd the socket descriptor of the client
void response_204(int connectionfd) {
    
    char *response;                         // The '204' message
    
    int length;                             // The length of the message
    
    add_status(&response, STATUS_204);      //  |---------------------------|
    add_message(&response, MESSAGE_204);    //  |204 No files requested\n\n |
    delimit_header(&response);              //  |---------------------------|
    
    length = strlen(response);
    
    // Check to see if the message sent successfully
    if (send_all(connectionfd, response, &length) == -1) {
        
        syslog(LOG_ERR, "Only sent %d bytes\n", length);
        
    } //end if
    
    free(response);
    response = NULL;
    
} //end response_204

// Sends the HMDP response '302'
// @param connectionfd the socket descriptor of the client
// @param response_list the list of files for the client to upload
void response_302(int connectionfd, csv_record *response_list) {
    
    char *response;                         // The '302' message
    char *list_size;                        // The size of the body of the message
    
    int length;                             // The length of the message
    
    asprintf(&list_size, "%d", get_list_size(response_list));
    
    add_status(&response, STATUS_302);      //  |----------------------|
    add_message(&response, MESSAGE_302);    //  |302 Files requested\n |
    add_key(&response, KEY_LENGTH);         //  |----------------------|
    add_value(&response, list_size);        //  |Length:value\n\n      |
    delimit_header(&response);              //  |----------------------|
    add_body(&response, response_list);     //  |Body:                 |
                                            //  |----------------------|
    length = strlen(response);
    
    // Check to see if the message sent successfully
    if (send_all(connectionfd, response, &length) == -1) {
        
        syslog(LOG_ERR, "Only sent %d bytes\n", length);
        
    } //end if
    
    free(list_size);
    free(response);
    
    list_size = NULL;
    response = NULL;
    
} //end response_302

// Sends the HMDP response '401'
// @param connectionfd the socket descriptor of the client
void response_401(int connectionfd) {
    
    char *response;                         // The '401' message
    
    int length;                             // The length of the message
    
    add_status(&response, STATUS_401);      //  |---------------------|
    add_message(&response, MESSAGE_401);    //  |401 Unauthorized\n\n |
    delimit_header(&response);              //  |---------------------|
    
    length = strlen(response);
    
    // Check to see if the message sent successfully
    if (send_all(connectionfd, response, &length) == -1) {
        
        syslog(LOG_ERR, "Only sent %d bytes\n", length);
        
    } //end if
    
    free(response);
    response = NULL;
    
} //end response_401

// Parses an 'Auth' request
// @param reference a pointer to the beginning of the message
// @param request a pointer to the current location in parsing
// @param command the command of the message
// @param username the client's username
// @param password the client's password
// @return 1 if the parse was succesful, -1 otherwise
static int parse_auth(char **reference, char **request, char **command,
                      char **username, char **password) {
    
    char *key;  // The key of the header
    
    key = read_key(request);
    
    // Store the username
    if (strcmp(key, KEY_USERNAME) == 0) {
        
        *username = strdup(read_value(request));
        
    } //end if
    
    // The 'Username' key is missing
    else {
        
        syslog(LOG_DEBUG, "Expected key 'Username', got '%s'. Invalid protocol.", key);
        
        free(*command);
        free(*reference);
        
        *command = NULL;
        *reference = NULL;
        
        return -1;
        
    } //end else
    
    key = read_key(request);
    
    // Store the password
    if (strcmp(key, KEY_PASSWORD) == 0) {
        
        *password = strdup(read_value(request));
        
    } //end if
    
    // The 'Password' key is missing
    else {
        
        syslog(LOG_DEBUG, "Expected key 'Password', got '%s'. Invalid protocol.", key);
        
        free(*command);
        free(*username);
        free(*reference);
        
        *command = NULL;
        *username = NULL;
        *reference = NULL;
        
        return -1;
        
    } //end else
    
    key = read_key(request);
    
    // Expecting an empty string between two newlines
    if (strcmp(key, "") == 0) {
        
        free(*reference);
        *reference = NULL;
        
        return 1;
        
    } //end if
    
    // Found something extra
    else {
        
        syslog(LOG_DEBUG, "Expected empty key, got '%s'. Invalid protocol.", key);
        
        free(*command);
        free(*username);
        free(*password);
        free(*reference);
        
        *command = NULL;
        *username = NULL;
        *password = NULL;
        *reference = NULL;
        
        return -1;
        
    } //end else
    
} //end parse_auth

// Parses an 'List' request
// @param reference a pointer to the beginning of the message
// @param request a pointer to the current location in parsing
// @param command the command of the message
// @param token the client's token
// @return 1 if the parse was succesful, -1 otherwise
static int parse_list(char **reference, char **request, char **command, char **token,
                      csv_record **request_list) {
    
    char* key;      // The key of the header
    char *length;   // The length of the body
    
    key = read_key(request);
    
    // Store the token
    if (strcmp(key, KEY_TOKEN) == 0) {
        
        *token = strdup(read_value(request));
        
    } //end if
    
    // The 'Token' key is missing
    else {
        
        syslog(LOG_DEBUG, "Expected key 'Token', got '%s'. Invalid protocol.", key);
        
        free(*command);
        free(*reference);
        
        *command = NULL;
        *reference = NULL;
        
        return -1;
        
    } //end else
    
    key = read_key(request);
    
    // Store the length
    if (strcmp(key, KEY_LENGTH) == 0) {
        
        length = strdup(read_value(request));
        
    } //end if
    
    // The 'Length' key is missing
    else {
        
        syslog(LOG_DEBUG, "Expected key 'Length', got '%s'. Invalid protocol.", key);
        
        free(*command);
        free(*token);
        free(*reference);
        
        *command = NULL;
        *token = NULL;
        *reference = NULL;
        
        return -1;
        
    } //end else
    
    key = read_key(request);
    
    // Expecting an empty string between two newlines
    if (strcmp(key, "") == 0) {
        
        *request_list = read_body(request);
        
        // Completed parsing the message
        if (*request_list != NULL) {
            
            free(length);
            free(*reference);
            
            length = NULL;
            *reference = NULL;
            
            return 1;
            
        } //end if
        
        // The body of the request is missing
        else {
            
            syslog(LOG_DEBUG, "Expected body of length '%s', got '%s'. Invalid protocol.",
                   length, *request);
            
            free(*command);
            free(*token);
            free(length);
            free(*reference);
            
            *command = NULL;
            *token = NULL;
            length = NULL;
            *reference = NULL;
            
            return -1;
            
        } //end else
        
    } //end if
    
    // Found something extra
    else {
        
        syslog(LOG_DEBUG, "Expected empty key, got '%s'. Invalid protocol.", key);
        
        free(*command);
        free(*token);
        free(length);
        free(*reference);
        
        *command = NULL;
        *token = NULL;
        length = NULL;
        *reference = NULL;
        
        return -1;
        
    } //end else
    
} //end parse_list

// Parses an '200' response
// @param reference a pointer to the beginning of the message
// @param response a pointer to the current location in parsing
// @param status the status of the message
// @param message the message of the message
// @param token the client's token
// @return 1 if the parse was succesful, -1 otherwise
static int parse_200(char **reference, char **response, char **status,
                     char **message, char **token) {
    
    char* key;  // The key of the header
    
    *message = strdup(read_message(response));
    
    key = read_key(response);
    
    // Store the token
    if (strcmp(key, KEY_TOKEN) == 0) {
        
        *token = strdup(read_value(response));
        
    } //end if
    
    // The 'Token' key is missing
    else {
        
        syslog(LOG_DEBUG, "Expected key 'Token', got '%s'. Invalid protocol.", key);
        
        free(*status);
        free(*message);
        free(*reference);
        
        *status = NULL;
        *message = NULL;
        *reference = NULL;
        
        return -1;
        
    } //end else
    
    key = read_key(response);
    
    // Expecting an empty string between two newlines
    if (strcmp(key, "") == 0) {
        
        free(*reference);
        *reference = NULL;
        
        return 1;
        
    } //end if
    
    // Found something extra
    else {
        
        syslog(LOG_DEBUG, "Expected empty key, got '%s'. Invalid protocol.", key);
        
        free(*status);
        free(*message);
        free(*token);
        free(*reference);
        
        *status = NULL;
        *message = NULL;
        *token = NULL;
        *reference = NULL;
        
        return -1;
        
    } //end else
    
} //end parse_200

// Parses an '204' response
// @param reference a pointer to the beginning of the message
// @param response a pointer to the current location in parsing
// @param status the status of the message
// @param message the message of the message
// @return 1 if the parse was succesful, -1 otherwise
static int parse_204(char **reference, char **response, char **status, char **message) {
    
    char* key;  // The key of the header
    
    *message = strdup(read_message(response));
    
    key = read_key(response);
    
    // Expecting an empty string between two newlines
    if (strcmp(key, "") == 0) {
        
        free(*reference);
        *reference = NULL;
        
        return 1;
        
    } //end if
    
    // Found something extra
    else {
        
        syslog(LOG_DEBUG, "Expected empty key, got '%s'. Invalid protocol.", key);
        
        free(*status);
        free(*message);
        free(*reference);
        
        *status = NULL;
        *message = NULL;
        *reference = NULL;
        
        return -1;
        
    } //end else
    
} //end parse_204

// Parses an '302' response
// @param reference a pointer to the beginning of the message
// @param response a pointer to the current location in parsing
// @param status the status of the message
// @param message the message of the message
// @return 1 if the parse was succesful, -1 otherwise
static int parse_302(char **reference, char **response, char **status,
                     char **message, csv_record **response_list) {
    
    char* key;      // The key of the header
    char *length;   // The length of the body of the response
    
    *message = strdup(read_message(response));
    
    key = read_key(response);
    
    // Store the length
    if (strcmp(key, KEY_LENGTH) == 0) {
        
        length = strdup(read_value(response));
        
    } //end if
    
    // The 'Length' key is missing
    else {
        
        syslog(LOG_DEBUG, "Expected key 'Length', got '%s'. Invalid protocol.", key);
        
        free(*status);
        free(*message);
        free(*reference);
        
        *status = NULL;
        *message = NULL;
        *reference = NULL;
        
        return -1;
        
    } //end else
    
    key = read_key(response);
    
    // Expecting an empty string between two newlines
    if (strcmp(key, "") == 0) {
        
        *response_list = read_body(response);
        
        // Completed parsing the message
        if (*response_list != NULL) {
            
            free(length);
            free(*reference);
            
            length = NULL;
            *reference = NULL;
            
            return 1;
            
        } //end if
        
        // Completed parsing the message (Length listed as 0)
        else if (*response_list == NULL && atoi(length) == 0) {
            
            free(length);
            free(*reference);
            
            length = NULL;
            *reference = NULL;
            
            return 1;
            
        } //end else if
        
        // The body of the response is missing
        else {
            
            syslog(LOG_DEBUG, "Expected body of length '%s', got '%s'. Invalid protocol.",
                   length, *response);
            
            free(*status);
            free(*message);
            free(length);
            free(*reference);
            
            *status = NULL;
            *message = NULL;
            length = NULL;
            *reference = NULL;
            
            return -1;
            
        } //end else
        
    } //end if
    
    // Found something extra
    else {
        
        syslog(LOG_DEBUG, "Expected empty key, got '%s'. Invalid protocol.", key);
        
        free(*status);
        free(*message);
        free(length);
        free(*reference);
        
        *status = NULL;
        *message = NULL;
        length = NULL;
        *reference = NULL;
        
        return -1;
        
    } //end else
    
} //end parse_302

// Parses an '401' response
// @param reference a pointer to the beginning of the message
// @param response a pointer to the current location in parsing
// @param status the status of the message
// @param message the message of the message
// @return 1 if the parse was succesful, -1 otherwise
static int parse_401(char **reference, char **response, char **status, char **message) {
    
    char* key;  // The key of the header
    
    *message = strdup(read_message(response));
    
    key = read_key(response);
    
    // Expecting an empty string between two newlines
    if (strcmp(key, "") == 0) {
        
        free(*reference);
        *reference = NULL;
        
        return 1;
        
    } //end if
    
    // Found something extra
    else {
        
        syslog(LOG_DEBUG, "Expected empty key, got '%s'. Invalid protocol.", key);
        
        free(*status);
        free(*message);
        free(*reference);
        
        *status = NULL;
        *message = NULL;
        *reference = NULL;
        
        return -1;
        
    } //end else
    
} //end parse_401

// Reads a HMDP request from a client
// @param connectionfd the socket descriptor of the client
// @param username the client's username
// @param password the client's password
// @param token the client's token
// @param request_list the client's list of files to be synced
// @param command the type of HMDP request being made
// @return 1 if the read was successful, otherwise -1
int read_request(int connectionfd, char **username, char **password, char** token,
                 csv_record **request_list, char **command) {
    
    char buffer[MAX_BUFFER_SIZE];  // The buffer used during recv
    char *request;      // The buffer to hold received messages (Will be altered while reading)
    char *reference;    // The unaltered pointer to the message memory
    
    int bytes_read = 0; // The number of bytes read from the request
    
    // Check to see if the message was completely read
    if (receive_all(connectionfd, buffer, &bytes_read) == -1) {
        
        syslog(LOG_ERR, "Only read %d bytes\n", bytes_read);
        return -1;
        
    } //end if
    
    // Keep a pointer to the memory location so we can free it later
    request = strdup(buffer);
    reference = request;
    
    *command = strdup(read_command(&request));
    
    // Parse an 'Auth' request
    if (strcmp(*command, COMMAND_AUTH) == 0) {
        
        return parse_auth(&reference, &request, command, username, password);
        
    } //end if
    
    // Parse a 'List' request
    else if (strcmp(*command, COMMAND_LIST) == 0) {
        
        return parse_list(&reference, &request, command, token, request_list);
        
    } //end else if
    
    // Found an unrecognized command
    else {
        
        syslog(LOG_ERR, "Incorrect command specified");
        
        free(*command);
        free(reference);
        
        *command = NULL;
        reference = NULL;
        
        return -1;
        
    } //end else
    
} //end read_request

// Read a HMDP response from a server
// @param sockfd the socket descriptor for the server
// @param token a valid verification token for the client
// @param status the status code of the response from the server
// @param message the message of the response from the server
// @param response_list the list of files the server wants uploaded
// @return 1 if the read was successful, -1 otherwise
int read_response(int sockfd, char **token, char **status, char **message,
                  csv_record **response_list) {
    
    char buffer[MAX_BUFFER_SIZE];  // The buffer used during recv
    char *response;     // The buffer to hold received messages (Will be altered while reading)
    char *reference;    // The unaltered pointer to the message memory
    
    int bytes_read = 0; // The number of bytes read from the response
    
    // Check to see if the message was completely read
    if (receive_all(sockfd, buffer, &bytes_read) == -1) {
        
        syslog(LOG_ERR, "Only read %d bytes\n", bytes_read);
        return -1;
        
    } //end if
    
    // Keep a pointer to the memory location so we can free it later
    response = strdup(buffer);
    reference = response;
    
    *status = strdup(read_status(&response));
    
    // Parse a 200 response
    if (strcmp(*status, STATUS_200) == 0) {
        
        return parse_200(&reference, &response, status, message, token);
        
    } //end if
    
    // Parse a 204 response
    else if (strcmp(*status, STATUS_204) == 0) {
        
        return parse_204(&reference, &response, status, message);
        
    } //end else if
    
    // Parse a 302 response
    else if (strcmp(*status, STATUS_302) == 0) {
        
        return parse_302(&reference, &response, status, message, response_list);
        
    } //end else if
    
    // Parse a 401 response
    else if (strcmp(*status, STATUS_401) == 0) {
        
        return parse_401(&reference, &response, status, message);
        
    } //end else if
    
    // Found an unrecognized status code
    else {
        
        syslog(LOG_ERR, "Invalid status code");
        
        free(*status);
        free(reference);
        
        *status = NULL;
        reference = NULL;
        
        return -1;
        
    } //end else
    
} //end read_response
