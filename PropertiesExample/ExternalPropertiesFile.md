# External properties
This document explains how to use the external properties file or override the default property values using external properties file.

### Running as Java application

    java -jar ./target/PropertiesExample-exec.jar --spring.config.location=./external-config/conf/production.properties --logging.config=./external-config/conf/logback-spring.xml

### Running with Maven plugin

    mvn spring-boot:run -Drun.arguments="--spring.config.location=./external-config/conf/production.properties,--logging.config=./external-config/conf/logback-spring.xml"

### Check the overridden values:

    curl --request GET --header "Accept:application/json" --header "Content-Type:application/json" http://localhost:8281

**When using external properties file, the available properties takes precedence over default properties, so if a given property is not set in external properties file, then the default value specified in application.properties is used**


