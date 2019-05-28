package com.cbt.util;

import com.cbt.entity.*;
import com.cbt.enums.FactoryScoreEnum;
import org.apache.commons.lang.StringUtils;

import java.util.concurrent.*;

public class CalculateFactoryScore {


    public static class MyTask implements Callable<Integer> {
        private FactoryInfo factoryInfo;

        public MyTask(FactoryInfo factoryInfo) {
            this.factoryInfo = factoryInfo;
        }

        @Override
        public Integer call(){
            Integer score = getScore(factoryInfo);
            return score;
        }
    }


    /**
     * 获取工厂总评分
     * @param factoryInfo
     * @return
     */
    public static Integer getScore(FactoryInfo factoryInfo) {
        int score = 0;
        //工厂名
        if(StringUtils.isNotBlank(factoryInfo.getFactoryName())){
            score += FactoryScoreEnum.FACTORY_NAME.getScore();
        }
        //工厂英文名
        if(StringUtils.isNotBlank(factoryInfo.getFactoryNameEn())){
            score += FactoryScoreEnum.FACTORY_NAME_EN.getScore();
        }
        //工厂logo
        if(StringUtils.isNotBlank(factoryInfo.getFactoryLogo())){
            score += FactoryScoreEnum.FACTORY_lOGO.getScore();
        }
        //1688网址
        if(StringUtils.isNotBlank(factoryInfo.getAliWebsite())){
            score += FactoryScoreEnum.WEBSITES_1688.getScore();
        }
        //电话
        if(StringUtils.isNotBlank(factoryInfo.getTel())){
            score += FactoryScoreEnum.TEL.getScore();
        }
        //手机
        if(StringUtils.isNotBlank(factoryInfo.getMobile())){
            score += FactoryScoreEnum.PHONE.getScore();
        }
        //地址
        if(StringUtils.isNotBlank(factoryInfo.getDetailsAddress())){
            score += FactoryScoreEnum.LOCATION.getScore();
        }
        //工厂照片
        if(StringUtils.isNotBlank(factoryInfo.getFactoryGate())){
            score += FactoryScoreEnum.FACTORY_PIC.getScore();
        }
        //工厂成立时间
        if(StringUtils.isNotBlank(factoryInfo.getEstablishmentYear())){
            score += FactoryScoreEnum.ESTABLISHMENT_YEAR.getScore();
        }
        //工厂人数
        if(StringUtils.isNotBlank(factoryInfo.getStaffNumber())){
            score += FactoryScoreEnum.STAFF_NUMBER.getScore();
        }
        //工厂面积
        if(StringUtils.isNotBlank(factoryInfo.getFactoryAcreage())){
            score += FactoryScoreEnum.FACTORY_ACREAGE.getScore();
        }
        //工厂产值
        if(StringUtils.isNotBlank(factoryInfo.getFactoryValue())){
            score += FactoryScoreEnum.FACTORY_VALUE.getScore();
        }
        //进出口权
        if("yes".equals(factoryInfo.getType())){
            score += FactoryScoreEnum.EXPORT_TYPE.getScore();
        }
        //公司网址
        if(StringUtils.isNotBlank(factoryInfo.getWebsite())){
            score += FactoryScoreEnum.WEBSITES.getScore();
        }
        //公司简介
        if(StringUtils.isNotBlank(factoryInfo.getFactoryInfo())){
            score += FactoryScoreEnum.FACTORY_INFO.getScore();
        }
        //公司技术优势
        if(StringUtils.isNotBlank(factoryInfo.getTechnologicalAdvantage())){
            score += FactoryScoreEnum.TECHNOLOGICAL_ADVANTAGE.getScore();
        }
        //公司技术材料
        if(StringUtils.isNotBlank(factoryInfo.getTechnologicalAdvantage())){
            score += FactoryScoreEnum.DOMINANT_MATERIAL_MODEL.getScore();
        }
        //公司主工艺
        if(StringUtils.isNotBlank(factoryInfo.getMainProcess())){
            score += FactoryScoreEnum.MAIN_PROCESS.getScore();
        }
        //工件大小
        if(StringUtils.isNotBlank(factoryInfo.getMainProcess())){
            score += FactoryScoreEnum.DOMINANT_MATERIAL_SIZE.getScore();
        }
        //场地大小
        if(StringUtils.isNotBlank(factoryInfo.getSiteSize())){
            if("0".equals(factoryInfo.getSiteSize())){
                score += FactoryScoreEnum.SITE_SIZE_3.getScore();
            }else if("1".equals(factoryInfo.getAcceptCondition())){
                score += FactoryScoreEnum.SITE_SIZE_2.getScore();
            }else if("2".equals(factoryInfo.getAcceptCondition())){
                score += FactoryScoreEnum.SITE_SIZE_1.getScore();
            }
        }

        //合作企业
        if(StringUtils.isNotBlank(factoryInfo.getCooperativeEnterprise())){
            int tl = factoryInfo.getCooperativeEnterprise().split(",").length;
            score +=  FactoryScoreEnum.COOPERATIVE_ENTERPRISE.getScore() * tl;
        }
        //可接受订单
        if(StringUtils.isNotBlank(factoryInfo.getAcceptCondition())){
            if("1".equals(factoryInfo.getAcceptCondition())){
                score += FactoryScoreEnum.ACCEPT_CONDITION_1.getScore();
            }else if("2".equals(factoryInfo.getAcceptCondition())){
                score += FactoryScoreEnum.ACCEPT_CONDITION_2.getScore();
            }else if("3".equals(factoryInfo.getAcceptCondition())){
                score += FactoryScoreEnum.ACCEPT_CONDITION_3.getScore();
            }
        }

        //设备运行视频
        if(StringUtils.isNotBlank(factoryInfo.getProductionVideo())){
            score +=  FactoryScoreEnum.EQUIPMENT_VIDEO.getScore();
        }
        //环境照片
        if(StringUtils.isNotBlank(factoryInfo.getProductionEnvironment())){
            int tl = factoryInfo.getProductionEnvironment().split(",").length;
            score +=  FactoryScoreEnum.PRODUCTION_ENVIRONMENT.getScore() * tl;
        }
        //资格认证
        if(factoryInfo.getQualificationList() != null && factoryInfo.getQualificationList().size() > 0){
            int tl = factoryInfo.getQualificationList().size();
            score +=  FactoryScoreEnum.CERTIFICATION.getScore() * tl;
        }

        //设备
        if(factoryInfo.getEquipmentList() != null && factoryInfo.getEquipmentList().size() > 0){
            int tl = factoryInfo.getEquipmentList().size();
            score +=  FactoryScoreEnum.EQUIPMENT.getScore() * tl;
        }

        return score;
    }



}





