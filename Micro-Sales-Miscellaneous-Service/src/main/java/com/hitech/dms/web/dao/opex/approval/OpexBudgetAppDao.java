package com.hitech.dms.web.dao.opex.approval;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.opex.approval.request.OpexBudgetAppRequestModel;
import com.hitech.dms.web.model.opex.approval.response.OpexBudgetAppResponseModel;

public interface OpexBudgetAppDao {
	public OpexBudgetAppResponseModel approveRejectOpexBudget(String userCode, OpexBudgetAppRequestModel requestModel, Device device);
}
