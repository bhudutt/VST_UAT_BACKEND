/**
 * 
 */
package com.hitech.dms.web.model.inv.inv.view.response;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
@ToString
public class InvCancelAppViewResponseModel {
	private BigInteger invCancelApprovalId;
	private BigInteger invCancelRequestId;
	private String cancelRequestDate;
	private String invCancelDate;
	private String cancelRemark;
	private String cancelReasonDesc;
	private String employeeName;
	private String desginationDesc;
	private String approvalStaus;
	private String approvedDate;
}
