FROM apache/activemq-classic:5.17.5
RUN apt-get update
RUN apt-get install wget curl netcat -y
EXPOSE 61616
EXPOSE 5005
CMD [ "tail","-f","/dev/null" ]

