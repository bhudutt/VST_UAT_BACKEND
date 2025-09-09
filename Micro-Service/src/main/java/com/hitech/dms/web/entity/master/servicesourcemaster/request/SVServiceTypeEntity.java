
package com.hitech.dms.web.entity.master.servicesourcemaster.request;

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

@Table(name = "SV_SRV_TYPE")

@Entity

@Data
public class SVServiceTypeEntity {

	@Id

	@Column(name = "Service_Type_ID")

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer serviceTypeId;

	@Column(name = "Service_Type_oem_Id")
	private Integer serviceTypeOemId;

	@Column(name = "Model_Family_Id")
	private Integer modelFamilyId;

	@Column(name = "IsActive")

	@Type(type = "yes_no")
	private Boolean activeStatus;

	@Column(name = "CreatedDate")
	private Date createdDate;

	@Column(name = "CreatedBy")
	private String createdBy;

	@Column(name = "ModifiedDate")
	private Date modfiedDate;

	@Column(name = "ModifiedBy")
	private String modifiedBy;

	@Transient
	private String serviceGroup;

	@Transient
	private String svTypeDesc;

	@Transient
	private String categoryDesc;

	@Transient
	private Integer serviceCategoryId;

	@Transient
	private Boolean haveToUpdateOrSaveCheck;
}
