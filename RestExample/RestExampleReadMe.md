### Spring Boot Plugin goodies
Run with default properties:

    mvn spring-boot:run

Run with external properties file (override the default application.properties):

    mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Dspring.config.location=classpath:/application.properties,./external-config/rest-service-and-consumer-external-config-live.properties"

Run with overridden default property value through command line (multiple values to be comma separated):

    mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Dserver.port=9000 -DquoteService.url=http://gturnquist-quoters.cfapps.io/api/random"

Run with overridden default property value through environment variable:

Set the environment variable as **SERVER_PORT=9000**

Set the environment variable as **QUOTESERVICE_URL=http://gturnquist-quoters.cfapps.io/api/random**

    mvn spring-boot:run

Debugging with suspend mode:

    mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005"

Debugging without suspend mode:

    mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"

### Java Commands to run application
Start application with default property values:

    java -jar ./RestServiceAndConsumer/target/RestServiceAndConsumer_local-exec.jar

Start application with external properties file (override the default application.properties):

    java -Dspring.config.location=classpath:/application.properties,./external-config/rest-service-and-consumer-external-config-live.properties -jar ./RestServiceAndConsumer/target/RestServiceAndConsumer_local-exec.jar

Start application with overridden default property value through command line:

    java -Dserver.port=9000 -DquoteService.url=http://gturnquist-quoters.cfapps.io/api/random -jar ./RestServiceAndConsumer/target/RestServiceAndConsumer_local-exec.jar

Start application with overridden default property value through environment variable:

Set the environment variable as **SERVER_PORT=9000**

Set the environment variable as **QUOTESERVICE_URL=http://gturnquist-quoters.cfapps.io/api/random**

    java -jar ./RestServiceAndConsumer/target/RestServiceAndConsumer_local-exec.jar

Debug application with suspend mode:

    java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005 -jar ./RestServiceAndConsumer/target/RestServiceAndConsumer_local-exec.jar

Debug application without suspend:

    java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005 -jar ./RestServiceAndConsumer/target/RestServiceAndConsumer_local-exec.jar