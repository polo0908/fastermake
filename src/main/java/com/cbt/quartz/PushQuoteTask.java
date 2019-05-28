package com.cbt.quartz;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


//已改为quartz.xml形式定时任务
//@Component("pushQuoteTask")
//public class PushQuoteTask {

//	    @Scheduled(cron = "0 0 12 * * ?")  
	
//		@Scheduled(cron = "0 10 10 * * MON")
//	    @Scheduled(cron = "0 49 16 * * ?")
//	    @Scheduled(cron="0/60 * *  * * ? ")
//	    public void job1() {
//	    	MyTimerTask task = new MyTimerTask();
//	    	task.run();
//	    }
//}