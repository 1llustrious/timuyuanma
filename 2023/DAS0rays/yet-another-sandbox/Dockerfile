FROM node:20

WORKDIR /app

COPY app/ .
COPY flag /flag
COPY readflag.c /readflag.c


RUN npm install
RUN chown root:root /flag && chmod 400 /flag
RUN gcc -o /readflag /readflag.c
RUN chmod u+s /readflag
RUN chown -R node:node /app

USER node

EXPOSE 1337

CMD [ "npm", "run", "start" ]
