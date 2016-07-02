#ifndef mariaqueries_h
#define mariaqueries_h

#include <stdint.h>

#define MAX_TRIALS 5
#define NUM_QUERIES 13

// Runs a series of queries on the database
void run_maria_queries(char *dbname, int trial, int num_selects,
                       uint64_t maria_query_results[MAX_TRIALS][NUM_QUERIES],
                       uint64_t affected_rows[MAX_TRIALS][NUM_QUERIES]);

#endif
