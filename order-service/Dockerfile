FROM maven:3.8.4-openjdk-17-slim as build

COPY . .
RUN mvn package -q -DskipTests

FROM openjdk:17.0.2-jdk-oraclelinux8

LABEL maintainer="Wander Campelo"

ARG JAR_FILE=target/*.jar
COPY --from=build ${JAR_FILE} order-service-1.0.0.jar

ENTRYPOINT ["java","-jar","/order-service-1.0.0.jar"]
