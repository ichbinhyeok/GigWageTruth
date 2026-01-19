# Use official Eclipse Temurin for Java 17 (matches build.gradle)
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy the built JAR file (Adjustment needed: build logic should happen here or COPY pre-built)
# Assuming user works with ./gradlew build
# We'll use a multi-stage build for robustness

# --- STAGE 1: BUILD ---
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /src
COPY . .
# Grant logic to gradlew just in case
RUN chmod +x gradlew
# Build skipping tests to save time during deploy (optional)
RUN ./gradlew build -x test

# --- STAGE 2: RUN ---
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /src/build/libs/*.jar app.jar

# Expose port (Documentation only)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
