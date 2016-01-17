/*
 * hmdpbuilder.c
 *
 * Computer Science 3357a - Fall 2015
 * Author: Andrew Bloch-Hansen
 *
 * Concatenates and seperates strings into the blocks 'Command',
 * 'Key', 'Value', 'Body', 'Status', and 'Message'.
 */

#include <err.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <syslog.h>

#include "linkedlist.h"
#include "hmdpbuilder.h"

#define NEWLINE_SIZE 1
#define NULL_BYTE_SIZE 1
#define COLON_SIZE 1
#define SPACE_SIZE 1
#define COLON ":"
#define NEWLINE "\n"
#define COLON_OR_NEWLINE ":\n"
#define NULL_TERMINATOR "\0"
#define SPACE " "

// Adds a command to a HMDP message
// @param buffer the message so far
// @param command the command to be added
void add_command(char **buffer, char *command) {
    
    *buffer = malloc(strlen(command) + NEWLINE_SIZE + NULL_BYTE_SIZE);
    
    strcpy(*buffer, command);
    strcat(*buffer, NEWLINE);
    
} //end add_command

// Reads a command from a HMDP message
// @param buffer the remaining message
// @return the command from the message
char* read_command(char **buffer) {
    
    return strsep(buffer, NEWLINE);
    
} //end read_command

// Adds a key to a HMDP message
// @param buffer the message so far
// @param key the key to be added
void add_key(char **buffer, char *key) {
    
    char *previous = malloc(strlen(*buffer) + NULL_BYTE_SIZE);
    int newsize = strlen(*buffer) + strlen(key) + COLON_SIZE;
    
    strcpy(previous, *buffer);
    *buffer = realloc(*buffer, newsize + COLON_SIZE + NULL_BYTE_SIZE);
    
    strcpy(*buffer, previous);
    strcat(*buffer, key);
    strcat(*buffer, COLON);
    
    free(previous);
    previous = NULL;
    
} //end add_key

// Reads a key from a HMDP message
// @param buffer the remaining message
// @return the key from the message
char* read_key(char **buffer) {
    
    return strsep(buffer, COLON_OR_NEWLINE);
    
} //end read_key

// Adds a value to a HMDP message
// @param buffer the message so far
// @param value the value to be added
void add_value(char **buffer, char *value) {
    
    char *previous = malloc(strlen(*buffer) + NULL_BYTE_SIZE);
    int newsize = strlen(*buffer) + strlen(value) + NEWLINE_SIZE;
    
    strcpy(previous, *buffer);
    *buffer = realloc(*buffer, newsize + NEWLINE_SIZE + NULL_BYTE_SIZE);
    
    strcpy(*buffer, previous);
    strcat(*buffer, value);
    strcat(*buffer, NEWLINE);
    
    free(previous);
    previous = NULL;
    
} //end add_value

// Reads a value from a HMDP message
// @param buffer the remaining message
// @return the value from the message
char* read_value(char **buffer) {
    
    return strsep(buffer, NEWLINE);
    
} //end read_value

// Adds an extra newline to a HMDP message
// @param buffer the message so far
void delimit_header(char **buffer) {
    
    char *previous = malloc(strlen(*buffer) + NULL_BYTE_SIZE);
    int newsize = strlen(*buffer) + NEWLINE_SIZE;
    
    strcpy(previous, *buffer);
    *buffer = realloc(*buffer, newsize + NEWLINE_SIZE + NULL_BYTE_SIZE);
    
    strcpy(*buffer, previous);
    strcat(*buffer, NEWLINE);
    
    free(previous);
    previous = NULL;
    
} //end delimit_header

// Adds a body to a HMDP message
// @param buffer the message so far
// @param list the linked list to be added
void add_body(char **buffer, csv_record *list) {
    
    char *previous = malloc(strlen(*buffer) + NULL_BYTE_SIZE);
    char *list_string = to_string(list);
    int newsize = strlen(*buffer) + get_list_size(list);
    
    strcpy(previous, *buffer);
    *buffer = realloc(*buffer, newsize + NULL_BYTE_SIZE);
    
    strcpy(*buffer, previous);
    strcat(*buffer, list_string);
    
    free(list_string);
    free(previous);
    
    list_string = NULL;
    previous = NULL;
    
} //end add_body

// Reads a body from a HMDP message
// @param buffer the remaining buffer
// @return a linked list containing the body of the message
csv_record* read_body(char **buffer) {
    
    csv_record *list = NULL;    // Root of the linked list
    
    char *filename;             // Filename to be read
    char *checksum;             // Checksum to be read
    
    // Continously read more of the message
    while (1) {
        
        filename = strsep(buffer, "\n\0");
        
        // Stop reading if we run out of information
        if (filename == NULL) {
            
            break;
            
        } //end if
        
        checksum = strsep(buffer, "\n\0");

        // Stop reading if we run out of information
        if (checksum == NULL) {
            
            break;
            
        } //end if
        
        add_file_to_list(&list, strdup(filename), strdup(checksum));
        
    } //end while
    
    return list;
    
} //end read_body

// Adds a status to a HMDP message
// @param buffer the message so far
// @param status the status to be added
void add_status(char **buffer, char *status) {
    
    *buffer = malloc(strlen(status) + SPACE_SIZE + NULL_BYTE_SIZE);
    
    strcpy(*buffer, status);
    strcat(*buffer, SPACE);

} //end add_status

// Reads a status from a HMDP message
// @param buffer the remaining message
// @return the status from the message
char* read_status(char **buffer) {
    
    return strsep(buffer, SPACE);
    
} //end read_status

// Adds a message to a HMDP message
// @param buffer the message so far
// @message the message to be added
void add_message(char **buffer, char *message) {
    
    char *previous = malloc(strlen(*buffer) + NULL_BYTE_SIZE);
    int newsize = strlen(*buffer) + strlen(message) + NEWLINE_SIZE;
    
    strcpy(previous, *buffer);
    *buffer = realloc(*buffer, newsize + NEWLINE_SIZE + NULL_BYTE_SIZE);
    
    strcpy(*buffer, previous);
    strcat(*buffer, message);
    strcat(*buffer, NEWLINE);
    
    free(previous);
    previous = NULL;
    
} //end add_message

// Reads a message from a HMDP message
// @param buffer the remaining message
// @return the message from the message
char* read_message(char **buffer) {
    
    return strsep(buffer, NEWLINE);
    
} //end read_messsage
