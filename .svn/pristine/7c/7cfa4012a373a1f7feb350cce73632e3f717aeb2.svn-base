package com.cbt.enums;

import java.util.ArrayList;
import java.util.List;


/**
 * 地区
 * @author Administrator
 *
 */
public enum ProcessZhAndEnEnum {

	Injection("注塑","Injection molding"),
	Vacuum("吸塑","Vacuum forming"),
	Rotational("滚塑","Rotational molding"),
	Blow("吹塑","Blow molding"),
	Plastic("挤塑","Plastic extrusion"),
	Others_Plastic("其他塑料工艺","Others(Plastic)"),
	
	Stainless("不锈钢铸造(硅溶胶、水玻璃)","Stainless steel casting (silicon glue, water glass)"),
	Die_casting("压铸(铝、锌、镁、铜等)","Die casting (aluminum, zinc, magnesium, copper, etc.)"),
	Sand_casting("熔模铸(钢、铁、铜、铝等)","Sand casting (steel, iron, copper, aluminum, etc.)"),
	Gravity_casting("低压重力铸造(铝、铜等)","Gravity casting (aluminum, copper, etc.)"),
	Powder_metallurgy("粉末冶金","Powder metallurgy"),
	Others_casting("其他铸造","Others(casting)"),
	
	Hot_forging("热锻","Hot forging"),
	Cold_forging("冷锻","Cold forging"),
	Raw_material("原料采购粗加工","Raw material procurement"),
	
	Sheet_metal("其他钣金加工","Other sheet metal processing"),
	Laser_plasma_flame_cutting("激光等离子火焰切割","Laser,plasma,flame cutting"),
	Bending("折弯","Sheet metal bending"),
	Tube_bending("弯管","Tube bending"),
	Welding("焊接","Welding"),
	Assembly("装配","Assembly"),
	Stamping("冲压","Stamping"),
	Deep_drawing("深拉伸","Deep drawing"),
	Turret_punching("数冲","Turret punching"),
	Spinning("旋压","Spinning"),
	Aluminum_extrusion("铝挤压","Raw material procurement"),
	
	CNC_machining("3轴以上精密加工中心","CNC machining center (3-axes or more)"),
	Lathe_machine("高速车床","High-speed lathe machine"),
	Milling_machine("高速铣床","High-speed milling machine"),
	Ordinary_lathe("普通车铣钻磨","Ordinary lathe & millilng machine"),
	Others("其他机加工","Others"),


	Anodizing("阳极氧化","Anodizing"),
	Spray_paint("喷塑","Spray paint"),
	Electroplating("电镀","Electroplating"),
	Hot_dip_Galvanizing("热浸锌","Hot-dip Galvanizing"),
	Polishing_or_wiredrawing("抛光或拉丝","Polishing or wiredrawing"),
	Other_surface_treatment("其他表面处理","Other surface treatment"),

	Electronics("电子","Electronics"),
	Retail_packaging("零售包装","Retail packaging"),


	Prompt_goods("现成商品采购","Prompt Goods"),
	Other_purchases("其他采购","Other purchases");


	private String str;
	public String getStr() {
		return str;
	}

	public String getValue() {
		return value;
	}

	private String value;
	
	ProcessZhAndEnEnum(String str, String value){
		this.str = str;
		this.value = value;
	}
	
	public static ProcessZhAndEnEnum getEnum(String str) {
		if(str != null) {
			for (ProcessZhAndEnEnum sourceEnum : ProcessZhAndEnEnum.values()) {
				if (str.equals(sourceEnum.getStr())) return sourceEnum;
				if (str.equals(sourceEnum.getValue())) return sourceEnum;
			}
		}
		return null;
	}

    //获取工艺中英文列表
	public static List<String> getProcessZhAndEn(String str){
		List<String> list = new ArrayList<String>();
		if(str != null){
			for(ProcessZhAndEnEnum sourceEnum:  ProcessZhAndEnEnum.values()) {
				if(str.equals(sourceEnum.getStr())){
					list.add(sourceEnum.getStr());
					list.add(sourceEnum.getValue());
				}
				if(str.equals(sourceEnum.getValue())){
					list.add(sourceEnum.getStr());
					list.add(sourceEnum.getValue());
				}
			}
		}
      return list;
	}



