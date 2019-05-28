package com.cbt.service.impl;

import com.cbt.dao.QuoteReportTypeMapper;
import com.cbt.entity.QuoteReportType;
import com.cbt.service.QuoteReportTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuoteReportTypeServiceImpl implements QuoteReportTypeService{

    @Autowired
    private QuoteReportTypeMapper quoteReportTypeMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return quoteReportTypeMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(QuoteReportType record) {
        return quoteReportTypeMapper.insert(record);
    }

    @Override
    public int insertSelective(QuoteReportType record) {
        return quoteReportTypeMapper.insertSelective(record);
    }

    @Override
    public QuoteReportType selectByPrimaryKey(Integer id) {
        return quoteReportTypeMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(QuoteReportType record) {
        return quoteReportTypeMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(QuoteReportType record) {
        return quoteReportTypeMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<QuoteReportType> queryByOrderId(String csgOrderId, Integer orderId) {
        return quoteReportTypeMapper.queryByOrderId(csgOrderId,orderId);
    }

    @Override
    public List<QuoteReportType> queryByOrderIdAndFactoryId(String csgOrderId, Integer orderId, String factoryId) {
        return quoteReportTypeMapper.queryByOrderIdAndFactoryId(csgOrderId,orderId,factoryId);
    }

    @Override
    public List<QuoteReportType> selectByOrderIdAndStage(String csgOrderId, Integer orderId, Integer projectStage) {
        return quoteReportTypeMapper.selectByOrderIdAndStage(csgOrderId,orderId,projectStage);
    }
}
