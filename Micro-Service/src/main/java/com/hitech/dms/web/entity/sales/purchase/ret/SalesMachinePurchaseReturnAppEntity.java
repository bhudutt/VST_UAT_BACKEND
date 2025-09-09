/**
 * 
 */
package com.hitech.dms.web.entity.sales.purchase.ret;

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

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Entity
@Table(name = "SA_MACHINE_PURCH_RET_APPROVAL")
@Data
public class SalesMachinePurchaseReturnAppEntity implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 7777397950582293691L;
	@Id
	@Column(name = "purchase_return_approval_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger purchaseReturnAppId;

	@JoinColumn(name = "purchase_return_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private SalesMachinePurchaseReturnEntity salesMachinePurchaseReturn;

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
