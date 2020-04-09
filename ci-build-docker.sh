#!/bin/sh

# Make the script to abort if any command fails
set -e

# Print the commands as it is executed. Useful for debugging
set -x

docker build --pull -t com.harishkannarao/spring-boot-jdbc:latest -f JdbcExample/JdbcApplication/Dockerfile JdbcExample/JdbcApplication/target