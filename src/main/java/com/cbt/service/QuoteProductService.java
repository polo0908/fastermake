package com.cbt.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cbt.entity.QuoteInfo;
import com.cbt.entity.QuoteProduct;

public interface QuoteProductService {
	
	
	public int deleteByPrimaryKey(Integer id);

	public int insert(QuoteProduct record) throws Exception;


	public QuoteProduct selectByPrimaryKey(Integer id);


	public int updateByPrimaryKeySelective(QuoteProduct record) throws Exception;
	
	
	public int deleteByOrderId(Integer orderId);
	
	
    /**
     * 批量询盘产品
     * @param list
     * @return
     */
    public void insert(QuoteInfo record,List<QuoteProduct> list) throws Exception;
    
	
	
    /**
     * 根据询盘号查询
     * @return
     */
   public List<QuoteProduct> queryByOrderId(Integer orderId);
    /**
     * 根据询盘号查询
     * @return
     */
   public List<QuoteProduct> queryByOrderIdEn(Integer orderId);

   
   /**
    * 根据询盘状态查询
    * @return
    */
   public List<QuoteProduct> queryAllProductGroupByOrderId(Integer orderStatus,String factoryId);

   /**
    * 根据询盘状态查询(en)
    * @return
    */
   public List<QuoteProduct> queryAllProductGroupByOrderIdEn(Integer orderStatus,String factoryId);

   
   
   /**
    * 查询邀请报价的询盘（非已报价）
    * @return
    */
   public List<QuoteProduct> queryInvitationOrder(Integer orderStatus,String process,String product,String factoryId);
   
   
   /**
    * 根据询盘状态查询(根据主工艺筛选，主要用于首页更多询盘显示)
    * 2018/1/9  
    * @return
    */
   public List<QuoteProduct> queryProductByMainProcess(Integer start,Integer pageSize,Integer orderStatus,List<String> processes,String factoryId);
   
   
   /**
    * 根据询盘状态查询(根据主工艺筛选，主要用于首页更多询盘显示)
    * 2018/1/9
    * @return
    */
   public List<QuoteProduct> queryProductByMainProcessEn(Integer start,Integer pageSize,Integer orderStatus,List<String> processes,String factoryId);


   /**
    * 根据询盘状态查询(根据主工艺筛选，用于查询时间节点内询盘)
    * 2018/6/23  
    * @return
    */
   public List<QuoteProduct> queryProductByMainProcessAndDate(Integer start,Integer pageSize,Integer orderStatus,List<String> processes,String factoryId,String startTime,String endTime);
   
   
   
   
   /**
    * 根据询盘状态查询
    * @return
    */
   public List<QuoteProduct> queryProductGroupByOrderId(Integer start,Integer pageSize,
	   		Integer orderStatus,String process,String product,Integer customerType,String factoryId,String bigBuyerId);
	   
   

   /**
    * 根据询盘状态查询 （en）
    * @return
    */
   public List<QuoteProduct> queryProductGroupByOrderIdEn(Integer start,Integer pageSize,
	   		Integer orderStatus,String process,String product,Integer customerType,String factoryId,String bigBuyerId);



   /**
    * 查询询盘数
    * @return
    */
   public int totalOrder(Integer orderStatus,String process,String product,Integer customerType,String factoryId,String bigBuyerId);



    /**
     * 查询咨询过的项目
     * @return
     */
    List<QuoteProduct> queryHaveMessageOrder(Integer start,Integer pageSize,
                                             String process,String product,
                                             String factoryId);

    /**
     * 查询咨询过的项目数
     * @return
     */
    int countMessageOrder(String process,String product,
                          String factoryId);

   
   
   /**
    * 查询工厂报价中的产品 
    * @return
    */
   public List<QuoteProduct> queryByFactoryGroupByOrderId(Integer start,Integer pageSize,String process,String product,
   		String factoryId,Integer quoteStatus);
   
   
   /**
    * 查询工厂报价询盘数
    * @return
    */
   public int totalQuoteOrder(String process,String product,String factoryId,Integer quoteStatus);
   
   
   
