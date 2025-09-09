/**
 * 
 */
package com.hitech.dms.web.model.activityplan.approval.response;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class ActivityPlanApprovalResponse {
	private String msg;
	private Integer statusCode;
	private String approvalStatus;
}
