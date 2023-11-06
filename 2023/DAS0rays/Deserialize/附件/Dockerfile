FROM openjdk:8-jre-alpine

# copy jar from the first stage
RUN mkdir /app/ && \ 
    mkdir /usr/lib/jvm/java-1.8-openjdk/jre/classes && \
    mkdir /tmp/upload
COPY ./app.jar /app/app.jar
COPY ./flag /flag
WORKDIR /app/

EXPOSE 9090

CMD ["java", "-jar", "app.jar"]