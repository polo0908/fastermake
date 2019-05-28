package com.cbt.util;

import java.util.List;

import com.cbt.entity.QuoteInfo;
import com.cbt.entity.QuoteProduct;
import com.cbt.entity.SupplierQuoteInfo;
import com.cbt.entity.SupplierQuoteProduct;

public interface GetQuoteMailUtil {

	
	
	public static String getQuoteMail(SupplierQuoteInfo supplierQuoteInfo,QuoteInfo quoteInfo,List<SupplierQuoteProduct> quoteProducts,QuoteProduct quoteProduct){
		String content =
				 "<!DOCTYPE html>"+
		         "<html>"+
		         "<head>"+
		         "<meta charset=\"utf-8\">"+
		         "<meta name=\"viewport\" content=\"width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no\"/>"+
		         "<title>报价提醒</title>"+
		             "<style>"+
		             	"body{font-family: sans-serif,arial;}"+
		             	".wrap-mind{font-size:14px;border:1px dashed #989898;width:800px;margin: auto;padding: 10px 20px 50px;color: #666;border-radius: 30px;}"+
		             	".email-head{margin-top: 20px;}"+
		             	".email-head h1{text-align: center;font-size: 24px;font-weight: bold;border-bottom: 1px dashed #989898;padding-bottom: 20px;color:#ff9d0b}"+
//		             	".email-head p span{width:50%;float: left;}"+
		             	".email-p{display: inline-block !important;margin-bottom:0px;margin-top: 15px !important;}"+
		             	".email-head p {display:block;margin-top:5px;}"+
		             	".email-wrap h2{text-align: left;border-top: 1px dashed #989898;padding-top: 10px;}"+
		             	".email-wrap table td{padding: 10px 10px 10px 0px;vertical-align: top;}"+
		             	".email-wrap table td span{margin-left: 10px;}"+
		             	".email-wrap table td span a{color:#33D4FF}"+
		             	".email-style b{width: 102px;float: left;}"+
		             	".email-style span{width: 310px;float: left;}"+
		             	
		             "</style><body>"+
		         "<div class=\"wrap-mind\">"+
		         	"<div class=\"email-head\">"+
		         		"<h1>询盘报价反馈</h1>"+	
		         		"<p><span><b>询盘号:</b>"+quoteInfo.getOrderId()+"</span><span style=\"margin-left:60px;\"><b>总价（元）:</b>"+supplierQuoteInfo.getTotalAmount()+"</span></p>"+
		         		"<p class=\"email-p\">尊敬的客户,</p>"+
		         		"<p>您有一个新的报价.<a href="+GetServerPathUtil.getServerPath()+"/zh/purchase_detail.html?orderId="+quoteInfo.getOrderId()+">查看</a></p>"+
		         	"</div>"+
		         	"<div class=\"email-wrap\">"+
		         		"<h2>报价信息</h2>"+
		         		"<span>询盘标题:"+quoteInfo.getQuoteTitle()+"</span>"+
		         		"<table>"+
		         			"<tr>"+
		         				"<td><b>产品名:</b><span>"+(quoteProduct.getProductName() == null ? quoteInfo.getQuoteTitle() : quoteProduct.getProductName())+"</span></td>"+
		         			"</tr>"+
		         			"<tr>"+
	         				    "<td><b>产品数量:</b><span>"+quoteProduct.getQuantityList()+"</span></td>"+
	         			    "</tr>"+	
		         			"<tr>"+
		         				"<td><b>产品单价（元）:</b><span>"+quoteProducts.get(0).getQuoteProductPrice()+"</span></td>"+
		         			"</tr>"+	
		         			"<tr>"+
	         				    "<td><b>模具费（元）:</b><span>"+quoteProducts.get(0).getQuoteMoldPrice1()+"</span></td>"+
	         			    "</tr>"+	
		         			"<tr>"+
	        				    "<td><b>备注:</b><span>"+quoteProducts.get(0).getProductRemark()+"</span></td>"+
	        			    "</tr>"+		
		         		"</table>"+
		         	"</div>"+
		         "</div></body></html>";
		
		
		return content;
	}
	
}
