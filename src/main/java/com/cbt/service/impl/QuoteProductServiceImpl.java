package com.cbt.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.cbt.dao.*;
import com.cbt.translate.TranslateExecutor;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cbt.entity.FactoryUser;
import com.cbt.entity.QuoteInfo;
import com.cbt.entity.QuoteProduct;
import com.cbt.entity.UserOrder;
import com.cbt.enums.OrderStatusEnum;
import com.cbt.service.QuoteProductService;


@Service
public class QuoteProductServiceImpl implements QuoteProductService {

	@Autowired
	private QuoteProductMapper quoteProductMapper;
	@Autowired
	private QuoteProductEnMapper quoteProductEnMapper;
	@Autowired
	private QuoteInfoMapper quoteInfoMapper;
	@Autowired
	private QuoteInfoEnMapper quoteInfoEnMapper;
	@Autowired
	private FactoryUserMapper factoryUserMapper;
	@Autowired
	private UserOrderMapper userOrderMapper;
	
	private static final Integer NORMAL = 10;
	private static final Integer PRICE_ASSISTANT = 20;
	@Transactional
	@Override
	public int deleteByPrimaryKey(Integer id) {

		quoteProductEnMapper.deleteByPrimaryKey(id);
		return quoteProductMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(QuoteProduct record) throws Exception {
		quoteProductMapper.insert(record);
         //产品翻译
		record = TranslateExecutor.translateQuoteProduct(record);
		return quoteProductEnMapper.insert(record);
	}

	@Override
	public QuoteProduct selectByPrimaryKey(Integer id) {
		return quoteProductMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(QuoteProduct record) throws Exception {
		quoteProductMapper.updateByPrimaryKeySelective(record);
		//产品翻译
		QuoteProduct recordEn = TranslateExecutor.translateQuoteProduct(record);
		return quoteProductEnMapper.updateByPrimaryKeySelective(recordEn);
	}

	@Override
	public List<QuoteProduct> queryByOrderId(Integer orderId) {
		return quoteProductMapper.queryByOrderId(orderId);
	}

	@Override
	public List<QuoteProduct> queryByOrderIdEn(Integer orderId) {
		return quoteProductEnMapper.queryByOrderId(orderId);
	}

	@Override
	public List<QuoteProduct> queryProductGroupByOrderId(Integer start,
			Integer pageSize, Integer orderStatus, String process,
			String product,Integer customerType,String factoryId,String bigBuyerId) {
		return quoteProductMapper.queryProductGroupByOrderId(start, pageSize, orderStatus, process, product,customerType,factoryId,bigBuyerId);
	}

	@Override
	public List<QuoteProduct> queryProductGroupByOrderIdEn(Integer start, Integer pageSize, Integer orderStatus, String process, String product, Integer customerType, String factoryId, String bigBuyerId) {
		return quoteProductEnMapper.queryProductGroupByOrderId(start, pageSize, orderStatus, process, product,customerType,factoryId,bigBuyerId);
	}

	@Override
	public int totalOrder(Integer orderStatus, String process, String product,Integer customerType,String factoryId,String buyerId) {
		return quoteProductMapper.totalOrder(orderStatus, process, product,customerType,factoryId,buyerId);
	}

	@Override
	public List<QuoteProduct> queryHaveMessageOrder(Integer start, Integer pageSize, String process, String product, String factoryId) {
		return quoteProductMapper.queryHaveMessageOrder(start,pageSize,process,product,factoryId);
	}

	@Override
	public int countMessageOrder( String process, String product, String factoryId) {
		return quoteProductMapper.countMessageOrder(process,product,factoryId);
	}

	@Override
	public List<QuoteProduct> queryByFactoryGroupByOrderId(Integer start,Integer pageSize, String process, 
			String product, String factoryId,Integer quoteStatus) {
		return quoteProductMapper.queryByFactoryGroupByOrderId(start, pageSize, process, product, factoryId,quoteStatus);
	}

	@Override
	public int totalQuoteOrder(String process, String product, String factoryId,Integer quoteStatus) {		
		return quoteProductMapper.totalQuoteOrder(process, product, factoryId,quoteStatus);
	}

	@Override
	public List<QuoteProduct> queryByCollectOrderId(Integer start,
			Integer pageSize, String process, String product, String factoryId) {
		return quoteProductMapper.queryByCollectOrderId(start, pageSize, process, product, factoryId);
	}

	@Override
	public int totalCollectCount(String process, String product,
			String factoryId) {		
		return quoteProductMapper.totalCollectCount(process, product, factoryId);
	}

	
	@Transactional
	@Override
	public void insert(QuoteInfo record, List<QuoteProduct> list) throws Exception {
		
		//过滤重复的询盘	
		//当已经存在则不执行保存
		int orderCount = quoteInfoMapper.queryCountByCgsOrderId(record.getCsgOrderId(), OrderStatusEnum.NORMAL_ORDER.getCode());
		if(orderCount == 0){
			quoteInfoMapper.insert(record);
			record = TranslateExecutor.translateQuote(record);
			quoteInfoEnMapper.insert(record);

			List<QuoteProduct> listEn = new ArrayList<QuoteProduct>();
			if(list != null && list.size() != 0){
				for (QuoteProduct quoteProduct : list) {
					quoteProduct.setOrderId(record.getOrderId());
					//翻译后产品
					QuoteProduct product = TranslateExecutor.translateQuoteProduct(quoteProduct);
					listEn.add(product);
				}
				quoteProductMapper.insertList(list);
				quoteProductEnMapper.insertList(listEn);
			}
			
			//保存报价员、询盘关系
			if(StringUtils.isNotBlank(record.getQuoter())){
				FactoryUser factoryUser = factoryUserMapper.selectByRealName(record.getQuoter());
				if(factoryUser != null){
					UserOrder userOrder = new UserOrder();
					userOrder.setCgsOrderId(record.getCsgOrderId());
					userOrder.setOrderId(record.getOrderId());
					userOrder.setFactoryUserId(factoryUser.getId());
					int count = userOrderMapper.selectByFactoryUserIdAndOrderId(factoryUser.getId(), record.getOrderId());
					if(count == 0){
						userOrderMapper.insert(userOrder);
					}
				}
			}
			//保存销售、询盘关系
			if(StringUtils.isNotBlank(record.getInitialSales())){
				FactoryUser factoryUser = factoryUserMapper.selectByRealName(record.getInitialSales());
				if(factoryUser != null){
					UserOrder userOrder = new UserOrder();
					userOrder.setCgsOrderId(record.getCsgOrderId());
					userOrder.setOrderId(record.getOrderId());
					userOrder.setFactoryUserId(factoryUser.getId());
					int count  = userOrderMapper.selectByFactoryUserIdAndOrderId(factoryUser.getId(), record.getOrderId());
					if(count == 0){
						userOrderMapper.insert(userOrder);
					}
				}
			}
			//保存报价助理
			if(StringUtils.isNotBlank(record.getPriceAssistant())){
				FactoryUser factoryUser = factoryUserMapper.selectByRealName(record.getPriceAssistant());
				if(factoryUser != null){
					UserOrder userOrder = new UserOrder();
					userOrder.setCgsOrderId(record.getCsgOrderId());
					userOrder.setOrderId(record.getOrderId());
					userOrder.setFactoryUserId(factoryUser.getId());						
					int count = userOrderMapper.selectByFactoryUserIdAndOrderId(factoryUser.getId(), record.getOrderId());
					if(count == 0){
						userOrderMapper.insert(userOrder);
					}
				}
			}

			//保存采购
			if(StringUtils.isNotBlank(record.getPurchase())){
				FactoryUser factoryUser = factoryUserMapper.selectByRealName(record.getPurchase());
				if(factoryUser != null){
					UserOrder userOrder = new UserOrder();
					userOrder.setCgsOrderId(record.getCsgOrderId());
					userOrder.setOrderId(record.getOrderId());
					userOrder.setFactoryUserId(factoryUser.getId());
					int count = userOrderMapper.selectByFactoryUserIdAndOrderId(factoryUser.getId(), record.getOrderId());
					if(count == 0){
						userOrderMapper.insert(userOrder);
					}
				}
			}

		}
		
		

		
	}

	@Override
	public QuoteProduct queryByCgsQuotationId(Integer cgsQuotationId) {
		return quoteProductMapper.queryByCgsQuotationId(cgsQuotationId);
	}

	@Override
	public List<QuoteProduct> queryAllProductGroupByOrderId(
			Integer orderStatus, String factoryId) {
		return quoteProductMapper.queryAllProductGroupByOrderId(orderStatus, factoryId);
	}

	@Override
	public List<QuoteProduct> queryAllProductGroupByOrderIdEn(Integer orderStatus, String factoryId) {
		return quoteProductEnMapper.queryAllProductGroupByOrderId(orderStatus, factoryId);
	}

	@Override
	public List<QuoteProduct> queryByFactoryIdGroupByOrderIdAdmin(String process,
			String product, String factoryId,Integer orderStatus) {
		return quoteProductMapper.queryByFactoryIdGroupByOrderIdAdmin(process, product, factoryId,orderStatus);
	}

	@Override
	public List<QuoteProduct> queryByFactoryIdGroupByOrderIdAdminEn(String process, String product, String factoryId, Integer orderStatus) {
		return quoteProductEnMapper.queryByFactoryIdGroupByOrderIdAdmin(process, product, factoryId,orderStatus);
	}

	@Override
	public int totalByFactoryIdAdmin(String process, String product,
			String factoryId, Integer orderStatus) {
		return quoteProductMapper.totalByFactoryIdAdmin(process, product, factoryId, orderStatus);
	}

	@Transactional
	@Override
	public int deleteByOrderId(Integer orderId) {
		quoteProductEnMapper.deleteByOrderId(orderId);
		return quoteProductMapper.deleteByOrderId(orderId);
	}

	
	@Transactional
	@Override
	public int updateQuoteProductGroupbyOrderId(List<QuoteProduct> list,QuoteInfo quoteInfo) {
		
		try{
			if(list!=null&&list.size()>0){
				for(QuoteProduct qp :list){
					if(qp.getTargetPrice()==null){
						qp.setTargetPrice(0.0);
					}
					if(qp.getTargetMoldPrice()==null){
						qp.setTargetMoldPrice(0.0);
					}
					qp.setCustomerId(quoteInfo.getCustomerId());
					quoteProductMapper.updateByPrimaryKeySelective(qp);
					QuoteProduct qp1 = TranslateExecutor.translateQuoteProduct(qp);
					quoteProductEnMapper.updateByPrimaryKeySelective(qp1);
				}
				quoteInfoMapper.updateByPrimaryKeySelective(quoteInfo);
			}else{
				
				List<QuoteProduct> quoteProducts = quoteProductMapper.queryByOrderId(quoteInfo.getOrderId());
				for (QuoteProduct quoteProduct : quoteProducts) {
					if(quoteProduct.getTargetPrice()==null){
						quoteProduct.setTargetPrice(0.0);
					}
					if(quoteProduct.getTargetMoldPrice()==null){
						quoteProduct.setTargetMoldPrice(0.0);
					}
					quoteProduct.setCustomerId(quoteInfo.getCustomerId());
					quoteProductMapper.updateByPrimaryKeySelective(quoteProduct);
					QuoteProduct quoteProduct1 = TranslateExecutor.translateQuoteProduct(quoteProduct);
					quoteProductEnMapper.updateByPrimaryKeySelective(quoteProduct1);
				}
				quoteInfoMapper.updateByPrimaryKeySelective(quoteInfo);
			}

			//询盘翻译
			QuoteInfo quoteInfoEn = TranslateExecutor.translateQuote(quoteInfo);
			quoteInfoEnMapper.updateByPrimaryKeySelective(quoteInfoEn);
		
			return 1;
		}catch(Exception e){
			throw new RuntimeException("更新失败");
		}
		
	}

	@Override
	public List<QuoteProduct> queryFinishByFactoryGroupByOrderId(Integer start,
			Integer pageSize, String process, String product, String factoryId) {
		return quoteProductMapper.queryFinishByFactoryGroupByOrderId(start, pageSize, process, product, factoryId);
	}

	@Override
	public List<QuoteProduct> queryFinishByFactoryGroupByOrderIdEn(Integer start, Integer pageSize, String process, String product, String factoryId) {
		return quoteProductEnMapper.queryFinishByFactoryGroupByOrderId(start, pageSize, process, product, factoryId);
	}

	@Override
	public int totalFinishQuoteOrder(String process, String product,
			String factoryId) {
		return quoteProductMapper.totalFinishQuoteOrder(process, product, factoryId);
	}

	@Override
	public List<QuoteProduct> queryInvitationOrder(Integer orderStatus, String process,
			String product, String factoryId) {
		return quoteProductMapper.queryInvitationOrder(orderStatus, process, product, factoryId);
	}

	@Override
	public List<QuoteProduct> queryProductByMainProcess(Integer start,
			Integer pageSize, Integer orderStatus, List<String> processes,
			String factoryId) {
		return quoteProductMapper.queryProductByMainProcess(start, pageSize, orderStatus, processes, factoryId);
	}

	@Override
	public List<QuoteProduct> queryProductByMainProcessEn(Integer start, Integer pageSize, Integer orderStatus, List<String> processes, String factoryId) {
		return quoteProductEnMapper.queryProductByMainProcess(start, pageSize, orderStatus, processes, factoryId);
	}

	@Override
	public List<QuoteProduct> queryProductByMainProcessAndDate(Integer start,
			Integer pageSize, Integer orderStatus, List<String> processes,
			String factoryId,String startTime,String endTime) {
		return quoteProductMapper.queryProductByMainProcessAndDate(start, pageSize, orderStatus, processes, factoryId,startTime,endTime);
	}

	@Override
	public List<QuoteProduct> queryByFactoryIdGroupByOrderId(String process,
			String product, String factoryId, Integer orderStatus,
			Integer factoryUserId) {
		return quoteProductMapper.queryByFactoryIdGroupByOrderId(process, product, factoryId, orderStatus, factoryUserId);
	}

	@Override
	public List<QuoteProduct> queryByFactoryIdGroupByOrderIdEn(String process, String product, String factoryId, Integer orderStatus, Integer factoryUserId) {
		return quoteProductEnMapper.queryByFactoryIdGroupByOrderId(process, product, factoryId, orderStatus, factoryUserId);
	}

	@Override
	public int totalByFactoryId(String process, String product,
			String factoryId, Integer orderStatus, Integer factoryUserId) {
		return quoteProductMapper.totalByFactoryId(process, product, factoryId, orderStatus, factoryUserId);
	}

	@Override
	public List<QuoteProduct> queryByProcessSearch(Integer start, Integer pageSize, List<String> processes) {
		return quoteProductMapper.queryByProcessSearch(start, pageSize, processes);
	}

	@Override
	public List<QuoteProduct> queryByProcessSearchEn(Integer start, Integer pageSize, List<String> processes) {
		return quoteProductEnMapper.queryByProcessSearch(start, pageSize, processes);
	}

	@Override
	public int processSearchCount(Integer start, Integer pageSize, List<String> processes) {
		return quoteProductMapper.processSearchCount(start, pageSize, processes);
	}

	@Override
	public List<QuoteProduct> selectWeekMaxAmonut(Integer page,Integer pageSize) {
		return quoteProductMapper.selectWeekMaxAmonut(page,pageSize);
	}


}
