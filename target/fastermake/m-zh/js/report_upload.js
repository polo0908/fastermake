var orderId;
var csgOrderId;
var supplierIdD;
var str = window.location.search;
if(str != null && str != ''){
	str = str.substr(1);
	orderId = str.split("&")[0].split("=")[1];
    csgOrderId = str.split("&")[1].split("=")[1];
    supplierIdD = str.split("&")[2].split("supplierId=")[1];
	$('#file_upload_id1').find('input[name="orderId"]').val(orderId);
	$('#file_upload_id1').find('input[name="csgOrderId"]').val(csgOrderId);
	$('#file_upload_id2').find('input[name="orderId"]').val(orderId);
	$('#file_upload_id2').find('input[name="csgOrderId"]').val(csgOrderId);
	$('#file_upload_id').find('input[name="orderId"]').val(orderId);
    $('#file_upload_id').find('input[name="csgOrderId"]').val(csgOrderId);
    $('#video_ul').find('input[name="csgOrderId"]').val(csgOrderId);
    $('#video_ul').find('input[name="orderId"]').val(orderId);


/*	if(projectStage == 1){
		$('#content').text('-大货生产-生产进度上传');
	}else if(projectStage == 2){
        $('#content').text('-大货生产-材质证明和质量证明上传');
	}else if(projectStage == 3){
        $('#content').text('-大货生产-出货质量报告上传');
	}else if(projectStage == 4){
        $('#content').text('-大货生产-出运报告上传');
    }*/


    $('#back').attr("href",'/report/reportList?csgOrderId='+csgOrderId+'&supplierId='+supplierIdD);

	$('#orderId').text(orderId?orderId:csgOrderId);
}




//下载进度报告
function download_report(id){
	window.location = "/download/report-download.do?id="+id;
}


//删除报告
function del_report(obj){
	$(obj).parents('tr').remove();
}
//删除图片
function del_pic(obj){
	$(obj).parents('li').remove();
}


//保存所有信息
function save(obj){
	$(obj).css({'background-color':'#ddd'}).attr("disabled",true);
    var reportList = [];         //需要保存的报告集合
	$('#pic_ul').find('li').each(function () {
		var report = $(this).find('img').attr('data-title');
		if(report){
            reportList.push(report);
		}
    })
    $('#material_div').find('.mt10').each(function () {
        var report = $(this).find('input').val();
        reportList.push(report);
    })
    $('#report_div').find('.mt10').each(function () {
        var report = $(this).find('input').val();
        reportList.push(report);
    })
    $('#video_ul').find('li').each(function () {
        var report = $(this).find('input[name="videos"]').val();
        var remarkName = $(this).find('input[name="remarkName"]').val();
        if(remarkName){
            var reportJson = {};
            var reportStr = decodeURIComponent(report);
            reportJson = jQuery.parseJSON(reportStr);
            reportJson["fileName"]=remarkName;
            report = encodeURIComponent(JSON.stringify(reportJson));
        }

        reportList.push(report);
    })

	$.post("/report/saveAll",
			{
		     "remark":$('#remark').val(),
             "reportList" : JSON.stringify(reportList),
			 "orderId": orderId,
             "csgOrderId":csgOrderId
			},
			 function(result){
				$(obj).css({'background-color':'#006dcc'}).removeAttr('disabled');	
			      if(result.state == 0){
		    		 easyDialog.open({
			      		   container : {
			          		     header : ' 提示信息',
			          		     content : ' 保存成功'
			      		   },
							  overlay : false,
							  autoClose : 1000
			      		 });   
		    		  window.location = '/report/reportList?csgOrderId='+csgOrderId+'&supplierId='+supplierIdD;
			      }else if(result.state == 2){
			    		 //如果还未登录，跳转登录页
			    		 window.location = "/zh/login.html";
			       }    
		     })	      
}








