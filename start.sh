#!/bin/bash

# Start MongoDB
mongod --fork --logpath /var/log/mongodb/mongod.log

# Wait for MongoDB to start (adjust the sleep duration as needed)
sleep 5

# Start your Java application
java -jar /url-shortener-images.jar
