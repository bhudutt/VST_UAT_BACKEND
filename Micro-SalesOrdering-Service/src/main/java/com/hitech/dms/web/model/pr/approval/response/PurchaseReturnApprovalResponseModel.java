/**
 * 
 */
package com.hitech.dms.web.model.pr.approval.response;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class PurchaseReturnApprovalResponseModel {
	private String msg;
	private Integer statusCode;
	private String approvalStatus;
}
