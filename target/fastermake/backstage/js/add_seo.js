$(function(){
	
	showAll();
})


//生成数据
function showAll(){
	
	var startTime = $('#startTime').val();
	var endTime = $('#endTime').val();
	
	$.post("/backstage/queryQuoteByMainProcess.do",	
			{
		     "startTime": startTime,
		     "endTime":endTime
			},
			 function(result){
			      if(result.state == 0){
			    	  var orders = result.data.orders;              //各工艺询盘
			    	  var processes = getProcessList();             //工艺列表			    	  
			    	  $('#process').empty();
			    	  
			    	  for(var i=0;i<processes.length;i++){
			    		  var p = '<p><input type="hidden" value="'+processes[i]+'"><span style="font-size: 18px;">======================================='+processes[i]+'类==========================================</span></p>';
			    	      $('#process').append(p);
			    	  }			    	  
			    	  
			    	  //获取工艺询盘，添加到对应工艺询盘列表下
			    	  if(orders){
			    		  for(var i=0;i<orders.length;i++){
			    				//获取国家
					 	     	var country = orders[i].country;
					 	     	var countryZh = getCountryZh(country);					 	     	
					 	     	var p = '<p><span></span>、['+countryZh+']'+orders[i].quoteTitle + '&nbsp;&nbsp;' + (orders[i].materials == null ? '' : "材料："+orders[i].materials) + '&nbsp;&nbsp;' + (new Date(orders[i].publishDate.replace(/-/g,"/").split(".")[0])).Format("yyyy-MM-dd") + '&nbsp;&nbsp;' + (orders[i].totalAmount == 0 ? '' : "预估总值："+orders[i].totalAmount+"元&nbsp;&nbsp;")  +  'https://www.kuaizhizao.cn/m-zh/detail.html?order='+orders[i].orderId+'</p>';
					 	     	
					 	     	$('#process').find('input').each(function(){
					 	     		if($(this).val() == orders[i].mainProcess){
					 	     			$(this).parent().append(p);
					 	     			var tl = $(this).parent().find('p').length;
					 	     			$(this).parent().find('p:last').find('span').text(tl);
					 	     		}
					 	     	})
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

