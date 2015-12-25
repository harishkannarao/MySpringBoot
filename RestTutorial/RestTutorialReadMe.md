### Spring Boot Plugin goodies
Debugging With suspend mode:
**mvn spring-boot:run -Drun.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005"**

Debugging With suspend mode:
**mvn spring-boot:run -Drun.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"**

Pass command line arguments for spring boot application (override property values):
**mvn spring-boot:run -Drun.arguments="--spring.config.location=../external-config/rest-service-and-consumer-external-config-live.properties"**


### Java Commands to run application
Start application with default property values:
**java -jar ./RestServiceAndConsumer/target/RestServiceAndConsumer-1.0.0-SNAPSHOT.jar**

Start application with overridden external property values:
**java -jar ./RestServiceAndConsumer/target/RestServiceAndConsumer-1.0.0-SNAPSHOT.jar --spring.config.location=./external-config/rest-service-and-consumer-external-config-live.properties**

Start application in debug mode
**java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005 -jar ./RestServiceAndConsumer/target/RestServiceAndConsumer-1.0.0-SNAPSHOT.jar**