package com.cbt.entity;
/**
 * 增加虚拟字段message_status 用于移动端判断是否发送消息
 * Auth:Polo  
 * 2018/4/18
 */
import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true) 
public class QuoteProduct implements Cloneable,Serializable{
    private Integer id;

	private String customerId;                                        //客户id
	private Integer orderId;                                           //订单号
	private String productName;                                       //产品名
	private String materials;                                         //材料
	private String process;                                           //基本工艺
	private String quantityList;                                      //数量集合（逗号隔开）
	private String quantityUnit;                                      //数量单位
	private String deliveryDate;                                      //交期时间
	private String drawingPath;                                       //图片路径
	private String drawingPathCompress;                               //产品缩略图
	private String filePath;                                          //图纸路径   //add by sang
	private String videoPath;                                         //视频路径
	
	private String annualQuantity;                                    //年度订量
	private String productRemark;                                     //产品备注 
	private Double targetPrice;                                       //目标价格
	private Integer targetPriceStatus;                                //是否显示目标价（0：不显示 1：显示）
	private Integer deliveryType;                                     //交期选择（0：自定义分批交货 1：一次交货）
	private Double weight;                                            //重量
	private Integer cgsQuotationId;                                   //内部报价产品表ID 用于同步产品原图纸
	private Double targetMoldPrice;                                   //目标模具价格

	private Double length;      //零件长
	private Double width;       //宽
	private Double high;        //高

	
	//虚拟字段
	private String country;                                            //国家
	private Integer state;                                             //地区
    private Integer collectState;                                      //个人是否收藏
	private String publishDate;                                        //发布日期
	private String mainProcess;                                        //主工艺
	private String quoteTitle;                                         //报价标题
	private Integer quoteState;                                        //是否已报价
	private Integer orderStatus;                                       //订单状态
	private Integer messageStatus;                                     //是否发送消息（用于移动端）
	private Integer isInspectedFactory;                                //是否验厂
	private Integer isInspectedProduct;                                //是否验货
	private Integer isProcess;                                         //是否大货
	private String inviteFactory;                                      //邀请工厂
	private String csgOrderId;                                         //内部报价项目号
	private Integer currentNumber;                                     //当前报价人数
	private String quoteDetail;                                        //报价详情
	private String quoteRemark;                                        //报价工程师谈该项目
	private double totalAmount;                                        //预计总价
	private Integer maxNumber;                                         //允许最大报价工厂数


	@Override
	public String toString() {
		return "QuoteProduct{" +
				"id=" + id +
				", customerId='" + customerId + '\'' +
				", orderId=" + orderId +
				", productName='" + productName + '\'' +
				", materials='" + materials + '\'' +
				", process='" + process + '\'' +
				", quantityList='" + quantityList + '\'' +
				", quantityUnit='" + quantityUnit + '\'' +
				", deliveryDate='" + deliveryDate + '\'' +
				", drawingPath='" + drawingPath + '\'' +
				", drawingPathCompress='" + drawingPathCompress + '\'' +
				", filePath='" + filePath + '\'' +
				", videoPath='" + videoPath + '\'' +
				", annualQuantity='" + annualQuantity + '\'' +
				", productRemark='" + productRemark + '\'' +
				", targetPrice=" + targetPrice +
				", targetPriceStatus=" + targetPriceStatus +
				", deliveryType=" + deliveryType +
				", weight=" + weight +
				", cgsQuotationId=" + cgsQuotationId +
				", targetMoldPrice=" + targetMoldPrice +
				", length=" + length +
				", width=" + width +
				", high=" + high +
				", country='" + country + '\'' +
				", state=" + state +
				", collectState=" + collectState +
				", publishDate='" + publishDate + '\'' +
				", mainProcess='" + mainProcess + '\'' +
				", quoteTitle='" + quoteTitle + '\'' +
				", quoteState=" + quoteState +
				", orderStatus=" + orderStatus +
				", messageStatus=" + messageStatus +
				", isInspectedFactory=" + isInspectedFactory +
				", isInspectedProduct=" + isInspectedProduct +
				", isProcess=" + isProcess +
				", inviteFactory='" + inviteFactory + '\'' +
				", csgOrderId='" + csgOrderId + '\'' +
				", currentNumber=" + currentNumber +
				", quoteDetail='" + quoteDetail + '\'' +
				", quoteRemark='" + quoteRemark + '\'' +
				", totalAmount=" + totalAmount +
				", maxNumber=" + maxNumber +
				'}';
	}

	public String getQuoteRemark() {
		return quoteRemark;
	}

	public void setQuoteRemark(String quoteRemark) {
		this.quoteRemark = quoteRemark;
	}

	public Double getLength() {
		return length;
	}

	public void setLength(Double length) {
		this.length = length;
	}

	public Double getWidth() {
		return width;
	}

	public void setWidth(Double width) {
		this.width = width;
	}

	public Double getHigh() {
		return high;
	}

	public void setHigh(Double high) {
		this.high = high;
	}

