package com.cbt.quartz;

import com.cbt.entity.DingBean.DingTalkMileStone;
import com.cbt.jdbc.AddMileStoneJdbc;
import com.cbt.util.DateFormat;
import com.cbt.util.DateUtil;
import com.cbt.util.JsonUtil;
import com.cbt.util.PropertiesUtils;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.request.OapiMessageCorpconversationGetsendresultRequest;
import com.dingtalk.api.request.SmartworkBpmsProcessinstanceListRequest;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.dingtalk.api.response.OapiMessageCorpconversationGetsendresultResponse;
import com.dingtalk.api.response.SmartworkBpmsProcessinstanceListResponse;
import com.taobao.api.ApiException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONTokener;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.*;

import static com.cbt.dingding.Dingtalk.*;


@Component
public class DingTalkTask {

    private static PropertiesUtils reader = new PropertiesUtils("dingtalk.properties");
    private static String CORP_ID = reader.getProperty("CorpId");
    private static String CORP_SECRET = reader.getProperty("CorpSecret");
    private static String processCode = reader.getProperty("processCode");
    private Logger logger = LoggerFactory.getLogger(this.getClass());







    //定时任务执行方法
    @Scheduled(cron = "0 0 10 ? * *")
    public void currentGetTalk() {
        getTalkNote(0L);
    }
    public String getTalkNote(Long nextCursor) {
        //获取accessToken
        try {
            String accessToken = getAccessToken(CORP_ID, CORP_SECRET);
            DingTalkClient client = new DefaultDingTalkClient("https://eco.taobao.com/router/rest");
            SmartworkBpmsProcessinstanceListRequest req = new SmartworkBpmsProcessinstanceListRequest();
            req.setProcessCode(processCode);
            req.setStartTime(DateFormat.time(new Date(),-1));
            req.setEndTime(new Date().getTime());
            req.setSize(1L);
            req.setCursor(nextCursor);
            req.setUseridList("");
            SmartworkBpmsProcessinstanceListResponse rsp = client.execute(req, accessToken);
            logger.info(rsp.getBody());
            //去除启动邮件内容
            String json = null;
            String str = rsp.getBody();
            String match = "\\{\"name\":\"2.项目启动邮件\",\"value\":\"";
            String[] split = str.split(match);
            if(split.length>1){
                String s = split[1];
                int i = s.indexOf("},");
                json = split[0]+s.substring(i+2,s.length());
            }

            //如果为空，则跳出
            if(StringUtils.isNotBlank(json)){
                saveMileStone(json);
            }
//          Client.sendPost("http://1j97509t42.51mypc.cn/port/getMileStoneJson",rsp.getBody());
            return JsonUtil.object2json(rsp.getBody());
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }



     //保存项目里程碑
     public void saveMileStone(String json){
         try {
             logger.info("===保存里程碑开始===");
//             DingTalkMileStone dingTalkMileStone = new DingTalkMileStone();
             List<DingTalkMileStone> list = new ArrayList<>();
    /*         JSONObject jsonObject = JSONObject.fromObject(json);
             List<DingTalkMileStone> milestones = analysisJson(jsonObject, dingTalkMileStone, list);*/
             List<DingTalkMileStone> milestones = analysisJsonToList(json,list);
             logger.info("获取到的里程碑milestones："+  milestones);
             if(milestones.size()>0){
                 logger.info("《《《《《《开始保存里程碑数据》》》》》");
                 AddMileStoneJdbc mileStoneJdbc = new AddMileStoneJdbc();
                 mileStoneJdbc.sendRequest(milestones);
             }
             if(json.contains("next_cursor") && milestones.size()> 0){
                 getTalkNote(milestones.get(0).getNextCursor());
             }
         } catch (Exception e) {
             e.printStackTrace();
             logger.error("获取里程碑数据失败：", e);
         }
     }



    /**
     * 根据字符串解析
     * @param objJson
     * @param list
     * @return
     */
    public List<DingTalkMileStone> analysisJsonToList(String objJson,List<DingTalkMileStone> list) {

        String projectNo = null;
        String dingTalkId = null;
        Long nextCursor = null;
        String processInstanceId = null;
        String[] split = objJson.split("里程碑");
        if(split.length>1){
            for (int j = 0; j < split.length; j++) {
                DingTalkMileStone mile = new DingTalkMileStone();
                String s = split[j];
                if(j>0){
                    if(s.contains("日期")){
                        String[] split2 = s.split("\"value\":");
                        if(split2.length > 1){
                            String string = split2[1];
                            int i = string.indexOf("},");
                            String json = string.substring(0,i);
                            Date mileStoneDate = DateUtil.StrToDate(json.replace("\"", ""));
                            //如果list最后一个对象里程碑名存在，里程碑日期不存在
                            //当前里程碑日期不为空，则保存在最后一个对象中
                            if(mile!=null){
                                if(list.size()>0){
                                    list.get(list.size()-1).setMilestoneDate(mileStoneDate);
                                }
                            }
                        }
                    }else{
                        String[] split2 = s.split("\"value\":");
                        if(split2.length > 1){
                            String string = split2[1];
                            int i = string.indexOf("},");
                            String json = string.substring(0,i);
                            //如果list最后一个对象里程碑名存在，里程碑日期不存在
                            //当前里程碑日期不为空，则保存在最后一个对象中
                            if(mile!=null){
                                mile.setMilestoneName(json.replace("\"", ""));
                                list.add(mile);
                            }
                        }
                    }
                }
                //获取项目号
                if(s.contains("项目号")){
                    String[] split2 = s.split("项目号");
                    if(split2.length>1){
                        String string = split2[1];
                        String[] split3 = string.split("\"value\":");
                        if(split3.length>1){
                            int i = split3[1].indexOf("},");
                            String json = split3[1].substring(0,i);
                            projectNo = json.replace("\"", "");
                        }
                    }
                }
                //dingTaklId
                if(s.contains("originator_userid")){
                    String[] split2 = s.split("originator_userid");
                    if(split2.length>1){
                        String string = split2[1];
                        String[] split3 = string.split(":");
                        if(split3.length>1){
                            int i = split3[1].indexOf(",");
                            String json = split3[1].substring(0,i);
                            dingTalkId = json.replace("\"", "");
                        }
                    }
                }
                //dingTaklId
                if(s.contains("next_cursor")){
                    String[] split2 = s.split("next_cursor");
                    if(split2.length>1){
                        String string = split2[1];
                        String[] split3 = string.split(":");
                        if(split3.length>1){
                            int i = split3[1].indexOf("}");
                            String json = split3[1].substring(0,i);
                            nextCursor = Long.parseLong(json.replace("\"", ""));
                        }
                    }
                }

                //process_instance_id
                if(s.contains("process_instance_id")){
                    String[] split2 = s.split("process_instance_id");
                    if(split2.length>1){
                        String string = split2[1];
                        String[] split3 = string.split(":");
                        if(split3.length>1){
                            int i = split3[1].indexOf(",");
                            String json = split3[1].substring(0,i);
                            processInstanceId = json.replace("\"", "");
                        }
                    }
                }
            }
        }

        for (DingTalkMileStone mileStone : list) {
            mileStone.setProjectNo("SHS"+projectNo);
            mileStone.setDingtalkid(dingTalkId);
            mileStone.setNextCursor(nextCursor);
            mileStone.setProcessInstanceId(processInstanceId);
        }
        return list;
    }
}
