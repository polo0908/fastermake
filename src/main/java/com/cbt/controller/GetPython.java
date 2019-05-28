package com.cbt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @ClassName GetPython
 * @Description 触发python脚本数据抓取
 * @Author zj
 * @Date 2019/5/15 17:54
 * @Cersion 1.0
 */
@RequestMapping("python")
@Controller
public class GetPython {


    /**
     * 根据id查询产品
     * @param factoryId  工厂id
     * @param type       类型
     * @param productNameEn   产品英文名（空格使用-分割）
     * @param pid       产品pid
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping("getProduct")
    public String  getProduct(String factoryId,String url) {
        try {
            String[] args1 = new String[] { "python", "E:\\python\\1688productDetail\\shop_all_spider.py", url ,factoryId};
            Process pr=Runtime.getRuntime().exec(args1);
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    pr.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            pr.waitFor();
            System.out.println("end");
        }
        catch (Exception e) {
            e.printStackTrace();
            return "false";
        }
        return "success";
    }
}
