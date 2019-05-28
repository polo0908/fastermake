
    var csgOrderId;
    var str = window.location.search;
    if(str != null && str != '') {
        str = str.substr(1);
        csgOrderId = str.split("&")[0].split("=")[1];
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
	 
	 $.post("/inquiry/addQuoteMessage.do",	
			  {
		     "orderId" : $('#orderId').val(),
			 "csgOrderId" : $('#csgOrderId').val(),
		     "message":message,
		     "filePath":filePath,
    	     "fileName" : fileName,
			 "isImportant" :isImportant
			 },
			 function(result){
				 
	 $(obj).css({'background-color':'#006dcc'}).removeAttr('disabled');   	
		       if(result.state == 0){
		    	  // var quoteMessages = result.data;
                  //
		    		//  $('#quote_message').find('.d').remove();
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
                 clearInterval();
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
                     $(obj).parent().parent().remove();

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



