FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY ./nxg-job-hub-backend/target/*.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
