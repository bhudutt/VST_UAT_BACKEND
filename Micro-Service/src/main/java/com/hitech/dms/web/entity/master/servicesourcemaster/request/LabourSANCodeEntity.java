package com.hitech.dms.web.entity.master.servicesourcemaster.request;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import lombok.Data;

@Table(name = "LBR_SAN_Code")
@Entity
@Data
public class LabourSANCodeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SAN_Id")
	private Integer sanId;

	@Column(name = "SANCode")
	private String sanCode;
	
	@Column(name = "taxcharge_oem_id")
	private Integer taxTypeId;
	
	@Transient
	private String taxTypeDesc;
	
	@Column(name = "Tax_Rate")
	private BigDecimal taxRate;
	
	@Column(name = "IsActive")
	@Type(type= "yes_no")
	private Boolean isActive;
	
	@Column(name = "IsSync")
	@Type(type= "yes_no")
	private Boolean isSync;
	
	@Column(name = "Created_Date")
	private Date createdDate;
	
	@Column(name = "Created_By")
	private String createdBy;
	
	@Column(name = "Modified_Date")
	private Date modifiedDate;
	
	@Column(name = "Modified_by")
	private String modifiedBy;
}
