
    var csgOrderId;
    var supplierId;
    var supplierIdEncryption;
    var shareTitle;
    var appId = "";
    var timestamp = 0;
    var nonceStr = "";
    var signature = "";
    var orderInfo = "";
    var imgSrc = "";
    var supplierName = $('#supplierName').val();

    //根据链接中type类型区分分享地址
    var type = "";


    var str = window.location.search;
    if(str != null && str != '') {
        str = str.substr(1);
        csgOrderId = str.split("&")[0].split("=")[1];
        supplierId = str.split("&")[1].split("=")[1];
        if(str.split("&").length > 2){
            type = str.split("&")[2].split("=")[1];
        }
        shareTitle = csgOrderId + "-"+ supplierName+ "-生产进度上传";
        orderInfo = csgOrderId + "生产进度上传";
        var base = new Base64();
        supplierIdEncryption = base.encode(supplierId);
    }



//发送询盘消息
function send_message(obj,isImportant){
	
	 var message = $('#message_details').val();
     var filePath = $('#filePath').val();
     var fileName = $('#fileName').val();
	 //如果isImportant为1，则表示为采购重点提示
	 if(isImportant == 1){
         message = $('#important_message').val();
         filePath = "";
         fileName = "";
	 }
	 if(message.trim() == null || message.trim() == '' || message.trim() == undefined){
			easyDialog.open({
				  container : {
				    content : '消息不能为空！'
				  },
	    		  overlay : false,
	    		  autoClose : 1000
				});
		 return false;
	 }
	 $(obj).css({'background-color':'#ddd'}).attr("disabled",true);


    $.post("/factoryInquiry/addQuoteMessage.do",
        {
            "orderId" : $('#orderId').val(),
            "csgOrderId" : $('#csgOrderId').val(),
            "message":message,
            "factoryId" : supplierId,
            "filePath":filePath,
            "fileName" : fileName,
            "isImportant" :isImportant
        }, function(result){
				 
	 $(obj).css({'background-color':'#006dcc'}).removeAttr('disabled');   	
		       if(result.state == 0){
		    	  // var quoteMessages = result.data;
                  //
		    		//  $('#message').empty();
			    	//  //询盘消息
			    	//  if(quoteMessages.length != 0){
			    	// 	 addMessage(quoteMessages);
			    	//  }
		    	  //
			    	//     $('#filePath').val('');
				   //  	$('#fileName').val('');
				   //  	$('#message_details').val('');
                   window.location.reload();

		       }else if(result.state == 2){
			          //如果还未登录，跳转登录页
			    	  window.location = "/m-zh/login.html";
			   }    
		  })
}


 //上传后返回图纸路径，以逗号隔开
function show_drawingName(obj){
		var path = $(obj).val();
	    sppath = path.split("\\");
	    var drawingName = sppath[sppath.length-1];
	    if(drawingName == null || drawingName == '' || drawingName == undefined){
	    	return false;
	    }else{
	       $('#fileName').val(drawingName);
    	   autTime();
		   $('#upload_title').children().text("上传进度");
	    }


		  //先上传后获取上传文件路径
		 $("#file_upload_id").ajaxSubmit({
			type: "post",
			url: "/upload/attachmentAndChangeName.do",
	     	dataType: "text",
	     	success: function(str){
	     	var result = eval('(' + str + ')');
	     	if(result.state == 0){
 	             $('#filePath').val(result.data);
 	             $(obj).val('');
	     	}else{
       	     	easyDialog.open({
	      		   container : {
	          		     header : '  提示信息',
	          		     content : '  上传失败'
	      		   },
					  overlay : false,
					  autoClose : 1000
	      		 });
       	     	$('#show_upload_dialog').hide();
       	        $(obj).val('');
	     	}

	     	},
			error: function(){
				 easyDialog.open({
         		   container : {
             		     header : '  提示信息',
             		     content : '  上传失败 '
         		   },
					  overlay : false,
					  autoClose : 1000
         		 });
                clearInterval(iCount);
	     		$('#show_upload_dialog').hide();
			}
	 	});

 }



 //删除报告
 function del_report(id,obj){

     var btnFn = function(){
         $.post("/report/deleteReport",
             {
                 "id":id
             },
             function(result){
                 if(result.state == 0){
                     easyDialog.open({
                         container : {
                             content : '删除成功'
                         },
                         overlay : false,
                         autoClose : 1000
                     });
                 }else if(result.state == 2){
                   window.location = "/m-zh/login.html";
				 }else if(result.state == 1){
                     easyDialog.open({
                         container : {
                             header : '提示信息',
                             content : result.message
                         },
                         overlay : false,
                         autoClose : 1000
                     });
				 }
             }
         )
         return false;
     };

     easyDialog.open({
         container : {
             header : '提示信息',
             content : '是否确认删除报告？',
             yesFn : btnFn,
             noFn : true
         }
     });

 }



//下载附件
function download_file(id){
	window.location = "/download/message-file-download.do?id="+id;
}


//获取工厂信息
var factoryInfo = getCookie("F_INFO");
//工厂id
var factoryId = getCookie("F_ID");




//时间格式化
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



