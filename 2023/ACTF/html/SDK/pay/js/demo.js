/*  Copyright */
var lastClickTime;
	var orderNo="20190111063614942";
	function isPhone() {  
	    var sUserAgent = navigator.userAgent.toLowerCase();  
	    var bIsIpad = sUserAgent.match(/ipad/i) == "ipad";  
	    var bIsIphoneOs = sUserAgent.match(/iphone os/i) == "iphone os";  
	    var bIsMidp = sUserAgent.match(/midp/i) == "midp";  
	    var bIsUc7 = sUserAgent.match(/rv:1.2.3.4/i) == "rv:1.2.3.4";  
	    var bIsUc = sUserAgent.match(/ucweb/i) == "ucweb";  
	    var bIsAndroid = sUserAgent.match(/android/i) == "android";  
	    var bIsCE = sUserAgent.match(/windows ce/i) == "windows ce";  
	    var bIsWM = sUserAgent.match(/windows mobile/i) == "windows mobile";
	    if (!(bIsIpad || bIsIphoneOs || bIsMidp || bIsUc7 || bIsUc || bIsAndroid || bIsCE || bIsWM) ){  
	        return false; 
	    } else {
	    	return true;
	    }  
	}
	
	$(function(){
		$('.PayMethod12 ul li').each(function(index, element) {
            $('.PayMethod12 ul li').eq(5*index+4).css('margin-right','0')
        });
		
		//鏀粯鏂瑰紡閫夋嫨
		$('.PayMethod12 ul li').click(function(e) {
            //$(this).addClass('active').siblings().removeClass('active');
        });	
		
		$(".pay_li").click(function(){
			var v = $(this).attr("value");
			if(v == ""){
				alert("鏆傛湭寮€鏀�");
				return false;
			}else{
				$(".pay_li").removeClass("active");
				$(this).addClass("active");
				$("#type").val(v);
			}
		});
		//鐐瑰嚮绔嬪嵆鏀粯鎸夐挳
		// $(".immediate_pay").click(function(){
		// 	alert("鍏徃涓氬姟璋冩暣锛屼綋楠屾敮浠樺姛鑳芥殏鏃朵笅绾匡紒");return;	//鏀粯浣撻獙寮€鍏�
			
		// 	//鍒ゆ柇鐢ㄦ埛鏄惁閫夋嫨浜嗘敮浠樻笭閬�
		// 	if(!$(".pay_li").hasClass("active")){
		// 		message_show("璇烽€夋嫨鏀粯鍔熻兘");
		// 		return false;
		// 	}
		// 	//鑾峰彇閫夋嫨鐨勬敮浠樻笭閬撶殑li
		// 	var payli =$(".pay_li[class='pay_li active']");
		// 	if(payli[0]){
		// 		$("#type").val($(payli[0]).attr("value"));
		// 		$("#payFrom").submit();
		// 	}else{
		// 		message_show("璇烽噸鏂伴€夋嫨鏀粯鍔熻兘");
		// 	}
			
		// });
		
	
		$('.mt_agree').click(function(e) {
	        $('.mt_agree').fadeOut(300);
	    });
		
		$('.mt_agree_main').click(function(e) {
	        return false;
	    });	

		
		$('.pay_sure12-main').click(function(e) {
			return false;
		});
	});

	function hide(){
		$('.pay_sure12').fadeOut(300);
	}