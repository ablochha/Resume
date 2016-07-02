#ifndef mongoqueries_h
#define mongoqueries_h

#include <stdint.h>

#define MAX_TRIALS 5
#define NUM_QUERIES 13

// Runs a series of queries on the database
void run_mongo_queries(char *dbname, int trial, int num_selects,
                       uint64_t mongo_query_results[MAX_TRIALS][NUM_QUERIES]);

#endif
