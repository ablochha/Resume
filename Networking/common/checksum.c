/*
 * checksum.c
 *
 * Computer Science 3357a - Fall 2015
 * Author: Andrew Bloch-Hansen
 *
 * Computes the crc32 checksum of a file, and converts a checksum between integer
 * hexadecimal form.
 */

#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <zlib.h>

#include "checksum.h"
#include "filereader.h"

#define CHUNK_SIZE 0.25

// Computes the checksum of a file
// @param path a character array containing a pathname for a file
// @return the checksum, or NULL if the file wasn't opened
char* compute_crc(const char *path) {
    
    int file_size = get_file_length(path);  // The total file size
    int chunksize = file_size * CHUNK_SIZE; // The max size of a chunk to read
    int index = 0;                          // The offset for opening the file
    int read = 0;                           // The amount of bytes read
    
    uLong crc = crc32(0L, Z_NULL, 0);       // Holds the checksum value
    
    char *buffer = read_from_file(path, index, chunksize, &read);
    
    // Read everything from the file
    while (buffer != NULL) {
        
        crc = crc32(crc, (const Bytef*)buffer, read);
        index += chunksize;
        free(buffer);
        
        buffer = read_from_file(path, index, chunksize, &read);
        
    } //end while
    
    return checksum_to_char(crc);
    
} //end compute_crc

// Converts a checksum in hexadecimal form (in ASCII) to an int
// @param checksum the ASCII checksum
// @return the int checksum
uint32_t checksum_to_int(char *checksum) {
    
    return (uint32_t)strtoul(checksum, NULL, 16);
    
} //end checksum_to_int

// Converts a checksum in integer form to hexadecimal (in ASCII)
// @param checksum the integer checksum
// @return the hexadecimal checksum
char* checksum_to_char(uint32_t checksum) {
    
    char *retchecksum;
    asprintf(&retchecksum, "%X", checksum);
    return retchecksum;
    
} //end checksum_to_char
