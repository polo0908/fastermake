package com.cbt.service.impl;

import com.cbt.dao.*;
import com.cbt.entity.Equipment;
import com.cbt.entity.FactoryInfo;
import com.cbt.entity.FactoryUser;
import com.cbt.entity.Qualification;
import com.cbt.exception.NameOrPasswordException;
import com.cbt.service.FactoryInfoService;
import com.cbt.translate.TranslateExecutor;
import com.cbt.util.CalculateFactoryScore;
import com.cbt.util.DateFormat;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Edit 修改查询有意向报价工厂（去除当前登录公司）
 * @ClassName FactoryInfoService 
 * @Description
 * @author polo
 * @date 2018年4月8日 下午1:22:21
 */

@Service
public class FactoryInfoServiceImpl implements FactoryInfoService {
	
	@Autowired
	private FactoryInfoMapper factoryInfoMapper;
	@Autowired
	private FactoryInfoEnMapper factoryInfoEnMapper;
	@Autowired
	private FactoryUserMapper factoryUserMapper;
	@Autowired
	private EquipmentMapper equipmentMapper;
	@Autowired
	private QualificationMapper qualificationMapper;

	private static final Integer PERMISSION = 1;       //初始注册权限为管理员
	
	
	@Transactional
	@Override
	public FactoryInfo login(String loginEmail, String pwd,String openid,String unionid)throws NameOrPasswordException {
		//入口参数检查
		if(loginEmail==null || loginEmail.trim().isEmpty()){
			throw new NameOrPasswordException("邮箱不能为空");
		}
		if(pwd==null || pwd.trim().isEmpty()){
			throw new NameOrPasswordException("密码不能为空");
		}
		//从业务层查询用户信息
		FactoryUser factoryUser = factoryUserMapper.selectByLoginEmail(loginEmail);
		FactoryInfo factoryInfo = new FactoryInfo();
		if(factoryUser != null){
			factoryInfo = factoryInfoMapper.selectFactoryInfo(factoryUser.getFactoryId());
		}		
		if(factoryUser==null){
			throw new NameOrPasswordException("邮箱未注册，请先注册");
		}else if(factoryInfo.getIsVip() == 105){
			throw new NameOrPasswordException("账号被屏蔽，请与管理员联系");
		}
		if(factoryUser.getPwd().equals(pwd)){	
			
			//如果unionid不存在，不进行操作
			if(StringUtils.isNotBlank(unionid)){
				//如果openid不为空已存在，不更新工厂表数据，否则更新工厂表openid
				if(StringUtils.isNotBlank(openid)){
					factoryInfoMapper.updateByOpenid(openid);
					factoryUserMapper.updateByOpenid(openid);
				}
				factoryInfo.setOpenid(openid);
				factoryInfo.setUpdateTime(DateFormat.format());
				factoryInfoMapper.updateByPrimaryKeySelective(factoryInfo);
				factoryUser.setOpenid(openid);
				factoryUser.setUpdateTime(DateFormat.format());
				factoryUser.setUnionid(unionid);
				factoryUserMapper.updateByPrimaryKeySelective(factoryUser);
			}			
			return factoryInfo;                    //登录成功
		}else{
			throw new NameOrPasswordException("账号或者密码错误");
		}

	}

	@Override
	public int selectMaxId() {	
		return factoryInfoMapper.selectMaxId();
	}


	
	@Transactional
	@Override
	public FactoryUser insertSelective(FactoryInfo record) throws Exception {
		FactoryUser user = factoryUserMapper.selectByLoginEmail(record.getEmail());		
		if(user != null){
			throw new RuntimeException("账号已存在");
		}
		if(record != null){
			record.setCreateTime(DateFormat.format());
			int flag = factoryInfoMapper.insertSelective(record);
			String factoryId = null;
			if(flag == 1){
			 String currentDay = DateFormat.currentDate().replace("-", "");	
			 factoryId = "f" + currentDay + record.getId();
			 record.setFactoryId(factoryId);
			 factoryInfoMapper.updateByPrimaryKeyWithBLOBs(record);
			}

			//工厂评分更新
			Integer score = CalculateFactoryScore.getScore(record);
			record.setFactoryScore(score);

			//工厂翻译数据
			FactoryInfo factoryInfo = TranslateExecutor.translateFactoryInfo(record);
			factoryInfoEnMapper.insertSelective(factoryInfo);

			FactoryUser factoryUser = new FactoryUser();
			factoryUser.setEmail(record.getEmail());
			factoryUser.setFactoryId(factoryId);
			factoryUser.setPermission(PERMISSION);
			factoryUser.setPwd(record.getPwd());
			factoryUser.setUsername(record.getUsername());
			factoryUser.setOpenid(record.getOpenid());
			factoryUser.setFacebookId(record.getFacebookId());
			factoryUser.setGoogleId(record.getGoogleId());
			factoryUserMapper.insert(factoryUser);
			return factoryUser;
		}else{
			throw new RuntimeException("注册失败");
		}		
	}


