/**
 * 
 */
package com.hitech.dms.web.model.activity.gstclaim.response;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class ActivityClaimInvApprovalResponseModel {
	private String msg;
	private Integer statusCode;
	private String approvalStatus;
}
