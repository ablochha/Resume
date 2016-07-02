/*
 * buildmongo.c
 *
 * Computer Science 4411
 * Author: Andrew Bloch-Hansen
 *
 * Iterates through the JSON and inserts the data into the MariaDB database.
 */

#include <stdio.h>
#include <stdint.h>
#include <bson.h>
#include <bcon.h>
#include <mongoc.h>
#include <time.h>

#include "../common/timer.h"
#include "buildmongo.h"
#include "cJSON.h"

// Drop the database
// @param dbname the name of the database
void drop_database_mongo(char *dbname) {
    
    mongoc_client_t *client;        // The MongoDB connection
    mongoc_database_t *database;    // The MongoDB database
    
    bson_error_t error;             // A MongoDB error structure
    
    mongoc_init();
    
    client = mongoc_client_new("mongodb://localhost:27017");
    database = mongoc_client_get_database(client, dbname);
    
    if (!mongoc_database_drop(database, &error)) {
        
        fprintf(stderr, "%s\n", error.message);
        
    } //end if
    
    mongoc_database_destroy(database);
    mongoc_client_destroy(client);
    mongoc_cleanup();
    
} //end drop_database

// Insert the data into the MongoDB books table
// @param root the first item in the JSON
// @param dbname the name of the database
// @return the total time of the insert
uint64_t insert_books_mongo(cJSON *root, char *dbname) {
    
    struct timespec start;              // Time structure to get current time
    uint64_t time = 0;                  // Test result
    
    mongoc_client_t *client;            // The MongoDB connection
    mongoc_database_t *database;        // The MongoDB database
    mongoc_collection_t *collection;    // The MongoDB collection
    
    bson_t *doc;                        // A MongoDB document
    bson_oid_t oid;                     // A MongoDB object ID
    bson_error_t error;                 // A MongoDB error structure
    
    mongoc_init();
    
    client = mongoc_client_new("mongodb://localhost:27017");
    database = mongoc_client_get_database(client, dbname);
    collection = mongoc_client_get_collection(client, dbname, "books");
    
    cJSON *name = NULL;
    cJSON *credited_to = NULL;
    cJSON *isbn = NULL;
    cJSON *publisher = NULL;
    cJSON *isfdb_id = NULL;
    
    cJSON *item = cJSON_GetObjectItem(root, "result");
    
    // Loop through all the items in the JSON file
    for (int i = 0 ; i < cJSON_GetArraySize(item); i++) {
        
        // The the items I want
        cJSON * subitem = cJSON_GetArrayItem(item, i);
        name = cJSON_GetObjectItem(subitem, "name");
        credited_to = cJSON_GetObjectItem(subitem, "credited_to");
        isbn = cJSON_GetObjectItem(subitem, "isbn");
        publisher = cJSON_GetObjectItem(subitem, "publisher");
        isfdb_id = cJSON_GetObjectItem(publisher, "isfdb_id");
    
        // Get the values
        char *str_name = name->valuestring;
        char *str_author = cJSON_GetObjectItem(credited_to, "value")->valuestring;
        char *str_isbn = cJSON_GetObjectItem(isbn, "isbn")->valuestring;
        char *str_isfdb = cJSON_GetObjectItem(isfdb_id, "value")->valuestring;

        // Only accept data with complete fields
        if (str_name != NULL && str_author != NULL && str_isbn != NULL && str_isfdb != NULL) {
            
            // Create the new document
            doc = bson_new();
            bson_oid_init(&oid, NULL);
            BSON_APPEND_OID(doc, "_id", &oid);
            BSON_APPEND_UTF8(doc, "isbn", str_isbn);
            BSON_APPEND_UTF8(doc, "name", str_name);
            BSON_APPEND_UTF8(doc, "author", str_author);
            BSON_APPEND_INT32(doc, "publisher id", atoi(str_isfdb));
            
            start_timer(&start);
            mongoc_collection_insert(collection, 0, doc, NULL, &error);
            time += stop_timer(start);
            
            bson_destroy(doc);
            
        } //end if
        
    } //end for
    
    /*bson_t keys;
    bson_init(&keys);
    BSON_APPEND_INT32(&keys, "isbn", 1);
    mongoc_index_opt_t opt;
    mongoc_index_opt_init(&opt);
    mongoc_collection_create_index(collection, &keys, &opt, &error);
    bson_destroy(&keys);*/
    
    mongoc_collection_destroy(collection);
    mongoc_database_destroy(database);
    mongoc_client_destroy(client);
    mongoc_cleanup();
    
    return time / (CLOCKS_PER_SEC / 1000);
    
} //end insert_books_mongo

