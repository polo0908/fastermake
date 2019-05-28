package com.cbt.service;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import com.cbt.entity.SupplierQuoteInfo;

public interface SupplierQuoteInfoService extends Serializable {

	/**
	 * 插入报价
	 * @param record
	 * @return
	 */
	 int insert(SupplierQuoteInfo record);
	
	
	/**
	 * 更新报价
	 * @param record
	 * @return
	 */
	 int updateByPrimaryKeySelective(SupplierQuoteInfo record);
	
	
	
	
	 Map<String,Object> insert(SupplierQuoteInfo record,String productList,String productIds,String productRemarks)throws UnsupportedEncodingException;
	
	
	
	
	
	/**
     * 查询工厂报价
     * @param orderId
     * @param factoryId
     * @return
     */
     SupplierQuoteInfo queryByOrderIdAndFactoryId(Integer orderId,String factoryId);
    
    
    
    /**
     * 根据ID查询
     * @param id
     * @return
     */
     SupplierQuoteInfo selectByPrimaryKey(Integer id);
    
    
    
    /**
     * 根据询盘号查询工厂报价
     * @Title queryByOrderId 
     * @Description
     * @param orderId
     * @return
     * @return List<SupplierQuoteInfo>
     */
     List<SupplierQuoteInfo> queryByOrderId(Integer orderId);
    
    
    /**
     * 批量更新工厂报价状态
     * @Title updateOrderStatus 
     * @Description
     * @param list
     * @return void
     */
     void updateOrderStatus(List<SupplierQuoteInfo> list);
    
    
    
    /**
     * 根据采购商ID、供应商ID、询盘号查询报价
     * @Title queryQuoteDetailByFactoryId 
     * @Description
     * @param orderId
     * @param factoryId
     * @param customerId
     * @return
     * @return SupplierQuoteInfo
     */
     SupplierQuoteInfo queryQuoteDetailByFactoryId(Integer orderId,String factoryId,String customerId);
    
    
    
    /**
     * 拒绝工厂理由
     * @Title updateQuote 
     * @Description
     * @param refuseReasons
     * @return void
     */
     void updateQuote(Integer orderId,String refuseReasons);
    
    /**
     * 查询订单报价工厂数
     * @return
     */
     int totalQuoteFactory(Integer orderId);
}
