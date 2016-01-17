/*
 * hftpd.c
 *
 * Computer Science 3357a - Fall 2015
 * Author: Andrew Bloch-Hansen
 *
 * The Hooli File Transfer Server attempts to receive files from a client, and
 * transfer them to the file server.
 *
 * Build with `make`
 */

#include <signal.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <syslog.h>
#include <unistd.h>

#include "hftp_server.h"
#include "udp_server.h"
#include "../common/argumentparser.h"

// Flag to shut the server off
static bool term_requested = false;

// Called when the user pushes Ctrl+C
// @param signal when the user presses Ctrl+C
static void term_handler(int signal) {
    
    syslog(LOG_INFO, "Termination requested...\n");
    term_requested = true;
    
} //end term_handler

// Attempts to sync any incoming connections with the Hooli file server
// @param port the port to use with the Hooli file server
// @param root the root to store the files under
// @param timewait the time to use for stop and wait
static void hooli_sync(char *server, char *port, char *root, int timewait) {
    
    int sockfd;                         // Socket descriptor for the socket we open
    int expected_sequence = 0;
    
    file_info *file = NULL;
    
    // Find and bind to an appropriate socket
    syslog(LOG_INFO, "Server listening on port %s", port);
    sockfd = create_server_socket(port);
    
    // Communicate with the client until it comes to a natural end or the server is terminated
    while (!term_requested) {
        
        if (handle_request(sockfd, server, root, timewait, &expected_sequence, &file) == -1) {
            
            continue;
            
        } //end if
        
    } //end while
    
    // Close the socket and exit
    close(sockfd);
    
} //end sync

// Runs the Hooli database file server, which attempts to sync any clients that connect to it
// @param argc the number of command line arguments
// @param argv the list of command line arguments
int main(int argc, char** argv) {
    
    struct sigaction act;                      // The signal action
    memset(&act, 0, sizeof(struct sigaction)); // Initialize act
    act.sa_handler = term_handler;             // Action will be redirected to the term_handler function
    sigaction(SIGINT, &act, NULL);             // Action will be called when the user presses Ctrl+C
    
    char *port = NULL;              // The port for the server to open a socket on
    char *root = NULL;              // The root to store the files at
    char *server = NULL;            // The redis server
    
    int timewait = 0;               // The time to wait for stop and wait
    
    // Open the log
    openlog("file server", LOG_PERROR | LOG_PID | LOG_NDELAY, LOG_USER);
    
    // Parse the command-line arguments
    parse_file_server_input_arguments(&server, &port, &root, &timewait, argc, argv);
    
    // Sync valid clients
    hooli_sync(server, port, root, timewait);
    
    free(root);
    
    // Close the log
    closelog();
    
    return EXIT_SUCCESS;
    
} //end main
