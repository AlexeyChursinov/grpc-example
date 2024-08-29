# Сборка клиента
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml ./
COPY client/pom.xml ./client/
COPY client/src ./client/src
RUN mvn clean package -f client/pom.xml -DskipTests

# Образ для запуска клиента
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/client/target/client-0.0.1-SNAPSHOT.jar client.jar

EXPOSE 8086
ENTRYPOINT ["java", "-jar", "client.jar"]
