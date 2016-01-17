#ifndef HDB2_H
#define HDB2_H

#include <stdbool.h>

// A record stored in the Hooli database
typedef struct hdb_record {
    char* username;
    char* filename;
    char* checksum;
    struct hdb_record* next;
} hdb_record;

// Store a file record in the Hooli database.
void hdb_store_file(hdb_record* record);

// Remove a file record from the Hooli database.
void hdb_remove_file(const char* username, const char* filename);

// If the specified file is found in the Hooli database, return its checksum.
// Otherwise, return NULL.
char* hdb_file_checksum(const char* username, const char* filename);

// Get the number of files stored in the Hooli database for the specified user.
int hdb_file_count(const char* username);

// Return a Boolean value indicating whether or not the specified user exists
// in the Hooli database
bool hdb_user_exists(const char* username);

// Return a Boolean value indicating whether or not the specified file exists
// in the Hooli database
bool hdb_file_exists(const char* username, const char* filename);

// Return a linked list of all of the specified user's file records stored in the
// Hooli database
hdb_record* hdb_user_files(const char* username);

// Free the linked list returned by hdb_user_files()
void hdb_free_result(hdb_record* record);

// Delete the specifid user and all of his/her file records from the Hooli
// database.
int hdb_delete_user(const char* username);

// Authenticates a user and provides a secure token
char* hdb_authenticate(const char* username, const char* password);

// Verify a token
char* hdb_verify_token(const char* token);

// Generates a random 16 character alphanumeric token
char* generate_token();

#endif
