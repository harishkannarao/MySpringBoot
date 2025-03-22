#!/bin/sh

# Make the script to abort if any command fails
set -e

# Print the commands as it is executed. Useful for debugging
set -x

docker pull public.ecr.aws/docker/library/postgres:16

mvn clean install --batch-mode -P run-in-ci -DskipDockerBuild=true