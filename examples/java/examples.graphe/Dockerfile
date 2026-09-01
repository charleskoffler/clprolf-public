FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY target/examples.graphe-1.0.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
