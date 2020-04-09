#!/bin/sh

# Make the script to abort if any command fails
set -e

# Print the commands as it is executed. Useful for debugging
set -x

$(aws ecr get-login --registry-ids $DOCKER_AWS_ACCOUNT_ID --region $DOCKER_AWS_REGION --no-include-email)

REPOSITORY_URI=$DOCKER_AWS_ACCOUNT_ID.dkr.ecr.$DOCKER_AWS_REGION.amazonaws.com/$APPLICATION_NAME/$ENVIRONMENT

GIT_SHORT_SHA=$(echo ${GITHUB_SHA} | cut -c1-8)

docker tag com.harishkannarao/spring-boot-jdbc:latest $REPOSITORY_URI:$GIT_SHORT_SHA

docker tag com.harishkannarao/spring-boot-jdbc:latest $REPOSITORY_URI:$ENVIRONMENT

docker push $REPOSITORY_URI:$GIT_SHORT_SHA

docker push $REPOSITORY_URI:$ENVIRONMENT