package com.cbt.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cbt.entity.QuoteWeeklyReport;

public interface QuoteWeeklyReportMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(QuoteWeeklyReport record);

    int insertSelective(QuoteWeeklyReport record);

    QuoteWeeklyReport selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(QuoteWeeklyReport record);

    int updateByPrimaryKey(QuoteWeeklyReport record);
    
    
    /**
     * 查询图片类型  根据日期显示
     * @Title queryByOrderId 
     * @Description TODO
     * @return
     * @return List<QuoteWeeklyReport>
     */
    List<QuoteWeeklyReport> queryByOrderId(@Param("orderId")Integer orderId);
    
    
    /**
     * 供应商查询所有图片 文档 
     * @Title queryByOrderIdAndSupplier 
     * @Description TODO
     * @param orderId
     * @return
     * @return List<QuoteWeeklyReport>
     */
    List<QuoteWeeklyReport> queryByOrderIdAndSupplier(@Param("orderId")Integer orderId);
    
    
    /**
     * 根据类型查询
     * @Title queryByOrderIdAndType 
     * @Description
     * @param orderId
     * @param type
     * @return
     * @return List<QuoteWeeklyReport>
     */
    List<QuoteWeeklyReport> queryByOrderIdAndType(@Param("orderId")Integer orderId,@Param("type")Integer type);
    
    
    
    
    
    
    /**
     * 根据询盘号和上传时间查询图片
     * @Title queryByOrderIdAndDate 
     * @Description
     * @param orderId
     * @param uploadDate
     * @return
     * @return List<QuoteWeeklyReport>
     */
    List<QuoteWeeklyReport> queryByOrderIdAndDate(@Param("orderId")Integer orderId,@Param("uploadDate")String uploadDate);
    
    
    
    /**
     * 批量插入周报图片
     * @Title insertPhotosBatch 
     * @Description TODO
     * @param reports
     * @return void
     */
    void insertPhotosBatch(List<QuoteWeeklyReport> reports);


    /**
     * 根据类型和阶段查询报告
     * @param csgOrderId
     * @param
     * @param
     * @return
     */
    List<QuoteWeeklyReport> queryByCsgOrderIdAndType(@Param("csgOrderId")String csgOrderId,@Param("fileType")Integer fileType,@Param("factoryId")String factoryId);



    /**
     * 根据reportTypeId查询所有报告
     * @Title queryByOrderId
     * @Description
     * @return
     * @return List<QuoteWeeklyReport>
     */
    List<QuoteWeeklyReport> queryByReportTypeId(Integer reportTypeId);


    /**
     * 查询最近上传日期
     * @param csgOrderId
     * @param factoryId
     * @return
     */
    String queryMaxUploadDate(@Param("csgOrderId")String csgOrderId,@Param("factoryId")String factoryId);


    /**
     * 查询所有报告
     * @return
     */
    List<QuoteWeeklyReport> selectAll();
}