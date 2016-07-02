/*
 * runqueries.c
 *
 * Computer Science 4411
 * Author: Andrew Bloch-Hansen
 *
 * Runs a series of selects, updates, and deletes. 3 single table selects based
 * on primary key, 1 single table select with multiple results, 2 joins, 1 aggregate
 * function.
 */

#include <math.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdint.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <unistd.h>

#include "../common/timer.h"
#include "mariaqueries.h"
#include "mongoqueries.h"
#include "runqueries.h"

#define MAX_TRIALS 5
#define NUM_QUERIES 13

// Runs the queries for the experiment
// @param mongo_query_results the results for the mongo queries
// @param maria_query_results the results for the maria queries
// @param affected_rows the number of affected rows for each query
// @param num_trials the number of trials in the experiment
// @param num_selects the number of selects in the experiment
void run_queries(uint64_t mongo_query_results[MAX_TRIALS][NUM_QUERIES],
                 uint64_t maria_query_results[MAX_TRIALS][NUM_QUERIES],
                 uint64_t affected_rows[MAX_TRIALS][NUM_QUERIES],
                 int num_trials, int num_selects) {
    
    // Test selects first, since updates ruin the database
    // Test the MongoDB selects
    for (int i = 0; i < num_trials; i++) {
        
        char *dbname;
        asprintf(&dbname, "Trial%d", i+1);
        run_mongo_queries(dbname, i, num_selects, mongo_query_results);
        free(dbname);
        
    } //end for
    
    printf("\n");
    
    // Test the MariaDB queries
    for (int i = 0; i < num_trials; i++) {
        
        char *dbname;
        asprintf(&dbname, "Trial%d", i+1);
        run_maria_queries(dbname, i, num_selects, maria_query_results, affected_rows);
        free(dbname);
        
    } //end for
    
    printf("\n");
    
} //end run_queries
