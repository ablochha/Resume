/*
 * filereader.c
 *
 * Computer Science 4411
 * Author: Andrew Bloch-Hansen
 *
 * Reads data from a file.
 */

#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

#include "filereader.h"

// Makes a new file with the specified name, overwrites previous file
// @param path the pathname of the file
// @return 1 if the file was created, -1 otherwise
int write_new_file(const char *path) {
    
    FILE *fp;           // The pointer to the file
    
    // Try to open the file
    fp = fopen(path, "wb");
    
    // The file failed to opened
    if (!fp) {
        
        fprintf(stderr, "Error opening file '%s'", path);
        return -1;
        
    } //end if
    
    fclose(fp);
    return 1;
    
} //end create_new_file

// Deletes a file
// @param path the path to the file
void remove_file(const char *path) {
    
    int ret = remove(path);
    
    if (ret != 0) {
        
        fprintf(stderr, "Unable to remove file '%s'", path);
        
    } //end if
    
} //end remove_file

// Reads from a file and outputs into a char array
// @param path the pathname of the file
// @return a chunk of the file in a char array
char* read_from_file(const char *path) {
    
    char *buffer;       // The buffer for the file
    FILE *fp;           // The pointer to the file
    long lSize;         // The length of the file
    int read;           // The number of bytes read
    
    // Try to open the file
    fp = fopen(path, "rb");
    
    // The file failed to opened
    if (!fp) {
        
        printf("Error opening file '%s'", path);
        return NULL;
        
    } //end if
    
    // The file successfully opened
    else {
        
        // Skip to the end of the file and check its length, allocate the character array memory
        // Skip to the end of the file and check its length
        fseek(fp, 0L, SEEK_END);
        lSize = ftell(fp);
        fseek(fp, 0L, SEEK_SET);
        
        buffer = malloc(lSize+1);
        
        // The memory couldn't be allocated
        if (!buffer) {
            
            fclose(fp);
            printf("Memory allocation failure\n");
            
        } //end if
        
        // The memory was successfully allocated
        else {
            
            // Check if the file read was successful
            read = fread(buffer, sizeof(char), lSize, fp);
            
            // Nothing was read from the file
            if (read == 0) {
                
                free(buffer);
                fclose(fp);
                
                return NULL;
                
            } //end if
            
            // Add null byte to buffer
            else {
                
                buffer[read] = '\0';
                
            } //end else
            
        } //end else
        
        fclose(fp);
        
    } //end else
    
    return buffer;
    
} //end read_from_file

// Writes a chunk of data to a file
// @param buffer the chunk of data
// @param path the pathname of the file
// @param length the length of the chunk being written
// @return the amount of bytes written to the file
int write_buffer_to_file(char *buffer, const char *path) {
    
    FILE *fp;           // The pointer to the file
    int length = strlen(buffer);
    
    // Try to open the file
    fp = fopen(path, "ab");
    
    // The file failed to opened
    if (!fp) {
        
        fprintf(stderr, "Error opening file '%s'", path);
        return -1;
        
    } //end if
    
    // The file successfully opened
    else {
        
        // Check if the file read was successful
        size_t bytes_written = fwrite(buffer, sizeof(char), length, fp);
        
        // Nothing was read from the file
        if (bytes_written != length) {
            
            fprintf(stderr, "Error writing to file");
            fclose(fp);
            
            return bytes_written;
            
        } //end if
        
        fclose(fp);
        
    } //end else
    
    return 1;
    
} //end write_buffer_to_file

