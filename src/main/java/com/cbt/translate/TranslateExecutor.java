package com.cbt.translate;

import com.cbt.entity.*;
import com.cbt.enums.ProcessZhAndEnEnum;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.concurrent.*;

public class TranslateExecutor {




    public static class MyTask implements Callable<String> {
        private String str;

        public MyTask(String str) {
            this.str = str;
        }

        @Override
        public String call(){
            System.out.println("正在执行task "+str);
            String transResult = null;
            try {
                transResult = TransApi.getTransResult(str, "zh", "en");
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("task "+transResult+"执行完毕");
            return transResult;
        }
    }






    /**
     * 询盘翻译方法
     * @param quote
     * @return
     * @throws Exception
     */
    public static QuoteInfo translateQuote(QuoteInfo quote) throws Exception {

        QuoteInfo quoteInfo = quote;
        //工艺
        if(StringUtils.isNotBlank(quoteInfo.getMainProcess())){
            String processEn = "";
            String[] split = quoteInfo.getMainProcess().split(",");
            for (int i = 0; i <split.length; i++) {
                if(ProcessZhAndEnEnum.getEnum(split[i])!=null){
                    processEn +=ProcessZhAndEnEnum.getEnum(split[i]).getValue()+",";
                }
            }
            if(StringUtils.isNotBlank(processEn)){
                processEn = processEn.substring(0,processEn.length()-1);
                quoteInfo.setMainProcess(processEn);
            }
        }
        //运输方式
        if(StringUtils.isNotBlank(quoteInfo.getQuoteFreightTerm())){
            MyTask myTask = new MyTask(quoteInfo.getQuoteFreightTerm());
            String result = myTask.call();
            if(StringUtils.isNotBlank(result)){
                quoteInfo.setQuoteFreightTerm(result);
            }
        }
        //报价备注
        if(StringUtils.isNotBlank(quoteInfo.getQuoteRemark())){
            MyTask myTask = new MyTask(quoteInfo.getQuoteRemark());
            String result = myTask.call();
            if(StringUtils.isNotBlank(result)){
                quoteInfo.setQuoteRemark(result);
            }
        }
        //工厂人数
        if(StringUtils.isNotBlank(quoteInfo.getStaffNumber())){
            MyTask myTask = new MyTask(quoteInfo.getStaffNumber());
            String result = myTask.call();
            if(StringUtils.isNotBlank(result)){
                quoteInfo.setStaffNumber(result);
            }
        }
        //运输城市
        if(StringUtils.isNotBlank(quoteInfo.getCity())){
            MyTask myTask = new MyTask(quoteInfo.getCity());
            String result = myTask.call();
            if(StringUtils.isNotBlank(result)){
                quoteInfo.setCity(result);
            }
        }
        //询盘标题
        if(StringUtils.isNotBlank(quoteInfo.getQuoteTitle())){
            MyTask myTask = new MyTask(quoteInfo.getQuoteTitle());
            String result = myTask.call();
            if(StringUtils.isNotBlank(result)){
                quoteInfo.setQuoteTitle(result);
            }
        }
        //设备关键词
        if(StringUtils.isNotBlank(quoteInfo.getEquipmentKeywords())){
            MyTask myTask = new MyTask(quoteInfo.getEquipmentKeywords());
            String result = myTask.call();
            if(StringUtils.isNotBlank(result)){
                quoteInfo.setEquipmentKeywords(result);
            }
        }
        //产品关键词
        if(StringUtils.isNotBlank(quoteInfo.getProductKeywords())){
            MyTask myTask = new MyTask(quoteInfo.getProductKeywords());
            String result = myTask.call();
            if(StringUtils.isNotBlank(result)){
                quoteInfo.setProductKeywords(result);
            }
        }
        //下单工厂名
        if(StringUtils.isNotBlank(quoteInfo.getOrderFactoryName())){
            MyTask myTask = new MyTask(quoteInfo.getOrderFactoryName());
            String result = myTask.call();
            if(StringUtils.isNotBlank(result)){
                quoteInfo.setOrderFactoryName(result);
            }
        }
        //询盘详情
        if(StringUtils.isNotBlank(quoteInfo.getQuoteDetail())){
            MyTask myTask = new MyTask(quoteInfo.getQuoteDetail());
            String result = myTask.call();
            if(StringUtils.isNotBlank(result)){
                quoteInfo.setQuoteDetail(result);
            }
        }
        //跟进详情
        if(StringUtils.isNotBlank(quoteInfo.getFollowDetail())){
            MyTask myTask = new MyTask(quoteInfo.getFollowDetail());
            String result = myTask.call();
            if(StringUtils.isNotBlank(result)){
                quoteInfo.setFollowDetail(result);
            }
        }
        return quoteInfo;
    }


