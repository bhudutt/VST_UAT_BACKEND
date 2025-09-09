package com.hitech.dms.web.dao.pr.approval;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.pr.approval.request.PurchaseReturnApprovalRequestModel;
import com.hitech.dms.web.model.pr.approval.response.PurchaseReturnApprovalResponseModel;

public interface PurchaseReturnApprovalDao {
	public PurchaseReturnApprovalResponseModel approveRejectPurchaseReturn(String userCode,
			PurchaseReturnApprovalRequestModel requestModel, Device device);
}
