/**
 * 
 */
package com.hitech.dms.web.model.dc.cancel.approval.request;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class DcCancelApprovalRequestModel {
	private BigInteger dcId;
	private BigInteger dcCancelRequestId;
	private String dcNumber;
	private String approvalStatus;
	private String remarks;
}
