#!/bin/bash

if [[ -f /flag.sh ]]; then
	source /flag.sh
	echo "" > /flag.sh
fi

echo "127.0.0.1 a.o.com"  >> /etc/hosts

node /app/main.js & /nps/nps