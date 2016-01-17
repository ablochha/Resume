#ifndef TCP_SOCKETS_H
#define TCP_SOCKETS_H

// Repeatedly calls send() until the complete message has been sent
int send_all(int sockfd, char *buffer, int *length);

// Repeatedly calls recv() until the complete message has been recieved
int receive_all(int sockfd, char *buffer, int *total);

// Returns a list of socket addresses
struct addrinfo* get_tcp_sockaddr(const char* hostname, const char* port, int flags);

#endif
