#3.8-openjdk-11-slim
#FROM maven:3.8-openjdk-11 AS build
#RUN mkdir -p /workspace
#WORKDIR /workspace
#COPY pom.xml /workspace
#COPY src /workspace/src
#RUN mvn -f pom.xml clean package

# For Java 11,
#FROM adoptopenjdk/openjdk11:alpine-jre
FROM adoptopenjdk/openjdk11:jdk-11.0.6_10-centos-slim
VOLUME /var/VST-DMS-APPS/FILES/Temp/
COPY ./target/Micro-PresaleEnquiry-Report-Service-0.0.1-SNAPSHOT.jar Micro-PresaleEnquiry-Report-Service.jar
EXPOSE 8086 8092
#ENV JAVA_OPTS="XX:ErrorFile='/var/VST-DMS-APPS/Presales-reports/$${HOSTNAME}/hs_err_pid_%p.log'"
ENTRYPOINT ["java","-Dspring.cloud.config.uri=http://vstdms.vstshakti.in:8012","-Dspring.profiles.active=prod","-Djava.awt.headless=true","-Dcom.sun.management.jmxremote", "-Dcom.sun.management.jmxremote.authenticate=false", "-Dcom.sun.management.jmxremote.ssl=false", "-Dcom.sun.management.jmxremote.port=8092", "-Dcom.sun.management.jmxremote.rmi.port=8092", "-Djava.rmi.server.hostname=vstdms.vstshakti.in", "-jar","Micro-PresaleEnquiry-Report-Service.jar"]

###########################################
#FROM eclipse-temurin:11 as jre-build

# Create a custom Java runtime
#RUN $JAVA_HOME/bin/jlink \
#         --add-modules java.base \
#         --strip-debug \
#         --no-man-pages \
#         --no-header-files \
#         --compress=2 \
#         --output /javaruntime

# Define your base image
#FROM debian:buster-slim
#ENV JAVA_HOME=/opt/java/openjdk
#ENV PATH "${JAVA_HOME}/bin:${PATH}"
#COPY --from=jre-build /javaruntime $JAVA_HOME

# Continue with your application deployment
#RUN mkdir /opt/app
#COPY --from=build /workspace/target/Micro-PresaleEnquiry-Report-Service-0.0.1-SNAPSHOT.jar /opt/app/Micro-PresaleEnquiry-Report-Service.jar
#EXPOSE 8086
#ENTRYPOINT ["java","-Dspring.cloud.config.uri=http://vstdms.vstshakti.in:8012", "-Dspring.profiles.active=prod","-jar","Micro-PresaleEnquiry-Report-Service.jar"]

###############################################