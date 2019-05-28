package com.cbt.enums;
/**
 * 订单状态
 * @author Administrator
 *
 */
public enum ProjectStageEnum {
	/**
	 * 进度报告
	 */
	PROGRESS_REPORT(1,"进度报告"),
	/**
	 * 材质证明
	 */
	MATERIAL_REPORT(2,"材质证明"),
	/**
	 * 出货质量报告
	 */
	QUANLITY_REPORT(3,"出货质量报告"),
	/**
	 * 出运报告
	 */
	SHIPPING_REPORT(4,"出运报告");


	private int code;
	public int getCode() {
		return code;
	}

	public String getValue() {
		return value;
	}

	private String value;

	ProjectStageEnum(int code, String value){
		this.code = code;
		this.value = value;
	}
	
	public static ProjectStageEnum getEnum(int code) {
		for(ProjectStageEnum sourceEnum:  ProjectStageEnum.values()) {
	    	if(sourceEnum.getCode() == code) return sourceEnum;
	    }
		return null;
	}
}
