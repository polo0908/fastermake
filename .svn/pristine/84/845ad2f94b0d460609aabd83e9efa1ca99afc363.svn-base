$(function(){
	
	$.post("/backstage/queryMessageList.do",	
			 function(result){
			      if(result.state == 0){
			    	  var messageList = result.data;
			    	  $('#tbody').empty();
			    	  for(var i=0;i<messageList.length;i++){
			    		  var fileName = messageList[i].fileName;
						  var filePath = messageList[i].filePath;		
						  var receiverInfo = messageList[i].receiverInfo;
						  var sendInfo = messageList[i].sendInfo;
						  var messageDetails = messageList[i].messageDetails;
						  
			    		  var tr =  '<tr onclick="show_detail('+messageList[i].id+')">'+
										'<td><span class="s1">'+(sendInfo == null || sendInfo.factoryName == "" || sendInfo.factoryName == null ? '系统管理员' : sendInfo.factoryName)+'</span></td>'+
										'<td><span class="s2">'+(receiverInfo == null || receiverInfo.factoryName == null ? '' : receiverInfo.factoryName)+'</span></td>'+
										'<td><span class="s3">'+messageList[i].messageTitle+'</span></td>'+
										'<td><span class="s4">'+(messageList[i].orderId == null ? '' : messageList[i].orderId)+'</span></td>'+
										'<td><span class="s5" style="overflow: hidden;text-overflow: ellipsis;white-space: nowrap;" title="'+messageDetails+'">'+messageDetails+'</span></td>'+
										'<td><span class="s6">'+messageList[i].createTime+'</span></td>'+
										'<td><span class="s6">'+(fileName != null && filePath != null ? '是' :'否' )+'</span></td>'+
									'</tr>';
			    		  $('#tbody').prepend(tr);
			    	  }
			    	  $('.table-sort').dataTable({"order": [[ 5, "desc" ]]});
			      }else if(result.state == 2){
			    		 //如果还未登录，跳转登录页
			    		 window.location = "/backstage/login.html";
			      }    
	      })	
})




//查看消息详情
function show_detail(id){
	window.location = '/backstage/detail.html?id='+id;	
}