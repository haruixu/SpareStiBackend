FROM maven:3.8.2-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
# build all dependencies - caches dependencies to avoid reinstalling between runs
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn -f pom.xml package -DskipTests

FROM openjdk:17-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]