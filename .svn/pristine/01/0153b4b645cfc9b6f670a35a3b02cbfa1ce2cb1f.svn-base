package com.cbt.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cbt.rpc.GetProjectCountByFactoryId;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cbt.entity.FactoryInfo;
import com.cbt.entity.NoteMessage;
import com.cbt.entity.QuoteBigProducts;
import com.cbt.entity.QuoteInfo;
import com.cbt.entity.QuoteProduct;
import com.cbt.entity.SysUser;
import com.cbt.enums.MessageTypeEnum;
import com.cbt.enums.OrderStatusEnum;
import com.cbt.enums.ProcessZhAndEnEnum;
import com.cbt.service.FactoryInfoService;
import com.cbt.service.FactoryUserService;
import com.cbt.service.NoteMessageService;
import com.cbt.service.QuoteBigProductsService;
import com.cbt.service.QuoteInfoService;
import com.cbt.service.QuoteProductService;
import com.cbt.service.SupplierQuoteInfoService;
import com.cbt.util.DateFormat;
import com.cbt.util.GetServerPathUtil;
import com.cbt.util.OperationFileUtil;
import com.cbt.util.UploadAndDownloadPathUtil;
import com.cbt.util.WebCookie;


/**
 * 后台用户管理
 * @ClassName BackstageFactoryInfoController 
 * @Description
 * @author zj
 * @date 2017年11月25日 下午3:00:50
 */
@Controller
@RequestMapping("/backstage")
public class BackstageFactoryInfoController {
	@Autowired
	private FactoryInfoService factoryInfoService;
	
	@Autowired
	private QuoteProductService quoteProductService;
	
	@Autowired
	private QuoteInfoService quoteInfoService;
	
	@Autowired
	private QuoteBigProductsService quoteBigProductsService;
	
	
	@Autowired
	private NoteMessageService noteMessageService;
	
	
	/**
	 * 查询后台用户
	 * @Title queryFactoryList 
	 * @Description
	 * @param request
	 * @param response
	 * @return
	 * @return JsonResult<Map<String,Object>>
	 */
	@RequestMapping("/queryFactoryList.do")
	@ResponseBody
	public JsonResult<List<FactoryInfo>> queryFactoryList(HttpServletRequest request,HttpServletResponse response){		
		List<FactoryInfo> factoryList = factoryInfoService.queryFactoryList();
		return new JsonResult<List<FactoryInfo>>(factoryList);		
	}
	
	
	@RequestMapping("/queryAll.do")
	@ResponseBody
	public JsonResult<List<QuoteInfo>> queryAll(HttpServletRequest request,HttpServletResponse response){	
		
    	//获取后台登录session
		HttpSession session = request.getSession();
    	SysUser sysUser = (SysUser) session.getAttribute("_SESSION_USER");
    	if(sysUser == null){
    		return new JsonResult<List<QuoteInfo>>(2,"未登录管理账号");	
    	}
		List<QuoteInfo> quoteInfos = quoteInfoService.queryAll(null);
		for (QuoteInfo quoteInfo : quoteInfos) {
			QuoteBigProducts quoteBigProducts = quoteBigProductsService.selectByOrderId(quoteInfo.getCustomerId(), quoteInfo.getOrderId());
			if(!(quoteBigProducts == null || "".equals(quoteBigProducts))){
				if(quoteBigProducts.getIsSupplierAccept() == 3){
					quoteInfo.setHasNewItem(1);
				}
			}
		}
		return new JsonResult<List<QuoteInfo>>(quoteInfos);		
	}
	
	
	
