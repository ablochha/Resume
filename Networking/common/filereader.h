#ifndef FILEREADER_H
#define FILEREADER_H

// Reads from a file and outputs into a char array
char* read_from_file(const char *path, long offset, int n, int *read);

// Computes the length of a file in bytes
int get_file_length(const char *path);

// Makes a new file with the specified name, overwrites previous file
int write_new_file(const char *path);

// Writes a chunk of data to a file
int write_buffer_to_file(const char *buffer, const char *path, int length);

// Deletes a file
void remove_file(const char *path);

// Makes all the directories required to make the pathname
void make_all_directories(const char *dir);

#endif 
