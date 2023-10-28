#!/bin/bash

sed -i "s/DASCTF_flag/$DASFLAG/g" /nps/conf/nps.conf
sed -i "s/DASCTF_flag/$DASFLAG/g" /app/main.js

export DASFLAG=nonono
DASFLAG=nonono