#ifndef TCP_CLIENT_H
#define TCP_CLIENT_H

#include <netdb.h>

// Make a TCP socket for a client
int create_tcp_client_socket(char *server, char *port);

// Opens a socket address and returns a socket descriptor
int open_connection(struct addrinfo* addr_list);

#endif
