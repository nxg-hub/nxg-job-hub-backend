FROM eclipse-temurin:17-jdk-alpine
ENV PSK_SK_LIVE=${PSK_SK_LIVE}
VOLUME /tmp
COPY target/*.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8383
