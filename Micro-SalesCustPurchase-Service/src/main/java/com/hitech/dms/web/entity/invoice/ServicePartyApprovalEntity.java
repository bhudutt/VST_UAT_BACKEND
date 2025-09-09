package com.hitech.dms.web.entity.invoice;

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
import javax.persistence.UniqueConstraint;

import lombok.Data;

@Entity
@Table(name = "SP_PARTY_APPROVAL")
@Data
public class ServicePartyApprovalEntity  implements Serializable{

	private static final long serialVersionUID = 926870347841281722L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
