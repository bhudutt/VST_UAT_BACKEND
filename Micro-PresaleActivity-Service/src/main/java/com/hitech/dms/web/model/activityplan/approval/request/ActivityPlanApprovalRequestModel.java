/**
 * 
 */
package com.hitech.dms.web.model.activityplan.approval.request;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class ActivityPlanApprovalRequestModel {
	private BigInteger activityPlanHdrId;
	private String approvalStatus;
	private String remarks;
}
