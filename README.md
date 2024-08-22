# wikimedia-stream-processor
Application retrieves recent change events from [Wikimedia's](https://stream.wikimedia.org/) 
[SSE (Server-sent events)](https://en.wikipedia.org/wiki/Server-sent_events) endpoint, process them and send them to a 
kafka topic to be consumed.