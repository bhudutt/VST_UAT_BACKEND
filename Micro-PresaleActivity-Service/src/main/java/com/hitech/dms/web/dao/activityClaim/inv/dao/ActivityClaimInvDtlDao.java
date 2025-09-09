package com.hitech.dms.web.dao.activityClaim.inv.dao;

import com.hitech.dms.web.model.activityClaim.invoice.request.ActivityClaimInvRequestModel;
import com.hitech.dms.web.model.activityClaim.invoice.response.ActivityClaimInvHdrResponseModel;

public interface ActivityClaimInvDtlDao {
	public ActivityClaimInvHdrResponseModel fetchActivityClaimInvDTL(String userCode,
			ActivityClaimInvRequestModel requestModel);
}
