package com.hitech.dms.web.entity.master.servicesourcemaster.request;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="SV_LABOUR_RATE")
@Data
public class ServiceLabourRateEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="labour_rate_id")
	private Integer labourRateId;
	@Column(name="model_family_id")
	private Integer modelFamilyId;
	@Column(name="branch_id")
	private Integer branchId;
	@Column(name="CustRatePerHour")
	private Double custRatePerHour;
	@Column(name="RecommendedCustRatePerHour")
	private Double recommendCustRatePerHour;
	@Column(name="ValidFrom")
	private Date validFrom;
	@Column(name="ValidTo")
	private Date validTo;
	@Column(name="IsActive")
	@org.hibernate.annotations.Type(type="yes_no")
	private Boolean isActive;
	@Column(name="CreatedDate")
	private Date createdDate;
	@Column(name="CreatedBy")
	private String createdBy;
	@Column(name="ModifiedDate")
	private Date  modifiedDate;
	@Column(name="ModifiedBy")
	private String modifiedBy;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="Labour_Id", nullable=false)
	private ServiceLabourMasterEntity labourMaster;
}
