package com.hitech.dms.web.entity.activity.create.request;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;


import lombok.Data;

@Data
@Entity
@Table(name="SV_Activity_HDR")
public class ActivityRequestEntity {

	@Id
	@Column(name = "ActivityId")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer activityId;
	
	@Column(name = "ActivityPlanId")
	private Integer activityPlanId;
	
	@Column(name = "ActivityNumber", updatable = false)
	private String activityNumber;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ActivityDate", updatable = false)
	private java.util.Date activityDate;
	
	@Column(name = "pcId")
	private Integer pcId;
	
	@Column(name = "ActivityNameId")
	private Integer activityNameId;
	
	@Column(name = "activityLocation")
	private String activityLocation;
	
	@Column(name = "actualActivityFromDate")
	private String actualActivityFromDate;
	
	@Column(name = "actualActivityToDate")
	private String actualActivityToDate;
	
	@Column(name = "totalAmount")
	private BigDecimal totalAmount;
	
	@Column(name = "branchId")
	private Integer branchId;
	
	
	@Column(name = "CreatedDate", updatable = false)
	private Date createdDate;

	@Column(name = "CreatedBy", updatable = false)
	private String createdBy;

	@Column(name = "ModifiedDate")
	private Date modifiedDate;

	@Column(name = "ModifiedBy")
	private String modifiedBy;
	
	
	@OneToMany( mappedBy = "activityEntity")
	@LazyCollection(LazyCollectionOption.FALSE)
	@Cascade(CascadeType.ALL)
	private List<ActivityRequestDtlEntity> detailRequest;
	
	
	
}
