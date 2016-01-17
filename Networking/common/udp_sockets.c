/*
 * udp_sockets.c
 *
 * Computer Science 3357a - Fall 2015
 * Author: Andrew Bloch-Hansen
 *
 * Includes functions to get a socket address or send
 * and recieve information accross a socket.
 */

#include <arpa/inet.h>
#include <netdb.h>
#include <stdbool.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <syslog.h>

#include "udp_sockets.h"

// Creates a new message variable
// @return a pointer to the message
message* create_message() {
    
    return (message*)malloc(sizeof(message));
    
} //end create_message

// Sends the message to the destination
// @param sockfd the socket file descriptor of the sender's socket
// @param msg the message to send
// @param dest the destination's information
// @return 1 if the message sent successfully, -1 otherwise
int send_message(int sockfd, message* msg, host* dest) {
    
    return sendto(sockfd, msg->buffer, msg->length, 0,
                  (struct sockaddr*)&dest->addr, dest->addr_len);
    
} //end send_message

// Receives a message from a source
// @param sockfd the socket file descriptor of the receivers socket
// @param source the source's destination
// @return the received message
message* receive_message(int sockfd, host* source) {
    
    message* msg = create_message();
    
    // Length of the remote IP structure
    source->addr_len = sizeof(source->addr);
    
    // Read message, storing its contents in msg->buffer, and
    // the source address in source->addr
    msg->length = recvfrom(sockfd, msg->buffer, sizeof(msg->buffer), 0,
                           (struct sockaddr*)&source->addr,
                           &source->addr_len);
    
    // If a message was read
    if (msg->length > 0) {
        
        // Convert the source address to a human-readable form,
        // storing it in source->friendly_ip
        inet_ntop(source->addr.sin_family, &source->addr.sin_addr,
                  source->friendly_ip, sizeof(source->friendly_ip));
        
        // Return the message received
        return msg;
        
    } //end if
    
    else {
        
        // Otherwise, free the allocated memory and return NULL
        free(msg);
        return NULL;
        
    } //end else
    
} //end receive_message

// Gets a UDP socket address
// @param node the name of the node
// @param the port to open the socket on
// @param flags any flags specified
// @return a list of socket addresses
struct addrinfo* get_udp_sockaddr(const char* node, const char* port, int flags) {
    
    struct addrinfo hints;
    struct addrinfo* results;
   
    int retval;
    memset(&hints, 0, sizeof(struct addrinfo));
    hints.ai_family = AF_INET; // Return socket addresses for our local IPv4 addresses
    hints.ai_socktype = SOCK_DGRAM; // Return UDP socket addresses
    hints.ai_flags = flags; // Socket addresses should be listening sockets
    
    retval = getaddrinfo(node, port, &hints, &results);
    
    if (retval != 0) {
        
        fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(retval));
        exit(EXIT_FAILURE);
        
    } //end if
    
    return results;
    
} //end get_udp_sockaddr
