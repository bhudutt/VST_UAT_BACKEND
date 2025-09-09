package com.hitech.dms.web.model.partybybranch.create.request;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="SP_PARTY_APPROVAL")
public class PartyApprovalEntity {
	
	
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
    private BigInteger id;
	@Column(name = "party_id")
    private BigInteger party_id;
	@Column(name = "approver_level_seq")
    private Integer approverLevelSeq;
	@Column(name = "designation_level_id")
    private Integer designationLevelId;
	@Column(name = "grp_seq_no")
    private Integer grpSeqNo;
	@Column(name = "approval_status")
    private String approvalStatus;
	@Column(name = "isFinalApprovalStatus")
    private Character isfinalapprovalstatus;
	@Column(name = "rejected_flag")
    private Character rejectedFlag;
	@Column(name = "approved_date")
    private Date approvedDate;
	@Column(name = "ho_user_id")
    private BigInteger hoUserId;
	@Column(name = "remark")
    private String remark;
	@Column(name = "reject_reason")
    private String rejectReason;

}
