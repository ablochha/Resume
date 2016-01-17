#ifndef HMDP_H
#define HMDP_H

#include "linkedlist.h"

// Sends the HMDP request 'Auth'
void request_auth(int sockfd, char *username, char *password);

// Sends the HMDP request 'List'
void request_list(int sockfd, char *token, csv_record *list);

// Sends the HMDP response '200'
void response_200(int connectionfd, char *token);

// Sends the HMDP response '204'
void response_204(int connectionfd);

// Sends the HMDP response '302'
void response_302(int connectionfd, csv_record *response_list);

// Sends the HMDP response '401'
void response_401(int connectionfd);

// Reads a HMDP request from a client
int read_request(int connectionfd, char **username, char **password, char **token,
                 csv_record **request_list, char **command);

// Read a HMDP response from a server
int read_response(int sockfd, char **token, char **status, char **message,
                  csv_record **response_list);

#endif
