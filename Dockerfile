# Use an official OpenJDK runtime as a parent image.
FROM openjdk:17-jdk-alpine

# Install Bash (Alpine uses "apk" package manager)
RUN apk add --no-cache bash

# Set the working directory to /app.
WORKDIR /app

# Download the wait-for-it script from GitHub and make it executable.
ADD https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

# Use an ARG to allow dynamic jar file naming.
ARG JAR_FILE=target/concert-management-server.jar
COPY ${JAR_FILE} app.jar

# Expose port 8080.
EXPOSE 8080

# Use the wait-for-it script to wait for the db on port 3306, then start the app.
ENTRYPOINT ["/wait-for-it.sh", "db:3306", "--", "java", "-jar", "/app/app.jar"]
