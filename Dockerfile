FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
WORKDIR /nxg-0.0.1-SNAPSHOT
COPY /target/nxg-0.0.1-SNAPSHOT.jar nxg.jar
ENTRYPOINT ["java","-jar","/nxg.jar"]
EXPOSE 8080




