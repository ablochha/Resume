#ifndef HFTP_H
#define HFTP_H

#include <stdint.h>

#include "linkedlist.h"
#include "udp_sockets.h"

#define HFTP_MSS 1472
#define CONTROL_HEADERS 28
#define DATA_HEADERS 4
#define RESPONSE_HEADERS 4

// A HFTP control message
typedef struct {
    
    int length;
    uint8_t type;
    uint8_t sequence;
    uint16_t filename_length;
    uint32_t file_size;
    uint32_t checksum;
    uint8_t token[16];
    uint8_t filename[HFTP_MSS-CONTROL_HEADERS];
    
} hftp_control_message;

// A HFTP data message
typedef struct {
    
    int length;
    uint8_t type;
    uint8_t sequence;
    uint16_t data_length;
    uint8_t data[HFTP_MSS-DATA_HEADERS];
    
} hftp_data_message;

// A HFTP response message
typedef struct {
    
    int length;
    uint8_t type;
    uint8_t sequence;
    uint16_t error_code;
    uint8_t padding[HFTP_MSS-RESPONSE_HEADERS];
    
} hftp_response_message;

// Creates a new control message
message* create_control_message(uint8_t type, uint8_t sequence, csv_record *record, char *token, uint32_t);

// Creates a new data message
message* create_data_message(uint8_t type, uint8_t sequence, char *data, uint16_t data_length);

// Creates a new response message
message* create_response_message(uint8_t type, uint8_t sequence, uint16_t error_code);

#endif
