/**
 * 
 */
package com.hitech.dms.web.model.inv.cancel.approval.request;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class InvCancelApprovalRequestModel {
	private BigInteger salesInvoiceHdrId;
	private BigInteger invCancelRequestId;
	private String invoiceNumber;
	private String approvalStatus;
	private String remarks;
}
