# Builder
FROM maven:3.9.5-eclipse-temurin-17 AS builder

WORKDIR /workspace
COPY pom.xml .
RUN mvn -B dependency:resolve dependency:resolve-plugins

COPY src ./src
RUN mvn -B clean package -DskipTests

# Runtime
FROM eclipse-temurin:17-jre-alpine

RUN apk add --no-cache bash curl

WORKDIR /app
ADD https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

COPY --from=builder /workspace/target/concertManagement_Server-0.0.1-SNAPSHOT.jar /app/app.jar


EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=10s --start-period=20s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

EXPOSE 80
ENTRYPOINT ["java","-jar","/app/app.jar","--server.port=80"]
