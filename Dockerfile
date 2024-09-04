FROM openjdk:11-jre-slim

# Set the working directory in the container
WORKDIR /app

# Copy the jar file into the container at /app
COPY target/HFT-0.1.jar /app/HFT-0.1.jar

# Expose the port your Spring Boot app runs on
EXPOSE 8081

# Set the default command to run your app
ENTRYPOINT ["java", "-jar", "HFT-0.1.jar"]
