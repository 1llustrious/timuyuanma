
touch /tmp/app.log
chmod 666 /tmp/app.log

nohup java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:3345 -jar /tmp/web.jar >/tmp/app.log