CC      = gcc
CFLAGS  = -g -O0 -Wall -Werror -std=gnu11 -lz
LDFLAGS = -L.

.PHONY : all clean

all: client

client: client.o hftp_client.o hmdp_client.o tcp_client.o udp_client.o directoryscanner.o ../common/argumentparser.o ../common/checksum.o ../common/client_structs.o ../common/filereader.o ../common/hftp.o ../common/tcp_sockets.o ../common/udp_sockets.o ../common/linkedlist.o ../common/hmdp.o ../common/hmdpbuilder.o
	$(CC) -o $@ $^ $(LDFLAGS) -lz

client.o: client.c hftp_client.h hmdp_client.h tcp_client.h udp_client.h directoryscanner.h ../common/argumentparser.h ../common/checksum.h ../common/client_structs.h ../common/filereader.h ../common/hftp.h ../common/tcp_sockets.h ../common/udp_sockets.h ../common/linkedlist.h ../common/hmdp.h ../common/hmdpbuilder.h
	$(CC) $(CFLAGS) -o $@ -c $<

directoryscanner.o: directoryscanner.c directoryscanner.h ../common/checksum.h ../common/filereader.h ../common/linkedlist.h
	$(CC) $(CFLAGS) -o $@ -c $<

hmdp_client.o: hmdp_client.c hmdp_client.h tcp_client.h ../common/client_structs.h ../common/linkedlist.h ../common/hmdp.h
	$(CC) $(CFLAGS) -o $@ -c $<

hftp_client.o: hftp_client.c hftp_client.h udp_client.h ../common/client_structs.h ../common/filereader.h ../common/hftp.h ../common/linkedlist.h ../common/udp_sockets.h
	$(CC) $(CFLAGS) -o $@ -c $<

tcp_client.o: tcp_client.c tcp_client.h ../common/tcp_sockets.h
	$(CC) $(CFLAGS) -o $@ -c $<

udp_client.o: udp_client.c udp_client.h ../common/udp_sockets.h
	$(CC) $(CFLAGS) -o $@ -c $<

../common/argumentparser.o: ../common/argumentparser.c ../common/argumentparser.h ../common/client_structs.h
	$(CC) $(CFLAGS) -o $@ -c $<

../common/checksum.o: ../common/checksum.c ../common/checksum.h
	$(CC) -D_GNU_SOURCE $(CFLAGS) -o $@ -c $<

../common/client_structs.o: ../common/client_structs.c ../common/client_structs.h ../common/linkedlist.h
	$(CC) $(CFLAGS) -o $@ -c $<

../common/filereader.o: ../common/filereader.c ../common/filereader.h 
	$(CC) $(CFLAGS) -o $@ -c $<

../common/hftp.o: ../common/hftp.c ../common/hftp.h ../common/filereader.h ../common/udp_sockets.h
	$(CC) $(CFLAGS) -o $@ -c $<

../common/tcp_sockets.o: ../common/tcp_sockets.c ../common/tcp_sockets.h
	$(CC) $(CLFAGS) -o $@ -c $<

../common/udp_sockets.o: ../common/udp_sockets.c ../common/udp_sockets.h
	$(CC) $(CFLAGS) -o $@ -c $<

../common/linkedlist.o: ../common/linkedlist.c ../common/linkedlist.h
	$(CC) $(CLFAGS) -o $@ -c $<

../common/hmdp.o: ../common/hmdp.c ../common/hmdp.h ../common/hmdpbuilder.h
	$(CC) -D_GNU_SOURCE $(CFLAGS) -o $@ -c $<

../common/hmdpbuilder.o: ../common/hmdpbuilder.c ../common/hmdpbuilder.h
	$(CC) $(CFLAGS) -o $@ -c $<

clean:
	rm -f *.o *.a *.txt client
	cd ../common/ && rm -f *.o
