package com.cbt.service;

import com.cbt.entity.InquiryClickLog;

public interface InquiryClickLogService {

    int insertSelective(InquiryClickLog record);

    InquiryClickLog selectByPrimaryKey(Integer id);
}