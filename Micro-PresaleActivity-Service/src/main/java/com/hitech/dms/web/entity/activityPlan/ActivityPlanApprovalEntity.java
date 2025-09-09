/**
 * 
 */
package com.hitech.dms.web.entity.activityPlan;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Table(name = "SA_ACT_PLAN_APPROVAL")
@Entity
@Data
public class ActivityPlanApprovalEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "activity_plan_approval_id")
	private BigInteger activityPlanAppId;
	
	@Column(name = "activity_plan_hdr_id")
	private BigInteger activityPlanHdrId;

	@Column(name = "approver_level_seq")
	private Integer appLevelSeq;
	
	@Column(name = "designation_level_id")
	private Integer designationLevelId;
	
	@Column(name = "grp_seq_no")
	private Integer grpSeqNo;
	
	@Column(name = "approval_status")
	private String approvalStatus;
	
	@Column(name = "isFinalApprovalStatus")
	@Type(type = "yes_no")
	private Boolean isFinalApprovalStatus;
	
	@Column(name = "rejected_flag")
	@Type(type = "yes_no")
	private Boolean rejectedFlag;
	
	@Column(name = "approved_date")
	private Date approvedDate;
	
	@Column(name = "remark")
	private String remark;
	
	@Column(name = "ho_user_id")
	private BigInteger hoUserId;
}
