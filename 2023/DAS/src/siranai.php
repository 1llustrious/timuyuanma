<?php

error_reporting(0);

highlight_file(__FILE__);
$allowed_ip = "127.0.0.1";
if ($_SERVER['REMOTE_ADDR'] != $allowed_ip) {
    die("S* has the kanojo but you don't");
}

 $finfo = finfo_open(FILEINFO_MIME_TYPE);
 if (finfo_file($finfo, $_FILES["file"]["tmp_name"]) === 'application/x-tar'){
 exec('cd /tmp && tar -xvf ' . $_FILES["file"]["tmp_name"]);
 }
