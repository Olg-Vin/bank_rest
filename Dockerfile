FROM openjdk:21-jdk
WORKDIR /app
COPY target/bank_rest-1.0-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]