	/**
	 * 查询工厂详情
	 * @Title queryFactoryList 
	 * @Description
	 * @param request
	 * @param response
	 * @return
	 * @return JsonResult<List<FactoryInfo>>
	 */
	@ResponseBody
	@RequestMapping("/queryFactoryById.do")
	public JsonResult<Map<String,Object>> queryFactoryById(HttpServletRequest request,HttpServletResponse response){
		//获取后台登录session
		HttpSession session = request.getSession();
    	SysUser sysUser = (SysUser) session.getAttribute("_SESSION_USER");
    	if(sysUser == null){
    		return new JsonResult<Map<String,Object>>(2,"未登录管理账号");	
    	}
		String factoryId = request.getParameter("factoryId");
		FactoryInfo factoryInfo = factoryInfoService.selectFactoryInfo(factoryId);
		List<QuoteProduct> quoteProduct = quoteProductService.queryByFactoryIdGroupByOrderIdAdmin(null, null, factoryId, OrderStatusEnum.CONFIRM.getCode());

		//查询各类统计数据
		Map<String, Object> statisticsCount = factoryInfoService.selectStatisticsCount(factoryId);
		//获取内部已完成项目
		GetProjectCountByFactoryId projectCount = new GetProjectCountByFactoryId();
		int count = projectCount.sendRequest(factoryId);
		if(statisticsCount != null){
			if(statisticsCount.get("orderCount") != null){
				int kuaizhizaoCount = Integer.parseInt(statisticsCount.get("orderCount").toString());
				count = count + kuaizhizaoCount;
				statisticsCount.put("orderCount",count);
			}else{
				statisticsCount.put("orderCount",count);
			}
		}


		Map<String,Object> map = new HashMap<String, Object>();
		map.put("factoryInfo", factoryInfo);
		map.put("quoteProduct", quoteProduct);
		map.put("statisticsCount", statisticsCount);
		return new JsonResult<Map<String,Object>>(map);
	}
	
	/**
	 * 设置封号、解封、VIP、取消VIP方法
	 * @Title queryFactoryById 
	 * @Description
	 * @param request
	 * @param response
	 * @return
	 * @return JsonResult<FactoryInfo>
	 */
	@ResponseBody
	@RequestMapping("/updateFactory.do")
	public JsonResult<String> updateFactory(HttpServletRequest request,HttpServletResponse response){
		try {
			
			//获取后台登录session
			HttpSession session = request.getSession();
	    	SysUser sysUser = (SysUser) session.getAttribute("_SESSION_USER");
	    	if(sysUser == null){
	    		return new JsonResult<String>(2,"未登录管理账号");	
	    	}
			String factoryId = request.getParameter("factoryId");
			FactoryInfo factoryInfo = factoryInfoService.selectFactoryInfo(factoryId);
			if(factoryInfo == null || "".equals(factoryInfo)){
				return new JsonResult<String>(1,"未获取到客户信息");
			}
			if(StringUtils.isNotBlank(request.getParameter("isVip"))){
				Integer isVip = Integer.parseInt(request.getParameter("isVip"));

				//如果是设置封号，录入封号时间
			    if(isVip == 105){
			    	factoryInfo.setCloseTime(DateFormat.format());
			    }else if(isVip == 101){
			    	int days = Integer.parseInt(request.getParameter("days"));
			    	if(factoryInfo.getIsVip() == 101 && factoryInfo.getVipExpiresTime() != null){
			    		factoryInfo.setVipExpiresTime(DateFormat.addDays(factoryInfo.getVipExpiresTime(), days));	
			    	}else{
			    		factoryInfo.setVipExpiresTime(DateFormat.addDays(DateFormat.format(),days));
			    	}
			    }else{
			    	if(factoryInfo.getIsVip() == 101){
			        	factoryInfo.setVipExpiresTime(DateFormat.format());	
			    	}
			    }           
				factoryInfo.setIsVip(isVip);
			}
			if(StringUtils.isNotBlank(request.getParameter("systemRemark"))){
				factoryInfo.setSystemRemark(request.getParameter("systemRemark"));
			}
			if(StringUtils.isNotBlank(request.getParameter("factoryStatus"))){
				factoryInfo.setFactoryStatus(Integer.parseInt(request.getParameter("factoryStatus")));
				if(StringUtils.isNotBlank(request.getParameter("inspectionNote"))){
				 factoryInfo.setInspectionNote(request.getParameter("inspectionNote"));	
				}
			}
			
			factoryInfoService.updateByPrimaryKeySelective(factoryInfo);
			return new JsonResult<String>(0,"更新成功",factoryInfo.getVipExpiresTime());		
		} catch (NumberFormatException e) {			
			e.printStackTrace();
			return new JsonResult<String>(1,"未获取到客户信息");
		} catch (ParseException e) {
			e.printStackTrace();
			return new JsonResult<String>(1,"格式转换错误");
		} catch (Exception e) {
			e.printStackTrace();
			return new JsonResult<String>(1,"翻译错误");
		}
	}
	
	
	
