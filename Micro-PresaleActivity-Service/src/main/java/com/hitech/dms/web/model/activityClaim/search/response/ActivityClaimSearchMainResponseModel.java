package com.hitech.dms.web.model.activityClaim.search.response;

import java.util.List;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */
@Data
public class ActivityClaimSearchMainResponseModel {
	private List<ActivityClaimSearchListResponseModel> acSearch;
	private Integer recordCount;

}
