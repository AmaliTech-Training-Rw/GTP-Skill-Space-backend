# base image that includes Java and Maven
FROM eclipse-temurin:21

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the host machine to the container
COPY target/user-0.0.1-SNAPSHOT.jar /app/user-service.jar

# Exposed port that the Spring Boot app will run on
EXPOSE 8081

# command to run the Spring Boot application
CMD ["java", "-jar", "/app/user-service.jar"]

