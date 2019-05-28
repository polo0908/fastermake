var orderId;
var orderStatus;
var selfFlag = true;
var str = window.location.search;
if(str != null && str != ''){
	str = str.substr(1);
	orderId = str.split("&")[0].split("=")[1];
}else{
    orderId = $('#order_id').val();
}
//当前报价工厂数
var currentNumber = 0;
//报价最大工厂数（默认为3）
var maxNumber = 3;
//用于判断是否为更新报价
var updateQuote = false;


$(function(){
	
	
	$.post("/inquiry/queryInquiryDetails.do",
			{"orderId" : orderId},
			 function(result){
			      if(result.state == 0){
			    	 var quoteMessages = result.data.quoteMessages; 
			    	 var quoteInfo = result.data.quoteInfo; 
			    	 var products = result.data.products; 
			    	 var compareProcess = result.data.compareProcess; 
			    	 var compareQualification = result.data.compareQualification; 
			    	 var compareState = result.data.compareState; 
			    	 var compareEquipment = result.data.compareEquipment; 
			    	 var compareProduct = result.data.compareProduct; 
			    	 var supplierQuoteInfo = result.data.supplierQuoteInfo; 
			    	 var isVip = result.data.isVip;
			    	 var isCollect = result.data.isCollect;
			    	  //是否属于被邀请
			    	 var inviteFlag = result.data.inviteFlag;
                      //允许报价的最大工厂数
                      maxNumber = quoteInfo.maxNumber;
                      //当前报价工厂数
                      currentNumber = quoteInfo.currentNumber;
                      //询盘状态
					  orderStatus = quoteInfo.orderStatus;

                      //更改询盘title,Description
                      $('meta[name="Description"]').attr('content',quoteInfo.quoteTitle + 'RFQ Detail');
                      $('title').text(quoteInfo.quoteTitle + ' RFQ');


			    	 //判断是自营还是第三方询盘
			    	 if(quoteInfo.csgOrderId == null || quoteInfo.csgOrderId == ''){
			    		 selfFlag = false;
			    		 $('#self_or_other').text('Other');
			    		 $('#self_or_other').next().text(quoteInfo.factoryName == null ? '' : quoteInfo.factoryName);
			    	 }else{
			    		 $('#end_date').parents('li').hide();
			    		 $('#delivery_date').parents('li').hide();
			    		 $('#self_or_other').text('Self');
			    		 $('#self_or_other').next().text('Quickpart.cn');
			    	 }
			    	 
			    	 //获取国家对标国旗显示
			    	 var country = quoteInfo.country;	
			    	 var flagSrc=getFlag(country);
			    	 $('#country').prev().attr('src',flagSrc);
			    	 
			    	 //排名
			    	 var rank = result.data.rank;
			    	 //工厂报价
			    	 var supplierQuotes = result.data.supplierQuotes;
			    	 
			    	 //查看是否属于自营询盘，如果是则显示对应报价员、销售照片
			    	 if(quoteInfo.csgOrderId != null && quoteInfo.csgOrderId != ''){
			    		 $('#quoter_div').find('span:first').text(quoteInfo.quoterShortName);
			    		 $('#quoter_div').find('img').attr('src',quoteInfo.quoterPhoto);
			    		 $('#sales_div').find('span:first').text(quoteInfo.salesShortName);
			    		 $('#sales_div').find('img').attr('src',quoteInfo.salesPhoto);
			    		 $('#assistant_div').find('span:first').text(quoteInfo.assistantShortName);
			    		 $('#assistant_div').find('img').attr('src',quoteInfo.assistantPhoto);
			    		 $('#assistant_div').find('.contact').text(quoteInfo.assistantTel == null ? '' : quoteInfo.assistantTel);
			    		 $('#assistant_div').find('.contace').text('qq:'+quoteInfo.assistantQQ); 
			    		 
			    		 if(quoteInfo.quoter == null || quoteInfo.quoter == ''){
			    			 $('#quoter_div').hide();
			    		 }
			    		 if(quoteInfo.initialSales == null || quoteInfo.initialSales == ''){
			    			 $('#sales_div').hide();
			    		 }
			    		 if(quoteInfo.priceAssistant == null || quoteInfo.priceAssistant == ''){
			    			 $('#assistant_div').hide();
			    		 }
			    		 $('.card').show();
			    	 }
			    	 
			    	 //询盘简述
			    	 if(quoteInfo.quoteDetail != null && quoteInfo.quoteDetail != ''){
				    	 $('.describe').find('em').text(quoteInfo.quoteDetail); 
			    	 }else{
			    		 $('.describe').hide();
			    	 }
			    	 
			    	 
			    	 //显示询盘详情二维码
			    	 var url = location.href.replace("https://","http://");
			    	 url = url.replace("offer_detail.html","detail.html");
			    	 $('#qr_code').attr('src',"/wimpl/qr-code.do?url="+url);	
			    	 
			    	 
			    	 
			    	 //询盘条件
			    	 $('#quoteId').text(quoteInfo.orderId);
			    	 $('#orderId').val(quoteInfo.orderId);
			    	 if(quoteInfo.quoteTitle == null || quoteInfo.quoteTitle == ''){
			    		 $('#quote_title').hide();
			    		 $('#quote_title').prev().hide();
			    	 }else{
				    	 $('#quote_title').text(quoteInfo.quoteTitle);
			    	 }
			    	 
			    	 
			    	 //自营询盘查看询盘状态和下单情况
			    	 var factoryName = "";
		        	 var hideName = "";
			    	 if(quoteInfo.orderFactoryName != '' && quoteInfo.orderFactoryName != null){
			    		 
			    		 factoryName = quoteInfo.orderFactoryName;
		        		 if(factoryName != null && factoryName.length > 3){
		        			 hideName = new Array(factoryName.length-3).join("*");
		        			 factoryName = factoryName.substr(0, 1) + hideName + factoryName.substr(factoryName.length-2); 
		        		 }

                         $('#order_factory').find('span').text(factoryName+' Order received, the total amount of the order is '+quoteInfo.totalAmount+'Yuan');
                         $('#order_factory').show();
			    	 }
			    	 if(quoteInfo.followDetail != '' && quoteInfo.followDetail != null){
			    		 $('#follow_status').find('span').text(quoteInfo.followDetail);
			    		 $('#follow_status').show();
			    	 }
			    	 
			    	 
			    	 
			    	 
			    	 //查看当前询盘进展 ，当已结束、已取消、生产中 则 不允许报价
			    	 if(quoteInfo.orderStatus == 2){
				    	 $('#quoteId').next().text('<Finished>');
			    	 }else if(quoteInfo.orderStatus == 3){
			    		 $('#quoteId').next().text('<Canceled>');
			    		 $('.to-quote').css({"color":"#ccc","border-color":"#666"}).attr("onclick",false).parent().css({"border-color":"#ccc"}).hide();
			    		 $('#title_ul').find('li').eq(1).attr("onclick",false);
			    	 }else if(quoteInfo.orderStatus == 5){
			    		 $('#quoteId').next().text('<Ordered>');
			    	 }else if(quoteInfo.orderStatus == 6){
			    		 $('#quoteId').next().text('<Processed>');
			    		 $('.to-quote').css({"color":"#ccc","border-color":"#ccc"}).attr("onclick",false).parent().css({"border-color":"#ccc"}).hide();
			    		 $('#title_ul').find('li').eq(1).find('a').attr("onclick",false);
			    		 $('#title_ul').find('li:last').find('a').attr("href","/zh/supplier_big_goods_four.html?orderId="+orderId);
			    		 $('#title_ul').find('li:last').find('a').attr("target","_blank");
			    		 $('#title_ul').find('li:last').show();
			    	 }


			    	 
			   		 $('#comparison_thead').find('tr').append('<th style="width:105px;">Process</th>');
		    		 $('#comparison_tbody').find('tr').eq(0).append('<td><img src="'+(compareProcess == 1 ? '../images/xz.png' : '../images/error1.png')+'" alt=""></td>');
	
			    	 if(quoteInfo.quoteLocation != 0){
			    		 $('#comparison_thead').find('tr').append('<th style="width:72px;">Location</th>');
			    		 $('#comparison_tbody').find('tr').eq(0).append('<td><img src="'+(compareState == 1 ? '../images/xz.png' : '../images/error1.png')+'" alt=""></td>');
			    	 }else{
			    		 $('#comparison_thead').find('tr').append('<th style="width:72px;">Location</th>');
			    		 $('#comparison_tbody').find('tr').eq(0).append('<td><img src="../images/xz.png" alt=""></td>');
			    	 }
			    	 if(quoteInfo.qualification != null && quoteInfo.qualification != ''){			   			    		 
			    		 $('#comparison_thead').find('tr').append('<th style="width:120px;">Certification</th>');
			    		 $('#comparison_tbody').find('tr').eq(0).append('<td><img src="'+(compareQualification == 1 ? '../images/xz.png' : '../images/error1.png')+'" alt=""></td>');
			    	 }  
			    		 
			    	 
			    	 //比较是否有产品
			    	 if(!(quoteInfo.productKeywords == null || quoteInfo.productKeywords == '' || quoteInfo.productKeywords == undefined)){
				    	 $('#comparison_thead').find('tr').append('<th style="width:80px;">Existing product</th>');
			    		 $('#comparison_tbody').find('tr').eq(0).append('<td><img src="'+(compareProduct == 0 ? '../images/error1.png' : '../images/xz.png')+'" alt=""></td>');
			    	 }
			    	 
		    		 //比较设备
			    	 if(!(quoteInfo.equipmentKeywords == null || quoteInfo.equipmentKeywords == '' || quoteInfo.equipmentKeywords == undefined)){		    		 
			    		 $('#comparison_thead').find('tr').append('<th style="border-right: 1px solid #eee;width:80px;">Equipment requirements</th>');
		    		     $('#comparison_tbody').find('tr').eq(0).append('<td style="border-right: 1px solid #eee;"><img src="'+(compareEquipment == 0 ? '../images/error1.png' : '../images/xz.png')+'" alt=""></td>');
			    	 }
			    	 
			    	 
			    	 $('#country').text((quoteInfo.country == 'Other' ? 'Other Country' : quoteInfo.country));
			    	 $('#update_date').text(quoteInfo.updateTime);
			    	 $('#payment_term').text(quoteInfo.paymentTerm);
			    	 $('#confidentiality_agreement').text((quoteInfo.confidentialityAgreement == 1 || quoteInfo.confidentialityAgreement == 2  ? 'Confidentiality agreement required': 'N/A'));

			    	 //如果给定保密协议，显示保密协议名
			    	 if(quoteInfo.confidentialityFileName){
						 var fileName = quoteInfo.confidentialityFileName;
						 $('#confidentiality_agreement').text(fileName).css({'color':'#08c','cursor':'pointer'});
                         $('#confidentiality_agreement').attr('onclick','downloadConfident('+quoteInfo.orderId+')');
					 }

			    	 $('#end_date').text(((quoteInfo.csgOrderId != null && quoteInfo.csgOrderId != '') ? 'N/A' : quoteInfo.quoteEndDate));
//			    	 $('#quote_status').text(quoteInfo.orderStatus == 1 ? '进行中' : (quoteInfo.orderStatus == 2 ? '已结束' : (quoteInfo.orderStatus == 3 ? '已取消': '')));
			    	 $('#publish_date').text((new Date(quoteInfo.publishDate.replace(/-/g,"/").split(".")[0])).Format("yyyy-MM-dd"));
			    	 $('#process').text(quoteInfo.mainProcess);

			    	 if(inviteFlag == true){
			    		 $('#current_number').text(quoteInfo.currentNumber +'(Designated factory quote)');
			    	 }else{
			    		 $('#current_number').text((quoteInfo.currentNumber < maxNumber ? quoteInfo.currentNumber+'/'+maxNumber : quoteInfo.currentNumber+'/'+maxNumber+'(The quotation factory is full)'));
			    	 }			    	
			    	 
			    	 
			    	 $('#delivery_date').text((quoteInfo.deliveryDate == null ? 'N/A' : quoteInfo.deliveryDate));
			    	 
			    	 //图纸附件下载
	    			 var drawingName = quoteInfo.drawingName;	
	    			 var drawingPath = quoteInfo.drawingPath;
	    			 if(drawingName && drawingPath){
				    	 $('#drawing_path').attr('onclick','download_drawing('+quoteInfo.orderId+')'); 
	    			 }else{
	    				 $('#drawing_path').css({'background-color':'#ccc'});
	    			 }
			    	 
			    	 
			       	 //产品介绍			    	 
			    	 $('#comparison_tbody').find('tr').eq(1).find('td').append('<h3>Description</h3>');
			    	 $('#comparison_tbody').find('tr').eq(1).find('td').append('<p>Main Process：'+(quoteInfo.mainProcess == null ? '' : quoteInfo.mainProcess)+"</p>");
			    	 $('#comparison_tbody').find('tr').eq(1).find('td').append('<p>Location：'+(quoteInfo.quoteLocation == 1 ? '江浙沪' : (quoteInfo.quoteLocation == 2 ? '深圳、广东、福建' : 'N/A'))+"</p>");
			    	 if(quoteInfo.qualification != null && quoteInfo.qualification != '' && quoteInfo.qualification != undefined){
			    		 $('#comparison_tbody').find('tr').eq(1).find('td').append('<p>Certification：'+quoteInfo.qualification+"</p>");
			    	 }
			    	 if(quoteInfo.productKeywords != null && quoteInfo.productKeywords != ''){
			    		 $('#comparison_tbody').find('tr').eq(1).find('td').append('<p>Product keyword：'+(quoteInfo.productKeywords == null ? '' : quoteInfo.productKeywords)+"</p>");
			    	 }
			    	 if(quoteInfo.equipmentKeywords != null && quoteInfo.equipmentKeywords != ''){
			    		 $('#comparison_tbody').find('tr').eq(1).find('td').append('<p>Equipment requirements：'+(quoteInfo.equipmentKeywords == null ? '' : quoteInfo.equipmentKeywords)+"</p>");
			    	 }
			    	 
			    	 if(quoteInfo.paymentTerm != null && quoteInfo.paymentTerm != '' && quoteInfo.paymentTerm != undefined){
			    		 $('#comparison_tbody').find('tr').eq(1).find('td').append('<p>Payment Terms：'+quoteInfo.paymentTerm+"</p>");
			    	 }
			    	 if(quoteInfo.quoteRemark != null && quoteInfo.quoteRemark != '' && quoteInfo.quoteRemark != undefined){
			    		 $('#comparison_tbody').find('tr').eq(1).find('td').append('<p>Other：'+quoteInfo.quoteRemark+"</p>");
			    	 }
			    	 
			    	 
			    	 
			    	 //判断是否是会员
//			    	 if(isVip == 101){			    		 
//			    		 $('#limit_quote').find('span').text('');
//			    		 $('#limit_quote').find('em').text('');
//			    		 $('.to-quote').css({"cursor":"pointer"}).removeAttr("disabled");
//			    	 }else{
//
//			    		 $('#limit_quote').show();
//				    		var hour = DateDiff(quoteInfo.publishDate,new Date());
//				    	   	if(hour > 48){
//				    		   $('#limit_quote').find('span').text('');
//					           $('#limit_quote').find('em').text('');
//					    	   $('.to-quote').css({"cursor":"pointer"}).removeAttr("disabled");				    	   	  
//				    	   	}else{
//				    	   	  hour = Number(48 - hour).toFixed(0);
//				    	   	  if(hour == 0){
//				    	   		  hour = 1;
//				    	   	  }
//				    	   	  $('#limit_quote').find('span').text(hour+"小时");	
//				    	      $('.to-quote').css({"cursor":"not-allowed","color":"#ddd"}).attr("disabled","true").removeAttr('onclick');
//				    	   	}
//			    	 }
			    	 
			    	 
			    	  //判断是否收藏
			    	 if(isCollect == 0){
		    		    $('#collect_order').text("收藏"); 
			    		$('#collect_order').prev().css( "background" ,"url(../images/sc.png)");
			    	 }else{
			    		$('#collect_order').text("已收藏"); 
			    		$('#collect_order').prev().css( "background" ,"url(../images/heart.png)");
			    	 }			    	 
			    	 
			    	 
			    	 
			    	
			    	 
			    	 
			    	 
			    	 
			    	 
			    	 //产品列表
			    	 $('#product_count').text(products.length);
                     $('#product_tbody').empty();
			    	 
			    	 var flag1 = false;    //判断是否有第二报价数
			    	 var flag2 = false;    //判断是否有第三报价数
			    	 			    		   
			    		   for(var j=0;j<products.length;j++){			    			   
			    			   var qty = products[j].quantityList;
			    			   var drawingPath = quoteInfo.drawingPath;		    			   
			    			   var list = qty.split(",");							   
							   
						  var tr1 = '<tr class="add_border-bottom"><td rowspan="2" class="add_border_td">'+
				                  				/*'<em onclick="view_detail(this)"></em>'+*/
				                  				'<div class="imgs">'+
				                               '<img src="'+((products[j].drawingPathCompress == null || products[j].drawingPathCompress == '') ? '../images/pic2.png' : products[j].drawingPathCompress)+'" alt=""/>'+
				                               '</div>'+
				 //                              (drawingPath == null ? '' : '<a href="###" class="fj1" onclick="download_drawing('+quoteInfo.orderId+')">图纸附件下载</a>')+
				                  			'</td>'+
				                  			'<td> <div class="ws w180"><span>'+products[j].productName+'</span></div></td>'+
				                  			'<td><div class="ws w180"><span>'+(products[j].process == null ? quoteInfo.mainProcess : products[j].process)+'</span></div></td>'+
				                  			'<td><div class="ws w180"><span>'+(products[j].materials == null ? '' : products[j].materials)+'</span></div></td>'+
				                  			'<td><div class="ws"><span>'+(products[j].weight == null ? '' : products[j].weight)+'</span></div></td>'+
				                  			'<td><div class="ws"><span>'+(list.length > 0 ? list[0] : '')+'</span></div></td><td><div class="ws"><span>'+(list.length > 1 ? list[1] : '')+'</span></div></td><td><div class="ws"><span>'+(list.length > 2 ? list[2] : '')+'</span></div></td>'+
				                  			'<td><div class="ws"><span>'+(products[j].annualQuantity == null ? '' : products[j].annualQuantity)+'</span></div></td>'+
			                  		   '</tr>'+
			                  		 '<tr class="add_notice_tr">'+
			                  		   	/*'<td></td>'+*/
			                  		'<td class="ljms">Description：</td>'+
	                       			'<td colspan="7" class="change_colspan">'+(products[j].productRemark == null ? 'None' : products[j].productRemark)+
	                       			'</td>'+     
				                  		/*'<td></td>'+
				                  		'<td></td>'+
				                  		'<td></td>'+
				                  		'<td></td>'+*/
			                  		   '</tr>';
							/* var tr2 = '<tr class="trcol currdis">'+
			                    			'<td class="ljms">零件描述：</td>'+
			                       			'<td colspan="8">'+(products[j].productRemark == null ? '无' : products[j].productRemark)+
			                       			'</td>'+                             			
			                       		'</tr>'; */
							   
							  $('#product_tbody').append(tr1);   
							 /* $('#product_tbody').append(tr2);*/
							   
							   
								 //判断产品是否存在第二报价 第三报价

							     if(list.length == 2){
								      flag1 = true;
								 }else if(list.length >2){
									  flag1 = true;
									  flag2 = true;
								 }
			    		   }
			    		   $('.lj_det tbody em').click();

			    	 
			    	 
			  	   //判断当前报价数(没有则不显示)
					   if(!flag2){
						   $('#product_thead').find('th').eq(7).remove();   
						   $('#product_tbody').find('tr').each(function(){
							   $(this).find('td').eq(7).remove();
							   $(this).find('.change_colspan').attr('colspan',6);
						   })
						  // $('.trcol').find('td').eq(1).attr('colspan',7);
					   }
					   if(!flag1){
						   $('#product_thead').find('th').eq(6).remove();  
						   $('#product_tbody').find('tr').each(function(){
							   $(this).find('td').eq(6).remove();
							   $(this).find('.change_colspan').attr('colspan',5);
						   })
						  // $('.trcol').find('td').eq(1).attr('colspan',6);
					   }
			    	 
			    	 
					 //添加消息显示
					  addMessage(quoteMessages);
					   
					   
					   
			    	 var supplierQuoteProducts = result.data.supplierQuoteProducts;
			    	 var supplierQuoteProduct = result.data.supplierQuoteProduct;
			    	 
			    	$('.history').empty();
			    	for(var k=0;k<supplierQuoteProduct.length;k++){
                        //当前供应商已报价
                        updateQuote = true;
			    					    		
			    		var tables = '';
			    		var trs = '';
			    		for(var n=0;n<supplierQuoteProducts[k].length;n++){
			    				
				    		var qty = supplierQuoteProducts[k][n].quantityList;
				    		var qtyList = qty.split(",");
				    	
			    			
		                    var table = '<table class="table table-bordered pull-left table2">'+
			                    '<thead>'+
			                      '<tr>'+
			                        '<th style="color:#006dcc;width: 350px;border-bottom:0 none;">'+products[n].productName+'</th>'+
			                        '<th style="border-bottom:0 none;">订量一</th>'+(supplierQuoteProducts[k][n].quoteUnitPrice2 == 0 ? '' : '<th style="border-bottom:0 none;">订量二</th>')+(supplierQuoteProducts[k][n].quoteUnitPrice3 == 0 ? '' : '<th style="border-bottom:0 none;">订量三</th>')+
                                    '<th style="border-bottom:0 none;">备注</th>'+	
                                  '</tr>'+ 
 			                    '</thead>'+
			                    '<tbody>'+
			                    '<tr class="f9">'+
			                        '<td>目标报价数量</td>'+
			                        '<td>'+(qtyList.length > 0 ? qtyList[0] : '')+'</td>'+(qtyList.length > 1 ? '<td>'+qtyList[1]+'</td>' : '')+(qtyList.length > 2 ? '<td>'+qtyList[2]+'</td>' : '')+
			                        '<td rowspan="4"><div class="bz">'+(supplierQuoteProducts[k][n].productRemark == null ? '' : supplierQuoteProducts[k][n].productRemark)+'</div></td>'+
			                    '</tr>'+
			                    '<tr >'+
			                        '<td>单价 (元)</td>'+
			                        '<td>'+(supplierQuoteProducts[k][n].quoteUnitPrice1)+'</td>'+(supplierQuoteProducts[k][n].quoteUnitPrice2 == 0 ? '' : '<td>'+supplierQuoteProducts[k][n].quoteUnitPrice2+'</td>')+(supplierQuoteProducts[k][n].quoteUnitPrice3 == 0 ? '' : '<td>'+supplierQuoteProducts[k][n].quoteUnitPrice3+'</td>')+		       
			                    '</tr>'+
			                    '<tr class="f9">'+
			                        '<td>模具费 (元)</td>'+
			                        '<td>'+(supplierQuoteProducts[k][n].quoteMoldPrice1)+'</td>'+(supplierQuoteProducts[k][n].quoteUnitPrice2 == 0 ? '' : '<td>'+supplierQuoteProducts[k][n].quoteMoldPrice2+'</td>')+(supplierQuoteProducts[k][n].quoteUnitPrice3 == 0 ? '' : '<td>'+supplierQuoteProducts[k][n].quoteMoldPrice3+'</td>')+		       
			                    '</tr>'+
			                    '<tr class="f9">'+
			                    '<td>总计 (元)</td>'+
			                    '<td>'+Number(qtyList[0]*supplierQuoteProducts[k][n].quoteUnitPrice1 + supplierQuoteProducts[k][n].quoteMoldPrice1).toFixed(0)+'</td>'+(supplierQuoteProducts[k][n].quoteUnitPrice2 == 0 ? '' : '<td>'+(Number(qtyList[1]*supplierQuoteProducts[k][n].quoteUnitPrice2) + Number(supplierQuoteProducts[k][n].quoteMoldPrice2))+'</td>')+(supplierQuoteProducts[k][n].quoteUnitPrice3 == 0 ? '' : '<td>'+(Number(qtyList[2]*supplierQuoteProducts[k][n].quoteUnitPrice3) + Number(supplierQuoteProducts[k][n].quoteMoldPrice3))+'</td>')+		       
			                    '</tr>'+
			                    '</tbody>'+
			                '</table>';	
		                    
		                    
		                    
		            
		                    
		                    var tr = '<tr class="f9">'+
										'<td>'+(supplierQuoteProducts[k][n].quoteMoldPrice1)+'</td>'+
										'<td>'+(supplierQuoteProducts[k][n].quoteProductPrice)+'</td>'+
										'<td><div class="bz">'+(supplierQuoteProducts[k][n].productRemark == null ? '' : supplierQuoteProducts[k][n].productRemark)+'</div></td>'+
									 '</tr>';
		                                
		                    
		                    
							trs +=tr;
		                    tables +=table;
			    		}
			    		 
//			    		var non_selfTable = '<table class="table table-bordered pull-left table2 non-self">'+
//												'<thead>'+
//												'<tr>'+
//													'<th class="w220">最大年定量模具费</th>'+
//													'<th class="w220">最大年定量总货值  '+qty+'件</th>'+
//													'<th>备注</th>'+
//												'</tr>'+
//											'</thead>'+
//											'<tbody>'+ trs+ 			
//											'</tbody>'+
//										'</table>';
  		    		
			    		

			    		 var path = '';
	    				 var base = new Base64();
	    				 if(supplierQuoteProduct[k].attachmentPath != null && supplierQuoteProduct[k].attachmentPath != undefined){
		    	             path = base.encode(supplierQuoteProduct[k].attachmentPath);
	    				 }

//	    				 $('#file_attachment').attr('href','/download/quote-file-download.do?path='+path+'');	
	    				 var historyQuote = "";
//	    				 if(selfFlag){
	    				     if(path != '' && path != null){
		    					historyQuote = '<p class="p1 clearfix"><i class="i1" onclick="history(this)"></i><em></em><span>日期：'+new Date(supplierQuoteProduct[k].createTime.replace(/-/g,"/").split(".")[0]).Format("yyyy-MM-dd hh:mm:ss")+' </span><em></em></p><div>'+tables+"</div><div class='text-right down_load'><span>原始报价单:</span><a href='/download/quote-file-download.do?id="+supplierQuoteProduct[k].supplierQuoteId+"'>点我下载</a></div>"; 
		    				 }else{
						    	historyQuote = '<p class="p1 clearfix"><i class="i1" onclick="history(this)"></i><em></em><span>日期：'+new Date(supplierQuoteProduct[k].createTime.replace(/-/g,"/").split(".")[0]).Format("yyyy-MM-dd hh:mm:ss")+' </span><em></em></p><div>'+tables+"</div><div class='text-right down_load'><span></span><a></a></div>";
		    				 }
//	    				 }else{
//	    					    if(path != '' && path != null){
//			    					historyQuote = '<p class="p1 clearfix"><i class="i1" onclick="history(this)"></i><em></em><span>日期：'+new Date(supplierQuoteProduct[k].createTime.replace(/-/g,"/").split(".")[0]).Format("yyyy-MM-dd hh:mm:ss")+' </span><em></em></p><div>'+non_selfTable+"</div><div class='text-right down_load'><span>原始报价单:</span><a href='/download/quote-file-download.do?id="+supplierQuoteProduct[k].supplierQuoteId+"'>点我下载</a></div>"; 
//			    				 }else{
//							    	historyQuote = '<p class="p1 clearfix"><i class="i1" onclick="history(this)"></i><em></em><span>日期：'+new Date(supplierQuoteProduct[k].createTime.replace(/-/g,"/").split(".")[0]).Format("yyyy-MM-dd hh:mm:ss")+' </span><em></em></p><div>'+non_selfTable+"</div><div class='text-right down_load'><span></span><a></a></div>";
//			    				 }
//	    				 }
 				 
	    				 

	    				$('.history_price').find('span:first').text(supplierQuoteProduct[0].priceType == 1 ? '含增值税到最近港口价格' : (supplierQuoteProduct[0].priceType == 2 ? '含增值税出厂价格' : ''));
						$('.history').append(historyQuote);
                        $('.panel').show();
                        $('.historys').show();
			    	} 
					if(supplierQuoteProduct.length == 0){
						$('.panel').hide();
						$('.historys').hide();
					}
			    	 
					//处理历史显示隐藏（超过一个则隐藏）
					show_history();
			    	 
			      }else if(result.state == 2){
			    		 //如果还未登录，跳转登录页
			    		 window.location = "/en/login.html";
			      }    			      			 
	      })
			 
	     
	      
	      
	        
    
})


     //控制历史报价显示隐藏
  function history(obj){
	  var $this = $(obj).parent().next('div');
      if($this.hasClass('dis')){
      	$this.removeClass('dis');
      	$this.next('.down_load').show();
        $this.show();
          $(obj).css({
              'background':'url(../images/red.png) no-repeat'
          })
      }else{
      	$this.addClass('dis');
        $this.hide();
        $this.next('.down_load').hide();
          $(obj).css({
              'background':'url(../images/green.png) no-repeat'
          })
      }
  }


