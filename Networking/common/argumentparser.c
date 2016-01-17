/*
 * argumentparser.c
 *
 * Computer Science 3357a - Fall 2015
 * Author: Andrew Bloch-Hansen
 *
 * Parses the command line arguments for the client and server.
 */

#include <getopt.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <syslog.h>

#include "argumentparser.h"
#include "client_structs.h"

#define DEFAULT_REDIS "localhost"
#define DEFAULT_PORT "9000"
#define DEFAULT_DIR "/hooli"
#define DEFAULT_SERVER "localhost"
#define DEFAULT_FSERVER "localhost"
#define DEFAULT_FPORT "10000"
#define DEFAULT_ROOT "/tmp/hftpd/"
#define SLASH "/"
#define DEFAULT_TIMEWAIT 10000
#define DEFAULT_DIR_LENGTH 6

// Parses the command line arguments for the server
// @param redis the name of the redis server
// @param port the port for the server to listen on
// @param flush whether to flush the Hooli database
// @param argc the number of command line arguments
// @param argv the command line arguments
void parse_metadata_server_input_arguments(char **redis, char **port, int *flush, int argc, char** argv) {
    
    int c;                          // Getopt variable
    
    static int verbose_flag = 0;    // Flag for verbose optional argument
    static int redis_flag = 0;      // Flag for redis optional argument
    static int port_flag = 0;       // Flag for port optional argument
    
    // Parse all the command line arguments
    while (1) {
        
        // Accepted long options
        static struct option long_options[] =
        {
            {"verbose", no_argument, &verbose_flag, 1},
            {"flush", no_argument, 0, 'f'},
            {"redis", required_argument, 0, 'r'},
            {"port", required_argument, 0, 'p'},
            {0, 0, 0, 0}
        }; //end long_options
        
        // Parse one option at a time
        int option_index = 0;
        c = getopt_long(argc, argv, "vfr:p:", long_options, &option_index);
        
        // If we've reached the end of the options, stop iterating
        if (c == -1) {
            
            break;
            
        } //end if
        
        // Handle the option
        switch (c) {
                
            // The verbose option was passed
            case 'v':
                
                verbose_flag = 1;
                setlogmask(LOG_UPTO(LOG_DEBUG));
                break;
                
            case 'f':
                
                *flush = 1;
                break;
                
            // A server argument was passed
            case 'r':
                
                redis_flag = 1;
                *redis = optarg;
                break;
                
            // A port argument was passed
            case 'p':
                
                port_flag = 1;
                *port = optarg;
                break;
                
            // An invalid option was passed
            case '?':
                
                exit(EXIT_FAILURE);
                break;
                
        } //end switch
        
    } //end while
    
    // Default syslog mask
    if (verbose_flag == 0) {
        
        setlogmask(LOG_UPTO(LOG_INFO));
        
    } //end if
    
    // Default redis server
    if (redis_flag == 0) {
        
        *redis = DEFAULT_REDIS;
        
    } //end if
    
    // Default port
    if (port_flag == 0) {
        
        *port = DEFAULT_PORT;
        
    } //end if
    
} //end parse_input_arguments

// Parse the command-line input arguments for the file server
// @param server the server to connect to redis on
// @param port the port for the server to listen on
// @param root the root of the directory to store files in
// @param timewait the time to wait in stop and wait
// @param argc the number of input arguments
// @param argv the input arguments
void parse_file_server_input_arguments(char **server, char **port, char **root,
                                       int *timewait, int argc, char** argv) {
    
    int c;                          // Getopt variable
    
    static int verbose_flag = 0;    // Flag for verbose optional argument
    static int server_flag = 0;
    static int port_flag = 0;       // Flag for port optional argument
    static int root_flag = 0;       // Flag for root optional argument
    static int timewait_flag = 0;   // Flag for timewait optional argument
    
    // Parse all the command line arguments
    while (1) {
        
        // Accepted long options
        static struct option long_options[] =
        {
            {"verbose", no_argument, &verbose_flag, 1},
            {"redis", required_argument, 0, 'r'},
            {"port", required_argument, 0, 'p'},
            {"dir", required_argument, 0, 'd'},
            {"timewait", required_argument, 0, 't'},
            {0, 0, 0, 0}
        }; //end long_options
        
        // Parse one option at a time
        int option_index = 0;
        c = getopt_long(argc, argv, "vr:p:d:t:", long_options, &option_index);
        
        // If we've reached the end of the options, stop iterating
        if (c == -1) {
            
            break;
            
        } //end if
        
        // Handle the option
        switch (c) {
                
            // The verbose option was passed
            case 'v':
                
                verbose_flag = 1;
                setlogmask(LOG_UPTO(LOG_DEBUG));
                break;
                
            // The server argument was passed
            case 'r':
                
                server_flag = 1;
                *server = optarg;
                break;
                
            // A port argument was passed
            case 'p':
                
                port_flag = 1;
                *port = optarg;
                break;
                
            // A root argument was passed
            case 'd':
                
                root_flag = 1;
                *root = strdup(optarg);
                break;
                
            // A timewait argument was passed
            case 't':
                
                timewait_flag = 1;
                *timewait = atoi(optarg);
                break;
                
            // An invalid option was passed
            case '?':
                
                exit(EXIT_FAILURE);
                break;
                
        } //end switch
        
    } //end while
    
    // Default syslog mask
    if (verbose_flag == 0) {
        
        setlogmask(LOG_UPTO(LOG_INFO));
        
    } //end if
    
    if (server_flag == 0) {
        
        *server = DEFAULT_SERVER;
        
    } //end if
    
    // Default fport
    if (port_flag == 0) {
        
        *port = DEFAULT_FPORT;
        
    } //end if
    
    // Default root
    if (root_flag == 0) {
        
        *root = strdup(DEFAULT_ROOT);
        
    } //end if
    
    // Default timewait
    if (timewait_flag == 0) {
        
        *timewait = DEFAULT_TIMEWAIT;
        
    } //end if
    
    char *ch = *root + strlen(*root) - 1;
    
    // Make sure theres a slash
    if (strcmp(ch, SLASH) != 0) {
        
        int old_length = strlen(*root);
        char *old_dir = strdup(*root);
        
        *root = realloc(*root, old_length + 2);
        strcpy(*root, old_dir);
        strcat(*root, SLASH);
        
        free(old_dir);
        
    } //end if
    
} //end parse_file_server_input_arguments

