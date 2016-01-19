## Hooli Database [Download](https://github.com/ablochha/Resume/blob/master/Networking/Networking.zip?raw=true)

This program was developed to run on Ubuntu 14.04.

The Hooli Database client lets the user upload files to a server. Each file's checksum is calculated and the filenames and checksums are checked against the Hooli Database (Redis), and only the files that have been locally updated will be uploaded. 

The Hooli Metadata server attempts to sync any clients that connect to it with the Hooli Database. If the client successfully authenticates with the database, the server will check to see which of the client's files need to be uploaded and will request them from the client. The [Hooli Metadata Protocol](https://gist.github.com/jsuwo/e9f4c35e0e78a9662e59) is used to communicate with the metadata server.

The Hooli File Transfer server attempts to receive files from a client. If the client was requested to upload files, then the transfer server attempts to validate the client's session token. If authenticated, client will transfer files according to the [Hooli File Transfer Protocol](https://gist.github.com/jsuwo/a4b827136bb62bf64b74). 

Since you will be running the client and the servers locally, the files you transfer will be end up in your `~/tmp/hftpd/` directory. This directory can be changed with a command line argument.

### Prerequisites

* Ability to make and compile C code

* Install `zlib`

```
sudo apt-get install zlib1g-dev
```

* Install Redis server 

```
sudo apt-get install redis-server
```

* Install `libhiredis`

```
sudo apt-get install libhiredis-dev
```

* Install the `check` library

```
sudo apt-get install check
```

### Build

Clone or download the files from this repository. From the Networking directory, type `make`.

### Run

This program has 3 runnable files: `client`, `hmds`, and `hftpd`. The client can be given the name of a directory to copy using `-d <directoryname>` and it will be copied to `~/tmp/hftpd/` by default. The client must be given a username and password as arguments. Both of the servers can be run without extra arguments.

Make sure both servers are running before starting the client.

From the hmds directory, type  

```
./hmds
```
In another window, from the hftpd directory, type

```
./hftpd
```

In another window, from the client directory, type

```
./client -d ../common/ username password
```

### Exit

To shut down a running server, press ctrl+c.

### Command line arguments

**Client**

* `-s HOSTNAME` / `--server HOSTNAME`

  Specifies the server's hostname.  If not given, default to `localhost`.

* `-p PORT` / `--port PORT`

  Specifies the server's port.  If not given, default to `9000`.

* `-d DIR` / `--dir DIR`

  Specifies the Hooli root directory.  If not given, default to `~/hooli`.

* `-v` / `--verbose`

  Enable verbose output: set the syslog level to `DEBUG`.  Otherwise, it should default to `INFO`.
  
* `-f` / `--fserver HOSTNAME`

  Specifies the hostname on which the HFTP server is listening.  If not given,
  default to `localhost`.

* `-o` / `--fport PORT`

  Specifies the port on which the HFTP server is listening.  If not given,
  default to `10000`.
  
**HMDS**

* `-p PORT` / `--port PORT`

  Specifies the port on which to listen.  If not given, default to `9000`.

* `-r HOSTNAME` / `--redis HOSTNAME`

  Specifies the hostname of the Redis server.  If not given, default to
  `localhost`.

* `-v` / `--verbose`

  Enable verbose output: set the syslog level to `DEBUG`.  Otherwise, it should default to `INFO`.
  
* `-f` / `--flush`

  Flushes the local Redis database.
  
**HFTPD**
  
* `-p` / `--port PORT`

  Specifies the port on which to listen for HFTP messages. If not given, default to `10000`.

* `-r HOSTNAME` / `--redis HOSTNAME`

  Specifies the hostname of the Redis server. If not given, default to `localhost`.
  
* `-d` / `--dir ROOT`

  Specifies the directory in which uploaded files are stored.  You should
  store files as `ROOT/username/relative/path/to/file`.  If not given, default
  to `/tmp/hftpd`.

* `-t` / `--timewait SECONDS`

  The number of seconds to spend in the *TIME_WAIT* state before exiting.  If
  not given, default to `10`.

* `-v` / `--verbose`

  Enable verbose output: set the syslog level to `DEBUG`.  Otherwise, it should default to `INFO`.
