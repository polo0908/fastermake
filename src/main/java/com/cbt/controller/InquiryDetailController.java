package com.cbt.controller;

import com.cbt.entity.*;
import com.cbt.enums.*;
import com.cbt.rpc.RpcSynNews;
import com.cbt.service.*;
import com.cbt.translate.TransApi;
import com.cbt.util.*;
import com.cbt.util.condition.ConditionScreening;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;


@RequestMapping("/rfq")
@Controller
public class InquiryDetailController {

	  @Autowired
	  private QuoteInfoService quoteInfoService;

	  @Autowired
	  private QuoteProductService quoteProductService;


	private Logger logger = LoggerFactory.getLogger(this.getClass());


	/**
	 * 跳转询盘详情页
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/{id}")
	public String  getDetail(@PathVariable Integer id, Model model,HttpServletRequest request) {

		//获取当前语言
		String lan = WebCookie.getLanguage(request);
		model.addAttribute("orderId",id);
		QuoteInfo quoteInfo = null;
		if("en".equals(lan)){
			quoteInfo = quoteInfoService.queryByOrderIdEn(id);
		}else{
			quoteInfo = quoteInfoService.queryByOrderId(id);
			quoteInfo.setCountry(CountryEnum.getEnum(quoteInfo.getCountry())!=null?CountryEnum.getEnum(quoteInfo.getCountry()).getZhName():"其他国家");
		}
		//查看推荐询盘
		List<String> processList = ProcessZhAndEnEnum.getProcessZhAndEn(quoteInfo.getMainProcess());
		List<QuoteProduct> quoteProducts = new ArrayList<QuoteProduct>();
		if(processList.size() == 0){
		    if("en".equals(lan)){
				quoteProducts = quoteProductService.queryProductGroupByOrderIdEn(0,4,OrderStatusEnum.NORMAL_ORDER.getCode(),null,null,null,null,null);
			}else{
				quoteProducts = quoteProductService.queryProductGroupByOrderId(0,4,OrderStatusEnum.NORMAL_ORDER.getCode(),null,null,null,null,null);
			}
		}else{
			if("en".equals(lan)){
				quoteProducts = quoteProductService.queryByProcessSearchEn(0, 4, processList);
			}else{
				quoteProducts = quoteProductService.queryByProcessSearch(0, 4, processList);
			}
		}


		//设置descrpition内容
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append("工艺："+quoteInfo.getMainProcess()==null?"":quoteInfo.getMainProcess());
		strBuffer.append(quoteInfo.getQuoteTitle()+"询盘(订单)材料：");
		for(int i=0;i<quoteProducts.size();i++){
			strBuffer.append(quoteProducts.get(i).getMaterials()+",");
			if(i == 0 && StringUtils.isNotBlank(quoteProducts.get(0).getQuantityList())){
				strBuffer.append(" 数量："+quoteProducts.get(0).getQuantityList());
			}
		}

		if(StringUtils.isNotBlank(quoteInfo.getEquipmentKeywords())){
			strBuffer.append("设备要求:"+quoteInfo.getEquipmentKeywords());
		}
		strBuffer.append("采购商来自："+ (quoteInfo.getCountry()==null?"Other":quoteInfo.getCountry()));
		strBuffer.append(quoteInfo.getQuoteDetail() == null ? "" : quoteInfo.getQuoteDetail());


		model.addAttribute("products",quoteProducts);
		model.addAttribute("quoteInfo",quoteInfo);
		model.addAttribute("descrpition",strBuffer);

		if(quoteInfo != null && StringUtils.isNotBlank(quoteInfo.getCsgOrderId())){
			if("en".equals(lan)){
				return "../en/detail.html";
			}
			return "detail.html";
		}else{
			if("en".equals(lan)){
				return "../en/detail_other.html";
			}
			return "detail_other.html";
		}

	}




}
