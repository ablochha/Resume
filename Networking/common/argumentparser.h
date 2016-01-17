#ifndef ARGUMENTPARSER_H
#define ARGUMENTPARSER_H

#include "client_structs.h"

// Parse the command-line input arguments for the metadata server
void parse_metadata_server_input_arguments(char **redis, char **port, int *flush,
                                           int argc, char** argv);

// Parse the command-line input arguments for the file server
void parse_file_server_input_arguments(char **server, char **port, char **root,
                                       int *timewait, int argc, char** argv);

// Parse the command-line arguments for the client
void parse_client_input_arguments(server_info **hmds, server_info **hftpd,
                                  client_info **client, int argc, char** argv);

#endif
