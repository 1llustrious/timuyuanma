<?php 
if(preg_match('/Baiduspider/', $_SERVER['HTTP_USER_AGENT']))exit;
$nosession = true;
require './Core/Common.php'; 
/*
if (function_exists("set_time_limit"))
{
	@set_time_limit(0);
}
if (function_exists("ignore_user_abort"))
{
	@ignore_user_abort(true);
}
*/
// +----------------------------------------------------------------------
// | Quotes [ 只为给用户更好的体验]**[我知道发出来有人会盗用,但请您留版权]
// +----------------------------------------------------------------------
// | Licensed ( http://www.apache.org/licenses/LICENSE-2.0 )
// +----------------------------------------------------------------------
// | Author: 零度            盗用不留版权,你就不配拿去!
// +----------------------------------------------------------------------
// | Date: 2019年08月20日
// +----------------------------------------------------------------------
	/*
	检测回调
*/
	$limit=10;//每次执行条数
	$time=time();//当前时间戳
	
	//遍历所有正常二维码
	$rs=$DB->query("SELECT * from pay_qrlist WHERE status='1' and type!='wxpay' and crontime<'{$time}' order by rand() limit {$limit}");
	while($row = $rs->fetch())
	{	
		$date = check_money_notify($row,true);
		echo $date;
	}	
	if($_GET['Cron']){
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
				sysmsg('<h3>'.$query_A['msg'].'</h3>',true);
			}elseif($query_B['msg']){
				sysmsg('<h3>'.$query_B['msg'].'</h3>',true);
			}else{
				sysmsg('<h3>云端链接异常</h3>',true);
			}
		}
	}
	echo 'Cron Ok '.$date;
?>