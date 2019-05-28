package com.cbt.controller;

import com.alibaba.fastjson.JSON;
import com.cbt.entity.*;
import com.cbt.enums.*;
import com.cbt.exception.NameOrPasswordException;
import com.cbt.rpc.GetProjectCountByFactoryId;
import com.cbt.rpc.GetProjectListByFactoryId;
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



@RequestMapping("/inquiry")
@Controller
public class InquiryController {
	  @Autowired
	  private FactoryInfoService factoryInfoService;  
	  @Autowired
	  private QualificationService qualificationService;  
	  @Autowired
	  private QuoteProductService quoteProductService;  
	  @Autowired
	  private QuoteInfoService quoteInfoService;  
	  @Autowired
	  private CollectTabService collectTabService;  
	  @Autowired
	  private QuoteMessageService quoteMessageService;  
	  @Autowired
	  private SupplierQuoteInfoService supplierQuoteInfoService;  
	  @Autowired
	  private SupplierQuoteProductService supplierQuoteProductService;  
	  @Autowired
	  private EquipmentService equipmentService;  
	  @Autowired
	  private ProductLibraryService productLibraryService;
	  @Autowired
	  private QuoteBigProductsService quoteBigProductsService;
	  @Autowired
	  private QuoteBigProductsTabService quoteBigProductsTabService;	  
	  @Autowired
	  private QuoteWeeklyReportService quoteWeeklyReportService;
	  @Autowired
	  private FactoryEvaluateService factoryEvaluateService;
	  @Autowired
	  private InquiryClickLogService inquiryClickLogService;

	  
	  
	  private Logger logger = LoggerFactory.getLogger(this.getClass());
	  
	  
	  
	  
	  /**
	   * 改变语言
	   * @Title changeLanguage 
	   * @Description
	   * @param request
	   * @param response
	   * @return
	   * @return String
	   */
	  @RequestMapping(value = "/changelanguage.do")  
	  public String changeLanguage(HttpServletRequest request, HttpServletResponse response) {  
	  return "redirect:" + request.getParameter("originalUrl");  
	  }  

	  
	  
	  /**
	   * 翻译产品数据
	   * @Title changeLan 
	   * @Description
	   * @param quoteProduct
	   * @return
	   * @return Object
	   */
	  public static QuoteProduct changeLan(QuoteProduct quoteProduct){
		  
		  if(quoteProduct!=null){			  
			  String transResult = "";
			  if(StringUtils.isNotBlank(quoteProduct.getMainProcess())){
//				  transResult = TransApi.getTransLocal(quoteProduct.getMainProcess());
//				  transResult = TransApi.getTransResult(quoteProduct.getMainProcess(), "zh", "en");
//				  quoteProduct.setMainProcess(transResult);  
				  String mainProcessEn = "";
//				  System.out.println(quoteProduct.getMainProcess());
				  ProcessZhAndEnEnum enumProcess = ProcessZhAndEnEnum.getEnum(quoteProduct.getMainProcess());
				  if(enumProcess != null){
					  mainProcessEn = enumProcess.getValue();
				  }
				  quoteProduct.setMainProcess(mainProcessEn);  
			  }
			  if(StringUtils.isNotBlank(quoteProduct.getQuoteTitle())){
//				  transResult = TransApi.getTransLocal(quoteProduct.getQuoteTitle());
				  
				  transResult = TransApi.getTransResult(quoteProduct.getQuoteTitle(), "zh", "en");
				  quoteProduct.setQuoteTitle(transResult);
			  }
			  if(StringUtils.isNotBlank(quoteProduct.getQuantityUnit())){
//				  transResult = TransApi.getTransLocal(quoteProduct.getQuantityUnit());
				  transResult = TransApi.getTransResult(quoteProduct.getQuantityUnit(), "zh", "en");
				  quoteProduct.setQuantityUnit(transResult);
			  }
			  if(StringUtils.isNotBlank(quoteProduct.getMaterials())){
//				  transResult = TransApi.getTransLocal(quoteProduct.getProductName());
				  transResult = TransApi.getTransResult(quoteProduct.getMaterials(), "zh", "en");
				  quoteProduct.setMaterials(transResult);
			  }
		  }		  
		  return quoteProduct;
	  }

	/**
	 * 微信快速扫码登录
	 * @param request
	 * @param model
	 * @param response
	 * @return
	 */
	@RequestMapping("/index")
	public String getIndex(HttpServletRequest request,Model model,HttpServletResponse response){
	  	//当前工厂id
		String factoryId = WebCookie.getFactoryId(request);
		//获取当前语言
		String lan = WebCookie.getLanguage(request);

		Integer pageSize = 24;
		Integer page = 1;
		Integer start = pageSize * (page-1);
		Integer orderStatus = OrderStatusEnum.NORMAL_ORDER.getCode();
		List<QuoteProduct> inquiryOrders = null;
		//如果是英文版，查询英文数据库内容
		if("en".equals(lan)){
			inquiryOrders = quoteProductService.queryProductGroupByOrderIdEn(start, pageSize, orderStatus, null, null,null,factoryId,null);
		}else{
			inquiryOrders = quoteProductService.queryProductGroupByOrderId(start, pageSize, orderStatus, null, null,null,factoryId,null);
		}

		for(QuoteProduct product : inquiryOrders){
			String quantityList = product.getQuantityList();
			String quanlityUnit = product.getQuantityUnit();
			if(StringUtils.isNotBlank(quantityList) && StringUtils.isNotBlank(quanlityUnit)){
				String[] split = quantityList.split(",");
				String[] split2 = quanlityUnit.split(",");
				if(split.length > 0){
					quantityList = split[0] + split2[0];
					product.setQuantityList(quantityList);
				}
			}
		}
		model.addAttribute("inquiryOrders",inquiryOrders);
		//当前微信账号
		//openid不为空时，与微信账号绑定
		String loginEmail = WebCookie.getLoginEmail(request);
		String pwd = WebCookie.getPwd(request);
		String openid = request.getParameter("openid");
		String unionid = request.getParameter("unionid");
		try {
			if(StringUtils.isNotBlank(loginEmail)&&StringUtils.isNotBlank(pwd)&&StringUtils.isNotBlank(unionid)){
                factoryInfoService.login(loginEmail, pwd, openid, unionid);
				//判断客户是否已经微信绑定
				Cookie userCookie5 = new Cookie("w_flag",Base64Encode.getBase64("true"));
				userCookie5.setPath("/");
				userCookie5.setMaxAge(60*60*24*365);
				response.addCookie(userCookie5);
            }
		} catch (NameOrPasswordException e) {
			e.printStackTrace();
			return "login.html";
		}

		if("en".equals(lan)){
			return "../en/index.html";
		}else{
			return "index.html";
		}
	}
	  
	  /**
	   * 查询询盘列表
	   * @param request
	   * @param response
	   * @return
	   */
	  @ResponseBody
	  @RequestMapping("/queryInquiryListToIndex.do")
	  public JsonResult<Map<String,Object>> queryInquiryListToIndex(HttpServletRequest request,HttpServletResponse response){
		  
		  try {

			  String factoryId = WebCookie.getFactoryId(request);
			  
			  String str = request.getParameter("page");
			  
			  //获取当前语言
			  String lan = WebCookie.getLanguage(request);
			  
			  
			  //add by jason  begin
			  String pageSizeString = request.getParameter("pageSize");
			  
			  String bigBuyerId = request.getParameter("bigBuyerId");
			  
			  Integer pageSize = 0;
			  Integer page = 1;
			  if(StringUtils.isBlank(str)){
				  page = 1;
			  }else{
				  page = Integer.parseInt(str);
			  }
			  
			  if (StringUtils.isBlank(pageSizeString)) {
				  pageSize = 18;
			  }else{
				  pageSize = Integer.parseInt(pageSizeString);
			  }
			  
			  
			  Integer start = pageSize * (page-1);
			  Integer orderStatus = OrderStatusEnum.NORMAL_ORDER.getCode();
			  
			  String process = request.getParameter("process");  //工艺
		      String product = request.getParameter("product");  //产品
			  String type = request.getParameter("customerType");//客户类型
			  Integer customerType = null;
			  if(StringUtils.isNotBlank(type)){
				  customerType = CustomerTypeEnum.getEnum(type).getCode();
			  }
			  
			  List<QuoteProduct> inquiryOrders = null;
			  //如果是英文版，查询英文数据库内容
			  if("en".equals(lan)){
				  inquiryOrders = quoteProductService.queryProductGroupByOrderIdEn(start, pageSize, orderStatus, process, product,customerType,factoryId,bigBuyerId);
			  }else{
				  inquiryOrders = quoteProductService.queryProductGroupByOrderId(start, pageSize, orderStatus, process, product,customerType,factoryId,bigBuyerId);
			  }
			  
			  int totalOrder = quoteProductService.totalOrder(orderStatus, process, product, customerType,factoryId,bigBuyerId);
			  
			  //获取最近一周总价（最高数量计算）
			  double totalEstimatedPrice = quoteInfoService.calEstimatedPrice();
			  
			  //查询最近完成订单
			  Integer[] items = {OrderStatusEnum.CONFIRM.getCode(),OrderStatusEnum.PROCESS.getCode()};
			  List<?> quotes = quoteInfoService.queryOrderFactoryList(items);

			  Map<String,Object> map = new HashMap<String, Object>();
			  map.put("inquiryOrders", inquiryOrders);			  
			  map.put("totalOrder", totalOrder);				  
			  map.put("totalEstimatedPrice", totalEstimatedPrice);		
			  map.put("finishQuotes", quotes);
			  
			  return  new JsonResult<Map<String,Object>>(map);
		  } catch (NumberFormatException e) {
			  logger.error("==========inquiry   queryInquiryListToIndex=========",e);
			  return  new JsonResult<Map<String,Object>>(1,"查询失败");
		  } 
		  
	  }
	  

	  
	  /**
	   * 查询一周询盘金额和最近确认订单询盘
	   * @param request
	   * @param response
	   * @return
	   */
	  @ResponseBody
	  @RequestMapping("/getTotalEstimatedPrice.do")
	  public JsonResult<Map<String,Object>> getTotalEstimatedPrice(HttpServletRequest request,HttpServletResponse response){
			  
		  try {
			  //获取最近一周总价（最高数量计算）
			  double totalEstimatedPrice = quoteInfoService.calEstimatedPrice();
			  //获取当前语言
			  String lan = WebCookie.getLanguage(request);
			  //查询最近完成订单
			  Integer[] items = {OrderStatusEnum.CONFIRM.getCode(),OrderStatusEnum.PROCESS.getCode()};
			  List<?> quotes = null;
			  if("en".equals(lan)){
				  quotes = quoteInfoService.queryOrderFactoryListEn(items);
			  }else{
				  quotes = quoteInfoService.queryOrderFactoryList(items);
			  }

			  Map<String,Object> map = new HashMap<String, Object>();
			  map.put("totalEstimatedPrice", totalEstimatedPrice);
			  map.put("finishQuotes", quotes);

			  return  new JsonResult<Map<String,Object>>(map);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("==========inquiry   getTotalEstimatedPrice=========",e);
			return  new JsonResult<Map<String,Object>>(1,"查询失败");
		}		  
		  
	  }
	  
	  
	  /**
	   * 查询询盘列表
	   * @param request
	   * @param response
	   * @return
	   */
	  @ResponseBody
      @RequestMapping("/queryInquiryList.do")
      public JsonResult<Map<String,Object>> queryInquiryList(HttpServletRequest request,HttpServletResponse response){
    	 
    	  try {
    		  
    		     		  
    		  
			     String factoryId = WebCookie.getFactoryId(request);

				  String str = request.getParameter("page");
				  
                   //add by jason  bigin
				  String pageSizeString = request.getParameter("pageSize");
				  
				  String bigBuyerId = request.getParameter("bigBuyerId");
				  
				  Integer pageSize = 0;
				  Integer page = 1;
				  if(StringUtils.isBlank(str)){
					  page = 1;
				  }else{
					  page = Integer.parseInt(str);
				  }
				  
				  if (StringUtils.isBlank(pageSizeString)) {
					pageSize = 18;
				  }else{
					pageSize = Integer.parseInt(pageSizeString);
				  }
				  
				  
				  Integer start = pageSize * (page-1);
				  Integer orderStatus = OrderStatusEnum.NORMAL_ORDER.getCode();
				  
				  String process = request.getParameter("process");
				  String product = request.getParameter("product");
				  String type = request.getParameter("customerType");
			      logger.info("product",product);


				  Integer customerType = null;
				  if(StringUtils.isNotBlank(type)){
					  customerType = CustomerTypeEnum.getEnum(type).getCode();
				  }
				  
				  //获取中、英文查询
//				  if(StringUtils.isNotBlank(process)){
//					  ProcessZhAndEnEnum enEnum = ProcessZhAndEnEnum.getEnum(process);
//					  if(enEnum != null){
//						  process = enEnum.getStr()+enEnum.getValue();
//					  }
//				  }
				  
				  List<QuoteProduct> inquiryOrders = null;
				
				  //获取当前语言
				  //如果是英文版，调用翻译接口，翻译部分数据
				  String lan = WebCookie.getLanguage(request);
				  if("en".equals(lan)){
					  inquiryOrders = quoteProductService.queryProductGroupByOrderIdEn(start, pageSize, orderStatus, process, product,customerType,factoryId,bigBuyerId);
				  }else{
					  inquiryOrders = quoteProductService.queryProductGroupByOrderId(start, pageSize, orderStatus, process, product,customerType,factoryId,bigBuyerId);
				  }
				  
				  int totalOrder = quoteProductService.totalOrder(orderStatus, process, product, customerType,factoryId,bigBuyerId);
				  
				  //匹配工厂工艺进行排序				  
//				  Collections.sort(inquiryOrders,new InquirySortState());
//				  Collections.sort(inquiryOrders,new InquirySortProcess());
				  
				  Map<String,Object> map = new HashMap<String, Object>();
				  map.put("inquiryOrders", inquiryOrders);			  
				  map.put("totalOrder", totalOrder);
				  return  new JsonResult<Map<String,Object>>(map);
			} catch (NumberFormatException e) {
				logger.error("==========inquiry   queryInquiryList=========",e);
		    	  return  new JsonResult<Map<String,Object>>(1,"查询失败");
			} 

      }
	  
	  
	  /**
	   * 查询询盘列表
	   * @param request
	   * @param response
	   * @return
	   */
	  @ResponseBody
      @RequestMapping("/queryAllListByStatus.do")
      public JsonResult<Map<String,Object>> queryAllList(HttpServletRequest request,HttpServletResponse response){
    	 
    	  try {
    		  
  		  
    		      String factoryId = WebCookie.getFactoryId(request); 
    		      String str = request.getParameter("page");
    		      Integer pageSize = 20;
				  Integer page = 1;
				  if(StringUtils.isBlank(str)){
					  page = 1;
				  }else{
					  page = Integer.parseInt(str);
				  }				  				  				  
				  Integer start = pageSize * (page-1);
				  String status = request.getParameter("status");
				  String process = request.getParameter("process");


				  Integer orderStatus = Integer.parseInt(status);
				  List<QuoteProduct> inquiryOrders = new ArrayList<QuoteProduct>();
				  inquiryOrders = quoteProductService.queryProductGroupByOrderId(start, pageSize, orderStatus, process, null, null, factoryId, null);
				  if(StringUtils.isBlank(factoryId)){
					     inquiryOrders = quoteProductService.queryAllProductGroupByOrderId(orderStatus, factoryId);

				  }else{
					  if(orderStatus == OrderStatusEnum.CONFIRM.getCode()){
						 inquiryOrders = quoteProductService.queryFinishByFactoryGroupByOrderId(start, pageSize, process, null, factoryId);	
					  }
				  }
				  //获取当前语言
				  //如果是英文版，调用翻译接口，翻译部分数据
				  String lan = WebCookie.getLanguage(request);
				  if("en".equals(lan)){
					  inquiryOrders = quoteProductService.queryProductGroupByOrderIdEn(start, pageSize, orderStatus, process, null, null, factoryId, null);
					  if(StringUtils.isBlank(factoryId)){
						  inquiryOrders = quoteProductService.queryAllProductGroupByOrderIdEn(orderStatus, factoryId);

					  }else{
						  if(orderStatus == OrderStatusEnum.CONFIRM.getCode()){
							  inquiryOrders = quoteProductService.queryFinishByFactoryGroupByOrderIdEn(start, pageSize, process, null, factoryId);
						  }
					  }
				  }
				  
				  
				  int totalOrder = quoteProductService.totalOrder(orderStatus, process, null, null,factoryId,null);	  				  
				  Map<String,Object> map = new HashMap<String, Object>();
				 
				  map.put("inquiryOrders", inquiryOrders);	
				  map.put("totalOrder", totalOrder);		
				  return  new JsonResult<Map<String,Object>>(map);
			} catch (NumberFormatException e) {
				logger.error("==========inquiry   queryAllListByStatus=========",e);
		    	  return  new JsonResult<Map<String,Object>>(1,"查询失败");
			} 

      }
	  
	  
	  
