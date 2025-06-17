# Build stage
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy only the necessary files for building
COPY pom.xml .
COPY config-server/pom.xml config-server/
COPY common/pom.xml common/
COPY service-registry/pom.xml service-registry/
COPY auth-service/pom.xml auth-service/
COPY user-service/pom.xml user-service/
COPY api-gateway/pom.xml api-gateway/

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY config-server/src config-server/src
COPY common/src common/src
COPY service-registry/src service-registry/src
COPY auth-service/src auth-service/src
COPY user-service/src user-service/src
COPY api-gateway/src api-gateway/src

# Build the application
RUN mvn clean package -DskipTests -pl config-server

# Run stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy the built artifact from build stage
COPY --from=build /app/config-server/target/*.jar app.jar

# Set environment variables
ENV JAVA_OPTS="-Xms512m -Xmx512m"

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"] 