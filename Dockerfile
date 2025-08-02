# Stage 1: Build the JAR
FROM gradle:7-jdk17 AS builder
WORKDIR /app
COPY . .
RUN gradle clean build -x test --no-daemon

# Stage 2: Run the app
FROM openjdk:17-jdk
WORKDIR /app

COPY --from=builder /app/build/libs/demo-0.0.1-SNAPSHOT.jar ELearningApp.jar

EXPOSE 9090

CMD ["java", "-jar", "ELearningApp.jar"]
