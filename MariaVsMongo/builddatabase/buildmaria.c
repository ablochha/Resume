/*
 * buildmaria.c
 *
 * Computer Science 4411
 * Author: Andrew Bloch-Hansen
 *
 * Iterates through the JSON and inserts the data into the MariaDB database.
 */

#include <stdint.h>
#include <stdio.h>
#include <string.h>
#include <my_global.h>
#include <mysql.h>

#include "../common/filereader.h"
#include "../common/timer.h"
#include "buildmaria.h"
#include "cJSON.h"

// Drop the database
// @param dbname the name of the database
void drop_database_maria(char *dbname) {
    
    MYSQL *con = mysql_init(NULL);  // The MariaDB connection
    char *query;                    // The query
    
    // Make sure we can connect
    if (con == NULL) {
        
        fprintf(stderr, "%s\n", mysql_error(con));
        exit(1);
        
    } //end if
    
    // Try to connect to a database
    if (mysql_real_connect(con, "localhost", "root", "proxymine", NULL, 0,
                           "/run/shm/mysqld.sock", 0) == NULL) {
        
        fprintf(stderr, "%s\n", mysql_error(con));
        mysql_close(con);
        exit(1);
        
    } //end if
    
    asprintf(&query, "DROP DATABASE IF EXISTS %s", dbname);
    
    // Try to drop the database
    if (mysql_real_query(con, query, strlen(query))) {
        
        fprintf(stderr, "%s\n", mysql_error(con));
        mysql_close(con);
        exit(1);
        
    } //end if
    
    free(query);
    asprintf(&query, "CREATE DATABASE %s", dbname);
    
    // Try to create the database
    if (mysql_real_query(con, query, strlen(query))) {
        
        fprintf(stderr, "%s\n", mysql_error(con));
        mysql_close(con);
        exit(1);
        
    } //end if

    free(query);
    mysql_close(con);
    
} //end drop_database

// Insert the data into the MariaDB books table
// @param root the first item in the JSON
// @param dbname the name of the database
// @return the total time of the insert
uint64_t insert_books_maria(cJSON *root, char *dbname) {
    
    struct timespec start;                  // Time structure to get current time
    uint64_t time = 0;                      // Test result
    
    MYSQL *con = mysql_init(NULL);          // The MariaDB connection
    char *query;                            // The query
    
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
    
    asprintf(&query, "CREATE TABLE books (isbn bigint not null, name varchar(20),"
             "author varchar(20), publisher_id int, PRIMARY KEY(isbn),"
             "FOREIGN KEY(author) REFERENCES authors(name) ON DELETE CASCADE ON "
             "UPDATE CASCADE, FOREIGN KEY(publisher_id) REFERENCES publishers(isfdb)"
             "ON DELETE CASCADE ON UPDATE CASCADE) ENGINE=MyISAM DEFAULT CHARSET=utf8");
    
    // Try to create the table
    if (mysql_real_query(con, query, strlen(query))) {
        
        fprintf(stderr, "%s\n", mysql_error(con));
        mysql_close(con);
        exit(1);
        
    } //end if
    
    free(query);
    
    cJSON *name = NULL;
    cJSON *credited_to = NULL;
    cJSON *isbn = NULL;
    cJSON *publisher = NULL;
    cJSON *isfdb_id = NULL;
    
    cJSON *item = cJSON_GetObjectItem(root, "result");
    
    // Loop through all the items in the JSON file
    for (int i = 0 ; i < cJSON_GetArraySize(item); i++) {
        
        // Find the items I want
        cJSON * subitem = cJSON_GetArrayItem(item, i);
        name = cJSON_GetObjectItem(subitem, "name");
        credited_to = cJSON_GetObjectItem(subitem, "credited_to");
        isbn = cJSON_GetObjectItem(subitem, "isbn");
        publisher = cJSON_GetObjectItem(subitem, "publisher");
        isfdb_id = cJSON_GetObjectItem(publisher, "isfdb_id");
        
        // Get the values
        char *str_name = name->valuestring;
        char *str_author = cJSON_GetObjectItem(credited_to, "value")->valuestring;
        char *str_isbn = cJSON_GetObjectItem(isbn, "isbn")->valuestring;
        char *str_isfdb = cJSON_GetObjectItem(isfdb_id, "value")->valuestring;
        
        // Only accept data will complete fields
        if (str_name != NULL && str_author != NULL && str_isbn != NULL && str_isfdb != NULL) {
            
            // Buffer for safe strings
            char *safe_isbn = malloc((strlen(str_isbn)*2)+1);
            char *safe_name = malloc((strlen(str_name)*2)+1);
            char *safe_author = malloc((strlen(str_author)*2)+1);
            char *safe_isfdb = malloc((strlen(str_isfdb)*2)+1);
            
            // Get safe strings
            mysql_real_escape_string(con, safe_isbn, str_isbn, strlen(str_isbn));
            mysql_real_escape_string(con, safe_name, str_name, strlen(str_name));
            mysql_real_escape_string(con, safe_author, str_author, strlen(str_author));
            mysql_real_escape_string(con, safe_isfdb, str_isfdb, strlen(str_isfdb));
            
            asprintf(&query, "INSERT INTO books values (\"%s\", \"%s\", \"%s\", \"%s\")", safe_isbn, safe_name,
                     safe_author, safe_isfdb);
            
            start_timer(&start);
            
            if (mysql_real_query(con, query, strlen(query))) {
                
            } //end if
            
            time += stop_timer(start);
            
            free(query);
            free(safe_isbn);
            free(safe_name);
            free(safe_author);
            free(safe_isfdb);
        
        } //end if
        
    } //end for
    
    mysql_close(con);
    
    return time / (CLOCKS_PER_SEC / 1000);
    
} //end insert_books_maria

