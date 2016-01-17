#ifndef HFTP_CLIENT_H
#define HFTP_CLIENT_H

#include <stdbool.h>

#include "../common/client_structs.h"
#include "../common/linkedlist.h"
#include "../common/udp_sockets.h"

// Information for file transfer
typedef struct {
    
    bool file_transferred;
    char *path;
    char *file_buffer;
    char *pos;
    int file_count;
    int file_length;
    int bytes_sent;
    int file_buffer_size;
    int buffer_length;
    int packets_in_buffer;
    int transfer_threshold;
    double percent;
    
} file_info;

// Sends the requested files to the Hooli file transfer server
int hooli_file_sync(server_info *hftpd, client_info *client);

#endif
