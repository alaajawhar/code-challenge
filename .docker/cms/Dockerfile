FROM maven:3.9.6 AS build
WORKDIR /app
COPY cms/pom.xml .
COPY cms/src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/cms.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]