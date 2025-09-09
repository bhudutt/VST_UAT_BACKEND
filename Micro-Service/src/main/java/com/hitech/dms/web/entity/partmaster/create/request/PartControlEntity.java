package com.hitech.dms.web.entity.partmaster.create.request;

import java.math.BigDecimal;
import java.math.BigInteger;
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

@Table(name = "PA_CONTROL_CODE")
@Entity
@Data
public class PartControlEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Control_code_id")
	private Integer controlCodeId;

	@Column(name = "Control_code")
	private String controlCode;
	
	@Column(name = "taxcharge_oem_id")
	private Integer taxTypeId;
	
	@Transient
	private String taxTypeDesc;
	
	@Column(name = "Tax_Rate")
	private BigDecimal taxRate;
	private transient BigInteger sgstRate;
	private transient BigInteger cgstRate;	
	private transient BigInteger igstRate;
	
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
