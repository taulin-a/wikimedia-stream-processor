#!/bin/bash

mvn clean install
mvn package
java -jar ./target/wikimedia-stream-processor-1.0-SNAPSHOT-jar-with-dependencies.jar

