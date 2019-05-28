package com.cbt.entity.DingBean;

import java.io.Serializable;
import java.util.Date;

public class DingTalkMileStone implements Serializable {
    private Integer id;

    private String dingtalkid;   //钉钉个人唯一id

    private String milestoneName;  //里程碑名

    private Date milestoneDate;    //里程碑日期

    private String projectNo;      //项目号

    private Long nextCursor;   //钉钉下次查询pageNum

    private String processInstanceId;//审批id


    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDingtalkid() {
        return dingtalkid;
    }

    public void setDingtalkid(String dingtalkid) {
        this.dingtalkid = dingtalkid == null ? null : dingtalkid.trim();
    }

    public String getMilestoneName() {
        return milestoneName;
    }

    public void setMilestoneName(String milestoneName) {
        this.milestoneName = milestoneName == null ? null : milestoneName.trim();
    }

    public Date getMilestoneDate() {
        return milestoneDate;
    }

    public void setMilestoneDate(Date milestoneDate) {
        this.milestoneDate = milestoneDate;
    }

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo == null ? null : projectNo.trim();
    }

    public Long getNextCursor() {
        return nextCursor;
    }

    public void setNextCursor(Long nextCursor) {
        this.nextCursor = nextCursor;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    @Override
    public String toString() {
        return "DingTalkMileStone{" +
                "id=" + id +
                ", dingtalkid='" + dingtalkid + '\'' +
                ", milestoneName='" + milestoneName + '\'' +
                ", milestoneDate=" + milestoneDate +
                ", projectNo='" + projectNo + '\'' +
                ", nextCursor=" + nextCursor +
                ", processInstanceId='" + processInstanceId + '\'' +
                '}';
    }
}