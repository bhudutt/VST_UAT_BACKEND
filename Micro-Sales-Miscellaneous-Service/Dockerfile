#3.8-openjdk-11-slim
#FROM maven:3.8-openjdk-11 AS build
#RUN mkdir -p /workspace
#WORKDIR /workspace
#COPY pom.xml /workspace
#COPY src /workspace/src
#RUN mvn -f pom.xml clean package

# For Java 11,
#FROM adoptopenjdk/openjdk11:alpine-jre

#COPY ./target/Micro-Sales-Miscellaneous-Servicee-0.0.1-SNAPSHOT.jar Micro-Sales-Miscellaneous-Servicee.jar
#EXPOSE 8088
#ENTRYPOINT ["java","-Dspring.cloud.config.uri=http://vstdms.vstshakti.in:8012","-Dspring.profiles.active=prod","-jar","Micro-Sales-Miscellaneous-Servicee.jar"]


FROM adoptopenjdk/openjdk11:jdk-11.0.6_10-centos-slim
VOLUME /var/VST-DMS-APPS/FILES/Temp/
COPY ./target/Micro-Sales-Miscellaneous-Servicee-0.0.1-SNAPSHOT.jar Micro-Sales-Miscellaneous-Servicee.jar
EXPOSE 8088
ENTRYPOINT ["java","-Dspring.cloud.config.uri=http://vstdms.vstshakti.in:8012", "-Dspring.profiles.active=prod","-Djava.awt.headless=true","-jar","Micro-Sales-Miscellaneous-Servicee.jar"]



