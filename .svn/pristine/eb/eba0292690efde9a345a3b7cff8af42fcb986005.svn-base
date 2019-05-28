package com.cbt.enums;

import java.util.ArrayList;
import java.util.List;


/**
 * 地区
 * @author Administrator
 *
 */
public enum ProcessEnum {

	Injection("注塑","injection-molding"),
	Vacuum("吸塑","vacuum-forming"),
	Rotational("滚塑","rotational-molding"),
	Blow("吹塑","blow-molding"),
	Plastic("挤塑","plastic-extrusion"),
	Others_Plastic("其他塑料工艺","others-plastic"),

	Stainless("不锈钢铸造(硅溶胶、水玻璃)","stainless-steel-casting"),
	Die_casting("压铸(铝、锌、镁、铜等)","die-casting"),
	Sand_casting("熔模铸(钢、铁、铜、铝等)","sand-casting"),
	Gravity_casting("低压重力铸造(铝、铜等)","gravity-casting"),
	Powder_metallurgy("粉末冶金","powder_metallurgy"),
	Others_casting("其他铸造","others-casting"),

	Hot_forging("热锻","hot-forging"),
	Cold_forging("冷锻","cold-forging"),
	Raw_material("原料采购粗加工","raw-material-procurement"),

	Sheet_metal("其他钣金加工","other-sheet-metal-processing"),
	Laser_plasma_flame_cutting("激光等离子火焰切割","laser-plasma-flame-cutting"),
	Bending("折弯","sheet-metal-bending"),
	Tube_bending("弯管","tube-bending"),
	Welding("焊接","welding"),
	Assembly("装配","assembly"),
	Stamping("冲压","stamping"),
	Deep_drawing("深拉伸","deep-drawing"),
	Turret_punching("数冲","turret-punching"),
	Spinning("旋压","spinning"),
	Aluminum_extrusion("铝挤压","raw-material-procurement"),

	CNC_machining("3轴以上精密加工中心","CNC-machining-center"),
	Lathe_machine("高速车床","high-speed-lathe-machine"),
	Milling_machine("高速铣床","high-speed-milling-machine"),
	Ordinary_lathe("普通车铣钻磨","ordinary-lathe"),
	Others("其他机加工","others"),


	Anodizing("阳极氧化","anodizing"),
	Spray_paint("喷塑","spray-paint"),
	Electroplating("电镀","electroplating"),
	Hot_dip_Galvanizing("热浸锌","hot-dip-Galvanizing"),
	Polishing_or_wiredrawing("抛光或拉丝","polishing-or-wiredrawing"),
	Other_surface_treatment("其他表面处理","other-surface-treatment"),

	Electronics("电子","electronics"),
	Retail_packaging("零售包装","retail-packaging"),

	Prompt_goods ("现成商品采购","prompt-goods"),
	Other_purchases("其他采购","other-purchases");


	private String str;
	public String getStr() {
		return str;
	}

	public String getValue() {
		return value;
	}

	private String value;

	ProcessEnum(String str, String value){
		this.str = str;
		this.value = value;
	}
	
	public static ProcessEnum getEnum(String str) {
		for(ProcessEnum sourceEnum:  ProcessEnum.values()) {
	    	if(str.equals(sourceEnum.getStr())) return sourceEnum;
	    	if(str.equals(sourceEnum.getValue())) return sourceEnum;
	    }
		return null;
	}


   public static String getStr(String str){
	   for(ProcessEnum sourceEnum:  ProcessEnum.values()) {
		   if(str.equals(sourceEnum.getValue())) return sourceEnum.getStr();
	   }
	   return null;
   }


	/**
	 * 根据后缀获取工艺中英文列表
	 * @param str
	 * @return
	 */
   public static List<String> getProcess(String str){
   	   List<String> strs = new ArrayList<String>();
	   for(ProcessEnum sourceEnum:  ProcessEnum.values()) {
		   if(str.equals(sourceEnum.getValue())){
		   	  strs.add(sourceEnum.getStr());
			   ProcessZhAndEnEnum processEnum = ProcessZhAndEnEnum.getEnum(sourceEnum.getStr());
               if(processEnum != null){
				   strs.add(processEnum.getValue());
			   }
		   }
	   }
	   return strs;
   }
}
