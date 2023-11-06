
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<meta name="applicable-device" content="pc,mobile">
<link href="/favicon.ico" rel="shortcut icon"/>
<title>MYMcode测试-支付</title>
<script src="pay/js/jquery-2.1.4.min.js"></script>
<!--script src="pay/js/qrcode.min.js"></script>
<!-- <script src="pay/js/sweetalert.min.js"></script> -->
<!-- <link rel="stylesheet" href="pay/css/sweetalert.css"/> -->
<link rel="stylesheet" href="pay/css/bootstrap.min.css"/>
<link rel="stylesheet" href="pay/css/style.css"/>
<link rel="stylesheet" href="pay/css/m_reset.css"/>

<link href="pay/css/demo_reset.css" rel="stylesheet" type="text/css">
<script src="pay/js/demo_jquery-1.11.3.min.js"></script>
<script src="pay/js/demo.js"></script>
<link href="pay/css/demo.css" rel="stylesheet" type="text/css">
<link href="pay/css/demo_main.css" rel="stylesheet" type="text/css">

</head>
<body>

<div class="container">
   <div class="row">

    <div class="col-lg-12 col-sx-12">
        <div class="head">
 | <a target="_blank" href="/">返回首页</a></span>

</div>  
<form name=alipayment action=epayapi.php method=post target="_blank">
    <div id="body" style="clear:left">
      <div class="content">
            <div class="content-head">
                                 <div class="logo" align="center">
         <a href="/"><img src="/Core/Assets/Img/logo.png"/></a>
     </div>
                <div class="order">

                <span class="sleft">订单编号: 
				<input size="30" name="WIDout_trade_no" value="<?php echo date("YmdHis").mt_rand(100,999); ?>" style= "background-color:transparent;border:0;"/></span>
				<br>
				<span class="sleft">商品名称: 
				<input size="30" name="WIDsubject" value="测试商品"/>
                <!--span class="sright">收款商家: <input type="text" id="param" name="param" value="Bycode码支付"style= "background-color:transparent;border:0;"/></span-->
            </div>
            </div>
            <div class="step step2">
                <ul class="steps clearfix">
                    <li>选择商品</li>
                    <li class="active">确认付款</li>
                    <li>下单成功</li>

                </ul>
            </div>

            <div class="pay_amount">
                <span class="amount_text">支付金额:</span>
                <span class="amount font-red">￥
				<input size="30" name="WIDtotal_fee" value="<?php echo '0.'.mt_rand(01,99); ?>" style= "background-color:transparent;border:0;width:64px;"/></span>
            </div>



            <div class="order" style="margin-top: 20px;margin-bottom: 5px;">
                <span class="address-title">请选择付款方式</span>
            </div>
            <div class="ways">
                <!--<span style="outline: none;border: none;">-->
                <!--     <select id="type" style="border: 1px solid #3497FC;margin-left: 7px;font-size: 20px;color:#3497FC;width:138px;height:42px;">-->
                <!--     <option value="2">支付宝支付 <img src="pay/picture/wx_pay.svg" style="margin:0 auto;width:34px" alt=""/></option>-->
                <!--     <option value="1">微信支付 <img src="pay/picture/wx_pay.svg" style="margin:0 auto;width:34px" alt=""/></option>-->
                <!-- </select>-->
                
                <div class="w1080 PayMethod12">
			    <!--<div class="row">-->
    				<!--<h2>支付方式</h2>-->
    				<!--ul>
    					<li class="pay_li active" type="radio" name="type" value="alipay">
    						<i class="i1"></i>
    						<span>支付宝</span>
    					</li>
    
    					<li class="pay_li" type="radio" name="type" value="wxpay">
    						<i class="i2"></i>
    						<span>微信扫码</span>
    					</li>
						<li class="pay_li" type="radio" name="type" value="qqpay">
    						<i class="i3"></i>
    						<span>QQ扫码</span>
    					</li>
    				</ul-->
			    <!--</div>-->
			                        <dd>
                        <label><input type="radio" name="type" value="alipay" checked="">支付宝&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label><input type="radio" name="type" value="qqpay">QQ钱包&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label><input type="radio" name="type" value="wxpay">微信</label><label>
                    </dd>
		        </div>
                <input hidden="hidden" type="radio" name="type" value="alipay">

                <!--<div class="borders wechat_pay" style="text-align: center" checked>-->
                <!--   <p>-->
                <!--       <img src="pay/picture/wx_pay.svg" style="margin:0 auto;width:34px" alt=""/><span style="margin-left: 7px;font-size: 13px;color:#878787;">微信支付</span>-->
                <!--   </p>-->
                <!--</div>-->
                <!--<div class="borders ali_pay">-->
                <!--    <p><img src="pay/picture/alipay.svg" style="margin:0 auto;width:80px" alt=""/> </p>-->
                <!--</div>-->
            </div>
            </div>
            <div class="go-pay">
                <span style="margin-right: 10px">测试体验商品不会发货</span>
                        <input type="submit" class="buy-button" style="width: 100px;cursor: pointer;"  value="发起支付"/>

                <!--<button class="buy-button" style="width: 100px;">立即支付</button>-->
            </div>

        </div>
</form>
        <div class="foot">
    <p> <span > © Copyright 2014-2021<a href="/">MYMcode码支付</a> | </a></span></p>
</div>    </div>

</div>
</div>

<!--script>

    $(function(){
		$('.PayMethod12 ul li').each(function(index, element) {
            $('.PayMethod12 ul li').eq(5*index+4).css('margin-right','0')
        });
		
		//
		$('.PayMethod12 ul li').click(function(e) {
            //$(this).addClass('active').siblings().removeClass('active');
        });	
		
		$(".pay_li").click(function(){
			var v = $(this).attr("value");
			//console.log(v);
			if(v == ""){
				alert("未选择支付方式");
				return false;
			}else{
				$(".pay_li").removeClass("active");
				$(this).addClass("active");
				$("#type").val(v);
			}
		});
	});

   var newTime = new Date().getTime();
    $("#WIDout_trade_no").val(newTime);
    function zf() {
        var p = "trade_no="+$("#WIDout_trade_no").val() + "&sitename="+$("SU5UTOa1i%2BivlQ%3D%3D").val();
        window.location.href = "Mcode_Pay.php?" + p;

    }
</script-->

</body>
</html>
