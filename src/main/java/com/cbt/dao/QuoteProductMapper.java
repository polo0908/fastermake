package com.cbt.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cbt.entity.QuoteProduct;

public interface QuoteProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(QuoteProduct record);
    
    int insertList(List<QuoteProduct> list);
    

    int insertSelective(QuoteProduct record);

    QuoteProduct selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(QuoteProduct record);

    int updateByPrimaryKeyWithBLOBs(QuoteProduct record);

    int updateByPrimaryKey(QuoteProduct record);
    
    int deleteByOrderId(@Param("orderId")Integer orderId);
    
    
    /**
     * 根据询盘号查询
     * @return
     */
    List<QuoteProduct> queryByOrderId(@Param("orderId")Integer orderId);
    
    
    /**
     * 根据询盘状态查询
     * @return
     */
    List<QuoteProduct> queryAllProductGroupByOrderId(@Param("orderStatus")Integer orderStatus,@Param("factoryId")String factoryId);
    
    
    /**
     * 查询邀请报价的询盘（非已报价）
     * @return
     */
    List<QuoteProduct> queryInvitationOrder(@Param("orderStatus")Integer orderStatus,@Param("process")String process,@Param("product")String product,
    		@Param("factoryId")String factoryId);
    
    
    
    /**
     * 根据询盘状态查询(根据主工艺筛选，主要用于首页更多询盘显示)
     * 2018/1/9  
     * @return
     */
    List<QuoteProduct> queryProductByMainProcess(@Param("start")Integer start,@Param("pageSize")Integer pageSize,
    		@Param("orderStatus")Integer orderStatus,@Param("processes")List<String> processes,@Param("factoryId")String factoryId);
   
    
    /**
     * 根据询盘状态查询(根据主工艺筛选，用于查询时间节点内询盘)
     * 2018/6/23  
     * @return
     */
    List<QuoteProduct> queryProductByMainProcessAndDate(@Param("start")Integer start,@Param("pageSize")Integer pageSize,
    		@Param("orderStatus")Integer orderStatus,@Param("processes")List<String> processes,@Param("factoryId")String factoryId,@Param("startTime")String startTime,@Param("endTime")String endTime);
   
    
    /**
     * 根据询盘状态查询
     * @return
     */
    List<QuoteProduct> queryProductGroupByOrderId(@Param("start")Integer start,@Param("pageSize")Integer pageSize,
    		@Param("orderStatus")Integer orderStatus,@Param("process")String process,@Param("product")String product,
    		@Param("customerType")Integer customerType,@Param("factoryId")String factoryId,@Param("bigBuyerId")String bigBuyerId);

    /**
     * 查询咨询过的项目
     * @return
     */
    List<QuoteProduct> queryHaveMessageOrder(@Param("start")Integer start,@Param("pageSize")Integer pageSize,
                                                  @Param("process")String process,@Param("product")String product,
                                                  @Param("factoryId")String factoryId);

    /**
     * 查询咨询过的项目数
     * @return
     */
    int countMessageOrder(@Param("process")String process,@Param("product")String product,
                                             @Param("factoryId")String factoryId);


    /**
     * 查询询盘数
     * @return
     */
    int totalOrder(@Param("orderStatus")Integer orderStatus,@Param("process")String process,
    		@Param("product")String product,@Param("customerType")Integer customerType,@Param("factoryId")String factoryId,@Param("bigBuyerId")String bigBuyerId);

    
    
    /**
     * 查询工厂报价中的产品 
     * @return
     */
    List<QuoteProduct> queryByFactoryGroupByOrderId(@Param("start")Integer start,@Param("pageSize")Integer pageSize,
    		@Param("process")String process,@Param("product")String product,
    		@Param("factoryId")String factoryId,@Param("quoteStatus")Integer quoteStatus);
    
    
    /**
     * 查询工厂报价询盘数
     * @return
     */
    int totalQuoteOrder(@Param("process")String process,@Param("product")String product,@Param("factoryId")String factoryId,
    		@Param("quoteStatus")Integer quoteStatus);
    
    /**
     * 查询工厂已授盘（包含已授盘、已完成、生产中）
     * @return
     */
    List<QuoteProduct> queryFinishByFactoryGroupByOrderId(@Param("start")Integer start,@Param("pageSize")Integer pageSize,
    		@Param("process")String process,@Param("product")String product,
    		@Param("factoryId")String factoryId);
    
    
    /**
     * 查询工厂已授盘（包含已授盘、已完成、生产中） 数量
     * @return
     */
    int totalFinishQuoteOrder(@Param("process")String process,@Param("product")String product,@Param("factoryId")String factoryId);
    
    
    
    /**
     * 查询收藏的询盘
     * @return
     */
    List<QuoteProduct> queryByCollectOrderId(@Param("start")Integer start,@Param("pageSize")Integer pageSize,
    		@Param("process")String process,@Param("product")String product,
    		@Param("factoryId")String factoryId);
    
    
    /**
     * 查询收藏询盘数
     * @return
     */
    int totalCollectCount(@Param("process")String process,@Param("product")String product,@Param("factoryId")String factoryId);
    
    
    
    /**
     * 
     * @return
     */
    QuoteProduct queryByCgsQuotationId(Integer cgsQuotationId);
    
    
    /**
     * 查询采购商询盘(管理员)
     * @param
     * @param
     * @param process
     * @param product
     * @param factoryId
     * @return
     */
    List<QuoteProduct> queryByFactoryIdGroupByOrderIdAdmin(@Param("process")String process,@Param("product")String product,
    		@Param("factoryId")String factoryId,@Param("orderStatus")Integer orderStatus);
    
    /**
     * 查询采购商询盘
     * @param
     * @param
     * @param process
     * @param product
     * @param factoryId
     * @return
     */
    List<QuoteProduct> queryByFactoryIdGroupByOrderId(@Param("process")String process,@Param("product")String product,
    		@Param("factoryId")String factoryId,@Param("orderStatus")Integer orderStatus,@Param("factoryUserId")Integer factoryUserId);
    
    /**
     * 客户询盘数(管理员)
     * @param process
     * @param product
     * @param factoryId
     * @return
     */
    int totalByFactoryIdAdmin(@Param("process")String process,@Param("product")String product,
    		@Param("factoryId")String factoryId,@Param("orderStatus")Integer orderStatus);
    /**
     * 客户询盘数
     * @param process
     * @param product
     * @param factoryId
     * @return
     */
    int totalByFactoryId(@Param("process")String process,@Param("product")String product,
    		@Param("factoryId")String factoryId,@Param("orderStatus")Integer orderStatus,@Param("factoryUserId")Integer factoryUserId);
    
    
    
    
    /**
     * 查询最近一周的询盘
     * @Title queryProductByWeek 
     * @Description
     * @return
     * @return List<QuoteProduct>
     */
    List<QuoteProduct> queryProductByWeek();


    /**
     * 根据主要工艺查询询盘列表（用于导航搜索）
     * 2018/9/13
     * @return
     */
    List<QuoteProduct> queryByProcessSearch(@Param("start")Integer start,@Param("pageSize")Integer pageSize,@Param("processes")List<String> processes);



    /**
     * 根据主要工艺查询询盘个数（用于导航搜索）
     * 2018/9/13
     * @return
     */
    int processSearchCount(@Param("start")Integer start,@Param("pageSize")Integer pageSize,@Param("processes")List<String> processes);

    /**
     * 查询一周金额最多的询盘
     * @return
     */
    List<QuoteProduct> selectWeekMaxAmonut(@Param("page")Integer page,@Param("pageSize")Integer pageSize);
}