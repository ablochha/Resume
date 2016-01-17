#ifndef PUTS3_H
#define PUTS3_H

#include <stdio.h>
#include <stdint.h>

typedef struct put_object_callback_data {
    
    FILE *infile;
    uint64_t contentLength, originalContentLength;
    
} put_object_callback_data;

// Puts a file into the S3 bucket
int putFileIntoS3(char *fileName, char *s3ObjName);

#endif
