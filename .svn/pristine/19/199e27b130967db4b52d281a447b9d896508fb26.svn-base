package com.cbt.controller;


import com.cbt.entity.FactoryInfo;
import com.cbt.entity.Qualification;
import com.cbt.enums.ProcessZhAndEnEnum;
import com.cbt.service.FactoryInfoService;
import com.cbt.service.QualificationService;
import com.cbt.util.WebCookie;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequestMapping("/manufacturer-category")
@Controller
public class FactoryCommentController {


	  
	  private Logger logger = LoggerFactory.getLogger(this.getClass());
	  @Autowired
	  private FactoryInfoService factoryInfoService;
	  @Autowired
	  private QualificationService qualificationService;


	/**
	 * 根据工艺List查询
	 * @param category
	 * @param model
	 * @return
	 */
	@RequestMapping("/{category}-{page}")
	public String  getByProcessList(@PathVariable String category, @PathVariable Integer page,
									@RequestParam(defaultValue = "5") Integer pageSize,Model model, HttpServletRequest request) {

		//获取当前语言
		String lan = WebCookie.getLanguage(request);
		int start = 0;
		try {
			if(StringUtils.isNotBlank(category)){
				start = pageSize * (page-1);
                String processList = ProcessZhAndEnEnum.getProcessZh(category);
				List<String> processGroup = ProcessZhAndEnEnum.getProcessList(category);
				//获取到的搜索集合
                String[] materialsGroup = request.getParameterValues("materialsGroup");
                //String[] processGroup = request.getParameterValues("processGroup");
                String[] regionGroup = request.getParameterValues("regionGroup");
                String[] qualificationGroup = request.getParameterValues("qualificationGroup");
                String[] staffNumberGroup = request.getParameterValues("staffNumberGroup");
                String[] factoryAcreageGroup = request.getParameterValues("factoryAcreageGroup");

                Map map1 = new HashMap();
                map1.put("materialsGroup", materialsGroup);
                map1.put("processGroup", processGroup);
                map1.put("stateGroup", regionGroup);

                map1.put("qualificationGroup", qualificationGroup);
                map1.put("staffNumberGroup", staffNumberGroup);
                map1.put("factoryAcreageGroup", factoryAcreageGroup);

                map1.put("start", start);
                map1.put("pageSize", pageSize);

                List<FactoryInfo> factoryList = null;
				if("en".equals(lan)){
					factoryList = factoryInfoService.selectByConditionEn(map1);
				}else{
					factoryList = factoryInfoService.selectByCondition(map1);
				}

                //获取资格认证列表
                for(FactoryInfo factoryInfo:factoryList){
					StringBuffer qualificationNames = new StringBuffer();
					List<Qualification> qualifications = qualificationService.queryByFactory(factoryInfo.getFactoryId());
					for(Qualification qualification:qualifications){
						qualificationNames.append(qualification.getQualificationName()+"、");
					}
					if(qualificationNames.length() > 0){
						qualificationNames.deleteCharAt(qualificationNames.length()-1);
						factoryInfo.setQualificationNames(qualificationNames.toString());
					}
				}
				//工厂总数
                int count = factoryInfoService.totalOrder(map1);

				int totalPage = new BigDecimal(count).divide(new BigDecimal(pageSize),0,BigDecimal.ROUND_UP).intValue();
                //获得总页数
				model.addAttribute("totalPages", totalPage);
				//是否是第一页
				model.addAttribute("isFirstPage", page == 1);
				//是否是最后一页
				model.addAttribute("isLastPage", (totalPage == page || totalPage == 0));
				model.addAttribute("factoryList",factoryList);
				model.addAttribute("count",count);

                model.addAttribute("category",category);
                model.addAttribute("page",page);
                model.addAttribute("processList",processList);
                model.addAttribute("categoryName",ProcessZhAndEnEnum.getType(category));
                model.addAttribute("processZh",ProcessZhAndEnEnum.getProcessListZh(category));
                model.addAttribute("processEn",ProcessZhAndEnEnum.getProcessListEn(category));
            }
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("未获取到工艺类型",e.getMessage());
		}

		if("en".equals(lan)){
			return "../en/comments_echart_process.html";
		}else{
			return "comments_echart_process.html";
		}
	}

}
