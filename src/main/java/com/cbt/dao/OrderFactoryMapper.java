package com.cbt.dao;

import com.cbt.entity.OrderFactory;

import java.util.List;

public interface OrderFactoryMapper {

    int deleteByPrimaryKey(Integer id);


    int insert(OrderFactory record);


    int insertSelective(OrderFactory record);


    OrderFactory selectByPrimaryKey(Integer id);


    int updateByPrimaryKeySelective(OrderFactory record);


    int updateByPrimaryKey(OrderFactory record);


    /**
     * 根据项目号查询
     * @param orderId
     * @return
     */
    List<OrderFactory> selectByOrderId(Integer orderId);
}