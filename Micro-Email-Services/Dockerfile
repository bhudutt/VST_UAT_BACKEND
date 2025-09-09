#3.8-openjdk-11-slim
#FROM maven:3.8-openjdk-11 AS build
#RUN mkdir -p /workspace
#WORKDIR /workspace
#COPY pom.xml /workspace
#COPY src /workspace/src
#RUN mvn -f pom.xml clean package

# For Java 11,
FROM adoptopenjdk/openjdk11:jre-11.0.11_9

COPY ./target/Micro-Email-Service-0.0.1-SNAPSHOT.jar Micro-Email-Service.jar
EXPOSE 9999
ENTRYPOINT ["java","-Dspring.cloud.config.uri=http://vstdms.vstshakti.in:8012","-Dspring.profiles.active=prod","-jar","Micro-Email-Service.jar"]


