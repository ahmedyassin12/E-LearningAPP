FROM openjdk:17-jdk


COPY target/demo-0.0.1-SNAPSHOT.jar /app/ELearningApp.jar

EXPOSE 9090

WORKDIR "/app"

CMD ["java", "-jar" ,"ELearningApp.jar"]
