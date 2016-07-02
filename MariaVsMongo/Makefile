CC	= gcc
CFLAGS	= -g -O0 -Wall -Werror -std=gnu11
LDFLAGS = -I/usr/local/include -lbson-1.0 -lmongoc-1.0 -lm
LIBS	= $(shell pkg-config --libs libmongoc-1.0)
LIBSC	= $(shell pkg-config --cflags libmongoc-1.0)

.PHONY: all clean

all: main

main: main.o builddatabase/builddata.o builddatabase/buildmaria.o builddatabase/buildmongo.o builddatabase/cJSON.o common/filereader.o common/timer.o queries/runqueries.o queries/mongoqueries.o queries/mariaqueries.o
	$(CC) -o $@ $^ $(LDFLAGS) $(LIBS) -L/usr/local/lib/mariadb -lmariadb

main.o: main.c builddatabase/builddata.h queries/runqueries.h common/filereader.h
	$(CC) -D_GNU_SOURCE $(CFLAGS) -o $@ -c $< $(LIBSC)

builddatabase/builddata.o: builddatabase/builddata.c builddatabase/builddata.h builddatabase/buildmaria.h builddatabase/buildmongo.h common/filereader.h
	$(CC) -D_GNU_SOURCE $(CFLAGS) -o $@ -c $< $(LIBSC)

builddatabase/buildmaria.o: builddatabase/buildmaria.c builddatabase/buildmaria.h builddatabase/cJSON.h common/timer.h
	$(CC) -D_GNU_SOURCE $(CFLAGS) -o $@ -c $< -I/usr/local/include/mariadb

builddatabase/buildmongo.o: builddatabase/buildmongo.c builddatabase/buildmongo.h builddatabase/cJSON.h common/timer.h
	$(CC) $(CFLAGS) -o $@ -c $< $(LIBSC)

builddatabase/cJSON.o: builddatabase/cJSON.c builddatabase/cJSON.h
	$(CC) $(CFLAGS) -o $@ -c $<

common/filereader.o: common/filereader.c common/filereader.h
	$(CC) -D_GNU_SOURCE $(CFLAGS) -o $@ -c $<

common/timer.o: common/timer.c common/timer.h
	$(CC) $(CFLAGS) -o $@ -c $<

queries/runqueries.o: queries/runqueries.c queries/runqueries.h queries/mongoqueries.h common/filereader.h
	$(CC) -D_GNU_SOURCE $(CFLAGS) -o $@ -c $< $(LIBSC)

queries/mongoqueries.o: queries/mongoqueries.c queries/mongoqueries.h common/timer.h
	$(CC) $(CFLAGS) -o $@ -c $< $(LIBSC)

queries/mariaqueries.o: queries/mariaqueries.c queries/mariaqueries.h common/timer.h
	$(CC) -D_GNU_SOURCE $(CFLAGS) -o $@ -c $< -I/usr/local/include/mariadb

clean:
	rm -f *.o results.txt main
	cd builddatabase && rm -f *.o
	cd queries && rm -f *.o
	cd common && rm -f *.o
	cd csv && rm -f *.csv