	  /**
	   * 查询邀请报价的询盘
	   * @param request
	   * @param response
	   * @return
	   */
	  @ResponseBody
	  @RequestMapping("/queryInvitation.do")
	  public JsonResult<Map<String,Object>> queryInvitation(HttpServletRequest request,HttpServletResponse response){
		  
		  try {
			  String factoryId = WebCookie.getFactoryId(request);
			  if(StringUtils.isBlank(factoryId)){
				  return  new JsonResult<Map<String,Object>>(2,"未获取到登录信息");
			  }else{
			  Integer status = OrderStatusEnum.NORMAL_ORDER.getCode();
			  String process = request.getParameter("process");
			  //获取中、英文查询
//			  if(StringUtils.isNotBlank(process)){
//				  ProcessZhAndEnEnum enEnum = ProcessZhAndEnEnum.getEnum(process);
//				  if(enEnum != null){
//					  process = enEnum.getStr()+enEnum.getValue();
//				  }
//			  }
			  
			  
			  String product = request.getParameter("product");
			  int totalOrder = 0;
			  List<QuoteProduct> inquiryOrders = quoteProductService.queryInvitationOrder(status, process, product, factoryId);
              if(inquiryOrders != null){
				  totalOrder = inquiryOrders.size();  
              }		 

			  	  				  
			  Map<String,Object> map = new HashMap<String, Object>();
			  map.put("inquiryOrders", inquiryOrders);	
			  map.put("totalOrder", totalOrder);		
			  return  new JsonResult<Map<String,Object>>(map);
			  }
		  } catch (NumberFormatException e) {
			  logger.error("==========inquiry   queryInvitation=========",e);
			  return  new JsonResult<Map<String,Object>>(1,"查询失败");
		  } 
		  
	  }
	  
	  
	  
	  
	  
	  
	  
	  
	  /**
	   * 查询已报价
	   * @param request
	   * @param response
	   * @return
	   */
	  @ResponseBody
      @RequestMapping("/queryQuoteList.do")
      public JsonResult<Map<String,Object>> queryQuoteList(HttpServletRequest request,HttpServletResponse response){
    	 
    	  try {
			  String factoryId = WebCookie.getFactoryId(request);
			  if(StringUtils.isBlank(factoryId)){
				  return  new JsonResult<Map<String,Object>>(2,"未获取到登录信息");
			  }else{
				  String str = request.getParameter("page");
				  Integer pageSize = 18;
				  Integer page = 1;
				  if(StringUtils.isBlank(str)){
					  page = 1;
				  }else{
					  page = Integer.parseInt(str);
				  }
				  Integer start = pageSize * (page-1);
				  
				  String process = request.getParameter("process");
				  //获取中、英文查询
//				  if(StringUtils.isNotBlank(process)){
//					  ProcessZhAndEnEnum enEnum = ProcessZhAndEnEnum.getEnum(process);
//					  if(enEnum != null){
//						  process = enEnum.getStr()+enEnum.getValue();
//					  }
//				  }
				  
				  
				  String product = request.getParameter("product");
				  Integer status = null;
				  if(StringUtils.isNotBlank(request.getParameter("status"))){
					  status = Integer.valueOf(request.getParameter("status"));
				  }

				  List<QuoteProduct> inquiryOrders = quoteProductService.queryByFactoryGroupByOrderId(start, pageSize, process, product, factoryId,status);
				  int totalOrder = quoteProductService.totalQuoteOrder(process, product, factoryId,status);
			  
				  Map<String,Object> map = new HashMap<String, Object>();
				  map.put("inquiryOrders", inquiryOrders);			  
				  map.put("totalOrder", totalOrder);				  
				  return  new JsonResult<Map<String,Object>>(map);
			  }
			} catch (NumberFormatException e) {
				  logger.error("==========inquiry   queryQuoteList=========",e);
		    	  return  new JsonResult<Map<String,Object>>(1,"查询失败");
			} catch (Exception e) {
				  logger.error("==========inquiry   queryQuoteList=========",e);
				  return  new JsonResult<Map<String,Object>>(1,"查询失败");
			} 

      }
	  
	  
	  /**
	   * 查询报价结束
	   * @param request
	   * @param response
	   * @return
	   */
	  @ResponseBody
      @RequestMapping("/queryFinishQuoteList.do")
      public JsonResult<Map<String,Object>> queryFinishQuoteList(HttpServletRequest request,HttpServletResponse response){
    	 
    	  try {
			  String factoryId = WebCookie.getFactoryId(request);
			  if(StringUtils.isBlank(factoryId)){
				  return  new JsonResult<Map<String,Object>>(2,"未获取到登录信息");
			  }else{
				  String str = request.getParameter("page");
				  Integer pageSize = 18;
				  Integer page = 1;
				  if(StringUtils.isBlank(str)){
					  page = 1;
				  }else{
					  page = Integer.parseInt(str);
				  }
				  Integer start = pageSize * (page-1);
				  
				  String process = request.getParameter("process");
				  //获取中、英文查询
//				  if(StringUtils.isNotBlank(process)){
//					  ProcessZhAndEnEnum enEnum = ProcessZhAndEnEnum.getEnum(process);
//					  if(enEnum != null){
//						  process = enEnum.getStr()+enEnum.getValue();
//					  }
//				  }
				  
				  
				  String product = request.getParameter("product");

				  List<QuoteProduct> inquiryOrders = quoteProductService.queryFinishByFactoryGroupByOrderId(start, pageSize, process, product, factoryId);				  				  
				  List<Integer> accepts = new ArrayList<Integer>();
				  for (QuoteProduct quoteProduct : inquiryOrders) {
					QuoteBigProducts quoteBigProducts = quoteBigProductsService.selectBySupplierId(factoryId,quoteProduct.getOrderId());
					if(!(quoteBigProducts == null || "".equals(quoteBigProducts))){
						accepts.add(quoteBigProducts.getIsSupplierAccept());
					}else{
						accepts.add(null);
					}
			      }

				  //获取任务系统项目详情
				  List<ProjectVO> projectList = null;
				  GetProjectListByFactoryId project = new GetProjectListByFactoryId();
				  Map<Object, Object> projectMap = project.sendRequest(factoryId);
				  //获取projectVO对象
				  if(projectMap.get("projectVOList")!= null){
					  projectList = JSON.parseArray(projectMap.get("projectVOList").toString(), ProjectVO.class);
					  for(ProjectVO projectVO:projectList){
						  if(projectVO.getDeliveryDate()!=null){
							  String stamp2Date = DateFormat.timeStamp2Date(projectVO.getDeliveryDate(), "yyyy-MM-dd");
							  projectVO.setDeliveryDate(stamp2Date);
						  }
						  if(projectVO.getDateSampleUploading()!=null){
							  String stamp2Date = DateFormat.timeStamp2Date(projectVO.getDateSampleUploading(), "yyyy-MM-dd");
							  projectVO.setDateSampleUploading(stamp2Date);
						  }

						  //查询最近上传时间
						  String uploadDate = quoteWeeklyReportService.queryMaxUploadDate(projectVO.getProjectNo(), factoryId);
						  projectVO.setUploadDate(uploadDate);
                          //查询上传报告(材质报告、开工视频)
						  List<QuoteWeeklyReport> reports = quoteWeeklyReportService.queryByCsgOrderIdAndType(projectVO.getProjectNo(), null, factoryId);
						  for(QuoteWeeklyReport report : reports){
							 if(report.getFileType() == FileTypeEnum.MATERIAL_FILE.getCode()){
								 projectVO.setUploadMaterial(true);
							 }
							  if(report.getFileType() == FileTypeEnum.VIDEO.getCode()){
								  projectVO.setUploadVideo(true);
							  }
						  }
					  }
				  }


                  //获取工厂名
				  FactoryInfo factoryInfo = factoryInfoService.selectFactoryInfo(factoryId);
				  int totalOrder = quoteProductService.totalFinishQuoteOrder(process, product, factoryId);
				  Map<String,Object> map = new HashMap<String, Object>();
				  map.put("inquiryOrders", inquiryOrders);			  
				  map.put("totalOrder", totalOrder);				  
				  map.put("accepts", accepts);				  
				  map.put("projectList", projectList);
				  map.put("factoryInfo", factoryInfo);
				  return  new JsonResult<Map<String,Object>>(map);
			  }
			} catch (NumberFormatException e) {
				  logger.error("==========inquiry   queryFinishQuoteList=========",e);
		    	  return  new JsonResult<Map<String,Object>>(1,"查询失败");
			} catch (Exception e) {
				  logger.error("==========inquiry   queryFinishQuoteList=========",e);
				  return  new JsonResult<Map<String,Object>>(1,"查询失败");
			} 

      }
	  
	  
	  /**
	   * 查询收藏产品
	   * @param request
	   * @param response
	   * @return
	   */
	  @ResponseBody
      @RequestMapping("/queryCollectList.do")
      public JsonResult<Map<String,Object>> queryCollectList(HttpServletRequest request,HttpServletResponse response){
    	 
    	  try {
			  String factoryId = WebCookie.getFactoryId(request);
			  if(StringUtils.isBlank(factoryId)){
				  return  new JsonResult<Map<String,Object>>(2,"未获取到登录信息");
			  }else{
				  String str = request.getParameter("page");
				  Integer pageSize = 18;
				  Integer page = 1;
				  if(StringUtils.isBlank(str)){
					  page = 1;
				  }else{
					  page = Integer.parseInt(str);
				  }
				  Integer start = pageSize * (page-1);
				  
				  String process = request.getParameter("process");
				  //获取中、英文查询
//				  if(StringUtils.isNotBlank(process)){
//					  ProcessZhAndEnEnum enEnum = ProcessZhAndEnEnum.getEnum(process);
//					  if(enEnum != null){
//						  process = enEnum.getStr()+enEnum.getValue();
//					  }
//				  }
				  
				  
				  
				  String product = request.getParameter("product");
				  				  
				  List<QuoteProduct> inquiryOrders = quoteProductService.queryByCollectOrderId(start, pageSize, process, product, factoryId);
				  int totalOrder = quoteProductService.totalCollectCount(process, product, factoryId);

				  
				  
				  Map<String,Object> map = new HashMap<String, Object>();
				  map.put("inquiryOrders", inquiryOrders);			  
				  map.put("totalOrder", totalOrder);				  
				  return  new JsonResult<Map<String,Object>>(map);
			  }
			} catch (NumberFormatException e) {
				 logger.error("==========inquiry   queryCollectList=========",e);
		    	  return  new JsonResult<Map<String,Object>>(1,"查询失败");
			} catch (Exception e) {
				 logger.error("==========inquiry   queryCollectList=========",e);
				  return  new JsonResult<Map<String,Object>>(1,"查询失败");
			} 

      }



