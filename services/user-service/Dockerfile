# Build stage
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy the parent pom.xml and all module pom.xml files
COPY pom.xml .
COPY user-service/pom.xml user-service/
COPY common/pom.xml common/
COPY config-server/pom.xml config-server/
COPY service-registry/pom.xml service-registry/
COPY auth-service/pom.xml auth-service/
COPY api-gateway/pom.xml api-gateway/

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY user-service/src user-service/src
COPY common/src common/src

# Build and install common module
RUN mvn clean install -pl common -am -DskipTests

# Verify common module installation
RUN ls -la /root/.m2/repository/com/jobber/common/1.0-SNAPSHOT/

# Build user-service with explicit dependency on common
RUN mvn clean package -pl user-service -am -DskipTests -U

# Run stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy the built artifact from build stage
COPY --from=build /app/user-service/target/*.jar app.jar

# Set environment variables
ENV JAVA_OPTS="-Xms512m -Xmx512m"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
