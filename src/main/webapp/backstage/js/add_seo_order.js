$(function(){
	
	showAll();
})


//生成数据
function showAll(){
	
	var startTime = $('#startTime').val();
	var endTime = $('#endTime').val();
	var amount = $('#amount').val();
	
	$.post("/backstage/queryFinish.do",
			{
		     "startTime": startTime,
		     "endTime":endTime,
			 "amount" : amount
			},
			 function(result){
			      if(result.state == 0){
			    	  var orders = result.data;              //结束的询盘
			    	  
			    	  //获取工艺询盘，添加到对应工艺询盘列表下
                      $('#order').empty();
			    	  if(orders){
			    		  for(var i=0;i<orders.length;i++){
			    				//获取国家
					 	     	var country = orders[i].country;
					 	     	var countryZh = getCountryZh(country);
							    var totalAmount = 0;
							    var factoryName = "";
							    var hideName = "";
							    if(!isNaN(Number(orders[i].totalAmount))){
								  totalAmount =  Number(orders[i].totalAmount/10000).toFixed(2);
							    }else{
								  continue;
							    }
							    if(orders[i].orderFactoryName != null){
								  factoryName = orders[i].orderFactoryName;
								  if(factoryName.length > 4){
									  hideName = new Array(factoryName.length-3).join("*");
								  }

								  factoryName = factoryName.substr(0, 2) + hideName + factoryName.substr(factoryName.length-2);
							   }

                               var p = '<p style="margin-top: 40px;">['+countryZh+']'+orders[i].quoteTitle + '</p><p>恭喜'+factoryName+ (new Date(orders[i].orderEndDate.replace(/-/g,"/").split(".")[0])).Format("yyyy-MM-dd")+'拿到价值'+totalAmount+'万人民币订单</p>';
                               $('#order').append(p);
			    		  }
			    	  }
			    	  
			    	  
			      }else if(result.state == 2){
			    		 //如果还未登录，跳转登录页
			    		 window.location = "/backstage/login.html";
			      }    
	      })	
}



function get_data(){
	showAll();	
}

