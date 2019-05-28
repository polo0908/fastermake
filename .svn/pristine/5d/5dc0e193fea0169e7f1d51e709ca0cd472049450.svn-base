package com.cbt.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cbt.entity.SupplierQuoteInfo;

public interface SupplierQuoteInfoMapper {
	
	
    int deleteByPrimaryKey(Integer id);

    int insert(SupplierQuoteInfo record);

    int insertSelective(SupplierQuoteInfo record);

    SupplierQuoteInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SupplierQuoteInfo record);

    int updateByPrimaryKey(SupplierQuoteInfo record);
    
    
    /**
     * 
     * @Title updateByFactoryIdAndOrderId 
     * @Description TODO
     * @param
     * @return
     * @return int
     */
    int updateByFactoryIdAndOrderId(@Param("orderId")Integer orderId,@Param("factoryId")String factoryId);
    
    /**
     * 查询工厂报价
     * @param orderId
     * @param factoryId
     * @return
     */
    SupplierQuoteInfo queryByOrderIdAndFactoryId(@Param("orderId")Integer orderId,@Param("factoryId")String factoryId);
    
    
    
    
    /**
     * 查询订单报价工厂数
     * @return
     */
    int totalQuoteFactory(Integer orderId); 
    
   
    /**
     * 根据询盘号查询工厂报价
     * @Title queryByOrderId 
     * @Description TODO
     * @param orderId
     * @return
     * @return List<SupplierQuoteInfo>
     */
    List<SupplierQuoteInfo> queryByOrderId(@Param("orderId")Integer orderId);
    
    /**
     * 批量更新工厂报价状态
     * @Title updateOrderStatus 
     * @Description TODO
     * @param list
     * @return void
     */
    void updateOrderStatus(List<SupplierQuoteInfo> list);
    
    
    /**
     * 根据采购商ID、供应商ID、询盘号查询报价
     * @Title queryQuoteDetailByFactoryId 
     * @Description TODO
     * @param orderId
     * @param factoryId
     * @param customerId
     * @return
     * @return SupplierQuoteInfo
     */
    SupplierQuoteInfo queryQuoteDetailByFactoryId(@Param("orderId")Integer orderId,@Param("factoryId")String factoryId,@Param("customerId")String customerId);


    /**
     * 批量更新报价工厂状态（报价中工厂）
     * @param quoteStatus
     * @param refuseReasons
     * @param operationTime
     * @param strs
     * @return
     */
    int updateQuoteStatusBatch(@Param("quoteStatus")Integer quoteStatus,@Param("refuseReasons")String refuseReasons,@Param("operationTime")String operationTime,@Param("item")String[] strs);

    /**
     *单个更新报价工厂状态（报价中工厂）
     * @return
     */
    int updateQuoteStatus(@Param("quoteStatus")Integer quoteStatus,@Param("refuseReasons")String refuseReasons,@Param("operationTime")String operationTime,@Param("projectId")String projectId);
}