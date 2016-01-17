/*
 * hdb2.c
 *
 * Computer Science 3357a - Fall 2015
 * Author: Andrew Bloch-Hansen
 *
 */

#include <aws_dynamo.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <syslog.h>
#include <time.h>

#include "hdb2.h"

#define PASSWORDS "########"
#define TOKENS "@@@@@@@@"
#define TOKEN_LENGTH 16
#define ACCESSKEYID "AKIAJEZNDQQR54H2AGOQ"
#define SECRETACCESSKEYID "rAOdIKyvhhy+2MGQbzi7V/qJnHZcUwbNrtm8elL8"
#define TABLE "ablochha-hooli"

// Store a file record in the Hooli database.
// @param record the record to be stored in the server
void hdb_store_file(hdb_record *record) {
    
    struct aws_handle *aws_dynamo = aws_init(ACCESSKEYID, SECRETACCESSKEYID);
    
    char *put_request;
    struct aws_dynamo_put_item_response *put_r;
    
    asprintf(&put_request, \
            "{\"TableName\":\"%s\",\
                \"Item\":{\
                    \"username\":{\"S\":\"%s\"},\
                    \"filename\":{\"S\":\"%s\"},\
                    \"checksum\":{\"S\":\"%s\"}\
                }\
            }", TABLE, record->username, record->filename, record->checksum);
    
    put_r = aws_dynamo_put_item(aws_dynamo, put_request, NULL, 0);
    aws_dynamo_free_put_item_response(put_r);
    aws_deinit(aws_dynamo);
    
    free(put_request);
    
} //end hdb_store_file

static void add_authenticate_info(const char *username, const char *filename, const char *checksum) {
    
    struct aws_handle *aws_dynamo = aws_init(ACCESSKEYID, SECRETACCESSKEYID);
    
    char *put_request;
    struct aws_dynamo_put_item_response *put_r;
    
    asprintf(&put_request, \
            "{\"TableName\":\"%s\",\
                \"Item\":{\
                    \"username\":{\"S\":\"%s\"},\
                    \"filename\":{\"S\":\"%s\"},\
                    \"checksum\":{\"S\":\"%s\"}\
                }\
            }", TABLE, username, filename, checksum);
    
    put_r = aws_dynamo_put_item(aws_dynamo, put_request, NULL, 0);
    aws_dynamo_free_put_item_response(put_r);
    aws_deinit(aws_dynamo);
    
    free(put_request);
    
} //end add_authenticate_info

// Remove a file record from the Hooli database.
// @param username the key to use in the database
// @param filename the field to use in the database
// @return the number of files removed
void hdb_remove_file(const char *username, const char *filename) {
    
    struct aws_handle *aws_dynamo = aws_init(ACCESSKEYID, SECRETACCESSKEYID);
    
    char *delete_item_request;
    struct aws_dynamo_delete_item_response *delete_r;
    
    struct aws_dynamo_attribute attributes[] = {
        {
            .type = AWS_DYNAMO_STRING,
            .name = "username",
            .name_len = strlen("username"),
        },
        {
            .type = AWS_DYNAMO_STRING,
            .name = "filename",
            .name_len = strlen("filename"),
        },
        {
            .type = AWS_DYNAMO_STRING,
            .name = "checksum",
            .name_len = strlen("checksum"),
        },
    };
    
    asprintf(&delete_item_request, \
            "{\"TableName\":\"%s\",\
                \"Key\":{\
                    \"username\":{\"S\":\"%s\"},\
                    \"filename\":{\"S\":\"%s\"}\
                }\
            }", TABLE, username, filename);
    
    delete_r = aws_dynamo_delete_item(aws_dynamo, delete_item_request, attributes,
                                      sizeof(attributes) / sizeof(attributes[0]));
    aws_dynamo_free_delete_item_response(delete_r);
    aws_deinit(aws_dynamo);
    
    free(delete_item_request);
    
} //end hdb_remove_file
    
