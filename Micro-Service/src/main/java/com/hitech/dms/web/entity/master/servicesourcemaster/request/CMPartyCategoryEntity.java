package com.hitech.dms.web.entity.master.servicesourcemaster.request;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "CM_PARTY_CTGRY")
public class CMPartyCategoryEntity {

	@Id
	@Column(name = "party_category_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer partyCategoryId;

	@Column(name = "PartyCategoryCode")
	private String partyCategoryCode;

	@Column(name = "PartyCategoryName")
	private String partyCategoryName;

	@Column(name = "WhoCanCreateParty")
	private String whoCanCreateParty;

	@Column(name = "IsPredefined")
	@Type(type = "yes_no")
	private Boolean predefinedStatus;

	@Column(name = "CanDlrCreateNew")
	@Type(type = "yes_no")
	private Boolean canDlrCreateNew;

	@Column(name = "CanDlrCreateParty")
	@Type(type = "yes_no")
	private Boolean canDlrCreateParty;

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
	
	private transient List<PartyMasterEntity> partyNameList;

	private transient BigDecimal requiredHoPoMinAmount;
	private transient BigDecimal requiredPoAmount;
	
	public CMPartyCategoryEntity(Integer partyCategoryId, String partyCategoryName) {
		this.partyCategoryId = partyCategoryId;
		this.partyCategoryName = partyCategoryName;
	}

	public CMPartyCategoryEntity(Integer partyCategoryId, String partyCategoryCode, String partyCategoryName) {
		this.partyCategoryId = partyCategoryId;
		this.partyCategoryCode = partyCategoryCode;
		this.partyCategoryName = partyCategoryName;
	}

	public CMPartyCategoryEntity(Integer partyCategoryId, String partyCategoryName, Boolean canDlrCreateNew,
			Boolean canDlrCreateParty) {
		this.partyCategoryId = partyCategoryId;
		this.partyCategoryName = partyCategoryName;
		this.canDlrCreateNew = canDlrCreateNew;
		this.canDlrCreateParty = canDlrCreateParty;
	}

	public CMPartyCategoryEntity(Integer partyCategoryId, String partyCategoryCode, String partyCategoryName,
			Boolean canDlrCreateNew, Boolean canDlrCreateParty) {
		this.partyCategoryId = partyCategoryId;
		this.partyCategoryName = partyCategoryName;
		this.partyCategoryCode = partyCategoryCode;
		this.canDlrCreateNew = canDlrCreateNew;
		this.canDlrCreateParty = canDlrCreateParty;
	}

	
	public CMPartyCategoryEntity() {
	}
}
