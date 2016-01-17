/*
 * linkedlist.c
 *
 * Computer Science 3357a - Fall 2015
 * Author: Andrew Bloch-Hansen
 *
 * Builds a linked list and can print it out as a string or compare
 * it to the Hooli database.
 */

#include <limits.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <syslog.h>
#include <zlib.h>

#include "../hdb/hdb.h"
#include "linkedlist.h"

#define NEWLINE_SIZE 1
#define NEWLINE "\n"
#define FILENAMES 0
#define CHECKSUMS 1

// Adds a file to a linked list
// @param root the root of the linked list
// @param filename the filename of the file
// @param checksum the checksum of the file
void add_file_to_list(csv_record **root, char *filename, char *checksum) {
    
    csv_record *p;  // A pointer to traverse the linked list
    p = (*root);
    
    // Check if the list is empty
    if (p == NULL) {
        
        // Try to allocate memory and assign values to the root node
        p = malloc(sizeof(struct csv_record));
        
        // The allocation failed
        if (p == NULL) {
            
            syslog(LOG_ERR, "Out of memory");
            
        } //end if
            
        // The allocation was successful
        else {
            
            p->filename = filename;
            p->checksum = checksum;
            p->next = NULL;
            (*root) = p;
            
        } //end else
        
    } //end if
    
    // If the list is not empty
    else {
        
        // Go to the end of the list
        while (p->next != NULL) {
            
            p = p->next;
            
        } //end while
        
        // Try to allocate memory and assign values to the next node in the list
        p->next = malloc (sizeof(struct csv_record));
        p = p->next;
        
        // The allocation failed
        if (p == NULL) {
            
            syslog(LOG_ERR, "Out of memory");
            
        } //end if
            
        // The allocation was successful
        else {
            
            p->filename = filename;
            p->checksum = checksum;
            p->next = NULL;
            
        } //end else
        
    } //end else
    
} //end add_file_to_list

// Concatenates the contents of a linked list into a string
// @param list the linked list
// @return the string containing the contents of the linked list
char* to_string(csv_record *list) {
    
    int list_size = get_list_size(list);
    char *list_string = malloc(list_size + 2);
    
    list_string[0] = '\0';
    csv_record *p;
    p = list;
    
    // Concatenate all the nodes's contents together
    while (p != NULL) {
        
        strcat(list_string, p->filename);
        strcat(list_string, NEWLINE);
        strcat(list_string, p->checksum);
        strcat(list_string, NEWLINE);
        p = p->next;
        
    } //end while
   
    list_string[list_size] = '\0';
    
    return list_string;
    
} //end to_string

// Prints a list of the filenames in the linked list
// @param list the linked list
// @param option whether or not to print out checksums
void to_string_files(csv_record *list, int option) {
    
    csv_record *p;
    p = list;
    
    // Visit all the nodes in the linked list
    while (p != NULL) {
        
        // Prints out the filenames
        if (option == FILENAMES) {
            
            syslog(LOG_INFO, "* %s", p->filename);
            
        } //end if
        
        // Prints out the filenames and checksums
        else if (option == CHECKSUMS) {
            
            syslog(LOG_DEBUG, "* %s (%s)", p->filename, p->checksum);
            
        } //end else if
        
        p = p->next;
        
    } //end while
    
} //end to_string_files

// Returns the size of the body in bytes
// @param list the linked list containing the body
// @return the size of the body
int get_list_size(csv_record *list) {
    
    int size = 0;           // The size of the body
    
    csv_record *p = list;   // Pointer to traverse the linked list
    
    // Visit all the nodes in the linked list
    while (p != NULL) {
        
        size += strlen(p->filename) + NEWLINE_SIZE;
        size += strlen(p->checksum) + NEWLINE_SIZE;
        p = p->next;
        
    } //end while
    
    return size - 1;
    
} //end get_list_size

// Gets the total number of nodes in the linked list
// @param list a pointer to the first node in the list
// @return the number of nodes in the list
int get_list_length(csv_record *list) {
    
    int length = 0;
    
    csv_record *p = list;
    
    // Visit all the nodes in the linked list
    while (p != NULL) {
        
        length++;
        p = p->next;
        
    } //end while
    
    return length;
    
} //end get_list_length

// Returns a linked list containing the files and checksums that the Hooli database doesn't have
// @param response_list the list of files and checksums that the Hooli database doesn't have
// @param request_list the list of files and checksums that the client sent
// @param database_list the list of files and checksums that the Hooli database has
void get_updated_files(csv_record **response_list, csv_record *request_list,
                       hdb_record *database_list) {
    
    csv_record *request_p = request_list;       // The request list sent from the client
    hdb_record *database_p = database_list;     // The database list sent from the Hooli databse
    
    bool updated = false;                       // Whether or not the file is up to date
    
    // Loop through the request files
    while (request_p != NULL) {
        
        // Loop through the database files while we haven't found the request file
        while (database_p != NULL && updated == false) {
            
            // If our request file matches the database file, we don't need to upload it
            if ((strcmp(request_p->filename, database_p->filename) == 0)
                && (strcmp(request_p->checksum, database_p->checksum) == 0)) {
                
                updated = true;
                
            } //end if
            
            database_p = database_p->next;
            
        } //end while
    
        // Any files not found in the database will be requested from the client
        if (!updated) {
            
            add_file_to_list(response_list, strdup(request_p->filename),
                             strdup(request_p->checksum));
            
        } //end if
        
        updated = false;
        database_p = database_list;
        request_p = request_p->next;
        
    } //end while
    
} //end get_updated_files

// Free the linked list
// @param record the linked list of files
void csv_free_result(csv_record *record) {
    
    csv_record *p;           // Pointer that traverses the linked list
    csv_record *temp;        // Temporary record to help free memory
    p = record;
    temp = p;
    
    // Loop through and free all of the nodes in the linked list
    while (p != NULL) {
        
        temp = p;
        p = p->next;
        
        free(temp->filename);
        free(temp->checksum);
        
        temp->filename = NULL;
        temp->checksum = NULL;
        
        free(temp);
        temp = NULL;
        
    } //end while
    
} //end hdb_free_result
