FROM eclipse-temurin:17-jdk-alpine
ENV sk_live_7d4392c59ec396cf5c33fd908774aa8130dab8a3=${PSK_SK_LIVE}
VOLUME /tmp
COPY target/*.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8383
