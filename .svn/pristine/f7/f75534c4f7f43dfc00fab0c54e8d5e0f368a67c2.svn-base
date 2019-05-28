package com.cbt.service.impl;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cbt.dao.FactoryInfoMapper;
import com.cbt.dao.FactoryUserMapper;
import com.cbt.entity.FactoryInfo;
import com.cbt.entity.FactoryUser;
import com.cbt.entity.User;
import com.cbt.service.FactoryUserService;
import com.cbt.util.Base64Encode;
import com.cbt.util.DateFormat;
import com.cbt.util.Md5Util;
import com.cbt.wximpl.Wechatimpl;

@Service
public class FactoryUserServiceImpl implements FactoryUserService {

	@Autowired
	private FactoryUserMapper factoryUserMapper;
	@Autowired
	private FactoryInfoMapper factoryInfoMapper;
	
	private static final Integer PERMISSION = 1;       //初始注册权限为管理员
	
	@Override
	public FactoryUser selectByLoginEmail(String loginEmail) {
		return factoryUserMapper.selectByLoginEmail(loginEmail);
	}

	@Override
	public int updateByPrimaryKeySelective(FactoryUser record) {
		return factoryUserMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public FactoryUser selectByRealName(String realName) {
		return factoryUserMapper.selectByRealName(realName);
	}

	@Override
	public FactoryUser selectByOpenId(String openid) {
		return factoryUserMapper.selectByOpenId(openid);
	}

	
	
	@Override
	public FactoryUser saveFactoryUserByWechat(String tokens, String encrypt) throws ParseException, UnsupportedEncodingException {
		
		FactoryUser factoryUser = new FactoryUser();
		//验证tokens是否正确
		if(StringUtils.isNotBlank(tokens)){
			//处理token是否合格
			String[] data = tokens.split("@");
			String time = data[0];
			String md5 = data[1];
			if(md5.equals(Md5Util.encoder(time))){
				String str = Base64Encode.getFromBase64(encrypt);
				String[] strs = str.split("&", -1);
				String email = strs[0];
				String pwd = strs[1];
				String factoryName = strs[2];
				String openid = strs[3];
				String userName= strs[4];
				
				//判断当前邮箱是否已注册，注册直接绑定openid
				//如果没注册，则保存注册
				factoryUser = factoryUserMapper.selectByLoginEmail(email);
				if(factoryUser != null){
					factoryUser.setOpenid(openid);
					factoryUser.setUpdateTime(DateFormat.format());
					factoryUserMapper.updateByPrimaryKeySelective(factoryUser);
				}else{
					
					FactoryInfo factoryInfo = new FactoryInfo();
					factoryInfo.setFactoryName(factoryName);
					factoryInfo.setCreateTime(DateFormat.format());
					int flag = factoryInfoMapper.insertSelective(factoryInfo);
					String factoryId = null;
					if(flag == 1){
					 String currentDay = DateFormat.currentDate().replace("-", "");	
					 factoryId = "f" + currentDay + factoryInfo.getId();
					 factoryInfo.setFactoryId(factoryId);
					 factoryInfoMapper.updateByPrimaryKeyWithBLOBs(factoryInfo);
			      	}	
					
					
					factoryUser = new FactoryUser();
					factoryUser.setEmail(email);
					factoryUser.setPwd(pwd);
					factoryUser.setFactoryId(factoryId);
					factoryUser.setOpenid(openid);
					factoryUser.setPermission(PERMISSION);
					factoryUser.setUsername(userName);
					factoryUserMapper.insert(factoryUser);
				}
				
			}			
		}
		
		return factoryUser;
		 
	}

	@Override
	public FactoryUser selectByFactoryId(String factoryId) {		
		return factoryUserMapper.selectByFactoryId(factoryId);
	}

	@Override
	public List<FactoryUser> selectListByFactoryId(String factoryId) {
		return factoryUserMapper.selectListByFactoryId(factoryId);
	}

	@Override
	public FactoryUser selectByPrimaryKey(Integer id) {
		return factoryUserMapper.selectByPrimaryKey(id);
	}

	@Override
	public FactoryUser selectByUnionid(String unionid) {
		return factoryUserMapper.selectByUnionid(unionid);
	}


}
