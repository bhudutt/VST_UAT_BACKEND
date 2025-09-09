/**
 * 
 */
package com.hitech.dms.web.model.pr.view.response;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class PurchaseReturnViewAppResponseModel {
	private BigInteger purchaseReturnAppId;
	private Integer approverLevelSeq;
	private Integer designationLevelId;
	private Integer grpSeqNo;
	private String approvalStatus;
	private char isFinalApprovalStatus;
	private char rejectedFlag;
	private String approvedDate;
	private BigInteger hoUserId;
	private String hoUser;
	private String remarks;
	private BigDecimal approvedAmount;
}
