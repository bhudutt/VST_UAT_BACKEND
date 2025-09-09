
package com.hitech.dms.web.entity.master.servicesourcemaster.request;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import lombok.Data;

@Table(name = "SV_OEM_SRV_TYPE")

@Entity

@Data
public class OEMServiceTypeEntity {

	@Id

	@Column(name = "Service_Type_oem_ID")
	private int serviceTypeOemId;

	@Column(name = "Service_Category_ID")
	private int serviceCategoryId;

	@Column(name = "ServiceGroup")
	private String serviceGroup;

	@Column(name = "SrvTypeCode")
	private String srvTypeCode;

	@Column(name = "SrvTypeDesc")
	private String svTypeDesc;

	@Column(name = "IsFreeService")

	@Type(type = "yes_no")
	private Boolean freeServiceStatus;

	@Column(name = "DisplayOrder")
	private int displayOrder;

	@Column(name = "ServiceOrder")
	private int serviceOrder;

	@Column(name = "IncludeInHistory")

	@Type(type = "yes_no")
	private Boolean includeInHistory;

	@Column(name = "GenerateReminder")

	@Type(type = "yes_no")
	private Boolean generateReminder;

	@Column(name = "IsProductDefect")

	@Type(type = "yes_no")
	private Boolean productDefectStatus;

	@Column(name = "IsExtendedFreeService")

	@Type(type = "yes_no")
	private Boolean extendedFreeServiceStatus;

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
	private String serviceCategory;

	@Transient
	private Integer serviceTypeId;

	private transient String srvCategoryCode;
}
