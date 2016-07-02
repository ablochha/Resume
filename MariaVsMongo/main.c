/*
 * main.c
 *
 * Computer Science 4411
 * Author: Andrew Bloch-Hansen
 *
 * This program runs an experiment on between 1-5 data sets. The data is read
 * from JSON files in the builddatabase/data/ directory. When the JSON is parsed
 * with cJSON, the data are inserted into MariaDB and MongoDB. There are then
 * 7 different selects, 3 updates, and 3 deletes performed.
 */

#include <ctype.h>
#include <math.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdint.h>
#include <stdlib.h>
#include <string.h>

#include "builddatabase/builddata.h"
#include "common/filereader.h"
#include "queries/runqueries.h"

#define MAX_TRIALS 5
#define NUM_TABLES 3
#define NUM_QUERIES 13
#define NUM_SELECTS 100

// Determine if a valid number of trials was given
// @param str the input number of trials
// @return the number of trials, MAX_TRIALS if input invalid
static int num_trials(const char *str) {
    
    const char *ref = str;
    
    // Handle negative numbers
    if (*str == '-') {
        
        ++str;
        
    } //end if
    
    // Handle empty string or just "-"
    if (!*str) {
        
        return MAX_TRIALS;
        
    } //end if
    
    // Check for non-digit chars in the rest of the stirng.
    while (*str) {
        
        if (!isdigit(*str)) {
            
            return MAX_TRIALS;
            
        } //end if
        
        else {
            
            ++str;
            
        } //end else
        
    } //end while
    
    // Only 5 data sets
    if (atoi(ref) > 5) {
        
        return MAX_TRIALS;
        
    } //end if
    
    return atoi(ref);
    
} //end num_trials

// Determine if a valid number of selects was given
// @param str the input number of selects
// @return the number of selects, NUM_SELECTS if input invalid
static int num_selects(const char *str) {
    
    const char *ref = str;
    
    // Handle negative numbers
    if (*str == '-') {
        
        ++str;
        
    } //end if
    
    // Handle empty string or just "-"
    if (!*str) {
        
        return NUM_SELECTS;
        
    } //end if
    
    // Check for non-digit chars in the rest of the stirng.
    while (*str) {
        
        if (!isdigit(*str)) {
            
            return NUM_SELECTS;
            
        } //end if
        
        else {
            
            ++str;
            
        } //end else
        
    } //end while
    
    return atoi(ref);
    
} //end

int main(int argc, char **argv) {
    
    // Make sure they used enough command line arguments
    if (argc != 3) {
        
        printf("Usage: %s trials selects\n", argv[0]);
        printf("e.g. './main 5 100'\n");
        return 1;
        
    } //end if
    
    int trials = num_trials(argv[1]);                       // The number of trials in the experiment
    int selects = num_selects(argv[2]);                     // The number of selects during the tests
    
    uint64_t mongo_build_results[MAX_TRIALS][NUM_TABLES];   // MongoDB build results
    uint64_t maria_build_results[MAX_TRIALS][NUM_TABLES];   // MariaDB build results
    uint64_t mongo_query_results[MAX_TRIALS][NUM_QUERIES];  // MongoDB query results
    uint64_t maria_query_results[MAX_TRIALS][NUM_QUERIES];  // MariaDB query results
    uint64_t affected_rows[MAX_TRIALS][NUM_QUERIES];        // Affected rows per query
    
    memset(mongo_build_results, 0, sizeof(mongo_build_results));
    memset(maria_build_results, 0, sizeof(maria_build_results));
    memset(mongo_query_results, 0, sizeof(mongo_query_results));
    memset(maria_query_results, 0, sizeof(maria_query_results));
    
    // Build the data and run the queries
    build_databases(mongo_build_results, maria_build_results, trials);
    run_queries(mongo_query_results, maria_query_results, affected_rows, trials, selects);
    
    // Output the results to a series of files
    output_build_csv(mongo_build_results, maria_build_results, trials, "csv/insertbooks.csv", 0);
    output_build_csv(mongo_build_results, maria_build_results, trials, "csv/insertauthors.csv", 1);
    output_build_csv(mongo_build_results, maria_build_results, trials, "csv/insertpublishers.csv", 2);
    output_query_csv(mongo_query_results, maria_query_results, trials, "csv/update1.csv", 0);
    output_query_csv(mongo_query_results, maria_query_results, trials, "csv/update2.csv", 1);
    output_query_csv(mongo_query_results, maria_query_results, trials, "csv/update3.csv", 2);
    output_query_csv(mongo_query_results, maria_query_results, trials, "csv/select1.csv", 3);
    output_query_csv(mongo_query_results, maria_query_results, trials, "csv/select2.csv", 4);
    output_query_csv(mongo_query_results, maria_query_results, trials, "csv/select3.csv", 5);
    output_query_csv(mongo_query_results, maria_query_results, trials, "csv/select4.csv", 6);
    output_query_csv(mongo_query_results, maria_query_results, trials, "csv/select5.csv", 7);
    output_query_csv(mongo_query_results, maria_query_results, trials, "csv/select6.csv", 8);
    output_query_csv(mongo_query_results, maria_query_results, trials, "csv/select7.csv", 9);
    output_query_csv(mongo_query_results, maria_query_results, trials, "csv/delete1.csv", 10);
    output_query_csv(mongo_query_results, maria_query_results, trials, "csv/delete2.csv", 11);
    output_query_csv(mongo_query_results, maria_query_results, trials, "csv/delete3.csv", 12);
    
    output_build_results(mongo_build_results, maria_build_results, trials);
    output_query_results(mongo_query_results, maria_query_results, trials, selects);
    output_affected_rows(affected_rows, trials);
    
    printf("CSV results saved to 'csv' directory\n");
    printf("Human-friendly results have been saved to 'results.txt'\n");
    
} //end main
