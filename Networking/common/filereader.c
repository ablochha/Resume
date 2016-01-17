/*
 * filereader.c
 *
 * Computer Science 3357a - Fall 2015
 * Author: Andrew Bloch-Hansen
 *
 * Reads and writes chunks of data to files.
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/stat.h>
#include <syslog.h>
#include <unistd.h>

#include "filereader.h"

#define SLASH "/"

// Reads from a file and outputs into a char array
// @param path the pathname of the file
// @param offset the position to start reading from
// @param read the amount of bytes read
// @return a chunk of the file in a char array
char* read_from_file(const char *path, long offset, int n, int *read) {
    
    char *buffer;       // The buffer for the file
    FILE *fp;           // The pointer to the file
    
    // Try to open the file
    fp = fopen(path, "rb");
    
    // The file failed to opened
    if (!fp) {
        
        syslog(LOG_ERR, "Error opening file '%s'", path);
        return NULL;
        
    } //end if
    
    // The file successfully opened
    else {
        
        // Skip to the end of the file and check its length, allocate the character array memory
        fseek(fp, offset, SEEK_SET);
        
        buffer = malloc(n+1);
        
        // The memory couldn't be allocated
        if (!buffer) {
            
            fclose(fp);
            syslog(LOG_ERR, "Mempory allocation failure\n");
            
        } //end if
        
        // The memory was successfully allocated
        else {
            
            // Check if the file read was successful
            *read = fread(buffer, sizeof(char), n, fp);
            
            // Nothing was read from the file
            if (*read == 0) {
                
                free(buffer);
                fclose(fp);
                
                return NULL;
                
            } //end if
            
            // Add null byte to buffer
            else {
                
                buffer[*read] = '\0';
                
            } //end else
            
        } //end else
        
        fclose(fp);
        
    } //end else
    
    return buffer;
    
} //end read_from_file

// Computes the length of a file in bytes
// @param path the pathname of the file
// @return the length of the file
int get_file_length(const char *path) {
    
    FILE *fp;           // The pointer to the file
    int lSize;          // The size of the file
    
    // Try to open the file
    fp = fopen(path, "rb");
    
    // The file failed to opened
    if (!fp) {
        
        syslog(LOG_ERR, "Error opening file '%s'", path);
        return -1;
        
    } //end if
    
    // The file successfully opened
    else {
        
        // Skip to the end of the file and check its length
        fseek(fp, 0L, SEEK_END);
        lSize = ftell(fp);
        fclose(fp);
        
    } //end else

    return lSize;
    
} //end get_file_length

// Makes a new file with the specified name, overwrites previous file
// @param path the pathname of the file
// @return 1 if the file was created, -1 otherwise
int write_new_file(const char *path) {
    
    FILE *fp;           // The pointer to the file
    
    // Try to open the file
    fp = fopen(path, "wb");
    
    // The file failed to opened
    if (!fp) {
        
        syslog(LOG_ERR, "Error opening file '%s'", path);
        return -1;
        
    } //end if
    
    fclose(fp);
    return 1;
    
} //end create_new_file

// Writes a chunk of data to a file
// @param buffer the chunk of data
// @param path the pathname of the file
// @param length the length of the chunk being written
// @return the amount of bytes written to the file
int write_buffer_to_file(const char *buffer, const char *path, int length) {
 
    FILE *fp;           // The pointer to the file
 
    // Try to open the file
    fp = fopen(path, "ab");
 
    // The file failed to opened
    if (!fp) {
 
        syslog(LOG_ERR, "Error opening file '%s'", path);
        return -1;
 
    } //end if
    
    // The file successfully opened
    else {
            
        // Check if the file read was successful
        size_t bytes_written = fwrite(buffer, sizeof(char), length, fp);
            
        // Nothing was read from the file
        if (bytes_written != length) {
                
            syslog(LOG_ERR, "Error writing to file");
            fclose(fp);
            
            return bytes_written;
                
        } //end if
        
        fclose(fp);
        
    } //end else
 
    return 1;
 
} //end write_buffer_to_file

// Deletes a file
// @param path the path to the file
void remove_file(const char *path) {
    
    int ret = remove(path);
    
    if (ret != 0) {
        
        syslog(LOG_ERR, "Unable to remove file '%s'", path);
        
    } //end if
    
} //end remove_file

// Makes a new directory with the specified name
// @param dir the directory name
static void make_directory(const char *dir) {
    
    struct stat st = {0};   // Displays directory status
    
    // If the directory doesn't exist, create a new one
    if (stat(dir, &st) == -1) {
        
        mkdir(dir, 0777);
        
    } //end if
    
} //end make_directory

// Makes all the directories required to make the pathname
// @param dir the directory name
void make_all_directories(const char *dir) {
    
    char *sub_dir;          // A subdirectory in the pathname
    char *ch = malloc(2);   // Looking for '/' to find subdirectories
    
    int pos = 1;
    
    // Loop through the full directory path
    while (pos < strlen(dir)) {
        
        strncpy(ch, dir + pos, 1);
        ch[1] = '\0';
        
        // Create subdirectories as neccesary
        if (strcmp(ch, SLASH) == 0) {
            
            sub_dir = malloc(pos + 2);
            strncpy(sub_dir, dir, pos + 1);
            sub_dir[pos+1] = '\0';
            make_directory(sub_dir);
            free(sub_dir);
            
        } //end if
        
        pos++;
        
    } //end while
    
    free(ch);
    
} //end make_all_directories
