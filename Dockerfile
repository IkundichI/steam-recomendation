# Use the latest Gradle image with JDK 17
FROM gradle:7.6-jdk17 AS build

WORKDIR /app

# Copy the build.gradle and settings.gradle files
COPY build.gradle settings.gradle ./

# Copy the rest of the project
COPY . .

# Build the project
RUN gradle build -x test

# Use the OpenJDK image to run the application
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the built application from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the port the application runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
