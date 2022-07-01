#!/bin/bash

cd /home/arch/OdiparPack-Back
git pull
./mvnw clean install
java -jar target/OdiparPack-Back-0.0.1-SNAPSHOT.jar