	/**
	 * 查询沟通过的项目
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryMessageOrder.do")
	public JsonResult<Map<String,Object>> queryMessageOrder(HttpServletRequest request,HttpServletResponse response){

		try {
			String factoryId = WebCookie.getFactoryId(request);
			if(StringUtils.isBlank(factoryId)){
				return  new JsonResult<Map<String,Object>>(2,"未获取到登录信息");
			}else{
				String str = request.getParameter("page");
				Integer pageSize = 18;
				Integer page = 1;
				if(StringUtils.isBlank(str)){
					page = 1;
				}else{
					page = Integer.parseInt(str);
				}
				Integer start = pageSize * (page-1);

				String process = request.getParameter("process");

				String product = request.getParameter("product");

				List<QuoteProduct> inquiryOrders = quoteProductService.queryHaveMessageOrder(start,pageSize,process,product,factoryId);
				int totalOrder = quoteProductService.countMessageOrder(process,product,factoryId);



				Map<String,Object> map = new HashMap<String, Object>();
				map.put("inquiryOrders", inquiryOrders);
				map.put("totalOrder", totalOrder);
				return  new JsonResult<Map<String,Object>>(map);
			}
		} catch (NumberFormatException e) {
			logger.error("==========inquiry   queryCollectList=========",e);
			return  new JsonResult<Map<String,Object>>(1,"查询失败");
		} catch (Exception e) {
			logger.error("==========inquiry   queryCollectList=========",e);
			return  new JsonResult<Map<String,Object>>(1,"查询失败");
		}

	}





	  
	  /**
	   * 首页查询各主工艺分类询盘
	   * @param request
	   * @param response
	   * @return
	   */
	  @ResponseBody
      @RequestMapping("/queryByMainProcess.do")
      public JsonResult<Map<String,Object>> queryByMainProcess(HttpServletRequest request,HttpServletResponse response){
    	 
    	  try {			      
    		  
    		      String factoryId = WebCookie.getFactoryId(request);
				  String str = request.getParameter("page");
				  Integer pageSize = 6;
				  Integer page = 1;
				  if(StringUtils.isBlank(str)){
					  page = 1;
				  }else{
					  page = Integer.parseInt(str);
				  }
				  Integer start = pageSize * (page-1);
				  
//				  String process1 = "塑料，注塑，吸塑，滚塑，吹塑，挤塑，其他塑料工艺";
//				  String process2 = "铸造，不锈钢铸造(硅溶胶、水玻璃)，压铸(铝、锌、镁、铜等)，砂铸(钢、铁、铜、铝等)，重力铸造(铝、铜等)，其他铸造";
//				  String process3 = "锻造，热锻，冷锻";
//				  String process4 = "加工，钣金加工（切割、折弯、焊接、组装），冲压拉伸，旋压，铝挤压";
//				  String process5 = "机加工，3轴以上精密加工，高速车床，高速铣床，普通车铣钻磨，其他机加工";
//				  String process6 = "其他，现成商品采购，其他采购";
				  
				  List<String> process1 = new ArrayList<String>(Arrays.asList("注塑", "吸塑","滚塑","吹塑","吸塑","挤塑","其他塑料工艺"));
				  int p1 = process1.size();
				  for (int i = 0; i < p1; i++) {
					  ProcessZhAndEnEnum enEnum = ProcessZhAndEnEnum.getEnum(process1.get(i));
					  if(enEnum != null){
						  process1.add(enEnum.getValue());
					  }
				  }								  
				  List<String> process2 = new ArrayList<String>(Arrays.asList("不锈钢铸造(硅溶胶、水玻璃)","压铸(铝、锌、镁、铜等)","砂铸(钢、铁、铜、铝等)","重力铸造(铝、铜等)","其他铸造"));
				  p1 = process2.size();
				  for (int i = 0; i < p1; i++) {
					  ProcessZhAndEnEnum enEnum = ProcessZhAndEnEnum.getEnum(process2.get(i));
					  if(enEnum != null){
					     process2.add(enEnum.getValue());
					  }
				  }		
				  List<String> process3 = new ArrayList<String>(Arrays.asList("热锻", "冷锻"));
				  p1 = process3.size();
				  for (int i = 0; i < p1; i++) {
					  ProcessZhAndEnEnum enEnum = ProcessZhAndEnEnum.getEnum(process3.get(i));
					  process3.add(enEnum.getValue());
				  }	
				  List<String> process4 = new ArrayList<String>(Arrays.asList("钣金加工(切割、折弯、焊接、组装)","冲压拉伸","旋压","铝挤压"));
				  p1 = process4.size();
				  for (int i = 0; i < p1; i++) {
					  ProcessZhAndEnEnum enEnum = ProcessZhAndEnEnum.getEnum(process4.get(i));
					  if(enEnum != null){
					     process4.add(enEnum.getValue());
					  }
				  }	
				  
				  List<String> process5 = new ArrayList<String>(Arrays.asList("3轴以上精密加工", "高速车床","高速铣床","普通车铣钻磨","其他机加工"));
				  p1 = process5.size();
				  for (int i = 0; i < p1; i++) {
					  ProcessZhAndEnEnum enEnum = ProcessZhAndEnEnum.getEnum(process5.get(i));
					  if(enEnum != null){
					     process5.add(enEnum.getValue());
					  }
				  }	
				  
				  List<String> process6 = new ArrayList<String>(Arrays.asList("现成商品采购", "其他","其他采购"));
				  p1 = process6.size();
				  for (int i = 0; i < p1; i++) {
					  ProcessZhAndEnEnum enEnum = ProcessZhAndEnEnum.getEnum(process6.get(i));
					  if(enEnum != null){
					     process6.add(enEnum.getValue());
					  }
				  }	
				  
				  
				  List<QuoteProduct> injectionOrders = null;
				  //获取当前语言
				  //如果是英文版，调用翻译接口，翻译部分数据
				  String lan = WebCookie.getLanguage(request);
				  /*
				   *注塑中英文查询
				   */
				  if("en".equals(lan)){
					  injectionOrders = quoteProductService.queryProductByMainProcessEn(start, pageSize, OrderStatusEnum.NORMAL_ORDER.getCode(), process1, factoryId);
				  }else{
					  injectionOrders = quoteProductService.queryProductByMainProcess(start, pageSize, OrderStatusEnum.NORMAL_ORDER.getCode(), process1, factoryId);
				  }

				  /*
				   *铸造中英文查询
				   */
				  List<QuoteProduct> metalOrders = null;
				  if("en".equals(lan)){
					  metalOrders = quoteProductService.queryProductByMainProcessEn(start, pageSize, OrderStatusEnum.NORMAL_ORDER.getCode(), process2, factoryId);
				  }else{
					  metalOrders = quoteProductService.queryProductByMainProcess(start, pageSize, OrderStatusEnum.NORMAL_ORDER.getCode(), process2, factoryId);
				  }

			      /*
				   *锻造中英文查询
				   */
				  List<QuoteProduct> castingOrders = null;
				  if("en".equals(lan)){
					  castingOrders = quoteProductService.queryProductByMainProcessEn(start, pageSize, OrderStatusEnum.NORMAL_ORDER.getCode(), process3, factoryId);
				  }else{
					  castingOrders = quoteProductService.queryProductByMainProcess(start, pageSize, OrderStatusEnum.NORMAL_ORDER.getCode(), process3, factoryId);
				  }

			      /*
				   *钣金中英文查询
				   */
				  List<QuoteProduct> machiningOrders = null;
				  if("en".equals(lan)){
					  machiningOrders= quoteProductService.queryProductByMainProcessEn(start, pageSize, OrderStatusEnum.NORMAL_ORDER.getCode(), process4, factoryId);
				  }else{
					  machiningOrders= quoteProductService.queryProductByMainProcess(start, pageSize, OrderStatusEnum.NORMAL_ORDER.getCode(), process4, factoryId);
				  }


                  /*
				   *机加工中英文查询
				   */
				  List<QuoteProduct> machiningCastingOrders = null;
				  if("en".equals(lan)){
					  machiningCastingOrders = quoteProductService.queryProductByMainProcessEn(start, pageSize, OrderStatusEnum.NORMAL_ORDER.getCode(), process5, factoryId);
				  }else{
					  machiningCastingOrders = quoteProductService.queryProductByMainProcess(start, pageSize, OrderStatusEnum.NORMAL_ORDER.getCode(), process5, factoryId);
				  }

			      /*
				   *其他工艺中英文查询
				   */
				  List<QuoteProduct> otherOrders = null;
				  if("en".equals(lan)){
					  otherOrders = quoteProductService.queryProductByMainProcessEn(start, pageSize, OrderStatusEnum.NORMAL_ORDER.getCode(), process6, factoryId);
				  }else{
					  otherOrders = quoteProductService.queryProductByMainProcess(start, pageSize, OrderStatusEnum.NORMAL_ORDER.getCode(), process6, factoryId);
				  }
				  				  
				  Map<String,Object> map = new HashMap<String, Object>();
				  map.put("injectionOrders", injectionOrders);			  				  
				  map.put("metalOrders", metalOrders);			  				  
				  map.put("machiningOrders", machiningOrders);			  				  
				  map.put("castingOrders", castingOrders);			  				  
				  map.put("machiningCastingOrders", machiningCastingOrders);			  				  
				  map.put("otherOrders", otherOrders);		
				  
				  return  new JsonResult<Map<String,Object>>(map);
			} catch (NumberFormatException e) {
				 logger.error("==========inquiry   queryByMainProcess=========",e);
		    	  return  new JsonResult<Map<String,Object>>(1,"查询失败");
			}

      }
	  
	  
	  
	  
	  
	  
	  /**
	   * 查询数量
	   * @param request
	   * @param response
	   * @return
	   */
	  @ResponseBody
      @RequestMapping("/queryCountList.do")
      public JsonResult<Map<String,Object>> queryCountList(HttpServletRequest request,HttpServletResponse response){
    	 
    	  try {
			  String factoryId = WebCookie.getFactoryId(request);
			  if(StringUtils.isBlank(factoryId)){
				  response.sendRedirect(GetServerPathUtil.getServerPath()+"/zh/login.html");
				  return  new JsonResult<Map<String,Object>>(1,"未获取到登录信息");
			  }else{
				  int invitationOrder = 0;
				  List<QuoteProduct> inquiryOrders = quoteProductService.queryInvitationOrder(OrderStatusEnum.NORMAL_ORDER.getCode(), null, null, factoryId);
	              if(inquiryOrders != null){
	            	  invitationOrder = inquiryOrders.size();  
	              }
				  
				  //供应商查询
				  int quoteOrder = quoteProductService.totalQuoteOrder(null, null, factoryId,null);	
				  //正常报价询盘
				  int normalOrder = quoteProductService.totalQuoteOrder(null, null, factoryId,QuoteOrderStatusEnum.NORMAL_ORDER.getCode());
				  //收藏询盘
				  int collectCount = quoteProductService.totalCollectCount(null, null, factoryId);
				  //已授盘询盘
				  int quoteFinishOrder = quoteProductService.totalFinishQuoteOrder(null, null, factoryId);
				  int refusedOrder = quoteProductService.totalQuoteOrder(null, null, factoryId,QuoteOrderStatusEnum.REFUSE_ORDER.getCode());
				  //获取内部已完成项目
				  GetProjectCountByFactoryId projectCount = new GetProjectCountByFactoryId();
				  int count = projectCount.sendRequest(factoryId);
				  quoteFinishOrder = count;

				  //查询咨询过的询盘数
				  int messageOrder = quoteProductService.countMessageOrder(null,null,factoryId);
				  
				  // 查看评价数
				  int evaluateCount = factoryEvaluateService.selectCountByFactoryId(factoryId);

				  //查询过去一周新增询盘数
				  int weekCount = quoteInfoService.queryWeekQuoteCount();


				  
				  	
				  Integer permission = WebCookie.getPermission(request);
				  Integer factoryUserId = WebCookie.getFactoryUserId(request);
				  //采购商查询
				  //草稿询盘
				  int buyerDraftOrder = 0;
			        if(permission == 1){
			        	buyerDraftOrder = quoteProductService.totalByFactoryIdAdmin(null, null, factoryId, OrderStatusEnum.DRAFT.getCode());
			        }else{
			        	buyerDraftOrder = quoteProductService.totalByFactoryId(null, null, factoryId, OrderStatusEnum.DRAFT.getCode(), factoryUserId);
			        }
						 
				  //正常询盘				 
				  int buyerNormalOrder = 0;
			        if(permission == 1){
			        	buyerNormalOrder = quoteProductService.totalByFactoryIdAdmin(null, null, factoryId, OrderStatusEnum.NORMAL_ORDER.getCode());
			        }else{
			        	buyerNormalOrder = quoteProductService.totalByFactoryId(null, null, factoryId, OrderStatusEnum.NORMAL_ORDER.getCode(), factoryUserId);
			        }
				  
				  //已结束询盘
				  int buyerFinishOrder = 0;
				       if(permission == 1){
				    	    buyerFinishOrder = quoteProductService.totalByFactoryIdAdmin(null, null, factoryId, OrderStatusEnum.CONFIRM.getCode());
				        }else{
				        	buyerFinishOrder = quoteProductService.totalByFactoryId(null, null, factoryId, OrderStatusEnum.CONFIRM.getCode(), factoryUserId);
				        }
				  //已过期询盘
				  int buyerExpireOrder = 0;
					   if(permission == 1){
						    buyerExpireOrder = quoteProductService.totalByFactoryIdAdmin(null, null, factoryId, OrderStatusEnum.EXPIRE.getCode());
				        }else{
				        	buyerExpireOrder = quoteProductService.totalByFactoryId(null, null, factoryId, OrderStatusEnum.EXPIRE.getCode(), factoryUserId);
				       }
				  
				  //已取消询盘
				  int buyerCancelOrder = 0;
					   if(permission == 1){
						   buyerCancelOrder = quoteProductService.totalByFactoryIdAdmin(null, null, factoryId, OrderStatusEnum.CANCEL.getCode());
				        }else{
				        	buyerCancelOrder = quoteProductService.totalByFactoryId(null, null, factoryId, OrderStatusEnum.CANCEL.getCode(), factoryUserId);
				       }
				  
				  //授盘中询盘
				  int buyerDecisionOrder = 0;
					   if(permission == 1){
						   buyerDecisionOrder = quoteProductService.totalByFactoryIdAdmin(null, null, factoryId, OrderStatusEnum.DECISION.getCode());
				        }else{
				           buyerDecisionOrder = quoteProductService.totalByFactoryId(null, null, factoryId, OrderStatusEnum.DECISION.getCode(), factoryUserId);
				       }
				  
				  //处理中询盘
				  int buyerProcessOrder = 0;
					   if(permission == 1){
						   buyerProcessOrder = quoteProductService.totalByFactoryIdAdmin(null, null, factoryId, OrderStatusEnum.PROCESS.getCode());
				        }else{
				        	buyerProcessOrder = quoteProductService.totalByFactoryId(null, null, factoryId, OrderStatusEnum.PROCESS.getCode(), factoryUserId);
				       }
				  
				  
				  //审核未通过询盘
				  int buyerNoPassOrder = 0;
					   if(permission == 1){
						   buyerNoPassOrder = quoteProductService.totalByFactoryIdAdmin(null, null, factoryId, OrderStatusEnum.NOPASS.getCode());
				        }else{
				           buyerNoPassOrder = quoteProductService.totalByFactoryId(null, null, factoryId, OrderStatusEnum.NOPASS.getCode(), factoryUserId);
				       }
				  
				  
				  
				  Map<String,Object> map = new HashMap<String, Object>();
			  
				  map.put("invitationOrder", invitationOrder);				  
				  map.put("quoteOrder", quoteOrder);				  
				  map.put("normalOrder", normalOrder);				  
				  map.put("collectCount", collectCount);				  		  
				  map.put("quoteFinishOrder", quoteFinishOrder);				  
				  map.put("refusedOrder", refusedOrder);				  
				  map.put("evaluateCount", evaluateCount);				  
				  map.put("messageOrder", messageOrder);
				  map.put("weekCount", weekCount);

	
				  map.put("buyerDraftOrder", buyerDraftOrder);				  
				  map.put("buyerNormalOrder", buyerNormalOrder);				  
				  map.put("buyerFinishOrder", buyerFinishOrder);				  
				  map.put("buyerExpireOrder", buyerExpireOrder);				  		  
				  map.put("buyerCancelOrder", buyerCancelOrder);				  
				  map.put("buyerDecisionOrder", buyerDecisionOrder);				  
				  map.put("buyerProcessOrder", buyerProcessOrder);	
				  map.put("buyerNoPassOrder", buyerNoPassOrder);	
				  
				  
				  
				  return  new JsonResult<Map<String,Object>>(map);
			  }
			} catch (NumberFormatException e) {
				 logger.error("==========inquiry   queryCountList=========",e);
		    	  return  new JsonResult<Map<String,Object>>(1,"查询失败");
			} catch (IOException e) {
				 logger.error("==========inquiry   queryCountList=========",e);
				  return  new JsonResult<Map<String,Object>>(1,"查询失败");
			} 

      }
	  
	  
	  
	  
	  
	  
	  