   /**
    * 查询工厂已授盘（包含已授盘、已完成、生产中）
    * @return
    */
   public List<QuoteProduct> queryFinishByFactoryGroupByOrderId(Integer start,Integer pageSize,
   		String process,String product,String factoryId);
   /**
    * 查询工厂已授盘（包含已授盘、已完成、生产中）(en)
    * @return
    */
   public List<QuoteProduct> queryFinishByFactoryGroupByOrderIdEn(Integer start,Integer pageSize,
   		String process,String product,String factoryId);

   
   /**
    * 查询工厂已授盘（包含已授盘、已完成、生产中） 数量
    * @return
    */
   public int totalFinishQuoteOrder(String process,String product,String factoryId);
   
   
   
   /**
    * 查询收藏的询盘
    * @return
    */
   public List<QuoteProduct> queryByCollectOrderId(Integer start,Integer pageSize,
   		String process,String product,
   		String factoryId);
   
   
   /**
    * 查询收藏询盘数
    * @return
    */
   public int totalCollectCount(String process,String product,String factoryId);
   
   
   
   
   /**
    * 
    * @return
    */
   public QuoteProduct queryByCgsQuotationId(Integer cgsQuotationId);
   
   
   
   /**
    * 查询采购商询盘(管理员)
    * @param process
    * @param product
    * @param process
    * @param product
    * @param factoryId
    * @return
    */
   List<QuoteProduct> queryByFactoryIdGroupByOrderIdAdmin(String process,String product,String factoryId,Integer orderStatus);
   
   

   /**
    * 查询采购商询盘(管理员En)
    * @param process
    * @param product
    * @param process
    * @param product
    * @param factoryId
    * @return
    */
   List<QuoteProduct> queryByFactoryIdGroupByOrderIdAdminEn(String process,String product,String factoryId,Integer orderStatus);



   
   /**
    * 客户询盘数(管理员)
    * @param process
    * @param product
    * @param factoryId
    * @return
    */
   int totalByFactoryIdAdmin(String process,String product,
   		String factoryId,Integer orderStatus);
   
   
   
   /**
    * 批量更新产品
    * @Title updateQuoteProductGroupbyOrderId 
    * @Description
    * @param list
    * @param quoteInfo
    * @return
    * @return int
    */
   public int updateQuoteProductGroupbyOrderId(List<QuoteProduct> list,QuoteInfo quoteInfo);
   
   
   /**
    * 查询采购商询盘
    * @param process
    * @param product
    * @param process
    * @param product
    * @param factoryId
    * @return
    */
   public List<QuoteProduct> queryByFactoryIdGroupByOrderId(String process,String product,String factoryId,
		   Integer orderStatus,Integer factoryUserId);
   /**
    * 查询采购商询盘(en)
    * @param process
    * @param product
    * @param process
    * @param product
    * @param factoryId
    * @return
    */
   public List<QuoteProduct> queryByFactoryIdGroupByOrderIdEn(String process,String product,String factoryId,
		   Integer orderStatus,Integer factoryUserId);

   
   
   
   /**
    * 客户询盘数
    * @param process
    * @param product
    * @param factoryId
    * @return
    */
   int totalByFactoryId(String process,String product,
   		String factoryId,Integer orderStatus,Integer factoryUserId);


    /**
     * 根据主要工艺查询询盘列表（用于导航搜索）
     * 2018/9/13
     * @return
     */
    List<QuoteProduct> queryByProcessSearch(Integer start,Integer pageSize,List<String> processes);

    /**
     * 根据主要工艺查询询盘列表（用于导航搜索）(En)
     * 2018/10/10
     * @return
     */
    List<QuoteProduct> queryByProcessSearchEn(Integer start,Integer pageSize,List<String> processes);



    /**
     * 根据主要工艺查询询盘个数（用于导航搜索）
     * 2018/9/13
     * @return
     */
    int processSearchCount(Integer start,Integer pageSize,List<String> processes);


    /**
     * 查询一周金额最多的询盘
     * @return
     */
    List<QuoteProduct> selectWeekMaxAmonut(Integer page,Integer pageSize);
}