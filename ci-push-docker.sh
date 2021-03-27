#!/bin/sh

# Make the script to abort if any command fails
set -e

# Print the commands as it is executed. Useful for debugging
set -x

aws ecr get-login-password --region $DOCKER_AWS_REGION | docker login --username AWS --password-stdin $DOCKER_AWS_ACCOUNT_ID.dkr.ecr.$DOCKER_AWS_REGION.amazonaws.com

REPOSITORY_URI=$DOCKER_AWS_ACCOUNT_ID.dkr.ecr.$DOCKER_AWS_REGION.amazonaws.com/$APPLICATION_NAME/$ENVIRONMENT

GIT_SHORT_SHA=$(echo $GITHUB_SHA | cut -c1-7)

docker tag com.harishkannarao/spring-boot-jdbc:latest $REPOSITORY_URI:$GIT_SHORT_SHA

docker tag com.harishkannarao/spring-boot-jdbc:latest $REPOSITORY_URI:$ENVIRONMENT

docker push $REPOSITORY_URI:$GIT_SHORT_SHA

docker push $REPOSITORY_URI:$ENVIRONMENT

aws ecs update-service --cluster $APPLICATION_NAME-$ENVIRONMENT-ecs-cluster --service $APPLICATION_NAME-$ENVIRONMENT --force-new-deployment