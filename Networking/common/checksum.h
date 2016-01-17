#ifndef CHECKSUM_H
#define CHECKSUM_H

#include <stdint.h>

// Computes the checksum of a file
char* compute_crc(const char *buffer);

// Converts a checksum in hexadecimal form (in ASCII) to an int
uint32_t checksum_to_int(char *hex);

// Converts a checksum in integer form to hexadecimal (in ASCII)
char* checksum_to_char(uint32_t hex);

#endif
