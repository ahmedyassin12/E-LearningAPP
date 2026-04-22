# Stage 1: Build the JAR
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run the app
FROM eclipse-temurin:17-jdk as build
WORKDIR /app

COPY --from=builder /app/target/*.jar ELearningApp.jar

EXPOSE 8080

CMD ["java", "-jar", "ELearningApp.jar"]
