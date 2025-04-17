# ──────────────────────────────
# 1) Builder stage – uses Maven
# ──────────────────────────────
FROM maven:3.9.5-eclipse-temurin-17 AS builder

# Copy pom.xml first to leverage Docker cache for deps
WORKDIR /workspace
COPY pom.xml .
RUN mvn -B dependency:resolve dependency:resolve-plugins

# Copy the actual source and build
COPY src ./src
RUN mvn -B clean package -DskipTests

# ────────────────────────────────
# 2) Runtime stage – use tiny JRE
# ────────────────────────────────
FROM eclipse-temurin:17-jre-alpine

# Install Bash (for wait‑for‑it) and curl (if you need it)
RUN apk add --no-cache bash curl

WORKDIR /app

# Grab wait‑for‑it
ADD https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

# Copy the fat jar from the builder
# (the shaded/boot jar is the only file ending with ".jar" in target)
COPY --from=builder /workspace/target/*jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["/wait-for-it.sh", "db:3306", "--", "java", "-jar", "/app/app.jar"]
