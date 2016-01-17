/*
 * tcp_sockets.c
 *
 * Computer Science 3357a - Fall 2015
 * Author: Andrew Bloch-Hansen
 *
 * Includes functions to get a socket address, bind a socket, or send
 * and recieve information accross a socket.
 */

#include <err.h>
#include <netdb.h>
#include <stdbool.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <syslog.h>
#include <unistd.h>

#define SOCK_TYPE(s) (s == SOCK_STREAM ? "Stream" : s == SOCK_DGRAM ? "Datagram" : \
s == SOCK_RAW ? "Raw" : "Other")
#define TYPE_CLIENT 0
#define TYPE_SERVER 1
#define OFFSET 7
#define MAX_BUFFER_SIZE 131072
#define KEY_LENGTH "Length"
#define NEWLINE "\n"
#define END_OF_MESSAGE "\n\n"

// Repeatedly calls send() until the complete message has been sent
// @param sockfd the socket descriptor we are sending to
// @param buffer the message to send
// @param length the length of the message
// @return 1 if the send was successful, -1 otherwise
int send_all(int sockfd, char *buffer, int *length) {
    
    int total = 0;              // The total number of bytes we have sent
    int bytes_left = *length;   // The number of bytes left to send
    int bytes_sent;             // The number of bytes read per each send
    
    // Continue until we have sent the entire length of the message
    while (total < *length) {
        
        bytes_sent = send(sockfd, (buffer + total), bytes_left, 0);
        
        // The send failed
        if (bytes_sent == -1) {
            
            break;
        
        } //end if
        
        total += bytes_sent;
        bytes_left -= bytes_sent;
        
    } //end while
    
    *length = total; // return number actually sent here
    
    return bytes_sent == -1 ? -1 : 0; // return -1 on failure, 0 on success
    
} //end send_all

// Repeatedly calls recv() until the complete message has been recieved
// @param sockfd the socket descriptor we are receiving from
// @param buffer the message we are reading
// @param total the total number of bytes read
// @return 1 if the read was successful, -1 otherwise
int receive_all(int sockfd, char *buffer, int *total) {
    
    char *key_length_position;      // The position at the start of Length
    char *value_length_position;    // The position at the start of the value of Length
    char *end_of_message_position;  // The position at the start of \n\n
    char *body_length;              // The position of the encoded body length
    
    int length = 0;     // The total length of the message we are receiving
    int bytes_left = 0; // The number of bytes that still need to be read from the message
    int bytes_read;     // The number of bytes read each recv call
    
    bool done = false;  // Flag for when we peek at the stream to find the message size
    
    // Loop until we have found the length of the message we are reading
    do {
        
        bytes_read = recv(sockfd, buffer, MAX_BUFFER_SIZE, MSG_PEEK);
        
        // The read failed
        if (bytes_read == -1) {
            
            break;
            
        } //end if
        
        // Find the position of the key Length or the end of message \n\n if they exist
        key_length_position = strstr(buffer, KEY_LENGTH);
        end_of_message_position = strstr(buffer, END_OF_MESSAGE);
        
        // The Length keyword was found, calculate the total message size
        if (key_length_position != NULL && end_of_message_position != NULL) {
            
            value_length_position = key_length_position + OFFSET;
            body_length = strsep(&value_length_position, NEWLINE);
            length = (int)((end_of_message_position + atoi(body_length) + 2) - buffer);
            done = true;
            
        } //end if
        
        // Found end of message with no Length keyword included, calculate total message size
        else if (end_of_message_position != NULL) {
            
            length = (int)((end_of_message_position + 2) - buffer);
            done = true;
            
        } //end else if
        
    } while (!done && bytes_read > 0); //end while
    
    bytes_left = length;
    memset(&buffer[0], 0, MAX_BUFFER_SIZE);
    
    // Continue receiving until we have exactly one complete message
    while (*total < length) {
        
        bytes_read = recv(sockfd, (buffer + *total), bytes_left, 0);
        
        // The read failed
        if (bytes_read == -1) {
            
            break;
            
        } //end if
        
        *total += bytes_read;
        bytes_left -= bytes_read;
        
    } //end while
    
    buffer[*total] = '\0';
    
    return bytes_read == -1 ? -1 : 0; // return -1 on failure, 0 on success
    
} //end receive_all

// Returns a list of socket addresses
// @param hostname the host to open a socket on
// @param port the port to open a socket on
// @param flags client or server
// @return a list of sockets
struct addrinfo* get_tcp_sockaddr(const char* hostname, const char* port, int flags) {
    
    struct addrinfo hints;    // Helps to specify what kind of socket addresses we want
    struct addrinfo* results; // Stores a list of socket addresses
    int retval;               // Holds value to see if we successfully got a list of socket addresses
    
    memset(&hints, 0, sizeof(struct addrinfo)); // Initialize hints
    hints.ai_family = AF_INET;                  // Return socket addresses for our local IPv4 addresses
    hints.ai_socktype = SOCK_STREAM;            // Return TCP socket addresses
    hints.ai_flags = flags;
    
    // Get a list of socket addresses that match our preferences
    retval = getaddrinfo(hostname, port, &hints, &results);
    
    // Check if we successfully got a list of socket addresses
    if (retval) {
        
        errx(EXIT_FAILURE, "%s", gai_strerror(retval));
        
    } //end if
    
    return results;
    
} //end get_sockaddr
