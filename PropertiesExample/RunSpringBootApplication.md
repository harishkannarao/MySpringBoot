# Running Spring Boot application
This document explains the commands to run spring boot application using:
* As Java application
* Maven plugin

### Running as Java application

    java -jar ./target/PropertiesExample_local-exec.jar

### Running with Maven plugin

    mvn spring-boot:run

### Check the application:

        curl --request GET --header "Accept:application/json" --header "Content-Type:application/json" http://localhost:8280

