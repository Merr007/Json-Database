# Json-Database
Project of JetBrains Academy

Json-Database is single-file client-server application for setting, getting and deletion data using JSON format. Server contains multithreading functionality with corresponding synchronization.

### Stack
* Java sockets
* Multithreading (executors, synchronization, thread management and locks, Future and Callable interfaces)
* Java Collections (Map, Set, List interfaces and impementations)
* Java i/o handling (Input/output streams)
* Gson library
* Maven
* JCommander (command-line arguments handling)

### Client arguments
- -t - type of request (get, set, delete or exit)
- -k - key for getting, deletion or setting from/to database
- -v - value to be set or update in database
- -in - input with file name for the request

### Run
Firstly, run main method from server directory or using java Main command in CL (don't forget to set if necessary IP-address and port values). Then, run main method from CL providing arguments.

### Input examples
 java Main -t set -k 1 -v "text" - setting following value directly from CL
 
 java Main -t get -k 1 - getting value by its key
 
 java Main -t delete -k 1 - deletion 
 
 java Main -in file.json - getting request from received file
