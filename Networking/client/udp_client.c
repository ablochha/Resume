/*
 * udp_client.c
 *
 * Computer Science 3357a - Fall 2015
 * Author: Andrew Bloch-Hansen
 *
 * Creates a TCP socket for a client.
 */

#include <err.h>
#include <netdb.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <syslog.h>

#include "udp_client.h"
#include "../common/udp_sockets.h"

// Make a UDP socket for a client
// @param hostname the hostname name
// @param port the port to make the socket on
// @return the socket file descriptor
int create_udp_client_socket(char* hostname, char* port, host* server) {
    
    int sockfd;
    struct addrinfo* addr;
    struct addrinfo* results = get_udp_sockaddr(hostname, port, 0);
    
    // Iterate through each addrinfo in the list;
    // stop when we successfully create a socket
    for (addr = results; addr != NULL; addr = addr->ai_next) {
        
        // Open a socket
        sockfd = socket(addr->ai_family, addr->ai_socktype, addr->ai_protocol);
        
        // Try the next address if we couldn't open a socket
        if (sockfd == -1) {
            
            continue;
            
        } //end if
        
        // Copy server address and length to the out parameter 'server'
        memcpy(&server->addr, addr->ai_addr, addr->ai_addrlen);
        memcpy(&server->addr_len, &addr->ai_addrlen, sizeof(addr->ai_addrlen));
        
        // We've successfully created a socket; stop iterating
        break;
        
    } //end for
    
    // Free the memory allocated to the addrinfo list
    freeaddrinfo(results);
    
    // If we tried every addrinfo and failed to create a socket
    if (addr == NULL) {
        
        err(EXIT_FAILURE, "%s", "Unable to listen on socket");
        
    } //end if
    
    else {
        
        // Otherwise, return the socket descriptor
        return sockfd;
        
    } //end else
    
} //end create_client_socket
