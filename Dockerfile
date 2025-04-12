# Use an official OpenJDK runtime as a parent image.
FROM openjdk:17-jdk-alpine

# Set the working directory to /app.
WORKDIR /app

# Copy the built jar file from the target directory.
ARG JAR_FILE=target/concert-management-server.jar
COPY ${JAR_FILE} app.jar

# Expose port 8080.
EXPOSE 8080

# Run the jar file.
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
