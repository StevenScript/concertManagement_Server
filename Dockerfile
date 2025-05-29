##############################
# 1) Builder stage – Maven   #
##############################
FROM maven:3.9.5-eclipse-temurin-17 AS builder

# leverage cache for dependencies
WORKDIR /workspace
COPY pom.xml .
RUN mvn -B dependency:resolve dependency:resolve-plugins

# copy sources & build
COPY src ./src
RUN mvn -B clean package -DskipTests

###########################################
# 2) Runtime stage – tiny Alpine JRE 🍃   #
###########################################
FROM eclipse-temurin:17-jre-alpine

# install bash + curl
RUN apk add --no-cache bash curl

WORKDIR /app

# grab wait-for-it and make it executable
ADD https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

# copy our fat jar from builder
COPY --from=builder /workspace/target/*jar /app/app.jar

EXPOSE 8080

# explicitly invoke via bash (avoids “permission denied” on script)
ENTRYPOINT ["bash", "/wait-for-it.sh", "db:3306", "--", "java", "-jar", "/app/app.jar"]
