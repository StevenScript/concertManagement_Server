##########################
# 1) Builder stage – Maven
##########################
FROM maven:3.9.5-eclipse-temurin-17 AS builder
ARG GIT_SHA
LABEL org.opencontainers.image.revision=$GIT_SHA

WORKDIR /workspace

# leverage Docker cache for dependencies
COPY pom.xml .
RUN mvn -B dependency:resolve dependency:resolve-plugins

# copy and build
COPY src ./src
RUN mvn -B clean package -DskipTests

###################################
# 2) Runtime stage – tiny JRE+alpine
###################################
FROM eclipse-temurin:17-jre-alpine
ARG GIT_SHA
LABEL org.opencontainers.image.revision=$GIT_SHA

# install helpers + useradd
RUN apk add --no-cache bash curl shadow

# create non-root user/group with UID=1001
RUN addgroup -g 1001 appgroup \
    && adduser -S -u 1001 -G appgroup appuser

WORKDIR /app

# grab wait-for-it to block until DB is ready
ADD https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

# copy the fat‐jar
COPY --from=builder /workspace/target/*jar /app/app.jar

EXPOSE 8080

# switch to the non-root user
USER appuser

ENTRYPOINT ["/wait-for-it.sh", "db:3306", "--", "java", "-jar", "/app/app.jar"]
