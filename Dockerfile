FROM eclipse-temurin:21-jre-alpine
EXPOSE 8080
WORKDIR /opt/app
COPY build/libs/DomofonParser-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
