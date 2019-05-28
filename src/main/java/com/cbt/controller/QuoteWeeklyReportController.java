package com.cbt.controller;


import com.alibaba.fastjson.JSON;
import com.cbt.cache.RedisUtil;
import com.cbt.entity.*;
import com.cbt.enums.FileTypeEnum;
import com.cbt.enums.ProjectStageEnum;
import com.cbt.enums.StateEnum;
import com.cbt.rpc.GetTaskProject;
import com.cbt.service.*;
import com.cbt.util.*;
import com.cbt.util.Base64;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.misc.BASE64Decoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

@Controller
@RequestMapping("/report")
public class QuoteWeeklyReportController {

    @Autowired
    private QuoteWeeklyReportService quoteWeeklyReportService;
    @Autowired
    private QuoteReportTypeService quoteReportTypeService;
    @Autowired
    private QuoteMessageService quoteMessageService;
    @Autowired
    private FactoryInfoService factoryInfoService;
    @Autowired
    private FactoryUserService factoryUserService;
    @Autowired
    private QuoteInfoService quoteInfoService;
    @Autowired
    private QualificationService qualificationService;


    private Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 保存进度报告、材质证明、检测报告
     * @Title insertReportPhoto
     * @Description
     * @param request
     * @param response
     * @return
     * @return JsonResult<String>
     */
    @ResponseBody
    @RequestMapping("/insertReport")
    public JsonResult<Map<String,Object>> insertReport(HttpServletRequest request, HttpServletResponse response){
        Map<String, String> fileNameMap = null;
        try {

            String factoryId = WebCookie.getFactoryId(request);
            if(StringUtils.isBlank(factoryId)){
                return new JsonResult<Map<String,Object>>(2,"未获取到登录信息");
            }else{
                if(StringUtils.isBlank(request.getParameter("orderId")) && StringUtils.isBlank(request.getParameter("csgOrderId"))){
                    return new JsonResult<Map<String,Object>>(1,"未获取到询盘信息");
                }

                //获取询盘号或者项目号
                //当存在自营项目号的时候，以项目号为文件夹名保存
                Integer orderId = null;
                String csgOrderId = null;
                if(StringUtils.isBlank(request.getParameter("fileType"))){
                    return new JsonResult<Map<String,Object>>(1,"未获取到文件类型");
                }
                Integer fileType = Integer.parseInt(request.getParameter("fileType"));          //文件类型
                String quoteName = "";
                if(StringUtils.isNotBlank(request.getParameter("orderId"))){
                    quoteName = request.getParameter("orderId");
                    orderId = Integer.parseInt(request.getParameter("orderId"));
                }
                if(StringUtils.isNotBlank(request.getParameter("csgOrderId"))){
                    quoteName = request.getParameter("csgOrderId");
                    csgOrderId = request.getParameter("csgOrderId");
                }
				    /*
				     * 进度报告 /100012/progress/
				     */
                String path1 = UploadAndDownloadPathUtil.getQuoteFile() +  quoteName + File.separator;
                File file1 = new File(path1);
                if  (!file1.exists()  && !file1 .isDirectory())
                {
                    file1 .mkdir();
                }

                //根据文件类型确定上传文件路径
                String path2 = "";
                //转码视频保存位置
                String path3 = "";
                String pathName = "";
                if(fileType == FileTypeEnum.IMG.getCode()){
                    path2 = path1 + "IMG" + File.separator;
                    path3 = path1 + "IMG" + File.separator + "convert" + File.separator;
                    pathName = "/IMG/";
                }else if(fileType == FileTypeEnum.MATERIAL_FILE.getCode()){
                    path2 =  path1 + "material" + File.separator;
                    pathName = "/material/";
                }else if(fileType == FileTypeEnum.QUANLITY_FILE.getCode()){
                    path2 = path1 + "quality" + File.separator;
                    pathName = "/quality/";
                }else if(fileType == FileTypeEnum.VIDEO.getCode()){
                    path2 = path1 + "VIDEO" + File.separator;
                    path3 = path1 + "VIDEO" + File.separator + "convert" + File.separator;
                    pathName = "/VIDEO/";
                }
                File file2 = new File(path2);
                if  (!file2.exists()  && !file2 .isDirectory())
                {
                    file2 .mkdir();
                }
                File file3 = new File(path3);
                if  (!file3.exists()  && !file3 .isDirectory())
                {
                    file3 .mkdir();
                }



                //报告集合
                List<QuoteWeeklyReport> reports = new ArrayList<QuoteWeeklyReport>();
                //根据文件名获取上传文件的位置  数据库保存原始文件名称
                //如果是图片类型，则调用多文件上传处理
                //如果是视频和图片则单独处理
                if(fileType == FileTypeEnum.IMG.getCode()) {
                    Map<String, Map<String, String>> multiFileUpload = OperationFileUtil.multiFileUpload2_changename(request, path2, path2, null);
                    if (!(multiFileUpload == null || multiFileUpload.size() == 0)) {
                        fileNameMap = multiFileUpload.get("filePaths");
                    }
                    if (fileNameMap != null && fileNameMap.size() != 0) {
                        Set<String> keySet = fileNameMap.keySet();
                        for (String key : keySet) {
                            String newName = fileNameMap.get(key);
                            if (fileType == FileTypeEnum.IMG.getCode() && StringUtils.isNotBlank(newName)) {
                                QuoteWeeklyReport report = new QuoteWeeklyReport();
                                report.setOrderId(orderId);
                                report.setFileName(key);
                                report.setCsgOrderId(csgOrderId);
                                report.setPhotoPath(UploadAndDownloadPathUtil.getQuoteFileStatic() + quoteName + pathName + newName);
                                report.setPhotoPathCompress(UploadAndDownloadPathUtil.getQuoteFileStatic() + quoteName + pathName + newName);
                                report.setFileType(fileType);
                                report.setUploadDate(DateFormat.format());
                                report.setFactoryId(factoryId);
                                reports.add(report);
                            }
                        }
                    }
                }else{
                    //获取上传文件名
                    String drawingName = request.getParameter("fileName");
                    //根据文件名获取上传文件的位置  数据库保存原始文件名称
                    Map<String, String> multiFileUpload = OperationFileUtil.multiFileUpload_changename(request, path2);
                    String fileName = "";
                    if(!(multiFileUpload == null || multiFileUpload.size() == 0)){
                        fileName = multiFileUpload.get(drawingName);
                    }
                    if(StringUtils.isNotBlank(fileName)){
                        QuoteWeeklyReport report = new QuoteWeeklyReport();
                        report.setOrderId(orderId);
                        report.setFileName(drawingName);
                        report.setCsgOrderId(csgOrderId);
                        report.setDocumentPath(path2 + fileName);
                        report.setFileType(fileType);
                        report.setUploadDate(DateFormat.format());
                        report.setFactoryId(factoryId);
                        if(fileType == FileTypeEnum.VIDEO.getCode()){
                            String newNMame = FilenameUtils.removeExtension(fileName)+".mp4";
                            report.setFileName(drawingName);
                            //如果非mp4格式视频，则进行转码处理
                            if(!"mp4".equals(FilenameUtils.getExtension(drawingName))){
                              SynConvertVideo.sendRequest(path2+fileName,path3,newNMame);
                                report.setPhotoPath(UploadAndDownloadPathUtil.getQuoteFileStatic() + quoteName + pathName +"convert/"+ newNMame);
                                report.setPhotoPathCompress(UploadAndDownloadPathUtil.getQuoteFileStatic() + quoteName + pathName +"convert/"+ newNMame);
                            }else{
                                report.setPhotoPath(UploadAndDownloadPathUtil.getQuoteFileStatic() + quoteName + pathName + newNMame);
                                report.setPhotoPathCompress(UploadAndDownloadPathUtil.getQuoteFileStatic() + quoteName + pathName + newNMame);
                            }

                        }
                        reports.add(report);
                    }
                }


                Map<String,Object> map = new HashMap<String, Object>();
                map.put("reports", reports);

                return new JsonResult<Map<String,Object>>(map);
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("==========report   insertReport=========",e);
            return new JsonResult<Map<String,Object>>(1,"保存失败");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("==========report   insertReport=========",e);
            return new JsonResult<Map<String,Object>>(1,"保存失败");
        }
    }



    /**
     * 文件上传
     */
    @ResponseBody
    @RequestMapping(value="fileUploadPicture")
    public JsonResult<Map<String,Object>> fileUploadPicture(String imgdata,HttpServletRequest request) {

        BASE64Decoder decoder = new BASE64Decoder();
        FileOutputStream fos = null;
        try {
            String fileName = request.getParameter("fileName");
            if(StringUtils.isBlank(request.getParameter("projectStage"))){
                return new JsonResult<Map<String,Object>>(1,"未获取到项目阶段");
            }
            Integer projectStage = Integer.parseInt(request.getParameter("projectStage"));  //项目阶段
            String quoteName = "";
            Integer orderId = null;
            String csgOrderId = null;
            if(StringUtils.isNotBlank(request.getParameter("orderId"))){
                quoteName = request.getParameter("orderId");
                orderId = Integer.parseInt(request.getParameter("orderId"));
            }
            if(StringUtils.isNotBlank(request.getParameter("csgOrderId"))) {
                quoteName = request.getParameter("csgOrderId");
                csgOrderId = request.getParameter("csgOrderId");
            }
            /*
             * 进度报告 /100012/progress/
             */
            String path1 = UploadAndDownloadPathUtil.getQuoteFile() +  quoteName + File.separator;
            File file1 = new File(path1);
            if  (!file1.exists()  && !file1 .isDirectory())
            {
                file1 .mkdir();
            }

            //根据文件类型确定上传文件路径
            String path2 = "";
            String pathName = "";
            if(projectStage == ProjectStageEnum.PROGRESS_REPORT.getCode()){
                path2 = path1 + "progress" + File.separator;
                pathName = "/progress/";
            }else if(projectStage == ProjectStageEnum.MATERIAL_REPORT.getCode()){
                path2 =  path1 + "material" + File.separator;
                pathName = "/material/";
            }else if(projectStage == ProjectStageEnum.QUANLITY_REPORT.getCode()){
                path2 = path1 + "quality" + File.separator;
                pathName = "/quality/";
            }else if(projectStage == ProjectStageEnum.SHIPPING_REPORT.getCode()){
                path2 = path1 + "shipping" + File.separator;
                pathName = "/shipping/";
            }
            File file2 = new File(path2);
            if  (!file2.exists()  && !file2 .isDirectory())
            {
                file2 .mkdir();
            }

            //新的图片名称
            String newFileName = UUID.randomUUID() + fileName.substring(fileName.lastIndexOf("."));
            String imgPath=path2+newFileName;
            // new一个文件对象用来保存图片，默认保存当前工程根目录
            File imageFile = new File(imgPath);
            // 创建输出流
            fos = new FileOutputStream(imageFile);
            // 获得一个图片文件流，我这里是从flex中传过来的
            byte[] result = decoder.decodeBuffer(imgdata.split(",")[1]);//解码
            for (int i = 0; i < result.length; ++i) {
                if (result[i] < 0) {// 调整异常数据
                    result[i] += 256;
                }
            }
            fos.write(result);
            fos.flush();

            QuoteWeeklyReport report = new QuoteWeeklyReport();
            report.setOrderId(orderId);
            report.setFileName(fileName);
            report.setPhotoPath(UploadAndDownloadPathUtil.getQuoteFileStatic() + quoteName + pathName + newFileName);
            report.setPhotoPathCompress(UploadAndDownloadPathUtil.getQuoteFileStatic() + quoteName + pathName + newFileName);
            report.setFileType(FileTypeEnum.IMG.getCode());
            report.setUploadDate(DateFormat.format());

            Map<String,Object> map = new HashMap<String, Object>();
            map.put("report", report);
            return new JsonResult<Map<String,Object>>(map);

        } catch (Exception e1) {
            logger.error("上传图片失败", e1);
            return new JsonResult<Map<String,Object>>(1,"上传失败");
        }finally{
            try {
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 更新图片，旋转图片保存
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/rotateImg")
    @ResponseBody
    public JsonResult<String> rotateImg(HttpServletRequest request,HttpServletResponse response) {
        try {
            String filePath = request.getParameter("filePath");
            String degree = request.getParameter("degree");
            if(StringUtils.isNotBlank(degree)){
                ImageUtil.spin(Integer.parseInt(degree), filePath);
            }
            return new JsonResult<String>(0,"旋转成功");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return new JsonResult<String>(1,"旋转失败");
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResult<String>(1,"旋转失败");
        }
    }


    /**
     * 保存图片数据
     * @Title insertPhotos
     * @Description
     * @param request
     * @param response
     * @return
     * @return JsonResult<List<QuoteWeeklyReport>>
     */
    @ResponseBody
    @RequestMapping("/saveAll")
    public JsonResult<String> saveAll(HttpServletRequest request,HttpServletResponse response){

        try {
            String factoryId = WebCookie.getFactoryId(request);
            if(StringUtils.isBlank(factoryId)){
                return new JsonResult<String>(2,"未获取到登录信息");
            }else{

                if(StringUtils.isBlank(request.getParameter("orderId")) && StringUtils.isBlank(request.getParameter("csgOrderId"))){
                    return new JsonResult<String>(1,"未获取到询盘信息");
                }

                //获取图片列表信息
                List<QuoteWeeklyReport> reports = null;
                String reportList = request.getParameter("reportList");
                reportList = URLDecoder.decode(reportList,"utf-8");
                reportList = JsonUtil.getJsonArray(reportList);
                ObjectMapper objectMapper = new ObjectMapper();
                JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, QuoteWeeklyReport.class);
                reports = objectMapper.readValue(reportList,javaType);

                //获取询盘号
                Integer orderId = null;
                String csgOrderId = null;
                if(StringUtils.isNotBlank(request.getParameter("orderId"))){
                    orderId = Integer.parseInt(request.getParameter("orderId"));
                }
                csgOrderId = request.getParameter("csgOrderId");

                if(reports != null){
                    quoteWeeklyReportService.insertPhotosBatch(request,reports);
                }
                return new JsonResult<String>(0,"保存成功");
            }
        } catch (NumberFormatException e) {
            logger.error("==========inquiry   insertPhotos=========",e);
            return new JsonResult<String>(1,"未获取到信息");
        } catch (Exception e) {
            logger.error("==========inquiry   insertPhotos=========",e);
            return new JsonResult<String>(1,"保存失败");
        }

    }




    /**
     * 根据项目号查询列表（免工厂登陆）
     *
     * @Title reportList
     * @Description
     * @param request
     * @return
     * @return JsonResult<Map<String,Object>>
     */
    @RequestMapping("/reportList")
    public String reportList(HttpServletRequest request, Model model,HttpServletResponse response){

        String loginEmail = "";
        String userName = "";
        String pwd = "";
        Integer quoteStatus = null;
        try {
            //根据quoteStatus判断是否跳转生产列表页
            if(StringUtils.isNotBlank(request.getParameter("quoteStatus"))){
                quoteStatus = Integer.parseInt(request.getParameter("quoteStatus"));
            }

            //判断工厂id是否为空
            String csgOrderId = request.getParameter("csgOrderId");
            String supplierIdD = request.getParameter("supplierId");
            String supplierId = "";
            if(StringUtils.isNotBlank(supplierIdD)){
                supplierId = Base64Encode.getFromBase64(supplierIdD);
            }
            FactoryInfo factoryInfo = factoryInfoService.selectFactoryInfo(supplierId.trim());
            FactoryUser factoryUser = factoryUserService.selectByLoginEmail(factoryInfo.getEmail());
            //获取工厂资格认证
            String factoryId = factoryInfo.getFactoryId();
            List<Qualification> qualification = qualificationService.queryByFactory(factoryId);
            //保存cookie
            SetCookies.setCookie(request, response, factoryInfo, factoryUser, qualification);

            //如果quoteStatus不为空则跳转生产列表页
            if(quoteStatus!=null){
                return "redirect:"+GetServerPathUtil.getServerPath()+"/m-zh/quote_list.html?quoteStatus=2";
            }

            Integer orderId = null;
            if(StringUtils.isNotBlank(request.getParameter("orderId"))){
                orderId = Integer.parseInt(request.getParameter("orderId"));
            }
            //获取询盘号
            if(csgOrderId != null && orderId == null){
                QuoteInfo quoteInfo = quoteInfoService.queryByCgsOrderId(csgOrderId);
                if(quoteInfo!=null){
                    orderId = quoteInfo.getOrderId();
                }
            }

            //List<QuoteReportType> reportTypes = quoteReportTypeService.queryByOrderIdAndFactoryId(csgOrderId, orderId,supplierId);
            List<QuoteWeeklyReport> reports = quoteWeeklyReportService.queryByCsgOrderIdAndType(csgOrderId, null,supplierId);

            //获取任务系统项目详情
            ProjectVO projectVO = new ProjectVO();
            List<ProjectDrawing> drawings = null;
            GetTaskProject project = new GetTaskProject();
            Map<Object, Object> map = project.sendRequest(csgOrderId,supplierId);
            //获取projectVO对象
            if(map.get("projectVO")!= null){
                projectVO = (ProjectVO) SerializeUtil.JsonToObj(map.get("projectVO").toString(), ProjectVO.class);
                if(projectVO.getDeliveryDate()!=null){
                    String stamp2Date = DateFormat.timeStamp2Date(projectVO.getDeliveryDate(), "yyyy-MM-dd");
                    projectVO.setDeliveryDate(stamp2Date);
                }
                if(projectVO.getDateSampleUploading()!=null){
                    String stamp2Date = DateFormat.timeStamp2Date(projectVO.getDateSampleUploading(), "yyyy-MM-dd");
                    projectVO.setDateSampleUploading(stamp2Date);
                }
            }
            //获取受控图纸
            if(map.get("drawings")!= null){
               drawings = JSON.parseArray(map.get("drawings").toString(), ProjectDrawing.class);
               for(ProjectDrawing drawing:drawings){
                   if(drawing.getUploadTime()!=null){
                       drawing.setUploadTime(DateFormat.timeStamp2Date(drawing.getUploadTime(), "yyyy-MM-dd"));
                   }
               }
            }

            model.addAttribute("projectVO",projectVO);
            model.addAttribute("drawings",drawings);
            model.addAttribute("reports",reports);
//        List<QuoteMessage> messages = quoteMessageService.queryMessageByCsgOrderId(supplierId,null, orderId, csgOrderId);
//        model.addAttribute("messages",messages);
            model.addAttribute("factoryId",supplierId);
            model.addAttribute("csgOrderId",csgOrderId);
            model.addAttribute("orderId",orderId);
            model.addAttribute("supplierIdD",supplierIdD);
            model.addAttribute("supplier",factoryInfo);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return "../m-zh/big_good.html";

    }


    /**
     * 根据项目号查询列表
     * @Title reportListPurchase
     * @Description
     * @param request
     * @return
     * @return JsonResult<Map<String,Object>>
     */
    @RequestMapping("/reportListPurchase")
    public String reportListPurchase(HttpServletRequest request,HttpServletResponse response, Model model){


        try {
            String loginEmail = "";
            String realName = "";
            String userName = "";
            String pwd = "";
            String factoryId = request.getParameter("factoryId");
            if(StringUtils.isBlank(factoryId)){
                return "redirect:"+GetServerPathUtil.getServerPath()+"/m-zh/login.html";
            }
            realName = request.getParameter("realName");
            if(StringUtils.isBlank(realName)){
                return "redirect:"+GetServerPathUtil.getServerPath()+"/m-zh/login.html";
            }
            FactoryInfo factoryInfo = factoryInfoService.selectFactoryInfo(factoryId.trim());
            FactoryUser factoryUser = factoryUserService.selectByRealName(realName);
            if(factoryUser == null){
                return "redirect:"+GetServerPathUtil.getServerPath()+"/m-zh/login.html";
            }
            //获取工厂资格认证
            List<Qualification> qualification = qualificationService.queryByFactory(factoryId);
            //保存cookie
            SetCookies.setCookie(request, response, factoryInfo, factoryUser, qualification);


            String csgOrderId = request.getParameter("csgOrderId");
            String supplierId = request.getParameter("supplierId");
            //查询供应商工厂快评分
            Integer factoryScore = null;
            FactoryInfo supplier = null;
            if(StringUtils.isNotBlank(supplierId)){
                supplier = factoryInfoService.selectFactoryInfo(supplierId);
                if(supplier!=null){
                    factoryScore = supplier.getFactoryScore();
                }
            }
            Integer orderId = null;
            if(StringUtils.isNotBlank(request.getParameter("orderId"))){
                orderId = Integer.parseInt(request.getParameter("orderId"));
            }
            if(csgOrderId != null && orderId == null){
                QuoteInfo quoteInfo = quoteInfoService.queryByCgsOrderId(csgOrderId);
                if(quoteInfo!=null){
                    orderId = quoteInfo.getOrderId();
                }
            }


            List<QuoteWeeklyReport> reports = quoteWeeklyReportService.queryByCsgOrderIdAndType(csgOrderId, null, supplierId);
            //判断是否上传图片
            Boolean isPic = false;
            //判断是否上传视频
            Boolean isVideo = false;
            //判断是否上传材质证明
            Boolean isMeterial = false;
            //判断是否上传检验报告
            Boolean isQuality = false;
            for (QuoteWeeklyReport report:reports) {
                if(report.getFileType() == FileTypeEnum.IMG.getCode()){
                    isPic=true;
                }
                if(report.getFileType() == FileTypeEnum.VIDEO.getCode()){
                    isVideo=true;
                }
                if(report.getFileType() == FileTypeEnum.MATERIAL_FILE.getCode()){
                    isMeterial=true;
                }
                if(report.getFileType() == FileTypeEnum.QUANLITY_FILE.getCode()){
                    isQuality=true;
                }
            }
            model.addAttribute("reports",reports);
//        List<QuoteMessage> messages = quoteMessageService.queryMessageByCsgOrderId(factoryId, supplierId, orderId, csgOrderId);
//        model.addAttribute("messages",messages);

            //获取任务系统项目详情
            ProjectVO projectVO = new ProjectVO();
            List<ProjectDrawing> drawings = null;
            GetTaskProject project = new GetTaskProject();
            Map<Object, Object> map = project.sendRequest(csgOrderId,supplierId);
            //获取projectVO对象
            if(map.get("projectVO")!= null){
                projectVO = (ProjectVO) SerializeUtil.JsonToObj(map.get("projectVO").toString(), ProjectVO.class);
                if(projectVO.getDeliveryDate()!=null){
                    String stamp2Date = DateFormat.timeStamp2Date(projectVO.getDeliveryDate(), "yyyy-MM-dd");
                    projectVO.setDeliveryDate(stamp2Date);
                }
                if(projectVO.getDateSampleUploading()!=null){
                    String stamp2Date = DateFormat.timeStamp2Date(projectVO.getDateSampleUploading(), "yyyy-MM-dd");
                    projectVO.setDateSampleUploading(stamp2Date);
                }
            }
            //获取受控图纸
            if(map.get("drawings")!= null){
                drawings = JSON.parseArray(map.get("drawings").toString(), ProjectDrawing.class);
                for(ProjectDrawing drawing:drawings){
                    if(drawing.getUploadTime()!=null){
                        drawing.setUploadTime(DateFormat.timeStamp2Date(drawing.getUploadTime(), "yyyy-MM-dd"));
                    }
                }
            }

            model.addAttribute("projectVO",projectVO);
            model.addAttribute("drawings",drawings);
            model.addAttribute("factoryId",factoryId);
            model.addAttribute("csgOrderId",csgOrderId);
            model.addAttribute("orderId",orderId);
            model.addAttribute("supplierId",supplierId);
            model.addAttribute("supplierIdD",Base64Encode.getBase64(supplierId));
            model.addAttribute("factoryScore",factoryScore);
            model.addAttribute("supplier",supplier);
            model.addAttribute("isPic",isPic);
            model.addAttribute("isVideo",isVideo);
            model.addAttribute("isMeterial",isMeterial);
            model.addAttribute("isQuality",isQuality);
            //查询报告最新日期
            model.addAttribute("uploadDate",(reports != null && reports.size() >0) ? reports.get(0).getUploadDate() : null);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "../m-zh/big_good_self.html";
    }








    /**
     * 根据项目号查询列表
     * @Title reportListOther
     * @Description
     * @param request
     * @return
     * @return JsonResult<Map<String,Object>>
     */
    @RequestMapping("/reportListOther")
    public String reportListOther(HttpServletRequest request,HttpServletResponse response, Model model){

        String csgOrderId = request.getParameter("csgOrderId");
        String supplierId = request.getParameter("supplierId");
        String factoryId = WebCookie.getFactoryId(request);
        if(StringUtils.isBlank(factoryId)){
            String requestUrl = GetServerPathUtil.getServerPath()
                    + request.getServletPath() //请求的相对url
                    + (StringUtils.isBlank(request.getQueryString())?"":("?"+request.getQueryString()));
            //登录前网址session
            HttpSession session = request.getSession();
            session.setAttribute("historyUrl",""+requestUrl+"");
            session.setMaxInactiveInterval(60*10);
            return "redirect:"+GetServerPathUtil.getServerPath()+"/m-zh/login.html";
        }
        //查询供应商工厂快评分
        Integer factoryScore = null;
        if(StringUtils.isNotBlank(supplierId)){
            FactoryInfo supplier = factoryInfoService.selectFactoryInfo(supplierId);
            if(supplier!=null){
                factoryScore = supplier.getFactoryScore();
            }
        }
        Integer orderId = null;
        if(StringUtils.isNotBlank(request.getParameter("orderId"))){
            orderId = Integer.parseInt(request.getParameter("orderId"));
        }
        if(csgOrderId != null && orderId == null){
            QuoteInfo quoteInfo = quoteInfoService.queryByCgsOrderId(csgOrderId);
            if(quoteInfo!=null){
                orderId = quoteInfo.getOrderId();
            }
        }

        List<QuoteWeeklyReport> reports = quoteWeeklyReportService.queryByCsgOrderIdAndType(csgOrderId, null, supplierId);
        model.addAttribute("reports",reports);
//        List<QuoteMessage> messages = quoteMessageService.queryMessageByCsgOrderId(factoryId, supplierId, orderId, csgOrderId);
//        model.addAttribute("messages",messages);
        model.addAttribute("factoryId",factoryId);
        model.addAttribute("csgOrderId",csgOrderId);
        model.addAttribute("orderId",orderId);
        model.addAttribute("supplierId",supplierId);
        model.addAttribute("factoryScore",factoryScore);
        return "../m-zh/big_good_self.html";
    }











    /**
     * 根据项目号查询列表
     * @Title uploadPhoto
     * @Description
     * @param request
     * @return
     * @return JsonResult<Map<String,Object>>
     */
    @RequestMapping("/queryByReportTypeId")
    public String queryByReportTypeId(HttpServletRequest request, Model model){
        //判断工厂id是否为空
        String factoryId = WebCookie.getFactoryId(request);
        if(StringUtils.isBlank(factoryId)){
            return "redirect:"+GetServerPathUtil.getServerPath()+"/zh/login.html";
        }
        //查询供应商工厂快评分
        Integer factoryScore = null;
        if(StringUtils.isNotBlank(factoryId)){
            FactoryInfo factoryInfo = factoryInfoService.selectFactoryInfo(factoryId);
            if(factoryInfo!=null){
                factoryScore = factoryInfo.getFactoryScore();
            }
        }
        Integer reportTypeId = null;
        if(StringUtils.isNotBlank(request.getParameter("reportTypeId"))){
            reportTypeId = Integer.parseInt(request.getParameter("reportTypeId"));
        }
        QuoteReportType reportType = quoteReportTypeService.selectByPrimaryKey(reportTypeId);
        model.addAttribute("reportType",reportType);
        model.addAttribute("factoryId",factoryId);
        model.addAttribute("factoryScore",factoryScore);

        return "../m-zh/graphic_preview.html";
    }


    /**
     * 根据项目号查询列表(采购商查询)
     * @Title uploadPhoto
     * @Description
     * @param request
     * @return
     * @return JsonResult<Map<String,Object>>
     */
    @RequestMapping("/queryByReportTypeIdPurchase")
    public String queryByReportTypeIdPurchase(HttpServletRequest request, Model model){
        //判断工厂id是否为空
        String factoryId = WebCookie.getFactoryId(request);
        if(StringUtils.isBlank(factoryId)){
            return "redirect:"+GetServerPathUtil.getServerPath()+"/zh/login.html";
        }
        Integer reportTypeId = null;
        if(StringUtils.isNotBlank(request.getParameter("reportTypeId"))){
            reportTypeId = Integer.parseInt(request.getParameter("reportTypeId"));
        }
        QuoteReportType reportType = quoteReportTypeService.selectByPrimaryKey(reportTypeId);
        model.addAttribute("reportType",reportType);
        model.addAttribute("factoryId",factoryId);

        return "../m-zh/graphic_preview_self.html";
    }


    /**
     * 保存图片数据
     * @Title insertPhotos
     * @Description
     * @param request
     * @param response
     * @return
     * @return JsonResult<List<QuoteWeeklyReport>>
     */
    @ResponseBody
    @RequestMapping("/updateReport")
    public JsonResult<String> updateReport(HttpServletRequest request,HttpServletResponse response){
        Integer reportId = null;
        if(StringUtils.isNotBlank(request.getParameter("reportId"))){
            reportId = Integer.parseInt(request.getParameter("reportId"));
        }
          QuoteWeeklyReport report = new QuoteWeeklyReport();
          report.setId(reportId);
          if(StringUtils.isBlank(request.getParameter("remark"))){
              report.setRemark("");
          }else{
              report.setRemark(request.getParameter("remark"));
          }
          quoteWeeklyReportService.updateByPrimaryKeySelective(report);
        return new JsonResult<String>(0,"更新成功");
    }




    /**
     * 删除报告（进度报告、材质证明、检测报告、出运报告）
     * @Title deleteReport
     * @Description
     * @param request
     * @param response
     * @return
     * @return JsonResult<String>
     */
    @ResponseBody
    @RequestMapping("/deleteReport")
    public JsonResult<String> deleteReport(HttpServletRequest request,HttpServletResponse response){

        try {
            String factoryId = WebCookie.getFactoryId(request);
            if(StringUtils.isBlank(factoryId)){
                return new JsonResult<String>(2,"未获取到登录信息");
            }else{
                if(StringUtils.isBlank(request.getParameter("id"))){
                    return new JsonResult<String>(1,"未获取报告id");
                }else{
                    Integer id = Integer.parseInt(request.getParameter("id"));
                    quoteWeeklyReportService.deleteByPrimaryKey(id);
                    return new JsonResult<String>(0,"删除成功");
                }
            }
        } catch (NumberFormatException e) {
            logger.error("==========report   deleteReport=========",e);
            return new JsonResult<String>(1,"未获取到ID信息");
        } catch (Exception e) {
            logger.error("==========report   deleteReport=========",e);
            return new JsonResult<String>(1,"删除失败");
        }

    }


    /**
     * 根据id查询图片
     * @Title queryPhotoById
     * @Description
     * @param request
     * @param response
     * @return
     * @return JsonResult<String>
     */
    @ResponseBody
    @RequestMapping("/queryPhotoById.do")
    public JsonResult<QuoteWeeklyReport> queryPhotoById(HttpServletRequest request,HttpServletResponse response){

        try {
            String factoryId = WebCookie.getFactoryId(request);
            if(StringUtils.isBlank(factoryId)){
                return new JsonResult<QuoteWeeklyReport>(2,"未获取到登录信息");
            }else{
                Integer id = Integer.parseInt(request.getParameter("id"));
                QuoteWeeklyReport report = quoteWeeklyReportService.selectByPrimaryKey(id);
                return new JsonResult<QuoteWeeklyReport>(report);
            }
        } catch (NumberFormatException e) {
            logger.error("==========inquiry   queryPhotoById=========",e);
            return new JsonResult<QuoteWeeklyReport>(1,"未获取到ID信息");
        } catch (Exception e) {
            logger.error("==========inquiry   queryPhotoById=========",e);
            return new JsonResult<QuoteWeeklyReport>(1,"查询失败");
        }


    }


    /**
     * 根据id更新图片信息
     * @Title updatePhotoById
     * @Description
     * @param request
     * @param response
     * @return
     * @return JsonResult<QuoteWeeklyReport>
     */
    @ResponseBody
    @RequestMapping("/updatePhotoById.do")
    public JsonResult<String> updatePhotoById(HttpServletRequest request,HttpServletResponse response){
        try {
            String factoryId = WebCookie.getFactoryId(request);
            if(StringUtils.isBlank(factoryId)){
                return new JsonResult<String>(2,"未获取到登录信息");
            }else{
                Integer id = Integer.parseInt(request.getParameter("id"));
                QuoteWeeklyReport report = quoteWeeklyReportService.selectByPrimaryKey(id);
                if(StringUtils.isNotBlank(request.getParameter("originalName"))){
                    report.setFileName(request.getParameter("originalName"));
                }
                if(StringUtils.isNotBlank(request.getParameter("newName"))){
                    String newName = request.getParameter("newName");
                    report.setPhotoPath(UploadAndDownloadPathUtil.getQuoteFileStatic() + report.getOrderId() + "/photo/" +newName);
                    report.setPhotoPathCompress(UploadAndDownloadPathUtil.getQuoteFileStatic() + report.getOrderId() + "/photo/" + OperationFileUtil.changeFilenameZip(newName));
                    if(StringUtils.isNotBlank(request.getParameter("remark"))){
                        report.setRemark(request.getParameter("remark"));
                    }
                    quoteWeeklyReportService.updateByPrimaryKeySelective(report);
                }
                return new JsonResult<String>(0,"更新成功");
            }
        } catch (NumberFormatException e) {
            logger.error("==========inquiry   updatePhotoById=========",e);
            return new JsonResult<String>(1,"未获取到ID信息");
        } catch (Exception e) {
            logger.error("==========inquiry   updatePhotoById=========",e);
            return new JsonResult<String>(1,"更新失败");
        }


    }


}
