package com.cbt.controller;

import com.cbt.entity.FactoryInfo;
import com.cbt.entity.FactoryUser;
import com.cbt.entity.Qualification;
import com.cbt.enums.StateEnum;
import com.cbt.model.AccessToken;
import com.cbt.model.SingleAccessToken;
import com.cbt.model.SingleAccessTokenWeb;
import com.cbt.service.FactoryInfoService;
import com.cbt.service.FactoryUserService;
import com.cbt.service.QualificationService;
import com.cbt.sina.model.User;
import com.cbt.util.*;
import com.cbt.wximpl.Wechatimpl;
import com.mysql.jdbc.util.Base64Decoder;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RequestMapping("/wechat")
@Controller
public class WechatLogin {
	  @Autowired
	  private FactoryInfoService factoryInfoService;    
	  @Autowired
	  private FactoryUserService factoryUserService;  
	  @Autowired
	  private QualificationService qualificationService;  
	  
	  //获取域名
	  private static final String SERVER_PATH = GetServerPathUtil.getServerPath();
	  
	  private static String appid = WriteProp.get("appid");
	  private static String secret = WriteProp.get("secret");
	  
	  private static String appid_web = WriteProp.get("appid_web");
	  private static String secret_web = WriteProp.get("secret_web");
	  
	  
	  private Logger logger = LoggerFactory.getLogger(this.getClass());
	    /**
		 * 用户授权，获取用户的公开信息，保存至session
		 * 
		 * @param request
		 * @param response
		 * @return
		 */
		@RequestMapping("authorization")
		public String authorization(HttpServletRequest request, HttpServletResponse response) {

			/* ===============第一步：根据授权确认操作得到CODE==================== */
			String code = request.getParameter("code");
			if (null == code || "".equals(code)) {
				logger.error("authorization : The user authorization failure!");
			}
            	
			
			
			//根据code获取用户openid保存session
		   if(StringUtils.isNotBlank(code)){
			    logger.error("获取code:"+code);
				JSONObject json = SendHttpsRequestUtil.sendGetRequest(WriteProp.get("pageAuthorization_access_token_url")
						.replace("APPID", appid).replace("SECRET", secret).replace("CODE", code));
				String openid = json.getString("openid");
				if(StringUtils.isNotBlank(openid)){
					 //客户登录信息存放到session
		    		  HttpSession session_openid = request.getSession();
		    		  session_openid.setAttribute("openid",openid);
		    		  session_openid.setMaxInactiveInterval(60*60*24*365);
					  logger.info("获取到的客户openid"+openid);
		    		  //获取access_token
					 String access_token = SingleAccessToken.getInstance().getAccessToken().getToken();
					 //获取用户信息
					 JSONObject jsonUser = SendHttpsRequestUtil.sendGetRequest(WriteProp.get("userinfo").replace("ACCESS_TOKEN", access_token).replace("OPENID", openid));
					 String unionid = jsonUser.getString("unionid");
					 if(StringUtils.isNotBlank(unionid)){
						 HttpSession session_unionid = request.getSession();
						 session_unionid.setAttribute("unionid",unionid);
						 session_unionid.setMaxInactiveInterval(60*60*24*365);
						 logger.info("获取到的客户unionid"+unionid);
					 }
				}
		    }
		   
			//获取跳转的页面
			if(StringUtils.isNotBlank(request.getParameter("go"))){
				String go = request.getParameter("go");
				switch (go) {
					case "1":
						return "redirect:"+SERVER_PATH+"/m-zh/login.html";
					case "2":
						return "redirect:"+SERVER_PATH+"/m-zh/register.html";
					case "3":
						return "redirect:"+SERVER_PATH+"/m-zh/quote_list.html";
					case "4":
						return "redirect:"+SERVER_PATH+"/m-zh/quote_list.html?quoteStatus=1";
					case "5":
						return "redirect:"+SERVER_PATH+"/m-zh/quote_list.html?quoteStatus=100";
					case "6":
						return "redirect:"+SERVER_PATH+"/m-zh/quote_list.html?quoteStatus=102";
					case "7":
						return "redirect:"+SERVER_PATH+"/m-zh/quote_list.html?quoteStatus=2";
					case "8":
						return "redirect:"+SERVER_PATH+"/m-zh/quote_list.html?quoteStatus=7";
					default:
						return "redirect:"+SERVER_PATH+"/m-zh/login.html";
				}
			}else{
				return "redirect:"+SERVER_PATH+"/m-zh/login.html";
			}
		}
		
		
		
		
		
		
		
