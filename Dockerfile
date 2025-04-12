# Use an official OpenJDK runtime as a parent image.
FROM openjdk:17-jdk-alpine

# Set the working directory to /app.
WORKDIR /app

# Download the wait-for-it script from GitHub and make it executable.
ADD https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

# Use an ARG to allow dynamic jar file naming. Ensure your Maven build produces a jar with this name.
ARG JAR_FILE=target/concert-management-server.jar
COPY ${JAR_FILE} app.jar

# Expose port 8080.
EXPOSE 8080

# Wait for the "db" service to be available before starting the backend application.
ENTRYPOINT ["/wait-for-it.sh", "db:3306", "--", "java", "-jar", "/app/app.jar"]

