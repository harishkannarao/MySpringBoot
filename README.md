# MySpringBoot
This repository is a playground for learning and trying new ideas with Spring Boot

### Required Software and Tools
* Java Version: Oracle Java 1.8.0_31 (Execute **_java -version_** in command line after installation)
* Apache Maven Version: 3.2.5 (Execute **_mvn -version_** in command line after installation)
* Postgresql Database Version: 9.4.4
* Git Client: Any latest version (Execute **_git --version_** in command line after installation)
* Integrated Development Environment: Any version IntelliJ Idea or Eclipse

### Pre-requisite to run the build
* Postgresql database should be setup and run in localhost:5432. Setup guide is available in the following [google doc] (https://docs.google.com/document/d/1INfxu8PEwMnzdDk_fli2WasbFk9rKRw3yZl3SfVDCFM/edit)
  
### Running full build
mvn clean install

### Run an application with spring boot maven plugin
mvn spring-boot:run

### Run an application in debug mode with spring boot maven plugin
mvn spring-boot:run -Drun.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005"

### Run an application with properties overriden by external properties file with spring boot maven plugin
mvn spring-boot:run -Drun.arguments="--spring.config.location={{absolute or relative path of property file}}"

e.g *mvn spring-boot:run -Drun.arguments="--spring.config.location=../external-config/rest-service-and-consumer-external-config-live.properties"*

### Run an application with spring boot generated jar
java -jar "./RestTutorial/RestServiceAndConsumer/target/RestServiceAndConsumer-1.0.0-SNAPSHOT.jar"

### Run an application in debug mode with spring boot generated jar
java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005 -jar "./RestTutorial/RestServiceAndConsumer/target/RestServiceAndConsumer-1.0.0-SNAPSHOT.jar"

### Run an application with properties overriden by external properties file with spring boot generated jar
java -jar "./RestTutorial/RestServiceAndConsumer/target/RestServiceAndConsumer-1.0.0-SNAPSHOT.jar" --spring.config.location="./RestTutorial/external-config/rest-service-and-consumer-external-config-live.properties"