//查看历史报价
function show_history(){
	   $('.history').find('.p1').each(function(i){
		   if(i>0){
			   $(this).next().addClass('dis');
			   $(this).next().hide();
			   $(this).next().next().hide();
			   $(this).find('i').css({
	                'background':'url(../images/green.png) no-repeat'
	            })
		   }else{
			   $(this).next().removeClass('dis');
			   $(this).next().next().show();
			   $(this).find('i').css({
	                'background':'url(../images/red.png) no-repeat'
	            })
		   }
	   })
}


//function input_message(obj){
//	var message = $(obj).val();
//	if(message == "" || message == null){
//		return false;
//	}
//	$(document).keyup(
//			function(event) {
//				if (event.keyCode == 13) {
//					message +="</br>";
//					$(obj).val(message);
//				}
//			});
//}



//查看产品详情
function view_detail(obj){


    var $this = $(obj).parent().parent().next('.trcol');
    if($this.hasClass('currdis')){
            $this.removeClass('currdis');
        $(obj).css({
            'background':'url(../images/red.png) no-repeat'
        })
    }else{
        $this.addClass('currdis');
        $(obj).css({
            'background':'url(../images/green.png) no-repeat'
        })
    }

}



var count = 3;
var countdown = null;
//添加取消收藏
function addOrCancelCollect(){

  $.post("/inquiry/addOrCancelCollect.do",	
		  {
	     "orderId":orderId	   
		 },
		 function(result){
	       if(result.state == 0){
	    	  var status = result.data;
	    	  if(status == '0'){
	    		  $('#collect_order').text("收藏");	    		 
		    	  $('#collect_order').prev().css( "background" ,"url(../images/sc.png)");
		    	  easyDialog.open({
					  container : {
					    content : '已取消收藏'
					  },
		    		  overlay : false,
		    		  autoClose : 1000
					});
	    	  }else if(status == '1'){
	    		  $('#collect_order').text("已收藏"); 
		    	  $('#collect_order').prev().css( "background" ,"url(../images/heart.png)");
		    	  $('.supplier_detail .tc').show();
		    	  countdown = setInterval(timeOut,1000); 	    	 
	    	  }
	       }else if(result.state == 2){
               $.post("/account/addHistoryUrl.do",
                   { url: "/rfq/"+orderId},
                   function(data){

                   });
               window.location = "/en/login.html";
           }
        })
   
}



