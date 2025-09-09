package com.hitech.dms.web.entity.master.storemaster.request;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
@Entity
@Table(name = "PA_BRANCH_STORE")
public class StoreMasterEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "branch_store_id")
	private Integer branchStoreId;
	
	@Column(name = "branch_id")
	private Integer branchId;

	@Column(name = "StoreCode")
	private String storeCode;

	@Column(name = "StoreDesc")
	private String storeDescription;

	@Column(name = "IsBlockedForTransaction")
	@Type(type = "yes_no")
	private boolean blockedForTransaction;

	@Column(name = "IsActive")
	@Type(type = "yes_no")
	private boolean activeStatus;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	@Column(name = "CreatedDate", updatable = false)
	private Date createdDate;

	@Column(name = "CreatedBy", updatable = false)
	private String createdBy;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	@Column(name = "ModifiedDate")
	private Date modifiedDate;

	@Column(name = "ModifiedBy")
	private String modifiedBy;

	@Column(name = "isMainStore")
	@Type(type = "yes_no")
	private Boolean mainStoreStatus;
}
