package com.hitech.dms.web.dao.machinepo.approval;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.machinepo.approval.request.MachinePoApprovalRequestModel;
import com.hitech.dms.web.model.machinepo.approval.response.MachinePoApprovalResponseModel;

public interface MachinePoApprovalDao {
	public MachinePoApprovalResponseModel approveRejectMachinePO(String userCode,
			MachinePoApprovalRequestModel requestModel, Device device);
}
