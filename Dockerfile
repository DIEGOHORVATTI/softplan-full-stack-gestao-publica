FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/backend-1.0.0.jar backend.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "backend.jar"]
