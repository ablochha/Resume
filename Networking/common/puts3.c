/*
 * puts3.c
 *
 * Computer Science 3357a - Fall 2015
 * Author: Andrew Bloch-Hansen
 *
 * Puts an object into an S3 bucket.
 */

#include <libs3.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <syslog.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <unistd.h>

#include "puts3.h"

#define ACCESSKEYID "AKIAJEZNDQQR54H2AGOQ"
#define SECRETACCESSKEYID "rAOdIKyvhhy+2MGQbzi7V/qJnHZcUwbNrtm8elL8"
#define BUCKET "ablochha-hooli"

// Status of putting the object into the bucket
static int statusG = 0;

// Displays any errors after attempting to add an object
// @param status the status of the put
// @param error the error details
// @param callbackdata the callbackdata
static void responseCompleteCallback(S3Status status,
                                     const S3ErrorDetails *error,
                                     void *callbackData) {
    
    int i;
    
    statusG = status;
    
    if (error && error->message) {
        
        syslog(LOG_ERR, "Message: %s", error->message);
        
    } //end if
    
    if (error && error->resource) {
        
        syslog(LOG_ERR, "Resource: %s", error->resource);
        
    } //end if
    
    if (error && error->furtherDetails) {
        
        syslog(LOG_ERR, "Further Details: %s", error->furtherDetails);
        
    } //end if
    
    if (error && error->extraDetailsCount) {
        
        syslog(LOG_ERR, "Extra Details:");
        
        for (i = 0; i < error->extraDetailsCount; i++) {
            
            syslog(LOG_ERR, "%s: %s", error->extraDetails[i].name, error->extraDetails[i].value);
            
        } //end for
        
    } //end if
    
} //end responseCompleteCallback

// The properties of the response
// @param properties the response properties
// @param callbackData the callback data
// @return the appropriate status code
static S3Status responsePropertiesCallback(const S3ResponseProperties *properties,
                                           void *callbackData) {
    
    return S3StatusOK;
    
} //end responsePropertiesCallback

// The amount of data put into the bucket
// @param bufferSize the size of the buffer
// @param buffer the buffer
// @param callbackData the callback data
// @return the amount of data being put into the bucket
static int putObjectDataCallback(int bufferSize, char *buffer,
                                 void *callbackData) {
    
    put_object_callback_data *data = (put_object_callback_data*) callbackData;
    
    int ret = 0;
    
    if (data->contentLength) {
        
        int length = ((data->contentLength > (unsigned)bufferSize) ?
                      (unsigned)bufferSize : data->contentLength);
        
        ret = fread(buffer, 1, length, data->infile);
        
    } //end if
    
    data->contentLength -= ret;
    return ret;
    
} //end putObjectDataCallback

// Adds an object into an S3 bucket
// @param fileName the name of the file
// @param s3ObjName the name of the object in the bucket
// @return 0 if the file was successfully uploaded, -1 otherwise
int putFileIntoS3(char *fileName, char *s3ObjName) {
    
    S3Status status;                // The status of the put
    struct stat statBuf;            // Buffer for the input file
    uint64_t fileSize;              // The size of the file
    FILE *fd;                       // A pointer to the file
    put_object_callback_data data;  // Object data
    
    char *accessKeyId = ACCESSKEYID;            // "username"
    char *secretAccessKey = SECRETACCESSKEYID;  // "password"
    
    char ch[1];
    strncpy(ch, s3ObjName, 1);
    ch[1] = '\0';
    
    // Don't make an S3 folder with "" as a name
    if (strcmp(ch, "/") == 0) {
        
        s3ObjName++;
        
    } //end if
    
    // Make sure the file exists
    if (stat(fileName, &statBuf) == -1) {
        
        syslog(LOG_ERR, "Unknown input file");
        return -1;
        
    } //end if
    
    fileSize = statBuf.st_size;
    fd = fopen( fileName, "r" );
    
    // Make sure the file was opened
    if (fd == NULL) {
        
        syslog(LOG_ERR, "Unable to open input file");
        return -1;
        
    } //end if
    
    data.infile = fd;
    
    // Use ablochha's credentials
    S3BucketContext bucketContext = {NULL, BUCKET, 1, 0, accessKeyId, secretAccessKey};
    S3PutObjectHandler putObjectHandler = {{&responsePropertiesCallback, &responseCompleteCallback},
                                            &putObjectDataCallback};
    
    // Attempt to initialize the S3 library
    if ((status = S3_initialize(NULL, S3_INIT_ALL, NULL)) != S3StatusOK) {
        
        syslog(LOG_ERR, "Failed to initialize libs3: %s\n", S3_get_status_name(status));
        return -1;
        
    } //end if
    
    S3_put_object(&bucketContext, s3ObjName, fileSize, NULL, 0, &putObjectHandler, &data);
    
    // The put failed
    if (statusG != S3StatusOK) {
        
        syslog(LOG_ERR, "Put failed: %s\n", S3_get_status_name(statusG));
        S3_deinitialize();
        return -1;
        
    } //end if
    
    S3_deinitialize();
    
    fclose(fd);
    return 0;
    
} //end putFileIntoS3