/*
 * client.c
 *
 * Computer Science 3357a - Fall 2015
 * Author: Andrew Bloch-Hansen
 *
 * The Hooli Database client lets the user upload files to a server. Each file's
 * checksum is calculated and the filenames and checksums are checked against the
 * Hooli Database, and only files that have been locally updated will be uploaded.
 *
 * Build with `make`
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <syslog.h>
#include <unistd.h>

#include "../common/argumentparser.h"
#include "../common/client_structs.h"
#include "../common/linkedlist.h"
#include "directoryscanner.h"
#include "hftp_client.h"
#include "hmdp_client.h"

#define CHECKSUMS 1

// This function checks that the correct amount of arguments were used, and scans
// the input directory. Each file in the directory has its checksum calculated and
// the filenames and checksums are stores in a linked list. After authenticating
// with the Hooli server, this list is sent to be compared with the database.
// Any files that need to be uploaded are then requested by the server by name.
// @param argc the number of command line arguments
// @param argv the command line arguments
int main(int argc, char** argv) {
    
    client_info *client = create_client_info();
    
    server_info *hmds = create_server_info();
    server_info *hftpd = create_server_info();
    
    // Open the log
    openlog("client", LOG_PERROR | LOG_PID | LOG_NDELAY, LOG_USER);
    
    // Parse the command-line arguments
    parse_client_input_arguments(&hmds, &hftpd, &client, argc, argv);
    
    client->list = get_files(client->dir);
    
    // Attempt to sync the files found in the hooli directory to the Hooli database
    if (client->list != NULL) {
        
        to_string_files(client->list, CHECKSUMS);
        
        // Attempt to transfer the files to the file server
        if (hooli_metadata_sync(hmds, &client) == 1) {
            
            free_server_info(hmds);
            hooli_file_sync(hftpd, client);
            
        } //end if
        
    } //end if
    
    else {
        
        syslog(LOG_INFO, "Directory '%s' has no files to upload.", client->dir);
        
    } //end else
    
    free_server_info(hftpd);
    free_client_info(client);
    
    // Close the log
    closelog();
    
    return EXIT_SUCCESS;

} //end main
