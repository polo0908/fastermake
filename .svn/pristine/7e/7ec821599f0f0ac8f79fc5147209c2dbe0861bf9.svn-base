var orderId;
var str = window.location.search;
if(str != null && str != ''){
	str = str.substr(1);
	orderId = str.split("&")[0].split("=")[1];
}

//总报价工厂数
var total;
//是否属于更新报价
var isUpdateQuote = false;


$(function(){
		
	
	$.post("/inquiry/queryInquiryDetails.do",	
			{"orderId" : orderId},
			 function(result){
			      if(result.state == 0){
			    	 var quoteInfo = result.data.quoteInfo; 
			    	 var products = result.data.products; 
			    	 var isVip = result.data.isVip;
			    	 var factoryInfo = result.data.factoryInfo;
			    	 
				    //历史报价记录
				    var supplierQuoteInfo = result.data.supplierQuoteInfo;
				    var supplierQuoteProducts = result.data.supplierQuoteProducts;
			    	var supplierQuoteProduct = result.data.supplierQuoteProduct;
			    	 			    	 
		    	 //支付条款选择为采购商选择的
		    	 $("#payment_select").find("option[value='"+quoteInfo.paymentTerm+"']").attr("selected",true);		    
		         $('#quoteId').text(quoteInfo.orderId); 	 
		         $('#orderId').val(quoteInfo.orderId); 	 
			     
		         //显示报价标题
		        if(quoteInfo.quoteTitle == null || quoteInfo.quoteTitle == ''){
		    		 $('#quote_title').hide();
		    		 $('#quote_title').prev().hide();
		    	}else{
			    	 $('#quote_title').text(quoteInfo.quoteTitle);
		    	} 
		         
		        //显示第三方厂家名称
		        $('#self_or_other').text('第三方');
	    		$('#self_or_other').next().text(quoteInfo.factoryName == null ? '' : quoteInfo.factoryName);
		        
			    
			   if(products.length > 0){	
			    $('#qty').text(products.length); 
				   
			    for(var i=0;i<products.length;i++){				    	
			    	var qty = products[i].quantityList;
	    			var list = qty.split(",");
	    			                               
			        var o_tr =  '<tr>'+
				        	    '<input type="hidden" value="'+products[i].id+'">'+
									'<td ><div class="imgs">'+
											'<img src="'+(products[i].drawingPathCompress == null ? '../images/pic2.png' : products[i].drawingPathCompress)+'">'+
										'</div></td>'+
									'<td><input type="text" onkeyup="keyUp(this)" onKeyPress="keyPress(this)" onblur="onBlur(this)" class="form-control" value="'+(supplierQuoteProducts != '' && supplierQuoteProducts != null ? (supplierQuoteProducts[0][i].quoteMoldPrice1) : '')+'"></td>'+
									'<td><input type="text" onkeyup="keyUp(this)" onKeyPress="keyPress(this)" onblur="onBlur(this)" class="form-control" value="'+(supplierQuoteProducts != '' && supplierQuoteProducts != null ? (supplierQuoteProducts[0][i].quoteProductPrice) : '')+'"></td>'+
									'<td rowspan="4"><textarea class="form-control">'+(supplierQuoteProducts != '' && supplierQuoteProducts != null ? (supplierQuoteProducts[0][i].productRemark == null ? '' : supplierQuoteProducts[0][i].productRemark) : '')+'</textarea></td>'+
								'</tr>';    
                    
                    
                    $('#product_div').find('tbody').append(o_tr);
                    
                    //判断不同报价数量table，宽度大小
//                     if(supplierQuoteProducts != '' && supplierQuoteProducts != null){
//                        switch (list.length) {
//										case 1:
//											$('#product_div').find('table').eq(i).css({"width":"54.5%"}); 		
//											break;
//										case 2:
//											$('#product_div').find('table').eq(i).css({"width":"77.3%"}); 		
//											break;					
//										default:
//											break;
//									 }
//                      }else{
//                    	  switch (list.length) {
//										case 1:
//											$('#product_div').find('table').eq(i).css({"width":"45.5%"}); 		
//											break;
//										case 2:
//											$('#product_div').find('table').eq(i).css({"width":"65.5%"}); 		
//											break;					
//										default:
//											break;
//						 }
//                     }
                
			      }     
	    
			   }    
			   
			   //询盘明细赋值
			   $('#title_ul').find('li:first').find('a').attr('href','/rfq/'+orderId+'');
			   
			   
			   

		    	
		    	//历史报价
		    	if(supplierQuoteInfo != null && supplierQuoteInfo != '' && supplierQuoteInfo != undefined){
		    		
		    		//更新是否属于更新报价状态
		    		isUpdateQuote = true;
		    		
		    		$('#title_ul').find('li:first').find('a').attr('href','/zh/offer_detail.html?orderId='+orderId+'');
		    		
		    		var validityDays = supplierQuoteInfo.validityDays;
		    		var paymentTerm = supplierQuoteInfo.paymentTerm;
		    		var paymentRemark = supplierQuoteInfo.paymentRemark;
		    		var quoteRemark = supplierQuoteInfo.quoteRemark;
		    		var quoteReasons = supplierQuoteInfo.quoteReasons;
		    		var priceType = supplierQuoteInfo.priceType;

		    		
		    		//交期选中状态
		    		 $('input:radio[name="dx"]').each(function(){
		    			if($(this).val() == validityDays ){
		    				$(this).attr("checked","checked");
		    			} 
		    		 })
		    		 
		    		 //价格类型选中状态
		    		$('input:radio[name="optionsRadios"]').each(function(){
		    			if($(this).val() == priceType ){
		    				$(this).attr("checked","checked");
		    			} 
		    		 })
		    		 
		    		 
		    		 
		    		 $("#payment").val(paymentTerm);
		    		 $('#payment_select option').each(function(){
		    			if($(this).val() == paymentTerm ){
		    				$(this).attr("selected","selected");
		    			}else{
		    				$("#payment").removeAttr("readonly");
		    				$("#payment").show();
		    				$("#payment").focus();
		    			} 
		    		 })
		    		 $('#quote_remark').val((quoteRemark == null ? '' : quoteRemark));
		    		 $('#quoteReasons').val((quoteReasons == null ? '' : quoteReasons));
		    	}		    	
		    	 
                    
			}else if(result.state == 2){
	    		 //如果还未登录，跳转登录页
	    		 window.location = "/zh/login.html";
	        }   
	          
	})
	
	
	
	
	    //支付条款改变   
//		$('#payment_select').change(function(){
//			var payment = $(this).find("option:selected").text(); 
//			if(payment == "其他"){
//				$('#paymentRemark').show();
//			}else{
//				$('#paymentRemark').hide();
//			}
//			
//		})
		
			
		
	$('.inquiry_detail .section nav li a').hover(function(){
        $(this).addClass('active')
        .parent().siblings('li').children('a').removeClass('active');
    })


    /* 更多消息下拉 */
    $('.header_top_in .xxs').click(function(){
        $(this).children('.xx').css({
            "border-color":"#dfdfdf",
            "border-bottom-color":"transparent"
        })
        $(this).children('.xxlist').toggle();
        $(this).children('.xxlist').show();
    })

    $('.header_top_in .xxs').hover(function(){
        $(this).children('.xx').css({
            'border-color':'#dfdfdf',
            'border-bottom-color':'transparent'
        })
    },function(){
        $(this).children('.xx').css({
            'border-color':'transparent',
        })
    })

    /*头部向下箭头点击变成向上箭头*/
    $('.header_top_in .iconf').click(function(){
        $(this).css({
            'background':'url(../images/xs.png)'
        });




        if($('.xxlist').hasClass('dis')){
            $(this).css({
                'background':'url(../images/xx.png)'
            });
        }else{
            $(this).css({
                'background':'url(../images/xs.png)'
            });
        }
    })
	
})




	    //提交报价
		function submit_quote(status,obj){
				
	      //如果不是更新报价，则进行工厂人数验证    
	      if(!isUpdateQuote){
				//查询当前报价数量
				verifyTotal();
				if(total == 5 || total > 5){
				    easyDialog.open({
						  container : {
							header : '  提示信息',
						    content : '当前报价人数已满'
						  },
			    		  overlay : false,
			    		  autoClose : 1000
						});
					return false;
				}
	      }
		    var flag = true;
		    var productList = '';
		    var productIds = '';
		    var productRemarks = '';
		    //获取报价数量
		    $('#product_div').find('tbody').each(function(){
		    	    This = $(this);
		    		var products = '';
		    		This.find('tr').each(function(i){
			    			var productPrice = $(this).find('input').eq(2).val();
			    			var moldPrice = $(this).find('input').eq(1).val();	   
			    			var totalAmount = Number(productPrice) + Number(moldPrice);
                            var price = 0;
			    			
					    	if(productPrice == null || productPrice == '' || productPrice == undefined){
					    		 easyDialog.open({
									  container : {
										header : '  提示信息',
									    content : '价格不能为空'
									  },
						    		  overlay : false,
						    		  autoClose : 1000
									});
					    		flag = false;
					    		return false;
					    	}
					    	products += price + "&" + moldPrice + "&" + totalAmount + "&" + productPrice + "&"; 
		    		})
		    		   		    		  
			          products = products.substring(0, products.length-1);
					  productList += products + ",";
					  var productId = This.find('input:first').val();
					  productIds +=productId + ',';		
					  
					  var productRemark = This.find('textarea').val();
					  productRemarks +=productRemark + ",,";
	    	
		    })

		    productIds = productIds.substring(0, productIds.length-1);
		    productRemarks = productRemarks.substring(0, productRemarks.length-2);
		    
		    var validityDays = $('input:radio[name="dx"]:checked').val();
		    if(validityDays == null || validityDays == ''){
		    	 easyDialog.open({
					  container : {
						header : '  提示信息',
					    content : '请选择报价有效期'
					  },
		    		  overlay : false,
		    		  autoClose : 1000
					});
		    	 return false;
		    }
		    
		    if(!flag){
		    	return;
		    }
		    
		    //获取价格类型
		    var priceType = $('input:radio[name="optionsRadios"]:checked').val();
		    
		    
			$(obj).css({'background-color':'#ddd'}).attr("disabled",true);
		    
		    $.post("/inquiry/addSupplierQuote.do",	
		    		{
		    	     "orderId" : orderId,
		    	     "paymentTerm": $("#payment").val(),
		    	     "quoteRemark": $('#quote_remark').val(),
		    	     "attachmentPath": $('#filePath').val(),
		    	     "quoteReasons" : $('#quoteReasons').val(),
		    	     "paymentRemark" : $('#paymentRemark').val(),
		    	     "quoteStatus" : status,
		    	     "productList" : productList,
		    	     "productIds" : productIds,
		    	     "validityDays" : validityDays,
		    	     "fileName" : $('#fileName').val(),
		    	     "productRemarks" : productRemarks,
		    	     "priceType" : priceType 
		    	     },
				   function(result){		    	    	 
		    	    $(obj).css({'background-color':'#006dcc'}).removeAttr('disabled');   	 
				     if(result.state == 0){	
				    					    	 
				    	var quoteInfoId = result.data.quoteInfoId;
	
				    	 //同步到内部报价
				    	 $.post("/sendPort/sendQuoteInfo.do",	
						    		 {
				    		          "quoteInfoId":quoteInfoId
						    	     },
								    function(result){
						    	     	 
						       })
						       
						 	  //发送微信通知
				    	 $.post("/wimpl/quote_note.do",	
						    		 {
				    		           "orderId" : orderId				    		          
						    	     },
								    function(result){
						    	     	 
						       })            
						       
						       
						 easyDialog.open({
							  container : {
							    header : '  提示信息',
							    content : '报价成功'
							  },
				    		  overlay : false,
				    		  autoClose : 1000
							});
				    	 setTimeout(function(){show()},1000);
						       
				    					    
				     }else if(result.state == 2){
			    		 //如果还未登录，跳转登录页
			    		 window.location = "/zh/login.html";
			         }else if(result.state == 3){
					    	 easyDialog.open({
								  container : {
								    header : '  提示信息',
								    content : 'vip已过期,请充值'
								  },
					    		  overlay : false,
					    		  autoClose : 1000
								});  
				    }else{
					    	 easyDialog.open({
								  container : {
								    header : '  提示信息',
								    content : '保存失败'
								  },
					    		  overlay : false,
					    		  autoClose : 1000
								}); 
				    }
		    })		      
	    }

		
		
