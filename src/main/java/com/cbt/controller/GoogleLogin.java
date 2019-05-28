package com.cbt.controller;

import com.cbt.entity.FactoryInfo;
import com.cbt.entity.FactoryUser;
import com.cbt.entity.Qualification;
import com.cbt.service.FactoryInfoService;
import com.cbt.service.FactoryUserService;
import com.cbt.service.QualificationService;
import com.cbt.util.GetServerPathUtil;
import com.cbt.util.WebCookie;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequestMapping("/google")
@Controller
public class GoogleLogin {
	  @Autowired
	  private FactoryInfoService factoryInfoService;    
	  @Autowired
	  private FactoryUserService factoryUserService;  
	  @Autowired
	  private QualificationService qualificationService;  
	  
	  //获取域名
	  private static final String SERVER_PATH = GetServerPathUtil.getServerPathEn();
	  
	  
	  
	  private Logger logger = LoggerFactory.getLogger(this.getClass());
	    /**
		 * 用户授权，获取用户的公开信息，保存至session
		 * 
		 * @param request
		 * @param response
		 * @return
		 */
		@RequestMapping("callback")
		public String callback(HttpServletRequest request, HttpServletResponse response) {		
			try {
				
				String email=request.getParameter("email");
				String userName = request.getParameter("name");
				String id = request.getParameter("id");
				FactoryInfo factoryInfo = new FactoryInfo();
				FactoryUser factoryUser = factoryUserService.selectByLoginEmail(email);
				if(factoryUser == null && email != null){					
					factoryInfo.setEmail(email);
					factoryInfo.setUsername(userName);
					factoryInfo.setGoogleId(id);
					factoryInfo.setFactoryType(2);
					factoryInfo.setIsVip(100);
					factoryUser = factoryInfoService.insertSelective(factoryInfo);
				}else{
					factoryInfo.setGoogleId(id);
					factoryInfoService.updateByPrimaryKeySelective(factoryInfo);
					factoryInfo = factoryInfoService.selectFactoryInfo(factoryUser.getFactoryId());
				}
				
				
				
	               String factoryId = factoryInfo.getFactoryId(); 		  
	    		  //获取资格认证
	    	      List<Qualification> qualification = qualificationService.queryByFactory(factoryId);
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
	    		 return "redirect:"+SERVER_PATH+"/en/index.html";
			} catch (Exception e) {
				e.printStackTrace();
				return "redirect:"+SERVER_PATH+"/en/login.html";
			}
		}			
	
}
