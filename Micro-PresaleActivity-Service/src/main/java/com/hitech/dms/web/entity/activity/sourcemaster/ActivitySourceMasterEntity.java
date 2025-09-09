package com.hitech.dms.web.entity.activity.sourcemaster;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
/**
 * @author Sunil.Singh
 *
 */
@Table(name = "SA_MST_ENQ_SOURCE_ACTIVITY")
@Entity
@Data
public class ActivitySourceMasterEntity implements Serializable {
	
	private static final long serialVersionUID = -6075989882964346585L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="Activity_ID")
	private Integer activityID;
	@Column(name="pc_id")
	private Integer profitCenter;
	@Column(name="ActivityType")
	private String activityType;
	@Column(name="ActivityCode")
	private String activityCode;
	@Column(name="Gl_Code")
	private String glCode;
	@Column(name="ActivityDesc")
	private String activityName;
	@Column(name="COST_PER_DAY")
	private BigDecimal activityCostPerDay;
	@Column(name="IsActive")
	private String active;
	@Column(name="CreatedDate")
	private Date createdDate =new Date();
	@Column(name="CreatedBy")
	private String createdBy;
	@Column(name="ModifiedDate")
	private Date modifiedDate;
	@Column(name="modifiedBy")
	private String modifiedBy;
	

}
