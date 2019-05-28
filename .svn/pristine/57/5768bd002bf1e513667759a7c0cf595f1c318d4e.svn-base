
var maxPage = 1;
var scrollPage = 1;
var quoteStatus = null;
$(function(){
	queryAll(1);
	
	  //移动端滚动分页
    //如果是完成项目禁止滚动事件
    if(quoteStatus != 2){
        $(window).scroll(function(){
            var scrollTop = $(this).scrollTop();
            var scrollHeight = $(document).height();
            var windowHeight = $(this).height();
            if (scrollTop + windowHeight == scrollHeight) {
                if(scrollPage == maxPage){
                    return false;
                }
                scrollPage++;
                queryAll(scrollPage);
            }
        })
    }else{
    	//如果生产项目，不显示搜索，显示工厂名
        $('#select1').hide();
        $('#select2').show();
	}
})

 var currentPage = 1;

//查询所有正常询盘
 function queryAll(page){
	
	var process = $('#select_process').find('option:selected').text();

	var product = $('#product_name').val();
	if(product == undefined){
		product  == "";
	}	
	if(process == undefined || process.trim() == "工艺"){
		   process = "";
	}
		
	
	
	//后台获取数据链接
	var queryUrl = "";
	var str = window.location.search;
	if(str != null && str != ''){
		quoteStatus = str.substr(1);
		quoteStatus = quoteStatus.split("&")[0].split("=")[1];
	}
	
	//根据状态获取链接
	if(quoteStatus == 1){
		$('#quote_ul').find('li').eq(0).addClass("active");
		queryUrl = "/inquiry/queryQuoteList.do";
	}else if(quoteStatus == 2){
        $('#quote_ul').find('li').eq(4).addClass("active");
        queryUrl = "/inquiry/queryFinishQuoteList.do";
	}else if(quoteStatus == 102){
        $('#quote_ul').find('li').eq(2).addClass("active");
        queryUrl = "/inquiry/queryMessageOrder.do";
    }else if(quoteStatus == 101){
        queryUrl = "/inquiry/queryCollectList.do";
    }else if(quoteStatus == 100){
        $('#quote_ul').find('li').eq(1).addClass("active");
        queryUrl = "/inquiry/queryInvitation.do";
    }else if(quoteStatus == 7){
        $('#quote_ul').find('li').eq(6).addClass("active");
        queryUrl = "/inquiry/queryQuoteList.do";
    }else{
        $('.ml_r_0').show();
        $('.row_logo').find('a').hide();
        queryUrl = "/inquiry/queryInquiryList.do";
	}
	
	
	$.post(queryUrl,
			{
		     "status":quoteStatus,
		     "process":process.trim(),
		     "product" : product,
		     "customerType" : $('#select_type').val(),
		     "page" : page
			 },
			function(result){
		      if(result.state == 0){
		    	  var inquiryOrders = result.data.inquiryOrders;
		    	  var totalOrder = result.data.totalOrder;
                  var projectList = result.data.projectList;
                  var factoryInfo = result.data.factoryInfo;
		    	  maxPage = Math.ceil(totalOrder/18);
		    	  
		    	  var tl = inquiryOrders.length;		    	  
		    	  var p_tl = projectList?projectList.length:0;
                  //显示工厂名
				  $('#select2').find('span').text(factoryInfo?(factoryInfo.factoryName?factoryInfo.factoryName:''):'');

		    	  //判断当前筛选是否为 '工艺'
		    	  var selectProcess = $('#select_process').find('option:selected').text();
	    	      if((selectProcess.trim() != '' || product != '') && scrollPage == 1){
	    			  $('#tbody').empty();
	    		  }


				  for (var i = 0; i < tl; i++) {
					  //获取国家国旗
					  var country = inquiryOrders[i].country;
					  var flagSrc = getFlag(country);
					  var tr = '<tr onclick="window.location=\'/m-zh/detail.html?orderId=' + inquiryOrders[i].orderId + '\'">' +
						  '<td class="th1">' +
						  '<a class="imgs" href="###">' +
						  '<img src="' + flagSrc + '" alt="" class="img1"/>' +
						  '<img src="' + ((inquiryOrders[i].drawingPathCompress == null || inquiryOrders[i].drawingPathCompress == '' ) ? '/images/pic2.png' : inquiryOrders[i].drawingPathCompress) + '" alt="" class="img2"/>' +
						  '</a>' +
						  '</td>' +
						  '<td class="th2">' +
						  '<span class="word">' + (inquiryOrders[i].quoteTitle == null ? inquiryOrders[i].productName : inquiryOrders[i].quoteTitle) + '</span>' +
						  '<span class="word">' + (inquiryOrders[i].mainProcess == null ? '' : inquiryOrders[i].mainProcess) + '</span>' +
						  '<span class="word">' + (inquiryOrders[i].state == 1 ? '江浙沪' : (inquiryOrders[i].state == 2 ? '深圳、广东、福建' : '不限')) + '</span>' +
						 /* '<span class="word s2">已传开工视频&nbsp;&nbsp;已传材质报告</span>'+
						  '<span class="word s1 red">缺开工视频&nbsp;&nbsp;缺材质报告</span><span class="word s2 red"></span>'+*/
						 
						  '<span class="word">' + (new Date(inquiryOrders[i].publishDate.replace(/-/g, "/").split(".")[0])).Format("yyyy-MM-dd") + '</span>' +
						  (inquiryOrders[i].quoteState == 1 ? '<button>已报价</button>' : (inquiryOrders[i].quoteState == 6 ? '<button>已过期</button>' : (inquiryOrders[i].messageStatus != null && inquiryOrders[i].messageStatus != '' ? '<button>咨询过</button>' : ''))) +
						  '</td>' +
						  '</tr>';
                      //如果自营生产、完成项目，则显示内部询盘数据
                      if(quoteStatus == 2 && inquiryOrders[i].csgOrderId != null) {

                      }else{
                          $('#tbody').append(tr);
					  }

				  }


		    	  //返单项目
		    	  for(var j=0;j<p_tl;j++){
					  var tr =	'<tr onclick="goUpload(\''+projectList[j].projectNo+'\',\''+projectList[j].factoryId+'\')">'+
										'<td class="th1">'+
										'<a class="imgs" href="###">'+
											'<img src="'+((projectList[j].productImg == null || projectList[j].productImg == '' ) ? '/images/pic2.png' : 'http://112.64.174.34:10010/product_img/'+projectList[j].productImg)+'" alt="" class="img2"/>'+
										'</a>'+
									'</td>'+
									'<td class="th2">'+
										'<span class="word">项目号：'+(projectList[j].projectNo == null?"":projectList[j].projectNo)+'</span>'+
										'<span class="word">'+(projectList[j].projectName == null?"":projectList[j].projectName)+'</span>'+
										'<span class="word">交期：'+(projectList[j].deliveryDate == null ? '暂无':projectList[j].deliveryDate)+'</span>'+
										'<span class="word">上次更新动态：'+(projectList[j].uploadDate == null ? '从未更新' : (new Date(projectList[j].uploadDate.replace(/-/g, "/").split(".")[0])).Format("yyyy-MM-dd"))+'</span>'+
                                        '<span class="word">'+(projectList[j].uploadVideo ? '' : '<span style="color:red;">缺开工视频</span>')+'&nbsp;&nbsp;&nbsp;'+(projectList[j].uploadMaterial != true ? '<span style="color:red;">缺材质报告</span>' : '')+'</span>'+
					               '</td>'+
								'</tr>';
					      console.log(projectList[j].uploadVideo ? '11' : '缺开工视频');
		    			  $('#tbody').append(tr);

		    	  }
                  /* 表格隔行换色效果*/
                  $('table tr:even').css({
                      'background-color':'#ececec'
                  }) ;

		    	  	    	  
		    	  
		      }else if(result.state == 2){
		          //如果还未登录，跳转登录页
		    	  window.location = "/m-zh/login.html";
		      }  
		  })			  		  
} 
 //根据工艺筛选
 function queryByProcess(obj){
	scrollPage = 1;
    queryAll(1);	
 }
 //根据关键词筛选
 function queryByKey(obj){
	 scrollPage = 1;
    queryAll(1);	
 }
 
 //跳转更新页面
 function goUpload(csgOrderId,supplierId) {
     var base = new Base64();
     var supplierIdD = base.encode(supplierId);
     window.location = '/report/reportList?csgOrderId='+ csgOrderId +'&supplierId='+supplierIdD;
 }


 
 Date.prototype.Format = function (fmt) { //author: meizz
	 var o = {
	 "M+": this.getMonth() + 1, //月份
	 "d+": this.getDate(), //日
	 "h+": this.getHours(), //小时
	 "m+": this.getMinutes(), //分
	 "s+": this.getSeconds(), //秒
	 "q+": Math.floor((this.getMonth() + 3) / 3), //季度
	 "S": this.getMilliseconds() //毫秒
	 };
	 if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	 for (var k in o)
	 if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	 return fmt;
	 }

 
 
 
