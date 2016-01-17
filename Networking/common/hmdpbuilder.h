#ifndef HMDPBUILDER_H
#define HMDPBUILDER_H

#include "linkedlist.h"

// Adds a command to a HMDP message
void add_command(char **buffer, char *command);

// Reads a command from a HMDP message
char* read_command(char **buffer);

// Adds a key to a HMDP message
void add_key(char **buffer, char *key);

// Reads a key from a HMDP message
char* read_key(char **buffer);

// Adds a value to a HMDP message
void add_value(char **buffer, char *value);

// Reads a value from a HMDP message
char* read_value(char **buffer);

// Adds an extra newline to a HMDP message
void delimit_header(char **buffer);

// Adds the body to a HMDP message
void add_body(char **buffer, csv_record *list);

// Reads the body from a HMDP message
csv_record* read_body(char **buffer);

// Adds a status to a HMDP message
void add_status(char **buffer, char *status);

// Reads a status from a HMDP message
char* read_status(char **buffer);

// Adds a message to a HMDP message
void add_message(char **buffer, char *message);

// Reads a message from a HMDP message
char* read_message(char **buffer);

#endif