	  /**
	   * 添加收藏
	   * @param request
	   * @param response
	   * @return
	   */
	  @ResponseBody
      @RequestMapping("/addCollect.do")
      public JsonResult<String> addCollect(HttpServletRequest request,HttpServletResponse response){
		  
		  try {
			  String factoryId = WebCookie.getFactoryId(request);
			  if(StringUtils.isBlank(factoryId)){
				  return new JsonResult<String>(2,"未获取到登录信息");
			  }else{
				  String orderIds = request.getParameter("orderIds");
				  List<Object> collects = new ArrayList<Object>();
				  if (orderIds.endsWith(",")) {
						if(orderIds.length()>1){
							orderIds = orderIds.substring(0, orderIds.length() - 1);					 
						    String[] split = orderIds.split(",");
						    for (int i = 0; i < split.length; i++) {
						    	CollectTab collect = new CollectTab();
						    	collect.setFactoryId(factoryId);
						    	collect.setOrderId(Integer.parseInt(split[i]));
						    	collect.setCollectDate(new Date());
						    	collects.add(collect);
						    }
						}
				  }
				  
				  if(collects != null && collects.size() != 0){
					  collectTabService.insertBatch(collects);
				  }
				  
				  return new JsonResult<String>(0,"保存成功");
			  }
			  
			  			  
			} catch (Exception e) {
				logger.error("==========inquiry   addCollect=========",e);
				  return new JsonResult<String>(1,"保存失败");
			}
		  	  		  
	  }
	  
	  
	  
	  
	  
