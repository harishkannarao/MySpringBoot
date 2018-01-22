# MySpringBoot
This repository is a playground for learning and trying new ideas with Spring Boot

### Travis CI Build status
[![Build Status](https://travis-ci.org/harishkannarao/MySpringBoot.svg?branch=master)](https://travis-ci.org/harishkannarao/MySpringBoot)

### Required Software and Tools
* Java Version: Oracle Java 1.8.0_31 (Execute **_java -version_** in command line after installation)
* Apache Maven Version: 3.2.5 (Execute **_mvn -version_** in command line after installation)
* Docker Version: Docker version 17.03.1-ce, build c6d412e (Execute **_docker --version_** in command line after installation)
* Docker Compose Version: docker-compose version 1.11.2, build dfed245 (Execute **_docker-compose --version_** in command line after installation)
* PhantomJS Browser: 2.1.1 (Execute **_phantomjs --version_** in command line after installation)
* Git Client: Any latest version (Execute **_git --version_** in command line after installation)
* Integrated Development Environment: Any version IntelliJ Idea or Eclipse

### Docker dependencies
Docker dependencies needs to be started using docker-compose before the build
##### Pull the latest images of docker services
    docker-compose -f docker_local/docker-compose.yml pull
##### Start docker services
    docker-compose -f docker_local/docker-compose.yml up --build -d
##### Stop docker services
    docker-compose -f docker_local/docker-compose.yml down -v

### Running full build
* Start Postgresql database through docker (steps given below)
* Execute ```mvn clean install```

### Generate artifacts only
Will skip unit tests, integration tests and docker commands
    
    mvn clean install -DskipTests=true -DskipDockerDependency=true -DskipDockerBuild=true

### Running multiple spring boot application in parallel
* Open terminal in root folder
* Execute the following to start rest third party stub and service ```mvn exec:exec@run-third-party antrun:run@wait-for-ping spring-boot:run -pl RestExample/RestServiceAndConsumer```

### Docker Commands
#### Check PostgreSql Database Container Logs
    docker logs -t -f  springboot-jdbc-postgres
#### Connect to PostgreSql Database
    docker run --network=dockerlocal_main -it --rm postgres:9.4.8 psql --host springboot-jdbc-postgres --username myuser --dbname myuser --port 5432
    
Type '\q' to quit the terminal and container