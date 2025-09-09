package com.hitech.dms.web.model.activityClaim.approval.response;

import java.util.List;

import com.hitech.dms.web.model.activityClaim.create.response.ActivityClaimHdrResponseModel;

import lombok.Getter;
import lombok.Setter;

/**
 * @author vinay.gautam
 *
 */
@Setter
@Getter
public class ActivityClaimHdrDtlViewResponseModel {
	private ActivityClaimHdrResponseModel actHdr;
	private List<ActivityClaimDtlListResponseModel> actDtl;
}