//添加消息显示
function addMessage(quoteMessages){
	 $('#message').empty();
	 //询盘消息
	 if(quoteMessages.length != 0){			    		 
		 for(var i=0;i<quoteMessages.length;i++){	
			 
			 var file_div = '';
			 if(quoteMessages[i].fileName != null && quoteMessages[i].fileName != '' && quoteMessages[i].fileName != undefined  && quoteMessages[i].filePath != null && quoteMessages[i].filePath != '' && quoteMessages[i].filePath != undefined){
				 file_div = '<div class="file-download" title="'+quoteMessages[i].fileName+'">附件:<a style="text-decoration: underline;" onclick="download_file(\''+quoteMessages[i].id+'\')">'+quoteMessages[i].fileName+'</a></div>';	 
			 }		
			 
			 if(quoteMessages[i].replyStatus == 0){
                 var message_div = '<div class="dialog_0 dialog_b clearfix">'+
									 '<div class="pull-left wrap col-xs-10 clearfix">'+
									 '<div class="top">'+
									 '<em class="pull-left">'+(quoteMessages[i].realName != null ? quoteMessages[i].realName : quoteMessages[i].userName)+'</em>'+
									 '<em class="pull-right">'+(new Date(quoteMessages[i].sendTime.replace(/-/g,"/").split(".")[0])).Format("yyyy-MM-dd hh:mm:ss")+'</em>'+
									 '</div>'+
									 '<p class="text-left">'+quoteMessages[i].messageDetails+'</p>'+file_div+
									 '</div>'+
									 '<img class="imgs pull-left" src="'+(quoteMessages[i].photo != null ?  quoteMessages[i].photo : (quoteMessages[i].factoryLogo == null || quoteMessages[i].factoryLogo == '' ? '../images/defaultLogo.png' : "/static_img/factory_logo/"+quoteMessages[i].factoryId +'\/'+ quoteMessages[i].factoryLogo+""))+'"></div>'+
									 '</div>';
				 $('#message').append(message_div);
			 }
			 if(quoteMessages[i].replyStatus == 1){
                 var message_div = '<div class="dialog_0 dialog_b clearfix">'+
									 '<div class="pull-right wrap col-xs-10 clearfix">'+
									 '<div class="top">'+
									 '<em class="pull-left">'+(quoteMessages[i].realName != null ? quoteMessages[i].realName : quoteMessages[i].userName)+'</em>'+
									 '<em class="pull-right">'+(new Date(quoteMessages[i].sendTime.replace(/-/g,"/").split(".")[0])).Format("yyyy-MM-dd hh:mm:ss")+'</em>'+
									 '</div>'+
									 '<p class="text-left">'+quoteMessages[i].messageDetails+'</p>'+file_div+
									 '</div>'+
									 '<div class="imgs pull-left" src="'+(quoteMessages[i].photo != null ?  quoteMessages[i].photo : (quoteMessages[i].factoryLogo == null || quoteMessages[i].factoryLogo == '' ? '../images/defaultLogo.png' : "/static_img/factory_logo/"+quoteMessages[i].factoryId +'\/'+ quoteMessages[i].factoryLogo+""))+'"></div>'+
									 '</div>';
				 $('#message').append(message_div);
			 }
			
		 }

         document.getElementById('message').scrollTop = document.getElementById('message').scrollHeight;
	 }	
}















$(function(){


    $.ajax({
        async : false,
        type : "GET",// 请求方式
        url : "/wimpl/signature.do",// 地址，就是action请求路径
        data : {
            'pageUrl':window.location.href.split('#')[0]
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
                //window.location.href = "/fastermake-wechat/m-zh/error.html";
            }, 0);

        }
    })

    //微信分享链接
    //var url = 'https://open.weixin.qq.com/connect/oauth2/authorize?appid='+appId+'&redirect_uri=http%3a%2f%2fkuaizhizao.cn%2fwechat%2fsupplierAuthorization?csgOrderId='+csgOrderId+'&supplierId='+supplierIdEncryption+'&response_type=code&scope=snsapi_userinfo&state=cerong#wechat_redirect';
    var url = '';
    if(type==1){
        url = location.href.replace("&type=1");
    }else{
        url = website+'/report/reportList?csgOrderId='+csgOrderId+'&supplierId='+supplierIdEncryption;
    }


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

            title: shareTitle, // 分享标题

            desc: orderInfo, // 分享描述

            link : url, // 分享链接

            imgUrl: website + imgSrc, // 分享图标

            type: 'link', // 分享类型,music、video或link，不填默认为link

            /* dataUrl: '',*/ // 如果type是music或video，则要提供数据链接，默认为空

            success: function () {
                // 用户确认分享后执行的回调函数

                setTimeout(function(){
                    easyDialog.open({
                        container : {
                            content : '分享成功'
                        },
                        autoClose : 1000
                    });
                }, 0);

            },

            /*cancel: function () {
                // 用户取消分享后执行的回调函数

            }*/

        });

        //分享到朋友圈
        wx.onMenuShareTimeline({

            title: shareTitle, // 分享标题

            desc: orderInfo, // 分享描述

            link : url, // 分享链接

            imgUrl : website + imgSrc,

            success : function(){
                // 用户确认分享后执行的回调函数

                setTimeout(function(){
                    easyDialog.open({
                        container : {
                            content : '分享成功'
                        },
                        autoClose : 1000
                    });
                }, 0);
            },

            /*cancel : function(){
                // 用户取消分享后执行的回调函数

            }*/
        });
    })
    })