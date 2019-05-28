package com.cbt.controller;

import com.cbt.util.GetServerPathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
@Component
public class SendMailUtil {
	
	
	/**
	 * 发送邮件通知（报价、回复消息）
	 * @Title sendMail 
	 * @Description
	 * @param email
	 * @param content
	 * @return
	 * @return String
	 */
	
    private static Logger logger = LoggerFactory.getLogger(SendMailUtil.class);

   
   
   /**
    * 发送邮件
    * @Title sendMail 
    * @Description
    * @return void
    */	
   public static void sendMail(String email,String title, String content){

			  
			  PrintWriter out = null;			  
		      BufferedReader in = null;
		      String result1 = "";
		      try {
			      	
		    	  content = URLEncoder.encode(content, "UTF-8");
		          URL realUrl = new URL(GetServerPathUtil.getNbmailPath()+"/NBEmail/helpServlet?action=SendEmail1&className=ExternalInterfaceServlet");
		          // 打开和URL之间的连接
		          URLConnection conn = realUrl.openConnection();
		          // 设置通用的请求属性
		         // conn.setRequestProperty("accept", "*/*");
		          conn.setRequestProperty("connection", "Keep-Alive");
		          conn.setRequestProperty("user-agent",
		                  "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
		          // 发送POST请求必须设置如下两行
		          conn.setDoOutput(true);
		          conn.setDoInput(true);
		          // 获取URLConnection对象对应的输出流
		          out = new PrintWriter(conn.getOutputStream());
		          // 发送请求参数emaillAddress, sm,map,path
		          out.print("&email="+email+"&&title="+title+"&content="+content);
		          // flush输出流的缓冲
		          out.flush();
		          // 定义BufferedReader输入流来读取URL的响应
		          in = new BufferedReader(
		                  new InputStreamReader(conn.getInputStream()));
		          String line;
		          while ((line = in.readLine()) != null) {
		              result1 += line;
		          }
		          result1 = new String(result1.getBytes("gbk"),"utf-8");
		          
		          logger.info("========发送推送询盘成功=========");
		      } catch (Exception e) {
		    	  logger.error("=========发送推送询盘邮件失败=========",e);
		      }
			  
   }
}
