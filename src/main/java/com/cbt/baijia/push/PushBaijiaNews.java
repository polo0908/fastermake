package com.cbt.baijia.push;


import com.alibaba.fastjson.JSON;
import com.cbt.baijia.bean.Image;
import com.cbt.baijia.bean.Message;
import com.cbt.entity.QuoteProduct;
import com.cbt.enums.CountryEnum;
import com.cbt.service.QuoteProductService;
import com.cbt.util.*;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author polo
 */
@Component
public class PushBaijiaNews {
    @Autowired
    private QuoteProductService quoteProductService;

    private static PropertiesUtils reader = new PropertiesUtils("baijiahao.properties");
    private static String APP_ID = reader.getProperty("app_id");
    private static String APP_TOKEN = reader.getProperty("app_token");
    private static String PUSH_URL = reader.getProperty("push_url");
    /**
     * 一周数
     */
    private static int WEEK_DAYS=7;
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    /**
     * 定时任务执行方法
     * 推送的个数根据百家号级别来定
     * 定时任务执行方法
     *
     */
    @Scheduled(cron = "0 0 9 * * ?")
    public void currentPushNews() {
        pushNews(1);
    }
    public void pushNews(int count) {
        try {
        //查询上周总询盘
        int totalCount = 0;
        List<QuoteProduct> quoteList = quoteProductService.selectWeekMaxAmonut(null,null);
        if(quoteList!=null){
            totalCount = quoteList.size();
        }
        int page = 0;
        int pageSize = 0;
        int currDay = DateFormat.getWeekDay();
        logger.info("当前日期"+ currDay);
        //每天推荐个数
        if(totalCount<WEEK_DAYS){
            pageSize = 1;
        }else{
            pageSize = totalCount/WEEK_DAYS;
        }
        //最后剩余询盘
        int rem = totalCount%WEEK_DAYS;
        //第一天查询从0开始
        if(currDay>1){
            page = (currDay-1)*pageSize;
        }
        //最后一天推荐剩余的询盘
        if(currDay==WEEK_DAYS){
            pageSize = pageSize+rem;
        }
        //当询盘不足一周发送时，直接return
        if(currDay>totalCount){
            return;
        }
        logger.info("当前页数"+ page+" 每页数量"+ pageSize);
        List<QuoteProduct> quotes = quoteProductService.selectWeekMaxAmonut(page,pageSize);
        logger.info("发送百家号询盘"+ quotes);
        Message message = new Message();
        message.setApp_id(APP_ID);
        message.setApp_token(APP_TOKEN);
        message.setTitle("快制造"+DateFormat.formatZh(new Date())+"询盘订单快讯");
        message.setIs_original(1);
        String firstImage = null;
        StringBuffer str = new StringBuffer();
        //标题显示图片
        List<Image> list = new ArrayList<>();
        //当前序号
        int index = 1;
        for(QuoteProduct quoteProduct:quotes){

                //产品的图片
                String drawingPathCompress = quoteProduct.getDrawingPathCompress();
                String[] split = null;
                String images = "";
                //获取产品的所有图片
                //取第一张放到标题内
                //获取图片集
                if(StringUtils.isNotBlank(drawingPathCompress)){
                        split = drawingPathCompress.split(",");
                        if(StringUtils.isBlank(firstImage)){
                            message.setOrigin_url(GetServerPathUtil.getServerPath()+"/rfq/"+quoteProduct.getOrderId()+"?r="+Math.random());
                        }
                        firstImage = split[0];
                        images +="<p><img src=\""+GetServerPathUtil.getServerPath()+split[0]+"\"/></p>";
                        //标题显示图片
                        if(index<4){
                            Image image = new Image();
                            image.setSrc(GetServerPathUtil.getServerPath()+firstImage);
                            list.add(image);
                        }
                }
                //询盘客户国家
                String country = quoteProduct.getCountry();
                if(CountryEnum.getEnum(country)!=null){
                    country = CountryEnum.getEnum(country).getZhName();
                }else{
                    country = "其他";
                }
                str.append("<p>");
                str.append(index+"、["+country+"]");
                str.append(quoteProduct.getQuoteTitle());
                str.append("  材料:"+quoteProduct.getMaterials());
                str.append("  工艺:"+quoteProduct.getMainProcess());
                str.append(DateFormat.formatDate1(" "+quoteProduct.getPublishDate()));
                str.append("</p>");
                str.append("<h3>");
                str.append("客户采购代表谈该项目:");
                str.append("</h3>");
                str.append("<p>");
                str.append(quoteProduct.getQuoteDetail());
                str.append("</p>");
                str.append("<h3>");
                str.append("报价管理工程师谈该项目:");
                str.append("</h3>");
                str.append("<p>");
                str.append(quoteProduct.getQuoteRemark());
                str.append("</p>");
                str.append(images);
                index++;
         }

            message.setCover_images(JSON.toJSONString(list));
            message.setContent(str.toString());
            String json = JSON.toJSON(message).toString();
            JSONObject jsonObject = SendHttpsRequestUtil.sendPostRequest(PUSH_URL, json);
            logger.info("发送百家号询盘回调值"+jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
