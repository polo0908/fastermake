package com.cbt.service.impl;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cbt.rpc.RpcSynNews;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cbt.dao.FactoryEvaluateMapper;
import com.cbt.dao.FactoryUserMapper;
import com.cbt.dao.NoteMessageMapper;
import com.cbt.dao.QuoteInfoMapper;
import com.cbt.dao.QuoteProductMapper;
import com.cbt.dao.SupplierQuoteInfoMapper;
import com.cbt.dao.SupplierQuoteProductMapper;
import com.cbt.entity.DialogueIds;
import com.cbt.entity.FactoryEvaluate;
import com.cbt.entity.FactoryEvaluateWithBLOBs;
import com.cbt.entity.FactoryUser;
import com.cbt.entity.NoteMessage;
import com.cbt.entity.QuoteInfo;
import com.cbt.entity.QuoteProduct;
import com.cbt.entity.SupplierQuoteInfo;
import com.cbt.entity.SupplierQuoteProduct;
import com.cbt.enums.MessageTypeEnum;
import com.cbt.enums.OrderStatusEnum;
import com.cbt.enums.QuoteOrderStatusEnum;
import com.cbt.service.SupplierQuoteInfoService;
import com.cbt.util.DateFormat;
import com.cbt.util.GetQuoteMailUtil;
import com.cbt.util.GetServerPathUtil;
import com.cbt.controller.SendMailUtil;


@Service
public class SupplierQuoteInfoServiceImpl implements SupplierQuoteInfoService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
	private SupplierQuoteInfoMapper supplierQuoteInfoMapper;
	 
	@Autowired
	private SupplierQuoteProductMapper supplierQuoteProductMapper;
	
	@Autowired
	private QuoteInfoMapper quoteInfoMapper;
	
	@Autowired
	private QuoteProductMapper quoteProductMapper;
	
	@Autowired
	private FactoryEvaluateMapper factoryEvaluateMapper;
	
	@Autowired
	private NoteMessageMapper noteMessageMapper;
	
	@Autowired
	private FactoryUserMapper factoryUserMapper;
	