//验证报价数量		
function verifyTotal(){
	$.ajax({  
         type : "post",  
         url : "/inquiry/queryQuoteAmount.do", 
         data: {"orderId" : orderId},
         async : false,  
         success : function(result){  
            total = result.data;
         }  
    }); 
}		
		
//支付方式改变事件
function select_payment(obj){
	if($(obj).val() == '（）%预付，其余出货后付清'){
		$('#payment').show();
		$('#payment').attr('readonly',false).val($(obj).val());
		$('#payment').focus();
	}else{
		$('#payment').hide();
		$('#payment').attr('readonly',true).val($(obj).val());
	}
}
	
		
		
function show(){
	 window.location =  "/rfq/"+orderId;
}

//文件上传
function show_drawingName(obj){
	var path = $(obj).val();
    sppath = path.split("\\");
    var drawingName = sppath[sppath.length-1];	  	   
    if(drawingName == null || drawingName == '' || drawingName == undefined){
    	return false;
    }else{
       $('#fileName').val(drawingName);
       $(obj).parent().next().text(drawingName);
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
	             var filename=result.data.substr(result.data.lastIndexOf('/')+1);  
	             $('.fj_name').attr('href',"/static_img/quote_file/"+orderId+"/quote/"+filename);
	             $('.fj_name').attr('download',drawingName);
	             
	             
	        	 //解析报价单
 	    	     //只有上传的是excel，才能进行报价解析
        	        var gen = filename.lastIndexOf("."); 
        	        var type = filename.substr(gen); 
        	        if(type.toLowerCase()  == ".xls" || type.toLowerCase()==".xlsx"){
        	        	
	    	        	 $.post("/excel/plastic.do",	
					    		 {
			    		         path:result.data
					    	     },
							    function(result){
					    	    	 
				    	    	    if(result.state == 0){
				    	    	    	var excelVO = result.data;
					    			    //判断报价是否已输入，输入则不解析
					    			    //获取报价数量
					    	    	    if( $('#product_div').find('tbody').find('input').eq(1).val() != ''){
					    	    	    	return false;
					    	    	    }
					    	    	   
					    			    $('#product_div').find('tbody').each(function(j){
					    			    	 if(j > excelVO.length-1){
					    			    		return false;
					    			    	 }
					    			    	 This = $(this);
					    			    	 var input_tl = $(this).find('input').length;
					    			    		var products = '';
					    			    		This.find('tr').eq(1).find('input').each(function(i){		    			    			
					    				    			var price = $(this).val(excelVO[j].unitPrice);
					    				    			var moldPrice = This.find('tr').eq(2).find('input').eq(i).val(excelVO[j].moldPrice);				    						    					    					    						    
					    				    			var remark = This.find('textarea').val(excelVO[j].remark);				    						    					    					    						    
					    				    			calculator($(this));
					    			    		})
					    			    })   		
				    	    	    }					    	    	    
					       })
        	        }
		    	
	             
	             
	             
     	}else{
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

     	},
		error: function(){
			 easyDialog.open({
     		   container : {
         		     header : '  提示信息',
         		     content : ' 上传失败 '
     		   },
				  overlay : false,
				  autoClose : 1000
     		 });   
     		$('#show_upload_dialog').hide();
		}       	     	
 	}); 	 		    
}


