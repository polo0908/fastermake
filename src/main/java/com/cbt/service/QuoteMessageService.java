package com.cbt.service;

import java.util.List;

import com.cbt.entity.NoteMessage;
import com.cbt.entity.QuoteMessage;
import org.apache.ibatis.annotations.Param;

public interface QuoteMessageService {
	
	
	/**
	 * 保存询盘消息
	 * @param record
	 * @return
	 */
   public int insert(QuoteMessage record,NoteMessage note) throws Exception;
	
	 /**
     * 根据供应商ID查询消息
     */
   public List<QuoteMessage> queryMessageByFactoryId(String factoryId,Integer orderId);
   
     /**
      * 根据ID查询
      * @param id
      * @return
      */
   public QuoteMessage selectByPrimaryKey(Integer id);


	/**
	 * 根据供应商ID,项目号查询消息
	 */
	List<QuoteMessage> queryMessageByCsgOrderId(String factoryId,String supplierId,Integer orderId,String csgOrderId);

}
