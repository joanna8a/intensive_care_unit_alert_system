# Build stage
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app

# Copy Maven files
COPY pom.xml .
COPY src ./src

# Build application
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Install curl for health checks
RUN apk add --no-cache curl

# Create non-root user
RUN addgroup -S medical && adduser -S medical -G medical

# Copy the built application
COPY --from=build /app/target/*.jar app.jar

# Change ownership to non-root user
RUN chown medical:medical /app/app.jar
USER medical

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

EXPOSE 8080

# Use exec form for better signal handling
ENTRYPOINT ["java", "-jar", "/app/app.jar"]