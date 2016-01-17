/*
 * tcp_server.c
 *
 * Computer Science 3357a - Fall 2015
 * Author: Andrew Bloch-Hansen
 *
 * Creates a TCP socket and connection for a server.
 */

#include <arpa/inet.h>
#include <err.h>
#include <netdb.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/socket.h>
#include <syslog.h>
#include <unistd.h>

#include "tcp_server.h"
#include "../common/tcp_sockets.h"

#define BACKLOG 25

// Make a listening socket for a server
// @param port the port to make the socket on
// @return 1 if the listen was successful, -1 otherwise
int create_server_socket(char* port) {
    
    struct addrinfo* results = get_tcp_sockaddr(NULL, port, AI_PASSIVE);
    int sockfd = bind_tcp_socket(results);
    
    // Start listening on the socket
    if (listen(sockfd, BACKLOG) == -1) {
        
        err(EXIT_FAILURE, "%s", "Unable to listen on socket");
        
    } //end if
    
    return sockfd;
    
} //end create_server_socket

// Binds to a socket address and returns a socket descriptor
// @param addr_list the list of socket addresses
// @return a socket descriptor
int bind_tcp_socket(struct addrinfo* addr_list) {
    
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
    
} //end bind_tcp_socket

// Wait for an outside connection and return its socket descriptor
// @param sockfd the socket descriptor  to listen on
// @return the socket descriptor of a client connection
int wait_for_connection(int sockfd) {
    
    struct sockaddr_in client_addr;         // Remote IP that is connecting to us
    unsigned int addr_len;                  // Length of the remote IP structure
    char ip_address[INET_ADDRSTRLEN];       // Buffer to store human-friendly IP address
    int connectionfd;                       // Socket file descriptor for the new connection
    addr_len = sizeof(struct sockaddr_in);
    
    // Wait for a new connection
    connectionfd = accept(sockfd, (struct sockaddr*)&client_addr, &addr_len);
    
    // Make sure the connection was established successfully
    if (connectionfd == -1) {
        
        err(EXIT_FAILURE, "%s", "Unable to accept connection");
        
    } //end if
    
    // Convert the connecting IP to a human-friendly form and print it
    inet_ntop(client_addr.sin_family, &client_addr.sin_addr, ip_address, sizeof(ip_address));
    syslog(LOG_INFO, "Incoming connection from %s\n", ip_address);
    
    return connectionfd;
    
} //end wait_for_connection
