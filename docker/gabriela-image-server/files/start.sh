#!/usr/bin/env ash

# Because the config file is on this folder, we need to cd to there first
cd /home/gabriela-image-server/

# Allow Docker to control the max memory
# https://merikan.com/2019/04/jvm-in-a-container/#java-10
java -XX:MinRAMPercentage=60.0 -XX:MaxRAMPercentage=90.0 -XX:+HeapDumpOnOutOfMemoryError -jar /home/gabriela-image-server/image-generation-server-fat-1.0-SNAPSHOT.jar