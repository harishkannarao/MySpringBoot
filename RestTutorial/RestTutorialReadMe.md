### Spring Boot Plugin goodies
Run with default properties:

    mvn spring-boot:run

Run with external properties file (override the default application.properties):

    mvn spring-boot:run -Drun.arguments="--spring.config.location=../external-config/rest-service-and-consumer-external-config-live.properties"

Run with overridden default property value through command line (multiple values to be comma separated):

    mvn spring-boot:run -Drun.arguments="--server.port=9000,--quoteService.url=http://gturnquist-quoters.cfapps.io/api/random"

Debugging with suspend mode:

    mvn spring-boot:run -Drun.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005"

Debugging without suspend mode:

    mvn spring-boot:run -Drun.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"

### Java Commands to run application
Start application with default property values:

    java -jar ./RestServiceAndConsumer/target/RestServiceAndConsumer-1.0.0-SNAPSHOT.jar

Start application with external properties file (override the default application.properties):

    java -jar ./RestServiceAndConsumer/target/RestServiceAndConsumer-1.0.0-SNAPSHOT.jar --spring.config.location=./external-config/rest-service-and-consumer-external-config-live.properties

Start application with overridden default property value through command line

    java -jar ./RestServiceAndConsumer/target/RestServiceAndConsumer-1.0.0-SNAPSHOT.jar --server.port=9000 --quoteService.url=http://gturnquist-quoters.cfapps.io/api/random

Debug application with suspend mode:

    java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005 -jar ./RestServiceAndConsumer/target/RestServiceAndConsumer-1.0.0-SNAPSHOT.jar

Debug application without suspend:

    java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005 -jar ./RestServiceAndConsumer/target/RestServiceAndConsumer-1.0.0-SNAPSHOT.jar