package com.cbt.entity;

import java.io.Serializable;
import java.util.Date;

public class OrderFactory implements Serializable {

    private Integer id;

    private String orderFactoryId;       //下单工厂id

    private String orderFactoryName;    //下单工厂名

    private Double orderAmount;        //下单金额

    private String orderTime;           //下单时间

    private String csgOrderId;         //项目号

    private Integer orderId;           //询盘号

    private static final long serialVersionUID = 1L;

    public String getCsgOrderId() {
        return csgOrderId;
    }

    public void setCsgOrderId(String csgOrderId) {
        this.csgOrderId = csgOrderId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderFactoryId() {
        return orderFactoryId;
    }

    public void setOrderFactoryId(String orderFactoryId) {
        this.orderFactoryId = orderFactoryId == null ? null : orderFactoryId.trim();
    }

    public String getOrderFactoryName() {
        return orderFactoryName;
    }

    public void setOrderFactoryName(String orderFactoryName) {
        this.orderFactoryName = orderFactoryName == null ? null : orderFactoryName.trim();
    }

    public Double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }
}