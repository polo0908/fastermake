package com.cbt;

import com.cbt.baijia.push.PushBaijiaNews;
import com.cbt.dao.EquipmentMapper;
import com.cbt.dao.ProductLibraryMapper;
import com.cbt.dao.QuoteProductEnMapper;
import com.cbt.entity.Equipment;
import com.cbt.entity.FactoryInfo;
import com.cbt.entity.ProductLibrary;
import com.cbt.entity.QuoteProduct;
import com.cbt.service.FactoryInfoService;
import com.cbt.service.QuoteInfoService;
import com.cbt.service.QuoteProductService;
import com.cbt.translate.TranslateExecutor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
// 表示继承了SpringJUnit4ClassRunner类
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml","classpath:spring-web.xml" ,"classpath:spring-quartz.xml" })
public class InquiryControllerTest1 {
    @Resource
    private PushBaijiaNews pushBaijiaNews;
    @Resource
    private ProductLibraryMapper productLibraryMapper;
    @Resource
    private EquipmentMapper equipmentMapper;
    /*@Resource
    private QuoteProductEnMapper quoteProductEnMapper;*/



    @Test
    public void test() throws Exception {
        System.out.println("开始");
//        pushBaijiaNews.currentPushNews();
        List<ProductLibrary> productLibraries = productLibraryMapper.selectAll();
        productLibraries.forEach(p->{
            ProductLibrary productLibrary = TranslateExecutor.translateProductLibrary(p);
            productLibraryMapper.updateByPrimaryKeySelective(productLibrary);
        });
        System.out.println("结束");
    }



    @Test
    public void test1(){
        System.out.println("开始");
        List<Equipment> equipment = equipmentMapper.selectAll();
        equipment.forEach(e->{
            try {
                e.setEquipmentNameEn(TranslateExecutor.translateStr(e.getEquipmentName()));
                e.setEquipmentModelEn(TranslateExecutor.translateStr(e.getEquipmentModel()));
                e.setTypeEn(TranslateExecutor.translateStr(e.getType()));
                equipmentMapper.updateByPrimaryKeySelective(e);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
        System.out.println("结束");
    }
}
