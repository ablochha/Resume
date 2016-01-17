/*
 * hdb.c
 *
 * Computer Science 3357a - Fall 2015
 * Author: Andrew Bloch-Hansen
 *
 * This is a library for commonly used interactions with the redis database. The
 * functions include connecting or disconnecting to the databse, adding or removing
 * files, and listing the files of a user.
 */

#include <hiredis/hiredis.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <syslog.h>
#include <time.h>

#include "hdb.h"

#define DEFAULT_PORT 6379
#define MINIMUM_TIMEOUT 1
#define MAXIMUM_TIMEOUT 10000
#define PASSWORDS "########"
#define TOKENS "@@@@@@@@"
#define TOKEN_LENGTH 16

// Connect to the specified Hooli database server, returning the initialized
// connection.
// @param server the server to connect to
// @return the connection to the server
hdb_connection* hdb_connect(const char *server) {
    
    redisContext *context;      // The context of the connection to the redis server
    int port = DEFAULT_PORT;    // The port to connect to
    
    struct timeval timeout = {MINIMUM_TIMEOUT, MAXIMUM_TIMEOUT};
    
    context = redisConnectWithTimeout(server, port, timeout);
    
    // Check if the connection was successful
    if (context == NULL || context->err) {
        
        if (context) {
            
            syslog(LOG_ERR, "Connection error: %s\n", context->errstr);
            redisFree(context);
            
        } //end if
        
        else {
            
            syslog(LOG_ERR, "Connection error: can't allocate redis context\n");
            
        } //end else
        
        exit(EXIT_FAILURE);
        
    } //end if

    return *(hdb_connection**)&context;
    
} //end hbd_connect

// Disconnect from the Hooli database server.
// @param con the connection to the server
void hdb_disconnect(hdb_connection *con) {
    
    redisFree((redisContext*)con);
    
} //end hdb_disconnect

// Store a file record in the Hooli database.
// @param con the connection to the server
// @param record the record to be stored in the server
void hdb_store_file(hdb_connection *con, hdb_record *record) {
    
    redisReply *reply = redisCommand((redisContext*)con, "HSET %s %s %s",
                                     record->username, record->filename, record->checksum);
    freeReplyObject(reply);
    
} //end hdb_store_file

// Remove a file record from the Hooli database.
// @param con the connection the the server
// @param username the key to use in the database
// @param filename the field to use in the database
// @return the number of files removed
int hdb_remove_file(hdb_connection *con, const char *username, const char *filename) {

    redisReply *reply = redisCommand((redisContext*)con, "HDEL %s %s", username, filename);
    int del = reply->integer;
    
    freeReplyObject(reply);
    
    return del;
    
} //end hdb_remove_file

// If the specified file is found in the Hooli database, return its checksum.
// Otherwise, return NULL.
// @param con the connection to the server
// @param username the key to use in the database
// @param filename the field to use in the database
// @return the checksum of the field in the database
char* hdb_file_checksum(hdb_connection *con, const char *username, const char *filename) {
    
    // Check if the file exists in the database
    if (hdb_file_exists(con, username, filename)) {
        
        redisReply *reply = redisCommand((redisContext*)con, "HGET %s %s", username, filename);
        char *checksum = strdup(reply->str);
        
        freeReplyObject(reply);
        
        return checksum;
        
    } //end if
    
    // The file doesn't exist
    else {
        
        syslog(LOG_INFO, "File does not exist");
        
        return NULL;
        
    } //end else

} //end hdb_file_checksum

// Get the number of files stored in the Hooli database for the specified user.
// @param con the connection to the server
// @param username the key to use in the database
// @return the number of fields for a key
int hdb_file_count(hdb_connection *con, const char *username) {
    
    redisReply *reply = redisCommand((redisContext*)con, "HLEN %s", username);
    int count = reply->integer;
    
    freeReplyObject(reply);
    
    return count;

} //end hdb_file_count

// Return a Boolean value indicating whether or not the specified user exists
// in the Hooli database
// @param con the connection to the server
// @param username the key to use in the database
// @return true if the user exists, otherwise false
bool hdb_user_exists(hdb_connection *con, const char *username) {
    
    redisReply *reply = redisCommand((redisContext*)con, "EXISTS %s", username);
    
    // Check if the database has an entry with the username
    if ((reply->integer) == 1) {
        
        freeReplyObject(reply);
        
        return true;
        
    } //end if

    freeReplyObject(reply);
    
    return false;
    
} //end hdb_user_exists

// Return a Boolean value indicating whether or not the specified file exists
// in the Hooli database
// @param con the connection to the server
// @param username the key to use in the database
// @param filename the field to use in the database
// @return true if the file exists, othewise false
bool hdb_file_exists(hdb_connection *con, const char *username, const char *filename) {

    redisReply *reply = redisCommand((redisContext*)con, "HEXISTS %s %s", username, filename);
    
    // Check if the database has an entry with the same username and filename
    if ((reply->integer) == 1) {
        
        freeReplyObject(reply);
        
        return true;
        
    } //end if
    
    freeReplyObject(reply);
    
    return false;
    
} //end hdb_file_exists