	/**
 	 * 上传验厂文档
 	 * @param request
 	 * @param response
 	 * @return
 	 */
    @ResponseBody
 	@RequestMapping("/uploadInspectionDocument.do")
 	public JsonResult<String> uploadAttachmentAndChangeName(HttpServletRequest request,HttpServletResponse response){
 		
 	 try {
 		 
		//获取后台登录session
		HttpSession session = request.getSession();
    	SysUser sysUser = (SysUser) session.getAttribute("_SESSION_USER");
    	if(sysUser == null){
    		return new JsonResult<String>(2,"未登录管理账号");	
    	}
		 String factoryId = request.getParameter("factoryId");
		 FactoryInfo factoryInfo = factoryInfoService.selectFactoryInfo(factoryId);
		 if(factoryInfo == null || "".equals(factoryInfo)){
			return new JsonResult<String>(1,"未获取到客户信息");
		 }
 		 String drawingName = request.getParameter("selectName");
 		 String path = UploadAndDownloadPathUtil.getFactoryDocument() +  factoryId + File.separator;
		 File file = new File(path);
		 if  (!file.exists()  && !file .isDirectory())      
		 {         
			 file .mkdir();     
		 }  	    	  
 		Map<String, String> multiFileUpload = OperationFileUtil.multiFileUpload1(request, path);
 		String filePath = "";
 		if(!(multiFileUpload == null || multiFileUpload.size() == 0)){
 			filePath = multiFileUpload.get(drawingName);
 		} 
 		factoryInfo.setInspectionDocumentPath(filePath);
 		factoryInfoService.updateByPrimaryKeySelective(factoryInfo);
 		return new JsonResult<String>(0,"success");	
	 	} catch (IllegalStateException e) {
	 		e.printStackTrace();
	 		return new JsonResult<String>(1,"failed");	
	 	} catch (IOException e) {
	 		e.printStackTrace();
	 		return new JsonResult<String>(1,"failed");	
	 	} catch (Exception e) {
			 e.printStackTrace();
			 return new JsonResult<String>(1,"failed");
	    }
	}

	
    /**
     * 根据询盘号查询订单
     * @Title queryInquiryByOrderId 
     * @Description
     * @param request
     * @param response
     * @return
     * @return JsonResult<List<QuoteInfo>>
     */
    @RequestMapping("/queryInquiryByOrderId.do")
	@ResponseBody
	public JsonResult<Map<String,Object>> queryInquiryByOrderId(HttpServletRequest request,HttpServletResponse response){		
      
    	String orderId = request.getParameter("orderId");
    	if(StringUtils.isBlank(orderId)){
    		return new JsonResult<Map<String,Object>>(1,"未获取到询盘号");	
    	}
    	//获取后台登录session
		HttpSession session = request.getSession();
    	SysUser sysUser = (SysUser) session.getAttribute("_SESSION_USER");
    	if(sysUser == null){
    		return new JsonResult<Map<String,Object>>(2,"未登录管理账号");	
    	}
    	Map<String,Object> map = new HashMap<String, Object>();
    	QuoteInfo quoteInfo = quoteInfoService.queryByOrderId(Integer.parseInt(orderId));   
    	  //判断当前询盘来自国内还是国外，显示工艺中英文
		if(quoteInfo.getQuoteArea() == 0){
			String mainProcess = quoteInfo.getMainProcess();
			if(StringUtils.isNotBlank(mainProcess)){
				mainProcess = ProcessZhAndEnEnum.getEnum(mainProcess).getStr();
				if(mainProcess != null){
					quoteInfo.setMainProcess(mainProcess);
				}
			}			
		}
    	
    	QuoteBigProducts quoteBigProducts = quoteBigProductsService.selectByOrderId(quoteInfo.getCustomerId(), Integer.parseInt(orderId));
    	map.put("quoteInfo", quoteInfo);
    	map.put("quoteBigProducts", quoteBigProducts);
		return new JsonResult<Map<String,Object>>(map);		
	}
	
	
    
