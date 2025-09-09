package com.hitech.dms.web.entity.wcr;

import java.math.BigInteger;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name= "SV_WA_WCR_APPROVAL")
public class WarrantyWcrApproval {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)	
	@Column(name = "id")
    private Long id;

	@Column(name = "warranty_wcr_id")
    private Long warrantyWcrId;
    
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
