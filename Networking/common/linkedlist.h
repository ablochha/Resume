#ifndef LINKEDLIST_H
#define LINKEDLIST_H

#include "../hdb/hdb.h"

// A record stored containing the clients files
typedef struct csv_record {
    char *filename;
    char *checksum;
    struct csv_record *next;
} csv_record;

// Adds a filename with a checksum to a linked list
void add_file_to_list(csv_record **root, char *filename, char *checksum);

// Concatenates the contents of a linked list into a string
char* to_string(csv_record *list);

// Prints a list of the filenames in the linked list
void to_string_files(csv_record *list, int option);

// Returns the size linked lists's output
int get_list_size(csv_record *list);

// Gets the total number of nodes in the linked list
int get_list_length(csv_record *list);

// Returns a linked list containing the filesnames and checksums that the Hooli database doesn't have
void get_updated_files(csv_record **response_list, csv_record *request_list,
                       hdb_record *database_list);

// Free the linked list
void csv_free_result(csv_record *record);

#endif