	  /**
	   * 单个添加取消收藏
	   * @param request
	   * @param response
	   * @return
	   */
	  @ResponseBody
      @RequestMapping("/addOrCancelCollect.do")
      public JsonResult<String> addOrCancelCollect(HttpServletRequest request,HttpServletResponse response){
		  
		  try {
			  String factoryId = WebCookie.getFactoryId(request);
			  if(StringUtils.isBlank(factoryId)){
				  return new JsonResult<String>(2,"未获取到登录信息");
			  }else{
				  Integer orderId = Integer.parseInt(request.getParameter("orderId"));
				  int isCollect = collectTabService.queryByOrderIdAndFactoryId(factoryId, orderId);
				  if(isCollect == 0){					  
			    	CollectTab collect = new CollectTab();
			    	collect.setFactoryId(factoryId);
			    	collect.setOrderId(orderId);
			    	collect.setCollectDate(new Date());
			    	collectTabService.insert(collect);
				  }else{
					collectTabService.deleteByOrderIdAndFactoryId(factoryId, orderId);
				  }		
				  
				  isCollect = collectTabService.queryByOrderIdAndFactoryId(factoryId, orderId);				  
				  return new JsonResult<String>(0, "收藏成功", isCollect+"");
			  }
			  
			  			  
			} catch (Exception e) {
				logger.error("==========inquiry   addOrCancelCollect=========",e);
				  return new JsonResult<String>(1,"保存失败");
			}
		  	  		  
	  }
	  
	  
	  
	  
	  
	  
	  
	  
	  /**
	   * 
	   * @param
	   * @param request
	   * @param response
	   * @return
	   */
	  @RequestMapping("/details.do")
	  public String  details(HttpServletRequest request,HttpServletResponse response){
		  
		      String orderId = request.getParameter("orderId");
		  
			  Cookie urlCookie = new Cookie("quoteDetailUrl","");
			  urlCookie.setPath("/");
			  urlCookie.setMaxAge(0);
			  response.addCookie(urlCookie); 
			  
			  //询盘号存放session
//			  Cookie cookie = new Cookie("_pid",orderId);
//			  cookie.setPath("/");
//			  cookie.setMaxAge(60*60*24*365);
//			  response.addCookie(cookie);
			  String language = WebCookie.getLanguage(request);
			  if("en".equals(language)){
				  return "redirect:"+GetServerPathUtil.getServerPath()+"/en/detail.html?orderId="+orderId;
			  }else{
				  return "redirect:"+GetServerPathUtil.getServerPath()+"/rfq/"+orderId;
			  }
			 
	 
	  }
	  
	  
	  /**
	   * 
	   * @param model
	   * @param request
	   * @param response
	   * @return
	   */
//	  @RequestMapping("/finishedDetails.do")
//	  public String  finishedDetails(HttpServletRequest request,HttpServletResponse response){
//		  
//		    String orderId = request.getParameter("orderId");
//		  			  
//			  //询盘号存放cookie
//			  Cookie cookie = new Cookie("_pid",orderId);
//			  cookie.setPath("/");
//			  cookie.setMaxAge(60*60*24*365);
//			  response.addCookie(cookie);
//			  return "redirect:/zh/supplier_big_goods.html";
//	 
//	  }
	  
	  
	  
	  
	  /**
	   * 
	   * @param model
	   * @param request
	   * @param response
	   * @return
	   */
//	  @RequestMapping("/quoteDetails.do")
//	  public String  quoteDetails(HttpServletRequest request,HttpServletResponse response){
//		  String orderId = request.getParameter("orderId");
//		  //询盘号存放session
//		  Cookie cookie = new Cookie("_pid",orderId);
//		  cookie.setPath("/");
//		  cookie.setMaxAge(60*60*24*365);
//		  response.addCookie(cookie);
//		  return "redirect:/zh/offer_detail.html";
//	  }
	  
	  
	  /**
	   * 查看产品详情（对比）
	   * @param request
	   * @param response
	   * @return
	   */
	  @ResponseBody
      @RequestMapping("/queryInquiryDetails.do")
      public JsonResult<Map<String,Object>> queryInquiryDetails(HttpServletRequest request,HttpServletResponse response){		  
		  	try {

				if(StringUtils.isBlank(request.getParameter("orderId"))){
					return new JsonResult<Map<String,Object>>(1,"未获取到询盘编号");	
				}
				//获取语言
				String lan = WebCookie.getLanguage(request);
				Integer orderId = Integer.parseInt(request.getParameter("orderId"));
				String factoryId = WebCookie.getFactoryId(request);
				Integer isVip = WebCookie.getVip(request);

					  QuoteInfo quoteInfo = quoteInfoService.queryByOrderId(orderId); 					  
					  List<QuoteMessage> quoteMessages = quoteMessageService.queryMessageByFactoryId(factoryId,orderId);
					  List<QuoteProduct> products = quoteProductService.queryByOrderId(orderId);	
					  int compareProcess = ConditionScreening.compareProcess(quoteInfo.getMainProcess(), request);
					  int compareQualification = ConditionScreening.compareQualification(quoteInfo.getQualification(), request);
					  int compareState = ConditionScreening.compareState(quoteInfo.getQuoteLocation(), request);
					  int compareEquipment = 1;
					  if(StringUtils.isNotBlank(quoteInfo.getEquipmentKeywords())){
						  compareEquipment = equipmentService.selectByEquipmentName(quoteInfo.getEquipmentKeywords(),factoryId);
					  }
					  int compareProduct = 1;
					  if(StringUtils.isNotBlank(quoteInfo.getProductKeywords())){
						  compareProduct = productLibraryService.selectCountByFIdAndPName(factoryId, quoteInfo.getProductKeywords());
					  }
					  
					  //查询是否属于被邀请报价的客户
				      Boolean flag = false;	  
					  if(StringUtils.isNotBlank(quoteInfo.getInviteFactory())){
						  String inviteFactory = quoteInfo.getInviteFactory();
						  String[] split = inviteFactory.split(",");
						  for (String string : split) {
							  if(string.equals(factoryId)){
								  flag = true;	  
							  }
						  }
					  }
					  
					  
					  //查询订询盘是否收藏
					  int isCollect = collectTabService.queryByOrderIdAndFactoryId(factoryId, orderId);


				SupplierQuoteInfo supplierQuoteInfo = supplierQuoteInfoService.queryByOrderIdAndFactoryId(orderId, factoryId);
				SupplierQuoteInfo orderSupplierQuote = null;
						//当报价完成后显示所有报价对比
						Integer rank = 1;                                   //排名
				        Double differPrice = 0.0;
						List<SupplierQuoteInfo> quotes = new ArrayList<SupplierQuoteInfo>();
						if(supplierQuoteInfo != null){
//							if(supplierQuoteInfo.getQuoteStatus() == QuoteOrderStatusEnum.REFUSE_ORDER.getCode()){
								String qualificationList = "";
								List<Qualification> qualification = qualificationService.queryByFactory(supplierQuoteInfo.getFactoryId());
								for (Qualification qualification2 : qualification) {
									qualificationList +=qualification2.getQualificationName() + ",";
								}
								if(!"".equals(qualificationList)){
									qualificationList = qualificationList.substring(0, qualificationList.length()-1);
									supplierQuoteInfo.setQualificationNameList(qualificationList);
								}
								Double myPrice = supplierQuoteInfo.getTotalAmount();
								Double minPrice = 0.0;               //最低工厂价格
								quotes = supplierQuoteInfoService.queryByOrderId(orderId);
								for (SupplierQuoteInfo supplierQuoteInfo2 : quotes) {
									if(!factoryId.equals(supplierQuoteInfo2.getFactoryId())){
										Double totalAmount = supplierQuoteInfo2.getTotalAmount();
										if(myPrice - totalAmount > 0){
											rank++;
										}
										//初始直接赋值当前价格
										if(minPrice == 0.0){
											minPrice = totalAmount;
										}else if(minPrice - totalAmount > 0){
											minPrice = totalAmount;
										}
										//获取资格认证列表
										List<Qualification> otherFactoryQualification = qualificationService.queryByFactory(supplierQuoteInfo2.getFactoryId());
										String otherFactoryQualificationList = "";
										for (Qualification qualification2 : otherFactoryQualification) {
											otherFactoryQualificationList +=qualification2.getQualificationName();
										}
										if(!"".equals(otherFactoryQualificationList)){
											otherFactoryQualificationList = otherFactoryQualificationList.substring(0, otherFactoryQualificationList.length()-1);
											supplierQuoteInfo2.setQualificationNameList(otherFactoryQualificationList);
										}
									}

								}

								//如果当前rank为第一，则当前是最低价
								if(rank == 1){
									differPrice = 0.0;
								}else{
									differPrice = myPrice - minPrice;
									differPrice = new BigDecimal(differPrice).setScale(0,BigDecimal.ROUND_UP).doubleValue();
								}


								//如果项目已完成，则和下单工厂比价
							    String orderQualificationList = "";
								if(quoteInfo.getOrderStatus() == OrderStatusEnum.CONFIRM.getCode() || quoteInfo.getOrderStatus() == OrderStatusEnum.PROCESS.getCode()){
									if(quoteInfo.getConfirmFactoryId() != factoryId){
										    orderSupplierQuote = supplierQuoteInfoService.queryByOrderIdAndFactoryId(orderId, quoteInfo.getConfirmFactoryId());
											//获取资格认证列表
											List<Qualification> qualifications = qualificationService.queryByFactory(orderSupplierQuote.getFactoryId());
											for (Qualification qualification2 : qualifications) {
												orderQualificationList +=qualification2.getQualificationName() + ",";
											}
											if(!"".equals(orderQualificationList)){
												orderQualificationList = qualificationList.substring(0, orderQualificationList.length()-1);
												supplierQuoteInfo.setQualificationNameList(orderQualificationList);
											}

										    Double totalAmount = orderSupplierQuote.getTotalAmount();
											differPrice = myPrice - totalAmount;
											differPrice = new BigDecimal(differPrice).setScale(0,BigDecimal.ROUND_UP).doubleValue();
									}
								}


							}
//						}

                      //查询历史报价
					  List<List<SupplierQuoteProduct>> supplierQuoteProducts = new ArrayList<List<SupplierQuoteProduct>>();
					  List<SupplierQuoteProduct> supplierQuoteProduct = supplierQuoteProductService.selectQuoteGroupByQuoteId(factoryId, orderId);
                      for (SupplierQuoteProduct supplierQuoteProduct2 : supplierQuoteProduct) {
                    	  List<SupplierQuoteProduct> list = supplierQuoteProductService.selectQuoteList(supplierQuoteProduct2.getSupplierQuoteId(), factoryId, orderId);
                    	  supplierQuoteProducts.add(list);
					  }
                      
                      FactoryInfo factoryInfo = new FactoryInfo();
                      if(StringUtils.isNotBlank(factoryId)){
                    	  factoryInfo = factoryInfoService.selectFactoryInfo(factoryId);
                      }

                      //如果是英文，则读取英文版询盘
					if("en".equals(lan)){
						quoteInfo = quoteInfoService.queryByOrderIdEn(orderId);
						products = quoteProductService.queryByOrderIdEn(orderId);
					}

                      //保存查询日志
				     InquiryClickLog inquiryClickLog = new InquiryClickLog();
					 if(StringUtils.isNotBlank(factoryId)){
						 inquiryClickLog.setFactoryId(factoryId);
						 inquiryClickLog.setClickTime(new Date());
						 inquiryClickLog.setUsername(WebCookie.getUserName(request));
						 inquiryClickLogService.insertSelective(inquiryClickLog);
					 }



				      Map<String, Object> map = new HashMap<String, Object>();
					  map.put("quoteMessages", quoteMessages);
					  map.put("quoteInfo", quoteInfo);
					  map.put("products", products);
					  map.put("compareProcess", compareProcess);
					  map.put("compareQualification", compareQualification);
					  map.put("compareState", compareState);
					  map.put("compareEquipment", compareEquipment);
					  map.put("compareProduct", compareProduct);
					  map.put("isVip", isVip);
					  map.put("isCollect", isCollect);
					  map.put("supplierQuoteProducts", supplierQuoteProducts);
					  map.put("supplierQuoteProduct", supplierQuoteProduct);
					  map.put("supplierQuoteInfo", supplierQuoteInfo);
					  map.put("inviteFlag", flag);
					  map.put("factoryInfo", factoryInfo);
				      map.put("rank", rank);
				      map.put("supplierQuotes", quotes);
				      map.put("differPrice", differPrice);
				      map.put("orderSupplierQuote", orderSupplierQuote);

					  return new JsonResult<Map<String,Object>>(map);
			} catch (Exception e) {
				logger.error("==========inquiry   queryInquiryDetails=========",e);
				return new JsonResult<Map<String,Object>>(1,"查询失败");
			}
		  	
	  }
	  
	  
	  /**
	   * 查询报价详情
	   * @param request
	   * @param response
	   * @return
	   */
	  @ResponseBody
      @RequestMapping("/queryQuoteDetails.do")
      public JsonResult<Map<String,Object>> queryQuoteDetails(HttpServletRequest request,HttpServletResponse response){		  
		  	try {
				if(StringUtils.isBlank(request.getParameter("orderId"))){
					return new JsonResult<Map<String,Object>>(1,"未获取到询盘编号");	
				}
				Integer orderId = Integer.parseInt(request.getParameter("orderId"));
				String factoryId = WebCookie.getFactoryId(request);
				Integer isVip = WebCookie.getVip(request);
				  if(StringUtils.isBlank(factoryId)){
					  return new JsonResult<Map<String,Object>>(2,"未获取到登录信息");
				  }else{
					  QuoteInfo quoteInfo = quoteInfoService.queryByOrderId(orderId); 					  
					  List<QuoteMessage> quoteMessages = quoteMessageService.queryMessageByFactoryId(factoryId,orderId);
					  List<QuoteProduct> products = quoteProductService.queryByOrderId(orderId);	
					  int compareProcess = ConditionScreening.compareProcess(quoteInfo.getMainProcess(), request);
					  int compareQualification = ConditionScreening.compareQualification(quoteInfo.getQualification(), request);
					  int compareState = ConditionScreening.compareState(quoteInfo.getQuoteLocation(), request);
					  int compareEquipment = 1;
					  if(StringUtils.isNotBlank(quoteInfo.getEquipmentKeywords())){
						  compareEquipment = equipmentService.selectByEquipmentName(quoteInfo.getEquipmentKeywords(),factoryId);
					  }	
					  int compareProduct = 1;
					  if(StringUtils.isNotBlank(quoteInfo.getProductKeywords())){
						  compareProduct = productLibraryService.selectCountByFIdAndPName(factoryId, quoteInfo.getProductKeywords());
					  }
					  //查询订询盘是否收藏
					  int isCollect = collectTabService.queryByOrderIdAndFactoryId(factoryId, orderId);
					  
					  SupplierQuoteInfo supplierQuoteInfo = supplierQuoteInfoService.queryByOrderIdAndFactoryId(orderId, factoryId);
					 
					  //当供应商报价被拒绝后，显示报价对比
					  Integer rank = 1;                                   //排名
					  List<SupplierQuoteInfo> quotes = new ArrayList<SupplierQuoteInfo>();
					  if(supplierQuoteInfo != null){
						  if(supplierQuoteInfo.getQuoteStatus() == QuoteOrderStatusEnum.REFUSE_ORDER.getCode()){
							  String qualificationList = "";
							  List<Qualification> qualification = qualificationService.queryByFactory(supplierQuoteInfo.getFactoryId());
							  for (Qualification qualification2 : qualification) {
								  qualificationList +=qualification2.getQualificationName() + ",";
							  }
							  if(!"".equals(qualificationList)){
								  qualificationList = qualificationList.substring(0, qualificationList.length()-1);
								  supplierQuoteInfo.setQualificationNameList(qualificationList);
							  }
							  Double myPrice = supplierQuoteInfo.getTotalAmount();
							  quotes = supplierQuoteInfoService.queryByOrderId(orderId);
							  for (SupplierQuoteInfo supplierQuoteInfo2 : quotes) {
								  if(!factoryId.equals(supplierQuoteInfo2.getFactoryId())){
									  Double totalAmount = supplierQuoteInfo2.getTotalAmount();
									  if(myPrice - totalAmount > 0){
										  rank++;
									  }
									  //获取资格认证列表
									  List<Qualification> otherFactoryQualification = qualificationService.queryByFactory(supplierQuoteInfo2.getFactoryId());
									  String otherFactoryQualificationList = "";
									  for (Qualification qualification2 : otherFactoryQualification) {
										  otherFactoryQualificationList +=qualification2.getQualificationName();
									  }
									  if(!"".equals(otherFactoryQualificationList)){
										  otherFactoryQualificationList = otherFactoryQualificationList.substring(0, otherFactoryQualificationList.length()-1);
										  supplierQuoteInfo2.setQualificationNameList(otherFactoryQualificationList);
									  }
								  }
							  }
						  }
					  }

					  
					  List<List<SupplierQuoteProduct>> supplierQuoteProducts = new ArrayList<List<SupplierQuoteProduct>>();
					  List<SupplierQuoteProduct> supplierQuoteProduct = supplierQuoteProductService.selectQuoteGroupByQuoteId(factoryId, orderId);
                      for (SupplierQuoteProduct supplierQuoteProduct2 : supplierQuoteProduct) {
                    	  List<SupplierQuoteProduct> list = supplierQuoteProductService.selectQuoteList(supplierQuoteProduct2.getSupplierQuoteId(), factoryId, orderId);
                    	  supplierQuoteProducts.add(list);
					  }
					  
					  
					  Map<String, Object> map = new HashMap<String, Object>();
					  map.put("quoteMessages", quoteMessages);
					  map.put("quoteInfo", quoteInfo);
					  map.put("products", products);
					  map.put("compareProcess", compareProcess);
					  map.put("compareQualification", compareQualification);
					  map.put("compareState", compareState);
					  map.put("compareEquipment", compareEquipment);
					  map.put("compareProduct", compareProduct);
					  map.put("isVip", isVip);
					  map.put("isCollect", isCollect);
					  map.put("supplierQuoteInfo", supplierQuoteInfo);
					  map.put("supplierQuoteProducts", supplierQuoteProducts);
					  map.put("supplierQuoteProduct", supplierQuoteProduct);
					  map.put("rank", rank);
					  map.put("supplierQuotes", quotes);
					  
					  return new JsonResult<Map<String,Object>>(map);
				  }
			} catch (Exception e) {
				logger.error("==========inquiry   queryQuoteDetails=========",e);
				return new JsonResult<Map<String,Object>>(1,"查询失败");
			}
		  	
	  }
	  
	  
	  
	  
	  /**
	   * 添加询盘消息
	   * @param request
	   * @param response
	   * @return
	   */
	  @ResponseBody
      @RequestMapping("/addQuoteMessage.do")
      public JsonResult<List<QuoteMessage>> addQuoteMessage(HttpServletRequest request,HttpServletResponse response){	
		  
		  try {
		  	  Integer orderId = null;
			  if(StringUtils.isNotBlank(request.getParameter("orderId"))){
				  orderId = Integer.parseInt(request.getParameter("orderId"));
			  }
			  String csgOrderId = request.getParameter("csgOrderId");
			  String factoryId = WebCookie.getFactoryId(request);
			  if(StringUtils.isBlank(factoryId)){
				  
	    		  //客户登录信息存放到session
	    		  HttpSession session = request.getSession();
	    		  session.setAttribute("historyUrl","/rfq/"+request.getParameter("orderId")+"");
	    		  session.setMaxInactiveInterval(60*10);
				  return new JsonResult<List<QuoteMessage>>(2,"未获取到登录信息");
			  }else{
				  String message = request.getParameter("message");
				  String filePath = request.getParameter("filePath");
				  String fileName = request.getParameter("fileName");
				  QuoteMessage quoteMessage = new QuoteMessage();
				  quoteMessage.setFactoryId(factoryId);
				  quoteMessage.setMessageDetails(message);
				  quoteMessage.setOrderId(orderId);
				  quoteMessage.setFilePath(filePath);
				  quoteMessage.setFileName(fileName);
				  quoteMessage.setSendTime(DateFormat.format());
				  quoteMessage.setCsgOrderId(csgOrderId);
				  //采购重点消息（当该字段为1时 表示为采购重点消息）
				  if(StringUtils.isNotBlank(request.getParameter("isImportant"))){
					  quoteMessage.setIsImportant(Integer.parseInt(request.getParameter("isImportant")));
				  }
				  String receiverId = request.getParameter("factoryId");
                  QuoteInfo quoteInfo = quoteInfoService.queryByOrderId(orderId);
				  String customerId = null;   //询盘客户名
				  Integer quoteArea = 1;      //发盘语言
				  if(quoteInfo!=null){
					  customerId = quoteInfo.getCustomerId();
					  csgOrderId = quoteInfo.getCsgOrderId();
					  quoteArea = quoteInfo.getQuoteArea();
					  receiverId = "0";                      //所有返单项目全部是自营
				  }
                  //保存到提醒消息表
            	  NoteMessage note = new NoteMessage();
				  //发送消息时同步到内部报价（工厂消息“新消息”  报价回复 “新回复”）
				  RpcSynNews rpc = new RpcSynNews();
            	  if(factoryId.equals(customerId)){
                	 quoteMessage.setReplyStatus(0);
                	 note.setReceiverId(receiverId);
               		 note.setSendId(factoryId);
               		 note.setOrderId(orderId);
               		 note.setMessageTitle(orderId+"询盘有新的消息");
               		 note.setMessageDetails(message);
               		 note.setMessageType(MessageTypeEnum.QUOTE_MESSAGE.getCode());
					  //判断自营还是第三方询盘跳转
					  if(StringUtils.isNotBlank(csgOrderId)){
						  note.setUrl(GetServerPathUtil.getServerPath()+"/rfq/"+orderId);
					  }else{
						  note.setUrl(GetServerPathUtil.getServerPath()+"/rfq/"+orderId);
					  }
               		 note.setCreateTime(DateFormat.format());
					  //如果是内部询盘，回复后回调到内部报价系统
					  if(csgOrderId != null){
						  rpc.sendRequest(null,csgOrderId,"新回复",null);
					  }

                  }else{
                	  quoteMessage.setReplyStatus(1);                 	  
                	  note.setReceiverId(receiverId);
            		  note.setSendId(factoryId);
            		  note.setOrderId(orderId);
            		  note.setMessageTitle((orderId==null?"":orderId)+(csgOrderId == null ? "" : "("+csgOrderId+")")+"询盘有新的供应商消息");
            		  note.setMessageDetails(message);
            		  note.setFilePath(filePath);
            		  note.setFileName(fileName);
            		  note.setMessageType(MessageTypeEnum.QUOTE_MESSAGE.getCode());
					  //如果询盘为国外询盘（0）调转英文版
					  //如果询盘为国内询盘（1）调转中文版
            		  if(quoteArea == 0){
						  note.setUrl(GetServerPathUtil.getServerPath()+"/en/purchase_supplier.html?factoryId="+factoryId+"&orderId="+orderId+"");
					  }else{
						  note.setUrl(GetServerPathUtil.getServerPath()+"/zh/purchase_supplier.html?factoryId="+factoryId+"&orderId="+orderId+"");
					  }
            		  note.setCreateTime(DateFormat.format());
					  //如果是内部询盘，回复后回调到内部报价系统
					  if(csgOrderId != null) {
						  rpc.sendRequest(null, csgOrderId, "新消息", null);
					  }
                  }
              
                  quoteMessageService.insert(quoteMessage,note);
				  List<QuoteMessage> quoteMessages = quoteMessageService.queryMessageByCsgOrderId(customerId,factoryId,orderId,csgOrderId);
				  return new JsonResult<List<QuoteMessage>>(quoteMessages);
			  }  
			} catch (Exception e) {
				logger.error("==========inquiry   addQuoteMessage=========",e);
				return new JsonResult<List<QuoteMessage>>(1,"查询失败");
			}
		  
	  }
	  
	  
	  
