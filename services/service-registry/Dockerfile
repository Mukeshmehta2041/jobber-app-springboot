# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build

WORKDIR /app

# Copy root pom.xml
COPY pom.xml .

# Create dummy folders for unrelated modules to avoid resolution errors
RUN mkdir -p api-gateway auth-service user-service config-server

# Add dummy POMs for unrelated modules
RUN echo '<?xml version="1.0" encoding="UTF-8"?><project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd"><modelVersion>4.0.0</modelVersion><parent><groupId>com.jobber</groupId><artifactId>jobber-app</artifactId><version>1.0-SNAPSHOT</version></parent><artifactId>api-gateway</artifactId></project>' > api-gateway/pom.xml

RUN echo '<?xml version="1.0" encoding="UTF-8"?><project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd"><modelVersion>4.0.0</modelVersion><parent><groupId>com.jobber</groupId><artifactId>jobber-app</artifactId><version>1.0-SNAPSHOT</version></parent><artifactId>auth-service</artifactId></project>' > auth-service/pom.xml

RUN echo '<?xml version="1.0" encoding="UTF-8"?><project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd"><modelVersion>4.0.0</modelVersion><parent><groupId>com.jobber</groupId><artifactId>jobber-app</artifactId><version>1.0-SNAPSHOT</version></parent><artifactId>user-service</artifactId></project>' > user-service/pom.xml

RUN echo '<?xml version="1.0" encoding="UTF-8"?><project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd"><modelVersion>4.0.0</modelVersion><parent><groupId>com.jobber</groupId><artifactId>jobber-app</artifactId><version>1.0-SNAPSHOT</version></parent><artifactId>config-server</artifactId></project>' > config-server/pom.xml

# Copy service-registry and common modules
COPY common/pom.xml ./common/
COPY common/src ./common/src

COPY service-registry/pom.xml ./service-registry/
COPY service-registry/src ./service-registry/src

# Build service-registry (also builds common since it's a dependency)
RUN mvn clean package -pl service-registry -am -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine

RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

WORKDIR /app

COPY --from=build /app/service-registry/target/*.jar app.jar

RUN chown -R appuser:appgroup /app
USER appuser

EXPOSE 8761

HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8761/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