//    @Autowired
//    private ThreadPoolTaskExecutor taskExecutor;
	
	
	@Override
	public int insert(SupplierQuoteInfo record) {
		return supplierQuoteInfoMapper.insert(record);
	}

	@Override
	public int updateByPrimaryKeySelective(SupplierQuoteInfo record) {
		return supplierQuoteInfoMapper.updateByPrimaryKeySelective(record);
	}

	
	/**
	 * 插入报价和产品价格信息
	 * @throws UnsupportedEncodingException 
	 */
	@Transactional
	@Override
	public Map<String,Object> insert(SupplierQuoteInfo record, String productList,String productIds,String productRemarks) throws UnsupportedEncodingException {
		//将该订单之前的报价设置为不生效
		supplierQuoteInfoMapper.updateByFactoryIdAndOrderId(record.getOrderId(), record.getFactoryId());
		supplierQuoteInfoMapper.insert(record);
		
		  //保存到提醒消息表（报价时提醒采购商）
	      QuoteInfo quoteInfo = quoteInfoMapper.queryByOrderId(record.getOrderId());
 	      NoteMessage note = new NoteMessage();    
		  note.setReceiverId(quoteInfo.getCustomerId());
		  note.setSendId(record.getFactoryId());
		  note.setMessageTitle(record.getOrderId()+(quoteInfo.getCsgOrderId() == null ? "" : "("+quoteInfo.getCsgOrderId()+")")+"询盘有新的报价消息");
		  note.setMessageDetails(record.getOrderId()+"询盘有新的报价消息");
		  note.setOrderId(record.getOrderId());
		  note.setMessageType(MessageTypeEnum.OFFER_MESSAGE.getCode());
		  note.setUrl(GetServerPathUtil.getServerPath()+"/zh/purchase_detail.html?orderId="+record.getOrderId());   
		  note.setCreateTime(DateFormat.format());
		  DialogueIds did = new DialogueIds();
		  noteMessageMapper.returnDialogueId(did);
		  int dialogueId = did.getId();
		  note.setDialogueId(dialogueId);
		  noteMessageMapper.insertSelective(note);
		  
		
		List<SupplierQuoteProduct> list = new ArrayList<SupplierQuoteProduct>();
		 if (productList.endsWith(",")) {
				if(productList.length()>1){
					productList = productList.substring(0, productList.length() - 1);
					String[] split = productList.split(",");
					String[] ids = productIds.split(",");
					String[] prs = null;
					if(StringUtils.isNotBlank(productRemarks)){
						prs = productRemarks.split(",,",-1);
					}
					Double totalAmount = 0.0;
					Double totalProductPrice = 0.0;
					Double totalMoldPrice = 0.0;
					for(int i = 0;i<split.length;i++){
						        Double amount = 0.0;
						        Double productPrice = 0.0;
						        Double productAmount = 0.0;
						        Double moldPrice = 0.0;
						        String[] split1 = split[i].split("&",-1);
					        	SupplierQuoteProduct  supplierQuoteProduct = new SupplierQuoteProduct();
									Double unitPrice1 = 0.0;
									if(StringUtils.isNotBlank(split1[0])){
										unitPrice1 = Double.parseDouble(split1[0].trim());
									}
									Double moldPrice1 = 0.0;
									if(StringUtils.isNotBlank(split1[1])){
										moldPrice1 = Double.parseDouble(split1[1].trim());
										moldPrice = moldPrice1;
									}
									if(StringUtils.isNotBlank(split1[2])){
										amount = Double.parseDouble(split1[2].trim());
										productAmount = new BigDecimal(amount).subtract(new BigDecimal(moldPrice1)).setScale(3,BigDecimal.ROUND_HALF_UP).doubleValue();
									}
									if(StringUtils.isNotBlank(split1[3])){
										productPrice = Double.parseDouble(split1[3].trim());
									}
									Double unitPrice2 = 0.0;
									Double moldPrice2 = 0.0;
									if(split1.length > 4){
										
										if(StringUtils.isNotBlank(split1[4])){
											unitPrice2 = Double.parseDouble(split1[4].trim());
										}									
										if(StringUtils.isNotBlank(split1[5])){
											moldPrice2 = Double.parseDouble(split1[5].trim());
											moldPrice = moldPrice2;
										}
										if(StringUtils.isNotBlank(split1[6])){
											amount = Double.parseDouble(split1[6].trim());
											productAmount = new BigDecimal(amount).subtract(new BigDecimal(moldPrice2)).setScale(3,BigDecimal.ROUND_HALF_UP).doubleValue();
										}										
									}
									Double unitPrice3 = 0.0;
									Double moldPrice3 = 0.0;
									if(split1.length > 8){										
										if(StringUtils.isNotBlank(split1[8])){
											unitPrice3 = Double.parseDouble(split1[8].trim());
										}									
										if(StringUtils.isNotBlank(split1[9])){
											moldPrice3 = Double.parseDouble(split1[9].trim());
											moldPrice = moldPrice3;
										}	
										if(StringUtils.isNotBlank(split1[10])){
											amount = Double.parseDouble(split1[10].trim());
											productAmount = new BigDecimal(amount).subtract(new BigDecimal(moldPrice3)).setScale(3,BigDecimal.ROUND_HALF_UP).doubleValue();
										}	
									}		
									
									totalAmount +=amount;
									totalMoldPrice +=moldPrice;
									totalProductPrice +=productAmount;
									supplierQuoteProduct.setQuoteUnitPrice1(unitPrice1);
									supplierQuoteProduct.setQuoteUnitPrice2(unitPrice2);
									supplierQuoteProduct.setQuoteUnitPrice3(unitPrice3);
									supplierQuoteProduct.setQuoteMoldPrice1(moldPrice1);
									supplierQuoteProduct.setQuoteMoldPrice2(moldPrice2);
									supplierQuoteProduct.setQuoteMoldPrice3(moldPrice3);
									supplierQuoteProduct.setOrderId(record.getOrderId());
									supplierQuoteProduct.setSupplierQuoteId(record.getId());	
									supplierQuoteProduct.setQuoteProductId(Integer.parseInt(ids[i]));
									supplierQuoteProduct.setQuoteProductPrice(productPrice);
									
									if(prs != null && prs.length != 0){
										supplierQuoteProduct.setProductRemark(prs[i]);
									}
								    list.add(supplierQuoteProduct);
							}
					
					             //获取产品报价总价（数量最多的价格）
				             	totalAmount = new BigDecimal(totalAmount).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
								//获取产品总价（数量最多的价格）
					            totalProductPrice = new BigDecimal(totalProductPrice).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
				            	//获取模具总价
					            totalMoldPrice =  new BigDecimal(totalMoldPrice).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					            record.setTotalAmount(totalAmount);
								record.setTotalProductPrice(totalProductPrice);
								record.setTotalMoldPrice(totalMoldPrice);
				             	record.setIsActive(1);
				             	supplierQuoteInfoMapper.updateByPrimaryKeySelective(record);
						}
		 }	
		
		 if(list.size() != 0 ){
			 supplierQuoteProductMapper.insertList(list);
		 }
		 
		 //更新报价人数(查询当前报价工厂数)
		 int totalQuoteFactory = supplierQuoteInfoMapper.totalQuoteFactory(record.getOrderId());
		 quoteInfo.setCurrentNumber(totalQuoteFactory);
		 quoteInfoMapper.updateByPrimaryKeySelective(quoteInfo);
		 
		 Map<String,Object> map = new HashMap<String, Object>();
		 map.put("quoteInfoId", record.getId());


		RpcSynNews rpc = new RpcSynNews();
		rpc.sendRequest(null,quoteInfo.getCsgOrderId(),null,"新报价");
		 //2018-6-22 待确认邮件格式
		 //报价成功后发送邮件
//		 QuoteProduct quoteProduct = quoteProductMapper.selectByPrimaryKey(list.get(0).getQuoteProductId());
//		 String content = GetQuoteMailUtil.getQuoteMail(record, quoteInfo, list, quoteProduct);
//		 String customerId = quoteInfo.getCustomerId();
//		 List<FactoryUser> factoryUsers = factoryUserMapper.selectListByFactoryId(customerId);
//		 //邮件标题
//		 String title = URLEncoder.encode("快制造kuaizhizao.cn有的新的报价信息", "utf-8");	
//		 for (FactoryUser factoryUser : factoryUsers) {				
//			 sendMailTask(factoryUser.getEmail(), title, content);
//		 }
		 
		 
		 return map;
		 
	}

	@Override
	public SupplierQuoteInfo queryByOrderIdAndFactoryId(Integer orderId,String factoryId) {
		return supplierQuoteInfoMapper.queryByOrderIdAndFactoryId(orderId, factoryId);
	}

	@Override
	public SupplierQuoteInfo selectByPrimaryKey(Integer id) {
		return supplierQuoteInfoMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<SupplierQuoteInfo> queryByOrderId(Integer orderId) {
		return supplierQuoteInfoMapper.queryByOrderId(orderId);
	}

	@Override
	public void updateOrderStatus(List<SupplierQuoteInfo> list) {
		supplierQuoteInfoMapper.updateOrderStatus(list);
		
	}

	@Override
	public SupplierQuoteInfo queryQuoteDetailByFactoryId(Integer orderId,
			String factoryId, String customerId) {
		return supplierQuoteInfoMapper.queryQuoteDetailByFactoryId(orderId, factoryId, customerId);
	}

	@Transactional
	@Override
	public void updateQuote(Integer orderId,String refuseReasons) {		
		QuoteInfo quoteInfo = quoteInfoMapper.queryByOrderId(orderId);
		quoteInfo.setOrderStatus(OrderStatusEnum.PROCESS.getCode());
		quoteInfoMapper.updateByPrimaryKeySelective(quoteInfo);
		
		 if (refuseReasons.endsWith("~")) {
				if(refuseReasons.length()>1){
					refuseReasons = refuseReasons.substring(0, refuseReasons.length() - 1);
					String[] split = refuseReasons.split("~");
					for(int i = 0;i<split.length;i++){
						    String[] split1 = split[i].split("&",-1);							 
						    String factoryId = null;
						    SupplierQuoteInfo supplierQuoteInfo = new SupplierQuoteInfo();
							if(StringUtils.isNotBlank(split1[0])){
								factoryId = split1[0].trim();
								supplierQuoteInfo = supplierQuoteInfoMapper.queryByOrderIdAndFactoryId(orderId, factoryId);
							}
							if(StringUtils.isNotBlank(split1[1])){
								supplierQuoteInfo.setRefuseReasons(split1[1]);
							}
//							if(StringUtils.isNotBlank(split1[2])){
//								FactoryEvaluateWithBLOBs factoryEvaluate = new FactoryEvaluateWithBLOBs();
//								factoryEvaluate.setCreateTime(DateFormat.format());
//								factoryEvaluate.setFactoryId(factoryId);
//								factoryEvaluate.setPublishId(quoteInfo.getCustomerId());
//								factoryEvaluate.setEvaluateInfo(split1[2]);
//								factoryEvaluateMapper.insertSelective(factoryEvaluate);
//							}
							if(!(supplierQuoteInfo == null || "".equals(supplierQuoteInfo))){
								supplierQuoteInfo.setQuoteStatus(QuoteOrderStatusEnum.REFUSE_ORDER.getCode());
								supplierQuoteInfo.setOperationTime(DateFormat.format());
								supplierQuoteInfoMapper.updateByPrimaryKeySelective(supplierQuoteInfo);	
								
								
								  //保存到提醒消息表（报价时提醒采购商）
						 	      NoteMessage note = new NoteMessage();    
								  note.setReceiverId(factoryId);
								  note.setSendId(quoteInfo.getCustomerId());
								  note.setMessageTitle(quoteInfo.getOrderId()+"询盘有新的报价反馈消息");
								  note.setMessageDetails(quoteInfo.getOrderId()+"询盘有新的报价消息");
								  note.setOrderId(quoteInfo.getOrderId());
								  note.setMessageType(MessageTypeEnum.OFFER_MESSAGE.getCode());
								  note.setUrl(GetServerPathUtil.getServerPath()+"/zh/offer_detail.html?orderId="+quoteInfo.getOrderId());   
								  note.setCreateTime(DateFormat.format());
								  DialogueIds did = new DialogueIds();
								  noteMessageMapper.returnDialogueId(did);
								  int dialogueId = did.getId();
								  note.setDialogueId(dialogueId);
								  noteMessageMapper.insertSelective(note);
							}

					}
				}
		  }		
	}

	@Override
	public int totalQuoteFactory(Integer orderId) {
		return supplierQuoteInfoMapper.totalQuoteFactory(orderId);
	}

	
   /**
    * 多线程发送推送邮件
    * @Title sendMail 
    * @Description
    * @return void
    */
//   public void sendMailTask(String email,String title,String content){
//	   taskExecutor.execute(new Runnable() {			
//		@Override
//		public void run() {
//			SendMailUtil.sendMail(email, title, content);			
//		}
//	 });
//   }
}
