/*
 * directoryscanner.c
 *
 * Computer Science 3357a - Fall 2015
 * Author: Andrew Bloch-Hansen
 *
 * The filereader scans a directory and computes a checksum for each file and 
 * stores the filename and checksum in a linked list.
 */

#include <dirent.h>
#include <limits.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <syslog.h>
#include <unistd.h>

#include "../common/checksum.h"
#include "../common/filereader.h"
#include "../common/linkedlist.h"
#include "directoryscanner.h"

// Scans a directory and puts all the files into a linked list
// @param dir_name the name of the directory to scan
// @param list the root of the linked list
static void scan_dir(const char *dir_name, csv_record **list, int root_length) {
    
    // Try to open the directory
    DIR *dir = opendir(dir_name);
    
    // Check if the directory was opened
    if (!dir) {
        
        syslog(LOG_ERR, "Cannot open directory '%s'\n", dir_name);
        
    } //end if
    
    // Continuously scan for more files and folders, loop exits when there are no more entries
    while (dir) {
        
        struct dirent *entry;         // A reference to an entry in the directory
        const char *d_name;           // A reference to name of an entry in the directory
        char path[PATH_MAX];          // The path and filename
        
        // Try read an entry in the directory
        entry = readdir (dir);
        
        // Break out of the while loop if there are no more entries in the directory
        if (!entry) {
            
            break;
            
        } //end if
        
        // Get the name of the entry, and build its pathname
        d_name = entry->d_name;
        int path_length = snprintf(path, PATH_MAX, "%s/%s", dir_name, d_name);
        
        // Compute the checksum of files and save them in a linked list
        if (!(entry->d_type & DT_DIR)) {
            
            char *filename = strdup(path + root_length);  // The relative path and filename
            char *checksum = compute_crc(path);    // Compute checksum of file array
            
            // Add the file and checksum to the linked list
            if (checksum != NULL) {
                
                add_file_to_list(list, filename, checksum);
                
            } //end if
            
        } //end if
        
        // Scan a subdirectory
        if (entry->d_type & DT_DIR) {
            
            // Check that the directory is not "d" or d's parent
            if (strcmp (d_name, "..") != 0 && strcmp (d_name, ".") != 0) {
                
                if (path_length >= PATH_MAX) {
                    
                    syslog(LOG_ERR, "Path length is too long.\n");
                    break;
                    
                } //end if
                
                // Recursively call "scan_dir" with the new path.
                scan_dir (path, list, root_length);
                
            } //end if
            
        } //end if
        
    } //end while
    
    // Try to close the directory
    if (closedir(dir)) {
        
        syslog(LOG_ERR, "Could not close %s\n", dir_name);
        exit(EXIT_FAILURE);
        
    } //end if
    
} //end scan_dir

// Returns a linked list of all the files to be synced with the Hooli database
// @param dir_name the name of the directory to scan
// @return a pointer to the linked list of files
csv_record* get_files(const char *dir_name) {
    
    make_all_directories(dir_name);
    
    csv_record *root = NULL;    // The root of the linked list
    
    syslog(LOG_INFO, "Scanning Hooli directory %s", dir_name);
    scan_dir(dir_name, &root, strlen(dir_name) + 1);
    return root;
    
} //end get_files()