    /**
     * 产品翻译方法
     * @param quoteProduct
     * @return
     * @throws Exception
     */
    public static QuoteProduct translateQuoteProduct(QuoteProduct quoteProduct) throws Exception {
        QuoteProduct product = (QuoteProduct)quoteProduct.clone();
        //产品名称
        if(StringUtils.isNotBlank(product.getProductName())){
            MyTask myTask = new MyTask(product.getProductName());
            String result = myTask.call();
            if(StringUtils.isNotBlank(result)){
                product.setProductName(result);
            }
        }
        //产品材质
        if(StringUtils.isNotBlank(product.getMaterials())){
            MyTask myTask = new MyTask(product.getMaterials());
            String result = myTask.call();
            if(StringUtils.isNotBlank(result)){
                product.setMaterials(result);
            }
        }
        //产品工艺
        if(StringUtils.isNotBlank(product.getProcess())){
            MyTask myTask = new MyTask(product.getProcess());
            String result = myTask.call();
            if(StringUtils.isNotBlank(result)){
                product.setProcess(result);
            }
        }
        //数量单位
        if(StringUtils.isNotBlank(product.getQuantityUnit())){
            MyTask myTask = new MyTask(product.getQuantityUnit());
            String result = myTask.call();
            if(StringUtils.isNotBlank(result)){
                product.setQuantityUnit(result);
            }
        }
        //询盘标题
        if(StringUtils.isNotBlank(product.getProductRemark())){
            MyTask myTask = new MyTask(product.getProductRemark());
            String result = myTask.call();
            if(StringUtils.isNotBlank(result)){
                product.setProductRemark(result);
            }
        }

        return product;
    }




    /**
     * 提醒消息翻译方法
     * @param message
     * @return
     * @throws Exception
     */
    public static NoteMessage translateMessage(NoteMessage message) throws Exception {

        NoteMessage noteMessage = message;
        //消息标题
        if(StringUtils.isNotBlank(noteMessage.getMessageTitle())){
            MyTask myTask = new MyTask(noteMessage.getMessageTitle());
            String result = myTask.call();
            if(StringUtils.isNotBlank(result)){
                noteMessage.setMessageTitle(result);
            }
        }
        //消息内容
        if(StringUtils.isNotBlank(noteMessage.getMessageDetails())){
            MyTask myTask = new MyTask(noteMessage.getMessageDetails());
            String result = myTask.call();
            if(StringUtils.isNotBlank(result)){
                noteMessage.setMessageDetails(result);
            }
        }

        return noteMessage;
    }



    /**
     * 消息翻译方法
     * @param message
     * @return
     * @throws Exception
     */
    public static QuoteMessage translateQuoteMessage(QuoteMessage message) throws Exception {
        QuoteMessage quoteMessage = message;
        //消息内容
        if(StringUtils.isNotBlank(quoteMessage.getMessageDetails())){
            MyTask myTask = new MyTask(quoteMessage.getMessageDetails());
            String result = myTask.call();
            if(StringUtils.isNotBlank(result)){
                quoteMessage.setMessageDetails(result);
            }
        }
        return quoteMessage;
    }



