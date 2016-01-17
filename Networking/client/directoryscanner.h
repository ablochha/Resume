#ifndef DIRECTORYSCANNER_H
#define DIRECTORYSCANNER_H

#include "../common/linkedlist.h"

// Returns a linked list of all the files in the Hooli directory
csv_record* get_files(const char *dir_name);

#endif 
