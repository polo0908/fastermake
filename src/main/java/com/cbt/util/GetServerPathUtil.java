package com.cbt.util;

import java.io.IOException;
import java.util.Properties;

public class GetServerPathUtil {  
    private static Properties p = new Properties();  
  
    /** 
     * 读取properties配置文件信息 
     */  
    static{  
        try {  
            p.load(GetServerPathUtil.class.getClassLoader().getResourceAsStream("serverPath.properties"));  
        } catch (IOException e) {  
            e.printStackTrace();   
        }  
    }  
    /** 
     * 根据key得到value的值 
     */  
    public static String getServerPath()  
    {  
        return p.getProperty("server_path_zh");  
    }  
    
    /** 
     * 根据key得到value的值 
     */  
    public static String getServerPathEn()  
    {  
    	return p.getProperty("server_path_en");  
    }  
    
    
    /**
     * 根据key得到value的值
     */
    public static String getServerRealPathZh()
    {
    	return p.getProperty("server_real_path_zh");
    }
    /**
     * 根据key得到value的值
     */
    public static String getServerRealPathEn()
    {
    	return p.getProperty("server_real_path_en");
    }
    /**
     * 根据key得到value的值
     */
    public static String getServerRealPathEn2()
    {
    	return p.getProperty("server_real_path_en_2");
    }


    /**
     * 根据key得到value的值 
     */  
    public static String getNbmailPath()  
    {  
    	return p.getProperty("nbmail_path");  
    }  
    
    
    /** 
     * 根据key得到value的值 
     */  
    public static String getInnerServerPath()  
    {  
    	return p.getProperty("inner_quotaion_server");  
    }  

    /**
     * 根据key得到value的值
     */
    public static String getTaskServerPath()
    {
    	return p.getProperty("task_server");
    }

 
    
   
}  
	
	



