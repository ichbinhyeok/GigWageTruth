# --- STAGE 1: BUILD ---
FROM bellsoft/liberica-openjdk-alpine:17 AS builder
WORKDIR /src
COPY . .
RUN chmod +x gradlew
# Build skipping tests and generating JTE templates
RUN ./gradlew clean build -x test --no-daemon

# --- STAGE 2: RUN ---
FROM bellsoft/liberica-openjre-alpine:17
WORKDIR /app

# Copy the built JAR file
COPY --from=builder /src/build/libs/*.jar app.jar

# Application runs on 8080 inside container
EXPOSE 8080

# Run the application with low-memory optimizations
# These options are managed by the deployment script/workflow to ensure flexibility
ENTRYPOINT ["java", "-jar", "app.jar"]