// Outputs the build results to a file
// @param mongo_build_results the MongoDB build results
// @param maria_build_results the MariaDB build results
// @param num_trials the number of trials in the experiment
void output_build_results(uint64_t mongo_build_results[MAX_TRIALS][NUM_TABLES],
                            uint64_t maria_build_results[MAX_TRIALS][NUM_TABLES],
                            int num_trials) {
    
    char *results = "results.txt";
    char *mongooutput = malloc(2048);
    
    // Overwrite existing file
    write_new_file(results);
    strcpy(mongooutput, "MongoDB build results (in ms)\n");
    strcat(mongooutput, "Trial\t\tBooks\t\tAuthors\t\tPublishers\n");
    
    // Loop through the Trials
    for (int i = 0; i < num_trials; i++) {
        
        char *trial;
        asprintf(&trial, "%d\t\t", i+1);
        strcat(mongooutput, trial);
        free(trial);
        
        // Loop through the tables
        for (int j = 0; j < NUM_TABLES; j++) {
            
            char *data;
            asprintf(&data, "%lu\t\t", mongo_build_results[i][j]);
            strcat(mongooutput, data);
            free(data);
            
        } //end for
        
        strcat(mongooutput, "\n");
        
    } //end for
    
    write_buffer_to_file(mongooutput, results);
    free(mongooutput);
    
    char *mariaoutput = malloc(2048);
    
    strcpy(mariaoutput, "\nMariaDB build results (in ms)\n");
    strcat(mariaoutput, "Trial\t\tBooks\t\tAuthors\t\tPublishers\n");
    
    // Loop through the trials
    for (int i = 0; i < num_trials; i++) {
        
        char *trial;
        asprintf(&trial, "%d\t\t", i+1);
        strcat(mariaoutput, trial);
        free(trial);
        
        // Loop through the tables
        for (int j = 0; j < NUM_TABLES; j++) {
            
            char *data;
            asprintf(&data, "%lu\t\t", maria_build_results[i][j]);
            strcat(mariaoutput, data);
            free(data);
            
        } //end for
        
        strcat(mariaoutput, "\n");
        
    } //end for
    
    strcat(mariaoutput, "\n");
    write_buffer_to_file(mariaoutput, results);
    free(mariaoutput);
    
} //end output_build_results

// Outputs the query results to a file
// @param mongo_query_results the MongoDB query results
// @param maria_query_results the MariaDB query results
// @param num_trials the number of trials in the experiment
// @param num_selects the number of selects in the experiment
void output_query_results(uint64_t mongo_query_results[MAX_TRIALS][NUM_QUERIES],
                                 uint64_t maria_query_results[MAX_TRIALS][NUM_QUERIES],
                                 int num_trials, int num_selects) {
    
    char *results = "results.txt";
    char *mongooutput = malloc(2048);
    
    strcpy(mongooutput, "MongoDB query results (in ms), ");
    
    char *selects;
    asprintf(&selects, "Number of selects per trial: %d\n", num_selects);
    strcat(mongooutput, selects);
    
    strcat(mongooutput, "Trial\tUpdate1\tUpdate2\tUpdate3\tSelect1\tSelect2\tSelect3"
           "\tSelect4\tSelect5\tSelect6\tSelect7\tDelete1\tDelete2\tDelete3\n");
    
    // Loop through the Trials
    for (int i = 0; i < num_trials; i++) {
        
        char *trial;
        asprintf(&trial, "%d\t", i+1);
        strcat(mongooutput, trial);
        free(trial);
        
        // Loop through the tests
        for (int j = 0; j < NUM_QUERIES; j++) {
            
            char *data;
            asprintf(&data, "%lu\t", mongo_query_results[i][j]);
            strcat(mongooutput, data);
            free(data);
            
        } //end for
        
        strcat(mongooutput, "\n");
        
    } //end for
    
    write_buffer_to_file(mongooutput, results);
    free(mongooutput);
    
    char *mariaoutput = malloc(2048);
    
    strcpy(mariaoutput, "\nMariaDB query results (in ms), ");
    strcat(mongooutput, selects);
    free(selects);
    
    strcat(mariaoutput, "Trial\tUpdate1\tUpdate2\tUpdate3\tSelect1\tSelect2\tSelect3"
           "\tSelect4\tSelect5\tSelect6\tSelect7\tDelete1\tDelete2\tDelete3\n");
    
    // Loop through the trials
    for (int i = 0; i < num_trials; i++) {
        
        char *trial;
        asprintf(&trial, "%d\t", i+1);
        strcat(mariaoutput, trial);
        free(trial);
        
        
        // Loop through the tests
        for (int j = 0; j < NUM_QUERIES; j++) {
            
            char *data;
            asprintf(&data, "%lu\t", maria_query_results[i][j]);
            strcat(mariaoutput, data);
            free(data);
            
        } //end for
        
        strcat(mariaoutput, "\n");
        
    } //end for
    
    strcat(mariaoutput, "\n");
    write_buffer_to_file(mariaoutput, results);
    free(mariaoutput);
    
} //end output_query_results

