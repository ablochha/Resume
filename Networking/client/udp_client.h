#ifndef UDP_CLIENT_H
#define UDP_CLIENT_H

#include "../common/udp_sockets.h"

// Make a UDP socket for a client
int create_udp_client_socket(char* hostname, char* port, host* server);

#endif