	@Transactional
	@Override
	public int updateByPrimaryKeySelective(FactoryInfo record) throws Exception {
		//资格认证列表
		List<Qualification> qualifications = qualificationMapper.queryByFactory(record.getFactoryId());
		record.setQualificationList(qualifications);
		//设备列表
		List<Equipment> equipments = equipmentMapper.selectFactoryEquipment(record.getFactoryId());
		record.setEquipmentList(equipments);
		//工厂评分更新
		Integer score = CalculateFactoryScore.getScore(record);
		record.setFactoryScore(score);
		factoryInfoMapper.updateByPrimaryKeySelective(record);
		//工厂翻译数据
		FactoryInfo recordEn = TranslateExecutor.translateFactoryInfo(record);



		return factoryInfoEnMapper.updateByPrimaryKeySelective(recordEn);
	}


	@Override
	public FactoryInfo selectByLoginEmail(String loginEmail) {
		return factoryInfoMapper.selectByLoginEmail(loginEmail);
	}
    /**
     * 查询工厂供应商基本信息
     */
	@Override
	public FactoryInfo selectFactoryInfo(String factoryId) {
		return factoryInfoMapper.selectFactoryInfo(factoryId);
	}

	@Override
	public FactoryInfo selectFactoryInfoEn(String factoryId) {
		return factoryInfoEnMapper.selectFactoryInfo(factoryId);
	}

	@Override
	public FactoryInfo selectFactoryInfoAndEquipment(String factoryId) {

		return factoryInfoMapper.selectFactoryInfoAndEquipment(factoryId);
	}

	@Override
	public int insertFactoryPreference(String factoryId, String buyerId,String factoryName,
			String createDate) {
	
		return factoryInfoMapper.insertFactoryPreference(factoryId, buyerId,factoryName ,createDate);
	}


	@Override
	public int deleteFactoryPreference(String factoryId, String buyerId) {
	
		return factoryInfoMapper.deleteFactoryPreference(factoryId, buyerId);
	}

	@Override
	public int selectFactoryPreference(String factoryId, String buyerId) {

		return factoryInfoMapper.selectFactoryPreference(factoryId, buyerId);
	}

	@Override
	public List<FactoryInfo> queryFactoryList() {
		return factoryInfoMapper.queryFactoryList();
	}

	@Override
	public List selectFactoryListByBuyerId(String buyerId) {
	
		return factoryInfoMapper.selectFactoryListByBuyerId(buyerId);
	}

	@Override
	public List<FactoryInfo> selectByCondition(Map map) {
		
		return factoryInfoMapper.selectByCondition(map);
	}

	@Override
	public List<FactoryInfo> selectByConditionEn(Map map) {
		return factoryInfoEnMapper.selectByCondition(map);
	}

	@Override
	public int totalOrder(Map map) {
		return factoryInfoMapper.totalOrder(map);
	}
    /**
     * 删除工厂运行视频
     */
    @Transactional
	@Override
	public void delProductionVideo(FactoryInfo factoryInfo) {
		//资格认证列表
		List<Qualification> qualifications = qualificationMapper.queryByFactory(factoryInfo.getFactoryId());
		factoryInfo.setQualificationList(qualifications);
		//设备列表
		List<Equipment> equipments = equipmentMapper.selectFactoryEquipment(factoryInfo.getFactoryId());
		factoryInfo.setEquipmentList(equipments);
		//工厂评分更新
		Integer score = CalculateFactoryScore.getScore(factoryInfo);
		factoryInfo.setFactoryScore(score);
		factoryInfoMapper.delProductionVideo(factoryInfo);
	}

	@Override
	public FactoryInfo selectByPrimaryKey(Integer id) {
		return factoryInfoMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<FactoryInfo> queryByOrderIdAndQuoteMessage(Integer orderId,String factoryId) {
		return factoryInfoMapper.queryByOrderIdAndQuoteMessage(orderId,factoryId);
	}

	@Override
	public List<Map<String, Object>> queryByMainProcess(String process) {
		return factoryInfoMapper.queryByMainProcess(process);
	}

	@Transactional
	@Override
	public int updateByClick(FactoryInfo record) {
		factoryInfoMapper.updateByPrimaryKeySelective(record);
		FactoryInfo factoryInfo = factoryInfoEnMapper.selectFactoryInfo(record.getFactoryId());
		factoryInfo.setClickCounts(record.getClickCounts());
		return factoryInfoEnMapper.updateByPrimaryKeySelective(factoryInfo);
	}

	@Override
	public Map<String, Object> selectStatisticsCount(String factoryId) {
		return factoryInfoMapper.selectStatisticsCount(factoryId);
	}

	@Transactional
	@Override
	public int updateByPrimaryKeySelective(FactoryInfo record, FactoryUser factoryUser) throws Exception {
		int state = factoryInfoMapper.updateByPrimaryKeySelective(record);
		//工厂翻译数据
		FactoryInfo factoryInfo = TranslateExecutor.translateFactoryInfo(record);
		factoryInfoEnMapper.updateByPrimaryKeySelective(factoryInfo);

		if(factoryUser != null){
			factoryUserMapper.updateByPrimaryKeySelective(factoryUser);
		}
		return state;
	}
		
}