	public Integer getMaxNumber() {
		return maxNumber;
	}

	public void setMaxNumber(Integer maxNumber) {
		this.maxNumber = maxNumber;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getQuoteDetail() {
		return quoteDetail;
	}

	public void setQuoteDetail(String quoteDetail) {
		this.quoteDetail = quoteDetail;
	}

	public Integer getMessageStatus() {
		return messageStatus;
	}

	public void setMessageStatus(Integer messageStatus) {
		this.messageStatus = messageStatus;
	}

	public Double getTargetMoldPrice() {
		return targetMoldPrice;
	}

	public void setTargetMoldPrice(Double targetMoldPrice) {
		this.targetMoldPrice = (targetMoldPrice == null ? 0.0 : targetMoldPrice);
	}

	public Integer getCurrentNumber() {
		return currentNumber;
	}

	public void setCurrentNumber(Integer currentNumber) {
		this.currentNumber = currentNumber;
	}

	public String getCsgOrderId() {
		return csgOrderId;
	}

	public void setCsgOrderId(String csgOrderId) {
		this.csgOrderId = csgOrderId;
	}

	public String getVideoPath() {
		return videoPath;
	}

	public void setVideoPath(String videoPath) {
		this.videoPath = videoPath;
	}

	public String getInviteFactory() {
		return inviteFactory;
	}

	public void setInviteFactory(String inviteFactory) {
		this.inviteFactory = inviteFactory;
	}

	public Integer getIsInspectedFactory() {
		return isInspectedFactory;
	}

	public void setIsInspectedFactory(Integer isInspectedFactory) {
		this.isInspectedFactory = isInspectedFactory;
	}

	public Integer getIsInspectedProduct() {
		return isInspectedProduct;
	}

	public void setIsInspectedProduct(Integer isInspectedProduct) {
		this.isInspectedProduct = isInspectedProduct;
	}

	public Integer getIsProcess() {
		return isProcess;
	}

	public void setIsProcess(Integer isProcess) {
		this.isProcess = isProcess;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getQuoteTitle() {
		return quoteTitle;
	}

	public void setQuoteTitle(String quoteTitle) {
		this.quoteTitle = quoteTitle;
	}

	public Integer getCgsQuotationId() {
		return cgsQuotationId;
	}

	public void setCgsQuotationId(Integer cgsQuotationId) {
		this.cgsQuotationId = cgsQuotationId;
	}

	public Integer getQuoteState() {
		return quoteState;
	}

	public void setQuoteState(Integer quoteState) {
		this.quoteState = quoteState;
	}

	public String getMainProcess() {
		return mainProcess;
	}

	public void setMainProcess(String mainProcess) {
		this.mainProcess = mainProcess;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(String publishDate) {
		
       this.publishDate = (publishDate == null ? "" : publishDate);

	}

	public Integer getCollectState() {
		return collectState;
	}

	public void setCollectState(Integer collectState) {
		this.collectState = collectState;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName == null ? null : productName.trim();
    }

    public String getMaterials() {
        return materials;
    }

    public void setMaterials(String materials) {
        this.materials = materials == null ? null : materials.trim();
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process == null ? null : process.trim();
    }

    public String getQuantityUnit() {
        return quantityUnit;
    }

    public void setQuantityUnit(String quantityUnit) {
        this.quantityUnit = quantityUnit == null ? null : quantityUnit.trim();
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = (deliveryDate == null ? "" : deliveryDate);
    }

    public String getDrawingPath() {
        return drawingPath;
    }

    public void setDrawingPath(String drawingPath) {
        this.drawingPath = drawingPath == null ? null : drawingPath.trim();
    }

    public String getDrawingPathCompress() {
        return drawingPathCompress;
    }

    public void setDrawingPathCompress(String drawingPathCompress) {
        this.drawingPathCompress = drawingPathCompress == null ? null : drawingPathCompress.trim();
    }

    public String getAnnualQuantity() {
        return annualQuantity;
    }

    public void setAnnualQuantity(String annualQuantity) {
        this.annualQuantity = annualQuantity;
    }

    public Double getTargetPrice() {
        return targetPrice;
    }

    public void setTargetPrice(Double targetPrice) {
        this.targetPrice = (targetPrice == null ? 0.0 : targetPrice);
    }

    public Integer getTargetPriceStatus() {
        return targetPriceStatus;
    }

    public void setTargetPriceStatus(Integer targetPriceStatus) {
        this.targetPriceStatus = targetPriceStatus;
    }

    public Integer getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(Integer deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getProductRemark() {
        return productRemark;
    }

    public void setProductRemark(String productRemark) {
        this.productRemark = productRemark == null ? null : productRemark.trim();
    }

	public String getQuantityList() {
		return quantityList;
	}

	public void setQuantityList(String quantityList) {
		this.quantityList = quantityList;
	}

	
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}



	@Override
	public Object clone() {
		QuoteProduct stu = null;
		try{
			stu = (QuoteProduct)super.clone();
		}catch(CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return stu;
	}
}