	  /**
	   * 跳转厂家报价页面
	   * @param request
	   * @param response
	   * @return
	   */
      @RequestMapping("/toOffer.do")
      public String toOffer(HttpServletRequest request,HttpServletResponse response){			  		  		  		  
		  return "redirect:"+GetServerPathUtil.getServerPath()+"/zh/offer.html";
	  }
	  
	  
	  
	  
	  
	  /**
	   * 供应商报价
	   * @param request
	   * @param response
	   * @return
	   */
      @ResponseBody
	  @RequestMapping("/addSupplierQuote.do")
      public JsonResult<Map<String, Object>> addSupplierQuote(HttpServletRequest request,HttpServletResponse response){	
		  		
		  	try {
				    if(StringUtils.isBlank(request.getParameter("orderId"))){
						return new JsonResult<Map<String, Object>>(1,"未获取到询盘编号");	
				    }
				    Integer orderId = Integer.parseInt(request.getParameter("orderId"));
				    String factoryId = WebCookie.getFactoryId(request);		
					 if(StringUtils.isBlank(factoryId)){
						 
			    		  //客户登录信息存放到session
			    		  HttpSession session = request.getSession();
			    		  session.setAttribute("historyUrl","/rfq/"+request.getParameter("orderId")+"");
			    		  session.setMaxInactiveInterval(60*10);
						 
						  return new JsonResult<Map<String, Object>>(2,"未获取到登录信息");
					  }else{
						  
//						QuoteInfo quoteInfo = quoteInfoService.queryByOrderId(orderId);
//						FactoryInfo factoryInfo = factoryInfoService.selectFactoryInfo(factoryId);
						//判断是否是会员 
//					    if(factoryInfo.getIsVip() == 101){			    		 
//
//					    }else{
//					    	String publishDate = quoteInfo.getPublishDate();
//					    	if(StringUtils.isNotBlank(publishDate)){
//					    		int calHours = DateFormat.calHours(publishDate);
//					    		if(calHours < 48 && calHours == 48){
//					    			return new JsonResult<Map<String, Object>>(3,"vip已过期");	
//					    		}
//					    	}
//					    }						  
						  
						 String paymentTerm = request.getParameter("paymentTerm"); 
						 SupplierQuoteInfo supplierQuoteInfo = new SupplierQuoteInfo();
						 if(StringUtils.isNotBlank(request.getParameter("quoteRemark"))){
							 supplierQuoteInfo.setQuoteRemark(request.getParameter("quoteRemark"));
						 }
						 if(StringUtils.isNotBlank(request.getParameter("attachmentPath"))){
							 supplierQuoteInfo.setAttachmentPath(request.getParameter("attachmentPath"));
						 }
						 if(StringUtils.isNotBlank(request.getParameter("quoteReasons"))){
							 supplierQuoteInfo.setQuoteReasons(request.getParameter("quoteReasons"));
						 }
						 if(StringUtils.isNotBlank(request.getParameter("quoteStatus"))){
							 supplierQuoteInfo.setQuoteStatus(Integer.parseInt(request.getParameter("quoteStatus")));
						 }
						 if(StringUtils.isNotBlank(paymentTerm)){
							 supplierQuoteInfo.setPaymentTerm(paymentTerm);
						 }
						 if(StringUtils.isNotBlank(request.getParameter("paymentRemark"))){
							 supplierQuoteInfo.setPaymentRemark(request.getParameter("paymentRemark"));
						 }
						 if(StringUtils.isNotBlank(request.getParameter("validityDays"))){
							 supplierQuoteInfo.setValidityDays(Integer.parseInt(request.getParameter("validityDays")));
						 }
						 if(StringUtils.isNotBlank(request.getParameter("fileName"))){
							 supplierQuoteInfo.setAttachmentName(request.getParameter("fileName"));
						 }
						 if(StringUtils.isNotBlank(request.getParameter("priceType"))){
							 supplierQuoteInfo.setPriceType(Integer.parseInt(request.getParameter("priceType")));
						 }
						 String productList = request.getParameter("productList");
						 String productIds = request.getParameter("productIds");
						 String productRemarks = request.getParameter("productRemarks");
						 supplierQuoteInfo.setCreateTime(new Date());
						 supplierQuoteInfo.setQuoteDate(new Date());
						 supplierQuoteInfo.setOrderId(orderId);
						 supplierQuoteInfo.setFactoryId(factoryId);		
						 logger.debug("报价产品价格"+productList);
						 Map<String, Object> map = supplierQuoteInfoService.insert(supplierQuoteInfo, productList,productIds,productRemarks);
						 return new JsonResult<Map<String, Object>>(map);
					  }
				} catch (Exception e) {
					logger.error("==========inquiry   addSupplierQuote=========",e);
					return new JsonResult<Map<String, Object>>(1,"保存失败");
				} 
	     }
	  
