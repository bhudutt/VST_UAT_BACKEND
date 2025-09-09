package com.hitech.dms.web.entity.targetSetting;


import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


import lombok.Data;

@Data
@Entity
@Table(name = "PA_TARGET_SETTING_HDR")
public class TargetSettingHdrEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="target_hdr_id")
	private BigInteger targetHdrId;

//	@Column(name="branch_id")
//	private BigInteger branchId;

	@Column(name="version_no")
	private BigInteger versionNumber;
	
	@Column(name="target_number")
	private String targetNumber;

	@Column(name="target_date")
	private Date targetDate;

	@Column(name="target_for")
	private String targetFor;

	@Column(name="product_category")
	private String productCategory;

	@Column(name="outlet_category")
	private String outletCategory;

	@Column(name="total_target")
	private Double totalTarget;

	@Column(name="updated_date")
	private Date updatedDate;

	@Column(name="created_date")
	private Date createdDate;

	@Column(name="created_by")
	private String createdBy;

}