// If the specified file is found in the Hooli database, return its checksum.
// Otherwise, return NULL.
// @param username the key to use in the database
// @param filename the field to use in the database
// @return the checksum of the field in the database
char* hdb_file_checksum(const char *username, const char *filename) {
    
    struct aws_handle *aws_dynamo = aws_init(ACCESSKEYID, SECRETACCESSKEYID);
    
    char *get_item_request;
    struct aws_dynamo_get_item_response *get_r;
    struct aws_dynamo_attribute *checksum_attribute;
    
    char *checksum;
    
    struct aws_dynamo_attribute attributes[] = {
        {
            .type = AWS_DYNAMO_STRING,
            .name = "username",
            .name_len = strlen("username"),
        },
        {
            .type = AWS_DYNAMO_STRING,
            .name = "filename",
            .name_len = strlen("filename"),
        },
        {
            .type = AWS_DYNAMO_STRING,
            .name = "checksum",
            .name_len = strlen("checksum"),
        },
    };
    
    asprintf(&get_item_request, \
            "{\"TableName\":\"%s\",\
                \"Key\":{\
                    \"username\":{\"S\":\"%s\"},\
                    \"filename\":{\"S\":\"%s\"}\
                }\
            }", TABLE, username, filename);
    
    get_r = aws_dynamo_get_item(aws_dynamo, get_item_request, attributes,
                                sizeof(attributes) / sizeof(attributes[0]));
                                
    if (get_r == NULL) {
        
        aws_deinit(aws_dynamo);
        return NULL;
        
    } //end if
                                
    if (get_r->item.attributes == NULL) {
        
        /* No item found. */
        aws_dynamo_free_get_item_response(get_r);
        aws_deinit(aws_dynamo);
        return NULL;
        
    } //end if
                                         
    checksum_attribute = &(get_r->item.attributes[2]);
    checksum = strdup(checksum_attribute->value.string);
                                         
    aws_dynamo_free_get_item_response(get_r);
    aws_deinit(aws_dynamo);
    
    free(get_item_request);
                                
    return checksum;
    
} //end hdb_file_checksum
    
// Get the number of files stored in the Hooli database for the specified user.
// @param username the key to use in the database
// @return the number of fields for a key
int hdb_file_count(const char *username) {
                                    
    struct aws_handle *aws_dynamo = aws_init(ACCESSKEYID, SECRETACCESSKEYID);
    
    char *get_item_request;
    struct aws_dynamo_get_item_response *get_r;
    
    struct aws_dynamo_attribute attributes[] = {
        {
            .type = AWS_DYNAMO_STRING,
            .name = "username",
            .name_len = strlen("username"),
        },
        {
            .type = AWS_DYNAMO_STRING,
            .name = "filename",
            .name_len = strlen("filename"),
        },
        {
            .type = AWS_DYNAMO_STRING,
            .name = "checksum",
            .name_len = strlen("checksum"),
        },
    };
    
    asprintf(&get_item_request, \
            "{\"TableName\":\"%s\",\
                \"Key\":{\
                    \"username\":{\"S\":\"%s\"}\
                }\
            }", TABLE, username);
    
    get_r = aws_dynamo_get_item(aws_dynamo, get_item_request, attributes,
                                sizeof(attributes) / sizeof(attributes[0]));
    
    int size = sizeof(get_r->item.attributes)/sizeof(get_r->item.attributes[0]);
                                
    aws_dynamo_free_get_item_response(get_r);
    aws_deinit(aws_dynamo);
    
    free(get_item_request);
                                
    return size / 3;
    
} //end hdb_file_count
    
