# Use the official OpenJDK image as a base
FROM openjdk:11-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the packaged JAR file into the container
COPY build/libs/*.jar app.jar

# Expose the port the application runs on
EXPOSE 8080

# Run the JAR file when the container starts
CMD ["java", "-jar", "app.jar"]