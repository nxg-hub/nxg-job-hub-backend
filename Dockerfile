FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
WORKDIR /nxg-0.0.1-SNAPSHOT
COPY target/*.jar /nxg-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/nxg-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080




