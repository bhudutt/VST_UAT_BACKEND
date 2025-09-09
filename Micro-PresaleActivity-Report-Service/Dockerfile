#3.8-openjdk-11-slim
#FROM maven:3.8-openjdk-11 AS build
#RUN mkdir -p /workspace
#WORKDIR /workspace
#COPY pom.xml /workspace
#COPY src /workspace/src
#RUN mvn -f pom.xml clean package

# For Java 11,
FROM adoptopenjdk/openjdk11:alpine-jre
VOLUME /var/VST-DMS-APPS/FILES/Temp/
COPY ./target/Micro-PresaleActivity-Report-Service-0.0.1-SNAPSHOT.jar Micro-PresaleActivity-Report-Service.jar
EXPOSE 8089
ENTRYPOINT ["java","-Dspring.cloud.config.uri=http://vstdms.vstshakti.in:8012", "-Dspring.profiles.active=prod","-jar","Micro-PresaleActivity-Report-Service.jar"]


