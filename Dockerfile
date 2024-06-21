# Use the official OpenJDK image as a base
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the packaged JAR file into the container
COPY build/libs/magic-card-game-0.0.1-SNAPSHOT.jar docker-mgc.jar
# Copy the deployment properties file into the container
COPY application-deployment.properties /config/

# Set the environment variable to point to the properties file location
ENV SPRING_CONFIG_ADDITIONAL_LOCATION=file:/config/application-deployment.properties
# Expose the port the application runs on
EXPOSE 8080

# Run the JAR file when the container starts
CMD ["java", "-jar", "docker-mgc.jar"]