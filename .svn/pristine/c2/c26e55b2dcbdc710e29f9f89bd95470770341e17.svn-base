package com.cbt.enums;
/**
 * 订单状态
 * @author Administrator
 *
 */
public enum FileTypeEnum {
	/**
	 * 图片
	 */
	IMG(0,"图片"),
	/**
	 * 材质证明
	 */
	MATERIAL_FILE(1,"材质证明"),
	/**
	 * 视频
	 */
	VIDEO(2,"视频"),

	/**
	 * 质检报告
	 */
	QUANLITY_FILE(3,"质检报告");


	private int code;
	public int getCode() {
		return code;
	}

	public String getValue() {
		return value;
	}

	private String value;
	
	FileTypeEnum(int code, String value){
		this.code = code;
		this.value = value;
	}
	
	public static FileTypeEnum getEnum(int code) {
		for(FileTypeEnum sourceEnum:  FileTypeEnum.values()) {
	    	if(sourceEnum.getCode() == code) return sourceEnum;
	    }
		return null;
	}
}
