package com.cbt.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.cbt.dao.*;
import com.cbt.entity.*;
import com.cbt.enums.*;
import com.cbt.rpc.RpcSynSuccessNote;
import com.cbt.translate.TranslateExecutor;
import com.cbt.util.Base64Encode;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cbt.service.QuoteInfoService;
import com.cbt.util.DateFormat;
import com.cbt.util.DateUtil;
import com.cbt.util.GetServerPathUtil;
/**
 * Add 增加英文版数据库存储，询盘修改、插入时先翻译后存储
 * @author zj
 * @date 2018年10月9日
 */
/**
 * Edit  增加修改图纸时，提醒已报价工厂图纸已更新
 * @ClassName QuoteInfoServiceImpl 
 * @Method updateByPrimaryKey
 * @Description
 * @author zj
 * @date 2018年5月8日 
 */
/**
 * Edit  同步询盘状态，首页根据状态获取完成询盘下单情况
 * @ClassName QuoteInfoServiceImpl 
 * @Method updateQuote,queryOrderFactoryList
 * @Description
 * @author zj
 * @date 2018年4月27日 
 */
/**
 * Edit  判断报价截止时间是否大于当前时间，如果项目状态为已过期，则更新为正常询盘
 * @ClassName QuoteInfoServiceImpl 
 * @Method updateByPrimaryKey
 * @Description
 * @author zj
 * @date 2018年4月4日 下午5:12:52
 */

@Service
public class QuoteInfoServiceImpl implements QuoteInfoService {

	@Autowired
	private QuoteInfoMapper quoteInfoMapper;
	@Autowired
	private QuoteInfoEnMapper quoteInfoEnMapper;

	@Autowired
	private FactoryInfoMapper factoryInfoMapper;


	@Autowired
	private QuoteProductMapper quoteProductMapper;
	@Autowired
	private QuoteProductEnMapper quoteProductEnMapper;

	@Autowired
	private SupplierQuoteInfoMapper supplierQuoteInfoMapper;
	@Autowired
	private NoteMessageMapper noteMessageMapper;
	@Autowired
	private NoteMessageEnMapper noteMessageEnMapper;
	@Autowired
	private UserOrderMapper userOrderMapper;
	@Autowired
	private FactoryUserMapper factoryUserMapper;
	@Autowired
	private OrderFactoryMapper orderFactoryMapper;

	private static final String INNER_FACTORY_ID = "0";  //自营工厂id


