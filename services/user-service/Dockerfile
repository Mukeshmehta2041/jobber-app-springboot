# ========================
# Build Stage
# ========================
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build

# Set working directory
WORKDIR /app

# Copy the top-level pom.xml
COPY pom.xml .

# Create dummy modules to satisfy the multi-module structure during `mvn install`
RUN mkdir -p api-gateway auth-service service-registry config-server

# Create minimal pom.xml files for placeholder modules
RUN echo '<?xml version="1.0" encoding="UTF-8"?><project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd"><modelVersion>4.0.0</modelVersion><parent><groupId>com.jobber</groupId><artifactId>jobber-app</artifactId><version>1.0-SNAPSHOT</version></parent><artifactId>api-gateway</artifactId></project>' > api-gateway/pom.xml

RUN echo '<?xml version="1.0" encoding="UTF-8"?><project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd"><modelVersion>4.0.0</modelVersion><parent><groupId>com.jobber</groupId><artifactId>jobber-app</artifactId><version>1.0-SNAPSHOT</version></parent><artifactId>auth-service</artifactId></project>' > auth-service/pom.xml

RUN echo '<?xml version="1.0" encoding="UTF-8"?><project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd"><modelVersion>4.0.0</modelVersion><parent><groupId>com.jobber</groupId><artifactId>jobber-app</artifactId><version>1.0-SNAPSHOT</version></parent><artifactId>service-registry</artifactId></project>' > service-registry/pom.xml

RUN echo '<?xml version="1.0" encoding="UTF-8"?><project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd"><modelVersion>4.0.0</modelVersion><parent><groupId>com.jobber</groupId><artifactId>jobber-app</artifactId><version>1.0-SNAPSHOT</version></parent><artifactId>config-server</artifactId></project>' > config-server/pom.xml

# Copy actual modules
COPY common ./common
COPY user-service ./user-service

# Build common first, then user-service
RUN mvn clean install -pl common -am -DskipTests
RUN mvn clean package -pl user-service -am -DskipTests

# ========================
# Runtime Stage
# ========================
FROM eclipse-temurin:21-jre-alpine

# Create app user for non-root security
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

# Set working directory
WORKDIR /app

# Copy built JAR from previous stage
COPY --from=build /app/user-service/target/*.jar app.jar

# Change file permissions
RUN chown -R appuser:appgroup /app

# Run as non-root user
USER appuser

# Expose service port (adjust if needed)
EXPOSE 8082

# Healthcheck endpoint (optional)
HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8082/actuator/health || exit 1

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
