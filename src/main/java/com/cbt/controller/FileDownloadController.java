package com.cbt.controller;

import com.cbt.entity.*;
import com.cbt.service.*;
import com.cbt.util.DateFormat;
import com.cbt.util.OperationFileUtil;
import com.cbt.util.UploadAndDownloadPathUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;


@RequestMapping("/download")
@Controller
public class FileDownloadController {
    	
	
	
		  @Autowired
		  private QuoteMessageService quoteMessageService; 
		  
		  @Autowired
		  private QuoteProductService quoteProductService; 
		  
		  @Autowired
		  private QualificationService qualificationService;
		  
		  @Autowired
		  private QuoteInfoService quoteInfoService;
		  
		  @Autowired
		  private NoteMessageService noteMessageService;
		  
		  @Autowired
		  private QuoteWeeklyReportService quoteWeeklyReportService;
		  
		  @Autowired
		  private SupplierQuoteInfoService supplierQuoteInfoService;
		  
		  
		  @Autowired
		  private FactoryInfoService factoryInfoService;

		  @Autowired
		  private QuoteReportTypeService quoteReportTypeService;
	    /**
	     * 文件下载Controller
	     */
	    
	    /**
	     * 根据路径下载
	     * @param request
	     * @param response
	     * @throws IOException 
	     * @throws UnsupportedEncodingException 
	     * @throws Exception 
	     */
	    @RequestMapping(value = "message-file-download.do")
	    public HttpServletResponse fileDownload_message(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, IOException{
	    	if(StringUtils.isNotBlank(request.getParameter("id"))){
	    		  Integer id = Integer.parseInt(request.getParameter("id"));
	    		  QuoteMessage quoteMessage = quoteMessageService.selectByPrimaryKey(id);
	    		  HttpServletResponse download  = OperationFileUtil.download(quoteMessage.getFileName(),quoteMessage.getFilePath(),response);
			      return download;
		    	}else{
		    		throw new RuntimeException("未获取到路径");
		    	}	       
	   }
	    
	    /**
	     * 下载供应商报价附件
	     * @Title fileDownload_quote 
	     * @Description TODO
	     * @param request
	     * @param response
	     * @return
	     * @throws UnsupportedEncodingException
	     * @throws IOException
	     * @return ResponseEntity<byte[]>
	     */
	    @RequestMapping(value = "quote-file-download.do")
	    public HttpServletResponse fileDownload_quote(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, IOException{
	    	if(StringUtils.isNotBlank(request.getParameter("id"))){
//                String path = Base64Encode.getFromBase64(request.getParameter("path"));
                SupplierQuoteInfo supplierQuoteInfo = supplierQuoteInfoService.selectByPrimaryKey(Integer.parseInt(request.getParameter("id")));
                HttpServletResponse download = OperationFileUtil.download(supplierQuoteInfo.getAttachmentName(),supplierQuoteInfo.getAttachmentPath(),response);
	    		return download;
	    	}else{
	    		throw new RuntimeException("未获取到路径");
	    	}	       
	    }

	/**
	 * 下载保密协议
	 * @Title fileDownload_confidentiality
	 * @Description
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 * @return ResponseEntity<byte[]>
	 */
	@RequestMapping(value = "file-confidentiality.do")
	public HttpServletResponse fileDownload_confidentiality(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, IOException{
		if(StringUtils.isNotBlank(request.getParameter("orderId"))){
            QuoteInfo quoteInfo = quoteInfoService.queryByOrderId(Integer.parseInt(request.getParameter("orderId")));
			HttpServletResponse download = OperationFileUtil.download(quoteInfo.getConfidentialityFileName(),quoteInfo.getFilePath(),response);
			return download;
		}else{
			throw new RuntimeException("未获取到路径");
		}
	}
	    
	    
	    
	    @RequestMapping(value = "drawing-download.do")
	    public HttpServletResponse fileDownload_drawing(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, IOException{
	    	if(StringUtils.isNotBlank(request.getParameter("orderId"))){
	    		Integer orderId = Integer.parseInt(request.getParameter("orderId"));
	    		QuoteInfo quoteInfo = quoteInfoService.queryByOrderId(orderId);
	    		HttpServletResponse download = OperationFileUtil.download(quoteInfo.getDrawingName(),quoteInfo.getDrawingPath(),response);
	    		return download;
	    	}else{
	    		throw new RuntimeException("未获取到路径");
	    	}	       
	    }
	    
	    @RequestMapping(value = "qualification-download.do")
	    public HttpServletResponse qualificationFileDownload_drawing(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, IOException{
	    	if(StringUtils.isNotBlank(request.getParameter("id"))){
	    		Integer id = Integer.parseInt(request.getParameter("id"));
	    		String downloadUrl = "";
	    		Qualification qualification = qualificationService.selectQualificationById(id);
	    
	    		downloadUrl = UploadAndDownloadPathUtil.getQualificationFile()+qualification.getFactoryId()+File.separator+qualification.getFileUrl()	;	
	    		
	    		HttpServletResponse download = OperationFileUtil.download(qualification.getQualificationName(),downloadUrl,response);
	    		return download;
	    	}else{
	    		throw new RuntimeException("未获取到路径");
	    	}	       
	    	
	    }
	    
	    @RequestMapping(value = "drawingfiledownload.do")
	    public HttpServletResponse drawingFileDownload(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, IOException{
	    	
	    	if(StringUtils.isNotBlank(request.getParameter("id"))){
	    		Integer id = Integer.parseInt(request.getParameter("id"));
	    		String downloadUrl = "";
	    		QuoteInfo quoteInfo = quoteInfoService.selectByPrimaryKey(id);
	    
	    		downloadUrl = quoteInfo.getDrawingPath();
	    		
	    		HttpServletResponse download = OperationFileUtil.download(quoteInfo.getDrawingName(),downloadUrl, response);
//	    		ResponseEntity<byte[]> download = OperationFileUtil.download(downloadUrl);
	    		return download;
	    	}else{
	    		throw new RuntimeException("未获取到路径");
	    	}	       
	    	
	    }
	    	
	    @RequestMapping(value = "messageFiledownload.do")
	    public HttpServletResponse messageFiledownload(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, IOException{
	    	
	    	if(StringUtils.isNotBlank(request.getParameter("id"))){
	    		Integer id = Integer.parseInt(request.getParameter("id"));
	    		String downloadUrl = "";
	    		
	    		NoteMessage nm = noteMessageService.selectByPrimaryKey(id);
	    		if(nm!=null){
	    			downloadUrl = nm.getFilePath();
	    		}
	    		
	    		
	    		HttpServletResponse download = OperationFileUtil.download(nm.getFileName(),downloadUrl,response);
	    		return download;
	    	}else{
	    		throw new RuntimeException("未获取到路径");
	    	}	       
	    	
	    }
	    
	    
	    
	    /**
	     * 用于下载进度报告、材质证明、质检报告
	     * @Title messageFiledownload 
	     * @Description TODO
	     * @param request
	     * @param response
	     * @return
	     * @throws UnsupportedEncodingException
	     * @throws IOException
	     * @return ResponseEntity<byte[]>
	     */
	    @RequestMapping(value = "report-download.do")
	    public HttpServletResponse reportDownload(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, IOException{
	    	try {
				QuoteWeeklyReport report = new QuoteWeeklyReport();
				HttpServletResponse download = null;
				if(StringUtils.isNotBlank(request.getParameter("id"))){
				    report = quoteWeeklyReportService.selectByPrimaryKey(Integer.parseInt(request.getParameter("id")));
				    if(report != null){
				    	download = OperationFileUtil.download(report.getFileName(),report.getDocumentPath(),response);
				    }
				    report.setReadTime(DateFormat.format());
				    report.setIsRead(1);
				    quoteWeeklyReportService.updateByPrimaryKeySelective(report);
				}
				return download;
			} catch (NumberFormatException e) {
				e.printStackTrace();
				throw new RuntimeException("未获取到路径");
			}
	    }
	    
	    /**
	     * 用于下载设备清单报告
	     * @Title equipmentDownload
	     * @Description TODO
	     * @param request
	     * @param response
	     * @return
	     * @throws UnsupportedEncodingException
	     * @throws IOException
	     * @return ResponseEntity<byte[]>
	     */
	    @RequestMapping(value = "equipment-download.do")
	    public HttpServletResponse equipmentDownload(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, IOException{
	    	try {
				FactoryInfo factoryInfo = new FactoryInfo();
				HttpServletResponse download = null;
				if(StringUtils.isNotBlank(request.getParameter("id"))){
					factoryInfo = factoryInfoService.selectByPrimaryKey(Integer.parseInt(request.getParameter("id")));
				    if(factoryInfo != null){
				    	download = OperationFileUtil.download(factoryInfo.getFactoryEquipmentDocument(),response);
				    }
				}
				return download;
			} catch (NumberFormatException e) {
				e.printStackTrace();
				throw new RuntimeException("未获取到路径");
			}
	    }




	/**
	 * 根据路径下载
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	@RequestMapping(value = "download_report")
	public HttpServletResponse download_report(HttpServletRequest request, HttpServletResponse response){
		if(StringUtils.isNotBlank(request.getParameter("id"))){
			Integer id = Integer.parseInt(request.getParameter("id"));
			QuoteWeeklyReport report = quoteWeeklyReportService.selectByPrimaryKey(id);
			HttpServletResponse download = OperationFileUtil.download(report.getFileName(),report.getDocumentPath(),response);
			return download;
		}else{
			throw new RuntimeException("未获取到路径");
		}
	}
	
}
