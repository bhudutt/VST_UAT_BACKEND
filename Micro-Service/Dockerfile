# For Java 11,
FROM adoptopenjdk/openjdk11:jre-11.0.11_9
VOLUME /var/VST-DMS-APPS/FILES/
COPY ./target/Micro-Service-0.0.1-SNAPSHOT.jar Micro-Service.jar
EXPOSE 8095
ENTRYPOINT ["java","-Dspring.cloud.config.uri=http://vstdms.vstshakti.in:8012","-Dspring.profiles.active=prod","-Djava.awt.headless=true","-jar","Micro-Service.jar"]
