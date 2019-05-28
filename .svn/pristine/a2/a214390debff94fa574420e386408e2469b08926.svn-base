package com.cbt.dao;

import com.cbt.entity.QuoteMessage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QuoteMessageEnMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(QuoteMessage record);

    int insertSelective(QuoteMessage record);

    QuoteMessage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(QuoteMessage record);

    int updateByPrimaryKey(QuoteMessage record);




    /**
     * 根据供应商ID查询消息
     */
    List<QuoteMessage> queryMessageByFactoryId(@Param("factoryId") String factoryId,
                                               @Param("orderId") Integer orderId);


    /**
     * 根据供应商ID,项目号查询消息
     */
    List<QuoteMessage> queryMessageByCsgOrderId(@Param("factoryId")String factoryId, @Param("orderId")Integer orderId,@Param("csgOrderId")String csgOrderId);
}