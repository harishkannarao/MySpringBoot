# Debugging Spring Boot application
This document explains the commands to debug spring boot application using:
* As Java application
* Maven plugin

## Debugging as Java application

#### With Suspend Mode:

    java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005 -jar ./target/PropertiesExample_local-exec.jar

#### Without Suspend Mode:

    java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005 -jar ./target/PropertiesExample_local-exec.jar

## Debugging with Maven plugin

#### With Suspend Mode:

    mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005"

#### Without Suspend Mode:

    mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"

### Check the application:

        curl --request GET --header "Accept:application/json" --header "Content-Type:application/json" http://localhost:8280

