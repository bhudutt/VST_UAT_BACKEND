#3.8-openjdk-11-slim
#FROM maven:3.8-openjdk-11 AS build
#RUN mkdir -p /workspace
#WORKDIR /workspace
#COPY pom.xml /workspace
#COPY src /workspace/src
#RUN mvn -f pom.xml clean package

# For Java 11,
FROM adoptopenjdk/openjdk11:alpine-jre

COPY ./target/Micro-Eureka-Service-0.0.1-SNAPSHOT.jar Micro-Eureka-Service.jar
EXPOSE 8761
ENTRYPOINT ["java","-Dspring.profiles.active=prod","-jar","Micro-Eureka-Service.jar"]

##########################################
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
#COPY ./target/Micro-Eureka-Service-0.0.1-SNAPSHOT.jar Micro-Eureka-Service.jar
#EXPOSE 8761
#ENTRYPOINT ["java","-Dspring.profiles.active=prod","-jar","Micro-Eureka-Service.jar"]

##############################################