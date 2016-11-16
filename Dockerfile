FROM openjdk:8-alpine
MAINTAINER NSD <support@nsd.no>

ENV PROFILE=production
ENV DB_HOST=postgres
EXPOSE 8080
COPY build/libs/QTTD.jar /QTTD.jar

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=${PROFILE}", "-Dspring.datasource.url=jdbc:postgresql://${DB_HOST}:5432/qddt", "/QTTD.jar"]