// Return a Boolean value indicating whether or not the specified user exists
// in the Hooli database
// @param username the key to use in the database
// @return true if the user exists, otherwise false
bool hdb_user_exists(const char *username) {
    
    struct aws_handle *aws_dynamo = aws_init(ACCESSKEYID, SECRETACCESSKEYID);
    
    char *get_item_request;
    struct aws_dynamo_get_item_response *get_r;
    
    struct aws_dynamo_attribute attributes[] = {
        {
            .type = AWS_DYNAMO_STRING,
            .name = "username",
            .name_len = strlen("username"),
        },
        {
            .type = AWS_DYNAMO_STRING,
            .name = "filename",
            .name_len = strlen("filename"),
        },
        {
            .type = AWS_DYNAMO_NUMBER,
            .name = "checksum",
            .name_len = strlen("checksum"),
        },
    };
    
    asprintf(&get_item_request, \
            "{\"TableName\":\"%s\",\
                \"Key\":{\
                    \"username\":{\"S\":\"%s\"}\
                }\
            }", TABLE, username);
    
    get_r = aws_dynamo_get_item(aws_dynamo, get_item_request, attributes,
                                sizeof(attributes) / sizeof(attributes[0]));
                                
    if (get_r->item.attributes == NULL) {
                                    
        /* No item found. */
        aws_dynamo_free_get_item_response(get_r);
        aws_deinit(aws_dynamo);
        
        free(get_item_request);
        
        return false;
                                    
    } //end if
                 
    aws_dynamo_free_get_item_response(get_r);
    aws_deinit(aws_dynamo);
    
    free(get_item_request);
    
    return true;
    
} //end hdb_user_exists
    
// Return a Boolean value indicating whether or not the specified file exists
// in the Hooli database
// @param username the key to use in the database
// @param filename the field to use in the database
// @return true if the file exists, othewise false
bool hdb_file_exists(const char *username, const char *filename) {
    
    struct aws_handle *aws_dynamo = aws_init(ACCESSKEYID, SECRETACCESSKEYID);
    
    char *get_item_request;
    struct aws_dynamo_get_item_response *get_r;
    
    struct aws_dynamo_attribute attributes[] = {
        {
            .type = AWS_DYNAMO_STRING,
            .name = "username",
            .name_len = strlen("username"),
        },
        {
            .type = AWS_DYNAMO_STRING,
            .name = "filename",
            .name_len = strlen("filename"),
        },
        {
            .type = AWS_DYNAMO_NUMBER,
            .name = "checksum",
            .name_len = strlen("checksum"),
        },
    };
    
    asprintf(&get_item_request, \
            "{\"TableName\":\"%s\",\
                \"Key\":{\
                    \"username\":{\"S\":\"%s\"},\
                    \"filename\":{\"S\":\"%s\"}\
                }\
            }", TABLE, username, filename);
    
    get_r = aws_dynamo_get_item(aws_dynamo, get_item_request, attributes,
                                sizeof(attributes) / sizeof(attributes[0]));
                                
    if (get_r->item.attributes == NULL) {
                                    
        /* No item found. */
        aws_dynamo_free_get_item_response(get_r);
        aws_deinit(aws_dynamo);
        
        free(get_item_request);
        
        return false;
                                    
    } //end if
                                
    aws_dynamo_free_get_item_response(get_r);
    aws_deinit(aws_dynamo);
    
    free(get_item_request);
    
    return true;
                                
} //end hdb_file_exists
    
