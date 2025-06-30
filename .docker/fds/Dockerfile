FROM maven:3.9.6 AS build
WORKDIR /app
COPY fds/pom.xml .
COPY fds/src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/fds.jar app.jar
EXPOSE 8081
CMD ["java", "-jar", "app.jar"]