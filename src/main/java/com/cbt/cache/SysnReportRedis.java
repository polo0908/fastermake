package com.cbt.cache;

import com.cbt.entity.QuoteWeeklyReport;
import com.cbt.service.QuoteWeeklyReportService;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;


public class SysnReportRedis {

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;
//    @Autowired
//    private RedisUtil redisUtil;
    @Autowired
    private QuoteWeeklyReportService quoteWeeklyReportService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    //存储报告
//    public void pushReports(SysnReportRedis obj){
//        List<QuoteWeeklyReport> reports = quoteWeeklyReportService.selectAll();
//        obj.sysnRedisTask("reports",reports);
//    }



    /**
     * 多线程存储redis
     * @Title sendMail
     * @Description
     * @return void
     */
//    public void sysnRedisTask(String key,List<QuoteWeeklyReport> list){
//        taskExecutor.execute(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    redisUtil.del(key);
//                    redisUtil.lSet(key,list);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    logger.error("sysnReportTask redis failed",e.getMessage());
//                }
//            }
//        });
//    }
}
