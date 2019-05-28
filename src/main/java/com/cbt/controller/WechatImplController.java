package com.cbt.controller;

import com.cbt.entity.*;
import com.cbt.enums.OrderStatusEnum;
import com.cbt.model.*;
import com.cbt.service.*;
import com.cbt.util.*;
import com.cbt.wximpl.Wechatimpl;
import com.google.gson.JsonSyntaxException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.DigestException;
import java.text.ParseException;
import java.util.*;
import java.util.List;

/**
 * 实现类控制
 */

@Controller
@RequestMapping("wimpl")
public class WechatImplController {

	// logo路径
	public static final String LOGO = UploadAndDownloadPathUtil.getQuoteFile() + "qr"+File.separator+"logo.png";
	// 创建二维码  
	public static final String CREATE_TICKET_PATH = "https://api.weixin.qq.com/cgi-bin/qrcode/create";  
	// 通过ticket换取二维码  
	public static final String SHOW_QRCODE_PATH = "https://mp.weixin.qq.com/cgi-bin/showqrcode"; 
	// 永久二维码  
	public static final String QR_LIMIT_SCENE = "QR_LIMIT_SCENE";//0  
	// 永久二维码(字符串)  
	public static final String QR_LIMIT_STR_SCENE = "QR_LIMIT_STR_SCENE";//1  
	
	//图片上传地址
	private static final String UPLOAD_IMG = "http://file.api.weixin.qq.com/cgi-bin/media/upload";
	//上传图文消息地址
	private static final String UPLOAD_NEWS = "https://api.weixin.qq.com/cgi-bin/media/uploadnews";
	//推送消息地址
	private static final String PUSH = "https://api.weixin.qq.com/cgi-bin/message/mass/sendall";
	
	//预览消息地址
	private static final String VIEW_PHSH = "https://api.weixin.qq.com/cgi-bin/message/mass/preview";
	
	private static final String PHSH_OPENID = "https://api.weixin.qq.com/cgi-bin/message/mass/send";
	
	//获取域名
	private static final String SERVER_PATH = GetServerPathUtil.getServerPath();
	
	private static String component_ticket = null;
	
	
    private static int width = 140;              //二维码宽度  
    private static int height = 140;             //二维码高度  
    private static int onColor = 0xFF000000;     //前景色  
    private static int offColor = 0xFFFFFFFF;    //背景色  
    private static int margin = 1;               //白边大小，取值范围0~4  
    private static ErrorCorrectionLevel level = ErrorCorrectionLevel.L;  //二维码容错率  
	
    
    @Autowired
    private QuoteInfoService quoteInfoService;
    @Autowired
    private QuoteProductService quoteProductService;
    @Autowired
    private FactoryInfoService factoryInfoService;
    @Autowired
    private FactoryUserService factoryUserService;
    @Autowired
	private SupplierQuoteInfoService supplierQuoteInfoService;
    
    
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping("menu")
	public String menu(HttpServletRequest req, HttpServletResponse resp) {
		System.out.println("menu");
		HttpSession session = req.getSession();
		session.setAttribute("name", "list");		
		
		return null;
	}

