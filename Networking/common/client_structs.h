#ifndef CLIENT_H
#define CLIENT_H

#include "linkedlist.h"

// Stores connection info for a server
typedef struct {
    
    char *port;
    char *server;
    
} server_info;

// Stores information for the current session
typedef struct {
    
    char *username;
    char *password;
    char *token;
    char *dir;
    csv_record *list;
    csv_record *response_list;
    
} client_info;

// Creates a new client variable
client_info* create_client_info();

// Creates a new server variable
server_info* create_server_info();

// Frees the client variable
void free_client_info(client_info *client);

// Frees the server variable
void free_server_info(server_info *server);

#endif
