########################
FROM openjdk:8-jre

# Set default values for environment variables
ENV SERVER_PORT 8280

# Copy Default Content
COPY ./target/PropertiesExample_local-exec.jar /var/www/

# Change to working directory
WORKDIR /var/www/

# Expose Ports
EXPOSE $SERVER_PORT

# Run Http Server
CMD java -jar /var/www/PropertiesExample_local-exec.jar
########################