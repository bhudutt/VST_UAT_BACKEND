package com.hitech.dms.web.entity.spare.inventorymanagement.stockadjustment;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * @author suraj.gaur
 */
@Getter
@Setter
@Entity
@Table(name = "SP_STOCK_ADJUSTMENT_APPROVAL")
public class SpareStockAdjustmentApproval {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
    private BigInteger id;
	
	@Column(name = "adjustment_id")
    private BigInteger adjustmentId;
    
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
    private Long hoUserId;
    
	@Column(name = "remark")
    private String remark;
	
	@Column(name = "reject_reason")
	private String rejectReason;
	
	@Column(name = "dealer_flag")
	private boolean dealerFlag;
	
}