	  /**
	   * 验证报价是否已满
	   * @param request
	   * @param response
	   * @return
	   */
	  @ResponseBody
	  @RequestMapping("/queryQuoteAmount.do")
      public JsonResult<Integer> queryQuoteAmount(HttpServletRequest request,HttpServletResponse response){
		  try {
			if(StringUtils.isNotBlank(request.getParameter("orderId"))){
				  Integer orderId = Integer.parseInt(request.getParameter("orderId"));
				  int totalQuoteFactory = supplierQuoteInfoService.totalQuoteFactory(orderId);	
				  return new JsonResult<Integer>(totalQuoteFactory);
			  }else{
				  return new JsonResult<Integer>(1,null);
			  }
		  } catch (NumberFormatException e) {
				logger.error("==========inquiry   queryQuoteAmount=========",e);
			return new JsonResult<Integer>(1,null);
		  }

	  }
	  
	  
	  /**
	   * 供应商报价详情
	   * @param request
	   * @param response
	   * @return
	   */
	  @ResponseBody
	  @RequestMapping("/querySupplierQuoteDetails.do")
      public JsonResult<Map<String, Object>> querySupplierQuoteDetails(HttpServletRequest request,HttpServletResponse response){	
		  		
		  	try {
				  if(StringUtils.isBlank(request.getParameter("orderId"))){
						return new JsonResult<Map<String, Object>>(1,"未获取到询盘编号");	
				  }
				  Integer orderId = Integer.parseInt(request.getParameter("orderId"));
				  String factoryId = WebCookie.getFactoryId(request);
				 if(StringUtils.isBlank(factoryId)){
					  return new JsonResult<Map<String, Object>>(2,"未获取到登录信息");
				  }else{
					 SupplierQuoteInfo supplierQuoteInfo = supplierQuoteInfoService.queryByOrderIdAndFactoryId(orderId, factoryId);
					 List<SupplierQuoteProduct> quoteProducts = supplierQuoteProductService.queryBySupplierQuoteId(supplierQuoteInfo.getId());
					 Map<String, Object> map = new HashMap<String, Object>();
					 map.put("supplierQuoteInfo", supplierQuoteInfo);
					 map.put("quoteProducts", quoteProducts);
					 return new JsonResult<Map<String, Object>>(map);
				  }
				} catch (Exception e) {
					logger.error("==========inquiry   querySupplierQuoteDetails=========",e);
					return new JsonResult<Map<String, Object>>(1,"保存失败");
				}
	     }
	  
	  
	  
	  
	  /**
	   * 查询大货报价详情
	   * @Title queryBigProductDetails 
	   * @Description 
	   * @param request
	   * @param response
	   * @return
	   * @return JsonResult<Map<String,Object>>
	   */
	  @ResponseBody
	  @RequestMapping("/queryBigProductDetails.do")
      public JsonResult<Map<String, Object>> queryBigProductDetails(HttpServletRequest request,HttpServletResponse response){
		  
		      try {
				  if(StringUtils.isBlank(request.getParameter("orderId"))){
						return new JsonResult<Map<String, Object>>(1,"未获取到询盘编号");	
				  }
				  Integer orderId = Integer.parseInt(request.getParameter("orderId"));
				  String factoryId = WebCookie.getFactoryId(request);
				  if(StringUtils.isBlank(factoryId)){
					  return new JsonResult<Map<String, Object>>(2,"未获取到登录信息");
				  }else{
					  QuoteInfo quoteInfo = quoteInfoService.queryByOrderId(orderId);
					  QuoteBigProducts quoteBigProducts = quoteBigProductsService.selectBySupplierId(factoryId, orderId);
					  List<QuoteBigProductsTab> quoteBigProductsTab = new ArrayList<QuoteBigProductsTab>();
					  if(!(quoteBigProducts == null || "".equals(quoteBigProducts))){
						  quoteBigProductsTab = quoteBigProductsTabService.selectByProductId(quoteBigProducts.getId());  
					  }
					 
					   //查询周报
					  List<QuoteWeeklyReport> quoteWeeklyReports = quoteWeeklyReportService.queryByOrderIdAndSupplier(orderId);
					  
				      Map<String, Object> map = new HashMap<String, Object>();		
				      map.put("quoteInfo", quoteInfo);
				      map.put("quoteBigProducts", quoteBigProducts);
				      map.put("quoteBigProductsTab", quoteBigProductsTab);
				      map.put("quoteWeeklyReports", quoteWeeklyReports);
					  return new JsonResult<Map<String, Object>>(map);  
				  }
			} catch (NumberFormatException e) {
				logger.error("==========inquiry   queryBigProductDetails=========",e);
				return new JsonResult<Map<String, Object>>(1,"查询失败");
			} catch (Exception e) {
				logger.error("==========inquiry   queryBigProductDetails=========",e);
				return new JsonResult<Map<String, Object>>(1,"查询失败");
			}
	  }
	  
	  
	  
	  /**
	   * 更新大货状态
	   * @Title updateBigProduct 
	   * @Description 
	   * @param request
	   * @param response
	   * @return
	   * @return JsonResult<String>
	   */
	  @ResponseBody
	  @RequestMapping("/updateBigProduct.do")
      public JsonResult<String> updateBigProduct(HttpServletRequest request,HttpServletResponse response){
		  
		  try {
			  if(StringUtils.isBlank(request.getParameter("orderId"))){
					return new JsonResult<String>(1,"未获取到询盘编号");	
			  }
			  Integer orderId = Integer.parseInt(request.getParameter("orderId"));
			  String factoryId = WebCookie.getFactoryId(request);
			  if(StringUtils.isBlank(factoryId)){
				  return new JsonResult<String>(2,"未获取到登录信息");
			  }else{
				  QuoteBigProducts quoteBigProducts = quoteBigProductsService.selectBySupplierId(factoryId, orderId);
				  if(StringUtils.isNotBlank(request.getParameter("supplierRemark"))){
					  quoteBigProducts.setSupplierRemark(request.getParameter("supplierRemark"));
				  }
				  if(StringUtils.isNotBlank(request.getParameter("isSupplierAccept"))){
					  quoteBigProducts.setIsSupplierAccept(Integer.parseInt(request.getParameter("isSupplierAccept")));
				  }
				  quoteBigProducts.setSupplierReplyTime(DateFormat.format());
				  quoteBigProductsService.updateByPrimaryKeySelective(request,quoteBigProducts);
				  return new JsonResult<String>(0,"保存成功");
			  }
		} catch (NumberFormatException e) {
			logger.error("==========inquiry   updateBigProduct=========",e);
			return new JsonResult<String>(1,"保存失败");
		} catch (Exception e) {
			logger.error("==========inquiry   updateBigProduct=========",e);
			return new JsonResult<String>(1,"保存失败");
		}		
		  
		  
	  }
	  
	  
	  
	  /**
	   * 删除周报
	   * @Title updateBigProduct 
	   * @Description 
	   * @param request
	   * @param response
	   * @return
	   * @return JsonResult<String>
	   */
	  @ResponseBody
	  @RequestMapping("/deleteById.do")
      public JsonResult<String> deleteById(HttpServletRequest request,HttpServletResponse response){
		  
		  try {

			  String factoryId = WebCookie.getFactoryId(request);
			  if(StringUtils.isBlank(factoryId)){
				  return new JsonResult<String>(2,"未获取到登录信息");
			  }else{
				  if(StringUtils.isBlank(request.getParameter("id"))){
					  return new JsonResult<String>(1,"未获取到id");  
				  }
				  Integer id = Integer.parseInt(request.getParameter("id"));
				  quoteWeeklyReportService.deleteByPrimaryKey(id);
				  return new JsonResult<String>(0,"删除成功");  
			  }
			} catch (NumberFormatException e) {
				logger.error("==========inquiry   deleteById=========",e);
				return new JsonResult<String>(1,"未获取到询盘信息");  
			} catch (Exception e) {
				logger.error("==========inquiry   deleteById=========",e);
				return new JsonResult<String>(1,"删除失败");  
			}
		  
	  }
	  
	  
	  
	  
	  /**
	   * 保存进度报告、材质证明、检测报告  2018.10.29 edit
	   * @Title insertReportPhoto 
	   * @Description
	   * @param request
	   * @param response
	   * @return
	   * @return JsonResult<String>
	   */
/*	  @ResponseBody
	  @RequestMapping("/insertReport.do")
      public JsonResult<Map<String,Object>> insertReport(HttpServletRequest request,HttpServletResponse response){
		  
		  try {
			String factoryId = WebCookie.getFactoryId(request);
			  if(StringUtils.isBlank(factoryId)){
				  return new JsonResult<Map<String,Object>>(2,"未获取到登录信息");
			  }else{
				  if(StringUtils.isBlank(request.getParameter("orderId"))){
					  return new JsonResult<Map<String,Object>>(1,"未获取到询盘信息");  
				  }
				  Integer orderId = Integer.parseInt(request.getParameter("orderId")); 
				  Integer fileType = Integer.parseInt(request.getParameter("fileType"));
				    *//*
				     * 进度报告   /100012/progress/
				     *//*
				    String path1 = UploadAndDownloadPathUtil.getQuoteFile() +  orderId + File.separator;
					 File file1 = new File(path1);
					 if  (!file1.exists()  && !file1 .isDirectory())      
					 {         
						 file1 .mkdir();     
					 }  
					 
					 //根据文件类型确定上传文件路径
			 		 String path2 = "";
			 		 if(fileType == ProjectStageEnum.PROGRESS_REPORT.getCode()){
			 			path2 = path1 + "progress" + File.separator;
			 		 }else if(fileType == ProjectStageEnum.MATERIAL_REPORT.getCode()){
			 			path2 = path1 + "material" + File.separator;
			 		 }else if(fileType == ProjectStageEnum.QUANLITY_REPORT.getCode()){
				 			path2 = path1 + "quanlity" + File.separator;
				 	 }
					 File file2 = new File(path2);
					 if  (!file2.exists()  && !file2 .isDirectory())      
					 {         
						 file2 .mkdir();     
					 }  
					 
					 //获取上传文件名
				    String drawingName = request.getParameter("fileName");             
                     //根据文件名获取上传文件的位置  数据库保存原始文件名称
			 		Map<String, String> multiFileUpload = OperationFileUtil.multiFileUpload_changename(request, path2);
			 		String fileName = "";
			 		if(!(multiFileUpload == null || multiFileUpload.size() == 0)){
			 			fileName = multiFileUpload.get(drawingName);
			 		} 
			 		Map<String,Object> map = new HashMap<String, Object>();
		            if(StringUtils.isNotBlank(fileName)){
					  QuoteWeeklyReport report = new QuoteWeeklyReport();
					  report.setOrderId(orderId);
					  report.setFileName(drawingName);
					  report.setDocumentPath(path2 + fileName);
					  report.setFileType(fileType);
					  report.setUploadDate(DateFormat.format()); 

					  
					  
					  quoteWeeklyReportService.insertSelective(request,report);
					  map.put("report", report);
		            }
                    
		            return new JsonResult<Map<String,Object>>(map);
			  }
			} catch (IOException e) {
				logger.error("==========inquiry   insertReport=========",e);
				return new JsonResult<Map<String,Object>>(1,"保存失败");
			}
	  }*/
	  
	  
	  
	  
	  
	  
	  
	  /**
	   * 批量保存周报图片
	   * @Title insertReportPhoto 
	   * @Description 
	   * @param request
	   * @param response
	   * @return
	   * @return JsonResult<Map<String,Object>>
	   */
	  @ResponseBody
	  @RequestMapping("/uploadPhotos.do")
      public JsonResult<String> uploadPhotos(HttpServletRequest request,HttpServletResponse response){
		  Map<String, String> fileNameMap = null;
		  try {
			  String factoryId = WebCookie.getFactoryId(request);
			  if(StringUtils.isBlank(factoryId)){
				  return new JsonResult<String>(2,"未获取到登录信息");
			  }else{
				  if(StringUtils.isBlank(request.getParameter("orderId"))){
					  return new JsonResult<String>(1,"未获取到询盘信息");  
				  }
				  Integer orderId = Integer.parseInt(request.getParameter("orderId")); 
				    /*
				     * 进度报告   /100012/progress/
				     */
				    String path1 = UploadAndDownloadPathUtil.getQuoteFile() +  orderId + File.separator;
					 File file1 = new File(path1);
					 if  (!file1.exists()  && !file1 .isDirectory())      
					 {         
						 file1 .mkdir();     
					 }  
					 
					 //判断上传图片路径是否存在
			 		 String path2 = path1 + "photo" + File.separator;
					 File file2 = new File(path2);
					 if  (!file2.exists()  && !file2 .isDirectory())      
					 {         
						 file2 .mkdir();     
					 }  
					             
                     //根据文件名获取上传文件的位置  数据库保存原始文件名称
			 		Map<String, Map<String, String>> multiFileUpload = OperationFileUtil.multiFileUpload2_changename(request, path2, path2, null);
			 		if (!(multiFileUpload == null || multiFileUpload.size() == 0)) {
						fileNameMap = multiFileUpload.get("filePaths");					
					}
			 		
			 		List<QuoteWeeklyReport> reports = new ArrayList<QuoteWeeklyReport>();
			 		if(fileNameMap != null && fileNameMap.size() != 0){
			 			Set<String> keySet = fileNameMap.keySet();
		 				   for (String key : keySet) {		 					   
		 					   String newName = fileNameMap.get(key);  
		 					   String photoCompressName = OperationFileUtil.changeFilenameZip(newName);
		 					   
		 					   if(StringUtils.isNotBlank(newName)){
		 						  QuoteWeeklyReport report = new QuoteWeeklyReport();
		 						  report.setOrderId(orderId);
		 						  report.setFileName(key);
		 						  report.setPhotoPath(UploadAndDownloadPathUtil.getQuoteFileStatic() + orderId + "/photo/" +newName);
		 						  report.setPhotoPathCompress(UploadAndDownloadPathUtil.getQuoteFileStatic() + orderId + "/photo/" + photoCompressName);
		 						  report.setFileType(FileTypeEnum.IMG.getCode());
		 						  reports.add(report);
		 			            }
		 				   }
                           //将上传信息存储session
		 				   HttpSession session = request.getSession();
		 				   session.setAttribute("photos", reports);		 	
		 	    		   session.setMaxInactiveInterval(60*60*24);
			 		}		 		
                    
		            return new JsonResult<String>(0,"上传成功");
			  }
			} catch (IOException e) {
				logger.error("==========inquiry   uploadPhotos=========",e);
				return new JsonResult<String>(1,"上传失败");
			} catch (Exception e) {
				logger.error("==========inquiry   uploadPhotos=========",e);
				return new JsonResult<String>(1,"上传失败");
			}
	  }
	  
	  
	  
