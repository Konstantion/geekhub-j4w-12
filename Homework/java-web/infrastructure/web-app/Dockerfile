FROM openjdk:17-jdk-alpine

ARG JAR_FILE=./build/my-app/*.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app.jar"]