// Parses the command line arguments for the client
// @param hmds the metadata server information
// @param hftpd the file server information
// @param client the client's information
// @param argc the number of command line arguments
// @param argv the command line arguments
void parse_client_input_arguments(server_info **hmds, server_info **hftpd,
                                  client_info **client, int argc, char** argv) {
    
    int c;                          // Getopt variables
    
    static int verbose_flag = 0;    // Flag for verbose optional argument
    static int server_flag = 0;     // Flag for server optional argument
    static int port_flag = 0;       // Flag for port optional argument
    static int dir_flag = 0;        // Flag for dir optional argument
    static int fserver_flag = 0;    // Flag for optional fserver argument
    static int fport_flag = 0;      // Flag for optional fport argument
    
    // Parse all the command line arguments
    while (1) {
        
        // Accepted long options
        static struct option long_options[] =
        {
            {"verbose", no_argument, &verbose_flag, 1},
            {"server", required_argument, 0, 's'},
            {"port", required_argument, 0, 'p'},
            {"dir", required_argument, 0, 'd'},
            {"fserver", required_argument, 0, 'f'},
            {"fport", required_argument, 0, 'o'},
            {0, 0, 0, 0}
        }; //end long_options
        
        // Parse one option at a time
        int option_index = 0;
        c = getopt_long(argc, argv, "vs:p:d:f:o:", long_options, &option_index);
        
        // If we've reached the end of the options, stop iterating
        if (c == -1) {
            
            break;
            
        } //end if
        
        // Handle the option
        switch (c) {
                
            // The verbose option was passed
            case 'v':
                
                verbose_flag = 1;
                setlogmask(LOG_UPTO(LOG_DEBUG));
                break;
                
            // A server argument was passed
            case 's':
                
                server_flag = 1;
                (*hmds)->server = strdup(optarg);
                break;
                
            // A port argument was passed
            case 'p':
                
                port_flag = 1;
                (*hmds)->port = strdup(optarg);
                break;
                
            // A directory argument was passed
            case 'd':
                
                dir_flag = 1;
                (*client)->dir = strdup(optarg);
                break;

            // A fserver argument was passed
            case 'f':
                
                fserver_flag = 1;
                (*hftpd)->server = strdup(optarg);
                break;
              
            // A fport argument was passed
            case 'o':
                
                fport_flag = 1;
                (*hftpd)->port = strdup(optarg);
                break;
                
            // An invalid option was passed
            case '?':
                
                // Error message already printed by getopt_long -- we'll just exit
                exit(EXIT_FAILURE);
                break;
                
        } //end switch
        
    } //end while
    
    // Default syslog mask
    if (verbose_flag == 0) {
        
        setlogmask(LOG_UPTO(LOG_INFO));
        
    } //end if
    
    // Default server name
    if (server_flag == 0) {
        
        (*hmds)->server = strdup(DEFAULT_SERVER);
        
    } //end if
    
    // Default port name
    if (port_flag == 0) {
        
        (*hmds)->port = strdup(DEFAULT_PORT);
        
    } //end if
    
    // Default dir name
    if (dir_flag == 0) {
        
        char *home = getenv("HOME");
        
        (*client)->dir = malloc(strlen(home) + DEFAULT_DIR_LENGTH + 1);
        strcpy((*client)->dir, home);
        strcat((*client)->dir, DEFAULT_DIR);
        
    } //end if
    
    // Default fserver name
    if (fserver_flag == 0) {
        
        (*hftpd)->server = strdup(DEFAULT_FSERVER);
        
    } //end if
    
    // Default fport name
    if (fport_flag == 0) {
        
        (*hftpd)->port = strdup(DEFAULT_FPORT);
        
    } //end if
    
    char *ch = (*client)->dir + strlen((*client)->dir) - 1;
    
    // Make sure theres a slash
    if (strcmp(ch, SLASH) != 0) {
        
        int old_length = strlen((*client)->dir);
        char *old_dir = strdup((*client)->dir);
        
        (*client)->dir = realloc((*client)->dir, old_length + 2);
        strcpy((*client)->dir, old_dir);
        strcat((*client)->dir, SLASH);
        
        free(old_dir);
        
    } //end if
    
    // Check that the username and password were input
    if (optind + 2 == argc) {
        
        (*client)->username = strdup(argv[optind]);
        (*client)->password = strdup(argv[optind+1]);
        
    } //end if
    
    // The required arguments are missing
    else {
        
        syslog(LOG_ERR, "Usage: ./client <-short || --long> <username> <password>\n");
        exit(EXIT_FAILURE);
        
    } //end else
    
} //end parse_input_arguments
