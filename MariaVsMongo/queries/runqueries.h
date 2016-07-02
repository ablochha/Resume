#ifndef runqueries_h
#define runqueries_h

#include <stdint.h>

#define MAX_TRIALS 5
#define NUM_QUERIES 13

// Runs the queries for the experiment
void run_queries(uint64_t mongo_query_results[MAX_TRIALS][NUM_QUERIES],
                 uint64_t maria_query_results[MAX_TRIALS][NUM_QUERIES],
                 uint64_t affected_rows[MAX_TRIALS][NUM_QUERIES],
                 int num_trials, int num_selects);

#endif
