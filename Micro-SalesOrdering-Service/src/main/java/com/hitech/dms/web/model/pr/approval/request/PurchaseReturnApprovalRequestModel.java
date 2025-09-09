/**
 * 
 */
package com.hitech.dms.web.model.pr.approval.request;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class PurchaseReturnApprovalRequestModel {
	private BigInteger purchaseReturnId;
	private String purchaseReturnNumber;
	private String approvalStatus;
	private String remarks;
}
