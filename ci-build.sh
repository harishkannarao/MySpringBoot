#!/bin/sh

# Make the script to abort if any command fails
set -e

# Print the commands as it is executed. Useful for debugging
set -x

docker-compose -f docker_local/docker-compose.yml pull

docker-compose -f docker_local/docker-compose.yml up --build -d

mvn clean install --batch-mode -P run-in-ci -DskipDockerBuild=true -DchromeDriverBinary=/usr/lib/chromium-browser/chromedriver -DchromeBinary=/usr/bin/chromium-browser -DchromeHeadless=true