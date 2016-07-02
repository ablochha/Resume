#ifndef timer_h
#define timer_h

#include <time.h>

// Compute the elapsed time
uint64_t stop_timer(struct timespec start);

// Start the timer
void start_timer(struct timespec *start);

#endif
