/*
 * client_structs.c
 *
 * Computer Science 3357a - Fall 2015
 * Author: Andrew Bloch-Hansen
 *
 * Variables for holding client information.
 */

#include <stdio.h>
#include <stdlib.h>

#include "client_structs.h"
#include "linkedlist.h"

// Creates a new client variable
// @return a pointer to the client variable
client_info* create_client_info() {
    
    client_info *client = malloc(sizeof(client_info));
    
    client->username = NULL;
    client->password = NULL;
    client->token = NULL;
    client->dir = NULL;
    client->list = NULL;
    client->response_list = NULL;
    
    return client;
    
} //end create_user

// Creates a new server variable
// @return a pointer to the server variable
server_info* create_server_info() {
    
    server_info *server = malloc(sizeof(server_info));
    
    server->server = NULL;
    server->port = NULL;
    
    return server;
    
} //end create_server_info

// Frees the client variable
// @param client the client variable
void free_client_info(client_info *client) {
    
    free(client->username);
    free(client->password);
    free(client->token);
    free(client->dir);
    csv_free_result(client->list);
    csv_free_result(client->response_list);
    
    client->username = NULL;
    client->password = NULL;
    client->token = NULL;
    client->dir = NULL;
    client->list = NULL;
    client->response_list = NULL;
    
    free(client);
    client = NULL;
    
} //end free_client_info

// Frees the server variable
// @param server the server variable
void free_server_info(server_info *server) {
    
    free(server->server);
    free(server->port);
    
    server->server = NULL;
    server->port = NULL;
    
    free(server);
    server = NULL;
    
} //end free_server_info
