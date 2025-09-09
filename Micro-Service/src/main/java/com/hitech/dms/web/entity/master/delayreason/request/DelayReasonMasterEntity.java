package com.hitech.dms.web.entity.master.delayreason.request;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hitech.dms.web.entity.master.storemaster.request.StoreMasterEntity;

import lombok.Data;

@Data
@Entity
@Table(name = "SV_REASON_TYPE")
public class DelayReasonMasterEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "delay_reason_id")
	private Integer delayReasonId;
	
	@Column(name = "delay_reason_code")
	private String delayReasonCode;
	
	@Column(name = "delay_reason_desc")
	private String delayReasonDescription;
	
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
}
