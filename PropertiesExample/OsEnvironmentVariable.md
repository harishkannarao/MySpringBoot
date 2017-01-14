# OS Environment variable to set properties
OS environment variables can be used to set or override properties of Spring Boot application

For example, to set spring.config.location property, use SPRING_CONFIG_LOCATION environment variable

### Set Environment Variables (Unix/Linux/Mac)

    export SPRING_CONFIG_LOCATION=./external-config/conf/production.yml
    export LOGGIN_CONFIG=./external-config/conf/logback-spring.xml

### Running as Java application

    java -jar ./target/PropertiesExample-exec.jar

### Running with Maven plugin

    mvn spring-boot:run

### Check the overridden values:

    curl --request GET --header "Accept:application/json" --header "Content-Type:application/json" http://localhost:8281