function share(){
	var appId = "";
	var timestamp = 0;
	var nonceStr = "";
	var signature = "";
	$.ajax({
			async : false,
			type : "GET",// 请求方式
			url : "/wimpl/signature.do",// 地址，就是action请求路径
			data : {
				'pageUrl':window.location.href.split('#')[0],
			},
			dataType : "json",// 数据类型text xml json script jsonp
			success : function(msg) {
				appId = msg.appid;
				timestamp = msg.timestamp;
				nonceStr = msg.noncestr;
				signature = msg.signature;
			},
			error : function() {
				setTimeout(function(){
					window.location.href = "/m-zh/error.html";
				}, 0);
				
			}
	})

	wx.config({

	    debug: false, // true开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。

	    appId: appId, // 公众号的唯一标识

	    timestamp: timestamp, // 时间戳

	    nonceStr: nonceStr, // 随机串

	    signature: signature,// 签名

	    jsApiList: ['onMenuShareAppMessage','onMenuShareTimeline','hideMenuItems','showOptionMenu','showMenuItems'] // 需要使用的JS接口列表

	});

	wx.ready(function(){

		//不隐藏菜单
		wx.showOptionMenu();
		
		
		//隐藏分享到QQ、QQ空间，微博和脸书功能
		wx.hideMenuItems({

		    menuList: ['menuItem:share:qq','menuItem:share:QZone','menuItem:share:weiboApp','menuItem:share:facebook']

		});
		//开放分享给朋友、分享到朋友圈功能
		wx.showMenuItems({

		    menuList: ['menuItem:share:appMessage','menuItem:share:timeline']

		});
		//分享给朋友
		wx.onMenuShareAppMessage({

			    title: '询盘信息', // 分享标题

			    desc: '快制造发布了的新的询盘，快来参与报价吧！', // 分享描述

			    link : website+'/m-zh/quote_list.html', // 分享链接

			    imgUrl: website+'/images/default_logo.png', // 分享图标

			    type: 'link', // 分享类型,music、video或link，不填默认为link

			   /* dataUrl: '',*/ // 如果type是music或video，则要提供数据链接，默认为空
			    trigger: function (res) {
			        // 不要尝试在trigger中使用ajax异步请求修改本次分享的内容，因为客户端分享操作是一个同步操作，这时候使用ajax的回包会还没有返回
			        alert('用户点击发送给朋友');
			      },
			    success: function () { 
			        // 用户确认分享后执行的回调函数
			    	
			    	setTimeout(function(){
			    		window.location.href = "/m-zh/shared.html";
					}, 0);
			    	
			    },

			    /*cancel: function () {
			        // 用户取消分享后执行的回调函数

			    }*/

			});
		
		//分享到朋友圈
		wx.onMenuShareTimeline({
			
			title: '快制造发布了的新的询盘，快来参与报价吧！', // 分享标题
			
			link : website+'/m-zh/quote_list.html',
			
			imgUrl : website+'/images/default_logo.png',
			
			success : function(){
				// 用户确认分享后执行的回调函数
				
				setTimeout(function(){
					window.location.href = "/m-zh/shared.html";
				}, 0);
			},
			
			/*cancel : function(){
				// 用户取消分享后执行的回调函数
				
			}*/
		});

		

	})
}


