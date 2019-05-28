package com.cbt.dao;

import java.util.List;

import com.cbt.entity.FactoryUser;
import org.apache.ibatis.annotations.Param;

public interface FactoryUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FactoryUser record);

    int insertSelective(FactoryUser record);

    FactoryUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FactoryUser record);

    int updateByPrimaryKey(FactoryUser record);
        
    /**
     * 根据邮箱去查询
     * @Title selectByLoginEmail 
     * @Description 
     * @param loginEmail
     * @return
     * @return FactoryUser
     */
    FactoryUser selectByLoginEmail(String loginEmail);
    /**
     * 根据名称查询
     * @Title selectByRealName 
     * @Description 
     * @param realName
     * @return
     * @return FactoryUser
     */
    FactoryUser selectByRealName(String realName);
    
    
    /**
     * 根据openid查询
     * @Title selectByOpenId 
     * @Description
     * @param openid
     * @return
     * @return FactoryUser
     */
    FactoryUser selectByOpenId(String openid);
    
    
    
    /**
     * 根据factoryId查询(取第一个)
     * @Title selectByFactoryId 
     * @Description
     * @param factoryId
     * @return
     * @return FactoryUser
     */
    FactoryUser selectByFactoryId(String factoryId);
    
    
    
    /**
     * 查询工厂下所有用户
     * @Title selectListByFactoryId 
     * @Description
     * @param factoryId
     * @return
     * @return List<FactoryUser>
     */
    List<FactoryUser> selectListByFactoryId(String factoryId);



    /**
     * 根据openid更新
     * @param openid
     * @return
     */
    int updateByOpenid(@Param("openid")String openid);


    /**
     * 根据微信unionid查询
     * @param unionid
     * @return
     */
    FactoryUser selectByUnionid(@Param("unionid")String unionid);
    
}