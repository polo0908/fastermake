package com.cbt.service;
import java.text.ParseException;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cbt.entity.NoteMessage;
import com.cbt.entity.QuoteInfo;
import com.cbt.entity.SupplierQuoteInfo;

public interface QuoteInfoService {

	/**
	 * 根据id删除
	 * 
	 * @param id
	 * @return
	 */
	public int deleteByPrimaryKey(Integer id);

	/**
	 * 插入询盘
	 * 
	 * @param record
	 * @return
	 */
	public int insert(QuoteInfo record) throws Exception;

	/**
	 * 插入询盘&零件
	 * 
	 * @param
	 * @return
	 */

	public int insertAll(QuoteInfo quoteInfo) throws Exception;
        /**
	 * 更新询盘&零件
	 * 
	 * 
	 */
	public int updateAll(QuoteInfo quoteInfo) throws Exception;

	/**
	 * 根据id查询
	 * 
	 * @param id
	 * @return
	 */
	public QuoteInfo selectByPrimaryKey(Integer id);
	/**
	 * 根据id查询(en)
	 *
	 * @param id
	 * @return
	 */
	public QuoteInfo selectByPrimaryKeyEn(Integer id);

	/**
	 * 更新数据
	 * 
	 * @param record
	 * @return
	 */
	public int updateByPrimaryKey(QuoteInfo record) throws Exception;
	/**
	 * 更新数据(发送提示给采购商)
	 * 
	 * @param record
	 * @return
	 */
	public int updateByPrimaryKey(QuoteInfo record,NoteMessage note) throws Exception;
	
	
	/**
	 * 更新数据(更新询盘和零件信息  2017/11/24)
	 * 
	 * @param record
	 * @return
	 */
	public void updateByPrimaryKey(QuoteInfo record,String productList,String drawingName) throws Exception;
	
	/**
	 * 更新数据(更新询盘和供应商报价信息  2017/12/26)
	 * 
	 * @param record
	 * @return
	 */
	public void updateByPrimaryKey(QuoteInfo record,List<SupplierQuoteInfo> list) throws Exception;

	/**
	 * 根据询盘状态查询
	 * 
	 * @return
	 */
	public List<QuoteInfo> queryAllInquiryOrder(Integer start,
			 Integer pageSize,
			Integer orderStatus);
	/**
	 * 根据询盘状态查询(en)
	 *
	 * @return
	 */
	public List<QuoteInfo> queryAllInquiryOrderEn(Integer start,
			 Integer pageSize,
			Integer orderStatus);

	/**
	 * 根据询盘状态查询询盘数
	 * 
	 * @return
	 */
	public int totalNormalOrder(@Param("orderStatus") Integer orderStatus);

	/**
	 * 根据询盘号查询
	 * 
	 * @param orderId
	 * @return
	 */
	public QuoteInfo queryByOrderId(Integer orderId);
	/**
	 * 根据询盘号查询(En)
	 *
	 * @param orderId
	 * @return
	 */
	public QuoteInfo queryByOrderIdEn(Integer orderId);

	
    
    /**
     * 根据询盘号和客户ID查询
     * @param orderId
     * @return
     */
    public QuoteInfo queryByOrderIdAndFactoryId(Integer orderId,String factoryId);
    /**
     * 根据询盘号和客户ID查询(En)
     * @param orderId
     * @return
     */
    public QuoteInfo queryByOrderIdAndFactoryIdEn(Integer orderId,String factoryId);

     /*
     * 查出询盘和订单详情
     */
    public QuoteInfo selectDetailByOrderId(Integer orderId,String buyerId);


     /*
     * 查出询盘和订单详情(En)
     */
    public QuoteInfo selectDetailByOrderIdEn(Integer orderId,String buyerId);

    
    /**
     * 根据询盘状态查询
     * @return
     */
    public List<QuoteInfo> queryAll(Integer orderStatus);
    /**
     * 根据询盘状态查询(En)
     * @return
     */
    public List<QuoteInfo> queryAllEn(Integer orderStatus);

    
    /**
     * 根据询盘状态查询
     * @return
     */
    public List<QuoteInfo> selectDetailListByFactoryId(Integer orderStatus,String customerId);
    /**
     * 根据询盘状态查询(En)
     * @return
     */
    public List<QuoteInfo> selectDetailListByFactoryIdEn(Integer orderStatus,String customerId);

    
    
    /**
     * 计算一周报价价格
     * @Title calEstimatedPrice 
     * @Description 
     * @param
     * @return
     * @return double
     */
    public double calEstimatedPrice();
    

    /**
     * 更新报价（当选择下单工厂后处理）
     * @Title updateQuote 
     * @Description 
     * @param projectId
     * @param factoryId
     * @param factoryName
     * @param totalAmount
     * @return
     * @return int
     */
    int updateQuote(String projectId,String factoryId,String factoryName,String totalAmount) throws Exception;
    
    
    /**
     * 更新询盘跟进状态（从内部报价或者NBMail进行同步）
     * @Title updateQuote 
     * @Description
     * @param projectId
     * @param followStatus
     * @return
     * @return int
     */
    int updateQuote(String projectId,String followStatus) throws Exception;
    
    
    /**
     * 多种状态查询
     * @Title queryOrderFactory 
     * @Description
     * @return
     * @return List<?>
     */
    <T>List<?> queryOrderFactoryList(Integer...item);


	/**
	 * 多种状态查询
	 * @Title queryOrderFactory
	 * @Description
	 * @return
	 * @return List<?>
	 */
	<T>List<?> queryOrderFactoryListEn(Integer...item);
    
    /**
     * 内部报价修改报价助理同步数据
     * @Title updateQuoteAssistant 
     * @Description 
     * @param projectId
     * @param assistant
     * @return
     * @return int
     */
    int updateQuoteAssistant(String projectId,String assistant) throws Exception;
    
    
    /**
     * 批量更新项目状态
     * @Title updateQuoteStatusBatch 
     * @Description 
     * @param projectIds
     * @return
     * @return int
     */
    int updateQuoteStatusBatch(String orderStatus,String projectIds);
    
    
    
    /**
     * 根据项目号查询最新的询盘
     * @Title queryByCgsOrderId 
     * @Description
     * @param cgsOrderId
     * @return
     * @return QuoteInfo
     */
    QuoteInfo queryByCgsOrderId(String cgsOrderId);

	/**
	 * 根据时间金额，查询时间点内完成的询盘
	 * @param amount
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<QuoteInfo> queryFinishByDateAndPrice(Double amount,String startTime,String endTime);



	/**
	 * 更新项目状态(内部使用  已取消项目重启)
	 * @Title updateQuoteByCsgOrderId
	 * @Description
	 * @param cgsOrderId
	 * @return
	 * @return int
	 */
	int updateQuoteByCsgOrderId(String cgsOrderId);


	/**
	 * 查询上周新增询盘数
	 * @return
	 */
	int queryWeekQuoteCount();
}