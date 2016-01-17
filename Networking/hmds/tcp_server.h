#ifndef TCP_SERVER_H
#define TCP_SERVER_H

#include <netdb.h>

// Make a listening socket for a server
int create_server_socket(char* port);

// Binds to a socket address and returns a socket descriptor
int bind_tcp_socket(struct addrinfo* addr_list);

// Wait for an outside connection and return its socket descriptor
int wait_for_connection(int sockfd);

#endif
