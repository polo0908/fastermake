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
import org.springframework.web.bind.annotation.RequestParam;
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
public class ProcessController {
	  @Autowired
	  private FactoryInfoService factoryInfoService;  
	  @Autowired
	  private QualificationService qualificationService;  
	  @Autowired
	  private QuoteProductService quoteProductService;  

	  
	  
	  
	  private Logger logger = LoggerFactory.getLogger(this.getClass());
	  
	  
	  


	/**
	 * 根据工艺List查询
	 * @param process
	 * @param model
	 * @return
	 */
	@RequestMapping("/{process}/{page}")
	public String  getByProcessList(@PathVariable String process, Model model, @PathVariable Integer page,
									@RequestParam(defaultValue = "20") Integer pageSize,HttpServletRequest request) {

		//获取当前语言
		String lan = WebCookie.getLanguage(request);
		int start = 0;
		if(StringUtils.isNotBlank(process)){
			start = pageSize * (page-1);
			List<String> processList = ProcessZhAndEnEnum.getProcessList(process);
			List<QuoteProduct> products = null;
			if("en".equals(lan)){
				products = quoteProductService.queryByProcessSearchEn(start,pageSize,processList);
			}else{
				products = quoteProductService.queryByProcessSearch(start,pageSize,processList);
			}

			int count = quoteProductService.processSearchCount(start,pageSize,processList);
			for(QuoteProduct product : products){
				String quantityList = product.getQuantityList();
				String quanlityUnit = product.getQuantityUnit();
				String[] split = quantityList.split(",");
				String[] split2 = quanlityUnit.split(",");
				if(split.length > 0){
					quantityList = split[0] + split2[0];
					product.setQuantityList(quantityList);
				}
			}


			int totalPage = new BigDecimal(count).divide(new BigDecimal(pageSize),0,BigDecimal.ROUND_UP).intValue();

			//获得总页数
			model.addAttribute("totalPages", totalPage);
			//是否是第一页
			model.addAttribute("isFirstPage", page == 1);
			//是否是最后一页
			model.addAttribute("isLastPage", (totalPage == page || totalPage == 0));
			model.addAttribute("products",products);
			model.addAttribute("count",count);
			model.addAttribute("page",page);
			model.addAttribute("process",process);
			model.addAttribute("category",ProcessZhAndEnEnum.getType(process));
		}

        //根据语言跳转不同页面
		if("en".equals(lan)){
			model.addAttribute("processList",ProcessZhAndEnEnum.getProcessListEn(process));
			return "../en/inquiry_list.html";
		}else{
			model.addAttribute("processList",ProcessZhAndEnEnum.getProcessListZh(process));
			return "inquiry_list.html";
		}

	}



	/**
	 * 根据工艺查询
	 * @param
	 * @param
	 * @return
	 */
	@RequestMapping("/{process}/{type}/{page}")
	public String  getByProcess(@PathVariable String process,@PathVariable String type,
								@PathVariable Integer page,Model model, @RequestParam(defaultValue = "20") Integer pageSize,HttpServletRequest request) {

		//获取当前语言
		String lan = WebCookie.getLanguage(request);
		int start = 0;
		if(StringUtils.isNotBlank(process) && type != null){
			start = pageSize * (page-1);
			List<String> processList = ProcessEnum.getProcess(type);
			List<QuoteProduct> products = null;
			if("en".equals(lan)){
				products = quoteProductService.queryByProcessSearchEn(start,pageSize,processList);
			}else{
				products = quoteProductService.queryByProcessSearch(start,pageSize,processList);
			}


			int count = quoteProductService.processSearchCount(start,pageSize,processList);
			for(QuoteProduct product : products){
				String quantityList = product.getQuantityList();
				String quanlityUnit = product.getQuantityUnit();
				String[] split1 = quantityList.split(",");
				String[] split2 = quanlityUnit.split(",");
				if(split1.length > 0){
					quantityList = split1[0] + split2[0];
					product.setQuantityList(quantityList);
				}
			}


			int totalPage = new BigDecimal(count).divide(new BigDecimal(pageSize),0,BigDecimal.ROUND_UP).intValue();

			//获得总页数
			model.addAttribute("totalPages", totalPage);
			//是否是第一页
			model.addAttribute("isFirstPage", page == 1);
			//是否是最后一页
			model.addAttribute("isLastPage", (totalPage == page || totalPage == 0));
			model.addAttribute("products",products);
			model.addAttribute("count",count);
			model.addAttribute("page",page);
			model.addAttribute("process",process);
			model.addAttribute("type",type);
			model.addAttribute("category",ProcessZhAndEnEnum.getType(process));
			model.addAttribute("processName",ProcessEnum.getStr(type));
		}


		//根据语言跳转不同页面
		if("en".equals(lan)){
			return "../en/inquiry_list_2_1.html";
		}else{
			return "inquiry_list_2_1.html";
		}

	}

}
