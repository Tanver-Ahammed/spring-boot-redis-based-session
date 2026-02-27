# Use Java 25 (Temurin)
FROM eclipse-temurin:25-jdk-alpine

# Set working directory
WORKDIR /app

# Copy built jar
COPY target/spring-boot-redis-based-session-0.0.1-SNAPSHOT.jar app.jar

# Expose internal port
EXPOSE 8080

# Run Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]