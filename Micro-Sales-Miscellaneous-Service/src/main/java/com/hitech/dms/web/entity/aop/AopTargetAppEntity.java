/**
 * 
 */
package com.hitech.dms.web.entity.aop;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Entity
@Table(name = "SA_MIS_AOP_TARGET_APPROVAL", uniqueConstraints = { @UniqueConstraint(columnNames = "aop_approval_id") })
@Data
public class AopTargetAppEntity implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = -8719045350406414597L;
	@Id
	@Column(name = "aop_approval_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger aopApprovalId;

	@Column(name = "aop_id")
	private BigInteger aopId;

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
}
