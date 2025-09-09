package com.hitech.dms.web.entity.activitymaster;

import java.io.Serializable;
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
@Table(name = "SV_MST_ACTIVITY_SOURCE")
public class ActivitySourceMasterEntity implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
	@Column(name="Activity_ID")
	private Integer activityID;
	@Column(name="ActivityType")
	private String activityType;
	@Column(name="ProfitCenter")
	private Integer profitCenter;
	@Column(name="GlCode")
	private String glCode;
	@Column(name="ActivitySourceName")
	private String activityName;
	@Column(name="ActivityCostPerDay")
	private String activityCostPerDay;
	@Column(name="IsActive")
	private String active;
	@Column(name="CreatedDate")
	private Date createdDate =new Date();
}
