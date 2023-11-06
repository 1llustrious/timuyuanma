FROM node:18-buster-slim

WORKDIR '/app'

COPY ./files/app/package.json /app/package.json

RUN sed -i -E 's/(deb|security).debian.org/mirrors.aliyun.com/g' /etc/apt/sources.list && \
    apt update && \
    apt-get install -y gstreamer1.0-libav libnss3-tools libatk-bridge2.0-0 libcups2 libxkbcommon-x11-0 libxcomposite-dev libxrandr2 libgbm1 && \
    npm install

COPY ./files/ /

ENV DASFLAG DASCTF_flag

EXPOSE 8080

ENTRYPOINT /start.sh