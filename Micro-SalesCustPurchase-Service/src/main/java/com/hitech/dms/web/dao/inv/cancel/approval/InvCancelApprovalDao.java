package com.hitech.dms.web.dao.inv.cancel.approval;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.inv.cancel.approval.request.InvCancelApprovalRequestModel;
import com.hitech.dms.web.model.inv.cancel.approval.response.InvCancelApprovalResponseModel;

public interface InvCancelApprovalDao {
	public InvCancelApprovalResponseModel approveRejectInvCancel(String userCode,
			InvCancelApprovalRequestModel requestModel, Device device);
}
