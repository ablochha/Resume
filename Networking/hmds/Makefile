CC      = gcc
CFLAGS  = -g -O0 -Wall -Werror -std=gnu11
LDFLAGS = -L../hdb/

.PHONY : all clean

all: hmds ../common/hmdpbuilder.o ../hdb/libhdb.a

../hdb/libhdb.a: ../hdb/hdb.o
	ar rcs ../hdb/libhdb.a ../hdb/hdb.o

../hdb/hdb.o: ../hdb/hdb.c ../hdb/hdb.h
	$(CC) $(CFLAGS) -o $@ -c $<

hmds: hmds.o hmdp_server.o tcp_server.o ../common/argumentparser.o ../common/client_structs.o ../common/tcp_sockets.o ../common/linkedlist.o ../common/hmdp.o ../common/hmdpbuilder.o ../hdb/libhdb.a
	$(CC) -o $@ $^ $(LDFLAGS) -lhdb -lhiredis

hmds.o: hmds.c hmdp_server.h tcp_server.h ../common/argumentparser.h ../common/client_structs.h ../common/tcp_sockets.h ../common/linkedlist.h ../common/hmdp.h ../common/hmdpbuilder.h ../hdb/hdb.h
	$(CC) $(CFLAGS) -o $@ -c $<

hmdp_server.o: hmdp_server.c hmdp_server.h ../common/client_structs.h ../common/hmdp.h ../common/linkedlist.h ../hdb/hdb.h
	$(CC) $(CFLAGS) -o $@ -c $<

tcp_server.o: tcp_server.c tcp_server.h ../common/tcp_sockets.h
	$(CC) $(CFLAGS) -o $@ -c $<

../common/argumentparser.o: ../common/argumentparser.c ../common/argumentparser.h ../common/client_structs.h
	$(CC) $(CFLAGS) -o $@ -c $<

../common/client_structs.o: ../common/client_structs.c ../common/client_structs.h ../common/linkedlist.h
	$(CC) $(CFLAGS) -o $@ -c $<

../common/tcp_sockets.o: ../common/tcp_sockets.c ../common/tcp_sockets.h
	$(CC) $(CFLAGS) -o $@ -c $<

../common/linkedlist.o: ../common/linkedlist.c ../common/linkedlist.h
	$(CC) $(CFLAGS) -o $@ -c $<

../common/hmdp.o: ../common/hmdp.c ../common/hmdp.h ../common/hmdpbuilder.h
	$(CC) -D_GNU_SOURCE $(CFLAGS) -o $@ -c $<

../common/hmdpbuilder.o: ../common/hmdpbuilder.c ../common/hmdpbuilder.h
	$(CC) $(CFLAGS) -o $@ -c $<

clean:
	rm -f *.o *.a hmds
	cd ../common/ && rm -f *.o
	cd ../hdb/ && rm -f *.o *.a
