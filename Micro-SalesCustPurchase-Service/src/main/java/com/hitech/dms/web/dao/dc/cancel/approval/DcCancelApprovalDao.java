package com.hitech.dms.web.dao.dc.cancel.approval;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.dc.cancel.approval.request.DcCancelApprovalRequestModel;
import com.hitech.dms.web.model.dc.cancel.approval.response.DcCancelApprovalResponseModel;

public interface DcCancelApprovalDao {
	public DcCancelApprovalResponseModel approveRejectDcCancel(String userCode,
			DcCancelApprovalRequestModel requestModel, Device device);
}
