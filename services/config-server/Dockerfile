FROM eclipse-temurin:17-jdk
VOLUME /tmp
COPY target/*-SNAPSHOT.jar app.jar
COPY config-repo ./config-repo
ENTRYPOINT ["java","-jar","/app.jar"] 