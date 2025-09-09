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
@Table(name = "SV_LABOUR_GRP_MST")
@Data
public class LabourGroupMasterEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Labour_Group_Id")
	private Integer labourGroupId;

	@Column(name = "LabourGroupCode")
	private String labourgroupcode;

	@Column(name = "LabourGroupDesc")
	private String labourGroupDesc;

	@Column(name = "IsActive")
	@Type(type = "yes_no")
	private Boolean activeStatus;

	@Column(name = "CreatedDate")
	private Date createdDate;

	@Column(name = "CreatedBy")
	private String createdBy;

	@Column(name = "ModifiedDate")
	private Date modifiedDate;

	@Column(name = "ModifiedBy")
	private String modifiedBy;
}
