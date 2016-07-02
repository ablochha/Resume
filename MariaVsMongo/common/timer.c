/*
 * timer.c
 *
 * Computer Science 4411
 * Author: Andrew Bloch-Hansen
 *
 * Stopwatch.
 */

#include <stdio.h>
#include <stdint.h>
#include <stdlib.h>
#include <time.h>
#include <string.h>
#include <unistd.h>
#include <sys/wait.h>
#include <math.h>

#include "timer.h"

#define BILLION  1000000000L
#define CLOCK_ID CLOCK_PROCESS_CPUTIME_ID

// Compute the elapsed time
// @param start the starting time
// @return the elapsed time
uint64_t stop_timer(struct timespec start) {
    
    struct timespec end;
    uint64_t diff;
    
    clock_gettime(CLOCK_ID, &end);
    diff = BILLION * (end.tv_sec - start.tv_sec) + end.tv_nsec - start.tv_nsec;
    return diff;
    
} //end diff

// Start the timer
// @param start the time struct
void start_timer(struct timespec *start) {
    
    clock_gettime(CLOCK_ID, start);
    
} //end start_timer
