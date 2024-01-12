FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/*.jar /app.jar libs/*.jar
ENTRYPOINT ["java","-jar","/app.jar"]



