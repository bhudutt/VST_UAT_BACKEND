package com.hitech.dms.web.model.activityClaim.create.response;

import lombok.Getter;
import lombok.Setter;

/**
 * @author vinay.gautam
 *
 */
@Setter
@Getter
public class ActivityClaimApproveResponseModel {
	private Integer approverLevelSeq;
	private Integer designationLevelId;
	private Integer grpSeqNo;
	private Integer approvalStatus;
	private Integer isFinalApprovalStatus;
	

}