//计算总价
function calculator(obj){
	
	 var index = $(obj).parent('td').index();
	 var unitPrice = $(obj).val();
	 
	 var number=Number(unitPrice);
	 if(isNaN(number)){
		 return false;
	 }	
	 
	 var qty = $(obj).parents('tbody').find('tr:first').find('td').eq(index+1).text();
	 var moldPrice = $(obj).parents('tr').next().find('td').eq(index).find('input').val();
	 if(moldPrice == null || moldPrice == '' || moldPrice == undefined){
		moldPrice = 0;
	 }
	 var totalPrice = Number(unitPrice*qty)+ Number(moldPrice);
	 totalPrice = Number(totalPrice).toFixed(0);
	 if(isNaN(totalPrice)){ 
		return false;
     }
	 $(obj).parents('tbody').find('tr:last').find('td').eq(index).find('span').text(totalPrice);
}
//计算总价
function mold_calculator(obj){	 
	
	
	var index = $(obj).parent('td').index();
	var moldPrice = $(obj).val();
	var number=Number(moldPrice);
	 if(isNaN(number)){
		 return false;
	 }	 
	
	var qty = $(obj).parents('tbody').find('tr:first').find('td').eq(index+1).text();
	var unitPrice = $(obj).parents('tr').prev().find('td').eq(index).find('input').val();
	if(unitPrice == null || unitPrice == '' || unitPrice == undefined){
		unitPrice = 0;
	}
	var totalPrice = Number(unitPrice*qty)+ Number(moldPrice);
	totalPrice = Number(totalPrice).toFixed(0);
	 if(isNaN(totalPrice)){ 
			return false;
	 }
	$(obj).parents('tbody').find('tr:last').find('td').eq(index).find('span').text(totalPrice);
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
	 if (!ob.value.match(/^[\+\-]?\d*?\.?\d*?$/)) 
		 ob.value = ob.t_value; 	 
	 else ob.t_value = ob.value; 
	 if (ob.value.match(/^(?:[\+\-]?\d+(?:\.\d+)?)?$/)) 
		 ob.o_value = ob.value;
}
function keyUp(ob) {
	 if (!ob.value.match(/^[\+\-]?\d*?\.?\d*?$/)) ob.value = ob.t_value; else ob.t_value = ob.value; if (ob.value.match(/^(?:[\+\-]?\d+(?:\.\d+)?)?$/)) ob.o_value = ob.value;
	 if ($(ob).val() == 'undefined'){
		 $(ob).val('');	 
	 }
	        }
function onBlur(ob) {
	if(!ob.value.match(/^(?:[\+\-]?\d+(?:\.\d+)?|\.\d*?)?$/))ob.value=ob.o_value;else{if(ob.value.match(/^\.\d+$/))ob.value=0+ob.value;if(ob.value.match(/^\.$/))ob.value=0;ob.o_value=ob.value};
}




