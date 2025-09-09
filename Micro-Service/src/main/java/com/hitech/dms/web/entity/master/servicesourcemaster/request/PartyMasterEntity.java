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
@Table(name = "CM_PARTY_MASTER")
public class PartyMasterEntity {

	@Id
	@Column(name = "Party_Master_Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer partyMasterId;

	@Column(name = "PartyName")
	private String partyName;

	@Column(name = "Party_Category_Id")
	private Integer partyCategoryId;
	
	@Column(name = "Remarks")
	private String remarks;

	@Column(name = "IsActive")
	@Type(type = "yes_no")
	private boolean activeStatus;

	@Column(name = "CreatedDate")
	private Date createdDate;

	@Column(name = "CreatedBy")
	private String createdBy;

	@Column(name = "ModifiedDate")
	private Date modifiedDate;

	@Column(name = "ModifiedBy")
	private String modifiedBy;

	@Column(name = "MIBL_FLAG")
	private boolean miblFlag;
	
	public PartyMasterEntity() {

	}

	public PartyMasterEntity(Integer partyMasterId, String partyName) {
		super();
		this.partyMasterId = partyMasterId;
		this.partyName = partyName;
	}
}