// Output the number of affect rows for each query in the experiment
// @param affected_rows the number of affected rows in each query
// @param num_trials the number of trials in the experiment
void output_affected_rows(uint64_t affected_rows[MAX_TRIALS][NUM_QUERIES],
                                  int num_trials) {
    
    char *results = "results.txt";
    char *output = malloc(2048);
    
    strcpy(output, "Number of affected rows per query\n");
    strcat(output, "Trial\tUpdate1\tUpdate2\tUpdate3\tSelect1\tSelect2\tSelect3\t"
           "Select4\tSelect5\tSelect6\tSelect7\tDelete1\tDelete2\tDelete3\n");
    
    // Loop through the trials
    for (int i = 0; i < num_trials; i++) {
        
        char *trial;
        asprintf(&trial, "%d\t", i+1);
        strcat(output, trial);
        free(trial);
        
        // Loop through the tests
        for (int j = 0; j < NUM_QUERIES; j++) {
            
            char *data;
            asprintf(&data, "%lu\t", affected_rows[i][j]);
            strcat(output, data);
            free(data);
            
        } //end for
        
        strcat(output, "\n");
        
    } //end for
    
    write_buffer_to_file(output, results);
    free(output);
    
} //end output_affected_rows

// Outputs the results of the building phase to a file
// @param mongo_build_results MongoDB build results
// @param maria_build_results MariaDB build results
// @param num_trials the number of trials in the experiment
// @param filename the filename to output to
// @param table the number of tables in the experiment
void output_build_csv(uint64_t mongo_build_results[MAX_TRIALS][NUM_TABLES],
                             uint64_t maria_build_results[MAX_TRIALS][NUM_TABLES],
                             int num_trials, char *filename, int table) {
    
    write_new_file(filename);
    char *csv = malloc(128);
    strcpy(csv, "");
    
    // Write the trial numbers
    for (int i = 0; i < num_trials; i++) {
        
        char *trial;
        asprintf(&trial, ",%d", i+1);
        strcat(csv, trial);
        free(trial);
        
    } //end for
    
    strcat(csv, "\nMongoDB");
    
    // Write MongoDB's results
    for (int i = 0; i < num_trials; i++) {
        
        char *data;
        asprintf(&data, ",%lu", mongo_build_results[i][table]);
        strcat(csv, data);
        free(data);
        
    } //end for
    
    strcat(csv, "\nMariaDB");
    
    // Write MariaDB's results
    for (int i = 0; i < num_trials; i++) {
        
        char *data;
        asprintf(&data, ",%lu", maria_build_results[i][table]);
        strcat(csv, data);
        free(data);
        
    } //end for
    
    write_buffer_to_file(csv, filename);
    free(csv);
    
} //end output_build_csv

// Outputs the results of the query phase to a file
// @param mongo_query_results MongoDB query results
// @param maria_query_results MariaDB query results
// @param num_trials the number of trials in the experiment
// @param filename the filename to output to
// @param test the current test number
void output_query_csv(uint64_t mongo_query_results[MAX_TRIALS][NUM_QUERIES],
                             uint64_t maria_query_results[MAX_TRIALS][NUM_QUERIES],
                             int num_trials, char *filename, int test) {
    
    write_new_file(filename);
    char *csv = malloc(128);
    strcpy(csv, "");
    
    // Write the trial numbers
    for (int i = 0; i < num_trials; i++) {
        
        char *trial;
        asprintf(&trial, ",%d", i+1);
        strcat(csv, trial);
        free(trial);
        
    } //end for
    
    strcat(csv, "\nMongoDB");
    
    // Write MongoDB's results
    for (int i = 0; i < num_trials; i++) {
        
        char *data;
        asprintf(&data, ",%lu", mongo_query_results[i][test]);
        strcat(csv, data);
        free(data);
        
    } //end for
    
    strcat(csv, "\nMariaDB");
    
    // Write MariaDB's results
    for (int i = 0; i < num_trials; i++) {
        
        char *data;
        asprintf(&data, ",%lu", maria_query_results[i][test]);
        strcat(csv, data);
        free(data);
        
    } //end for
    
    write_buffer_to_file(csv, filename);
    free(csv);
    
} //end output_query_csv
