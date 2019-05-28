package com.cbt.rpc;

import com.alibaba.fastjson.JSON;
import com.cbt.util.Client;
import com.cbt.util.GetServerPathUtil;
import com.cbt.util.SerializeUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;


public class GetTaskProject {

	private Logger LOG = LoggerFactory.getLogger(this.getClass());
	public Map<Object,Object> sendRequest(String projectNo,String factoryId) {
		Map<Object, Object> map = new HashMap<Object,Object>();
		try {
			//当项目号为空时，直接跳出
			if(StringUtils.isBlank(projectNo)){
               return map;
			}
			String url = GetServerPathUtil.getTaskServerPath()+"/port/getProjectInfo";
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put("projectNo", projectNo);
			parameters.put("factoryId", factoryId);
			String json = new SendHttp(url, parameters).call();
			if(StringUtils.isNotBlank(json)){
				map =  SerializeUtil.JsonToMap(json);
				return map;
			}else{
				return map;
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("获取任务系统项目详情失败"+projectNo , e);
			return map;
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
//			json = json.replace("\\","");
			return json;
		}	
	}
}