	/**
	 * 创建公众号菜单
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping("cemenu")
	public String createMenu(HttpServletRequest req, HttpServletResponse resp) {
		// String name = req.getSession().getAttribute("name").toString();
		// System.out.println(name);
		Wechatimpl wechatimpl = new Wechatimpl();
		wechatimpl.createmenu();
		return null;
	}

	@RequestMapping("autosend")
	public String autosend(HttpServletRequest req, HttpServletResponse resp) {
		System.out.println("autosend");
		HttpSession session = req.getSession();
		session.setAttribute("name", "zhangsan");
		// Wechatimpl.getautosend();
		return null;
	}

	// 处理微信post过来的数据
	@RequestMapping("/saxmessage.do")
	public String saxmessage(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		resp.setContentType("text/json;charset=utf-8");
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		resp.setHeader("Access-Control-Allow-Origin", "*");
		PrintWriter out = resp.getWriter();
		String result = "";
		String echostr = req.getParameter("echostr");// 获取接入微信接口凭证
		if (StringUtils.isNotEmpty(echostr) && echostr.length() > 1) {
			result = echostr;
			resp.getWriter().print(result);
		} else {
			// 消息转换
			Map<String, String> map = MessageUtil.xmlToMap(req);
			String fromUserName = map.get("FromUserName");
			String toUserName = map.get("ToUserName");
			String createTime = map.get("CreateTime");
			String msgType = MessageUtil.domethod(map.get("MsgType"));
			String content = map.get("Content");
			String event = map.get("Event");
			String eventKey = map.get("EventKey");
			String ArticleCount = map.get("ArticleCount");
			String Articles = map.get("Articles");
			String Title = map.get("Title");
			String Description = map.get("Description");
			String PicUrl = map.get("PicUrl");
			String Url = map.get("Url");
			// 微信处理流程
			TextMessage text = new TextMessage();
			text.setCreateTime(createTime);
			text.setToUserName(fromUserName);
			text.setFromUserName(toUserName);
			// 文本消息、图片消息、语音消息、视频消息、音乐消息、图文消息
			if ("text".equals(msgType) || "image".equals(msgType)
					|| "voice".equals(msgType) || "video".equals(msgType)
					|| "music".equals(msgType) || "news".equals(msgType)) {
				 text.setMsgType("news");
				 Item item = new Item();
				 item.setDescription("点击查看询盘"); //图文消息的描述
				 item.setPicUrl(SERVER_PATH+"/images/wechatLogo.jpg"); //图文消息图片地址
				 item.setTitle("欢迎使用快制造"); //图文消息标题
				 item.setUrl(SERVER_PATH); //图文url链接
				 
				 Item item1 = new Item();
				 item1.setDescription("工厂接单就去 www.kuaizhizao.com 最专业的制造业订单交易平台，每天更新几十条订单，让接单不再是难事！ 都是制造业的工厂、公司，多交朋友，才能接到订单！ 网上接单，就选择快制造"); //图文消息的描述
				 item1.setPicUrl(SERVER_PATH+"/images/wechatLogo.jpg"); //图文消息图片地址
				 item1.setTitle("制造业订单哪里找，就上快制造"); //图文消息标题
				 item1.setUrl("https://mp.weixin.qq.com/s?__biz=MzU2MTQ3MTA5MQ==&mid=100000014&idx=1&sn=c3704713b720d1d940f0dbf0fa413fc8&scene=19#wechat_redirect"); //图文url链接
				 
				 List<Item> list=new ArrayList<Item>();
				 list.add(item);  //这里发送的是单图文，如果需要发送多图文则在这里list中加入多个Article即可！
				 list.add(item1);
				 text.setArticleCount(list.size());
				 text.setArticles(list);
				 result = MessageUtil.textMessageToXml(text);
				 result = result.replace("com.cbt.model.Item", "item");
				
//				text.setMsgType("text");
//				text.setContent("人工服务暂未开启，请进行其他操作！");
//				result = MessageUtil.textMessageToXml(text);
				resp.getWriter().print(result);
				logger.info("关注后推送的消息："+result);
//				 out.print(result);
			} else if ("event".equals(msgType)) {
				if ("subscribe".equals(event)) {					
					 text.setMsgType("news");
					 Item item = new Item();
					 item.setDescription("点击查看询盘"); //图文消息的描述
					 item.setPicUrl(SERVER_PATH+"/images/wechatLogo.jpg"); //图文消息图片地址
					 item.setTitle("欢迎使用快制造"); //图文消息标题
					 item.setUrl(SERVER_PATH); //图文url链接
					 
					 Item item1 = new Item();
					 item1.setDescription("工厂接单就去 www.kuaizhizao.com 最专业的制造业订单交易平台，每天更新几十条订单，让接单不再是难事！ 都是制造业的工厂、公司，多交朋友，才能接到订单！ 网上接单，就选择快制造"); //图文消息的描述
					 item1.setPicUrl(SERVER_PATH+"/images/wechatLogo.jpg"); //图文消息图片地址
					 item1.setTitle("制造业订单哪里找，就上快制造"); //图文消息标题
					 item1.setUrl("https://mp.weixin.qq.com/s?__biz=MzU2MTQ3MTA5MQ==&mid=100000014&idx=1&sn=c3704713b720d1d940f0dbf0fa413fc8&scene=19#wechat_redirect"); //图文url链接
					 
					 List<Item> list=new ArrayList<Item>();
					 list.add(item);  //这里发送的是单图文，如果需要发送多图文则在这里list中加入多个Article即可！
					 list.add(item1);
					 text.setArticleCount(list.size());
					 text.setArticles(list);
					 result = MessageUtil.textMessageToXml(text);
					 result = result.replace("com.cbt.model.Item", "item");
					 logger.info("关注后推送的消息："+result);
					 out.print(result);
				}
			}
			out.close();
		}
		return null;
	}
	
	
	
	

	
	
	
	
	// 获取第三方平台component_ticket
//	@RequestMapping("/componentMessage.do")
//	public String componentMessage(HttpServletRequest req, HttpServletResponse resp){
//		
//	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	@RequestMapping("/signature.do")
	public String signature(String pageUrl, HttpServletRequest req,
			HttpServletResponse resp) {

		/* String ticket = SignatureUtil.getJsapi_ticket(); */
		// logger.debug("进入获取signature方法");
		String ticket = SingleJsapi.getInstance().getJsapiToken()
				.getAccess_token();
		// Log.debug("获取ticket"+ticket);
		String noncestr = SignatureUtil.createNonceStr();
		// Log.debug("获取noncestr"+noncestr);
		int timestamp = SignatureUtil.getTimestamp();
		// Log.debug("获取timestamp"+timestamp);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("jsapi_ticket", ticket);
		map.put("noncestr", noncestr);
		map.put("timestamp", timestamp);
		map.put("url", pageUrl);