		/**
		 * PC端通过微信登录访问
		 * @Title callBack 
		 * @Description
		 * @param request
		 * @param response
		 * @return
		 * @return String
		 */
		@RequestMapping("callback")
		public String callBack(HttpServletRequest request, HttpServletResponse response) {

			try {
				/* ===============第一步：根据授权确认操作得到CODE==================== */
				String code = request.getParameter("code");
				if (null == code || "".equals(code)) {
					logger.error("authorization : The user authorization failure!");
				}
							
				//根据code获取用户openid保存session
                if(StringUtils.isNotBlank(code)){
				    logger.error("获取code:"+code);
					JSONObject json = SendHttpsRequestUtil.sendGetRequest(WriteProp.get("pageAuthorization_access_token_url")
							.replace("APPID", appid_web).replace("SECRET", secret_web).replace("CODE", code));
					String openid = json.getString("openid");
					logger.error("获取json:"+json);
					//获取web端access_token
					String access_token = json.getString("access_token");
					logger.info("获取access_token信息"+access_token);
					if(StringUtils.isNotBlank(openid)){
						 //客户登录信息存放到session
						  HttpSession session_openid = request.getSession();
						  session_openid.setAttribute("openid",openid);
						  session_openid.setMaxInactiveInterval(60*60*24*365);
			        	//获取用户信息
						JSONObject jsonUser = SendHttpsRequestUtil.sendGetRequest(WriteProp.get("user_info_url").replace("ACCESS_TOKEN", access_token).replace("OPENID", openid));
						logger.info("获取jsonUser用户信息"+jsonUser);
						String unionid = jsonUser.getString("unionid");
						logger.info("获取unionid信息"+unionid);
						FactoryUser factoryUser = null;
						if(StringUtils.isNotBlank(unionid)){
							factoryUser = factoryUserService.selectByUnionid(unionid);
							HttpSession session_unionid = request.getSession();
							session_unionid.setAttribute("unionid",unionid);
							session_unionid.setMaxInactiveInterval(60*60*24*365);
							logger.info("获取到的客户unionid"+unionid);
						}
					    if(factoryUser == null){
						  return "redirect:"+SERVER_PATH+"/zh/jump.html?unionid="+unionid;
					    }else{
						FactoryInfo factoryInfo = factoryInfoService.selectFactoryInfo(factoryUser.getFactoryId());
						//获取工厂资格认证
						List<Qualification> qualification = qualificationService.queryByFactory(factoryUser.getFactoryId());
						//保存cookie
						SetCookies.setCookie(request, response, factoryInfo, factoryUser, qualification);

						 //历史打开链接（发送消息、报价）
						 String histroyUrl = WebCookie.getHistroyUrl(request);
						 if(StringUtils.isNotBlank(histroyUrl)){
							 return "redirect:"+histroyUrl;
						 }

						 //报价详情页
						 String quoteDetailUrl = WebCookie.cookie(request, "quoteDetailUrl");
						   if(StringUtils.isNotBlank(quoteDetailUrl)){
							  return "redirect:"+quoteDetailUrl;
						 }
						 String language = WebCookie.getLanguage(request);
						 if("en".equals(language)){
							 return "redirect:"+SERVER_PATH+"/en/index.html";
						 }else{
							 return "redirect:"+SERVER_PATH+"/zh/business_view.html";
						 }
					   }
					}
				}
				return "redirect:"+SERVER_PATH+"/zh/login.html";
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return "redirect:"+SERVER_PATH+"/zh/login.html";
			}
		}



