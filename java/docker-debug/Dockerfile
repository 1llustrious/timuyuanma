#FROM openjdk:8-alpine
FROM adoptopenjdk/openjdk11
COPY SimpleAPI-1.0-SNAPSHOT.jar /tmp/web.jar
COPY flag /flag
COPY start.sh /start.sh
RUN chmod +x /start.sh
ENTRYPOINT /start.sh 
EXPOSE 8877
EXPOSE 3345