package com.cbt.quartz;


import com.cbt.entity.FactoryInfo;
import com.cbt.entity.FactoryUser;
import com.cbt.entity.QuoteProduct;
import com.cbt.enums.OrderStatusEnum;
import com.cbt.service.*;
import com.cbt.util.DateFormat;
import com.cbt.util.GetServerPathUtil;
import com.cbt.util.SerializeUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 每周询盘邮件推送
 * @author polo
 * @date 2019.05.07
 */
@Component
public class SendMailTask {
    @Autowired
    private QuoteProductService quoteProductService;
    @Autowired
    private FactoryInfoService factoryInfoService;
    @Autowired
    private FactoryUserService factoryUserService;
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;
    @Autowired
    private MQProducerService mqProducerService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Scheduled(cron = "0 0 10 ? * MON")
    public void pushEmail(){

        try {
            List<FactoryInfo> factoryList = factoryInfoService.queryFactoryList();
            //邮件标题
            String title = URLEncoder.encode("本周最新询盘-快制造询盘订单报价网", "utf-8");
            for (FactoryInfo factoryInfo : factoryList) {
                String email = "";
                String mainProcess = "";

                //如果工厂是采购商，则不进行询盘推送
                //factory_type  2:采购商
                if(factoryInfo.getFactoryType() == 2){
                    continue;
                }
                //正常询盘状态
                Integer orderStatus = OrderStatusEnum.NORMAL_ORDER.getCode();
                //工厂主工艺
                mainProcess = factoryInfo.getMainProcess();

                String content =
                        "<!DOCTYPE html>"+
                                "<html>"+
                                "<head>"+
                                "<meta charset=\"utf-8\">"+
                                "<meta name=\"viewport\" content=\"width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no\"/>"+
                                "<title>ORDER CONFIRMATION</title>"+
                                "<style>"+
                                ".imgs {"+
                                "width: 260px;"+
                                "height: 200px;"+
                                "text-align: center;"+
                                "line-height: 200px;"+
                                "}"+
                                ".imgs img {"+
                                "max-width: 260px;"+
                                "max-height: 200px;"+
                                "width: auto;"+
                                "height: auto;"+
                                "}"+
                                "</style>"+
                                "<body>"+
                                "<div class=\"wrap-mind\">"+
                                "<div class=\"email-head\">"+
                                "<p class=\"email-p\">亲爱的会员，您好!</p>"+
                                "<p>以下是快制造kuaizhizao.cn根据您所设置的工艺专长推荐的最新询盘！</p>"+
                                "</div>"+
                                "<div class=\"email-wrap\">"+
                                "<p>"+(mainProcess == null ? "您还未设置擅长工艺" : "您擅长的工艺是"+mainProcess)+"&nbsp;&nbsp;<a href='"+ GetServerPathUtil.getServerPath()+"/zh/edit_enterprise_archives.html'>修改</a></p>";





                //获取工艺匹配询盘（去除已报价询盘）
                //如果未填写工艺，则取最近两个询盘（去除已报价询盘）
                if(StringUtils.isNotBlank(mainProcess)){
                    String[] split = mainProcess.split(",");
                    for (int i=0; i<split.length; i++) {
                        List<QuoteProduct> quoteProduct = quoteProductService.queryProductGroupByOrderId(0, 2, orderStatus, split[i], null, null, factoryInfo.getFactoryId(), null);
                        if(quoteProduct != null){
                            content += "<p>"+split[i]+"工艺本周最新询盘:</p>";
                            for (int j=0;j<quoteProduct.size();j++) {
                                content += "<p>"+(j+1)+"、询盘名称："+quoteProduct.get(j).getQuoteTitle()+"&nbsp;&nbsp;材料："+quoteProduct.get(j).getMaterials()+"&nbsp;&nbsp;工艺："+quoteProduct.get(j).getMainProcess()+"&nbsp;&nbsp;数量："+quoteProduct.get(j).getQuantityList()+"&nbsp;&nbsp;已有报价工厂数量："+quoteProduct.get(j).getCurrentNumber()+"/"+quoteProduct.get(j).getMaxNumber()+" &nbsp;&nbsp;<a href="+GetServerPathUtil.getServerPath()+"/rfq/"+quoteProduct.get(j).getOrderId()+">点击报价</a></p><div class='imgs'><img src='"+GetServerPathUtil.getServerPath()+quoteProduct.get(j).getDrawingPathCompress()+"'></div>";
                            }
                        }
                    }
                }else{
                    List<QuoteProduct> quoteProduct = quoteProductService.queryProductGroupByOrderId(0, 2, orderStatus, null, null, null, factoryInfo.getFactoryId(), null);
                    for (int i=0;i<quoteProduct.size();i++) {
                        content += "<p>"+(i+1)+"、询盘名称："+quoteProduct.get(i).getQuoteTitle()+"&nbsp;&nbsp;材料："+quoteProduct.get(i).getMaterials()+"&nbsp;&nbsp;工艺："+quoteProduct.get(i).getMainProcess()+"&nbsp;&nbsp;数量："+quoteProduct.get(i).getQuantityList()+"&nbsp;&nbsp;预计总值："+quoteProduct.get(i).getTotalAmount()+"元&nbsp;&nbsp;已有报价工厂数量："+quoteProduct.get(i).getCurrentNumber()+"/"+quoteProduct.get(i).getMaxNumber()+" &nbsp;&nbsp;<a href="+GetServerPathUtil.getServerPath()+"/rfq/"+quoteProduct.get(i).getOrderId()+">点击报价</a></p><div class='imgs'><img src='"+GetServerPathUtil.getServerPath()+quoteProduct.get(i).getDrawingPathCompress()+"'></div>";
                    }
                }
                content += "</div><p>上海快制造</p><p><a href='https://kuaizhizao.cn'>kuaizhizao.cn</a></p><p>"+ DateFormat.currentDate()+"</p><p>有问题请垂询15921081083/021-63757325-803或者Email:2037189289@qq.com</p></div>" +
                        "<div><p>请扫描关注快制造微信公众号并登陆，来获取最新询盘，更可在线报价及获取微信新消息通知：</p><img src='https://www.kuaizhizao.cn/images/qrcode.jpg'/></div>" +
                        "</body></html>";

                content = URLEncoder.encode(content,"utf-8");
                //获取工厂下所有用户，进行询盘推送
                List<FactoryUser> factoryUsers = factoryUserService.selectListByFactoryId(factoryInfo.getFactoryId());
                for (FactoryUser factoryUser : factoryUsers) {
                    email = factoryUser.getEmail();
                    sendMailTaskToMq(email,title,content);
                }
            }
        } catch (ParseException e) {
            logger.error("=========发送推送询盘邮件失败=========",e);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.error("=========发送推送询盘邮件失败=========",e);
        }
    }




    /**
     * 多线程发送推送邮件
     * @Title sendMail
     * @Description
     * @return void
     */
    public void sendMailTaskToMq(String email,String title,String content){
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String,Object> map = new HashMap<>();
                    map.put("email",email);
                    map.put("title",title);
                    map.put("content",content);
                    String json = SerializeUtil.ObjToJson(map);
                    mqProducerService.sendDataToQueue("mail",json);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
