#ifndef buildmongo_h
#define buildmongo_h

#include <stdint.h>

#include "cJSON.h"

// Drop the database
void drop_database_mongo(char *dbname);

// Insert the data into the MongoDB books table
uint64_t insert_books_mongo(cJSON *root, char *dbname);

// Insert the data into the MongoDB authors table
uint64_t insert_authors_mongo(cJSON *root, char *dbname);

// Insert the data into the MongoDB authors table
uint64_t insert_publishers_mongo(cJSON *root, char *dbname);

#endif
