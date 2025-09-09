package com.hitech.dms.web.entity.activityClaim;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */

@Table(name = "SA_ACT_CLAIM_APPROVAL")
@Entity
@Data
public class ActivityClaimApprovalEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1704296315041906868L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "activity_claim_approval_id")
	private BigInteger activityClaimApprovalId;
	
	
//	@JsonProperty(value = "activityClaimHdrId", required = true)
//	@NotNull(message = "Activity claim Hdr Id")
//	@Column(name = "activity_claim_hdr_id")
//	private BigInteger activityClaimHdrId;
//	
	
	@JoinColumn(name = "activity_claim_hdr_id")
    @ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	private ActivityClaimHdrEntity acHdr;
	
	@Column(name = "approver_level_seq")
	private Integer approverLevelSeq;
	

	@Column(name = "designation_level_id")
	private Integer designationLevelId;
	

	@Column(name = "grp_seq_no")
	private Integer grpSeqNo;

	@Column(name = "approval_status")
	private String approvalStatus;
	

	@Column(name = "isFinalApprovalStatus")
	private char isFinalApprovalStatus;

	@Column(name = "rejected_flag")
	private char rejectedFlag;
	
	
	@JsonProperty(value = "approvedDate", required = true)
	@Column(name = "approved_date")
	private Date approvedDate;
	
	@Column(name = "ho_user_id")
	private BigInteger hoUserId;
	
	@Column(name = "remarks")
	private String remarks;

	@Column(name = "approved_amount")
	private BigDecimal approvedAmount;
}
