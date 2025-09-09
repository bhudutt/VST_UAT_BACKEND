package com.hitech.dms.web.entity.baymaster;

import java.io.Serializable;
import java.math.BigInteger;
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
@Table(name = "BAY_MASTER")
public class BayMasterEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "bay_master_id")
	private Integer bayMasterId;
	
	@Column(name = "bay_code")
	private String bayCode;

	@Column(name = "dealer_id")
	private Integer dealerId;
	
	@Column(name = "bay_type")
	private Integer bayType;

	@Column(name = "bay_desc")
	private String bayDescription;

	@Column(name = "is_active")
	@Type(type = "yes_no")
	private boolean isActive;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	@Column(name = "created_date", updatable = false)
	private Date createdDate;

	@Column(name = "created_by", updatable = false)
	private BigInteger createdBy;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	@Column(name = "modified_date")
	private Date modifiedDate;

	@Column(name = "modified_by")
	private BigInteger modifiedBy;
}