    /**
     * 消息翻译方法
     * @param factory
     * @return
     * @throws Exception
     */
    public static FactoryInfo translateFactoryInfo(FactoryInfo factory) throws Exception {
        FactoryInfo factoryInfo = (FactoryInfo)factory.clone();
        //联系人
        if(StringUtils.isNotBlank(factoryInfo.getUsername()) && !StringUtils.isNumeric(factoryInfo.getUsername())){
            MyTask myTask = new MyTask(factoryInfo.getUsername());
            String result = myTask.call();
            if(StringUtils.isNotBlank(result)){
                factoryInfo.setUsername(result);
            }
        }

        //工厂名
        if(StringUtils.isNotBlank(factoryInfo.getFactoryName())){
            MyTask myTask = new MyTask(factoryInfo.getFactoryName());
            String result = myTask.call();
            if(StringUtils.isNotBlank(result)){
                factoryInfo.setFactoryName(result);
            }
        }

        //国家
        if(StringUtils.isNotBlank(factoryInfo.getCountry())){
            MyTask myTask = new MyTask(factoryInfo.getCountry());
            String result = myTask.call();
            if(StringUtils.isNotBlank(result)){
                factoryInfo.setCountry(result);
            }
        }
        //城市
        if(StringUtils.isNotBlank(factoryInfo.getCity())){
            MyTask myTask = new MyTask(factoryInfo.getCity());
            String result = myTask.call();
            if(StringUtils.isNotBlank(result)){
                factoryInfo.setCity(result);
            }
        }
        //区、省
        if(StringUtils.isNotBlank(factoryInfo.getState())){
            MyTask myTask = new MyTask(factoryInfo.getState());
            String result = myTask.call();
            if(StringUtils.isNotBlank(result)){
                factoryInfo.setState(result);
            }
        }
        //详细地址
        if(StringUtils.isNotBlank(factoryInfo.getDetailsAddress())){
            MyTask myTask = new MyTask(factoryInfo.getDetailsAddress());
            String result = myTask.call();
            if(StringUtils.isNotBlank(result)){
                factoryInfo.setDetailsAddress(result);
            }
        }
        //主工艺
        if(StringUtils.isNotBlank(factoryInfo.getMainProcess())){

            StringBuffer strBuffer = new StringBuffer();
            String[] split = factoryInfo.getMainProcess().split(",");
            for(int i=0;i<split.length;i++){
                //获取工艺英文名称
                if(ProcessZhAndEnEnum.getEnum(split[i]) != null){
                    strBuffer.append(ProcessZhAndEnEnum.getEnum(split[i]).getValue());
                    strBuffer.append(",");
                }
            }
            if(strBuffer.length()>0){
                strBuffer.deleteCharAt(strBuffer.length()-1);
                factoryInfo.setMainProcess(strBuffer.toString());
            }

        }

        //公司备注
        if(StringUtils.isNotBlank(factoryInfo.getFactoryRemark())){
            MyTask myTask = new MyTask(factoryInfo.getFactoryRemark());
            String result = myTask.call();
            if(StringUtils.isNotBlank(result)){
                factoryInfo.setFactoryRemark(result);
            }
        }

        //职位
        if(StringUtils.isNotBlank(factoryInfo.getPosition())){
            MyTask myTask = new MyTask(factoryInfo.getPosition());
            String result = myTask.call();
            if(StringUtils.isNotBlank(result)){
                factoryInfo.setPosition(result);
            }
        }

        //合作企业
        if(StringUtils.isNotBlank(factoryInfo.getCooperativeEnterprise())){
            MyTask myTask = new MyTask(factoryInfo.getCooperativeEnterprise());
            String result = myTask.call();
            if(StringUtils.isNotBlank(result)){
                factoryInfo.setCooperativeEnterprise(result);
            }
        }

        //技术
        if(StringUtils.isNotBlank(factoryInfo.getTechnologicalAdvantage())){
            MyTask myTask = new MyTask(factoryInfo.getTechnologicalAdvantage());
            String result = myTask.call();
            if(StringUtils.isNotBlank(result)){
                factoryInfo.setTechnologicalAdvantage(result);
            }
        }
        //材料
        if(StringUtils.isNotBlank(factoryInfo.getDominantMaterialModel())){
            MyTask myTask = new MyTask(factoryInfo.getDominantMaterialModel());
            String result = myTask.call();
            if(StringUtils.isNotBlank(result)){
                factoryInfo.setDominantMaterialModel(result);
            }
        }
        //可接受金额
        if(StringUtils.isNotBlank(factoryInfo.getAcceptMoney())){
            MyTask myTask = new MyTask(factoryInfo.getAcceptMoney());
            String result = myTask.call();
            if(StringUtils.isNotBlank(result)){
                factoryInfo.setAcceptMoney(result);
            }
        }
        //区域
        if(StringUtils.isNotBlank(factoryInfo.getDistrict())){
            MyTask myTask = new MyTask(factoryInfo.getDistrict());
            String result = myTask.call();
            if(StringUtils.isNotBlank(result)){
                factoryInfo.setDistrict(result);
            }
        }
        //公司详情
        if(StringUtils.isNotBlank(factoryInfo.getFactoryInfo())){
            MyTask myTask = new MyTask(factoryInfo.getFactoryInfo());
            String result = myTask.call();
            System.out.println(result);
            if(StringUtils.isNotBlank(result)){
                factoryInfo.setFactoryInfo(result);
            }
        }


        return factoryInfo;
    }


    /**
     * 产品库翻译方法
     * @param message
     * @return
     * @throws Exception
     */
    public static ProductLibrary translateProductLibrary(ProductLibrary product){

        //零件详情
        if(StringUtils.isNotBlank(product.getProductInfo())){
            MyTask myTask = new MyTask(product.getProductInfo());
            String result = myTask.call();
            if(StringUtils.isNotBlank(result)){
                product.setProductInfoEn(result);
            }
        }
        //材质详情
        if(StringUtils.isNotBlank(product.getMaterials())){
            MyTask myTask = new MyTask(product.getMaterials());
            String result = myTask.call();
            if(StringUtils.isNotBlank(result)){
                product.setMaterialsEn(result);
            }
        }
        //行业详情
        if(StringUtils.isNotBlank(product.getInductry())){
            MyTask myTask = new MyTask(product.getInductry());
            String result = myTask.call();
            if(StringUtils.isNotBlank(result)){
                product.setInductryEn(result);
            }
        }
        //主工艺
        if(StringUtils.isNotBlank(product.getMainProcess())){
            if(ProcessZhAndEnEnum.getEnum(product.getMainProcess())!=null){
                String processEn = ProcessZhAndEnEnum.getEnum(product.getMainProcess()).getValue();
                product.setMainProcessEn(processEn);
            }
        }
        return product;
    }



    /**
     * 消息翻译方法
     * @param str
     * @return
     * @throws Exception
     */
    public static String translateStr(String str) throws Exception {
        String translateStr = "";
        //消息内容
        if(StringUtils.isNotBlank(str)){
            MyTask myTask = new MyTask(str);
            String result = myTask.call();
            if(StringUtils.isNotBlank(result)){
                translateStr = result;
            }
        }
        return translateStr;
    }

}