	@Transactional
	@Override
	public int deleteByPrimaryKey(Integer id) {

		quoteProductEnMapper.deleteByPrimaryKey(id);
		return quoteInfoMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(QuoteInfo record) throws Exception {
		quoteInfoMapper.insert(record);
		QuoteInfo quoteInfo = TranslateExecutor.translateQuote(record);
		return quoteInfoEnMapper.insert(quoteInfo);
	}
    
	@Transactional
	@Override
	public int insertAll(QuoteInfo quoteInfo) throws Exception {
		
		quoteInfo.setPublishDate(DateFormat.format());
		quoteInfo.setCurrentNumber(0);
		quoteInfo.setQuoteLocation(0);
		quoteInfo.setMaxNumber(3);
		quoteInfoMapper.insertBackOrderId(quoteInfo);

		if (quoteInfo.getOrderId() != null) {
			Integer orderId = quoteInfo.getOrderId();
			List<QuoteProduct> list = quoteInfo.getProducts();
			List<QuoteProduct> listEn = new ArrayList<QuoteProduct>();

			if (list != null && list.size() > 0) {
				for (QuoteProduct quoteProduct : list) {	
					quoteProduct.setCustomerId(quoteInfo.getCustomerId());
					quoteProduct.setOrderId(orderId);
					if(quoteProduct.getTargetMoldPrice() == null){
						quoteProduct.setTargetMoldPrice(0.0);
					}
					if(quoteProduct.getTargetPrice() == null){
						quoteProduct.setTargetPrice(0.0);;
					}
					//翻译后产品
					QuoteProduct product = TranslateExecutor.translateQuoteProduct(quoteProduct);
					listEn.add(product);
				}
				quoteProductMapper.insertList(list);
				quoteProductEnMapper.insertList(listEn);
			}
		}

		//翻译后的询盘
		quoteInfo = TranslateExecutor.translateQuote(quoteInfo);
		quoteInfoEnMapper.insert(quoteInfo);

		return quoteInfo.getOrderId();
	}

	@Transactional
	@Override
	public int updateAll(QuoteInfo quoteInfo) throws Exception {
		
		Integer orderId = quoteInfo.getOrderId();
		if (orderId == null) {
			throw new  RuntimeException("更新数据，orderId不能为空");
		}
		List<QuoteProduct> list = quoteInfo.getProducts();
		List<QuoteProduct> listEn = new ArrayList<QuoteProduct>();

		quoteProductMapper.deleteByOrderId(orderId);
		quoteProductEnMapper.deleteByOrderId(orderId);
		quoteInfoMapper.updateByPrimaryKeySelective(quoteInfo);
		QuoteInfo quoteInfoEn = TranslateExecutor.translateQuote(quoteInfo);
		quoteInfoEnMapper.updateByPrimaryKeySelective(quoteInfoEn);
		
		if (list != null && list.size() > 0) {
			for (QuoteProduct quoteProduct : list) {					
				quoteProduct.setOrderId(orderId);
				quoteProduct.setCustomerId(quoteInfo.getCustomerId());
				//翻译后产品
				QuoteProduct product = TranslateExecutor.translateQuoteProduct(quoteProduct);
				listEn.add(product);
			}
			quoteProductMapper.insertList(list);
			quoteProductEnMapper.insertList(listEn);
		}
		
		return orderId;
		
		
	}
    @Override
	public QuoteInfo selectByPrimaryKey(Integer id) {
		return quoteInfoMapper.selectByPrimaryKey(id);
	}

	@Override
	public QuoteInfo selectByPrimaryKeyEn(Integer id) {
		return quoteInfoEnMapper.selectByPrimaryKey(id);
	}

	@Transactional
	@Override
	public int updateByPrimaryKey(QuoteInfo record) throws Exception {
		quoteInfoMapper.updateByPrimaryKeySelective(record);
		QuoteInfo recordEn = TranslateExecutor.translateQuote(record);
		return quoteInfoEnMapper.updateByPrimaryKeySelective(recordEn);
	}

	@Override
	public List<QuoteInfo> queryAllInquiryOrder(Integer start,
			Integer pageSize, Integer orderStatus) {
		return quoteInfoMapper.queryAllInquiryOrder(start, pageSize,
				orderStatus);
	}

	@Override
	public List<QuoteInfo> queryAllInquiryOrderEn(Integer start, Integer pageSize, Integer orderStatus) {
		return quoteInfoEnMapper.queryAllInquiryOrder(start, pageSize,
				orderStatus);
	}

	@Override
	public int totalNormalOrder(Integer orderStatus) {
		return quoteInfoMapper.totalNormalOrder(orderStatus);
	}

	@Override
	public QuoteInfo queryByOrderId(Integer orderId) {
		return quoteInfoMapper.queryByOrderId(orderId);
	}

	@Override
	public QuoteInfo queryByOrderIdEn(Integer orderId) {
		return quoteInfoEnMapper.queryByOrderId(orderId);
	}

	@Override
	public QuoteInfo queryByOrderIdAndFactoryId(Integer orderId,
			String factoryId) {
		return quoteInfoMapper.queryByOrderIdAndFactoryId(orderId, factoryId);
	}

	@Override
	public QuoteInfo queryByOrderIdAndFactoryIdEn(Integer orderId, String factoryId) {
		return quoteInfoEnMapper.queryByOrderIdAndFactoryId(orderId, factoryId);
	}


	@Transactional
	@Override
	public void updateByPrimaryKey(QuoteInfo record, String productList,String drawingName) throws Exception {
	 if(StringUtils.isNotBlank(productList)){
		 if (productList.endsWith("~")) {
				if(productList.length()>1){
					productList = productList.substring(0, productList.length() - 1);
					String[] split = productList.split("~");
					for(int i = 0;i<split.length;i++){
				        String[] split1 = split[i].split("&", -1);
				        Integer productId = Integer.parseInt(split1[0]);
				        QuoteProduct quoteProduct = quoteProductMapper.selectByPrimaryKey(productId);
				        if(StringUtils.isNotBlank(split1[1])){				        	
				        	quoteProduct.setProductName(split1[1]);			        	
				        }
				        if(StringUtils.isNotBlank(split1[2])){
				        	quoteProduct.setProcess(split1[2]);
				        }
				        if(StringUtils.isNotBlank(split1[3])){
				        	quoteProduct.setMaterials(split1[3]);
				        }
				        if(StringUtils.isNotBlank(split1[4])){
				        	quoteProduct.setWeight(Double.parseDouble(split1[4]));
				        }
				        if(StringUtils.isNotBlank(split1[5])){
				        	quoteProduct.setQuantityList(split1[5]);
				        }
				        if(StringUtils.isNotBlank(split1[6])){
				        	
				        	//如果第三方询盘，更新的第六个字段是数量单位
				        	if(StringUtils.isBlank(record.getCsgOrderId())){
				        		quoteProduct.setQuantityUnit(split1[6]);
				        	}else{
				        		quoteProduct.setAnnualQuantity(split1[6]);
				        	}				  
				        }
				        if(StringUtils.isNotBlank(split1[7])){
				        	quoteProduct.setProductRemark(split1[7]);
				        }	
				        
				        if(StringUtils.isBlank(record.getCsgOrderId())){
				        	 if(StringUtils.isNotBlank(split1[8])){
				        		 quoteProduct.setTargetPrice(Double.parseDouble(split1[8]));
				        	 }				        	
				        }				        
				        quoteProductMapper.updateByPrimaryKeySelective(quoteProduct);

						//翻译后产品
						QuoteProduct quoteProductEn = TranslateExecutor.translateQuoteProduct(quoteProduct);
						quoteProductEnMapper.updateByPrimaryKeySelective(quoteProductEn);
					}  
				}
		    }
	 }	
		
	    //判断报价截止时间是否大于当前时间，如果项目状态为已过期，则更新为正常询盘
	    String quoteEndDate = record.getQuoteEndDate();
	    if(StringUtils.isNotBlank(quoteEndDate)){
		    Boolean flag = DateUtil.calExpires(quoteEndDate);
		    if(flag && record.getOrderStatus() == OrderStatusEnum.EXPIRE.getCode()){
		    	record.setOrderStatus(OrderStatusEnum.NORMAL_ORDER.getCode());
		    }
	    }
	    quoteInfoMapper.updateByPrimaryKeySelective(record);
		QuoteInfo recordEn = TranslateExecutor.translateQuote(record);
		quoteInfoEnMapper.updateByPrimaryKeySelective(recordEn);
	    
		  //更新图纸时，提醒已报价的工厂图纸已更新
	    if(StringUtils.isNotBlank(drawingName)){
	    	
	    	  Integer orderId = record.getOrderId();
	    	  List<SupplierQuoteInfo> supplierQuoteInfo = supplierQuoteInfoMapper.queryByOrderId(orderId);
	    	  NoteMessage note = new NoteMessage();
	    	  note.setSendId(record.getCustomerId());
			  note.setMessageTitle(orderId+"询盘图纸已更新，请下载查看");
		 	  note.setMessageDetails(orderId+"询盘图纸已更新，请下载查看。");  
			  note.setMessageType(MessageTypeEnum.OTHER_MESSAGE.getCode());
			  note.setOrderId(orderId);
			  note.setUrl(GetServerPathUtil.getServerPath()+"/zh/offer_detail.html?orderId="+orderId);
			  //翻译后消息
			  NoteMessage noteEn = TranslateExecutor.translateMessage(note);

			for (SupplierQuoteInfo supplierQuoteInfo2 : supplierQuoteInfo) {
	    		  
		  		  DialogueIds did = new DialogueIds();
				  noteMessageMapper.returnDialogueId(did);
				  int dialogueId = did.getId();
				  note.setDialogueId(dialogueId);
				  note.setReceiverId(supplierQuoteInfo2.getFactoryId());
				  note.setCreateTime(DateFormat.format());

				  noteEn.setDialogueId(dialogueId);
				  noteEn.setReceiverId(supplierQuoteInfo2.getFactoryId());
				  noteEn.setCreateTime(DateFormat.format());

				  noteMessageMapper.insertSelective(note);
				  noteMessageEnMapper.insertSelective(note);
			  }	    	
	  	      
	    }
	    
	    
	    
	}

	@Override
	public List<QuoteInfo> queryAll(Integer orderStatus) {
		return quoteInfoMapper.queryAll(orderStatus);
	}

	@Override
	public List<QuoteInfo> queryAllEn(Integer orderStatus) {
		return quoteInfoEnMapper.queryAll(orderStatus);
	}

	@Override
	public QuoteInfo selectDetailByOrderId(Integer orderId,String buyerId) {
		return quoteInfoMapper.selectDetailByPrimaryKey(orderId,buyerId);
	}

	@Override
	public QuoteInfo selectDetailByOrderIdEn(Integer orderId, String buyerId) {
		return quoteInfoEnMapper.selectDetailByPrimaryKey(orderId,buyerId);
	}


	@Override
		public List<QuoteInfo> selectDetailListByFactoryId(Integer orderStatus,
				String customerId) {
			return quoteInfoMapper.selectDetailListByFactoryId(orderStatus, customerId);
		}

	@Override
	public List<QuoteInfo> selectDetailListByFactoryIdEn(Integer orderStatus, String customerId) {
		return quoteInfoEnMapper.selectDetailListByFactoryId(orderStatus, customerId);
	}


	@Transactional
		@Override
		public void updateByPrimaryKey(QuoteInfo record,
				List<SupplierQuoteInfo> list) throws Exception {
			quoteInfoMapper.updateByPrimaryKeySelective(record);
			record = TranslateExecutor.translateQuote(record);
			quoteInfoEnMapper.updateByPrimaryKeySelective(record);
			supplierQuoteInfoMapper.updateOrderStatus(list);
		}

		@Transactional
		@Override
		public int updateByPrimaryKey(QuoteInfo record, NoteMessage note) throws Exception {
			DialogueIds did = new DialogueIds();
			noteMessageMapper.returnDialogueId(did);
			int dialogueId = did.getId();
			note.setDialogueId(dialogueId);
			noteMessageMapper.insertSelective(note);
            //消息英文版同时保存
			note = TranslateExecutor.translateMessage(note);
			noteMessageEnMapper.insertSelective(note);
			//报价英文版修改
			quoteInfoMapper.updateByPrimaryKeySelective(record);
			record = TranslateExecutor.translateQuote(record);
			return quoteInfoEnMapper.updateByPrimaryKeySelective(record);
		}

		@Override
		public double calEstimatedPrice() {			
			List<QuoteProduct> quoteProducts = quoteProductMapper.queryProductByWeek();
			Double totalEstimatedPrice = 0.0;
			String quantityList;
			Double targetPrice = 0.0;
			Double targetMoldPrice = 0.0;
			Integer quantity = 0;
			String[] split;
			for (QuoteProduct quoteProduct : quoteProducts) {
				
				if(quoteProduct != null){
					quantityList = quoteProduct.getQuantityList();
					if(StringUtils.isNotBlank(quantityList)){
						split = quantityList.split(",");
						quantity = Integer.parseInt(split[split.length-1]);
					}
					targetPrice = quoteProduct.getTargetPrice();	
					targetMoldPrice = quoteProduct.getTargetMoldPrice();
					if(targetMoldPrice != null){
						totalEstimatedPrice += quantity*targetPrice+targetMoldPrice;
					}else{
						totalEstimatedPrice += quantity*targetPrice;
					}					
				}				
				//System.out.println(totalEstimatedPrice);
			}	
		
			return totalEstimatedPrice;
		}

		
		/*
		 * 修改下单工厂为多个情况
		 */
		//更新工厂下单金额（内部报价同步）
		@Transactional
		@Override
		public int updateQuote(String projectId, String factoryId,String factoryName, String totalAmount) throws Exception {
			int state = 0;
			QuoteInfo quoteInfo = quoteInfoMapper.queryByCgsOrderId(projectId);

			if(StringUtils.isNotBlank(factoryId)){
				FactoryInfo factoryInfo = factoryInfoMapper.selectFactoryInfo(factoryId);
				if(factoryInfo != null){
					quoteInfo.setOrderFactoryName(factoryInfo.getFactoryName());
					factoryName = factoryInfo.getFactoryName();
				}
				
				//修改报价状态为生产中
				if(quoteInfo!=null){
					SupplierQuoteInfo supplierQuoteInfo = supplierQuoteInfoMapper.queryByOrderIdAndFactoryId(quoteInfo.getOrderId(), factoryId);
					if(supplierQuoteInfo!=null){
						supplierQuoteInfo.setQuoteStatus(QuoteOrderStatusEnum.PROCESS_ORDER.getCode());
						supplierQuoteInfoMapper.updateByPrimaryKey(supplierQuoteInfo);

						//其他的工厂报价状态更新为已拒绝
						String refuseReason = "其他工厂价格更便宜";
						List<SupplierQuoteInfo> supplierQuoteInfos = supplierQuoteInfoMapper.queryByOrderId(quoteInfo.getOrderId());
						for(SupplierQuoteInfo s1 : supplierQuoteInfos){
							if(s1.getFactoryId().equals(factoryId)){
								continue;
							}else if(s1.getQuoteStatus() != QuoteOrderStatusEnum.NORMAL_ORDER.getCode()){  //只针对已报价工厂拒绝报价
								continue;
							}else{
								supplierQuoteInfoMapper.updateQuoteStatus(QuoteOrderStatusEnum.REFUSE_ORDER.getCode(),refuseReason,DateFormat.format(),projectId);
								//发送报价失败消息提醒
								sendMessage("您报价的项目号为 "+quoteInfo.getOrderId()+" 项目报价失败，原因如下："+refuseReason,quoteInfo.getCustomerId(),s1.getFactoryId(),quoteInfo.getOrderId());
							}
						}
					}
					//发送报价成功消息提醒
					sendSuccessMessage("您报价的项目号为 "+quoteInfo.getOrderId()+" 项目已授盘,请及时上传进度报告",quoteInfo.getCustomerId(),factoryId,quoteInfo.getOrderId(),projectId,"您报价的项目号为 "+quoteInfo.getOrderId()+" 项目已授盘");
				}else{
					//发送报价成功消息提醒(返单项目，系统无报价)
					sendSuccessMessage("您报价的项目号为 "+ projectId +" 项目已授盘,请及时上传进度报告", INNER_FACTORY_ID ,factoryId ,null,projectId,"您报价的项目号为 "+quoteInfo.getOrderId()+" 项目已授盘");
				}


				//微信公众号推送消息
				RpcSynSuccessNote sysn = new RpcSynSuccessNote();
				sysn.sendRequest("",projectId,factoryId);
            }
            if(quoteInfo!=null){
				quoteInfo.setConfirmFactoryId(factoryId);
				quoteInfo.setOrderFactoryName(factoryName);
				quoteInfo.setTotalAmount(Double.parseDouble(totalAmount));
				quoteInfo.setOrderEndDate(DateFormat.currentDate());
				quoteInfo.setOrderStatus(OrderStatusEnum.CONFIRM.getCode());  //更新项目状态为已完成
				state = quoteInfoMapper.updateByPrimaryKeySelective(quoteInfo);
				QuoteInfo quoteInfoEn = TranslateExecutor.translateQuote(quoteInfo);
				quoteInfoEnMapper.updateByPrimaryKeySelective(quoteInfoEn);
			}

			//保存到下单工厂
			OrderFactory orderFactory = new OrderFactory();
			orderFactory.setCsgOrderId(projectId);
			orderFactory.setOrderAmount(Double.parseDouble(totalAmount));
			orderFactory.setOrderFactoryId(factoryId);
			orderFactory.setOrderFactoryName(factoryName);
			orderFactory.setOrderId(quoteInfo.getOrderId());
			orderFactory.setOrderTime(DateFormat.currentDate());
			orderFactoryMapper.insertSelective(orderFactory);

			return state;
		}

		@Override
		public int updateQuote(String projectId, String followStatus) throws Exception {

		   int state = 0;
           QuoteInfo quoteInfo = quoteInfoMapper.queryByCgsOrderId(projectId);
           if(StringUtils.isNotBlank(followStatus)){
        	   Integer status = Integer.parseInt(followStatus);
        	   quoteInfo.setFollowStatusQuotation(status);
        	   quoteInfo.setFollowDetail(FollowStatusQuotationEnum.getEnum(status).getValue());
        	   quoteInfo.setFollowTime(DateFormat.format());
        	   
        	   //如果询盘状态为死亡，更新快制造询盘状态
        	   if(status == FollowStatusQuotationEnum.OTHER_DEATH.getCode() || status == FollowStatusQuotationEnum.HIGH_PRICE_DEATH.getCode()){
        		   quoteInfo.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
                   supplierQuoteInfoMapper.updateQuoteStatus(QuoteOrderStatusEnum.REFUSE_ORDER.getCode(),FollowStatusQuotationEnum.getEnum(status).getValue(),DateFormat.format(),projectId);
        	   }
        	  //如果询盘状态为生产，更新快制造询盘状态
        	   if(status == FollowStatusQuotationEnum.PRODUCTION.getCode()){
        		   quoteInfo.setOrderStatus(OrderStatusEnum.PROCESS.getCode());
                   String refuseReason = "其他工厂价格更便宜";
                   supplierQuoteInfoMapper.updateQuoteStatus(QuoteOrderStatusEnum.REFUSE_ORDER.getCode(),refuseReason,DateFormat.format(),projectId);
        	   }
        	  	  //如果询盘状态为完成，更新快制造询盘状态
        	   if(status == FollowStatusQuotationEnum.QUOTE_COMPLETE.getCode()){
        		   quoteInfo.setOrderStatus(OrderStatusEnum.CONFIRM.getCode());
                   String refuseReason = "其他工厂价格更便宜";
                   supplierQuoteInfoMapper.updateQuoteStatus(QuoteOrderStatusEnum.REFUSE_ORDER.getCode(),refuseReason,DateFormat.format(),projectId);
        	   }
        	   
        	   state = quoteInfoMapper.updateByPrimaryKeySelective(quoteInfo);

			   QuoteInfo quoteInfoEn = TranslateExecutor.translateQuote(quoteInfo);
			   quoteInfoEnMapper.updateByPrimaryKeySelective(quoteInfoEn);
           }			
			return state;
		}

		@Override
		public <T> List<?> queryOrderFactoryList(Integer... item) {
			return quoteInfoMapper.queryOrderFactoryList(item);
		}
		@Override
		public <T> List<?> queryOrderFactoryListEn(Integer... item) {
			return quoteInfoEnMapper.queryOrderFactoryList(item);
		}

		
		@Transactional
		@Override
		public int updateQuoteAssistant(String projectId,String assistant)throws Exception {
			
			
			//查看订单、内部人员关联表
			//更新关联关系
			UserOrder userOrder = userOrderMapper.selectAssistantByCgsOrderId(projectId);
			FactoryUser factoryUser = factoryUserMapper.selectByRealName(assistant);
			int count = 0;
			//更新报价单中报价助理
			QuoteInfo quoteInfo = quoteInfoMapper.queryByCgsOrderId(projectId);
			if(StringUtils.isNotBlank(assistant) && quoteInfo!=null){
				quoteInfo.setPriceAssistant(assistant);
				quoteInfo.setContactTel(factoryUser.getTel());
				quoteInfoMapper.updateByPrimaryKeySelective(quoteInfo);
				QuoteInfo quoteInfoEn = TranslateExecutor.translateQuote(quoteInfo);
				quoteInfoEnMapper.updateByPrimaryKeySelective(quoteInfoEn);
			}
			

			if(factoryUser != null && userOrder != null){
				userOrder.setFactoryUserId(factoryUser.getId());
				count = userOrderMapper.updateByPrimaryKey(userOrder);
			}else if(factoryUser != null && userOrder == null){
				UserOrder order = new UserOrder(); 
				order.setCgsOrderId(projectId);
				order.setFactoryUserId(factoryUser.getId());
				order.setOrderId(quoteInfo.getOrderId());
				count = userOrderMapper.insert(order);
			}
			
			return count;
		}


		/**
		 * 内部报价同步项目状态
		 * @param followStatus
		 * @param projectIds
		 * @return
		 */
		@Override
		@Transactional
		public int updateQuoteStatusBatch(String followStatus, String projectIds) {
			String followDetail = "";
			Integer orderStatus = OrderStatusEnum.NORMAL_ORDER.getCode();
			Integer status = null;
			if(StringUtils.isNotBlank(followStatus) && StringUtils.isNotBlank(projectIds)){
				status = Integer.parseInt(followStatus);
				followDetail = FollowStatusQuotationEnum.getEnum(Integer.parseInt(followStatus)).getValue();
				 
        	   //如果询盘状态为死亡，更新快制造询盘状态
        	   if(status == FollowStatusQuotationEnum.OTHER_DEATH.getCode() || status == FollowStatusQuotationEnum.HIGH_PRICE_DEATH.getCode()){
        		   orderStatus = OrderStatusEnum.CANCEL.getCode();
			   }
        	  //如果询盘状态为生产，更新快制造询盘状态
        	   if(status == FollowStatusQuotationEnum.PRODUCTION.getCode()){
        		   orderStatus = OrderStatusEnum.PROCESS.getCode();
        	   }
        	   //如果询盘状态为完成，更新快制造询盘状态
        	   if(status == FollowStatusQuotationEnum.QUOTE_COMPLETE.getCode()){
        		   orderStatus = OrderStatusEnum.CONFIRM.getCode();
        	   }

	        	//更新时间
        	    String followTime = DateFormat.format();
        	     
				String[] split = projectIds.split(",");
				//去除已完成、生产中、授盘中项目
				List<String> strs = Arrays.asList(split);
				Iterator<String> it = strs.iterator();
				while (it.hasNext())
				{
					QuoteInfo quoteInfo = quoteInfoMapper.queryByCgsOrderId(it.next());
					if(quoteInfo != null){
						if(quoteInfo.getOrderStatus() == OrderStatusEnum.PROCESS.getCode() || quoteInfo.getOrderStatus() == OrderStatusEnum.CONFIRM.getCode() || quoteInfo.getOrderStatus() == OrderStatusEnum.DECISION.getCode()){
							it.remove();
						}
					}
				}
				split = strs.toArray(new String[0]);
				int state = quoteInfoMapper.updateQuoteStatusBatch(orderStatus,status,followDetail,followTime,split);
				if(status != FollowStatusQuotationEnum.MONTH_NO_UPDATE.getCode()){
					supplierQuoteInfoMapper.updateQuoteStatusBatch(QuoteOrderStatusEnum.REFUSE_ORDER.getCode(), followDetail, followTime, split);
				}

				return state;
			}			
			return 0;
		}

		@Override
		public QuoteInfo queryByCgsOrderId(String cgsOrderId) {
			return quoteInfoMapper.queryByCgsOrderId(cgsOrderId);
		}

	@Override
	public List<QuoteInfo> queryFinishByDateAndPrice(Double amount, String startTime, String endTime) {
		return quoteInfoMapper.queryFinishByDateAndPrice(amount,startTime,endTime);
	}

	@Transactional
	@Override
	public int updateQuoteByCsgOrderId(String cgsOrderId) {
		quoteInfoMapper.updateQuoteByCsgOrderId(cgsOrderId);
		return quoteInfoEnMapper.updateQuoteByCsgOrderId(cgsOrderId);
	}

	@Override
	public int queryWeekQuoteCount() {
		return quoteInfoMapper.queryWeekQuoteCount();
	}


	//发送询盘状态改变消息
	@Transactional
	public void sendMessage(String messageDetails,String senderId,String receiverId,Integer orderId) throws Exception {
		NoteMessage noteMessage = new NoteMessage();
		noteMessage.setMessageTitle("您报价的询盘号为"+orderId+"有新的更新");
		noteMessage.setMessageDetails(messageDetails);
		noteMessage.setSendId(senderId);
		noteMessage.setReceiverId(receiverId);
		noteMessage.setCreateTime(DateFormat.format());
		noteMessage.setOrderId(orderId);
		noteMessage.setUrl(GetServerPathUtil.getServerPath()+"/rfq/"+orderId);
		DialogueIds did = new DialogueIds();
		noteMessageMapper.returnDialogueId(did);
		int dialogueId = did.getId();
		noteMessage.setDialogueId(dialogueId);
		noteMessageMapper.insertSelective(noteMessage);
		//翻译后保存
		noteMessage = TranslateExecutor.translateMessage(noteMessage);
		noteMessageEnMapper.insertSelective(noteMessage);
	}



	//发送询盘状态改变消息
	@Transactional
	public void sendSuccessMessage(String messageDetails,String senderId,String receiverId,Integer orderId,String projectNo,String title) throws Exception {
		NoteMessage noteMessage = new NoteMessage();
		noteMessage.setMessageTitle(title);
		noteMessage.setMessageDetails(messageDetails);
		noteMessage.setSendId(senderId);
		noteMessage.setReceiverId(receiverId);
		noteMessage.setCreateTime(DateFormat.format());
		noteMessage.setOrderId(orderId);
		//如果第三方询盘 跳详情页
		if(orderId!=null && projectNo == null){
			noteMessage.setUrl(GetServerPathUtil.getServerPath()+"/rfq/"+orderId);
		}else{
			String supplierId = Base64Encode.getBase64(receiverId);
			noteMessage.setUrl(GetServerPathUtil.getServerPath()+"/report/reportList?csgOrderId="+projectNo+"&supplierId="+supplierId);
		}
		
		DialogueIds did = new DialogueIds();
		noteMessageMapper.returnDialogueId(did);
		int dialogueId = did.getId();
		noteMessage.setDialogueId(dialogueId);
		noteMessageMapper.insertSelective(noteMessage);
		//翻译后保存
		noteMessage = TranslateExecutor.translateMessage(noteMessage);
		noteMessageEnMapper.insertSelective(noteMessage);
	}
}
