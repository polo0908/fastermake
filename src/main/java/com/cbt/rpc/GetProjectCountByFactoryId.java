package com.cbt.rpc;

import com.cbt.util.Client;
import com.cbt.util.GetServerPathUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;


public class GetProjectCountByFactoryId {

	private Logger LOG = LoggerFactory.getLogger(this.getClass());
	public int sendRequest(String factoryId) {
		int count = 0;
		try {
			//当项目号为空时，直接跳出
			if(StringUtils.isBlank(factoryId)){
               return count;
			}
			Map<String, String> parameters = new HashMap<String, String>();
			String url = GetServerPathUtil.getTaskServerPath()+"/port/getProjectCount";
			parameters.put("factoryId", factoryId);
			String json = new SendHttp(url, parameters).call();
			if(StringUtils.isNotBlank(json)){
				count = Integer.parseInt(json);
				return count;
			}else{
				return count;
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("获取工厂任务系统项目失败"+factoryId , e);
			return count;
		}

	}

	//获取任务系统项目详情，返回值为json对象
	class SendHttp implements Callable<String> {
		
		private String url;
		
		private Map<String, String> parameters ;
		
		SendHttp(String url,Map<String, String> parameters){
			this.url = url;
			this.parameters = parameters;
		}

		@Override
		public String call() {
			String json = Client.sendPost(url, parameters);
//			json = json.replaceFirst("\"","");
//			json = json.substring(0,json.length()-1);
			return json;
		}	
	}
}