		String signature = "";
		try {
			signature = SignatureUtil.SHA1(map);
		} catch (DigestException e) {
			e.printStackTrace();
		}
		map.remove("url");// 封装json对象时删除，页面不需要的参数不封装
		map.remove("jsapi_ticket");// 封装json对象时删除，页面不需要的参数不封装
		map.put("signature", signature);
		map.put("appid", WriteProp.get("appid"));
		JSONObject json = JSONObject.fromObject(map);
		try {
			resp.getWriter().print(json);
			resp.getWriter().close();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	/**
	 * 获取第三方授权预授权码
	 * @Title getPreAuthCode 
	 * @Description 
	 * @return
	 * @return String
	 */
	@RequestMapping("/getPreAuthCode.do")
	public String getPreAuthCode(){
		String access_token = SingleAccessToken.getInstance().getAccessToken().getToken();		
		JSONObject json = SendHttpsRequestUtil.sendPostRequest(WriteProp.get(
				"pre_auth_code_url").replace("ACCESS_TOKEN", access_token),"");
		System.out.println(json.toString());	
		return json.toString();
	}
	
	
	
	
	
	
	
	
	

	/**
	 * 报价成功消息推送
	 * 
	 * @param orderId
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping("quote_note")
	public String quoteNote(String orderId, HttpServletRequest req,
			HttpServletResponse resp) {

		try {
			String openid = WebCookie.getOpenid(req);
			if(StringUtils.isBlank(openid)){
				String loginEmail = WebCookie.getLoginEmail(req);
				FactoryUser factoryUser = factoryUserService.selectByLoginEmail(loginEmail);
				openid = factoryUser.getOpenid();
			}
			if(StringUtils.isNotBlank(openid) && StringUtils.isNotBlank(orderId)){
				
				QuoteInfo quoteInfo = quoteInfoService.queryByOrderId(Integer.parseInt(orderId));
				
				String access_token = SingleAccessToken.getInstance().getAccessToken().getToken();

				String message_push_url = WriteProp.get("message_push_url").replace("ACCESS_TOKEN", access_token);
				
				//报价成功通知
				MessageTemplate template = new MessageTemplate();
				template.setTouser(openid);
				template.setTemplate_id("q8W_iQwKVXJzJTy697TreymDNdbQxqQwn1phdcLX2EM");
				template.setUrl(SERVER_PATH + "/m-zh/detail.html?orderId="+quoteInfo.getOrderId());

				DataTemplate data = new DataTemplate();
				Map<Object,Object> map = new HashMap<Object, Object>();
				
				ValueAndColor first = new ValueAndColor();
				first.setValue(quoteInfo.getOrderId()+"您已经成功报价");
				first.setColor("#173177");

				ValueAndColor keyword1 = new ValueAndColor();
				keyword1.setValue(quoteInfo.getQuoteTitle());
				keyword1.setColor("#173177");
				map.put("keyword1", keyword1);

				ValueAndColor keyword2 = new ValueAndColor();
				keyword2.setValue(quoteInfo.getFactoryName());
				keyword2.setColor("#173177");
				map.put("keyword2", keyword2);
				
				ValueAndColor keyword3 = new ValueAndColor();
				keyword3.setValue(DateFormat.currentDate());
				keyword3.setColor("#173177");
				map.put("keyword3", keyword3);

				ValueAndColor remark = new ValueAndColor();
				remark.setValue("请登录快制造微信端或网站PC端查看");
				remark.setColor("#173177");

				System.out.println(map);
				
				data.setFirst(first);
				data.setMap(map);
				data.setRemark(remark);

				template.setData(data);
			  
				JSONObject json = JSONObject.fromObject(template);
			    
				//去除json数据中 "map":{}
				String str = json.toString();
				str = Wechatimpl.changeToFormat(str);
				
				JSONObject j = SendHttpsRequestUtil.sendPostRequest(message_push_url,str);




				String factoryId = WebCookie.getFactoryId(req);
				SupplierQuoteInfo supplierQuoteInfo = supplierQuoteInfoService.queryByOrderIdAndFactoryId(quoteInfo.getOrderId(), factoryId);

				//当报价完成后显示所有报价对比
				Integer rank = 1;                                   //排名
				Double differPrice = 0.0;
				List<SupplierQuoteInfo> quotes = new ArrayList<SupplierQuoteInfo>();
				if(supplierQuoteInfo != null){

					Double myPrice = supplierQuoteInfo.getTotalAmount();
					Double minPrice = 0.0;               //最低工厂价格
					quotes = supplierQuoteInfoService.queryByOrderId(quoteInfo.getOrderId());
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
						}

					}

					//如果当前rank为第一，则当前是最低价
					if(rank == 1){
						differPrice = 0.0;
					}else{
						differPrice = myPrice - minPrice;
						differPrice = new BigDecimal(differPrice).setScale(0,BigDecimal.ROUND_UP).doubleValue();
					}

				}

				//显示具体对比数据
				StringBuffer strs = new StringBuffer();
                if(rank == 1){
					strs.append("当前在所有报价工厂排名第" + rank + "名(价格由低到高排名)");
				}else{
					strs.append("当前在所有报价工厂排名第" + rank + "名(价格由低到高排名)。");
					strs.append("您的总价比第一名贵了"+differPrice+"(元)");
				}

				//报价成功价格对比推送
				MessageTemplate temp = new MessageTemplate();
				temp.setTouser(openid);
				temp.setTemplate_id("Bt1fW4-XZeWeX6HsrStxlCJLiDWL6FRVAKS7x1hAM1g");
				temp.setUrl(SERVER_PATH  + "/m-zh/detail.html?orderId="+quoteInfo.getOrderId());

				DataTemplate dataTemp = new DataTemplate();
				Map<Object,Object> map1 = new HashMap<Object, Object>();

				ValueAndColor first1 = new ValueAndColor();
				first1.setValue(quoteInfo.getOrderId()+"报价反馈");
				first1.setColor("#173177");

				ValueAndColor keyword1_1 = new ValueAndColor();
				keyword1_1.setValue(DateFormat.format());
				keyword1_1.setColor("#173177");
				map1.put("keyword1", keyword1_1);

				ValueAndColor keyword2_2 = new ValueAndColor();
				keyword2_2.setValue(strs.toString());
				keyword2_2.setColor("#173177");
				map1.put("keyword2", keyword2_2);

				ValueAndColor remark1 = new ValueAndColor();
				remark1.setValue("请登录快制造微信端或网站PC端查看");
				remark1.setColor("#173177");

				dataTemp.setFirst(first1);
				dataTemp.setMap(map1);
				dataTemp.setRemark(remark1);

				temp.setData(dataTemp);

				JSONObject json1 = JSONObject.fromObject(temp);
				//去除json数据中 "map":{}
				String str1 = json1.toString();
				str1 = Wechatimpl.changeToFormat(str1);

				SendHttpsRequestUtil.sendPostRequest(message_push_url,str1);


			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}	
		return null;
	}
	




	
	
	/**
	 * 回复消息推送
	 * 
	 * @param factoryId
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping("message_note")
	public String messageNote(String factoryId,String orderId, String messageDetail, HttpServletRequest req,HttpServletResponse resp) {

		try {
            String openid = null;
            if(StringUtils.isNotBlank(factoryId)){
            	//取第一个用户进行推送
				List<FactoryUser> factoryUser = factoryUserService.selectListByFactoryId(factoryId);
				if(factoryUser!=null){
					openid = factoryUser.get(0).getOpenid();
				}
				logger.info("《《《《《获取到的用户openid为》》》》"+ openid);
    			if(StringUtils.isNotBlank(openid) && StringUtils.isNotBlank(orderId)){
    				
    				QuoteInfo quoteInfo = quoteInfoService.queryByOrderId(Integer.parseInt(orderId));
    				
    				String access_token = SingleAccessToken.getInstance().getAccessToken().getToken();

    				String message_push_url = WriteProp.get("message_push_url").replace("ACCESS_TOKEN", access_token);
    				
    				MessageTemplate template = new MessageTemplate();
    				template.setTouser(openid);
    				template.setTemplate_id("Bt1fW4-XZeWeX6HsrStxlCJLiDWL6FRVAKS7x1hAM1g");
    				template.setUrl(SERVER_PATH  + "/m-zh/detail.html?orderId="+quoteInfo.getOrderId());

    				DataTemplate data = new DataTemplate();
    				Map<Object,Object> map = new HashMap<Object, Object>();
    				
    				ValueAndColor first = new ValueAndColor();
    				first.setValue(quoteInfo.getOrderId()+"您有新的回复消息");
    				first.setColor("#173177");

    				ValueAndColor keyword1 = new ValueAndColor();
    				keyword1.setValue(DateFormat.format());
    				keyword1.setColor("#173177");
    				map.put("keyword1", keyword1);

    				ValueAndColor keyword2 = new ValueAndColor();
    				keyword2.setValue(messageDetail);
    				keyword2.setColor("#173177");
    				map.put("keyword2", keyword2);

    				ValueAndColor remark = new ValueAndColor();
    				remark.setValue("请登录快制造微信端或网站PC端查看");
    				remark.setColor("#173177");

    				data.setFirst(first);
    				data.setMap(map);
    				data.setRemark(remark);

    				template.setData(data);
    			  
    				JSONObject json = JSONObject.fromObject(template);
    				//去除json数据中 "map":{}
    				String str = json.toString();
    				str = Wechatimpl.changeToFormat(str);
					logger.info("《《《《《获取到微信返回值为》》》》"+ str);
    				JSONObject j = SendHttpsRequestUtil.sendPostRequest(message_push_url,str);
					logger.info("《《《《《获取到微信返回值为》》》》"+ j);
    			}
            }			

		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 通过消息中心发送的消息
	 * 订单号为空时只显示消息内容
	 * @param receiverId
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping("message_reply")
	public String messageReply(String receiverId,String orderId, String messageDetails, HttpServletRequest req,HttpServletResponse resp) {

		try {
            String openid = null; 
            if(StringUtils.isNotBlank(receiverId)){
				List<FactoryUser> factoryUser = factoryUserService.selectListByFactoryId(receiverId);
				if(factoryUser!=null){
					openid = factoryUser.get(0).getOpenid();
				}
    			if(StringUtils.isNotBlank(openid)){
    				Integer pid = null;
    				if(StringUtils.isNotBlank(orderId)){
    					QuoteInfo quoteInfo = quoteInfoService.queryByOrderId(Integer.parseInt(orderId));
    					pid = quoteInfo.getOrderId();
    				}
    				
    				
    				String access_token = SingleAccessToken.getInstance().getAccessToken().getToken();

    				String message_push_url = WriteProp.get("message_push_url").replace("ACCESS_TOKEN", access_token);
    				
    				MessageTemplate template = new MessageTemplate();
    				template.setTouser(openid);
    				template.setTemplate_id("Bt1fW4-XZeWeX6HsrStxlCJLiDWL6FRVAKS7x1hAM1g");
    				if(pid == null){
    					template.setUrl(SERVER_PATH + "/m-zh/quote_list.html");
    				}else{
        				template.setUrl(SERVER_PATH + "/m-zh/detail.html?orderId="+pid);
    				}


    				DataTemplate data = new DataTemplate();
    				Map<Object,Object> map = new HashMap<Object, Object>();
    				
    				ValueAndColor first = new ValueAndColor();
    				if(pid == null){
    					first.setValue("您有新的回复消息");
    				}else{
    					first.setValue(pid+"您有新的回复消息");
    				}
    				
    				first.setColor("#173177");

    				ValueAndColor keyword1 = new ValueAndColor();
    				keyword1.setValue(DateFormat.format());
    				keyword1.setColor("#173177");
    				map.put("keyword1", keyword1);

    				ValueAndColor keyword2 = new ValueAndColor();
    				keyword2.setValue(messageDetails);
    				keyword2.setColor("#173177");
    				map.put("keyword2", keyword2);

    				ValueAndColor remark = new ValueAndColor();
    				remark.setValue("请登录快制造微信端或网站PC端查看");
    				remark.setColor("#173177");

    				data.setFirst(first);
    				data.setMap(map);
    				data.setRemark(remark);

    				template.setData(data);
    			  
    				JSONObject json = JSONObject.fromObject(template);
    				//去除json数据中 "map":{}
    				String str = json.toString();
    				str = Wechatimpl.changeToFormat(str);
    				
    				JSONObject j = SendHttpsRequestUtil.sendPostRequest(message_push_url,str);
    			}
            }			

		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return null;
	}





	/**
	 * 授盘成功消息推送
	 * @param
	 * @param
	 * @param
	 * @return
	 */
	@RequestMapping("success_quote")
	public String successQuote(String projectId, String factoryId) {

		try {
			String openid = null;   //微信openid
			List<FactoryUser> factoryUser = factoryUserService.selectListByFactoryId(factoryId);
			if(factoryUser!=null){
				openid = factoryUser.get(0).getOpenid();
			}

			if(StringUtils.isNotBlank(openid) && StringUtils.isNotBlank(projectId)){

				String access_token = SingleAccessToken.getInstance().getAccessToken().getToken();
				String message_push_url = WriteProp.get("message_push_url").replace("ACCESS_TOKEN", access_token);

				//工厂id
				String supplierId = Base64Encode.getBase64(factoryId);
				//报价成功通知
				MessageTemplate template = new MessageTemplate();
				template.setTouser(openid);
				template.setTemplate_id("q8W_iQwKVXJzJTy697TreymDNdbQxqQwn1phdcLX2EM");
				template.setUrl(SERVER_PATH + "/report/reportList?csgOrderId="+projectId+"&supplierId="+supplierId);

				DataTemplate data = new DataTemplate();
				Map<Object,Object> map = new HashMap<Object, Object>();

				ValueAndColor first = new ValueAndColor();
				first.setValue(projectId+"项目已授盘,请及时上传进度报告");
				first.setColor("#173177");

				ValueAndColor keyword1 = new ValueAndColor();
				keyword1.setValue("2");
				keyword1.setColor("#173177");
				map.put("keyword1", keyword1);

				ValueAndColor keyword2 = new ValueAndColor();
				keyword2.setValue("1");
				keyword2.setColor("#173177");
				map.put("keyword2", keyword2);

				ValueAndColor keyword3 = new ValueAndColor();
				keyword3.setValue(DateFormat.currentDate());
				keyword3.setColor("#173177");
				map.put("keyword3", keyword3);

				ValueAndColor remark = new ValueAndColor();
				remark.setValue("请登录快制造微信端或网站PC端查看");
				remark.setColor("#173177");

				System.out.println(map);

				data.setFirst(first);
				data.setMap(map);
				data.setRemark(remark);

				template.setData(data);

				JSONObject json = JSONObject.fromObject(template);

				//去除json数据中 "map":{}
				String str = json.toString();
				str = Wechatimpl.changeToFormat(str);

				JSONObject j = SendHttpsRequestUtil.sendPostRequest(message_push_url,str);

			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	/**
	 * 每周询盘消息推送
	 * @param orderId
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping("push")
	public String push(String orderId, HttpServletRequest req,HttpServletResponse resp) {

		try {
			String factoryId = WebCookie.getFactoryId(req);
			//取最新的工厂未报价询盘进行推送，当不含图片则直接跳过
			List<QuoteProduct> quoteProducts = quoteProductService.queryProductGroupByOrderId(0, 2, OrderStatusEnum.NORMAL_ORDER.getCode(), null, null,null,factoryId,null);
				
			
			//获取access_token
			String access_token = SingleAccessToken.getInstance().getAccessToken().getToken();
			List<MessageNewsImg> list = new ArrayList<MessageNewsImg>();
			for (QuoteProduct quoteProduct : quoteProducts) {
				
				String drawingPath = quoteProduct.getDrawingPath();
				String path = "";
				if(StringUtils.isNotBlank(drawingPath)){
					String[] split = drawingPath.split("\\/");
					path = UploadAndDownloadPathUtil.getDrawingFile() + split[split.length-1];
				}else{
					continue;
				}
				File file = new File(path);
				
				String formUpload = Wechatimpl.formUpload(UPLOAD_IMG + "?access_token="+access_token+"&type=image", file);
				Map map = JsonUtil.jsonToObject(formUpload, Map.class);						
				//上传图文消息
				MessageNewsImg messageNewsImg = new MessageNewsImg();		
				messageNewsImg.setAuthor("kuaizhizao");
				messageNewsImg.setContent(quoteProduct.getQuoteTitle());
				messageNewsImg.setContent_source_url(GetServerPathUtil.getServerPath()+"/m-zh/detail.html?orderId="+quoteProduct.getOrderId());
				messageNewsImg.setDigest(quoteProduct.getQuoteTitle());
				messageNewsImg.setShow_cover_pic(1);
				messageNewsImg.setThumb_media_id(map.get("media_id").toString());
				messageNewsImg.setTitle(quoteProduct.getQuoteTitle());

				list.add(messageNewsImg);				
			}
			Map<Object,Object> mapNews = new HashMap<Object, Object>();
			mapNews.put("articles", list);
			JSONObject fromObject = JSONObject.fromObject(mapNews);
			logger.info("图文消息上传json"+fromObject);
			//获取图文消息ID
			String mediaId = Wechatimpl.getMediaId(UPLOAD_NEWS + "?access_token="+access_token, fromObject.toString());
	      
			PushNewsImg pushNewImg = new PushNewsImg();
			Filter filter = new Filter();
			Mpnews mpnews = new Mpnews();
			filter.setIs_to_all(true);
			mpnews.setMedia_id(mediaId);
			pushNewImg.setFilter(filter);
			pushNewImg.setMpnews(mpnews);
			pushNewImg.setMsgtype("mpnews");
			pushNewImg.setSend_ignore_reprint(1);
			
			JSONObject fromObject2 = JSONObject.fromObject(pushNewImg);
			logger.info("完整推送图文消息json"+fromObject2);
			//发送推送消息
			Client.sendPost(PUSH + "?access_token="+access_token,fromObject2.toString());

		} catch (NumberFormatException e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	
	
	
	/** 
	 * 创建永久二维码(字符串) 
	 *  
	 * @param accessToken 
	 * @param sceneStr 
	 *            场景str 
	 * @return 
	 */  
	public static String createForeverStrTicket(String accessToken, String sceneStr) {  
	    TreeMap<String, String> params = new TreeMap<String, String>();
	    params.put("access_token", accessToken);  
	    // output data  
	    Map<String, String> intMap = new HashMap<String, String>();
	    intMap.put("scene_str", sceneStr);  
	    Map<String, Map<String, String>> mapMap = new HashMap<String, Map<String, String>>();
	    mapMap.put("scene", intMap);  
	    Map<String, Object> paramsMap = new HashMap<String, Object>();
	    paramsMap.put("action_name", QR_LIMIT_STR_SCENE);  
	    paramsMap.put("action_info", mapMap);  
//	    paramsMap.put("access_token", accessToken);
	    String data = JsonUtil.map2json(paramsMap);  
	    
	    data = Client.sendPost(CREATE_TICKET_PATH + "?access_token="+accessToken, data);
 
	    WechatQRCode wechatQRCode = null;  
	    try {  
	        wechatQRCode = JsonUtil.jsonToObject(data, WechatQRCode.class);  
	        System.out.println(wechatQRCode);
	    } catch (JsonSyntaxException e) {  
	        e.printStackTrace();  
	    }  
	    return wechatQRCode == null ? null : wechatQRCode.getTicket();  
	}  
	
	
	
	/**
     * 链接url下载图片
     * String URL
     * 
    **/        
    private static void downloadPicture(String urlList) {
        URL url = null; 
        try {
            url = new URL(urlList);
            DataInputStream dataInputStream = new DataInputStream(url.openStream());

          //本地地址及名称
            String imageName =  "E:/"+UUID.randomUUID()+".jpg";

            FileOutputStream fileOutputStream = new FileOutputStream(new File(imageName));
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int length;

            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            byte[] context=output.toByteArray();
            fileOutputStream.write(output.toByteArray());
            dataInputStream.close();
            fileOutputStream.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("下载成功");
    }
	
    /**
     * 获取二维码 
     * String tickefile
     * 
     * String   
     * */
    public static String toticke(String ticket){

          //发送 get 请求 换取二维码 (ticke)        
        String tickefile=Client.sendGet(SHOW_QRCODE_PATH, "ticket="+ticket);
      //  System.out.println("filepath=="+url3+"?"+"ticket="+ticke);    
        return SHOW_QRCODE_PATH+"?"+"ticket="+ticket;
    }  
	
    
    
    /**
     * 生成报价的二维码
     * @Title placeQrOrder 
     * @Description
     * @param resp
     * @param url
     * @return void
     */
    @RequestMapping("qr-code")
    public void placeQrOrder(HttpServletResponse resp,@Param("url") String url) {
        try {
        	if(StringUtils.isNotBlank(url)){
        		url = URLDecoder.decode(url,"utf-8");
			}
			resp.setHeader("Cache-Control", "no-store");
			resp.setHeader("Pragma", "no-cache");
			resp.setDateHeader("Expires", 0);
			resp.setContentType("image/png");

			Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.MARGIN, 1);
			
			BitMatrix bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, 140, 140, hints);
			
//			bitMatrix = deleteWhite(bitMatrix);//删除白边

			
			
			MatrixToImageWriter.writeToStream(bitMatrix, "png", resp.getOutputStream());

		} catch (WriterException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    
    /**
     * 生成带logo的二维码
     * @Title placeQrOrderLogo 
     * @Description
     * @param resp
     * @param url
     * @return void
     */
    @ResponseBody
    @RequestMapping("qr-code-logo")
    public String placeQrOrderLogo(HttpServletResponse resp,@Param("url")String url,@Param("orderId")String orderId) {
    	
    	try {
			String imgPath = UploadAndDownloadPathUtil.getQuoteFile() + "qr";   	
			generateQRImage(url, LOGO, imgPath, orderId+".png", "png");
			return imgPath + orderId + ".png";
		} catch (Exception e) {
			e.printStackTrace();
			return "NO";
		}
    	
    }
    
    
    
    
    /** 
     * 生成带logo的二维码图片  
     * @param txt          //二维码内容 
     * @param logoPath     //logo绝对物理路径 
     * @param imgPath      //二维码保存绝对物理路径 
     * @param imgName      //二维码文件名称 
     * @param suffix       //图片后缀名 
     * @throws Exception 
     */  
    public static void generateQRImage(String txt, String logoPath, String imgPath, String imgName, String suffix) throws Exception{  
     
        File filePath = new File(imgPath);  
        if(!filePath.exists()){  
            filePath.mkdirs();  
        }  
          
        imgPath = imgPath + File.separator + imgName;
          
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();     
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");      
        hints.put(EncodeHintType.ERROR_CORRECTION, level);    
        hints.put(EncodeHintType.MARGIN, margin);  //设置白边  
        BitMatrix bitMatrix = new MultiFormatWriter().encode(txt, BarcodeFormat.QR_CODE, width, height, hints);    
        File qrcodeFile = new File(imgPath);      
        writeToFile(bitMatrix, suffix, qrcodeFile, logoPath);      
    }    
        
    /**  
     *   
     * @param matrix 二维码矩阵相关  
     * @param format 二维码图片格式  
     * @param file 二维码图片文件  
     * @param logoPath logo路径  
     * @throws IOException  
     */    
    public static void writeToFile(BitMatrix matrix,String format,File file,String logoPath) throws IOException {    
        BufferedImage image = toBufferedImage(matrix);    
        Graphics2D gs = image.createGraphics();    
           
        int ratioWidth = image.getWidth()*2/10;  
        int ratioHeight = image.getHeight()*2/10;  
        //载入logo    
        Image img = ImageIO.read(new File(logoPath));   
        int logoWidth = img.getWidth(null)>ratioWidth?ratioWidth:img.getWidth(null);  
        int logoHeight = img.getHeight(null)>ratioHeight?ratioHeight:img.getHeight(null);  
          
        int x = (image.getWidth() - logoWidth) / 2;   
        int y = (image.getHeight() - logoHeight) / 2;  
          
        gs.drawImage(img, x, y, logoWidth, logoHeight, null);   
        gs.setColor(Color.black);  
        gs.setBackground(Color.WHITE);  
        gs.dispose();    
        img.flush();    
        if(!ImageIO.write(image, format, file)){    
            throw new IOException("Could not write an image of format " + format + " to " + file);      
        }    
    }    
        
    public static BufferedImage toBufferedImage(BitMatrix matrix){    
        int width = matrix.getWidth();    
        int height = matrix.getHeight();    
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);    
            
        for(int x=0;x<width;x++){    
            for(int y=0;y<height;y++){    
                image.setRGB(x, y, matrix.get(x, y) ? onColor : offColor);    
            }    
        }    
        return image;       
    }         
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * 删除二维码白边
     * */
    private static BitMatrix deleteWhite(BitMatrix matrix) {
        int[] rec = matrix.getEnclosingRectangle();
        int resWidth = rec[2] + 1;
        int resHeight = rec[3] + 1;

        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
        resMatrix.clear();
        for (int i = 0; i < resWidth; i++) {
            for (int j = 0; j < resHeight; j++) {
                if (matrix.get(i + rec[0], j + rec[1]))
                    resMatrix.set(i, j);
            }
        }
        return resMatrix;
    }
    
    
    
    
    
	
	public static void main(String[] args) {
		
		
		File file = new File("D:\\5.png");
		String access_token = SingleAccessToken.getInstance().getAccessToken().getToken();
		String formUpload = Wechatimpl.formUpload("http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token="+access_token+"&type=image", file);
		Map map = JsonUtil.jsonToObject(formUpload, Map.class);		
		
		//上传图文消息
		MessageNewsImg messageNewsImg = new MessageNewsImg();		
		messageNewsImg.setAuthor("kuaizhizao");
		messageNewsImg.setContent("hdpe清洁剂瓶 4种规格 各100万个");
		messageNewsImg.setContent_source_url("www.kuaizhizao.cn");
		messageNewsImg.setDigest("测试项目");
		messageNewsImg.setShow_cover_pic(1);
		messageNewsImg.setThumb_media_id(map.get("media_id").toString());
		messageNewsImg.setTitle("hdpe清洁剂瓶 4种规格 各100万个");
		
		List<MessageNewsImg> list = new ArrayList<MessageNewsImg>();
		list.add(messageNewsImg);
		Map<Object,Object> mapNews = new HashMap<Object, Object>();
		mapNews.put("articles", list);
		JSONObject fromObject = JSONObject.fromObject(mapNews);
		
		//获取图文消息ID
		String mediaId = Wechatimpl.getMediaId("https://api.weixin.qq.com/cgi-bin/media/uploadnews?access_token="+access_token, fromObject.toString());
      
//		String mediaId = "GsQT_pOQd7kiFhkk8GuG_R7WKCJgwFZ_w4MHoC-N-W0t-F3Jdc8LueB4vmykTmVa";
		
		PushNewsImg pushNewImg = new PushNewsImg();
		Filter filter = new Filter();
		Mpnews mpnews = new Mpnews();
		filter.setIs_to_all(true);
		mpnews.setMedia_id(mediaId);
		pushNewImg.setFilter(filter);
		pushNewImg.setMpnews(mpnews);
		pushNewImg.setMsgtype("mpnews");
		pushNewImg.setSend_ignore_reprint(1);
		
//		JSONObject fromObject2 = JSONObject.fromObject(pushNewImg);
		//发送推送消息
//		String sendPost = Client.sendPost("https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token="+access_token,fromObject2.toString());
//	    System.out.println(sendPost);
	
//		ArrayList<String> arrayList = new ArrayList<String>();
//		arrayList.add("ou29Q1n81AyccF00OHJxrXeft20k");
//        System.out.println(arrayList);
//		Touser touser = new Touser();
//		touser.setList(arrayList);
        
//        String getUser = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=="+access_token+"&next_openid=NEXT_OPENID";
        //获取用户列表
        String getUser = Client.sendGet("https://api.weixin.qq.com/cgi-bin/user/get", "access_token="+access_token);      
        Map users = JsonUtil.jsonToObject(getUser, Map.class);	
        Object object = users.get("data");
        Map openids = JsonUtil.jsonToObject(object.toString(), Map.class);	
        String ids = openids.get("openid").toString();
        
        //测试单条推送使用
//        String ids = "o8lfmwsmVgDqHcHX-IuBMUvc1TbI";
        
		PushTouser<String> pushTouser = new PushTouser();
		pushTouser.setTouser(ids);
		pushTouser.setMpnews(mpnews);
		pushTouser.setMsgtype("mpnews");
		pushTouser.setSend_ignore_reprint(1);		
		
		JSONObject fromObject3 = JSONObject.fromObject(pushTouser);
		System.out.println(fromObject3);	
		
		
		//测试单条推送使用
//	    String sendPost = Client.sendPost(VIEW_PHSH +"?access_token="+access_token, fromObject3.toString());
	    //推送所有人
//	    String sendPost = Client.sendPost(PHSH_OPENID +"?access_token="+access_token, fromObject3.toString());
//	    System.out.println(sendPost);
	}
	
	
	

	
	
	
}
