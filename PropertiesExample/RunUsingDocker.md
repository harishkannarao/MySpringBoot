# Running Spring Boot application as Docker container
This document explains the commands to run spring boot application using docker

### Generate the artifact (jar) file

    mvn clean install -DskipTests=true
    
### Build the docker image

    docker build -t harishkannarao/properties_example:latest -f Dockerfile .
    
### Run as a docker container

    docker run -it --rm -p "8280:8280" harishkannarao/properties_example:latest

### Check the application:

    curl --request GET --header "Accept:application/json" --header "Content-Type:application/json" "http://localhost:8280"

