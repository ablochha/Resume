/*
 * udp_server.c
 *
 * Computer Science 3357a - Fall 2015
 * Author: Andrew Bloch-Hansen
 *
 * Creates a UDP socket for a server.
 */

#include <err.h>
#include <netdb.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/socket.h>
#include <unistd.h>

#include "udp_server.h"
#include "../common/udp_sockets.h"

// Make a socket for a server
// @param port the port to make the socket on
// @return the socket file descriptor
int create_server_socket(char* port) {
    
    struct addrinfo* results = get_udp_sockaddr(NULL, port, AI_PASSIVE);
    int sockfd = bind_udp_socket(results);
    return sockfd;
    
} //end create_server_socket

// Binds to a socket address and returns a socket descriptor
// @param addr_list the list of socket addresses
// @return a socket descriptor
int bind_udp_socket(struct addrinfo* addr_list) {
    
    struct addrinfo* addr;  // Pointer to iterate through the list of socket addresses
    int sockfd;             // Socket file descriptor
    char yes = '1';         // Override the time delay for re-opening socket addresses
    
    // Iterate over the addresses in the list; stop when we successfully bind to one
    for (addr = addr_list; addr != NULL; addr = addr->ai_next) {
        
        // Open a socket
        sockfd = socket(addr->ai_family, addr->ai_socktype, addr->ai_protocol);
        
        // Move on to the next address if we couldn't open a socket
        if (sockfd == -1) {
            
            continue;
            
        } //end if
        
        // Allow the port to be re-used if currently in the TIME_WAIT state
        if (setsockopt(sockfd, SOL_SOCKET, SO_REUSEADDR, &yes, sizeof(int)) == -1) {
            
            err(EXIT_FAILURE, "%s", "Unable to set socket option");
            
        } //end if
        
        // Try to bind the socket to the address/port, try the next one if it fails
        if (bind(sockfd, addr->ai_addr, addr->ai_addrlen) == -1) {
            
            close(sockfd);
            continue;
            
        } //end if
        
        // Successfully bound the socket
        else {
            
            break;
            
        } //end else
        
    } //end for
    
    freeaddrinfo(addr_list);
    
    // If addr is NULL, we tried every address and weren't able to bind to any
    if (addr == NULL) {
        
        err(EXIT_FAILURE, "%s", "Unable to bind");
        
    } //end if
    
    // Return the socket descriptor
    else {
        
        return sockfd;
        
    } //end else
    
} //end bind_udp_socket
