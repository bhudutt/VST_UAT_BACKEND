package com.hitech.dms.web.entity.master.servicesourcemaster.request;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.Data;

@Entity
@Data
@Table(name = "SV_SRV_CATEGORY")
public class ServiceCategoryEntity {

	@Id
	@Column(name = "Service_Category_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer serviceCategoryID;
	@Column(name = "CategoryCode")
	private String categoryCode;
	@Column(name = "CategoryDesc")
	private String categoryDesc;
	@Column(name = "DisplayOrder")
	private Integer displayOrder;
	@Column(name = "CreatedDate")
	private Date createdDate;
	@Column(name = "CreatedBy")
	private String createdBy;
	@Column(name = "ModifiedDate")
	private Date modifiedDate;
	@Column(name = "ModifiedBy")
	private String modifiedBy;
	@Column(name = "IsActive")
	@Type(type = "yes_no")
	private Boolean activeStatus;
	@Column(name = "IsRCMandatory")
	@Type(type = "yes_no")
	private Boolean isRCMandatory;
	/*
	 * @Column(name = "IsForMTB")
	 * 
	 * @Type(type = "yes_no")
	 */
	private transient Boolean isForMTB;
	@Column(name = "InAppointment")
	@Type(type = "yes_no")
	private Boolean inAppointment;
	@Column(name = "InQuotation")
	@Type(type = "yes_no")
	private Boolean inQuotation;

	@Column(name = "InRO")
	@Type(type = "yes_no")
	private Boolean inRO;

	@Column(name = "BusinessType_ID")
	private Integer busineessTypeID;
	
	public ServiceCategoryEntity() {
	}
	
	public ServiceCategoryEntity(Integer serviceCategoryID, String categoryDesc, String categoryCode,
			Integer displayOrder, Boolean activeStatus) {
		super();
		this.serviceCategoryID = serviceCategoryID;
		this.categoryDesc = categoryDesc;
		this.categoryCode = categoryCode;
		this.displayOrder = displayOrder;
		this.activeStatus = activeStatus;
	}
	
	public ServiceCategoryEntity(Integer serviceCategoryID, String categoryDesc, String categoryCode,
			Integer displayOrder, Boolean activeStatus, Boolean inAppointment) {
		super();
		this.serviceCategoryID = serviceCategoryID;
		this.categoryDesc = categoryDesc;
		this.categoryCode = categoryCode;
		this.displayOrder = displayOrder;
		this.activeStatus = activeStatus;
		this.inAppointment = inAppointment;
	}

	public ServiceCategoryEntity(Integer serviceCategoryID, String categoryDesc, String categoryCode,
			Boolean activeStatus, Boolean inQuotation) {
		super();
		this.serviceCategoryID = serviceCategoryID;
		this.categoryDesc = categoryDesc;
		this.categoryCode = categoryCode;
		this.activeStatus = activeStatus;
		this.inQuotation = inQuotation;
	}

	public ServiceCategoryEntity(Integer serviceCategoryID, String categoryDesc) {

	}

	public ServiceCategoryEntity(Integer serviceCategoryID, String categoryDesc, String categoryCode) {

		this.serviceCategoryID = serviceCategoryID;
		this.categoryDesc = categoryDesc;
		this.categoryCode = categoryCode;
	}

	public ServiceCategoryEntity(Integer serviceCategoryID, String categoryDesc, String categoryCode,
			Boolean isRCMandatory) {
		this.serviceCategoryID = serviceCategoryID;
		this.categoryDesc = categoryDesc;
		this.categoryCode = categoryCode;
		this.isRCMandatory = isRCMandatory;
	}

	public ServiceCategoryEntity(Integer serviceCategoryID, Boolean isForMTB, String categoryDesc, String categoryCode,
			Boolean isRCMandatory) {
		this.serviceCategoryID = serviceCategoryID;
		this.categoryDesc = categoryDesc;
		this.categoryCode = categoryCode;
		this.isRCMandatory = isRCMandatory;
		this.isForMTB = isForMTB;
	}
}
