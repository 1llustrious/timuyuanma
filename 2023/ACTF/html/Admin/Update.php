<?php
$title='更新检测';
include './Head.php';
$checkurl=$conf['Instant_url'].'api/Api_Check.php?url='.$_SERVER['HTTP_HOST'].'&authcode='.trim($Authcode).'&ver='.$Version;
//函数
function zipExtract($src, $dest)
{
$zip = new ZipArchive();
if ($zip->open($src)===true)
{
$zip->extractTo($dest);
$zip->close();
return true;
}
return false;
}
?>
			<div class="col-sm-12 col-md-12">
				<div class="panel panel-primary">
					<div class="panel-heading">
						<h4 class="panel-title"><span class="glyphicon glyphicon-globe"></span> 在线更新</h4>
					</div>
					<div class="panel-body">
							<a class="list-group-item">写入文件①（推荐）<?php if (is_writable('./')) {
								echo '<font color="green">可用√</font>';
							} else {
								echo '<font color="black">不支持</font>';
							} ?></a>
<?php
$act = isset($_GET['act']) ? $_GET['act'] : null;
switch ($act) {
default:
//$res=file_get_contents($checkurl);
$res=get_curl($checkurl);
$res=json_decode($res,true);
if(!$res['msg'])$res['msg']='啊哦，更新服务器开小差了，请刷新此页面。';
echo '<div class="alert alert-info">'.$res['msg'].'</div>';
echo '<hr/>';
if($res['code']==1) {
if(!class_exists('ZipArchive') || ini_get('acl.app_id') || defined("SAE_ACCESSKEY") || defined("BAE_ENV_APPID")) {
echo '您的空间不支持自动更新，请手动下载更新包并覆盖到程序根目录！<br/>
更新包下载：<a href="'.$res['file'].'" class="btn btn-primary">update.zip('.$Version.')</a>';
} else {
echo '<a href="?act=do" class="btn btn-primary btn-block">立即更新到最新版本</a>';
}

echo '<hr/><div class="well">'.$res['uplog'].'</div>';
}
break;
case 'do':
if(isset($_GET['test']))$checkurl=$update.'?url='.$_SERVER['HTTP_HOST'].'&authcode='.$Authcode.'&ver='.$Version;
$res=get_curl($checkurl);
//$res=file_get_contents($checkurl);
$res=json_decode($res,true);
$RemoteFile = $res['file'];
$ZipFile = "updata.zip";
download_zip($ZipFile,$RemoteFile);
//if(!copy($RemoteFile,$ZipFile)){showmsg("无法下载更新包文件",false,"",true);}
//if(!httpcopy($RemoteFile, $ZipFile)){showmsg("无法下载更新包文件",false,"",true);}
//zipExtract($ZipFile,$_SERVER['DOCUMENT_ROOT'])get_zip_originalsize($ZipFile,$_SERVER['DOCUMENT_ROOT'])
if (zipExtract($ZipFile,$_SERVER['DOCUMENT_ROOT'])) {//$_SERVER['DOCUMENT_ROOT']
echo "<div class='well'>程序更新成功！(".get_zip_originalsize($ZipFile,$_SERVER['DOCUMENT_ROOT']).")<br>";
echo '<a href="?">返回</a></br>';
unlink('../user.ini');
unlink($ZipFile);
}else {
echo "无法解压文件！<br>";
echo '<a href="?">返回</a>';
if (file_exists($ZipFile))
unlink('../user.ini');
unlink($ZipFile);
}
break;
}

function get_zip_originalsize($filename, $path) {
  //先判断待解压的文件是否存在
  if(!file_exists($filename)){
    die("文件 $filename 不存在！");
  }
  $starttime = explode(' ',microtime()); //解压开始的时间

  //将文件名和路径转成windows系统默认的gb2312编码，否则将会读取不到
  $filename = iconv("utf-8","gb2312",$filename);
  $path = iconv("utf-8","gb2312",$path);
  //打开压缩包
  $resource = zip_open($filename);
  $i = 1;
  //遍历读取压缩包里面的一个个文件
  while ($dir_resource = zip_read($resource)) {
    //如果能打开则继续
    if (zip_entry_open($resource,$dir_resource)) {
      //获取当前项目的名称,即压缩包里面当前对应的文件名
      $file_name = $path.zip_entry_name($dir_resource);
      //以最后一个“/”分割,再用字符串截取出路径部分
      $file_path = substr($file_name,0,strrpos($file_name, "/"));
      //如果路径不存在，则创建一个目录，true表示可以创建多级目录
      if(!is_dir($file_path)){
        mkdir($file_path,0777,true);
      }
      //如果不是目录，则写入文件
      if(!is_dir($file_name)){
        //读取这个文件
        $file_size = zip_entry_filesize($dir_resource);
        //最大读取6M，如果文件过大，跳过解压，继续下一个
        if($file_size<(1024*1024*30)){
          $file_content = zip_entry_read($dir_resource,$file_size);
          file_put_contents($file_name,$file_content);
        }else{
          echo "<p> ".$i++." 此文件已被跳过，原因：文件过大， -> ".iconv("gb2312","utf-8",$file_name)." </p>";
        }
      }
      //关闭当前
      zip_entry_close($dir_resource);
    }
  }
  //关闭压缩包
  zip_close($resource);
  $endtime = explode(' ',microtime()); //解压结束的时间
  $thistime = $endtime[0]+$endtime[1]-($starttime[0]+$starttime[1]);
  $thistime = round($thistime,3); //保留3为小数
  return "解压完毕！，本次解压花费：$thistime 秒。";
}

function httpcopy($url, $file="", $timeout=60) {
  $file = empty($file) ? pathinfo($url,PATHINFO_BASENAME) : $file;
  $dir = pathinfo($file,PATHINFO_DIRNAME);
  !is_dir($dir) && @mkdir($dir,0777,true);
  $url = str_replace(" ","%20",$url);
  
  if(function_exists('curl_init')) {
    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, $url);
    curl_setopt($ch, CURLOPT_TIMEOUT, $timeout);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, TRUE);
    $temp = curl_exec($ch);
    if(@file_put_contents($file, $temp) && !curl_error($ch)) {
      return $file;
    } else {
      return false;
    }
  } else {
    $opts = array(
      "http"=>array(
      "method"=>"GET",
      "header"=>"",
      "timeout"=>$timeout)
    );
    $context = stream_context_create($opts);
    if(@copy($url, $file, $context)) {
      //$http_response_header
      return $file;
    } else {
      return false;
    }
  }
}
function download_zip($zip_name,$zip_url)
{
    ob_start(); //打开输出
    readfile($zip_url); //输出图片文件
    $zip = ob_get_contents(); //得到浏览器输出
    ob_end_clean(); //清除输出并关闭
    file_put_contents($zip_name, $zip);
	mkdir('../'.dirname(__FILE__).$zip_name, 0777);
	@chmod('../'.dirname(__FILE__).$zip_name, 0777);
    return $zip_name;
}
function unzip_file($file, $destination){ 
// 实例化对象 
$zip = new ZipArchive() ; 
//打开zip文档，如果打开失败返回提示信息 
if ($zip->open($file) !== TRUE) { 
  die ("Could not open archive"); 
} 
//将压缩文件解压到指定的目录下 
$zip->extractTo($destination); 
//关闭zip文档 
$zip->close(); 
  echo 'Archive extracted to directory'; 
} 

?>

		<p><iframe src="../readme.txt" style="width:100%;height:465px;"></iframe></p>
</blockquote>
    </div>
  </div>
<?php include'foot.php';?>