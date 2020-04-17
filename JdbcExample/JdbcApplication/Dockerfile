FROM openjdk:11-jre-slim

# Install basic debugging packages
RUN apt-get update
RUN apt-get install -y curl
RUN apt-get install -y jq

# Set default values for environment variables
ENV SERVER_PORT 80

# Copy application
COPY JdbcApplication-exec.jar /var/application/

# Change to working directory
WORKDIR /var/application/

# Expose Ports
EXPOSE $SERVER_PORT

HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=10 CMD curl -f http://localhost/health-check || exit 1

# Run application
CMD java -XX:MaxRAMPercentage=75.0 -XX:+PrintFlagsFinal -jar /var/application/JdbcApplication-exec.jar