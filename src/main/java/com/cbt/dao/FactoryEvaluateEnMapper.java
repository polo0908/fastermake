package com.cbt.dao;

import com.cbt.entity.FactoryEvaluate;
import com.cbt.entity.FactoryEvaluateWithBLOBs;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FactoryEvaluateEnMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FactoryEvaluateWithBLOBs record);

    int insertSelective(FactoryEvaluateWithBLOBs record);

    FactoryEvaluateWithBLOBs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FactoryEvaluateWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(FactoryEvaluateWithBLOBs record);

    int updateByPrimaryKey(FactoryEvaluate record);

    List<FactoryEvaluateWithBLOBs> selectByFactoryId(@Param(value = "factoryId") String factoryId, @Param(value = "viewId") String viewId);

    int selectCountByFactoryId(@Param(value = "factoryId") String factoryId);

    /*
     * 点赞操作
     *
     */
    int selectEvaluatePreference(@Param(value = "factoryEvaluateId") String factoryEvaluateId, @Param(value = "viewId") String viewId);


    int insertEvaluatePreference(@Param(value = "factoryEvaluateId") String factoryEvaluateId, @Param(value = "viewId") String viewId);


    int deleteEvaluatePreference(@Param(value = "factoryEvaluateId") String factoryEvaluateId, @Param(value = "viewId") String viewId);
    
    
    int selectCountEvaluateById(@Param(value = "factoryEvaluateId") String factoryEvaluateId);
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}