	  /**
	   * 上传图片获取图片重命名名称
	   * @Title uploadPhoto 
	   * @Description 
	   * @param request
	   * @param response
	   * @return
	   * @return JsonResult<Map<String,Object>>
	   */
	  @ResponseBody
	  @RequestMapping("/uploadPhoto.do")
      public JsonResult<Map<String,Object>> uploadPhoto(HttpServletRequest request,HttpServletResponse response){
		  Map<String, String> fileNameMap = null;
		  Map<String,Object> map = new HashMap<String, Object>();
		  String filePath = null;
		  try {
			  String factoryId = WebCookie.getFactoryId(request);
			  if(StringUtils.isBlank(factoryId)){
				  return new JsonResult<Map<String,Object>>(2,"未获取到登录信息");
			  }else{
				  if(StringUtils.isBlank(request.getParameter("orderId"))){
					  return new JsonResult<Map<String,Object>>(1,"未获取到询盘信息");  
				  }
				  Integer orderId = Integer.parseInt(request.getParameter("orderId")); 
				    /*
				     * 进度报告   /100012/progress/
				     */
				    String path1 = UploadAndDownloadPathUtil.getQuoteFile() +  orderId + File.separator;
					 File file1 = new File(path1);
					 if  (!file1.exists()  && !file1 .isDirectory())      
					 {         
						 file1 .mkdir();     
					 }  
					 
					 //判断上传图片路径是否存在
			 		 String path2 = path1 + "photo" + File.separator;
					 File file2 = new File(path2);
					 if  (!file2.exists()  && !file2 .isDirectory())      
					 {         
						 file2 .mkdir();     
					 }  
					             
                     //根据文件名获取上传文件的位置  数据库保存原始文件名称
			 		Map<String, Map<String, String>> multiFileUpload = OperationFileUtil.multiFileUpload2_changename(request, path2, path2, null);
			 		if (!(multiFileUpload == null || multiFileUpload.size() == 0)) {
						fileNameMap = multiFileUpload.get("filePaths");					
					}
			 		
			 		if(fileNameMap != null && fileNameMap.size() != 0){
			 			Set<String> keySet = fileNameMap.keySet();
		 				   for (String key : keySet) {		 					   
		 					   String newName = fileNameMap.get(key);  		
		 					   filePath = UploadAndDownloadPathUtil.getQuoteFileStatic() + orderId + "/photo/" + OperationFileUtil.changeFilenameZip(newName);
		 					   map.put("filePath", filePath);
		 					   map.put("newName", newName);
		 				   }
			 		}		 		
                    
		            return new JsonResult<Map<String,Object>>(map);
			  }
			} catch (IOException e) {
				logger.error("==========inquiry   uploadPhoto=========",e);
				return new JsonResult<Map<String,Object>>(1,"上传失败");
			} catch (Exception e) {
				logger.error("==========inquiry   uploadPhoto=========",e);
				return new JsonResult<Map<String,Object>>(1,"上传失败");
			}
	  }
	  
	  
	  
	  /**
	   * 保存图片数据
	   * @Title insertPhotos 
	   * @Description
	   * @param request
	   * @param response
	   * @return
	   * @return JsonResult<List<QuoteWeeklyReport>>
	   */
//	  @ResponseBody
//	  @RequestMapping("/insertPhotos.do")
//      public JsonResult<String> insertPhotos(HttpServletRequest request,HttpServletResponse response){
//
//		  try {
//				String factoryId = WebCookie.getFactoryId(request);
//				  if(StringUtils.isBlank(factoryId)){
//					  return new JsonResult<String>(2,"未获取到登录信息");
//				  }else{
//
//					  //获取session存储的图片信息
//					   List<QuoteWeeklyReport> reports = null;
//					   HttpSession session = request.getSession();
//					   Object str = session.getAttribute("photos");
//					   if(!(str == null || "".equals(str))){
//						   reports = (List<QuoteWeeklyReport>)str;
//						   for (QuoteWeeklyReport quoteWeeklyReport : reports) {
//							   if(StringUtils.isNotBlank(request.getParameter("remark"))){
//								   quoteWeeklyReport.setRemark(request.getParameter("remark"));
//							   }
//							   quoteWeeklyReport.setUploadDate(DateFormat.format());
//						   }
//						   quoteWeeklyReportService.insertPhotosBatch(request,reports);
//					   }
//					   return new JsonResult<String>(0,"保存成功");
//				 }
//			} catch (NumberFormatException e) {
//				logger.error("==========inquiry   insertPhotos=========",e);
//				return new JsonResult<String>(1,"未获取到信息");
//			} catch (Exception e) {
//				logger.error("==========inquiry   insertPhotos=========",e);
//				return new JsonResult<String>(1,"保存失败");
//			}
//
//	  }
	  
	  
	  
	  
	  
	  /**
	   * 删除报告（进度报告、材质证明、检测报告）
	   * @Title deleteReport 
	   * @Description 
	   * @param request
	   * @param response
	   * @return
	   * @return JsonResult<String>
	   */
	  @ResponseBody
	  @RequestMapping("/deleteReport.do")
      public JsonResult<String> deleteReport(HttpServletRequest request,HttpServletResponse response){
		  
		  try {
			String factoryId = WebCookie.getFactoryId(request);
			  if(StringUtils.isBlank(factoryId)){
				  return new JsonResult<String>(2,"未获取到登录信息");
			  }else{
				  if(StringUtils.isBlank(request.getParameter("id"))){
					  return new JsonResult<String>(1,"未获取到登录信息");  
				  }else{
					  Integer id = Integer.parseInt(request.getParameter("id"));
					  quoteWeeklyReportService.deleteByPrimaryKey(id);
					  return new JsonResult<String>(0,"删除成功");
				  }
			  }
		} catch (NumberFormatException e) {
			logger.error("==========inquiry   deleteReport=========",e);
			return new JsonResult<String>(1,"未获取到ID信息");
		} catch (Exception e) {
			logger.error("==========inquiry   deleteReport=========",e);
			return new JsonResult<String>(1,"删除失败");
		}
		  
	  }
	  
	  
	  /**
	   * 根据id查询图片
	   * @Title queryPhotoById 
	   * @Description
	   * @param request
	   * @param response
	   * @return
	   * @return JsonResult<String>
	   */
	  @ResponseBody
	  @RequestMapping("/queryPhotoById.do")
      public JsonResult<QuoteWeeklyReport> queryPhotoById(HttpServletRequest request,HttpServletResponse response){

				 try {
					String factoryId = WebCookie.getFactoryId(request);
					  if(StringUtils.isBlank(factoryId)){
						  return new JsonResult<QuoteWeeklyReport>(2,"未获取到登录信息");
					  }else{
						  Integer id = Integer.parseInt(request.getParameter("id"));
						  QuoteWeeklyReport report = quoteWeeklyReportService.selectByPrimaryKey(id);
						  return new JsonResult<QuoteWeeklyReport>(report);
					  }
				} catch (NumberFormatException e) {					
					logger.error("==========inquiry   queryPhotoById=========",e);
					return new JsonResult<QuoteWeeklyReport>(1,"未获取到ID信息");
				} catch (Exception e) {
					logger.error("==========inquiry   queryPhotoById=========",e);
					return new JsonResult<QuoteWeeklyReport>(1,"查询失败");
				}
		  
		  
	  }
	  
	  
	  /**
	   * 根据id更新图片信息
	   * @Title updatePhotoById 
	   * @Description
	   * @param request
	   * @param response
	   * @return
	   * @return JsonResult<QuoteWeeklyReport>
	   */
	  @ResponseBody
	  @RequestMapping("/updatePhotoById.do")
      public JsonResult<String> updatePhotoById(HttpServletRequest request,HttpServletResponse response){
		  try {
				  String factoryId = WebCookie.getFactoryId(request);
				  if(StringUtils.isBlank(factoryId)){
					  return new JsonResult<String>(2,"未获取到登录信息");
				  }else{
					  Integer id = Integer.parseInt(request.getParameter("id"));
					  QuoteWeeklyReport report = quoteWeeklyReportService.selectByPrimaryKey(id);
					  if(StringUtils.isNotBlank(request.getParameter("originalName"))){
						  report.setFileName(request.getParameter("originalName"));
					  }
					  if(StringUtils.isNotBlank(request.getParameter("newName"))){
						  String newName = request.getParameter("newName");
						  report.setPhotoPath(UploadAndDownloadPathUtil.getQuoteFileStatic() + report.getOrderId() + "/photo/" +newName);
 						  report.setPhotoPathCompress(UploadAndDownloadPathUtil.getQuoteFileStatic() + report.getOrderId() + "/photo/" + OperationFileUtil.changeFilenameZip(newName));
 						  if(StringUtils.isNotBlank(request.getParameter("remark"))){
 							 report.setRemark(request.getParameter("remark"));
 						  }
 						  quoteWeeklyReportService.updateByPrimaryKeySelective(report);
					  }
					  return new JsonResult<String>(0,"更新成功");
				  }
			} catch (NumberFormatException e) {					
				logger.error("==========inquiry   updatePhotoById=========",e);
				return new JsonResult<String>(1,"未获取到ID信息");
			} catch (Exception e) {
				logger.error("==========inquiry   updatePhotoById=========",e);
				return new JsonResult<String>(1,"更新失败");
			}
	  
		  
	  }





	/**
	 * 查询所有询盘
	 * @Title
	 * @Description
	 * @param
	 * @param
	 * @return
	 * @return JsonResult<String>
	 */
	@ResponseBody
	@RequestMapping("/queryAll")
	public JsonResult<List<QuoteProduct>> queryAll() throws Exception {
		 List<QuoteProduct> quoteProducts = quoteProductService.queryAllProductGroupByOrderId(null, null);
		 for(QuoteProduct quoteProduct:quoteProducts){
              QuoteInfo quoteInfo = quoteInfoService.queryByOrderId(quoteProduct.getOrderId());
			  quoteInfoService.updateByPrimaryKey(quoteInfo);
			  List<QuoteProduct> quoteProducts1 = quoteProductService.queryByOrderId(quoteProduct.getOrderId());
			  for(QuoteProduct product:quoteProducts1){
                 quoteProductService.updateByPrimaryKeySelective(product);
			  }
		 }

		 JsonResult jsonResult = new JsonResult();
		 jsonResult.setState(0);
		 jsonResult.setData(quoteProducts);
		 return jsonResult;
	}


	@ResponseBody
	@RequestMapping("/translateFactory")
	public JsonResult<List<FactoryInfo>> translateFactory() throws Exception {
		JsonResult jsonResult = null;
		String userName = null;
		try {
			List<FactoryInfo> factoryInfos = factoryInfoService.queryFactoryList();
			for(FactoryInfo factoryInfo:factoryInfos){
				userName = factoryInfo.getUsername();
               factoryInfoService.updateByPrimaryKeySelective(factoryInfo);
            }
			jsonResult = new JsonResult();
			jsonResult.setState(0);
			jsonResult.setData(factoryInfos);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("用户名:"+userName);
		}
		return jsonResult;
	}

}
