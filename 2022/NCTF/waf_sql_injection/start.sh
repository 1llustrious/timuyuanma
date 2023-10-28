
docker-php-ext-configure opcache --enable-opcache && docker-php-ext-install opcache

echo $FLAG > /flag
export $FLAG = "not"
