package com.cbt.controller;

import com.cbt.entity.FactoryInfo;
import com.cbt.entity.FactoryUser;
import com.cbt.entity.Qualification;
import com.cbt.entity.QuoteInfo;
import com.cbt.service.*;
import com.cbt.util.GetServerPathUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 内部报价点击跳转报价页面
 * @author polo
 *
 */
@Controller
@RequestMapping("/view")
public class ViewDataPort {
	
       @Autowired
	   private QuoteProductService quoteProductService;
       @Autowired
       private QuoteInfoService quoteInfoService;
       @Autowired
       private FactoryInfoService factoryInfoService;
       @Autowired
       private FactoryUserService factoryUserService;
 	   @Autowired
 	   private QualificationService qualificationService;  	   

       
       
     	private Logger logger = LoggerFactory.getLogger(this.getClass());
	  
		  /**
		   * 跳转到工厂报价
		   * @Title updateProjectStatusBatch 
		   * @Description 
		   * @param map
		   * @return
		   * @return int
		 * @throws IOException 
		   */
		  @RequestMapping(value ="/viewQuote.do",method = RequestMethod.GET)	  
		   public void viewQuote(HttpServletRequest request,HttpServletResponse response) throws IOException{
		 		  
	        	  try {
					  //获取项目号和项目助理
					  String factoryId = request.getParameter("factoryId");
					  String projectId = request.getParameter("projectId");
					  if(StringUtils.isNotBlank(factoryId) || StringUtils.isNotBlank(projectId)){
						  FactoryUser factoryUser = factoryUserService.selectByFactoryId(factoryId);
						  FactoryInfo factoryInfo = factoryInfoService.selectFactoryInfo(factoryId);
						  QuoteInfo quoteInfo = quoteInfoService.queryByCgsOrderId(projectId);
					      	  
			    		  //获取资格认证
			    	      List<Qualification> qualification = qualificationService.queryByFactory(factoryId);
			              //保存cookie
			    		  SetCookies.setCookie(request, response, factoryInfo, factoryUser, qualification);
			    		  
			    		  response.sendRedirect(GetServerPathUtil.getServerPath()+"/zh/offer_7.html?orderId="+quoteInfo.getOrderId());
					  }else{
						  response.sendRedirect(GetServerPathUtil.getServerPath()+"/zh/login.html");
					  }					 				  	  
                        
				} catch (Exception e) {
					logger.info("============跳转到工厂报价===viewQuote======",e);
					response.sendRedirect(GetServerPathUtil.getServerPath()+"/zh/login.html");
				}
						  
		  }
		   
}
