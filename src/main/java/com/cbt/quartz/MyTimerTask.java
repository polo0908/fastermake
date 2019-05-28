package com.cbt.quartz;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.TimerTask;

import com.cbt.util.GetServerPathUtil;

public class MyTimerTask extends TimerTask{
	
	@Override
	public void run(){
		
		PrintWriter out = null;			  
        BufferedReader in = null;
        String result1 = "";
		
		try {
			  URL realUrl = new URL(GetServerPathUtil.getServerPath()+"/mail/pushEmail.do");			
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
	          
	          
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}    

}
