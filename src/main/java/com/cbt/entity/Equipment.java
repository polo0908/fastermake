package com.cbt.entity;

import java.io.Serializable;
import java.util.Date;

public class Equipment implements Serializable {
    private Integer id;

    private String factoryId;
    /**
     * 设备名
     */
    private String equipmentName;
    /**
     * 设备英文名
     */
    private String equipmentNameEn;
    /**
     * 设备类型
     */
    private String equipmentModel;

    private String equipmentModelEn;

    private Integer number;

    private String parameter;

    private String type;

    private String typeEn;

    private Date createDate;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public String getEquipmentNameEn() {
        return equipmentNameEn;
    }

    public void setEquipmentNameEn(String equipmentNameEn) {
        this.equipmentNameEn = equipmentNameEn;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFactoryId() {
		return factoryId;
	}

	public void setFactoryId(String factoryId) {
		this.factoryId = factoryId;
	}

	public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName == null ? null : equipmentName.trim();
    }

    public String getEquipmentModel() {
        return equipmentModel;
    }

    public void setEquipmentModel(String equipmentModel) {
        this.equipmentModel = equipmentModel == null ? null : equipmentModel.trim();
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter == null ? null : parameter.trim();
    }

    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getEquipmentModelEn() {
        return equipmentModelEn;
    }

    public void setEquipmentModelEn(String equipmentModelEn) {
        this.equipmentModelEn = equipmentModelEn;
    }

    public String getTypeEn() {
        return typeEn;
    }

    public void setTypeEn(String typeEn) {
        this.typeEn = typeEn;
    }

    @Override
    public String toString() {
        return "Equipment{" +
                "id=" + id +
                ", factoryId='" + factoryId + '\'' +
                ", equipmentName='" + equipmentName + '\'' +
                ", equipmentNameEn='" + equipmentNameEn + '\'' +
                ", equipmentModel='" + equipmentModel + '\'' +
                ", equipmentModelEn='" + equipmentModelEn + '\'' +
                ", number=" + number +
                ", parameter='" + parameter + '\'' +
                ", type='" + type + '\'' +
                ", typeEn='" + typeEn + '\'' +
                ", createDate=" + createDate +
                '}';
    }
}