package com.hitech.dms.web.model.activityClaim.create.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author vinay.gautam
 *
 */
@Setter
@Getter
public class ActivityClaimHdrDtlResponseModel {
	private ActivityClaimHdrResponseModel actHdr;
	private List<ActivityClaimPlanResponseModel> actDtl;

}
