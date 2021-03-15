FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} omsordercreationservice.jar
ENTRYPOINT ["java","-jar","/omsordercreationservice.jar"]
