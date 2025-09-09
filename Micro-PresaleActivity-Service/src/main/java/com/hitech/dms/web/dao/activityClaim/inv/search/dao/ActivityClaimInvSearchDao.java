package com.hitech.dms.web.dao.activityClaim.inv.search.dao;

import java.util.List;

import com.hitech.dms.web.model.activityClaim.invoice.search.request.ActivityClaimAutoSearchRequestModel;
import com.hitech.dms.web.model.activityClaim.invoice.search.request.ActivityClaimInvSearchRequest;
import com.hitech.dms.web.model.activityClaim.invoice.search.response.ActivityClaimAutoSearchResponseModel;
import com.hitech.dms.web.model.activityClaim.invoice.search.response.ActivityClaimInvSearchResponseModel;

public interface ActivityClaimInvSearchDao {
	public List<ActivityClaimInvSearchResponseModel> fetchActivityClaimInvSearchList(String userCode,
			ActivityClaimInvSearchRequest requestModel);
	public List<ActivityClaimAutoSearchResponseModel> fetchActivityClaimInvAutoSearchList(String userCode,
			ActivityClaimAutoSearchRequestModel requestModel);
}
