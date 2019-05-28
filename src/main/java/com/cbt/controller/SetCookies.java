package com.cbt.controller;

import com.cbt.entity.FactoryInfo;
import com.cbt.entity.FactoryUser;
import com.cbt.entity.Qualification;
import com.cbt.enums.StateEnum;
import com.cbt.util.Base64Encode;
import com.cbt.util.Md5Util;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class SetCookies {

	public static void setCookie(HttpServletRequest request,HttpServletResponse response,
			FactoryInfo factoryInfo,FactoryUser factoryUser,List<Qualification> qualification) throws UnsupportedEncodingException{
		
		
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
		  
		  
		  Cookie userCookie = new Cookie("factoryInfo",Base64Encode.getBase64(str1).replace("\"", ""));      
		  userCookie.setPath("/");
		  userCookie.setMaxAge(60*60*24*365);
		  response.addCookie(userCookie);  
		  
		  //存放客户id
		  Cookie userCookie3 = new Cookie("F_ID",Base64Encode.getBase64(factoryId).replace("\"", ""));      
		  userCookie3.setPath("/");
		  userCookie3.setMaxAge(60*60*24*365);
		  response.addCookie(userCookie3);    	
		  //存放客户Email
		  Cookie userCookie4 = new Cookie("F_M",Base64Encode.getBase64(loginEmail).replace("\"", ""));      
		  userCookie4.setPath("/");
		  userCookie4.setMaxAge(60*60*24*365);
		  response.addCookie(userCookie4);    	
		  
		  Cookie userCookie2 = new Cookie("F_INFO",Base64Encode.getBase64(str2).replace("\"", ""));      
		  userCookie2.setPath("/");
		  userCookie2.setMaxAge(60*60*24*365);
		  response.addCookie(userCookie2);    	
		  
		  //判断客户是否已经微信绑定
		  if(StringUtils.isNotBlank(factoryUser.getUnionid())){
			  Cookie userCookie5 = new Cookie("w_flag",Base64Encode.getBase64("true"));      
			  userCookie5.setPath("/");
			  userCookie5.setMaxAge(60*60*24*365);
			  response.addCookie(userCookie5);
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
		  if(StringUtils.isNotBlank(userName)){
  		  Cookie userCookie1 = new Cookie("userName",Base64Encode.getBase64(userName).replace("\"", ""));      
  		  userCookie1.setPath("/");
  		  userCookie1.setMaxAge(60*60*24*365);
  		  response.addCookie(userCookie1);
		  }

	  
		  
		  AccountController.setProcess(factoryInfo.getMainProcess());  		 
		  AccountController.setState(state);   
       
		  
        StringBuffer qs = new StringBuffer();
        String q = null;
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
	}
	
}