// Insert the data into the MariaDB authors table
// @param root the first item in the JSON
// @param dbname the name of the database
// @return the total time of the insert
uint64_t insert_authors_maria(cJSON *root, char *dbname) {
    
    struct timespec start;                  // Time structure to get current time
    uint64_t time = 0;                      // Test result
    
    MYSQL *con = mysql_init(NULL);          // The MariaDB connection
    char *query;                            // The query
    
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
    
    asprintf(&query, "CREATE TABLE authors (name varchar(20) not null, dob int,"
             "nationality varchar(20), gender varchar(10), PRIMARY KEY(name))"
             "ENGINE=MyISAM DEFAULT CHARSET=utf8");
    
    // Try to create the table
    if (mysql_real_query(con, query, strlen(query))) {
        
        fprintf(stderr, "%s\n", mysql_error(con));
        mysql_close(con);
        exit(1);
        
    } //end if
    
    free(query);
    
    cJSON *name = NULL;
    cJSON *dob = NULL;
    cJSON *gender = NULL;
    cJSON *nationality = NULL;
    
    cJSON *item = cJSON_GetObjectItem(root, "result");
    
    // Loop through all the items in the JSON file
    for (int i = 0 ; i < cJSON_GetArraySize(item) ; i++) {
       
        // Get the items I want
        cJSON * subitem = cJSON_GetArrayItem(item, i);
        name = cJSON_GetObjectItem(subitem, "name");
        dob = cJSON_GetObjectItem(subitem, "/people/person/date_of_birth");
        nationality = cJSON_GetObjectItem(subitem, "/people/person/nationality");
        gender = cJSON_GetObjectItem(subitem, "/people/person/gender");
        
        // Get the values
        char *str_name = name->valuestring;
        char *str_dob = cJSON_GetObjectItem(dob, "value")->valuestring;
        char *str_nationality = cJSON_GetObjectItem(nationality, "name")->valuestring;
        char *str_gender = cJSON_GetObjectItem(gender, "name")->valuestring;
        str_dob[4] = '\0';
        
        // Only accept data with complete fields
        if (str_name != NULL && str_dob != NULL && str_nationality != NULL &&
            str_gender != NULL) {
            
            // Buffer for safe strings
            char *safe_name = malloc((strlen(str_name)*2)+1);
            char *safe_dob = malloc((strlen(str_dob)*2)+1);
            char *safe_nationality = malloc((strlen(str_nationality)*2)+1);
            char *safe_gender = malloc((strlen(str_gender)*2)+1);
            
            // Get safe strings
            mysql_real_escape_string(con, safe_name, str_name, strlen(str_name));
            mysql_real_escape_string(con, safe_dob, str_dob, strlen(str_dob));
            mysql_real_escape_string(con, safe_nationality, str_nationality, strlen(str_nationality));
            mysql_real_escape_string(con, safe_gender, str_gender, strlen(str_gender));
            
            asprintf(&query, "INSERT into authors VALUES (\"%s\", \"%s\", \"%s\", \"%s\")", safe_name, safe_dob,
                     safe_nationality, safe_gender);
            
            start_timer(&start);
            
            if (mysql_real_query(con, query, strlen(query))) {
                
            } //end if
            
            time += stop_timer(start);
            
            free(query);
            free(safe_name);
            free(safe_dob);
            free(safe_nationality);
            free(safe_gender);
            
        } //end if
        
    } //end for
    
    mysql_close(con);
    
    return time / (CLOCKS_PER_SEC / 1000);
    
} //end insert_authors_maria

