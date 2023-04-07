FROM maven:3.8.4-openjdk-17-slim as build

COPY . .
RUN mvn package -q -DskipTests

FROM openjdk:17.0.2-jdk-oraclelinux8

LABEL maintainer="Wander Campelo"

ARG JAR_FILE=target/*.jar
COPY --from=build ${JAR_FILE} configuration-service-1.0.0.jar

EXPOSE 8071

ENTRYPOINT ["java","-jar","/configuration-service-1.0.0.jar"]
