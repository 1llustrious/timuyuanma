FROM openjdk:8-alpine


RUN mkdir -p /app/templates
COPY ./index.ftl /app/templates/index.ftl

COPY web.jar /app/web.jar
COPY flag /flag
WORKDIR /app
