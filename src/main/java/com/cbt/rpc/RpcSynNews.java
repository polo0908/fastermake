package com.cbt.rpc;

import java.util.HashMap;
import java.util.Map;

import com.cbt.util.Client;
import com.cbt.util.GetServerPathUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RpcSynNews {

	private Logger LOG = LoggerFactory.getLogger(this.getClass());
	public void sendRequest(String url, String projectNo,String newNews,String newQuotation) {
		try {
			//当项目号为空时，直接跳出
			if(StringUtils.isBlank(projectNo)){
               return;
			}
			if (StringUtils.isBlank(url)) {
			  //url = "http://192.168.1.91:8080/ERP-NBEmail/helpServlet?action=qualityReport&className=ExternalinterfaceServlet";
			  url = GetServerPathUtil.getInnerServerPath()+"/New-Quotation/quote/receiveMessagesAndQuotes";
			}
//			JSONObject object = JSONObject.fromObject(obj);
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put("projectId", projectNo);	
			parameters.put("newNews", newNews == null ? "" : newNews);
			parameters.put("newQuotation", newQuotation == null ? "" : newQuotation);
			new Thread(new RpcSynNews().new SendHttp(url,parameters)).start();
			
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("发送失败", e);
		}
	}
	class SendHttp implements Runnable{
		
		private String url;
		
		private Map<String, String> parameters ;
		
		SendHttp(String url,Map<String, String> parameters){
			this.url = url;
			this.parameters = parameters;
		}

		@Override
		public void run() { Client.sendPost(url, parameters);
		}	
	}
}