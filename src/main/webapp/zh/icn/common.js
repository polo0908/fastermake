$(function(){
	// 公共尾部点击更多效果
	$('.footer .more').on('click',function(){
		alert(1)
		var button_text = $('.footer .more').text();
		if(button_text == '更多'){
			$('.footer .more').text('收起').animate({'top':'280px'},300);
			$('.footer').css({'overflow':'hidden'});
			$('.footer .dl_gy,.footer .footer_in').css({'height':'340px'});
			$('.footer,.footer .footer_in .dls').css({'height':'320px'});
			$('.footer .dl_gy').animate({'height':'285px','margin-bottom':'0px'},300);
		}else if(button_text == '收起'){
			$('.footer .more').text('更多').animate({'top':'110px'},600);
			$('.footer,.footer .footer_in').animate({'height':'150px'},300);			
			$('.footer .footer_in .dls').animate({'height':'140px'},300);
			$('.footer .dl_gy').animate({'height':'94px'},300);
		};
			
	});
})