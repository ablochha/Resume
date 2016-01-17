#ifndef HMDP_SERVER_H
#define HMDP_SERVER_H

#include "../common/client_structs.h"

// Handle the client's request
int handle_request(int connectionfd, char *server, char **command, client_info **client);

#endif
