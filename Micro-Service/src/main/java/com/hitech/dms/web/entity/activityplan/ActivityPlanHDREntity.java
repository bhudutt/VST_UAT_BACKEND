package com.hitech.dms.web.entity.activityplan;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="SV_Activity_Plan")
@Data
public class ActivityPlanHDREntity implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ActivityPlanId")
	private Integer activityPlanId;
	@Column(name="ActivityPlan")
	private String activityPlanNo;
	@Column(name="ActivityPlanstatus")
	private String activityPlanstatus;
	@Column(name="ActivityPlanDate")
	private Date activityPlanDate;
	@Column(name="CreatedDate")
	private Date createdDate =new Date();
	@Column(name="CreatedBy")
	private BigInteger createdBy;
	@Column(name="ModifiedBy")
	private Integer modifiedBy;
	@Column(name="ModifiedDate")
	private Date modifiedDate;
	@Column(name="Branch_id")
	private BigInteger branchId;
	
}