// Insert the data into the MariaDB publishers table
// @param root the first item in the JSON
// @param dbname the name of the database
// @return the total time of the insert
uint64_t insert_publishers_maria(cJSON *root, char *dbname) {
    
    struct timespec start;                      // Time structure to get current time
    uint64_t time = 0;                          // Test result
    
    MYSQL *con = mysql_init(NULL);              // The MariaDB connection
    char *query;                                // The query
    
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
    
    asprintf(&query, "CREATE TABLE publishers (isfdb int not null, name varchar(20),"
             "PRIMARY KEY(isfdb)) ENGINE=MyISAM DEFAULT CHARSET=utf8");
    
    // Try to create the table
    if (mysql_real_query(con, query, strlen(query))) {
        
        fprintf(stderr, "%s\n", mysql_error(con));
        mysql_close(con);
        exit(1);
        
    } //end if
    
    free(query);
    
    cJSON *name = NULL;
    cJSON *isfdb_id = NULL;
    
    cJSON *item = cJSON_GetObjectItem(root, "result");
    
    // Loop through all the items in the JSON file
    for (int i = 0 ; i < cJSON_GetArraySize(item) ; i++) {
        
        // Get the items I want
        cJSON * subitem = cJSON_GetArrayItem(item, i);
        name = cJSON_GetObjectItem(subitem, "name");
        isfdb_id = cJSON_GetObjectItem(subitem, "isfdb_id");
        
        // Get the values
        char *str_name = name->valuestring;
        char *str_isfdb = cJSON_GetObjectItem(isfdb_id, "value")->valuestring;
        
        // Only accept data with complete fields
        if (str_name != NULL && str_isfdb != NULL) {
            
            // Buffer for safe strings
            char *safe_isfdb = malloc((strlen(str_isfdb)*2)+1);
            char *safe_name = malloc((strlen(str_name)*2)+1);
            
            // Get safe strings
            mysql_real_escape_string(con, safe_isfdb, str_isfdb, strlen(str_isfdb));
            mysql_real_escape_string(con, safe_name, str_name, strlen(str_name));
            
            asprintf(&query, "INSERT INTO publishers VALUES (\"%s\", \"%s\")", safe_isfdb, safe_name);
            
            start_timer(&start);
            
            if (mysql_real_query(con, query, strlen(query))) {
                
            } //end if
            
            time += stop_timer(start);
            
            free(query);
            free(safe_isfdb);
            free(safe_name);
            
        } //end if
        
    } //end for
    
    mysql_close(con);
    
    return time / (CLOCKS_PER_SEC / 1000);
    
} //end insert_publishers_maria
