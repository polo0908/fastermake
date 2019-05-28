package com.cbt.enums;
/**
 * 工厂评分标准
 * @author Administrator
 *
 */
public enum FactoryScoreEnum {

	/**
	 * 公司名称
	 */
	FACTORY_NAME("FACTORY_NAME",5),

	/**
	 * 公司名称英文
	 */
	FACTORY_NAME_EN("FACTORY_NAME_EN",5),

	/**
	 * 公司logo
	 */
	FACTORY_lOGO("FACTORY_lOGO", 5),

	/**
	 * 1688网址
	 */
	WEBSITES_1688("WEBSITES_1688",10),

	/**
	 * 电话
	 */
	TEL("TEL",5),


	/**
	 * 手机
	 */
	PHONE("PHONE",5),


	/**
	 * 营业执照
	 */
	FACTORY_LICENSE("FACTORY_LICENSE",5),


	/**
	 * 公司地址
	 */
	LOCATION("LOCATION",5),

	/**
	 * 工厂照片
	 */
	FACTORY_PIC("FACTORY_PIC",2),

	/**
	 * 成立时间
	 */
	ESTABLISHMENT_YEAR("ESTABLISHMENT_YEAR",5),
	/**
	 * 工厂人数
	 */
	STAFF_NUMBER("STAFF_NUMBER",5),
	/**
	 * 工厂面积
	 */
	FACTORY_ACREAGE("FACTORY_ACREAGE",5),
	/**
	 * 年产值
	 */
	FACTORY_VALUE("FACTORY_VALUE",5),
	/**
	 * 进出口权
	 */
	EXPORT_TYPE("EXPORT_TYPE",5),
	/**
	 * 公司网址
	 */
	WEBSITES("WEBSITES",10),
	/**
	 * 公司简介
	 */
	FACTORY_INFO("FACTORY_INFO",5),

	/**
	 * 资格认证
	 */
	CERTIFICATION("CERTIFICATION",2),

	/**
	 * 公司技术优势
	 */
	TECHNOLOGICAL_ADVANTAGE("TECHNOLOGICAL_ADVANTAGE",5),
	/**
	 * 公司技术材料
	 */
	DOMINANT_MATERIAL_MODEL("DOMINANT_MATERIAL_MODEL",5),

	/**
	 * 公司主工艺
	 */
	MAIN_PROCESS("MAIN_PROCESS",5),

	/**
	 * 工件大小
	 */
	DOMINANT_MATERIAL_SIZE("DOMINANT_MATERIAL_SIZE",5),

	/**
	 * 场地大小(暂不允许验厂)
	 */
	SITE_SIZE_1("SITE_SIZE_1",5),
	/**
	 * 场地大小(正常验厂)
	 */
	SITE_SIZE_2("SITE_SIZE_2",10),
	/**
	 * 场地大小(500强验厂)
	 */
	SITE_SIZE_3("SITE_SIZE_3",15),

	/**
	 * 合作企业
	 */
	COOPERATIVE_ENTERPRISE("COOPERATIVE_ENTERPRISE",5),

	/**
	 * 可接受任何订单
	 */
	ACCEPT_CONDITION_1("ACCEPT_CONDITION_1",15),
	/**
	 * 可接受全部定制
	 */
	ACCEPT_CONDITION_2("ACCEPT_CONDITION_1",10),
	/**
	 * 可接受部分定制
	 */
	ACCEPT_CONDITION_3("ACCEPT_CONDITION_1",5),

	/**
	 * 设备
	 */
	EQUIPMENT("EQUIPMENT",5),
	/**
	 * 设备运行视频
	 */
	EQUIPMENT_VIDEO("EQUIPMENT_VIDEO",15),
	/**
	 * 环境照片
	 */
	PRODUCTION_ENVIRONMENT("PRODUCTION_ENVIRONMENT",5);





	private String name;
	public String getName() {
		return name;
	}

	public Integer getScore() {
		return score;
	}

	private Integer score;

	FactoryScoreEnum(String name, Integer score){
		this.name = name;
		this.score = score;
	}
	
	public static Integer getEnum(String name) {
		for(FactoryScoreEnum sourceEnum:  FactoryScoreEnum.values()) {
	    	if(sourceEnum.getName().equals(name)) return sourceEnum.getScore();
	    }
		return null;
	}

}
