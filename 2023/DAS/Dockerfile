FROM php:8.2.10-apache

COPY ./flag /flag
COPY ./image/mywife.png /var/www/html/mywife.png
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN apt-get update
RUN apt-get -y install libxml2-dev
RUN docker-php-ext-configure opcache --enable-opcache && docker-php-ext-install opcache&&docker-php-ext-install soap
RUN chmod -R 777 /tmp
RUN echo "opcache.enable=1" >> "/usr/local/etc/php/conf.d/docker-php-ext-opcache.ini"
RUN echo 'opcache.file_cache="/tmp"'>> '/usr/local/etc/php/conf.d/docker-php-ext-opcache.ini'
RUN echo "opcache.file_cache_only=1" >> "/usr/local/etc/php/conf.d/docker-php-ext-opcache.ini"
COPY ./index.php /tmp/index
COPY ./siranai.php /var/www/html/siranai.php
RUN echo $(cat /tmp/index)>/var/www/html/index.php
RUN rm /tmp/index



