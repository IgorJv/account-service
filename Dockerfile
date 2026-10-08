FROM maven:3.9-eclipse-temurin-23 as build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:23-jre