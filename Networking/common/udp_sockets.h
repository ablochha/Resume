#ifndef UDP_SOCKETS_H
#define UDP_SOCKETS_H

#include <arpa/inet.h>
#include <stdint.h>
#include <sys/types.h>
#include <sys/socket.h>

#define HFTP_MSS 1472

// A generic message
typedef struct {
    
    int length;
    uint8_t buffer[HFTP_MSS];
    
} message;

// Information for a host
typedef struct {
    
    struct sockaddr_in addr;
    socklen_t addr_len;
    char friendly_ip[INET_ADDRSTRLEN];
    
} host;

// Creates a new message variable
message* create_message();

// Sends the message to the destination
int send_message(int sockfd, message* msg, host* dest);

// Receives a message from a source
message* receive_message(int sockfd, host* source);

// Gets a UDP socket address
struct addrinfo* get_udp_sockaddr(const char* node, const char* port, int flags);

#endif
