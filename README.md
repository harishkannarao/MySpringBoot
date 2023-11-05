# MySpringBoot
This repository is a playground for learning and trying new ideas with Spring Boot

## Github Actions Build status
[![Build Status](https://github.com/harishkannarao/MySpringBoot/workflows/CI-master/badge.svg)](https://github.com/harishkannarao/MySpringBoot/actions?query=workflow%3ACI-master)

### Required Software and Tools
* Java Version: OpenJDK 17 (Execute **_java -version_** in command line after installation)
* Apache Maven Version: 3.2.5 (Execute **_mvn -version_** in command line after installation)
* Colima or Docker Desktop for Mac, Linux or Windows: latest
* Docker Cli Version: latest (`docker --version`)
* Docker Compose Version: latest (`docker-compose --version`)
* Git Client: Any latest version (`git --version`)
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
    
    mvn clean install -DskipTests=true

### Running single spring boot application

    mvn spring-boot:run -pl JdbcExample/JdbcApplication
    
Open URL in browser:

    http://localhost:8180/swagger-ui.html

### Update all dependencies

    mvn versions:use-latest-versions

    mvn versions:use-latest-releases

    mvn versions:use-latest-snapshots

    mvn versions:update-properties

    mvn versions:update-child-modules

    mvn versions:update-parent

### Running multiple spring boot application in parallel
* Open terminal in root folder
* Execute the following to start rest third party stub and service ```mvn exec:exec@run-third-party antrun:run@wait-for-ping spring-boot:run -pl RestExample/RestServiceAndConsumer```
* Verify all applications are running ```curl --header "Content-Type: application/json" -X GET "http://localhost:8080/quote"```

### Docker Commands

#### Check PostgreSql Database Container Logs
    docker logs -t -f  springboot-jdbc-postgres
    
#### Connect to PostgreSql Database
    docker run --network=docker_local_main -it --rm -e PGPASSWORD=superpassword postgres:10 psql --host springboot-jdbc-postgres --username myuser --dbname myuser --port 5432
    
Type '\q' to quit the terminal and container

#### Create a docker image for jdbc application

    docker build --pull -t com.harishkannarao/spring-boot-jdbc:latest -f JdbcExample/JdbcApplication/Dockerfile JdbcExample/JdbcApplication/target
    
#### Run the dockerised jdbc application

    docker run --network=docker_local_main -e SSH_PUBLIC_KEY -e 'REMOTE_JMX_OPTIONS=' -e 'THIRDPARTY_PING_URL=http://www.example.org' -e 'SPRING_DATASOURCE_URL=jdbc:postgresql://springboot-jdbc-postgres:5432/myuser' -e 'SPRING_DATASOURCE_USERNAME=myuser' -e 'SPRING_DATASOURCE_PASSWORD=superpassword' --rm -it --name spring-boot-jdbc -p '10022:22' -p '10006:10006' -p '8180:80' com.harishkannarao/spring-boot-jdbc:latest
    
    curl -s -X GET 'http://localhost:8180/menuentries'
    
Swagger / Open API urls:

    http://localhost:8180/api-docs
    
    http://localhost:8180/api-docs.yaml
    
    http://localhost:8180/swagger-ui.html
    
    http://localhost:8180/swagger-ui/index.html?configUrl=/api-docs/swagger-config
    
    
## Triggering github CI-deploy-master-to-aws workflow/actions using http

Set the following secrets for this repo:

* AwsAccountId
* AwsAccessKeyId
* AwsSecretAccessKey
* GithubAccessToken

Execute the following commands:
    
    export GITHUB_PERSONAL_ACCESS_TOKEN=<<your_personal_token>>

    curl -v -H "Accept: application/vnd.github.everest-preview+json" \
        -H "Authorization: token $GITHUB_PERSONAL_ACCESS_TOKEN" \
        --request POST \
        --data '{"event_type": "do-deploy-master-to-aws-development", "client_payload": { "transaction_id": "some reference"}}' \
        'https://api.github.com/repos/harishkannarao/MySpringBoot/dispatches'
