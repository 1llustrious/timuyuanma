<?php
@header("content-Type: text/html; charset=utf-8");
date_default_timezone_set('Asia/Shanghai');
date_default_timezone_set('PRC');
session_start();
error_reporting(E_ALL & ~E_NOTICE);
//if(defined('IN_CRONLITE'))return;
define('SYSTEM_ROOT', dirname(__FILE__).'/');
define('ROOT', dirname(SYSTEM_ROOT).'/');
define('TEMPLATE_ROOT', ROOT.'Template/');
define('PAYTEMPLATE_ROOT', ROOT.'Submit/Template/');
$date = date("Y-m-d H:i:s");
$Version	=	2075;//版本号
if(phpversion() < '5.6')
{
	exit('<!DOCTYPE html>
    <html xmlns="http://www.w3.org/1999/xhtml" lang="zh-CN">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>站点提示信息</title>
        <style type="text/css">
html{background:#eee}body{background:#fff;color:#333;font-family:"微软雅黑","Microsoft YaHei",sans-serif;margin:2em auto;padding:1em 2em;max-width:700px;-webkit-box-shadow:10px 10px 10px rgba(0,0,0,.13);box-shadow:10px 10px 10px rgba(0,0,0,.13);opacity:.8}h1{border-bottom:1px solid #dadada;clear:both;color:#666;font:24px "微软雅黑","Microsoft YaHei",,sans-serif;margin:30px 0 0 0;padding:0;padding-bottom:7px}#error-page{margin-top:50px}h3{text-align:center}#error-page p{font-size:9px;line-height:1.5;margin:25px 0 20px}#error-page code{font-family:Consolas,Monaco,monospace}ul li{margin-bottom:10px;font-size:9px}a{color:#21759B;text-decoration:none;margin-top:-10px}a:hover{color:#D54E21}.button{background:#f7f7f7;border:1px solid #ccc;color:#555;display:inline-block;text-decoration:none;font-size:9px;line-height:26px;height:28px;margin:0;padding:0 10px 1px;cursor:pointer;-webkit-border-radius:3px;-webkit-appearance:none;border-radius:3px;white-space:nowrap;-webkit-box-sizing:border-box;-moz-box-sizing:border-box;box-sizing:border-box;-webkit-box-shadow:inset 0 1px 0 #fff,0 1px 0 rgba(0,0,0,.08);box-shadow:inset 0 1px 0 #fff,0 1px 0 rgba(0,0,0,.08);vertical-align:top}.button.button-large{height:29px;line-height:28px;padding:0 12px}.button:focus,.button:hover{background:#fafafa;border-color:#999;color:#222}.button:focus{-webkit-box-shadow:1px 1px 1px rgba(0,0,0,.2);box-shadow:1px 1px 1px rgba(0,0,0,.2)}.button:active{background:#eee;border-color:#999;color:#333;-webkit-box-shadow:inset 0 2px 5px -3px rgba(0,0,0,.5);box-shadow:inset 0 2px 5px -3px rgba(0,0,0,.5)}table{table-layout:auto;border:1px solid #333;empty-cells:show;border-collapse:collapse}th{padding:4px;border:1px solid #333;overflow:hidden;color:#333;background:#eee}td{padding:4px;border:1px solid #333;overflow:hidden;color:#333}
        </style>
    </head>
    <body id="error-page">
        <h3>站点提示信息</h3>
        当前PHP版本('.phpversion().'),请切换到5.6以上方可使用!
    </body>
    </html>');
}
	$scriptpath=str_replace('\\','/',$_SERVER['SCRIPT_NAME']);
	$sitepath = substr($scriptpath, 0, strrpos($scriptpath, '/'));
	$siteurl = ($_SERVER['SERVER_PORT'] == '443' ? 'https://' : 'http://').$_SERVER['HTTP_HOST'].$sitepath.'/';

	if(is_file(SYSTEM_ROOT.'360_Safe/360webscan.php')){//360网站卫士
  	  require_once(SYSTEM_ROOT.'360_Safe/360webscan.php');
	}
	require_once(SYSTEM_ROOT."Core_Class/Autoloader.php"); //自动载入
	Autoloader::register();
	include_once(SYSTEM_ROOT."Core_Class/Security.php");
	

	include(SYSTEM_ROOT."Config.php");

	if(!defined('SQLITE') && (!$dbconfig['user']||!$dbconfig['pwd']||!$dbconfig['dbname']))//检测安装
	{
		header('Content-type:text/html;charset=utf-8');
		echo '你还没安装！<a href="/Install/">点此安装</a>';
		exit();
	}
	try {
  	 	$DB = new PDO("mysql:host={$dbconfig['host']};dbname={$dbconfig['dbname']};port={$dbconfig['port']}",$dbconfig['user'],$dbconfig['pwd']);
	}catch(Exception $e){
	    exit('链接数据库失败:'.$e->getMessage());
	}
	$DB->exec("set names utf8");
	require_once SYSTEM_ROOT.'Core_Class/Cache.Class.php'; //获取系统配置支持库
	$CACHE=new CACHE();
	$conf=$CACHE->pre_fetch();//获取系统配置
	if($conf['version'] < $Version	&& $update==null){//检测更新
		@header('Location:/Install/update.php');
		exit();
	}
	//此地址作为回调地址 判断是否存在无CDN 如果不存在则默认使用当前发起支付的域名
	//if(!$conf['local_domain'])$conf['local_domain']=$_SERVER['HTTP_HOST']; 
	$conf['local_domain']=$_SERVER['HTTP_HOST'];
	//require_once(SYSTEM_ROOT."Pay_Class/Mpay/Mpay_core.function.php"); //支付签名组装支持库
	//require_once(SYSTEM_ROOT."Pay_Class/Mpay/Mpay_md5.function.php"); //支付签名加密支持库
	require_once(SYSTEM_ROOT."Core_Class/Function.Class.php"); //核心支持库
	require_once(SYSTEM_ROOT."Core_Class/MPay_function.Class.php"); //支付签名组装支持库
	include_once(SYSTEM_ROOT.'Core_Class/Login.Class.php');//登录支持库
	include_once(SYSTEM_ROOT.'Authcode.php');//授权码
	
	//云端接口 开始
	include_once(SYSTEM_ROOT.'Pay_Apis/Pay_Cookie_Api.php');
	$Pay_Cookie_Api=new Pay_Cookie_Api($conf['Instant_url']);//扫码取Cookie	
	
	//Cookie登录取余额
	include_once(SYSTEM_ROOT.'Pay_Apis/Pay_Money_Api.php');
	$Pay_Money_Api=new Pay_Money_Api();//Cookie登录取余额
	
	require_once(SYSTEM_ROOT."Pay_Apis/Instant_Api.Class.php");//云端服务器对接支持库
	$Instant_Api=new Instant_Api($conf['Instant_url'],$conf['Instant_pid'],$conf['Instant_key'],$Authcode);
	//云端接口 结束
		
	
	$ip = real_ip();//获取访问者IP地址
	
	if(strlen($Authcode)!=32)
		sysmsg('<h2>你的网站未经授权，购买正版请联系27751585，享受声誉，安全，程序升级<br/><br/>',true);
	if(!isset($_SESSION['authcode'])	&&	$islogin_admin==1) {
		$query_A=file_get_contents($conf['Instant_url'].'api/Api_Check.php?url='.$_SERVER['HTTP_HOST'].'&authcode='.$Authcode);
		$query_A=json_decode($query_A,true);
		$query_B=file_get_contents($Instant_url_list[0].'api/Api_Check.php?url='.$_SERVER['HTTP_HOST'].'&authcode='.$Authcode);
		$query_B=json_decode($query_B,true);
		if($query_A or $query_B){
			if($query_A['code']==1){
				$_SESSION['authcode']=true;
			}elseif($query_B['code']==1){
				$_SESSION['authcode']=true;
			}elseif($query_A['msg']){
				$_SESSION['authcode']=true;
			}elseif($query_B['msg']){
				$_SESSION['authcode']=true;
			}else{
				sysmsg('<h3>云端链接异常</h3>',true);
			}
		}
	}
	
?>
