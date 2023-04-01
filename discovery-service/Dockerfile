FROM maven:3.8.4-openjdk-17-slim as build

COPY . .
RUN mvn install -q -DskipTests

FROM openjdk:17.0.2-jdk-oraclelinux8

#LABEL schedlerdavid=david.schedler@stud.fh-campuswien.ac.at
#LABEL caf3babe=nichil.strasser@stud.fh-campuswien.ac.at
#LABEL schneidermichael=michael.schneider@stud-fh.campusvienna.ac.at

ARG JAR_FILE=target/*.jar
COPY --from=build ${JAR_FILE} discovery-service.jar

ENTRYPOINT ["java","-jar","/discovery-service.jar"]