	public static List<String> getProcessList(String str){
		List<String> list = new ArrayList<String>();
		if(str != null) {
			int tl = ProcessZhAndEnEnum.values().length;
			for (int i = 0; i < tl; i++) {
				ProcessZhAndEnEnum sourceEnum = ProcessZhAndEnEnum.values()[i];
				//塑料类集合
				if ("plastic".equals(str) && i < 6) {
					list.add(sourceEnum.getValue());
					list.add(sourceEnum.getStr());
				} else if ("cast".equals(str) && i > 5 && i < 11) {   //铸造类集合
					list.add(sourceEnum.getValue());
					list.add(sourceEnum.getStr());
				} else if ("forging".equals(str) && i > 10 && i < 13) {   //锻造类集合
					list.add(sourceEnum.getValue());
					list.add(sourceEnum.getStr());
				} else if ("raw".equals(str) && i == 13) {            //原材料粗加工
					list.add(sourceEnum.getValue());
					list.add(sourceEnum.getStr());
				} else if ("metal".equals(str) && i > 13 && i < 25) {  //钣金集合
					list.add(sourceEnum.getValue());
					list.add(sourceEnum.getStr());
				} else if ("machining".equals(str) && i > 24 && i < 30) {  //机加工集合
					list.add(sourceEnum.getValue());
					list.add(sourceEnum.getStr());
				} else if ("surface".equals(str) && i > 29 && i < 36) {
					list.add(sourceEnum.getValue());
					list.add(sourceEnum.getStr());
				}else if ("electronics".equals(str) && i > 35 && i < 38) {
					list.add(sourceEnum.getValue());
					list.add(sourceEnum.getStr());
				} else if ("goods".equals(str) && i > 37 && i < 40) {
					list.add(sourceEnum.getValue());
					list.add(sourceEnum.getStr());
				}
			}
		}
		return list;
	}


	/**
	 * 获取工艺大类名称
	 * @param str
	 * @return
	 */
	public static String getType(String str){
		   String type = "";
			//塑料类
			if("plastic".equals(str)){
				type = "塑料";
			}else if("cast".equals(str)){   //铸造类集合
				type = "铸造";
			}else if("forging".equals(str)){   //锻造类集合
				type = "锻造";
			}else if("raw".equals(str)){            //原材料粗加工
				type = "锻造及原料采购粗加工";
			}else if("metal".equals(str)){
				type = "钣金加工";
			}else if("machining".equals(str)){
				type = "机加工";
			}else if("goods".equals(str)){
				type = "现货采购";
			}
          return type;
	}



	public static List<String> getProcess(int num){
		List<String> list = new ArrayList<String>();
		int tl = ProcessZhAndEnEnum.values().length;
		for(int i=0;i<tl;i++) {
			ProcessZhAndEnEnum  sourceEnum = ProcessZhAndEnEnum.values()[i];
            if(i == num){
				list.add(sourceEnum.getValue());
				list.add(sourceEnum.getStr());
			}
		}
		return list;
	}


	/**
	 * 获取工艺中文名
	 * @param num
	 * @return
	 */
	public static String getStr(int num){
		String str = "";
		int tl = ProcessZhAndEnEnum.values().length;
		for(int i=0;i<tl;i++) {
			ProcessZhAndEnEnum  sourceEnum = ProcessZhAndEnEnum.values()[i];
			if(i == num){
				str = sourceEnum.getStr();
			}
		}
		return str;
	}


	/**
	 * 获取中文工艺列表
	 * @param str
	 * @return
	 */
	public static String getProcessListZh(String str){
		StringBuffer buffer = new StringBuffer();
		if(str != null){
		int tl = ProcessZhAndEnEnum.values().length;
		for(int i=0;i<tl;i++) {
			ProcessZhAndEnEnum  sourceEnum = ProcessZhAndEnEnum.values()[i];
			//塑料类集合
			if("plastic".equals(str) && i < 6){
				buffer.append(sourceEnum.getStr()+"、");
			}else if("cast".equals(str) && i > 5 && i < 11){   //铸造类集合
				buffer.append(sourceEnum.getStr()+"、");
			}else if("forging".equals(str) && i > 10 && i < 13){   //锻造类集合
				buffer.append(sourceEnum.getStr()+"、");
			}else if("raw".equals(str) && i == 13){            //原材料粗加工
				buffer.append(sourceEnum.getStr()+"、");
			}else if("metal".equals(str) && i > 13 && i< 25){
				buffer.append(sourceEnum.getStr()+"、");
			}else if("machining".equals(str) && i > 24 && i< 30){
				buffer.append(sourceEnum.getStr()+"、");
			}else if("surface".equals(str) && i > 29 && i< 36){
				buffer.append(sourceEnum.getStr()+"、");
			}else if("electronics".equals(str) && i > 35 && i< 38){
				buffer.append(sourceEnum.getStr()+"、");
			}else if("goods".equals(str) && i > 37 && i< 40){
				buffer.append(sourceEnum.getStr()+"、");
			}
		}
		if(buffer.length() > 0){
			buffer.deleteCharAt(buffer.length() - 1);
		}
		}
		return buffer.toString();
	}



