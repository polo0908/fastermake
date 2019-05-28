package com.cbt.service;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.List;

import com.cbt.entity.FactoryUser;
import org.apache.ibatis.annotations.Param;

public interface FactoryUserService {
    /**
     * 根据邮箱去查询
     * @Title selectByLoginEmail 
     * @Description 
     * @param loginEmail
     * @return
     * @return FactoryUser
     */
   public FactoryUser selectByLoginEmail(String loginEmail);
   
   
   /**
    * 更新用户信息
    * @Title updateByPrimaryKeySelective 
    * @Description
    * @param record
    * @return
    * @return int
    */
   public int updateByPrimaryKeySelective(FactoryUser record);
   
   
   /**
    * 根据名称查询
    * @Title selectByRealName 
    * @Description 
    * @param realName
    * @return
    * @return FactoryUser
    */
  public FactoryUser selectByRealName(String realName);
  
  
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
   * 第三方微信登录处理方法
   * @Title saveFactoryUserByWechat 
   * @Description
   * @param tokens
   * @param encrypt
   * @return void
   */
  FactoryUser saveFactoryUserByWechat(String tokens,String encrypt)throws ParseException, UnsupportedEncodingException; 
  
  
  
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
     * 根据id查询
     * @param id
     * @return
     */
  FactoryUser selectByPrimaryKey(Integer id);


    /**
     * 根据微信unionid查询
     * @param unionid
     * @return
     */
    FactoryUser selectByUnionid(String unionid);
}
