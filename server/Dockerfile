# Сборка сервера
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml ./
COPY server/pom.xml ./server/
COPY server/src ./server/src
RUN mvn clean package -f server/pom.xml -DskipTests

# Образ для запуска сервера
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/server/target/server-0.0.1-SNAPSHOT.jar server.jar

EXPOSE 8085
ENTRYPOINT ["java", "-jar", "server.jar"]
