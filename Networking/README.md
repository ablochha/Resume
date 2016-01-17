**Networking**

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

Clone or download the files from this repository. From the Networking folder, type `make`.

### Run

This program has 3 runnable files: `client`, `hmds`, and `hftpd`. Run each file in its own window/terminal/session. The client can be given the name of a directory to copy using `-d <directoryname>` and it will be copied to `~/tmp/hftpd/` by default. The client must be given a username and password as arguments. Both of the servers can be run without extra arguments.

```
./client -d ../common/ username password
./hmds
./hftpd
```

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

  Enable verbose output: set the syslog level to `DEBUG`.  Otherwise, it
  should default to `INFO`.
  
* `-f` / `--flush`

  Flushes the local Redis database.
  
**HFTPD**
  
* `-p` / `--port PORT`

  Specifies the port on which to listen for HFTP messages.

* `-r HOSTNAME` / `--redis HOSTNAME`

  Specifies the hostname of the Redis server. If not given, default to `localhost`.
  
* `-d` / `--dir ROOT`

  Specifies the directory in which uploaded files are stored.  You should
  store files as `ROOT/username/relative/path/to/file`.  If not given, default
  to `/tmp/hftpd`.

  If the directory does not exist, your program should create it.

* `-t` / `--timewait SECONDS`

  The number of seconds to spend in the *TIME_WAIT* state before exiting.  If
  not given, default to `10`.

* `-v` / `--verbose`

  Enable verbose output (see description later).  If not given, default to
  non-verbose output.
