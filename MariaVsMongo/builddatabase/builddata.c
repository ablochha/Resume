/*
 * builddata.c
 *
 * Computer Science 4411
 * Author: Andrew Bloch-Hansen
 *
 * Parses the JSON using cJSON and builds the MariaDB and MongoDB databases.
 */

#include <bcon.h>
#include <bson.h>
#include <math.h>
#include <mongoc.h>
#include <stdbool.h>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

#include "builddata.h"
#include "buildmaria.h"
#include "buildmongo.h"
#include "cJSON.h"
#include "../common/filereader.h"
#include "../common/timer.h"

#define MAX_TRIALS 5
#define NUM_TABLES 3

// Parses the JSON and builds the MariaDB database
// @param dbname the name of the database
// @param book_path the path to the book JSON file
// @param author_path the path to the author JSON file
// @param publisher_path the path to the publisher JSON file
// @param results the results of the build
// @param trial the current trial number
static void build_maria_database(char *dbname, char *book_path, char *author_path,
                                 char *publisher_path,
                                 uint64_t results[MAX_TRIALS][NUM_TABLES], int trial) {
    
    char *book_str;         // Buffer for the book JSON file
    char *author_str;       // Buffer for the author JSON file
    char *publisher_str;    // Buffer for the publisher JSON file
    
    cJSON *book_root;       // cJSON root pointer for book JSON
    cJSON *author_root;     // cJSON root pointer for author JSON
    cJSON *publisher_root;  // cJSON root pointer for publisher JSON
    
    printf("Building MariaDB %s...", dbname);
    fflush(NULL);
    
    // Drop database if exists
    drop_database_maria(dbname);
    
    // Add authors table
    author_str = read_from_file(author_path);
    author_root = cJSON_Parse(author_str);
    results[trial][1] = insert_authors_maria(author_root, dbname);
    free(author_str);
    cJSON_Delete(author_root);
    
    // Add publishers table
    publisher_str = read_from_file(publisher_path);
    publisher_root = cJSON_Parse(publisher_str);
    results[trial][2] = insert_publishers_maria(publisher_root, dbname);
    free(publisher_str);
    cJSON_Delete(publisher_root);
    
    // Add books table
    book_str = read_from_file(book_path);
    book_root = cJSON_Parse(book_str);
    results[trial][0] = insert_books_maria(book_root, dbname);
    free(book_str);
    cJSON_Delete(book_root);
    
    printf("Done\n");
    
} //end build_maria_database

// Parses the JSON and builds the MongoDB database
// @param dbname the name of the database
// @param book_path the path to the book JSON file
// @param author_path the path to the author JSON file
// @param publisher_path the path to the publisher JSON file
// @param results the results of the build
// @param trial the current trial number
static void build_mongo_database(char *dbname, char *book_path, char *author_path,
                                 char *publisher_path,
                                 uint64_t results[MAX_TRIALS][NUM_TABLES], int trial) {
    
    char *book_str;         // Buffer for the book JSON file
    char *author_str;       // Buffer for the author JSON file
    char *publisher_str;    // Buffer for the publisher JSON file
    
    cJSON *book_root;
    cJSON *author_root;
    cJSON *publisher_root;
    
    printf("Building MongoDB %s...", dbname);
    fflush(NULL);
    
    // Drop database if exists
    drop_database_mongo(dbname);
    
    // Add books table
    book_str = read_from_file(book_path);
    book_root = cJSON_Parse(book_str);
    results[trial][0] = insert_books_mongo(book_root, dbname);
    free(book_str);
    cJSON_Delete(book_root);
    
    // Add authors table
    author_str = read_from_file(author_path);
    author_root = cJSON_Parse(author_str);
    results[trial][1] = insert_authors_mongo(author_root, dbname);
    free(author_str);
    cJSON_Delete(author_root);
    
    // Add publishers table
    publisher_str = read_from_file(publisher_path);
    publisher_root = cJSON_Parse(publisher_str);
    results[trial][2] = insert_publishers_mongo(publisher_root, dbname);
    free(publisher_str);
    cJSON_Delete(publisher_root);
    
    printf("Done\n");
    
} //end build_mongo_database

// Build the correct number of databases according the the number of trials
// @param mongo_build_results the MongoDB build results
// @param maria_build_results the MariaDB build results
// @param num_trials the number of trials in the experiment
void build_databases(uint64_t mongo_build_results[MAX_TRIALS][NUM_TABLES],
                            uint64_t maria_build_results[MAX_TRIALS][NUM_TABLES],
                            int num_trials) {
    
    int trial = 0;  // The current trial number
    
    char *JSON[15]; // The paths to the data
    
    JSON[0] = "builddatabase/data/book/100book.json";
    JSON[1] = "builddatabase/data/author/100author.json";
    JSON[2] = "builddatabase/data/publisher/100publisher.json";
    
    JSON[3] = "builddatabase/data/book/1000book.json";
    JSON[4] = "builddatabase/data/author/1000author.json";
    JSON[5] = "builddatabase/data/publisher/1000publisher.json";
    
    JSON[6] = "builddatabase/data/book/5000book.json";
    JSON[7] = "builddatabase/data/author/5000author.json";
    JSON[8] = "builddatabase/data/publisher/2500publisher.json";
    
    JSON[9] = "builddatabase/data/book/10000book.json";
    JSON[10] = "builddatabase/data/author/10000author.json";
    JSON[11] = "builddatabase/data/publisher/5000publisher.json";
    
    JSON[12] = "builddatabase/data/book/25000book.json";
    JSON[13] = "builddatabase/data/author/25000author.json";
    JSON[14] = "builddatabase/data/publisher/7500publisher.json";
    
    // Build the MongoDB databases for each trial
    for (int i = 0; i < num_trials; i++) {
        
        char *dbname;
        asprintf(&dbname, "Trial%d", i+1);
        build_mongo_database(dbname, JSON[i*NUM_TABLES], JSON[(i*NUM_TABLES)+1],
                             JSON[(i*NUM_TABLES)+2], mongo_build_results, trial);
        trial++;
        free(dbname);
    
    } //end for
    
    printf("\n");
    trial = 0;
    
    // Build the MariaDB databases for each trial
    for (int i = 0; i < num_trials; i++) {
        
        char *dbname;
        asprintf(&dbname, "Trial%d", i+1);
        build_maria_database(dbname, JSON[i*NUM_TABLES], JSON[(i*NUM_TABLES)+1],
                             JSON[(i*NUM_TABLES)+2], maria_build_results, trial);
        trial++;
        free(dbname);
        
    } //end for
    
    printf("\n");
    
} //end build_databases
