# MySpringBoot
This repository is a playground for learning and trying new ideas with Spring Boot

### Required Software and Tools
* Java Version: Oracle Java 1.8.0_31 (Execute **_java -version_** in command line after installation)
* Apache Maven Version: 3.2.5 (Execute **_mvn -version_** in command line after installation)
* Docker Version: 1.12.0-rc4, build e4a0dbc, experimental (Execute **_docker --version_** in command line after installation)
* PhantomJS Browser: 2.1.1 (Execute **_phantomjs --version_** in command line after installation)
* Git Client: Any latest version (Execute **_git --version_** in command line after installation)
* Integrated Development Environment: Any version IntelliJ Idea or Eclipse

### Running full build
* Start Postgresql database through docker (steps given below)
* Execute ```mvn clean install```

### Running multiple spring boot application in parallel
* Open terminal in root folder
* Execute the following to start rest third party stub and service ```mvn exec:exec@run-third-party antrun:run@wait-for-ping exec:exec@run-rest -pl RestExample/RestServiceAndConsumer```

### Docker Commands
#### Create PostgreSql Database Container (one off setup)
```
docker create --name springboot-jdbc-postgres -e POSTGRES_USER=myuser -e POSTGRES_PASSWORD=superpassword -p 5432:5432 postgres:9.4.8
```
#### Start PostgreSql Database Container
```
docker start springboot-jdbc-postgres
```
#### Check PostgreSql Database Container Logs
```
docker logs -t -f  springboot-jdbc-postgres
```
#### Stop PostgreSql Database Container
```
docker stop springboot-jdbc-postgres
```
#### Connect to PostgreSql Database
```
docker run -it --rm --link springboot-jdbc-postgres:springboot-jdbc-postgres-link postgres:9.4.8 psql --host springboot-jdbc-postgres-link --username myuser --dbname myuser --port 5432
```
Type '\q' to quit the terminal and container