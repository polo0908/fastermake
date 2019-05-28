package com.cbt.rpc;

import com.cbt.util.Client;
import com.cbt.util.GetServerPathUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


public class RpcSynSuccessNote {

	private Logger LOG = LoggerFactory.getLogger(this.getClass());
	public void sendRequest(String url, String projectNo,String factoryId) {
		try {
			//当项目号为空时，直接跳出
			if(StringUtils.isBlank(projectNo)){
               return;
			}
			if (StringUtils.isBlank(url)) {
		     url = GetServerPathUtil.getServerPath()+"/wimpl/success_quote";
			}
		    String param = "projectNo="+projectNo+"&factoryId="+factoryId;
			new Thread(new RpcSynSuccessNote().new SendHttp(url,param)).start();
			
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("发送失败", e);
		}
	}
	class SendHttp implements Runnable{
		
		private String url;
		private String param;

		
		SendHttp(String url,String param){
			this.url = url;
			this.param = param;
		}

		@Override
		public void run() { Client.sendGet(url, param);
		}	
	}
}