FROM maven:3.8.2-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
# build all dependencies - caches dependencies to avoid reinstalling between runs
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn -f pom.xml package -DskipTests -Dspotless.check.skip=true

FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]