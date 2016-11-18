FROM openjdk:8-alpine
MAINTAINER NSD <support@nsd.no>

ENV PROFILE=production
ENV DB_HOST=postgres
EXPOSE 5001
EXPOSE 5002
COPY build/libs/QTTD.jar /QTTD.jar

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=${PROFILE}", "/QTTD.jar"]
