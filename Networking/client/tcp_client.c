/*
 * tcp_client.c
 *
 * Computer Science 3357a - Fall 2015
 * Author: Andrew Bloch-Hansen
 *
 * Creates a TCP socket for a client.
 */

#include <err.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <syslog.h>

#include "tcp_client.h"
#include "../common/tcp_sockets.h"

// Make a TCP socket for a client
// @param port the port to make the socket on
// @return the socket file descriptor
int create_tcp_client_socket(char *server, char *port) {
    
    struct addrinfo* results = get_tcp_sockaddr(server, port, 0);
    int sockfd = open_connection(results);
    return sockfd;
    
} //end create_server_socket

// Opens a socket address and returns a socket descriptor
// @param addr_list the list of socket addresses
// @return the socket descriptor
int open_connection(struct addrinfo* addr_list) {
    
    struct addrinfo* addr;  // A socket address
    int sockfd;             // A socket descriptor
    
    // Iterate through each addrinfo in the list; stop when we successfully
    // connect to one
    for (addr = addr_list; addr != NULL; addr = addr->ai_next) {
        
        // Open a socket
        sockfd = socket(addr->ai_family, addr->ai_socktype, addr->ai_protocol);
        
        // Try the next address if we couldn't open a socket
        if (sockfd == -1) {
            
            continue;
            
        } //end if
        
        // Stop iterating if we're able to connect to the server
        if (connect(sockfd, addr->ai_addr, addr->ai_addrlen) != -1) {
            
            break;
            
        } //end if
        
    } //end for
    
    freeaddrinfo(addr_list);
    
    // If addr is NULL, we tried every addrinfo and weren't able to connect to any
    if (addr == NULL) {
        
        err(EXIT_FAILURE, "%s", "Unable to connect");
        
    } //end if
    
    // Return the socket descriptor
    else {
        
        return sockfd;
        
    } //end else
    
} //end open_connection