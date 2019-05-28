package com.cbt.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 获取当前时间
 * 
 * @since 2013-12-03
 */
public class DateFormat {

	/**
	 * 
	 * @param
	 * @return
	 */
	public static String format()
	{
		long time = System.currentTimeMillis();
	  	Date date = new Date(time);    	
	  	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	  	String currentTime = sdf.format(date);
		
		
		return currentTime;
	}

	public static String formatZh(Date date)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日");
		String currentTime = sdf.format(date);
		return currentTime;
	}

	public static String formatDate1(String date) throws ParseException
	{
//		long time = System.currentTimeMillis();
//		Date date = new Date(time);    	
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date time = sdf.parse(date); 
		String currentTime = sdf.format(time);
		
		
		return currentTime;
	}


	public static String formatDate2(String date) throws ParseException
	{
//		long time = System.currentTimeMillis();
//		Date date = new Date(time);    	
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date time = sdf.parse(date); 
		String currentTime = sdf.format(time);
		
		
		return currentTime;
	}
	public static String currentDate() throws ParseException
	{
	    long time = System.currentTimeMillis();
	    Date date = new Date(time);    	
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    String currentTime = sdf.format(date);		
		return currentTime;
	}
	
	public static String weekLaterDate() throws ParseException
	{
		   Calendar calendar = Calendar.getInstance();  
	       calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 7);  
	       Date today = calendar.getTime();  
	       SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
	       String result = format.format(today);  
	       return result;
	}
	
	public static Date formatToDate(Date date) throws ParseException
	{ 	
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String str = sdf.format(date);
		return sdf.parse(str);
	}
	
	
	public static String addDays(String dateTime,int days) throws ParseException
	{ 	
			
         SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
          Date  currdate = format.parse(dateTime);
//          System.out.println("现在的日期是：" + currdate);
          Calendar ca = Calendar.getInstance();
          ca.setTime(currdate);
          ca.add(Calendar.DATE, days);// num为增加的天数，可以改变的
          currdate = ca.getTime();
          String enddate = format.format(currdate);
//          System.out.println("增加天数以后的日期：" + enddate);
          return enddate;
	}
	
	
	
	
	public static int calHours(String date) throws ParseException
	{ 	

     SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
     String currentDate = simpleFormat.format(new Date());  
     String publishDate = simpleFormat.format(date);  
     long from = simpleFormat.parse(currentDate).getTime();  
     long to = simpleFormat.parse(publishDate).getTime();  
     int hours = (int) ((from - to)/(1000 * 60 * 60));  
     return hours;
	}


	/**
	 *
	 * 时间戳转换成日期格式字符串
	 * @param seconds 精确到秒的字符串
	 * @param
	 * @return
	 */
     public static String timeStamp2Date(String seconds,String format) {
		   if(seconds == null || seconds.isEmpty() || seconds.equals("null")){
			            return "";
			 }
	       if(format == null || format.isEmpty()){
			           format = "yyyy-MM-dd HH:mm:ss";
		   }
		    SimpleDateFormat sdf = new SimpleDateFormat(format);
			Date dNow=new Date(Long.parseLong(seconds));
			System.out.println("日期类型："+sdf.format(dNow));
			return sdf.format(dNow);
		}


	/**
	 * 当前时间
 	 * @param date
	 * @return
	 */
	public static long time(Date date,int days){
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		ca.add(Calendar.DATE, days);// num为增加的天数
		return ca.getTime().getTime();
	}



	public static Long addSecond(Date currdate,int second)
	{

		Calendar ca = Calendar.getInstance();
		ca.setTime(currdate);
		// second为增加的秒数，可以改变的
		ca.add(Calendar.SECOND, second);
		currdate = ca.getTime();
		return currdate.getTime();
	}


	public static int getWeekDay(){
		Date date=new Date();
		String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		//获取当前日期 星期几（1,2,3,4,5,6,7）
		int currDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (currDay < 0){
			currDay = 0;
		}else if(currDay == 0){
			currDay = 7;
		}
		return currDay;
	}
}

