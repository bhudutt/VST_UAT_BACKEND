/**
 * 
 */
package com.hitech.dms.web.model.dc.cancel.approval.response;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class DcCancelApprovalResponseModel {
	private String msg;
	private Integer statusCode;
	private String approvalStatus;
}