/**
 * 弹框倒计时变化
 */ 
 function timeOut(){
	 count--;
     $("#count_down").html(count + "s");
     if(count<1){
         clearInterval(countdown);
         count = 3;
         $("#count_down").html(count + "s");
         $('.supplier_detail .tc').hide();
     }
 };


//发送询盘消息
function send_message(obj){
	
	 var message = $('#message_details').val();
	 var filePath = $('#filePath').val();
	 if(message.trim() == null || message.trim() == '' || message.trim() == undefined){
			easyDialog.open({
				  container : {
				    content : 'Message can not empty'
				  },
	    		  overlay : false,
	    		  autoClose : 1000
				});
		 return false;
	 }
	 $(obj).css({'background-color':'#ddd'}).attr("disabled",true);
	 
	 $.post("/inquiry/addQuoteMessage.do",	
			  {
		     "orderId" : orderId,
		     "message":message,
		     "filePath":filePath,
    	     "fileName" : $('#fileName').val()  
			 },
			 function(result){
				 
	 $(obj).css({'background-color':'#006dcc'}).removeAttr('disabled');   	
		       if(result.state == 0){
		    	  var quoteMessages = result.data;

		    		 $('#quote_message').find('.d').remove();
			    	 //询盘消息
			    	 if(quoteMessages.length != 0){			    		 
			    		 addMessage(quoteMessages);
			    	 }
		    	  
			    	    $('#filePath').val('');
				    	$('#fileName').val('');
				    	$('#message_details').val('');
		       }else if(result.state == 2){
			          //如果还未登录，跳转登录页
			    	  window.location = "/en/login.html";
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
		   $('#upload_title').children().text("Upload Progress");
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
	          		     header : '  Prompt message',
	          		     content : '  Upload failed'
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
             		     header : '  Prompt message',
             		     content : '  Upload failed '
         		   },
					  overlay : false,
					  autoClose : 1000
         		 });   
	     		$('#show_upload_dialog').hide();
			}       	     	
	 	}); 	 		    

 }


