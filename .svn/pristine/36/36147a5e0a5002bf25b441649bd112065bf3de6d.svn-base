package com.cbt.service.impl;


import com.cbt.dao.InquiryClickLogMapper;
import com.cbt.entity.InquiryClickLog;
import com.cbt.service.InquiryClickLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InquiryClickLogServiceImpl implements InquiryClickLogService {

    @Autowired
    private InquiryClickLogMapper inquiryClickLogMapper;

    @Override
    public int insertSelective(InquiryClickLog record) {
        return inquiryClickLogMapper.insertSelective(record);
    }

    @Override
    public InquiryClickLog selectByPrimaryKey(Integer id) {
        return inquiryClickLogMapper.selectByPrimaryKey(id);
    }
}
