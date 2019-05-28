package com.cbt.controller;
import com.cbt.entity.DingBean.DingTalkMileStone;
import com.cbt.jdbc.AddMileStoneJdbc;
import com.cbt.quartz.DingTalkMileStoneEditTask;
import com.cbt.util.*;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.taobao.api.ApiException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONTokener;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.*;

import static com.cbt.dingding.Dingtalk.getAccessToken;
import static com.cbt.dingding.Dingtalk.getJsapiTicket;
import static com.cbt.dingding.Dingtalk.sign;

@Controller
@RequestMapping("/Ding-Talk")
public class DingTalkController {

    private static PropertiesUtils reader = new PropertiesUtils("dingtalk.properties");
    private static String CORP_ID = reader.getProperty("CorpId");
    private static String CORP_SECRET = reader.getProperty("CorpSecret");
    private static long AgentID = Integer.parseInt(reader.getProperty("AgentID"));
    private static String processCode = reader.getProperty("processCode");
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @ResponseBody
    @RequestMapping("/getConfig")
    public String getConfig(HttpServletRequest request) throws ApiException {

            //1.准备好参与签名的字段
            /*
             *以http://localhost/test.do?a=b&c=d为例
             *request.getRequestURL的结果是http://localhost/test.do
             *request.getQueryString的返回值是a=b&c=d
             */
            String urlString = request.getRequestURL().toString();
            String queryString = request.getQueryString();

            String queryStringEncode = null;
            String url;
            if (queryString != null) {
                queryStringEncode = URLDecoder.decode(queryString);
                url = urlString + "?" + queryStringEncode;
            } else {
                url = urlString;
            }


            String nonceStr= UUID.randomUUID().toString();      //随机数
            long timeStamp = System.currentTimeMillis() / 1000;     //时间戳参数

            String signedUrl = url;
            String accessToken = null;
            String ticket = null;
            String signature = null;   	//签名

            //2.进行签名，获取signature
            try {
                accessToken= getAccessToken(CORP_ID, CORP_SECRET);
                ticket=getJsapiTicket(accessToken);
                signature= sign(ticket,nonceStr,timeStamp,signedUrl);


            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            String configValue = "{jsticket:'" + ticket + "',signature:'" + signature + "',nonceStr:'" + nonceStr + "',timeStamp:'"
                    + timeStamp + "',corpId:'" + CORP_ID + "',agentId:'" + AgentID + "'}";
            System.out.println(configValue);

        sendNotice(accessToken,"05435661022279","测试项目提醒16000","SHS19202","dsfd","111","task");




        return configValue;
    }


    /**
     * 发送消息提醒
     * @param accessToken
     * @param dingTalkId
     * @param description
     * @param projectNo
     * @param sendUser
     */
    public void sendNotice(String accessToken,String dingTalkId,String description,String projectNo,String sendUser,String id,String type){
        //获取验证
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2");

        OapiMessageCorpconversationAsyncsendV2Request request = new OapiMessageCorpconversationAsyncsendV2Request();
        request.setUseridList(dingTalkId);
        request.setAgentId(AgentID);
        request.setToAllUser(false);

        OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
//        msg.setMsgtype("text");
//        msg.setText(new OapiMessageCorpconversationAsyncsendV2Request.Text());
//        msg.getText().setContent("你好，你有新消息");
//        request.setMsg(msg);

        msg.setMsgtype("link");
        msg.setLink(new OapiMessageCorpconversationAsyncsendV2Request.Link());
        if("task".equals(type)){
            msg.getLink().setTitle(projectNo+"有新的任务,任务发送人"+sendUser);
            msg.getLink().setMessageUrl("http://112.64.174.34:10010/task/taskDetails?id="+id+"&dingTalkId="+dingTalkId);
        }else if("notice".equals(type)){
            msg.getLink().setTitle(projectNo+"质检报告有新的留言消息");
            msg.getLink().setMessageUrl("https://www.kuaizhizao.cn/complaint/detail?id="+id+"&dingTalkId="+dingTalkId);
        }

        msg.getLink().setText(description);
        msg.getLink().setPicUrl("https://www.kuaizhizao.cn/images/detail2.png");
        request.setMsg(msg);


        try {
            OapiMessageCorpconversationAsyncsendV2Response response = client.execute(request,accessToken);
            if(response.getErrmsg() != null){
                logger.error("发送消息提醒错误日志："+ response.getTaskId() + response.getErrmsg());
            }
            logger.info("发送消息提醒:"+ response.getTaskId() + response.getErrmsg());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //获取钉钉消息发送进度
    @ResponseBody
    @RequestMapping("/getSendResult")
    public OapiMessageCorpconversationGetsendresultResponse.AsyncSendResult getSendResult(String taskId) throws ApiException {
        String accessToken = getAccessToken(CORP_ID, CORP_SECRET);
//        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/message/corpconversation/getsendprogress");
//        OapiMessageCorpconversationGetsendprogressRequest request  = new OapiMessageCorpconversationGetsendprogressRequest();
//        request.setAgentId(AgentID);
//        request.setTaskId(Long.parseLong("17155652637"));
//        OapiMessageCorpconversationGetsendprogressResponse response = client.execute(request, accessToken);
//        OapiMessageCorpconversationGetsendprogressResponse.AsyncSendProgress sendResult = response.getProgress();



        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/message/corpconversation/getsendresult");
        OapiMessageCorpconversationGetsendresultRequest request  = new OapiMessageCorpconversationGetsendresultRequest();
        request.setAgentId(AgentID);
        request.setTaskId(Long.parseLong(taskId));
        OapiMessageCorpconversationGetsendresultResponse response = client.execute(request, accessToken);
        OapiMessageCorpconversationGetsendresultResponse.AsyncSendResult sendResult = response.getSendResult();
        return sendResult;
    }


        /**
         * 发送钉钉消息（接口地址）
         * @param
         * @param
         * @return
         */
    @ResponseBody
    @RequestMapping("/sendTalkNotice")
    public String sendTalkNotice(@RequestParam Map<String,String> map,HttpServletRequest req) {

        logger.info("获取消息数据："+ map);
        //获取钉钉的userid
        String dingTalkId = map.get("dingTalkId");

        //获取任务内容
        String description = map.get("description");
        //获取项目号
        String projectNo = map.get("projectNo");
        //获取任务发出人
        String sendUser = map.get("sendUser");
        //获取任务id
        String id = map.get("id");
        //获取提醒类型
        String type = map.get("type");

        //获取accessToken
        String accessToken = getAccessToken(CORP_ID, CORP_SECRET);

        logger.info("获取到的AccessToken"+accessToken);
        sendNotice(accessToken,dingTalkId,description,projectNo,sendUser,id,type);
        return "yes";
    }



    @ResponseBody
    @RequestMapping("/getTalkNote")
    public String getDingTalkNote() {
        String talkNote = getTalkNote(0L);

        DingTalkMileStoneEditTask task = new DingTalkMileStoneEditTask();
        task.currentGetTalk();
/*        String	jsonStr = "{\"dingtalk_smartwork_bpms_processinstance_list_response\":{\"result\":{\"ding_open_errcode\":0,\"result\":{\"list\":{\"process_instance_top_vo\":[{\"business_id\":\"201903061457000372107\",\"cc_userid_list\":{\"string\":[\"0541283264532669437\",\"05413816132428110\",\"05413547031185225278\",\"12406613501197197725\"]},\"create_time\":\"2019-03-06 14:57:38\",\"finish_time\":\"2019-03-06 14:59:36\",\"form_component_values\":{\"form_component_value_vo\":[{\"name\":\"1.项目号SHS\",\"value\":\"20858\"},{\"name\":\"2.项目启动邮件\",\"value\":\"11\"},{\"name\":\"5.价格是否有问题？\",\"value\":\"无\"},{\"name\":\"6.初步工厂选择\",\"value\":\"安平哲瀚\"},{\"name\":\"7.预计的生产工艺\",\"value\":\"丝网 - 卷圆 - 焊接 - 清洗 - 酸洗钝化 - 包装\"},{\"name\":\"8.确定要视频的关键步骤\",\"value\":\"焊接 \"},{\"name\":\"9.强制要求\",\"value\":\"提供材质报告及丝网线径及宽度检验报告\"},{\"name\":\"10.里程碑a\",\"value\":\"丝网采购\"},{\"name\":\"里程碑a日期\",\"value\":\"2019-03-10\"},{\"name\":\"里程碑b\",\"value\":\"连接件加工\"},{\"name\":\"里程碑b日期\",\"value\":\"2019-03-12\"},{\"name\":\"里程碑c\",\"value\":\"丝网焊接\"},{\"name\":\"里程碑c日期\",\"value\":\"2019-03-15\"},{\"name\":\"里程碑d\",\"value\":\"两端焊接\"},{\"name\":\"里程碑d日期\",\"value\":\"2019-03-18\"},{\"name\":\"里程碑e\",\"value\":\"清洗包装\"},{\"name\":\"里程碑e日期\",\"value\":\"2019-03-20\"}]},\"originator_dept_id\":\"14019490\",\"originator_userid\":\"06076009391348503023\",\"process_instance_id\":\"8b275c1a-d652-46a2-995d-54838008f4f7\",\"process_instance_result\":\"agree\",\"status\":\"COMPLETED\",\"title\":\"徐平提交的采购.项目生产启动\"}]},\"next_cursor\":2},\"success\":true},\"request_id\":\"4wtsm4zskwb0\"}}";
        List<DingTalkMileStone> list = new ArrayList<DingTalkMileStone>();
        DingTalkMileStone menu = new DingTalkMileStone();
        if(StringUtils.isNotBlank(jsonStr)){
            JSONObject jsonObject = JSONObject.fromObject(jsonStr);
            List<DingTalkMileStone> analysisJson = analysisJson(jsonObject,menu,list);
            System.out.println(analysisJson);
        }*/

        return talkNote;
    }

    //定时任务执行方法
    public void currentGetTalk() {
        getTalkNote(0L);
    }


//    @RequestMapping("/getTalkNote")
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
     * 使用递归解决复杂json解析
     * @Title analysisJson
     * @Description
     * @param objJson
     * @param menu
     * @param list
     * @return
     * @return List<DingTalkMileStone>
     */
    public List<DingTalkMileStone> analysisJson(Object objJson, DingTalkMileStone menu, List<DingTalkMileStone> list) {

        try {
            logger.info("开始解析json数据:");
            //钉钉id
            String dingTalkId = null;
            //项目号
            String projectNo = null;
            //下次查询号码

            // 如果obj为json数组
            if (objJson instanceof JSONArray) {
                //将接收到的对象转换为JSONArray数组
                JSONArray objArray = (JSONArray) objJson;
                //对JSONArray数组进行循环遍历
                for (int i = 0; i < objArray.size(); i++) {
                    //新建menu对象
                    DingTalkMileStone mile = new DingTalkMileStone();
                    //设置menu属性
                    Object obj = objArray.get(i);
                    if(obj instanceof JSONObject){

                        //里程碑名
                        String mileStoneName = null;
                        //里程碑日期
                        Date mileStoneDate = null;

                        if(((JSONObject)objArray.get(i)).get("name")!= null && ((JSONObject)objArray.get(i)).get("name")!= "" && ((JSONObject)objArray.get(i)).get("name").toString().contains("里程碑")){
                            if(((JSONObject)objArray.get(i)).get("name").toString().contains("日期") && ((JSONObject)objArray.get(i)).get("value")!=null){
                                mileStoneDate = DateUtil.StrToDate(((JSONObject)objArray.get(i)).get("value").toString());
                            }else if(!((JSONObject)objArray.get(i)).get("name").toString().contains("日期") && ((JSONObject)objArray.get(i)).get("value")!=null){
                                mileStoneName = ((JSONObject)objArray.get(i)).get("value").toString();
                            }
                        }
                        if(((JSONObject)objArray.get(i)).get("originator_userid")!= null){
                            dingTalkId = ((JSONObject)objArray.get(i)).get("originator_userid").toString();
                            menu.setDingtalkid(dingTalkId);
                        }
                        if(((JSONObject)objArray.get(i)).get("name")!= null && ((JSONObject)objArray.get(i)).get("name")!= "" && ((JSONObject)objArray.get(i)).get("name").toString().contains("项目号")){
                            projectNo = "SHS"+((JSONObject)objArray.get(i)).get("value").toString();
                            menu.setProjectNo(projectNo);
                        }

                        //如果list最后一个对象里程碑名存在，里程碑日期不存在
                        //当前里程碑日期不为空，则保存在最后一个对象中
                        if(mileStoneDate!=null){
                            if(list.size()>0){
                                list.get(list.size()-1).setMilestoneDate(mileStoneDate);
                            }
                        }
                        //当里程碑名不为空时，执行添加
                        if(mileStoneName!=null){
                            mile.setDingtalkid(dingTalkId);
                            mile.setMilestoneDate(mileStoneDate);
                            mile.setMilestoneName(mileStoneName);
                            mile.setProjectNo(projectNo);
                            //将该级菜单对象存进list集合中
                            list.add(mile);
                        }
                    }

                    //调用回调方法
                    analysisJson(objArray.get(i),menu,list);
                }
                // 如果为json对象
            }else if (objJson instanceof JSONObject) {
                //将Object对象转换为JSONObject对象
                JSONObject jsonObject = (JSONObject) objJson;
                //迭代多有的Key值
                Iterator it = jsonObject.keys();
                //遍历每个Key值
                while (it.hasNext()) {
                    //将key值转换为字符串
                    String key = it.next().toString();
                    //根据key获取对象
                    Object object = new JSONTokener(jsonObject.get(key).toString()).nextValue();
                    if("next_cursor".equals(key.toString())){
                        menu.setNextCursor(Long.parseLong(jsonObject.get(key).toString()));
                    }

    //					System.out.println(object.getClass() +"："+ (object instanceof JSONArray));
                    // 如果得到的是数组
                    if (object instanceof JSONArray) {
                        //将Object对象转换为JSONObject对象
                        JSONArray objArray = (JSONArray) object;
                        //调用回调方法
                        analysisJson(objArray,menu,list);
                    }
                    // 如果key中是一个json对象
                    else if (object instanceof JSONObject) {
                        //调用回调方法
                        analysisJson((JSONObject) object,menu,list);
                    }
                }
            }
            for (DingTalkMileStone dingTalkMileStone : list) {
                dingTalkMileStone.setProjectNo(menu.getProjectNo());
                dingTalkMileStone.setDingtalkid(menu.getDingtalkid());
                dingTalkMileStone.setNextCursor(menu.getNextCursor());
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.info("获取钉钉数据解析错误："+  e);
        }
        logger.info("获取到的里程碑数据===："+  list);
        return list;
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
                //next_cursor
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
