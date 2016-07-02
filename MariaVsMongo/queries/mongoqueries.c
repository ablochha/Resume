/*
 * mongoqueries.c
 *
 * Computer Science 4411
 * Author: Andrew Bloch-Hansen
 *
 * Interfaces with MongoDB to run queries.
 */

#include <stdint.h>
#include <stdio.h>
#include <bson.h>
#include <bcon.h>
#include <mongoc.h>
#include <time.h>

#include "../common/filereader.h"
#include "../common/timer.h"
#include "mongoqueries.h"

#define MAX_TRIALS 5
#define NUM_QUERIES 13

// Sends an update request to MongoDB
// @param dbname the name of the database to update
// @param col the name of the collection to update
// @param query the query to send
// @param update the update to send
// @param the time it took
uint64_t run_mongo_update(char *dbname, char *col, bson_t *query, bson_t *update) {
    
    struct timespec start;              // The structure to get the time
    uint64_t time;                      // The elapsed time
    
    mongoc_client_t *client;            // The MongoDB connection
    mongoc_database_t *database;        // The MongoDB database
    mongoc_collection_t *collection;    // The MongoDB collection
    
    bson_error_t error;                 // A MongoDB error structure
    
    mongoc_init();
    
    client = mongoc_client_new("mongodb://localhost:27017");
    database = mongoc_client_get_database(client, dbname);
    collection = mongoc_client_get_collection(client, dbname, col);
    
    start_timer(&start);

    if (!mongoc_collection_update(collection, MONGOC_UPDATE_MULTI_UPDATE, query,
                                  update, NULL, &error)) {
        
        fprintf (stderr, "update() failure: %s\n", error.message);
        
    } //end if
    
    time = stop_timer(start);
    
    bson_destroy(update);
    bson_destroy(query);
    
    mongoc_collection_destroy(collection);
    mongoc_database_destroy(database);
    mongoc_client_destroy(client);
    mongoc_cleanup();
    
    return time / (CLOCKS_PER_SEC / 1000);
    
} //end run_mongo_updates

// Sends an delete request to MongoDB
// @param dbname the name of the database
// @param col the name of the collection
// @param query the query to send
// @param the time it took
uint64_t run_mongo_deletes(char *dbname, char *col, bson_t *query) {
    
    struct timespec start;              // The structure to get the time
    uint64_t time;                      // The elapsed time
    
    mongoc_client_t *client;            // The MongoDB connection
    mongoc_database_t *database;        // The MongoDB database
    mongoc_collection_t *collection;    // The MongoDB collection
    
    bson_error_t error;                 // A MongoDB error structure
    
    mongoc_init();
    
    client = mongoc_client_new("mongodb://localhost:27017");
    database = mongoc_client_get_database(client, dbname);
    collection = mongoc_client_get_collection(client, dbname, col);
    
    start_timer(&start);
    
    if (!mongoc_collection_remove(collection, false, query, NULL, &error)) {
        
        fprintf (stderr, "remove() failure: %s\n", error.message);
        
    } //end if
    
    time = stop_timer(start);
    
    bson_destroy(query);
    
    mongoc_collection_destroy(collection);
    mongoc_database_destroy(database);
    mongoc_client_destroy(client);
    mongoc_cleanup();
    
    return time / (CLOCKS_PER_SEC / 1000);
    
} //end run_mongo_deletes

// Sends an query request to MongoDB
// @param dbname the name of the database
// @param col the name of the collection
// @param query the query to send
// @param num_selects the number of selections to perform
// @param the time it took
uint64_t run_mongo_selects(char *dbname, char *col, bson_t *query, int num_selects) {
    
    struct timespec start;              // The structure to get the time
    uint64_t time;                      // The elapsed time
    
    mongoc_client_t *client;            // The MongoDB connection
    mongoc_database_t *database;        // The MongoDB database
    mongoc_collection_t *collection;    // The MongoDB collection
    
    mongoc_cursor_t *cursor;            // A MongoDB cursor
    
    mongoc_init();
    
    client = mongoc_client_new("mongodb://localhost:27017");
    database = mongoc_client_get_database(client, dbname);
    collection = mongoc_client_get_collection(client, dbname, col);
    
    start_timer(&start);
    
    // Perform the select 'num_select' times
    for (int i = 0; i < num_selects; i++) {

        cursor = mongoc_collection_find(collection, MONGOC_QUERY_NONE, 0, 0, 0,
                                        query, NULL, NULL);
        
        const bson_t *doc;  // Stores a document
        
        // Loop through all of the results
        while (mongoc_cursor_next(cursor, &doc)) {
         
        } //end while
        
        mongoc_cursor_destroy(cursor);
        
    } //end for
    
    time = stop_timer(start);
    
    bson_destroy(query);
    
    mongoc_collection_destroy(collection);
    mongoc_database_destroy(database);
    mongoc_client_destroy(client);
    mongoc_cleanup();
    
    return time / (CLOCKS_PER_SEC / 1000);
    
} //end run_mongo_selects

