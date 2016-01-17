#ifndef HMDP_CLIENT_H
#define HMDP_CLIENT_H

#include "../common/client_structs.h"
#include "../common/linkedlist.h"

// Syncs the contents of the client's hooli directory with the Hooli Database
int hooli_metadata_sync(server_info *hmds, client_info **client);

#endif
