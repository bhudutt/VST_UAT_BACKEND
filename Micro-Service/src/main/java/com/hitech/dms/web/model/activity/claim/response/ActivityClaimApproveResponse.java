/**
 * 
 */
package com.hitech.dms.web.model.activity.claim.response;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class ActivityClaimApproveResponse {
	private String msg;
	private Integer statusCode;
	private String approvalStatus;

}