// Sends an aggregate request to MongoDB
// @param dbname the name of the database
// @param col the name of the collection
// @param query the query to send
// @param num_selects the number of selects to perform
// @param the time it took
uint64_t run_mongo_aggregates(char *dbname, char *col, bson_t *query, int num_selects) {
    
    struct timespec start;              // The structure to get the time
    uint64_t time;                      // The elapsed time
    
    mongoc_client_t *client;            // The MongoDB connection
    mongoc_database_t *database;        // The MongoDB database
    mongoc_collection_t *collection;    // The MongoDB collection
    
    mongoc_cursor_t *cursor;            // A MongoDB cursor
    
    mongoc_init();
    
    client = mongoc_client_new("mongodb://localhost:27017");
    database = mongoc_client_get_database(client, dbname);
    collection = mongoc_client_get_collection(client, dbname, col);
    
    start_timer(&start);
    
    // Perform the select 'num_select' times
    for (int i = 0; i < num_selects; i++) {
        
        cursor = mongoc_collection_aggregate(collection, MONGOC_QUERY_NONE,
                                             query, NULL, NULL);
        
        
        const bson_t *doc;
        while (mongoc_cursor_next(cursor, &doc)) {
            //char *str = bson_as_json (doc, NULL);
            //printf ("%s\n", str);
            //bson_free (str);
        }
        
        mongoc_cursor_destroy(cursor);
        
    } //end for
    
    time = stop_timer(start);
    
    bson_destroy(query);
    
    mongoc_collection_destroy(collection);
    mongoc_database_destroy(database);
    mongoc_client_destroy(client);
    mongoc_cleanup();
    
    return time / (CLOCKS_PER_SEC / 1000);
    
} //end run_mongo_aggregates

// Runs a series of queries on the database
// @param dbname the name of the database
// @param trial the number of trials in the experiment
// @param num_selects the number of selects to perform
// @param mongo_query_results the query results
void run_mongo_queries(char *dbname, int trial, int num_selects,
                       uint64_t mongo_query_results[MAX_TRIALS][NUM_QUERIES]) {
    
    bson_t *query;      // The MongoDB query to use
    bson_t *update;     // The update portion of the query
    
    printf("Performing MongoDB queries trial %d...", trial+1);
    fflush(NULL);
    
    query = BCON_NEW("isbn", "9780802755513");
    mongo_query_results[trial][3] = run_mongo_selects(dbname, "books", query, num_selects);
    
    query = BCON_NEW("name", "Joey Goebel");
    mongo_query_results[trial][4] = run_mongo_selects(dbname, "authors", query, num_selects);
    
    query = BCON_NEW("name", "Meisha Merlin");
    mongo_query_results[trial][5] = run_mongo_selects(dbname, "publishers", query, num_selects);
    
    query = BCON_NEW("dob", "{", "$lt", BCON_INT32(1950), "}");
    mongo_query_results[trial][6] = run_mongo_selects(dbname, "authors", query, num_selects);
    
    query = BCON_NEW("pipeline", "[", "{", "$lookup", "{", "from", "books", "localField",
                     "name", "foreignField", "author", "as", "books_authors", "}", "}", "]");
    mongo_query_results[trial][7] = run_mongo_aggregates(dbname, "authors", query, num_selects);
    
    query = BCON_NEW("pipeline", "[", "{", "$lookup", "{", "from", "publishers", "localField",
                     "publisher id", "foreignField", "publisher id", "as", "books_publishers", "}", "}", "]");
    mongo_query_results[trial][8] = run_mongo_aggregates(dbname, "books", query, num_selects);
    
    query = BCON_NEW("pipeline", "[", "{", "$group", "{", "_id", "$nationality", "avgDoB",
                     "{", "$avg", "$dob", "}", "}", "}", "]");
    mongo_query_results[trial][9] = run_mongo_aggregates(dbname, "authors", query, num_selects);
    
    query = BCON_NEW("name", "{", "$lt", "M", "}");
    update = BCON_NEW("$set", "{", "name", "Andrew", "}");
    mongo_query_results[trial][0] = run_mongo_update(dbname, "books", query, update);
    
    query = BCON_NEW("dob", "{", "$lt", BCON_INT32(1950), "}");
    update = BCON_NEW("$set", "{", "nationality", "Russia", "}");
    mongo_query_results[trial][1] = run_mongo_update(dbname, "authors", query, update);
    
    query = BCON_NEW("publisher id", "{", "$gt", BCON_INT32(130), "}");
    update = BCON_NEW("$set", "{", "name", "The Daily Planet", "}");
    mongo_query_results[trial][2] = run_mongo_update(dbname, "publishers", query, update);
    
    query = BCON_NEW("name", "Andrew");
    mongo_query_results[trial][10] = run_mongo_deletes(dbname, "books", query);
    
    query = BCON_NEW("nationality", "Russia");
    mongo_query_results[trial][11] = run_mongo_deletes(dbname, "authors", query);
    
    query = BCON_NEW("name", "The Daily Planet");
    mongo_query_results[trial][12] = run_mongo_deletes(dbname, "publishers", query);
    
    printf("Done\n");
    
} //end run_mongo_queries
