/*
 * hftp.c
 *
 * Computer Science 3357a - Fall 2015
 * Author: Andrew Bloch-Hansen
 *
 * Defines the protocol for the Hooli file transfer messages.
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <syslog.h>

#include "checksum.h"
#include "hftp.h"
#include "filereader.h"
#include "udp_sockets.h"

// Creates a new control message
// @param type the type of the message
// @param sequence the sequence number of the message
// @param record a file from the client
// @param token the client's authentication token
// @param file_size the size of the file being sent
// @return a pointer to the message
message* create_control_message(uint8_t type, uint8_t sequence, csv_record *record,
                                char *token, uint32_t file_size) {
    
    hftp_control_message *msg = (hftp_control_message*)create_message();
    
    msg->type = type;
    msg->sequence = sequence;
    
    // Initialization message
    if (type == 1) {
        
        msg->filename_length = htons(strlen(record->filename));
        msg->file_size = htonl(file_size);
        msg->checksum = htonl(checksum_to_int(record->checksum));
        memcpy(msg->token, token, strlen(token));
        memcpy(msg->filename, record->filename, strlen(record->filename));
        msg->length = 28 + strlen(record->filename);
        
    } //end if
    
    // Termination message (doesn't need file info)
    else if (type == 2){
        
        msg->filename_length = 0;
        msg->file_size = 0;
        msg->checksum = 0;
        memset(msg->token, 0, 16);
        memset(msg->filename, 0, 1444);
        msg->length = 2;
        
    } //end else
    
    return (message*)msg;
    
} //end create_control_message

// Creates a new data message
// @param type the type of the message
// @param sequence the sequence number of the message
// @param data a chunk of a file
// @param data_length the size of the data being sent
// @return a pointer to the message
message* create_data_message(uint8_t type, uint8_t sequence, char *data, uint16_t data_length) {
    
    hftp_data_message *msg = (hftp_data_message*)create_message();
    
    msg->type = type;
    msg->sequence = sequence;
    msg->data_length = htons(data_length);
    memcpy(msg->data, data, data_length);
    msg->length = 4 + data_length;
    
    return (message*)msg;
    
} //end create_data_message

// Creates a new response message
// @param type the type of the message
// @param sequence the sequence number of the message
// @param error_code the error code of the message
// @return a pointer to the message
message* create_response_message(uint8_t type, uint8_t sequence, uint16_t error_code) {
    
    hftp_response_message *msg = (hftp_response_message*)create_message();
    
    msg->type = type;
    msg->sequence = sequence;
    msg->error_code = htons(error_code);
    msg->length = 4;
    
    return (message*)msg;
    
} //end create_response_message
