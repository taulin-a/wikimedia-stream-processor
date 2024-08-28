# wikimedia-stream-processor
Application retrieves recent change events from [Wikimedia's](https://stream.wikimedia.org/) 
[SSE (Server-sent events)](https://en.wikipedia.org/wiki/Server-sent_events) endpoint, process them and send them to a 
kafka topic to be consumed.
## Requirements
- [Java 21](https://github.com/adoptium/temurin21-binaries/releases/tag/jdk-21.0.4%2B7)
- [Maven](https://maven.apache.org/download.cgi)
## Running Project
To run the project you execute the script **run.sh** in terminal:
```
./run.sh
```