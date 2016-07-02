#ifndef filereader_h
#define filereader_h

#include <stdint.h>

#define MAX_TRIALS 5
#define NUM_TABLES 3
#define NUM_QUERIES 13

// Makes a new file with the specified name, overwrites previous file
int write_new_file(const char *path);

// Deletes a file
void remove_file(const char *path);

// Reads from a file and outputs into a char array
char* read_from_file(const char *path);

// Writes a chunk of data to a file
int write_buffer_to_file(char *buffer, const char *path);

// Outputs the build results to a file
void output_build_results(uint64_t mongo_build_results[MAX_TRIALS][NUM_TABLES],
                          uint64_t maria_build_results[MAX_TRIALS][NUM_TABLES],
                          int num_trials);

// Outputs the query results to a file
void output_query_results(uint64_t mongo_query_results[MAX_TRIALS][NUM_QUERIES],
                          uint64_t maria_query_results[MAX_TRIALS][NUM_QUERIES],
                          int num_trials, int num_selects);

// Output the number of affect rows for each query in the experiment
void output_affected_rows(uint64_t affected_rows[MAX_TRIALS][NUM_QUERIES],
                          int num_trials);

// Outputs the results of the building phase to a file
void output_build_csv(uint64_t mongo_build_results[MAX_TRIALS][NUM_TABLES],
                      uint64_t maria_build_results[MAX_TRIALS][NUM_TABLES],
                      int num_trials, char *filename, int table);

// Outputs the results of the query phase to a file
void output_query_csv(uint64_t mongo_query_results[MAX_TRIALS][NUM_QUERIES],
                      uint64_t maria_query_results[MAX_TRIALS][NUM_QUERIES],
                      int num_trials, char *filename, int test);

#endif