//上传周报
function upload_photos(obj){	
	var path = $(obj).val();
    sppath = path.split("\\");
    var fileName = sppath[sppath.length-1];
    if(fileName == null || fileName == '' || fileName == undefined){
    	return false;
    }else{
        var extStart=path.lastIndexOf(".");
        var ext=path.substring(extStart,path.length).toUpperCase();
        if(".jpg|.png|.bmp|.jpeg".toUpperCase().indexOf(ext.toUpperCase())==-1){
            easyDialog.open({
                container : {
                    content : '只允许上传jpg、png、bmp、jpeg格式的图片'
                },
                overlay : false,
                autoClose : 2000
            });
            return false;
        }

	   autTime(); 
	   $('#upload_title').children().text("上传进度");
    }	 		    	
 		
	  
	  //先上传后获取上传文件路径
	 $("#file_upload_id1").ajaxSubmit({    			
		type: "post",
		url: "/report/insertReport",
     	dataType: "text",
     	success: function(str){
     	var result = eval('(' + str + ')');		
     	if(result.state == 0){       	
     		var reports = result.data.reports;
     		if(reports.length > 0){
                for(var i=0;i<reports.length;i++){
                    var reportJson =  JSON.stringify(reports[i]);
                    reportJson = encodeURIComponent(reportJson);
                    var li = '<li>'+
                              '<img src="'+reports[i].photoPath+'" data-title='+reportJson+'>'+
                              '<span class="glyphicon glyphicon-remove-sign" onclick="del_pic(this)"></span>'+
					          '<em class="glyphicon glyphicon-repeat" onclick="rotate(\''+reports[i].photoPath+'\',this,0)"></em>'+
							'</li>';
                    $('#pic_ul').prepend(li);

				}
			}
     	}else if(result.state == 2){
   		 //如果还未登录，跳转登录页
   		 window.location = "/m-zh/login.html";
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
     		$('#show_upload_dialog').hide();
		}       	     	
 	}); 	 		    
}


function fileChange(that){
    var filepath=$(that).val();
    if(filepath=="")
    {
        return;
    }
    var extStart=filepath.lastIndexOf(".");
    var ext=filepath.substring(extStart,filepath.length).toUpperCase();
    if(".jpg|.png|.bmp|.jpeg".toUpperCase().indexOf(ext.toUpperCase())==-1){
        easyDialog.open({
            container : {
                content : '只允许上传jpg、png、bmp、jpeg格式的图片'
            },
            overlay : false,
            autoClose : 1000
        });
        return false;
    }


    var tl = that.files.length;
    //以图片宽度为1600进行压缩
    var fileName;
    var file;
    for(var i=0;i<tl;i++){
        var fileName = that.files[i].name;
        var fileForm = that.files[i];
        synPic(that,fileForm,fileName);
    }
}


function synPic(obj,fileForm,fileName){

    var width = 1024;
    lrz(fileForm, {
        width: width
    }).then(function (rst) {

        // var imgdata = rst.base64;
        // var reg = new RegExp('\\!\\\\}', 'g');
        // imgdata = imgdata.replace(reg);
        // alert(rst.base64);
        //压缩后异步上传
        $.ajax({
            url : "/report/fileUploadPicture",
            type: "post",
            data : {
                "orderId":orderId,
                "fileType":0,
                "projectStage":projectStage,
                "csgOrderId":csgOrderId,
                "fileName":fileName,
                "imgdata":rst.base64     //压缩后的base值
            },
            dataType:"json",
            cache:false,
            async:true,
            success : function(result) {
                if(result.state == 0){
                    var report = result.data.report;
                    if(report){
                            var reportJson =  JSON.stringify(report);
                            reportJson = encodeURIComponent(reportJson);
                            var li = '<li>'+
                                '<img src="'+report.photoPath+'" data-title='+reportJson+'>'+
                                '<span class="glyphicon glyphicon-remove-sign" onclick="del_pic(this)"></span>'+
                                '<em class="glyphicon glyphicon-repeat" onclick="rotate(\''+report.photoPath+'\',this,0)"></em>'+
                                '</li>';
                            $('#pic_ul').prepend(li);
                    }
                }else if(result.state == 2){
                    //如果还未登录，跳转登录页
                    window.location = "/m-zh/login.html";
                }

            },
            error : function(){
                alert("上传失败");
            }
        });
    });
}



//旋转当前图片
function rotate(filePath,obj,num){
    var deg = num + 90;
    $.ajax({
        url : "/report/rotateImg",
        type: "POST",
        data : {
            filePath:filePath,
            degree:90
        },
        dataType:"json",
        cache:false,
        async:true,
        success : function(json) {
            $(obj).attr('onclick','rotate(\''+filePath+'\',this,'+deg+')');
            $(obj).parent().find('img').css({"transform":"rotate(" + deg + "deg)"});
        }
    })
}



//上传视频
function upload_video(obj){
		var path = $(obj).val();
	    sppath = path.split("\\");
	    var fileName = sppath[sppath.length-1];	  	   
	    if(fileName == null || fileName == '' || fileName == undefined){
	    	return false;
	    }else{
            //获取文件大小
            var fileSize =getFilesize(obj);
            if(fileSize>30){
                easyDialog.open({
                    container : {
                        content : '最大文件大小30M'
                    },
                    overlay : false,
                    autoClose : 2000
                });
                return false;
            }
            $(obj).parents('form').find('.video-size').text(fileSize+"Mb");
			$(obj).parents('form').find('input[name="fileName"]').val(fileName);
			$(obj).parents('form').find('.file-name').text(fileName);
    	   autTime(); 
		   $('#upload_title').children().text("上传进度");
	    }	 		    	
     		
		  
		  //先上传后获取上传文件路径
		 $(obj).parents('form').ajaxSubmit({
			type: "post",
			url: "/report/insertReport",
	     	dataType: "text",
	     	success: function(str){
	     	var result = eval('(' + str + ')');		
	     	var reports = result.data.reports;
	     	if(result.state == 0){  
                if(reports && reports.length > 0){
                    for(var i=0;i<reports.length;i++){
                        var reportJson =  JSON.stringify(reports[i]);
                        reportJson = encodeURIComponent(reportJson);
                         $(obj).parents('form').find('input[name="videos"]').val(reportJson);
                    }
				}
	     		
	     	}else if(result.state == 2){
	    		 //如果还未登录，跳转登录页
	    		 window.location = "/m-zh/login.html";
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
	     		$('#show_upload_dialog').hide();
			}       	     	
	 	}); 	 		    

 }



//上传报告
function upload_report(obj){
    var path = $(obj).val();
    sppath = path.split("\\");
    var fileName = sppath[sppath.length-1];
    if(fileName == null || fileName == '' || fileName == undefined){
        return false;
    }else{

        //获取文件大小
        var fileSize =getFilesize(obj);
        if(fileSize>30){
            easyDialog.open({
                container : {
                    content : '您的视频超过30M了，请按网页上方法先压缩一下再上传'
                },
                overlay : false,
                autoClose : 3000
            });
            return false;
        }

        $(obj).parents('form').find('input[name="fileName"]').val(fileName);
        autTime();
        $('#upload_title').children().text("上传进度");
    }


    //先上传后获取上传文件路径
    $(obj).parents('form').ajaxSubmit({
        type: "post",
        url: "/report/insertReport",
        dataType: "text",
        success: function(str){
            var result = eval('(' + str + ')');
            var reports = result.data.reports;
            if(result.state == 0){
                if(reports && reports.length > 0){
                    for(var i=0;i<reports.length;i++){
                        var reportJson =  JSON.stringify(reports[i]);
                        reportJson = encodeURIComponent(reportJson);
                        var div = '<div class="clearfix mt10">'+
                            '<a class="del pull-left btn" onclick="del(this)">删除</a>'+
                            '<p class="pull-left name">文档名:'+reports[i].fileName+'</p><input type="hidden" value="'+reportJson+'">'+
                            '</div>';

                        $(obj).parents('form').after(div);
                    }
                }

            }else if(result.state == 2){
                //如果还未登录，跳转登录页
                window.location = "/m-zh/login.html";
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
            $('#show_upload_dialog').hide();
        }
    });

}



/**
 * 获取输入字符串长度
 * @returns {Boolean}
 */
function getCharacterLength(){
	var character = $('#remark').val();
	var tl = character.length;
	if(tl > 300){
		easyDialog.open({
   		   container : {
       		     header : ' 提示信息',
       		     content : '最多只能300个字'
   		   },
					  overlay : false,
					  autoClose : 1000
   		   });   		
		$('#remark').val(character.substring(0,300));
		return false;
	}else{
		$('#remark').next().find('i').text(tl);
	}
}
//获取编辑框大小
function get_char(obj){
	var character = $(obj).val();
	var tl = character.length;
	if(tl > 300){
		easyDialog.open({
   		   container : {
       		     header : ' 提示信息',
       		     content : '最多只能300个字'
   		   },
					  overlay : false,
					  autoClose : 1000
   		   });   		
		$(obj).val(character.substring(0,300));
		return false;
	}else{
		$(obj).next().find('i').text(tl);
	}
}



/*
 * 检测文件大小
 */
function getFilesize(file) {
    var fileSize = file.files[0].size / 1024 / 1024;
    fileSize = Number(fileSize).toFixed(1);
    return fileSize;
}




function compare(){
	/*侧边栏长度控制效果*/
	var h1 = $(document.body).height()+230 ;
	var h2 = window.screen.availHeight  ;
	if(h1 < h2){
		$('#footer').addClass('footer1');
	}else{
		$('#footer').removeClass('footer1');
	}
}




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



function keyPress(ob) {
	 if (!ob.value.match(/^[\+\-]?\d*?\.?\d*?$/)) ob.value = ob.t_value; else ob.t_value = ob.value; if (ob.value.match(/^(?:[\+\-]?\d+(?:\.\d+)?)?$/)) ob.o_value = ob.value;
	}
function keyUp(ob) {
	 if (!ob.value.match(/^[\+\-]?\d*?\.?\d*?$/)) ob.value = ob.t_value; else ob.t_value = ob.value; if (ob.value.match(/^(?:[\+\-]?\d+(?:\.\d+)?)?$/)) ob.o_value = ob.value;
	        }
function onBlur(ob) {
	if(!ob.value.match(/^(?:[\+\-]?\d+(?:\.\d+)?|\.\d*?)?$/))ob.value=ob.o_value;else{if(ob.value.match(/^\.\d+$/))ob.value=0+ob.value;if(ob.value.match(/^\.$/))ob.value=0;ob.o_value=ob.value};
	}


