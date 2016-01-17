CC      = gcc
CFLAGS  = -g -O0 -Wall -Werror -std=gnu11
LDFLAGS = -L../hdb/

.PHONY : all clean

all: hftpd ../hdb/libhdb.a

../hdb/libhdb.a: ../hdb/hdb.o
	ar rcs ../hdb/libhdb.a ../hdb/hdb.o

../hdb/hdb.o: ../hdb/hdb.c ../hdb/hdb.h
	$(CC) $(CFLAGS) -o $@ -c $<

hftpd: hftpd.o hftp_server.o udp_server.o ../common/argumentparser.o ../common/checksum.o ../common/filereader.o ../common/hftp.o ../common/puts3.o ../common/tcp_sockets.o ../common/udp_sockets.o ../common/linkedlist.o ../hdb/libhdb.a
	$(CC) -o $@ $^ $(LDFLAGS) -lhdb -lhiredis -lz -ls3 -L/usr/local/lib -laws_dynamo

hftpd.o: hftpd.c hftp_server.h udp_server.h ../common/argumentparser.h ../common/checksum.h ../common/filereader.h ../common/hftp.h ../common/puts3.h ../common/tcp_sockets.h ../common/udp_sockets.h ../common/linkedlist.h ../hdb/hdb.h
	$(CC) $(CFLAGS) -o $@ -c $<

hftp_server.o: hftp_server.c hftp_server.h ../common/checksum.h ../common/filereader.h ../common/hftp.h ../common/udp_sockets.h ../hdb/hdb.h

udp_server.o: udp_server.c udp_server.h ../common/udp_sockets.h
	$(CC) $(CFLAGS) -o $@ -c $<

../common/argumentparser.o: ../common/argumentparser.c ../common/argumentparser.h
	$(CC) $(CFLAGS) -o $@ -c $<

../common/checksum.o: ../common/checksum.c ../common/checksum.h
	$(CC) -D_GNU_SOURCE $(CFLAGS) -o $@ -c $<

../common/filereader.o: ../common/filereader.c ../common/filereader.h
	$(CC) $(CFLAGS) -o $@ -c $<

../common/hftp.o: ../common/hftp.c ../common/hftp.h ../common/filereader.h ../common/udp_sockets.h
	$(CC) $(CFLAGS) -o $@ -c $<

../common/puts3.o: ../common/puts3.c ../common/puts3.h
	$(CC) $(CFLAGS) -o $@ -c $<

../common/tcp_sockets.o: ../common/tcp_sockets.c ../common/tcp_sockets.h
	$(CC) $(CFLAGS) -o $@ -c $<

../common/udp_sockets.o: ../common/udp_sockets.c ../common/udp_sockets.h
	$(CC) $(CFLAGS) -o $@ -c $<

../common/linkedlist.o: ../common/linkedlist.c ../common/linkedlist.h
	$(CC) $(CFLAGS) -o $@ -c $<

clean:
	rm -f *.o *.a hftpd
	cd ../common/ && rm -f *.o
	cd ../hdb/ && rm -f *.o *.a