	/**
	 * 第三方代公众号
	 * @Title callBack
	 * @Description
	 * @param request
	 * @param response
	 * @return
	 * @return String
	 */
	@RequestMapping("otherLogin")
	public String otherLogin(HttpServletRequest request, HttpServletResponse response) {

		try {
				/* ===============第一步：根据授权确认操作得到CODE==================== */
			String code = request.getParameter("code");
			if (null == code || "".equals(code)) {
				logger.error("authorization : The user authorization failure!");
			}

			//根据code获取用户openid保存session
			if(StringUtils.isNotBlank(code)){
				logger.error("获取code:"+code);
				JSONObject json = SendHttpsRequestUtil.sendGetRequest(WriteProp.get("pageAuthorization_access_token_url")
						.replace("APPID", appid_web).replace("SECRET", secret_web).replace("CODE", code));
				String openid = json.getString("openid");
				logger.error("获取json:"+json);
				//获取web端access_token
				String access_token = json.getString("access_token");
				logger.info("获取access_token信息"+access_token);
				if(StringUtils.isNotBlank(openid)){
					//客户登录信息存放到session
					HttpSession session_openid = request.getSession();
					session_openid.setAttribute("openid",openid);
					session_openid.setMaxInactiveInterval(60*60*24*365);
					//获取用户信息
					JSONObject jsonUser = SendHttpsRequestUtil.sendGetRequest(WriteProp.get("user_info_url").replace("ACCESS_TOKEN", access_token).replace("OPENID", openid));
					logger.info("获取jsonUser用户信息"+jsonUser);
					String unionid = jsonUser.getString("unionid");
					logger.info("获取unionid信息"+unionid);
					FactoryUser factoryUser = null;
					if(StringUtils.isNotBlank(unionid)){
						factoryUser = factoryUserService.selectByUnionid(unionid);
						HttpSession session_unionid = request.getSession();
						session_unionid.setAttribute("unionid",unionid);
						session_unionid.setMaxInactiveInterval(60*60*24*365);
						logger.info("获取到的客户unionid"+unionid);
					}
					if(factoryUser == null){
						return "redirect:"+SERVER_PATH+"/zh/jump.html?unionid="+unionid;
					}else{
						FactoryInfo factoryInfo = factoryInfoService.selectFactoryInfo(factoryUser.getFactoryId());
						//获取工厂资格认证
						List<Qualification> qualification = qualificationService.queryByFactory(factoryUser.getFactoryId());
						//保存cookie
						SetCookies.setCookie(request, response, factoryInfo, factoryUser, qualification);

						//历史打开链接（发送消息、报价）
						String histroyUrl = WebCookie.getHistroyUrl(request);
						if(StringUtils.isNotBlank(histroyUrl)){
							return "redirect:"+histroyUrl;
						}

						//报价详情页
						String quoteDetailUrl = WebCookie.cookie(request, "quoteDetailUrl");
						if(StringUtils.isNotBlank(quoteDetailUrl)){
							return "redirect:"+quoteDetailUrl;
						}
						String language = WebCookie.getLanguage(request);
						if("en".equals(language)){
							return "redirect:"+SERVER_PATH+"/en/index.html";
						}else{
							return "redirect:"+SERVER_PATH+"/zh/business_view.html";
						}
					}
				}
			}
			return "redirect:"+SERVER_PATH+"/zh/login.html";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "redirect:"+SERVER_PATH+"/zh/login.html";
		}
	}


		/**
		 * 查询微信用户轮询绑定
		 * 判断unionid是否相等
		 * @param request
		 * @param response
		 */
		@RequestMapping("bindWechatUser")
		public void bindWechatUser(HttpServletRequest request, HttpServletResponse response) {
			//获取access_token
			try {
				String access_token = SingleAccessToken.getInstance().getAccessToken().getToken();
				JSONObject json = SendHttpsRequestUtil.sendGetRequest(WriteProp.get("all_user_url").replace("ACCESS_TOKEN", access_token));
				logger.info("获取到的数组"+json.toString());
				String userJson = json.getString("user_info_list");
				JSONArray jsonarray = JSONArray.fromObject(userJson);
				if(jsonarray.size()>0) {
                    for(int i=0;i<jsonarray.size();i++){
                        JSONObject job = jsonarray.getJSONObject(i);
                        String openid = job.get("openid").toString();
                        String unionid = job.get("unionid").toString();
                        FactoryUser factoryUser = factoryUserService.selectByOpenId(openid);
                        if (factoryUser != null) {
                            factoryUser.setUnionid(unionid);
                            factoryUserService.updateByPrimaryKeySelective(factoryUser);
                        }
                    }
                }
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("《《《《bindWechatUser》》》》",e);
			}
		}


