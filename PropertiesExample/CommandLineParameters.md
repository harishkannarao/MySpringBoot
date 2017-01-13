# Command Line Parameters
This document explains how to use the command line parameters to override default property values.

### Running as Java application

    java -jar ./target/PropertiesExample.jar --server.port=8281 --custom.property1=production.value

### Running with Maven plugin

    mvn spring-boot:run -Drun.arguments="--server.port=8281,--custom.property1=production.value"

### Check the overridden values:

    curl --request GET --header "Accept:application/json" --header "Content-Type:application/json" http://localhost:8281

**When using external properties file, the available properties takes precedence over default properties, so if a given property is not set in external properties file, then the default value specified in application.properties is used**


