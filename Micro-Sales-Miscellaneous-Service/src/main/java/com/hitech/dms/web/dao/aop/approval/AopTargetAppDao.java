package com.hitech.dms.web.dao.aop.approval;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.aop.approval.request.AopTargetAppRequestModel;
import com.hitech.dms.web.model.aop.approval.response.AopTargetAppResponseModel;

public interface AopTargetAppDao {
	public AopTargetAppResponseModel approveRejectAopTarget(String userCode, AopTargetAppRequestModel requestModel, Device device);
}
