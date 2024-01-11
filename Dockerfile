#FROM eclipse-temurin:17-jdk-alpine
#VOLUME /tmp
#WORKDIR /nxg-0.0.1-SNAPSHOT
#COPY target/*.jar /nxg-0.0.1-SNAPSHOT.jar
#ENTRYPOINT ["java","-jar","/nxg-0.0.1-SNAPSHOT.jar"]
#EXPOSE 8080

#
# Build stage
#
FROM maven:3.8.2-jdk-11 AS build
COPY . .
RUN mvn clean package -Pprod -DskipTests

#
# Package stage
#
FROM openjdk:11-jdk-slim
COPY --from=build /target/nxg-0.0.1-SNAPSHOT.jar nxg.jar
# ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","nxg.jar"]