// Return a linked list of all of the specified user's file records stored in the
// Hooli database
// @param con the connection to the server
// @param username the key to use in the database
// @return a linked list of user files
hdb_record* hdb_user_files(hdb_connection *con, const char *username) {
    
    // Check if there are any files for the user
    if (hdb_file_count(con, username) > 0) {
        
        redisReply *reply;      // The reply from the redis server after a command
        hdb_record *list;       // The head of the linked list
        hdb_record *p;          // The pointer that traverses the linked list
        
        list = malloc(sizeof(struct hdb_record));
        p = list;
        
        reply = redisCommand((redisContext*)con, "HGETALL %s", username);
        
        if (reply->type == REDIS_REPLY_ARRAY) {
            
            // For each entry, put the information into the linked list
            for (int i = 0; i < reply->elements; i++) {
                
                p->username = strdup(username);
                p->filename = strdup(reply->element[i]->str);
                i++;
                p->checksum = strdup(reply->element[i]->str);
                p->next = NULL;
                
                // Trying to find good way to handle what to do with next
                if ((i+1) < reply->elements) {
                    
                    p->next = malloc(sizeof(struct hdb_record));
                    p = p->next;
                    
                } //end if
                
            } //end for
            
        } //end if
        
        freeReplyObject(reply);
        return list;
        
    } //end if
    
    // There are no files for the user
    else {
        
        return NULL;
        
    } //end else

} //end hdb_user_files

// Free the linked list returned by hdb_user_files()
// @param record the linked list of user files
void hdb_free_result(hdb_record *record) {
    
    struct hdb_record *p;           // Pointer that traverses the linked list
    struct hdb_record *temp;        // Temporary record to help free memory
    p = record;
    
    // Loop through and free all of the nodes in the linked list
    while (p != NULL) {
        
        temp = p;
        p = p->next;
        
        free(temp->username);
        free(temp->filename);
        free(temp->checksum);
        
        temp->username = NULL;
        temp->filename = NULL;
        temp->checksum = NULL;
        
        free(temp);
        temp = NULL;
        
    } //end while
    
} //end hdb_free_result

// Delete the specifid user and all of his/her file records from the Hooli
// database.
// @param con the connection to the server
// @param username the key to use in the database
// @return the number of files deleted
int hdb_delete_user(hdb_connection *con, const char *username) {
    
    redisReply *reply = redisCommand((redisContext*)con, "DEL %s", username);
    int count = reply->integer;
    
    freeReplyObject(reply);
    
    return count;
    
} //end hdb_delete_user

// Authenticates a user and provides a secure token
// @param con the connection to the server
// @param username the key to use in the database
// @param password
// @return the number of files deleted
char* hdb_authenticate(hdb_connection* con, const char* username, const char* password) {
    
    // The username is in the database
    if (hdb_file_exists(con, PASSWORDS, username)) {
        
        char *hooli_password = hdb_file_checksum(con, PASSWORDS, username);
        
        // The given password matches the database's
        if (strcmp(hooli_password, password) == 0) {
            
            redisReply *reply_search = redisCommand((redisContext*)con, "HGETALL %s", TOKENS);
            
            // Expecting an array response
            if (reply_search->type == REDIS_REPLY_ARRAY) {
                
                // For each entry, check for our username
                for (int i = 0; i < reply_search->elements; i++) {
                    
                    // We've found a match on the username
                    if (strcmp(username, reply_search->element[i]->str) == 0) {
                        
                        // Search every entry in the TOKENS hash
                        // If we find an entry with the username in it, remove that token
                        hdb_remove_file(con, TOKENS, reply_search->element[i-1]->str);
                        
                    } //end if
                    
                } //end for
                
            } //end if
            
            freeReplyObject(reply_search);

            // Generate a new random token and add it to the TOKENS hash
            char *token = generate_token();
            redisReply *reply = redisCommand((redisContext*)con, "HSET %s %s %s",
                                             TOKENS, token, username);
            
            freeReplyObject(reply);
            free(hooli_password);
            
            return token;
            
        } //end if
        
        // The username exists but the passwords didn't match
        else {
            
            syslog(LOG_INFO, "Incorrect password!");
            free(hooli_password);
            return NULL;
            
        } //end else
        
    } //end if
    
    // The username is not in the database
    else {
        
        redisReply *reply_password = redisCommand((redisContext*)con, "HSET %s %s %s",
                                                  PASSWORDS, username, password);
        freeReplyObject(reply_password);
        
        char *token = generate_token();
        
        redisReply *reply_token = redisCommand((redisContext*)con, "HSET %s %s %s",
                                               TOKENS, token, username);
        freeReplyObject(reply_token);
        
        return token;
        
    } //end else
    
    return NULL;
    
} //end hdb_authenticate

// Verify a token
// @param token the token
// @return the username associated with the token, or NULL if that token is invalid
char* hdb_verify_token(hdb_connection* con, const char* token) {
        
    return hdb_file_checksum(con, TOKENS, token);
    
} //end hdb_verify_token

// Generates a random 16 character alphanumeric token
char* generate_token() {
    
    char charset[] = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    char *token = malloc(TOKEN_LENGTH + 1);
    int i;
    
    srand(time(NULL));
    
    // Generate 16 random characters
    for (i = 0; i < TOKEN_LENGTH; i++) {
        
            int key = rand() % (strlen(charset) - 1);
            token[i] = charset[key];
        
    } //end for
    
    token[TOKEN_LENGTH] = '\0';
    
    return token;
    
} //end generate_token
