package com.hitech.dms.web.dao.activityClaim.search;

import com.hitech.dms.web.model.activityClaim.search.request.ActivityClaimSearchRequestModel;
import com.hitech.dms.web.model.activityClaim.search.response.ActivityClaimSearchListResponseModel;
import com.hitech.dms.web.model.activityClaim.search.response.ActivityClaimSearchMainResponseModel;

/**
 * @author vinay.gautam
 *
 */
public interface ActivityClaimSearchDao {
	public ActivityClaimSearchMainResponseModel fetchActivityClaimSearchList(String userCode, ActivityClaimSearchRequestModel acRequestModel);

}