	/**
	 * 获取英文工艺列表
	 * @param str
	 * @return
	 */
	public static String getProcessListEn(String str){
		StringBuffer buffer = new StringBuffer();
		if(str != null){
		int tl = ProcessZhAndEnEnum.values().length;
		for(int i=0;i<tl;i++) {
			ProcessZhAndEnEnum  sourceEnum = ProcessZhAndEnEnum.values()[i];
			//塑料类集合
			if("plastic".equals(str) && i < 6){
				buffer.append(sourceEnum.getValue()+"、");
			}else if("cast".equals(str) && i > 5 && i < 11){   //铸造类集合
				buffer.append(sourceEnum.getValue()+"、");
			}else if("forging".equals(str) && i > 10 && i < 13){   //锻造类集合
				buffer.append(sourceEnum.getValue()+"、");
			}else if("raw".equals(str) && i == 13){            //原材料粗加工
				buffer.append(sourceEnum.getValue()+"、");
			}else if("metal".equals(str) && i > 13 && i< 25){
				buffer.append(sourceEnum.getValue()+"、");
			}else if("machining".equals(str) && i > 24 && i< 30){
				buffer.append(sourceEnum.getValue()+"、");
			}else if("surface".equals(str) && i > 29 && i< 36){
				buffer.append(sourceEnum.getValue()+"、");
			}else if("electronics".equals(str) && i > 35 && i< 38){
				buffer.append(sourceEnum.getValue()+"、");
			}else if("goods".equals(str) && i > 37 && i< 40){
				buffer.append(sourceEnum.getValue()+"、");
			}
		}
		if(buffer.length() > 0){
			buffer.deleteCharAt(buffer.length() - 1);
		}
		}
		return buffer.toString();
	}




	/**
	 * 获取中文工艺列表(逗号隔开)
	 * @param str
	 * @return
	 */
	public static String getProcessZh(String str){
		StringBuffer buffer = new StringBuffer();
		if(str != null) {
			int tl = ProcessZhAndEnEnum.values().length;
			for (int i = 0; i < tl; i++) {
				ProcessZhAndEnEnum sourceEnum = ProcessZhAndEnEnum.values()[i];
				//塑料类集合
				if ("plastic".equals(str) && i < 6) {
					buffer.append(sourceEnum.getStr() + ",");
				} else if ("cast".equals(str) && i > 5 && i < 11) {   //铸造类集合
					buffer.append(sourceEnum.getStr() + ",");
				} else if ("forging".equals(str) && i > 10 && i < 13) {   //锻造类集合
					buffer.append(sourceEnum.getStr() + ",");
				} else if ("raw".equals(str) && i == 13) {            //原材料粗加工
					buffer.append(sourceEnum.getStr() + ",");
				} else if ("metal".equals(str) && i > 13 && i< 25) {
					buffer.append(sourceEnum.getStr() + ",");
				} else if ("machining".equals(str) && i > 24 && i< 30) {
					buffer.append(sourceEnum.getStr() + ",");
				} else if ("surface".equals(str) && i > 29 && i< 36) {
					buffer.append(sourceEnum.getStr() + ",");
				} else if ("electronics".equals(str) && i > 35 && i< 38) {
					buffer.append(sourceEnum.getStr() + ",");
				} else if ("goods".equals(str) && i > 37 && i < 40) {
					buffer.append(sourceEnum.getStr() + ",");
				}
			}
			if (buffer.length() > 0) {
				buffer.deleteCharAt(buffer.length() - 1);
			}
		}
		return buffer.toString();
	}

}
