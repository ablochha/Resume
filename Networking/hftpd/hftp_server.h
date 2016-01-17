#ifndef HFTP_SERVER_H
#define HFTP_SERVER_H

// Information about a file being received
typedef struct {
    
    char *username;
    char *filename;
    char *path;
    char *checksum;
    char *file_buffer;
    int filename_length;
    int file_size;
    int file_received_size;
    int file_buffer_size;
    int transfer_threshold;
    int packets_in_buffer;
    double percent;
    
} file_info;

// Reads a request from a client
int handle_request(int sockfd, char *server, char *root, int timewait, int *expected_sequence, file_info **file);

#endif