// Return a linked list of all of the specified user's file records stored in the
// Hooli database
// @param username the key to use in the database
// @return a linked list of user files
hdb_record* hdb_user_files(const char *username) {
    
    // Check if there are any files for the user
    if (hdb_file_count(username) > 0) {
        
        struct aws_handle *aws_dynamo = aws_init(ACCESSKEYID, SECRETACCESSKEYID);
        
        char *get_item_request;
        struct aws_dynamo_get_item_response *get_r;
        
        hdb_record *list;       // The head of the linked list
        hdb_record *p;          // The pointer that traverses the linked list
        
        struct aws_dynamo_attribute attributes[] = {
            {
                .type = AWS_DYNAMO_STRING,
                .name = "username",
                .name_len = strlen("username"),
            },
            {
                .type = AWS_DYNAMO_STRING,
                .name = "filename",
                .name_len = strlen("filename"),
            },
            {
                .type = AWS_DYNAMO_NUMBER,
                .name = "checksum",
                .name_len = strlen("checksum"),
            },
        };
        
        asprintf(&get_item_request, \
                "{\"TableName\":\"%s\",\
                    \"Key\":{\
                        \"username\":{\"S\":\"%s\"}\
                    }\
                }", TABLE, username);
        
        get_r = aws_dynamo_get_item(aws_dynamo, get_item_request, attributes,
                                    sizeof(attributes) / sizeof(attributes[0]));
        
        list = malloc(sizeof(struct hdb_record));
        p = list;

        int i = 0;
            
        while (&(get_r->item.attributes[i]) != NULL) {
            
            struct aws_dynamo_attribute *username_attribute = &(get_r->item.attributes[i]);
            struct aws_dynamo_attribute *filename_attribute = &(get_r->item.attributes[i+1]);
            struct aws_dynamo_attribute *checksum_attribute = &(get_r->item.attributes[i+2]);
            
            p->username = strdup(username_attribute->value.string);
            p->filename = strdup(filename_attribute->value.string);
            p->checksum = strdup(checksum_attribute->value.string);
            
            i+= 3;
            p->next = NULL;
            
            if (&(get_r->item.attributes[i]) != NULL) {
                
                p->next = malloc(sizeof(struct hdb_record));
                p = p->next;
                
            } //end if
            
        } //end while
                                    
        aws_dynamo_free_get_item_response(get_r);
        aws_deinit(aws_dynamo);
        
        free(get_item_request);
        
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
        
// Removes a token from the token database
// @param username the key to use in the database
// @param checksum the value to use in the database
static void remove_token(const char *username, const char *checksum) {
                                        
    struct aws_handle *aws_dynamo = aws_init(ACCESSKEYID, SECRETACCESSKEYID);
                                        
    char *delete_item_request;
    struct aws_dynamo_delete_item_response *delete_r;
                                        
    struct aws_dynamo_attribute attributes[] = {
        {
            .type = AWS_DYNAMO_STRING,
            .name = "username",
            .name_len = strlen("username"),
        },
        {
            .type = AWS_DYNAMO_STRING,
            .name = "filename",
            .name_len = strlen("filename"),
        },
        {
            .type = AWS_DYNAMO_STRING,
            .name = "checksum",
            .name_len = strlen("checksum"),
        },
    };
                                        
    asprintf(&delete_item_request, \
            "{\"TableName\":\"%s\",\
                \"Key\":{\
                    \"username\":{\"S\":\"%s\"},\
                    \"checksum\":{\"S\":\"%s\"}\
                }\
            }", TABLE, username, checksum);
                                        
    delete_r = aws_dynamo_delete_item(aws_dynamo, delete_item_request, attributes,
                                      sizeof(attributes) / sizeof(attributes[0]));
    aws_dynamo_free_delete_item_response(delete_r);
    aws_deinit(aws_dynamo);
    
    free(delete_item_request);
                                                                          
} //end remove_token
                                    
// Authenticates a user and provides a secure token
// @param username the key to use in the database
// @param password
// @return the number of files deleted
char* hdb_authenticate(const char* username, const char* password) {
                                        
    // The username is in the database
    if (hdb_file_exists(PASSWORDS, username)) {
                                            
        char *hooli_password = hdb_file_checksum(PASSWORDS, username);
                                            
         // The given password matches the database's
         if (strcmp(hooli_password, password) == 0) {
             
             remove_token(TOKENS, username);
                                                
             // Generate a new random token and add it to the TOKENS hash
             char *token = generate_token();
             add_authenticate_info(TOKENS, token, username);
             
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
        
        add_authenticate_info(PASSWORDS, username, password);
        
        char *token = generate_token();
        
        add_authenticate_info(TOKENS, token, username);

        return token;
                                            
    } //end else
                                        
    return NULL;
                                        
} //end hdb_authenticate
    
// Verify a token
// @param token the token
// @return the username associated with the token, or NULL if that token is invalid
char* hdb_verify_token(const char* token) {
                                          
    return hdb_file_checksum(TOKENS, token);
                                          
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
