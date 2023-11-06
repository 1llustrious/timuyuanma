FROM php:8.2.10-apache

COPY ./flag /flag
COPY ./image/mywife.png /var/www/html/mywife.png
COPY ./index.php /tmp/index
COPY ./siranai.php /var/www/html/siranai.php

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    apt-get update && \
    apt-get -y install libxml2-dev && \
    docker-php-ext-configure opcache --enable-opcache && docker-php-ext-install opcache&&docker-php-ext-install soap&& \
    chmod -R 777 /tmp && \
    echo "opcache.enable=1" >> "/usr/local/etc/php/conf.d/docker-php-ext-opcache.ini" && \
    echo 'opcache.file_cache="/tmp"'>> '/usr/local/etc/php/conf.d/docker-php-ext-opcache.ini' && \
    echo "opcache.file_cache_only=1" >> "/usr/local/etc/php/conf.d/docker-php-ext-opcache.ini" && \
    RUN echo $(cat /tmp/index)>/var/www/html/index.php && \
    RUN rm /tmp/index

    
EXPOSE 80



