/*
 * hmds.c
 *
 * Computer Science 3357a - Fall 2015
 * Author: Andrew Bloch-Hansen
 *
 * The Hooli database server attempts to sync any clients that connect to it with
 * the Hooli database. If the client successfully authenticates with the database,
 * the server will check to see which of the client's files need to be uploaded
 * and will request them from the client.
 *
 * Build with `make`
 */

#include <err.h>
#include <hiredis/hiredis.h>
#include <signal.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <syslog.h>
#include <unistd.h>

#include "hmdp_server.h"
#include "tcp_server.h"
#include "../common/argumentparser.h"
#include "../common/client_structs.h"
#include "../common/hmdp.h"
#include "../common/linkedlist.h"
#include "../hdb/hdb.h"

// Flag to shut the server off
static bool term_requested = false;

// Called when the user pushes Ctrl+C
// @param signal when the user presses Ctrl+C
static void term_handler(int signal) {
    
    syslog(LOG_INFO, "Termination requested...\n");
    term_requested = true;
    
} //end term_handler

// Flushes the database when the server starts up
// @param server the server for the Hooli database
static void flush_connection(char *server) {
    
    hdb_connection *con = hdb_connect(server);
    redisReply* reply = redisCommand((redisContext*)con,"FLUSHALL");
    freeReplyObject(reply);
    hdb_disconnect(con);

    syslog(LOG_INFO, "The Hooli database at '%s' has been flushed!", server);
    
} //end flush_connection

// Attempts to sync a specfic client with the Hooli database
// @param connectionfd the socket descriptor for the client
// @param con the connection to the Hooli database
static void sync_client(int connectionfd, char *server) {
    
    char *command = NULL;             // The type of request that the client has issued
    
    client_info *client = create_client_info();
    
    // Communicate with the client until it comes to a natural end or the server is terminated
    while (!term_requested) {
        
        // Handle the client's request
        if (handle_request(connectionfd, server, &command, &client) == -1) {
            
            break;
            
        } //end if
        
    } //end while
    
    free(client);
    
} //end sync_client

// Attempts to sync any incoming connections with the Hooli database
// @param server the address of the Hooli database
// @param port the port to use with the Hooli database
// @param flush whether to flush the Hooli database
static void hooli_sync(char *server, char *port, int flush) {
    
    int sockfd;                         // Socket descriptor for the socket we open
    int connectionfd;                   // Socket descriptor for client
    
    // Flushes the Hooli database
    if (flush == 1) {
        
        flush_connection(server);
        
    } //end if
    
    // Find and bind to an appropriate socket
    syslog(LOG_INFO, "Server listening on port %s", port);
    sockfd = create_server_socket(port);
    
    // Attempt to sync any clients that connect to the server
    while (!term_requested) {
        
        connectionfd = wait_for_connection(sockfd);
        sync_client(connectionfd, server);
        close(connectionfd);
        
        syslog(LOG_INFO, "Connection terminated");
        
    } //end while
    
    // Close the socket and exit
    close(sockfd);
    
} //end sync

// Runs the Hooli database server, which attempts to sync any clients that connect to it
// @param argc the number of command line arguments
// @param argv the list of command line arguments
int main(int argc, char** argv) {
    
    struct sigaction act;                      // The signal action
    memset(&act, 0, sizeof(struct sigaction)); // Initialize act
    act.sa_handler = term_handler;             // Action will be redirected to the term_handler function
    sigaction(SIGINT, &act, NULL);             // Action will be called when the user presses Ctrl+C
    
    char *server = NULL;            // The name of the Redis server
    char *port = NULL;              // The port for the server to open a socket on
    
    int flush = 0;                  // Whether to flush the Hooli database
    
    // Open the log
    openlog("metadata server", LOG_PERROR | LOG_PID | LOG_NDELAY, LOG_USER);
    
    // Parse the command-line arguments
    parse_metadata_server_input_arguments(&server, &port, &flush, argc, argv);
    
    // Sync valid clients
    hooli_sync(server, port, flush);
    
    // Close the log
    closelog();
    
    return EXIT_SUCCESS;
    
} //end main
