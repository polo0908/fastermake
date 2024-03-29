package com.cbt.controller;

import com.cbt.entity.Equipment;
import com.cbt.entity.FactoryInfo;
import com.cbt.entity.ProductLibrary;
import com.cbt.enums.ProcessZhAndEnEnum;
import com.cbt.service.EquipmentService;
import com.cbt.service.FactoryInfoService;
import com.cbt.service.ProductLibraryService;
import com.cbt.util.WebCookie;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 根据factory_id 查询各工厂
 */
@RequestMapping("/manufacturer-category")
@Controller
public class FactoryController {

	  @Autowired
	  private FactoryInfoService factoryInfoService;
	  @Autowired
	  private EquipmentService equipmentService;
	  @Autowired
	  private ProductLibraryService productLibraryService;


	  private Logger logger = LoggerFactory.getLogger(this.getClass());
	  
	  
	  


	/**
	 * 根据工艺List查询
	 * @param factoryId
	 * @param model
	 * @return
	 */
	@RequestMapping("/{factoryId}/{type}")
	public String  getByProcessList(@PathVariable String factoryId, @PathVariable String type, Model model, HttpServletRequest request) {

		//获取当前语言
		String lan = null;
		try {
			lan = WebCookie.getLanguage(request);
			FactoryInfo factoryInfo = null;
			if("en".equals(lan)){
                factoryInfo = factoryInfoService.selectFactoryInfoEn(factoryId);
            }else{
                factoryInfo = factoryInfoService.selectFactoryInfo(factoryId);
            }
			//2.查询设备清单
			List<Equipment> equipmentList=equipmentService.selectFactoryEquipment(factoryId);
			factoryInfo.setEquipmentList(equipmentList);

			//获取工厂照片
			List<String> picList = new ArrayList<String>();
			if(StringUtils.isNotBlank(factoryInfo.getProductionEnvironment())){
                String[] split = factoryInfo.getProductionEnvironment().split(",");
                for(int i=0;i<split.length;i++){
                    picList.add(split[i]);
                }
                factoryInfo.setPicList(picList);
            }
            //获取产品


			model.addAttribute("factoryId",factoryId);
			model.addAttribute("factoryInfo",factoryInfo);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("======getByProcessList======",e.getMessage());
		}
		if("en".equals(lan)){
			if("products".equals(type)){
				return "../en/view_factory_products.html";
			}else if("photos".equals(type)){
				return "../en/view_factory_environments.html";
			}else if("comments".equals(type)){
				return "../en/view_factory_evaluations.html";
			}else if("equipments".equals(type)){
				return "../en/view_factory_equipments.html";
			}else{
				return "../en/view_factory_info.html";
			}
		}else{

			if("products".equals(type)){
				return "view_factory_products.html";
			}else if("photos".equals(type)){
				return "view_factory_environments.html";
			}else if("comments".equals(type)){
				return "view_factory_evaluations.html";
			}else if("equipments".equals(type)){
				return "view_factory_equipments.html";
			}else{
				return "view_factory_info.html";
			}
		}
	}


	@RequestMapping("/updateFactoryScore.do")
	@ResponseBody
	public String updateFactoryScore() throws Exception {
		List<FactoryInfo> factoryInfos = factoryInfoService.queryFactoryList();
		for(FactoryInfo factoryInfo:factoryInfos){
			factoryInfoService.updateByPrimaryKeySelective(factoryInfo);
		}
		return "yes";
	}




	/**
	 * 根据id查询产品
	 * @param factoryId  工厂id
	 * @param type       类型
	 * @param productNameEn   产品英文名（空格使用-分割）
	 * @param pid       产品pid
	 * @param model
	 * @return
	 */
	@RequestMapping("/{factoryId}/{type}/{productNameEn}")
	public String  getFactoryProductById(@PathVariable String factoryId, @PathVariable String productNameEn,
										  Model model, HttpServletRequest request) {

		//获取当前语言
		String lan = null;
		ProductLibrary productLibrary = new ProductLibrary();
		FactoryInfo factoryInfo = new FactoryInfo();
		FactoryInfo factoryInfoEn = new FactoryInfo();
		Map<String, Object> map = null;
		try {

			lan = WebCookie.getLanguage(request);
			String buyerId = WebCookie.getFactoryId(request);
			Integer pid = null;
			if(StringUtils.isNotBlank(productNameEn)){
				String[] split = productNameEn.split("-");
				if(split.length>1){
					pid = Integer.parseInt(split[split.length-1]);
				}
			}
			productLibrary = productLibraryService.selectByPrimaryKey(pid);

			if (productLibrary != null && productLibrary.getFactoryId() != null) {
				factoryInfo = factoryInfoService.selectFactoryInfoAndEquipment(factoryId);
				if("en".equals(lan)){
					factoryInfoEn = factoryInfoService.selectFactoryInfoEn(factoryId);
				}
			}
			model.addAttribute("product",productLibrary);
			model.addAttribute("factoryInfo",factoryInfo);
			model.addAttribute("factoryInfoEn",factoryInfoEn);
			model.addAttribute("productNameEn",productNameEn);
			model.addAttribute("pid",pid);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("======getFactoryProductById======",e.getMessage());
		}
		//中英文版跳转不同页面
		if("en".equals(lan)){
            return "../en/product_detail.html";
		}else{
			return "product_detail.html";
		}
	}
}