// Insert the data into the MongoDB authors table
// @param root the first item in the JSON
// @param dbname the name of the database
// @return the total time of the insert
uint64_t insert_authors_mongo(cJSON *root, char *dbname) {
    
    struct timespec start;              // Time structure to get current time
    uint64_t time = 0;                  // Test result
    
    mongoc_client_t *client;            // The MongoDB connection
    mongoc_database_t *database;        // The MongoDB database
    mongoc_collection_t *collection;    // The MongoDB collection
    
    bson_t *doc;                        // A MongoDB document
    bson_oid_t oid;                     // A MongoDB object ID
    bson_error_t error;                 // A MongoDB error structure

    mongoc_init();
    
    client = mongoc_client_new("mongodb://localhost:27017");
    database = mongoc_client_get_database(client, dbname);
    collection = mongoc_client_get_collection(client, dbname, "authors");
    
    cJSON *name = NULL;
    cJSON *dob = NULL;
    cJSON *gender = NULL;
    cJSON *nationality = NULL;
    
    cJSON *item = cJSON_GetObjectItem(root, "result");
    
    // Loop through all the items in the JSON file
    for (int i = 0 ; i < cJSON_GetArraySize(item) ; i++) {
        
        // Get the items I want
        cJSON * subitem = cJSON_GetArrayItem(item, i);
        name = cJSON_GetObjectItem(subitem, "name");
        dob = cJSON_GetObjectItem(subitem, "/people/person/date_of_birth");
        nationality = cJSON_GetObjectItem(subitem, "/people/person/nationality");
        gender = cJSON_GetObjectItem(subitem, "/people/person/gender");
        
        // Get the values
        char *str_nationality = cJSON_GetObjectItem(nationality, "name")->valuestring;
        char *str_gender = cJSON_GetObjectItem(gender, "name")->valuestring;
        char *str_name = name->valuestring;
        char *str_dob = cJSON_GetObjectItem(dob, "value")->valuestring;
        str_dob[4] = '\0';
        
        // Only accept data with complete fields
        if (str_name != NULL && str_dob != NULL && str_nationality != NULL && str_gender != NULL) {
            
            // Build the new document
            doc = bson_new();
            bson_oid_init(&oid, NULL);
            BSON_APPEND_OID(doc, "_id", &oid);
            BSON_APPEND_UTF8(doc, "name", str_name);
            BSON_APPEND_INT32(doc, "dob", atoi(str_dob));
            BSON_APPEND_UTF8(doc, "nationality", str_nationality);
            BSON_APPEND_UTF8(doc, "gender", str_gender);
            
            start_timer(&start);
            mongoc_collection_insert(collection, 0, doc, NULL, &error);
            time += stop_timer(start);
            
            bson_destroy(doc);
            
        } //end if
        
    } //end for
    
    /*bson_t keys;
    bson_init(&keys);
    BSON_APPEND_INT32(&keys, "name", 1);
    mongoc_index_opt_t opt;
    mongoc_index_opt_init(&opt);
    mongoc_collection_create_index(collection, &keys, &opt, &error);
    bson_destroy(&keys);*/
    
    mongoc_collection_destroy(collection);
    mongoc_database_destroy(database);
    mongoc_client_destroy(client);
    mongoc_cleanup();
    
    return time / (CLOCKS_PER_SEC / 1000);
    
} //end insert_authors_mongo

// Insert the data into the MongoDB authors table
// @param root the first item in the JSON
// @param dbname the name of the database
// @return the total time of the insert
uint64_t insert_publishers_mongo(cJSON *root, char *dbname) {
    
    struct timespec start;              // Time structure to get current time
    uint64_t time = 0;                  // Test result
    
    mongoc_client_t *client;            // The MongoDB connection
    mongoc_database_t *database;        // The MongoDB database
    mongoc_collection_t *collection;    // The MongoDB collection
    
    bson_t *doc;                        // A MongoDB document
    bson_oid_t oid;                     // A MongoDB object ID
    bson_error_t error;                 // A MongoDB error structure

    mongoc_init();
    
    client = mongoc_client_new("mongodb://localhost:27017");
    database = mongoc_client_get_database(client, dbname);
    collection = mongoc_client_get_collection(client, dbname, "publishers");

    cJSON *name = NULL;
    cJSON *isfdb_id = NULL;
    
    cJSON *item = cJSON_GetObjectItem(root, "result");
    
    // Loop through all the items in the JSON file
    for (int i = 0 ; i < cJSON_GetArraySize(item) ; i++) {
        
        // Get the items I want
        cJSON * subitem = cJSON_GetArrayItem(item, i);
        name = cJSON_GetObjectItem(subitem, "name");
        isfdb_id = cJSON_GetObjectItem(subitem, "isfdb_id");
        
        // Get the values
        char *str_name = name->valuestring;
        char *str_isfdb = cJSON_GetObjectItem(isfdb_id, "value")->valuestring;
        
        // Only accept the data with complete values
        if (str_name != NULL && str_isfdb != NULL) {
        
            // Create the new document
            doc = bson_new();
            bson_oid_init(&oid, NULL);
            BSON_APPEND_OID(doc, "_id", &oid);
            BSON_APPEND_INT32(doc, "publisher id", atoi(str_isfdb));
            BSON_APPEND_UTF8(doc, "name", str_name);
            
            start_timer(&start);
            mongoc_collection_insert(collection, 0, doc, NULL, &error);
            time += stop_timer(start);
            
            bson_destroy(doc);
            
        } //end if
        
    } //end for
    
    /*bson_t keys;
    bson_init(&keys);
    BSON_APPEND_INT32(&keys, "publisher id", 1);
    mongoc_index_opt_t opt;
    mongoc_index_opt_init(&opt);
    mongoc_collection_create_index(collection, &keys, &opt, &error);
    bson_destroy(&keys);*/
    
    mongoc_collection_destroy(collection);
    mongoc_database_destroy(database);
    mongoc_client_destroy(client);
    mongoc_cleanup();
    
    return time / (CLOCKS_PER_SEC / 1000);

} //end insert_publishers_mongo
