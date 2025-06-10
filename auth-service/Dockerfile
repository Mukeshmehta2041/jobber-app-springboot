# Multi-stage build for auth-service
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build

# Set working directory
WORKDIR /app

# Copy parent pom.xml first for dependency resolution
COPY pom.xml .

# Create dummy pom.xml files for missing modules to avoid Maven errors
RUN mkdir -p api-gateway user-service service-registry config-server

# Create minimal pom.xml files for missing modules
RUN echo '<?xml version="1.0" encoding="UTF-8"?><project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd"><modelVersion>4.0.0</modelVersion><parent><groupId>com.jobber</groupId><artifactId>jobber-app</artifactId><version>1.0-SNAPSHOT</version></parent><artifactId>api-gateway</artifactId></project>' > api-gateway/pom.xml

RUN echo '<?xml version="1.0" encoding="UTF-8"?><project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd"><modelVersion>4.0.0</modelVersion><parent><groupId>com.jobber</groupId><artifactId>jobber-app</artifactId><version>1.0-SNAPSHOT</version></parent><artifactId>user-service</artifactId></project>' > user-service/pom.xml

RUN echo '<?xml version="1.0" encoding="UTF-8"?><project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd"><modelVersion>4.0.0</modelVersion><parent><groupId>com.jobber</groupId><artifactId>jobber-app</artifactId><version>1.0-SNAPSHOT</version></parent><artifactId>service-registry</artifactId></project>' > service-registry/pom.xml

RUN echo '<?xml version="1.0" encoding="UTF-8"?><project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd"><modelVersion>4.0.0</modelVersion><parent><groupId>com.jobber</groupId><artifactId>jobber-app</artifactId><version>1.0-SNAPSHOT</version></parent><artifactId>config-server</artifactId></project>' > config-server/pom.xml

# Copy actual modules we need
COPY common/pom.xml ./common/
COPY auth-service/pom.xml ./auth-service/

# Copy common module source (since auth-service depends on it)
COPY common/src ./common/src

# Copy auth-service source
COPY auth-service/src ./auth-service/src

# Build the application
# First build common module, then auth-service
RUN mvn clean install -pl common -am
RUN mvn clean package -pl auth-service -am -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine

# Create app user for security
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

# Set working directory
WORKDIR /app

# Copy the built JAR from build stage
COPY --from=build /app/auth-service/target/*.jar app.jar

# Change ownership to app user
RUN chown -R appuser:appgroup /app

# Switch to non-root user
USER appuser

# Expose port
EXPOSE 8081

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8081/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]