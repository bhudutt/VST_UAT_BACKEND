package com.hitech.dms.web.dao.activityClaim.inv.approval.dao;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.activityClaim.invoice.approve.request.ActivityClaimInvApprovalRequestModel;
import com.hitech.dms.web.model.activityClaim.invoice.approve.response.ActivityClaimInvApprovalResponseModel;

public interface ActivityClaimInvApprovalDao {
	public ActivityClaimInvApprovalResponseModel approveRejectActivityClaimInvoice(String userCode,
			ActivityClaimInvApprovalRequestModel requestModel, Device device);
}
