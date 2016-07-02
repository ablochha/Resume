#ifndef builddata_h
#define builddata_h

#include <stdint.h>

#define MAX_TRIALS 5
#define NUM_TABLES 3

// Build the correct number of databases according the the number of trials
void build_databases(uint64_t mongo_build_results[MAX_TRIALS][NUM_TABLES],
                     uint64_t maria_build_results[MAX_TRIALS][NUM_TABLES],
                     int num_trials);

#endif
