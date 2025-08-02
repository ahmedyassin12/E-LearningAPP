# Stage 1: Build the JAR
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run the app
FROM openjdk:17-jdk
WORKDIR /app

COPY --from=builder /app/target/*.jar ELearningApp.jar

EXPOSE 9090

CMD ["java", "-jar", "ELearningApp.jar"]
