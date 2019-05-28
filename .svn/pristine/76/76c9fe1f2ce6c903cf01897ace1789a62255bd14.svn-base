package com.cbt.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
/**
 * 项目实体对象
 * @author Administrator
 *
 */
public class ProjectVO implements Serializable {

    /**
	 * @fieldName serialVersionUID
	 * @fieldType long
	 * @Description TODO
	 */
	private static final long serialVersionUID = 1L;

	private String projectNo;

    private String projectName;

    private String deliveryDate;
    
    private Date scheduledDate;

    private Date actualStartDate;

    private Date createDate;
    
    private int finish;  
    
    private Date sampleScheduledDate;//样品交期       
    
    private String companyName;  //工厂名称
        
    private String purchaseName; //采购
    
    private String sellName;//跟单
    
    private String zhijian1;
    
    private String zhijian2;
    
    private String zhijian3;
    
    private String projectAmount;
    
    private Date finishTime; //完成时间
    
    private Date sampleFinishTime;  //样品完成时间
    
    //ERP过来的三个字段
    /** 样品交期*/
    private Date dateSample; //样品交期
    /** 合同交期*/
    private Date completionTime; //大货交期
    /** 上传日期*/
    private String dateSampleUploading; //合同签订日期(也用于记录日期录入时间)
    
    private String productImg;//产品图;
    
    private int sampleFinish; //样品完结;        
    
    private Integer projectStatus;//项目状态  0:新立项项目 1：正常进行项目 2：大货完结 3:延期项目 5：取消项目 6：样品完结 7：上周完结项目
    
    private String technician;    //技术员
    
    private String customerName;  //客户名


	//项目详情状态*(跟单进行更新)
    private Integer detailStatus;   //1：一切正常    2:重新打样 3：打样失败 4：反复沟通 5：样品没确认 6：样品已确认 7：其他问题
    private Integer scheduledDays;   //预计耗时
    private Date newPredictDate;     //最新交货日
    private Date urgentDeliveryDate; //紧急交货日
    private String factoryId;       //工厂id

	private String uploadDate;      //上传时间


	//虚拟字段
	private Boolean isUploadVideo;          //是否上传视频
	private Boolean isUploadMaterial;       //是否上传材质报告


	public Boolean getUploadVideo() {
		return isUploadVideo;
	}

	public void setUploadVideo(Boolean uploadVideo) {
		isUploadVideo = uploadVideo;
	}

	public Boolean getUploadMaterial() {
		return isUploadMaterial;
	}

	public void setUploadMaterial(Boolean uploadMaterial) {
		isUploadMaterial = uploadMaterial;
	}


	public String getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(String uploadDate) {
		this.uploadDate = uploadDate;
	}

