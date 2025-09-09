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

@Data
@Entity
@Table(name = "SV_REPAIR_CATG")
public class SvRepairCategoryEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "repair_catg_id")
	private Integer repairCatgId;
	
	@Column(name = "RepairCatgCode")
	private String repairCatgCode;
	
	@Column(name = "RepairCatgDesc")
	private String repairCatgDesc;
	
	@Column(name = "CompletionHrs")
	private double completionHrs;
	
	@Column(name = "IsActive")
	@Type(type = "yes_no")
	private boolean isActive;
	
	@Column(name = "CreatedDate")
	private Date createdDate;
	
	@Column(name = "CreatedBy")
	private String createdBy;
	
	@Column(name = "ModifiedDate")
	private Date modifiedDate;
	
	@Column(name = "ModifiedBy")
	private String modifiedBy;
	
	public SvRepairCategoryEntity(){}
	
	public SvRepairCategoryEntity(Integer repairCatgId, String repairCatgDesc) {
		this.repairCatgId = repairCatgId;
		this.repairCatgDesc = repairCatgDesc;
	}
	
	public SvRepairCategoryEntity(Integer repairCatgId, String repairCatgDesc, String repairCatgCode) {
		this.repairCatgId = repairCatgId;
		this.repairCatgDesc = repairCatgDesc;
		this.repairCatgCode = repairCatgCode;
	}
}
