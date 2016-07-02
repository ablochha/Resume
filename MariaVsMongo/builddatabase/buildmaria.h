#ifndef buildmaria_h
#define buildmaria_h

#include <stdint.h>

#include "cJSON.h"

// Drop the database
void drop_database_maria(char *dbname);

// Insert the data into the MariaDB books table
uint64_t insert_books_maria(cJSON *root, char *dbname);

// Insert the data into the MariaDB authors table
uint64_t insert_authors_maria(cJSON *root, char *dbname);

// Insert the data into the MariaDB publishers table
uint64_t insert_publishers_maria(cJSON *root, char *dbname);

#endif
