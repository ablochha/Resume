/*
 * mariaqueries.c
 *
 * Computer Science 4411
 * Author: Andrew Bloch-Hansen
 *
 * Interfaces with MariaDB to run queries.
 */

#include <stdint.h>
#include <stdio.h>
#include <string.h>
#include <time.h>
#include <my_global.h>
#include <mysql.h>

#include "../common/timer.h"
#include "mariaqueries.h"

#define MAX_TRIALS 5
#define NUM_QUERIES 13

// Sends an update request to MariaDB
// @param dbname the name of the database
// @param query the query to send
// @param count the number of affected rows
// @return the time it took
static uint64_t run_maria_update(char *dbname, char *query, uint64_t *count) {
    
    struct timespec start;          // The time structure
    uint64_t time;                  // The elapsed time
    
    MYSQL *con = mysql_init(NULL);  // Mariadb connection
    
    // Make sure we can connect
    if (con == NULL) {
        
        fprintf(stderr, "%s\n", mysql_error(con));
        exit(1);
        
    } //end if
    
    // Try to connect to a database
    if (mysql_real_connect(con, "localhost", "root", "proxymine", dbname, 0,
                           "/run/shm/mysqld.sock", 0) == NULL) {
        
        fprintf(stderr, "%s\n", mysql_error(con));
        mysql_close(con);
        exit(1);
        
    } //end if
    
    start_timer(&start);
    
    // Try to update the table
    if (mysql_real_query(con, query, strlen(query))) {
        
        fprintf(stderr, "%s\n", mysql_error(con));
        mysql_close(con);
        exit(1);
        
    } //end if
    
    time = stop_timer(start);
    
    *count = mysql_affected_rows(con);
    
    mysql_close(con);
    
    return time / (CLOCKS_PER_SEC / 1000);
    
} //end run_maria_updates

// Sends an update request to MariaDB
// @param dbname the name of the database
// @param query the query to send
// @param count the number of affected rows
// @return the time it took
static uint64_t run_maria_selects(char *dbname, char *query, int num_selects,
                                  uint64_t *count) {
    
    struct timespec start;          // The time structure
    uint64_t time;                  // The elapsed time
    
    MYSQL *con = mysql_init(NULL);  // Mariadb connection
    MYSQL_RES *result;
    
    // Make sure we can connect
    if (con == NULL) {
        
        fprintf(stderr, "%s\n", mysql_error(con));
        exit(1);
        
    } //end if
    
    // Try to connect to a database
    if (mysql_real_connect(con, "localhost", "root", "proxymine", dbname, 0,
                           "/run/shm/mysqld.sock", 0) == NULL) {
        
        fprintf(stderr, "%s\n", mysql_error(con));
        mysql_close(con);
        exit(1);
        
    } //end if
    
    start_timer(&start);
    
    // Perform the select 'num_select' times
    for (int i = 0; i < num_selects; i++) {
        
        // Try to perform the select
        if (mysql_real_query(con, query, strlen(query))) {
            
            fprintf(stderr, "%s\n", mysql_error(con));
            mysql_close(con);
            exit(1);
            
        } //end if
        
        result = mysql_use_result(con);
        
        MYSQL_ROW row;
        
        // Loop through the results
        while ((row = mysql_fetch_row(result))) {
            
        } //end while
        
        mysql_free_result(result);
        
    } //end for
    
    time = stop_timer(start);
    
    *count = mysql_affected_rows(con);
    
    mysql_close(con);
    
    return time / (CLOCKS_PER_SEC / 1000);
    
} //end run_maria_selects

// Runs a series of queries on the database
// @param dbname the name of the database
// @param trial the number of trials in the experiment
// @param num_selects the number of selects to perform
// @param maria_query_results the query results
// @param affected_rows the number of affected rows
void run_maria_queries(char *dbname, int trial, int num_selects,
                       uint64_t maria_query_results[MAX_TRIALS][NUM_QUERIES],
                       uint64_t affected_rows[MAX_TRIALS][NUM_QUERIES]) {
    
    uint64_t count = 0; // The affected row count
    
    char *query;        // The query to send
    
    printf("Performing MariaDB queries trial %d...", trial+1);
    fflush(NULL);
    
    asprintf(&query, "SELECT * FROM books WHERE isbn = 9780802755513");
    maria_query_results[trial][3] = run_maria_selects(dbname, query, num_selects, &count);
    affected_rows[trial][3] = count;
    free(query);
    
    asprintf(&query, "SELECT * FROM authors WHERE name = 'Joey Goebel'");
    maria_query_results[trial][4] = run_maria_selects(dbname, query, num_selects, &count);
    affected_rows[trial][4] = count;
    free(query);
    
    asprintf(&query, "SELECT * FROM publishers WHERE name = 'Meisha Merlin'");
    maria_query_results[trial][5] = run_maria_selects(dbname, query, num_selects, &count);
    affected_rows[trial][5] = count;
    free(query);
    
    asprintf(&query, "SELECT * FROM authors WHERE dob < 1950");
    maria_query_results[trial][6] = run_maria_selects(dbname, query, num_selects, &count);
    affected_rows[trial][6] = count;
    free(query);
    
    asprintf(&query, "SELECT * FROM authors LEFT OUTER JOIN books ON authors.name = books.author");
    maria_query_results[trial][7] = run_maria_selects(dbname, query, num_selects, &count);
    affected_rows[trial][7] = count;
    free(query);
    
    asprintf(&query, "SELECT * FROM books LEFT OUTER JOIN publishers ON books.publisher_id = publishers.isfdb");
    maria_query_results[trial][8] = run_maria_selects(dbname, query, num_selects, &count);
    affected_rows[trial][8] = count;
    free(query);
    
    asprintf(&query, "SELECT nationality, AVG(dob) as AvgDOB FROM authors GROUP BY nationality");
    maria_query_results[trial][9] = run_maria_selects(dbname, query, num_selects, &count);
    affected_rows[trial][9] = count;
    free(query);
    
    asprintf(&query, "UPDATE books SET name = 'Andrew' WHERE name < 'M'");
    maria_query_results[trial][0] = run_maria_update(dbname, query, &count);
    affected_rows[trial][0] = count;
    free(query);
    
    asprintf(&query, "UPDATE authors SET nationality = 'Russia' WHERE dob < 1950");
    maria_query_results[trial][1] = run_maria_update(dbname, query, &count);
    affected_rows[trial][1] = count;
    free(query);
    
    asprintf(&query, "UPDATE publishers SET name = 'The Daily Planet' WHERE isfdb > 130");
    maria_query_results[trial][2] = run_maria_update(dbname, query, &count);
    affected_rows[trial][2] = count;
    free(query);
    
    asprintf(&query, "DELETE FROM books WHERE name = 'Andrew'");
    maria_query_results[trial][10] = run_maria_update(dbname, query, &count);
    affected_rows[trial][10] = count;
    free(query);
    
    asprintf(&query, "DELETE FROM authors WHERE nationality = 'Russia'");
    maria_query_results[trial][11] = run_maria_update(dbname, query, &count);
    affected_rows[trial][11] = count;
    free(query);
    
    asprintf(&query, "DELETE FROM publishers WHERE name = 'The Daily Planet'");
    maria_query_results[trial][12] = run_maria_update(dbname, query, &count);
    affected_rows[trial][12] = count;
    free(query);
    
    printf("Done\n");
    
} //end run_maria_queries
