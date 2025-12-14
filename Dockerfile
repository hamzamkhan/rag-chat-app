# ============================
# Build stage
# ============================
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy Maven descriptor first (better cache)
COPY pom.xml .
RUN mvn -q -B dependency:go-offline

# Now copy source and build
COPY src ./src
RUN mvn -q -B -DskipTests package

# ============================
# Runtime stage
# ============================
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the JAR built in the first stage
COPY --from=build /app/target/rag-chat-app-0.0.1-SNAPSHOT.jar app.jar

# Expose app port
EXPOSE 8090

# Optional: allow passing extra JVM args via JAVA_OPTS
ENV JAVA_OPTS=""

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]