	public String getProjectNo() {
		return projectNo;
	}
	public void setProjectNo(String projectNo) {
		this.projectNo = projectNo;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public Date getScheduledDate() {
		return scheduledDate;
	}
	public void setScheduledDate(Date scheduledDate) {
		this.scheduledDate = scheduledDate;
	}
	public Date getActualStartDate() {
		return actualStartDate;
	}
	public void setActualStartDate(Date actualStartDate) {
		this.actualStartDate = actualStartDate;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public int getFinish() {
		return finish;
	}
	public void setFinish(int finish) {
		this.finish = finish;
	}
	public Date getSampleScheduledDate() {
		return sampleScheduledDate;
	}
	public void setSampleScheduledDate(Date sampleScheduledDate) {
		this.sampleScheduledDate = sampleScheduledDate;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getPurchaseName() {
		return purchaseName;
	}
	public void setPurchaseName(String purchaseName) {
		this.purchaseName = purchaseName;
	}
	public String getSellName() {
		return sellName;
	}
	public void setSellName(String sellName) {
		this.sellName = sellName;
	}

	public String getZhijian1() {
		return zhijian1;
	}
	public void setZhijian1(String zhijian1) {
		this.zhijian1 = zhijian1;
	}
	public String getZhijian2() {
		return zhijian2;
	}
	public void setZhijian2(String zhijian2) {
		this.zhijian2 = zhijian2;
	}
	public String getZhijian3() {
		return zhijian3;
	}
	public void setZhijian3(String zhijian3) {
		this.zhijian3 = zhijian3;
	}
	public String getProjectAmount() {
		return projectAmount;
	}
	public void setProjectAmount(String projectAmount) {
		this.projectAmount = projectAmount;
	}
	public Date getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}
	public Date getSampleFinishTime() {
		return sampleFinishTime;
	}
	public void setSampleFinishTime(Date sampleFinishTime) {
		this.sampleFinishTime = sampleFinishTime;
	}
	public Date getDateSample() {
		return dateSample;
	}
	public void setDateSample(Date dateSample) {
		this.dateSample = dateSample;
	}
	public Date getCompletionTime() {
		return completionTime;
	}
	public void setCompletionTime(Date completionTime) {
		this.completionTime = completionTime;
	}
	public String getDateSampleUploading() {
		return dateSampleUploading;
	}
	public void setDateSampleUploading(String dateSampleUploading) {
		this.dateSampleUploading = dateSampleUploading;
	}
	public String getProductImg() {
		return productImg;
	}
	public void setProductImg(String productImg) {
		this.productImg = productImg;
	}
	public int getSampleFinish() {
		return sampleFinish;
	}
	public void setSampleFinish(int sampleFinish) {
		this.sampleFinish = sampleFinish;
	}
	public Integer getProjectStatus() {
		return projectStatus;
	}
	public void setProjectStatus(Integer projectStatus) {
		this.projectStatus = projectStatus;
	}
	public String getTechnician() {
		return technician;
	}
	public void setTechnician(String technician) {
		this.technician = technician;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public Integer getDetailStatus() {
		return detailStatus;
	}
	public void setDetailStatus(Integer detailStatus) {
		this.detailStatus = detailStatus;
	}
	public Integer getScheduledDays() {
		return scheduledDays;
	}
	public void setScheduledDays(Integer scheduledDays) {
		this.scheduledDays = scheduledDays;
	}
	public Date getNewPredictDate() {
		return newPredictDate;
	}
	public void setNewPredictDate(Date newPredictDate) {
		this.newPredictDate = newPredictDate;
	}
	public Date getUrgentDeliveryDate() {
		return urgentDeliveryDate;
	}
	public void setUrgentDeliveryDate(Date urgentDeliveryDate) {
		this.urgentDeliveryDate = urgentDeliveryDate;
	}
	public String getFactoryId() {
		return factoryId;
	}
	public void setFactoryId(String factoryId) {
		this.factoryId = factoryId;
	}

	@Override
	public String toString() {
		return "ProjectVO{" +
				"projectNo='" + projectNo + '\'' +
				", projectName='" + projectName + '\'' +
				", deliveryDate='" + deliveryDate + '\'' +
				", scheduledDate=" + scheduledDate +
				", actualStartDate=" + actualStartDate +
				", createDate=" + createDate +
				", finish=" + finish +
				", sampleScheduledDate=" + sampleScheduledDate +
				", companyName='" + companyName + '\'' +
				", purchaseName='" + purchaseName + '\'' +
				", sellName='" + sellName + '\'' +
				", zhijian1='" + zhijian1 + '\'' +
				", zhijian2='" + zhijian2 + '\'' +
				", zhijian3='" + zhijian3 + '\'' +
				", projectAmount='" + projectAmount + '\'' +
				", finishTime=" + finishTime +
				", sampleFinishTime=" + sampleFinishTime +
				", dateSample=" + dateSample +
				", completionTime=" + completionTime +
				", dateSampleUploading='" + dateSampleUploading + '\'' +
				", productImg='" + productImg + '\'' +
				", sampleFinish=" + sampleFinish +
				", projectStatus=" + projectStatus +
				", technician='" + technician + '\'' +
				", customerName='" + customerName + '\'' +
				", detailStatus=" + detailStatus +
				", scheduledDays=" + scheduledDays +
				", newPredictDate=" + newPredictDate +
				", urgentDeliveryDate=" + urgentDeliveryDate +
				", factoryId='" + factoryId + '\'' +
				", uploadDate='" + uploadDate + '\'' +
				'}';
	}


}