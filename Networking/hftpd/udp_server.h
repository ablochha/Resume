#ifndef UDP_SERVER_H
#define UDP_SERVER_H

#include <netdb.h>

// Make a socket for a server
int create_server_socket(char* port);

// Binds to a socket address and returns a socket descriptor
int bind_udp_socket(struct addrinfo* addr_list);

#endif
