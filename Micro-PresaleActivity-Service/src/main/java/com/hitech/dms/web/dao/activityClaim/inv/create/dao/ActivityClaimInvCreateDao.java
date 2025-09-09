package com.hitech.dms.web.dao.activityClaim.inv.create.dao;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.activityClaim.invoice.create.request.ActivityClaimInvCreateRequestModel;
import com.hitech.dms.web.model.activityClaim.invoice.create.response.ActivityClaimInvCreateResponseModel;

public interface ActivityClaimInvCreateDao {
	public ActivityClaimInvCreateResponseModel createActivityClaimInvoice(String userCode,
			ActivityClaimInvCreateRequestModel requestModel, Device device);
}
