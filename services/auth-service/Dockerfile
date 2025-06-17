# Build stage
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy the parent pom.xml and all module pom.xml files
COPY pom.xml .
COPY auth-service/pom.xml auth-service/
COPY common/pom.xml common/
COPY config-server/pom.xml config-server/
COPY service-registry/pom.xml service-registry/
COPY user-service/pom.xml user-service/
COPY api-gateway/pom.xml api-gateway/

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY auth-service/src auth-service/src
COPY common/src common/src

# Build the application
RUN mvn clean package -pl auth-service -am -DskipTests

# Run stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy the built artifact from build stage
COPY --from=build /app/auth-service/target/*.jar app.jar

# Set environment variables
ENV JAVA_OPTS="-Xms512m -Xmx512m"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]