//下载附件
function download_file(id){
	window.location = "/download/message-file-download.do?id="+id;
}
//下载图纸
function download_drawing(orderId){
    easyDialog.open({
        container : {
            content : 'Require Registration and NDA Signed. Also, the buyer might only allow pre-selected manufacturers to view.'
        },
        overlay : false,
        autoClose : 4000
    });
    return false;
	window.location = "/download/drawing-download.do?orderId="+orderId;
}
//下载保密协议
function downloadConfident(orderId){
    easyDialog.open({
        container : {
            content : 'Temporary not available for oversea manufacturers'
        },
        overlay : false,
        autoClose : 2000
    });
    return false;
	window.location = "/download/file-confidentiality.do?orderId="+orderId;
}

//获取工厂信息
var factoryInfo = getCookie("F_INFO");
//工厂id
var factoryId = getCookie("F_ID");
function to_offer(){

	if(!updateQuote) {
        //如果当前报价数大于最大报价数
        if (currentNumber > maxNumber || currentNumber == maxNumber) {
            easyDialog.open({
                container: {
                    header: '  提示信息',
                    content: ' 报价工厂数已满'
                },
                overlay: false,
                autoClose: 1000
            });
            return false;
        }
        //询盘已取消
        if (orderStatus == 3) {
            easyDialog.open({
                container: {
                    header: '  提示信息',
                    content: ' 询盘已取消'
                },
                overlay: false,
                autoClose: 1000
            });
            return false;
        }
        //询盘已过期
        if (orderStatus == 4) {
            easyDialog.open({
                container: {
                    header: '  提示信息',
                    content: ' 询盘已过期'
                },
                overlay: false,
                autoClose: 1000
            });
            return false;
        }
        //询盘已授盘
        if (orderStatus == 6) {
            easyDialog.open({
                container: {
                    header: '  提示信息',
                    content: ' 询盘已授盘'
                },
                overlay: false,
                autoClose: 1000
            });
            return false;
        }
    }

    //项目还未发布
    if(orderStatus == 0){
        easyDialog.open({
            container : {
                header : '  提示信息',
                content : ' 项目还未发布'
            },
            overlay : false,
            autoClose : 1000
        });
        return false;
    }
    //项目已结束
    if(orderStatus == 2){
        easyDialog.open({
            container : {
                header : '  提示信息',
                content : ' 项目已结束'
            },
            overlay : false,
            autoClose : 1000
        });
        return false;
    }

    //询盘审核未通过
    if(orderStatus == 7){
        easyDialog.open({
            container : {
                header : '  提示信息',
                content : ' 询盘审核未通过'
            },
            overlay : false,
            autoClose : 1000
        });
        return false;
    }

    if(factoryInfo&&factoryId){
        window.location = "/zh/offer_7.html?orderId="+orderId;
    }else{
        $.post("/account/addHistoryUrl.do",
            { url: "/zh/offer_7.html?orderId="+orderId},
            function(data){

            });
        window.location = "/en/login.html";
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
				 var message_div =  '<div class="d d2">'+
					    				 '<div class="imgs pull-left">'+
				                         '<img src="'+(quoteMessages[i].photo != null ?  quoteMessages[i].photo : (quoteMessages[i].factoryLogo == null || quoteMessages[i].factoryLogo == '' ? '../images/defaultLogo.png' : "/static_img/factory_logo/"+quoteMessages[i].factoryId +'\/'+ quoteMessages[i].factoryLogo+""))+'" alt="" class="pull-left"/></div>'+
				                         '<div class="c pull-left">'+
				                             '<div class="arrs">'+
				                                 '<div class="arr1"></div>'+
				                                 '<div class="arr1 arr2"></div>'+
				                             '</div>'+
				                             '<div class="t1 clearfix">'+
				                                 '<span class="pull-left">'+(quoteMessages[i].realName != null ? quoteMessages[i].realName : quoteMessages[i].userName)+'</span>'+
				                                 '<em class="pull-right">'+(new Date(quoteMessages[i].sendTime.replace(/-/g,"/").split(".")[0])).Format("yyyy-MM-dd hh:mm:ss")+'</em>'+
				                             '</div>'+
				                             '<div class="t2">'+quoteMessages[i].messageDetails+'</div>'+file_div+
				                        ' </div>'+
				                     '</div>'; 
				 $('#message').append(message_div);
			 }
			 if(quoteMessages[i].replyStatus == 1){
				 var message_div =      '<div class="d d1">'+
			                                 '<div class="c pull-left">'+
			                                 '<div class="arrs">'+
			                                     '<div class="arr1"></div>'+
			                                     '<div class="arr1 arr2"></div>'+
			                                 '</div>'+
			                                 '<div class="t1 clearfix">'+
			                                     '<span class="pull-left">'+(quoteMessages[i].realName != null ? quoteMessages[i].realName : quoteMessages[i].userName)+'</span>'+
			                                     '<em class="pull-right">'+(new Date(quoteMessages[i].sendTime.replace(/-/g,"/").split(".")[0])).Format("yyyy-MM-dd hh:mm:ss")+'</em>'+
			                                 '</div>'+
			                                 '<div class="t2">'+quoteMessages[i].messageDetails+'</div>	'+file_div+					
			                                 '</div><div class="imgs pull-left">'+
				                             '<img src="'+(quoteMessages[i].photo != null ?  quoteMessages[i].photo : (quoteMessages[i].factoryLogo == null || quoteMessages[i].factoryLogo == '' ? '../images/defaultLogo.png' : "/static_img/factory_logo/"+quoteMessages[i].factoryId +'\/'+ quoteMessages[i].factoryLogo+""))+'" alt="" class="pull-left"/></div>'+	
			                         '</div>';
				 $('#message').append(message_div);
			 }
			
		 }	
		 
		 $('#message').scrollTop( $('#message').height());
	 }	
}