		/**
		 * 
		 * @Title confirmMail 
		 * @Description
		 * @param request
		 * @param response
		 * @return
		 * @return String
		 */
		@RequestMapping("sendMail")
		public void sendMail(HttpServletRequest request, HttpServletResponse response) {
			
			  try {
				  String email = request.getParameter("email");
				  String encrypt = request.getParameter("encrypt");
				  //保存cookie tokens
				  String now = String.valueOf(System.currentTimeMillis());
				  String tokens = now + "@" + Md5Util.encoder(now);

				  String result1 = "";
				  PrintWriter out = null;
				  BufferedReader in = null;
				  //如果选择的是英语，发送为英文页面
				  String getPwdUrl  = GetServerPathUtil.getServerPath()+"/wechat/confirmMail.do?tokens="+tokens+"&encrypt="+encrypt;
				  getPwdUrl = URLEncoder.encode(getPwdUrl, "utf-8");
				  try {

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
				      out.print("&email="+email+"&&title="+URLEncoder.encode("确认绑定邮箱到快制造", "utf-8")+"&content="+getPwdUrl);
				      // flush输出流的缓冲
				      out.flush();
				      // 定义BufferedReader输入流来读取URL的响应
				      in = new BufferedReader(
				              new InputStreamReader(conn.getInputStream()));
				      String line;
				      while ((line = in.readLine()) != null) {
				          result1 += line;
				      }
				  } catch (Exception e) {
					  logger.error("=========sendMail=========",e);
				  }
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
			
		
		
		
		@RequestMapping("confirmMail")
		public String confirmMail(HttpServletRequest request, HttpServletResponse response) {
			try {
				String tokens = request.getParameter("tokens");
				String encrypt = request.getParameter("encrypt");
				FactoryUser factoryUser = factoryUserService.saveFactoryUserByWechat(tokens, encrypt);
							
				  FactoryInfo factoryInfo = factoryInfoService.selectFactoryInfo(factoryUser.getFactoryId());
				  //保存cookie token
	    		  String now = String.valueOf(System.currentTimeMillis());
	    		  String token = Md5Util.encoder(now);
	    		  Cookie cookie = new Cookie("token",now+"|"+token);
	    		  cookie.setPath("/");
	    		  cookie.setMaxAge(60*60*24*365);
	    		  response.addCookie(cookie);
	    		  
	              String factoryId = factoryInfo.getFactoryId(); 		  
	    		  String pwd = factoryUser.getPwd();
	    		  String loginEmail = factoryUser.getEmail();
	    		  Integer state = null;
	    		  String province = "";
	    		  province = factoryInfo.getState();
	    		  if(StringUtils.isNotBlank(factoryInfo.getState())){
	    			  state =  StateEnum.getCode(factoryInfo.getState()).getCode();	 
	        		  Cookie pro2 = new Cookie("FSM_S",state.toString());           
	        		  pro2.setPath("/");
	        		  pro2.setMaxAge(60*60*24*365);
	        		  response.addCookie(pro2); 		 
	    		  }  		    
	    		  String userName = factoryUser.getUsername();
	  			  //存放登录数据到Cookie   
	    		  //添加客户类型（0：供应商加采购商  1：供应商  2：采购商）    2017/11/22   polo
	    		  String str1 = 1 +"&"+ 1 +"&"+pwd+"&"+factoryUser.getPermission()+"&"+factoryUser.getId();
	    		  String str2 = factoryInfo.getIsVip() +"&"+province + "&"+ factoryInfo.getFactoryType();  		 
	    		  
	    		  
	    		  Cookie userCookie = new Cookie("factoryInfo",Base64Encode.getBase64(str1));      
	    		  userCookie.setPath("/");
	    		  userCookie.setMaxAge(60*60*24*365);
	    		  response.addCookie(userCookie);  
	    		  
	    		  //存放客户id
	    		  Cookie userCookie3 = new Cookie("F_ID",Base64Encode.getBase64(factoryId));      
	    		  userCookie3.setPath("/");
	    		  userCookie3.setMaxAge(60*60*24*365);
	    		  response.addCookie(userCookie3);    	
	    		  //存放客户Email
	    		  Cookie userCookie4 = new Cookie("F_M",Base64Encode.getBase64(loginEmail));      
	    		  userCookie4.setPath("/");
	    		  userCookie4.setMaxAge(60*60*24*365);
	    		  response.addCookie(userCookie4);    	
	    		  
	    		  Cookie userCookie2 = new Cookie("F_INFO",Base64Encode.getBase64(str2));      
	    		  userCookie2.setPath("/");
	    		  userCookie2.setMaxAge(60*60*24*365);
	    		  response.addCookie(userCookie2);    
	    		  
	    		  
	    		  //客户名
	    		  if(StringUtils.isNotBlank(userName)){
		    		  Cookie userCookie1 = new Cookie("userName",Base64Encode.getBase64(userName));      
		    		  userCookie1.setPath("/");
		    		  userCookie1.setMaxAge(60*60*24*365);
		    		  response.addCookie(userCookie1);
	    		  }
	    		  
	    		  //客户登录信息存放到session
	    		  HttpSession session = request.getSession();
	    		  session.setAttribute("factoryInfo",Base64Encode.getBase64(str1));
	    		  session.setMaxInactiveInterval(60*60*24*365);
	    		  HttpSession session2 = request.getSession();
	    		  session2.setAttribute("F_INFO",Base64Encode.getBase64(str2));
	    		  session2.setMaxInactiveInterval(60*60*24*365);
	    		  HttpSession session3 = request.getSession();
	    		  session3.setAttribute("F_ID",Base64Encode.getBase64(factoryId));
	    		  session3.setMaxInactiveInterval(60*60*24*365);
	    		  HttpSession session4 = request.getSession();
	    		  session4.setAttribute("F_M",Base64Encode.getBase64(loginEmail));
	    		  session4.setMaxInactiveInterval(60*60*24*365);
	    		  
	    		  
	    		  
	    		  //客户公司名存放到session
	    		  session.setAttribute("factoryName",factoryInfo.getFactoryName());
	    		  session.setMaxInactiveInterval(60*60*24*365);
	    		  
	    		  
	    		  //客户名
//	    		  Cookie userCookie1 = new Cookie("userName",Base64Encode.getBase64(userName));      
//	    		  userCookie1.setPath("/");
//	    		  userCookie1.setMaxAge(60*60*24*365);
//	    		  response.addCookie(userCookie1);
			  
	    		  
	    		  AccountController.setProcess(factoryInfo.getMainProcess());  		 
	    		  AccountController.setState(state);   
	             
	    		  
	              StringBuffer qs = new StringBuffer();
	              String q = null;
	              List<Qualification> qualification = qualificationService.queryByFactory(factoryId);
	              if(qualification != null && qualification.size() != 0){
	            	  for (Qualification qualification2 : qualification) {
	            		  qs.append(qualification2.getQualificationName()+",");
					  }
	    			  if(qs.length()>1){
	    			  q = qs.substring(0, qs.length() -1);
	    			  }
	    			  
	    			  Cookie pro3 = new Cookie("FSM_Q",URLEncoder.encode(q , "UTF-8"));           
	        		  pro3.setPath("/");
	        		  pro3.setMaxAge(60*60*24*365);
	        		  response.addCookie(pro3);
	              }
	              
	             if(StringUtils.isNotBlank(factoryInfo.getMainProcess())){
	            	  //存放process、地区 、资格认证
		       		  Cookie pro1 = new Cookie("FSM_P",URLEncoder.encode(factoryInfo.getMainProcess(), "UTF-8"));           
		       		  pro1.setPath("/");
		       		  pro1.setMaxAge(60*60*24*365);
		       		  response.addCookie(pro1); 
	             }
	             
	             //历史打开链接（发送消息、报价）
	             String histroyUrl = WebCookie.getHistroyUrl(request);
	             if(StringUtils.isNotBlank(histroyUrl)){
	            	 return "redirect:"+histroyUrl;
	             }
	             
	             //报价详情页
	             String quoteDetailUrl = WebCookie.cookie(request, "quoteDetailUrl");
	    		   if(StringUtils.isNotBlank(quoteDetailUrl)){
	    			  return "redirect:"+quoteDetailUrl; 	
	    		 }  		   
	    		 String language = WebCookie.getLanguage(request);
	    		 if("en".equals(language)){
	    			 return "redirect:"+SERVER_PATH+"/en/index.html"; 
	    		 }else{
	    			 return "redirect:"+SERVER_PATH+"/zh/business_view.html"; 
	    		 }		    			  		    			  

				
			} catch (ParseException e) {
				 return "redirect:"+SERVER_PATH+"/zh/index.html"; 
			} catch (UnsupportedEncodingException e) {				
				 return "redirect:"+SERVER_PATH+"/zh/index.html"; 
			}
		}
		
		
		
		
		/**
		 * 清除用户的openid
		 * @Title clearOpenid 
		 * @Description 
		 * @param
		 * @param
		 * @return
		 * @return String
		 */
		@ResponseBody
		@RequestMapping("clearOpenid")
		public JsonResult<String> clearOpenid(String email) {
	           
			try {
				FactoryUser factoryUser = factoryUserService.selectByLoginEmail(email);
				if(factoryUser != null){
					factoryUser.setOpenid("");
					factoryUser.setUpdateTime(DateFormat.format());			
					factoryUserService.updateByPrimaryKeySelective(factoryUser);
				}
				return new JsonResult<String>(0,"更新成功");	
			} catch (Exception e) {
				e.printStackTrace();
				return new JsonResult<String>(1,"更新失败");	
			}
			
		}







	/**
	 * 用户授权，获取用户的公开信息，保存至session
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("purchaseAuthorization")
	public String purchaseAuthorization(HttpServletRequest request, HttpServletResponse response) {

			/* ===============第一步：根据授权确认操作得到CODE==================== */
		String code = request.getParameter("code");
		if (null == code || "".equals(code)) {
			logger.error("authorization : The user authorization failure!");
		}

		//质检报告id
		Integer id = null;
		if(StringUtils.isNotBlank(request.getParameter("id"))) {
			id = Integer.parseInt(request.getParameter("id"));
		}

		//根据code获取用户openid保存session
		if(StringUtils.isNotBlank(code)){
			logger.info("获取code:"+code);
			JSONObject json = SendHttpsRequestUtil.sendGetRequest(WriteProp.get("pageAuthorization_access_token_url")
					.replace("APPID", appid).replace("SECRET", secret).replace("CODE", code));
			logger.info("获取用户openid:"+json);
			String openid = json.getString("openid");
			if(StringUtils.isNotBlank(openid)){
				//客户登录信息存放到session
				HttpSession session_openid = request.getSession();
				session_openid.setAttribute("openid",openid);
				session_openid.setMaxInactiveInterval(60*60*24*365);
			}
            //获取accessToken
			String token = json.getString("access_token");
			JSONObject userInfo = SendHttpsRequestUtil.sendGetRequest(WriteProp.get("user_info_url")
					.replace("APPID", appid).replace("ACCESS_TOKEN", token));

			logger.info("获取用户信息:"+ userInfo);
			//登录保存cookie
			Cookie userCookie = new Cookie("name",userInfo.getString("nickname"));
			userCookie.setPath("/");
			userCookie.setMaxAge(60*60*24*365);
			response.addCookie(userCookie);
			//获取跳转的页面
			return "redirect:"+SERVER_PATH+"/quality/shareQuality?id="+id;
		}else{
			return "redirect:"+SERVER_PATH+"/quality/shareQuality?id="+id;
		}
	}






	/**
	 * 用户授权，获取用户的公开信息，保存openid
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("supplierAuthorization")
	public String supplierAuthorization(HttpServletRequest request, HttpServletResponse response) {

			/* ===============第一步：根据授权确认操作得到CODE==================== */
		String code = request.getParameter("code");
		if (null == code || "".equals(code)) {
			logger.error("authorization : The user authorization failure!");
		}

		//工厂id
	    String supplierId = request.getParameter("supplierId");
		//项目号
	    String csgOrderId = request.getParameter("csgOrderId");
		//如果检测到cookie工厂id不为空，则不进行更新openid
		String factoryId = WebCookie.getFactoryId(request);
		//根据code获取用户openid保存session
			if(StringUtils.isBlank(factoryId) && StringUtils.isNotBlank(code)) {
				logger.info("获取code:" + code);
				JSONObject json = SendHttpsRequestUtil.sendGetRequest(WriteProp.get("pageAuthorization_access_token_url")
						.replace("APPID", appid).replace("SECRET", secret).replace("CODE", code));
				logger.info("获取用户openid:" + json);
				String openid = json.getString("openid");
				if (StringUtils.isNotBlank(openid)) {
					//客户登录信息存放到session
					HttpSession session_openid = request.getSession();
					session_openid.setAttribute("openid", openid);
					session_openid.setMaxInactiveInterval(60 * 60 * 24 * 365);
				}
				//获取accessToken
				String token = json.getString("access_token");
				JSONObject userInfo = SendHttpsRequestUtil.sendGetRequest(WriteProp.get("user_info_url")
						.replace("APPID", appid).replace("ACCESS_TOKEN", token));
				logger.info("获取用户信息:" + userInfo);
				String supplierIdD = Base64Encode.getFromBase64(supplierId);
				FactoryInfo factoryInfo = factoryInfoService.selectFactoryInfo(supplierIdD);
				if(factoryInfo != null){
					FactoryUser factoryUser = factoryUserService.selectByLoginEmail(factoryInfo.getEmail());
					factoryUser.setOpenid(openid);
					factoryUserService.updateByPrimaryKeySelective(factoryUser);
				}

			}

			return "redirect:"+SERVER_PATH+"/report/reportList?csgOrderId="+csgOrderId+"&supplierId="+supplierId;

	}
}
