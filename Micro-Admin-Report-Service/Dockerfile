#FROM maven:3.8-openjdk-11 AS build
#RUN mkdir -p /workspace
#WORKDIR /workspace
#COPY pom.xml /workspace
#COPY src /workspace/src
#RUN mvn -f pom.xml clean package

# For Java 11,
#FROM eclipse-temurin:11

FROM adoptopenjdk/openjdk11:jdk-11.0.6_10-centos-slim
VOLUME /var/VST-DMS-APPS/FILES/Temp/
COPY ./target/Micro-Admin-Report-Service-0.0.1-SNAPSHOT.jar Micro-Admin-Report-Service.jar
EXPOSE 9091
ENTRYPOINT ["java","-Dspring.cloud.config.uri=http://vstdms.vstshakti.in:8012", "-Dspring.profiles.active=prod","-Djava.awt.headless=true","-jar","Micro-Admin-Report-Service.jar"]

##################################################
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
#COPY --from=build /workspace/target/Micro-Admin-Service-0.0.1-SNAPSHOT.jar Micro-Admin-Service.jar
#EXPOSE 9090
#CMD ["java", "-Dspring.cloud.config.uri=http://vstdms.vstshakti.in:8012", "-Dspring.profiles.active=prod", "-jar", "/opt/app/Micro-Admin-Service.jar"]