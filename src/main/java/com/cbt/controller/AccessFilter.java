package com.cbt.controller;

import com.cbt.util.GetServerPathUtil;
import com.cbt.util.Md5Util;
import com.cbt.util.SecurityHelper;
import com.cbt.util.WebCookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


/**
 * 验证用户是否登录
 * @author polo
 *
 */
public class AccessFilter implements Filter{

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private static final String SERVER_NAME_ZH = "kuaizhizao.cn";
	private static final String SERVER_NAME_EN = "quickpart.cn";


	public void destroy() {
		
		
	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;	
	     //设置当前quickpart.cn域名直接访问，跳转en/index.html
		 StringBuffer url = request.getRequestURL();
		 String path = url.toString();
		 String serverName = request.getServerName();
//		 logger.info(serverName);
//		 HttpSession session = request.getSession();
//		 session.setAttribute("viewURL", path);	
		 
		 
//		 if(path.endsWith(".html") || path.endsWith(".do")){
//
//			 if(path.endsWith(".html")){
//				 chain.doFilter(req, res);
//				 return;
//			 }
//			 if(path.endsWith("register_zh.html")){
//				 chain.doFilter(req, res);
//				 return;
//			 }
//			 if(path.endsWith("queryMailsByUserId.do")){
//				 chain.doFilter(req, res);
//				 return;
//			 }
//			 if(path.endsWith("viewQuotationById.do")){
//				 chain.doFilter(req, res);
//				 return;
//			 }
//			 if(path.endsWith("progressStatus.do")){
//				 chain.doFilter(req, res);
//				 return;
//			 }
//			 //放过/account/*.do
//			 if(path.indexOf("/account/")>0 || path.indexOf("/port/")>0){
//				 chain.doFilter(req, res);
//				 return;
//			 }
//
//			 processAccessControl(request,response,chain);
//			 return;
//		 }
         //如果是中文站，cookie存储中文
		 if(serverName.contains(SERVER_NAME_ZH)){
			 Cookie cookie = new Cookie("language", "zh");
			 cookie.setPath("/");
			 cookie.setMaxAge(60*60*24*365);
			 response.addCookie(cookie);
		 }
		 if(serverName.contains(SERVER_NAME_EN)){
			 Cookie cookie = new Cookie("language", "en");
			 cookie.setPath("/");
			 cookie.setMaxAge(60*60*24*365);
			 response.addCookie(cookie);
		 }

		 if(path.indexOf("/css/")>0 || path.indexOf("/backstage/")>0 || path.indexOf("/font/")>0
				 || path.indexOf("/images/")>0 || path.indexOf("/img/")>0 || path.indexOf("/js/")>0 || path.indexOf("/lib/")>0){
			 chain.doFilter(req, res);
			 return;
		 }
		if(GetServerPathUtil.getServerRealPathZh().equals(path)){
			request.getRequestDispatcher("/inquiry/index").forward(request, response);
		}else if(GetServerPathUtil.getServerRealPathEn().equals(path) || GetServerPathUtil.getServerRealPathEn2().equals(path)){
			request.getRequestDispatcher("/en/index.html").forward(request, response);
		}else{
			chain.doFilter(req, res);
		}

	}



	/**
	 * 处理访问控制
	 * @throws IOException 
	 * @throws ServletException 
	 * 
	 */
	private void processAccessControl(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		
	
		
		//检查Cookie中是否有Token 没有就去登录
		Cookie[] cookies = request.getCookies();
		Cookie token = null;
		if(cookies != null){
		for(Cookie cookie :cookies){
			if(cookie.getName().equals("token")){
				token = cookie;
				break;
			}			
		}
	  }
		 StringBuffer url = request.getRequestURL();
		 String urlPath = url.toString();
		 HttpSession session = request.getSession();
		 session.setAttribute("viewURL", urlPath);	
		
		
		if(token == null){
			//如果没有找到，就重定向到登录界面
			String path = request.getContextPath();
			String login = path+"/login.html";
			response.sendRedirect(login);
			return;
		}
		//处理token是否合格
		String value = token.getValue();
		String[] data = value.split("\\|");
		String time = data[0];
		String md5 = data[1];
		if(md5.equals(Md5Util.encoder(time))){
			chain.doFilter(request, response);
			return;
		}
		//如果token验证不合理，去登录页面
		String path = request.getContextPath();
		String login = path+"/login.html";
		response.sendRedirect(login);
		return;
		
	}

	public void init(FilterConfig arg0) throws ServletException {
	
		
	}
   
}
