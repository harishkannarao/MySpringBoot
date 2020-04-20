FROM openjdk:11-jre-slim

# Install basic debugging packages
RUN apt-get update
RUN apt-get install -y curl
RUN apt-get install -y jq
RUN apt-get install -y openssh-server

# Set default values for environment variables
ENV SERVER_PORT="80"
ENV JMX_PORT="10006"
ENV SSH_PORT="22"
ENV REMOTE_JMX_OPTIONS="-Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=$JMX_PORT -Dcom.sun.management.jmxremote.rmi.port=$JMX_PORT -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.local.only=false -Djava.rmi.server.hostname=localhost"

# Copy application
COPY JdbcApplication-exec.jar /var/application/

# Change to working directory
WORKDIR /var/application/

# Expose Ports
EXPOSE $SERVER_PORT
EXPOSE $JMX_PORT
EXPOSE $SSH_PORT

HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=10 CMD curl -f http://localhost/health-check || exit 1

# Run application
CMD mkdir ~/.ssh && \
    echo $SSH_PUBLIC_KEY > ~/.ssh/authorized_keys && \
    service ssh start && \
    java \
    -XX:MaxRAMPercentage=75.0 \
    -XX:+PrintFlagsFinal \
    $REMOTE_JMX_OPTIONS \
    -jar /var/application/JdbcApplication-exec.jar