    /**
     * 根据询盘号更新
     * @Title updateQuote 
     * @Description
     * @param request
     * @param response
     * @return
     * @return JsonResult<String>
     */
    @RequestMapping("/updateQuote.do")
	@ResponseBody
	public JsonResult<String> updateQuote(HttpServletRequest request,HttpServletResponse response){
    	
		//获取后台登录session
		HttpSession session = request.getSession();
    	SysUser sysUser = (SysUser) session.getAttribute("_SESSION_USER");
    	if(sysUser == null){
    		return new JsonResult<String>(2,"未登录管理账号");	
    	}
    	String orderId = request.getParameter("orderId");
    	if(StringUtils.isBlank(orderId)){
    		return new JsonResult<String>(1,"未获取到询盘号");	
    	}
    	QuoteInfo quoteInfo = quoteInfoService.queryByOrderId(Integer.parseInt(orderId));    	
    	if(quoteInfo.getOrderStatus() == 0 && quoteInfo.getMainCategory() == 0){
    		
    		//判断当前询盘来自国内还是国外，显示工艺中英文
    		String mainProcess = request.getParameter("mainProcess");
    		if(StringUtils.isNotBlank(mainProcess)){
	    			if(quoteInfo.getQuoteArea() == 0){
	        			mainProcess = ProcessZhAndEnEnum.getEnum(mainProcess).getValue();
	        			if(mainProcess != null){
	        				quoteInfo.setMainProcess(mainProcess);
	        			}
	        		}else{
	        			quoteInfo.setMainProcess(mainProcess);
	        		}
    		}    		
    	}
    	if(StringUtils.isNotBlank(request.getParameter("orderStatus"))){
    		quoteInfo.setOrderStatus(Integer.parseInt(request.getParameter("orderStatus")));
    		if("7".equals(request.getParameter("orderStatus"))){
        		quoteInfo.setNoPassReasons(request.getParameter("noPassReasons"));	
    		}
    		quoteInfo.setPublishDate(DateFormat.format());
    	}
    	
    	  NoteMessage note = new NoteMessage();    
 		  note.setReceiverId(quoteInfo.getCustomerId());
 		  note.setSendId("");
 		  if(quoteInfo.getOrderStatus() == OrderStatusEnum.NOPASS.getCode()){
 			  note.setMessageTitle(quoteInfo.getOrderId()+"询盘未通过系统审核");
 	 		  note.setMessageDetails(quoteInfo.getNoPassReasons());
 		  }else{
 			  note.setMessageTitle(quoteInfo.getOrderId()+"询盘已通过系统审核");
	 		  note.setMessageDetails(quoteInfo.getOrderId()+"询盘已通过系统审核");  
 		  }
 		  note.setMessageType(MessageTypeEnum.QUOTE_INFO.getCode());
 		  note.setOrderId(Integer.parseInt(orderId));

		  //如果询盘为国外询盘（0）跳转英文版
		  //如果询盘为国内询盘（1）跳转中文版
 		  if(quoteInfo.getQuoteArea() == 0){
			  note.setUrl(GetServerPathUtil.getServerPath()+"/en/purchase_detail.html?orderId="+quoteInfo.getOrderId());
		  }else{
			  note.setUrl(GetServerPathUtil.getServerPath()+"/zh/purchase_detail.html?orderId="+quoteInfo.getOrderId());
		  }

 		  note.setCreateTime(DateFormat.format());


		try {
			quoteInfoService.updateByPrimaryKey(quoteInfo,note);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new JsonResult<String>(0,"更新成功");
    }
    
    
	/**
	 * 查询消息类别
	 * @Title queryMessageList 
	 * @Description
	 * @param request
	 * @param response
	 * @return
	 * @return JsonResult<Map<String,Object>>
	 */
	@RequestMapping("/queryMessageList.do")
	@ResponseBody
	public JsonResult<List<?>> queryMessageList(HttpServletRequest request,HttpServletResponse response){		
		
		List<?> messageList = noteMessageService.queryByBackstage();
		return new JsonResult<List<?>>(messageList);		
	}
    
	
	
	/**
	 * 查询消息明细
	 * @Title queryMessageById 
	 * @Description 
	 * @param id
	 * @return
	 * @return JsonResult<NoteMessage>
	 */
	@ResponseBody
	@RequestMapping("/queryMessageById.do")
	public JsonResult<NoteMessage> queryMessageById(String id){
		try {
			if(StringUtils.isNotBlank(id)){
				NoteMessage noteMessage = noteMessageService.selectByPrimaryKey(Integer.parseInt(id));
				return new JsonResult<NoteMessage>(noteMessage);
			}else{
				return new JsonResult<NoteMessage>(1,"未获取到ID");
			}			
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return new JsonResult<NoteMessage>(1,"查询失败");
		}
	}
	
	
	
	
	 /**
	   * 后台生成根据工艺推荐询盘所需要的数据
	   * @param request
	   * @param response
	   * @return
	   */
	@ResponseBody
    @RequestMapping("/queryQuoteByMainProcess.do")
    public JsonResult<Map<String,Object>> queryByMainProcess(HttpServletRequest request,HttpServletResponse response){
  	 
  	  try {			      
  		  
  		          String factoryId = WebCookie.getFactoryId(request);
				  String str = request.getParameter("page");
				  String startTime = request.getParameter("startTime");
				  String endTime = request.getParameter("endTime");
				  
				  Integer pageSize = 20;
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
				  
//				  List<String> process1 = new ArrayList<String>(Arrays.asList("注塑", "吸塑","滚塑","吹塑","吸塑","挤塑","其他塑料工艺"));
//				  int p1 = process1.size();
//				  for (int i = 0; i < p1; i++) {
//					  ProcessZhAndEnEnum enEnum = ProcessZhAndEnEnum.getEnum(process1.get(i));
//					  if(enEnum != null){
//						  process1.add(enEnum.getValue());
//					  }
//				  }								  
//				  List<String> process2 = new ArrayList<String>(Arrays.asList("不锈钢铸造(硅溶胶、水玻璃)","压铸(铝、锌、镁、铜等)","砂铸(钢、铁、铜、铝等)","重力铸造(铝、铜等)","其他铸造"));
//				  p1 = process2.size();
//				  for (int i = 0; i < p1; i++) {
//					  ProcessZhAndEnEnum enEnum = ProcessZhAndEnEnum.getEnum(process2.get(i));
//					  if(enEnum != null){
//					     process2.add(enEnum.getValue());
//					  }
//				  }		
//				  List<String> process3 = new ArrayList<String>(Arrays.asList("热锻", "冷锻"));
//				  p1 = process3.size();
//				  for (int i = 0; i < p1; i++) {
//					  ProcessZhAndEnEnum enEnum = ProcessZhAndEnEnum.getEnum(process3.get(i));
//					  process3.add(enEnum.getValue());
//				  }	
//				  List<String> process4 = new ArrayList<String>(Arrays.asList("钣金加工(切割、折弯、焊接、组装)","冲压拉伸","旋压","铝挤压"));
//				  p1 = process4.size();
//				  for (int i = 0; i < p1; i++) {
//					  ProcessZhAndEnEnum enEnum = ProcessZhAndEnEnum.getEnum(process4.get(i));
//					  if(enEnum != null){
//					     process4.add(enEnum.getValue());
//					  }
//				  }	
//				  
//				  List<String> process5 = new ArrayList<String>(Arrays.asList("3轴以上精密加工", "高速车床","高速铣床","普通车铣钻磨","其他机加工"));
//				  p1 = process5.size();
//				  for (int i = 0; i < p1; i++) {
//					  ProcessZhAndEnEnum enEnum = ProcessZhAndEnEnum.getEnum(process5.get(i));
//					  if(enEnum != null){
//					     process5.add(enEnum.getValue());
//					  }
//				  }	
//				  
//				  List<String> process6 = new ArrayList<String>(Arrays.asList("现成商品采购", "其他","其他采购"));
//				  p1 = process6.size();
//				  for (int i = 0; i < p1; i++) {
//					  ProcessZhAndEnEnum enEnum = ProcessZhAndEnEnum.getEnum(process6.get(i));
//					  if(enEnum != null){
//					     process6.add(enEnum.getValue());
//					  }
//				  }	
				  
				  //查询塑料类询盘
//				  List<QuoteProduct> injectionOrders = quoteProductService.queryProductByMainProcessAndDate(start, pageSize, OrderStatusEnum.NORMAL_ORDER.getCode(), process1, factoryId,startTime,endTime);
//				  //查询铸造类询盘
//				  List<QuoteProduct> metalOrders = quoteProductService.queryProductByMainProcessAndDate(start, pageSize, OrderStatusEnum.NORMAL_ORDER.getCode(), process2, factoryId,startTime,endTime);
//				  //查询锻造类询盘
//				  List<QuoteProduct> castingOrders = quoteProductService.queryProductByMainProcessAndDate(start, pageSize, OrderStatusEnum.NORMAL_ORDER.getCode(), process3, factoryId,startTime,endTime);
//				  //查询钣金类询盘
//				  List<QuoteProduct> machiningOrders= quoteProductService.queryProductByMainProcessAndDate(start, pageSize, OrderStatusEnum.NORMAL_ORDER.getCode(), process4, factoryId,startTime,endTime);
//			      //查询机加工类询盘
//				  List<QuoteProduct> machiningCastingOrders = quoteProductService.queryProductByMainProcessAndDate(start, pageSize, OrderStatusEnum.NORMAL_ORDER.getCode(), process5, factoryId,startTime,endTime);
//		          //现货商品
//				  List<QuoteProduct> otherOrders = quoteProductService.queryProductByMainProcessAndDate(start, pageSize, OrderStatusEnum.NORMAL_ORDER.getCode(), process6, factoryId,startTime,endTime);		
				  	
				  
				  
				  //获取所有工艺对象
				  List<String> processes = new ArrayList<String>();
				  ProcessZhAndEnEnum[] values = ProcessZhAndEnEnum.values();
				  for (ProcessZhAndEnEnum processZhAndEnEnum : values) {
					  processes.add(processZhAndEnEnum.getValue());
					  processes.add(processZhAndEnEnum.getStr());
				  }
				  
				  //根据工艺查询询盘
				  List<QuoteProduct> quoteProducts = quoteProductService.queryProductByMainProcessAndDate(start, pageSize, OrderStatusEnum.NORMAL_ORDER.getCode(), processes, factoryId, startTime, endTime);
				  for (QuoteProduct quoteProduct : quoteProducts) {
					  List<QuoteProduct> list = quoteProductService.queryByOrderId(quoteProduct.getOrderId());
					  Double totalPrice = 0.0;
					  for (QuoteProduct quoteProduct2 : list) {
						  Double targetPrice = quoteProduct2.getTargetPrice();
						  String quantity = quoteProduct2.getQuantityList();
						  Double targetMoldPrice = quoteProduct2.getTargetMoldPrice();
						  if(targetPrice != null && quantity != null && targetMoldPrice != null){
							  quantity = quantity.split(",")[quantity.split(",").length -1];
							  int qty = Integer.parseInt(quantity);			
							  totalPrice +=targetPrice * qty + targetMoldPrice;
						  }
					  }
					  
					  quoteProduct.setTotalAmount(new BigDecimal(totalPrice).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue());
                      quoteProduct.setMainProcess(ProcessZhAndEnEnum.getEnum(quoteProduct.getMainProcess()).getStr());            
				  }
				  
				  
				  
				  Map<String,Object> map = new HashMap<String, Object>();
				  map.put("orders", quoteProducts);	
//				  map.put("metalOrders", metalOrders);		
//				  map.put("castingOrders", castingOrders);		
//				  map.put("machiningOrders", machiningOrders);		
//				  map.put("machiningCastingOrders", machiningCastingOrders);		
//				  map.put("otherOrders", otherOrders);		
					
				  
				  return  new JsonResult<Map<String,Object>>(map);
			} catch (NumberFormatException e) {
		    	  return  new JsonResult<Map<String,Object>>(1,"查询失败");
			}

    }


	/**
	 * 查询已结束的订单
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryFinish.do")
	public JsonResult<List<QuoteInfo>> queryFinish(HttpServletRequest request) {

		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
        Double amount = 2.0;
        if(StringUtils.isNotBlank(request.getParameter("amount"))){
			amount = Double.parseDouble(request.getParameter("amount"));
		}
		List<QuoteInfo> quoteInfos = quoteInfoService.queryFinishByDateAndPrice(amount, startTime, endTime);
		return  new JsonResult<List<QuoteInfo>>(quoteInfos);
	}







}
