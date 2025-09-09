package com.hitech.dms.web.dao.machinepo.create;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.machinepo.create.request.MachinePOCreateRequestModel;
import com.hitech.dms.web.model.machinepo.create.response.MachinePOCreateResponseModel;

public interface MachinePOCreateDao {
	public MachinePOCreateResponseModel createMachinePO(String authorizationHeader, String userCode,
			MachinePOCreateRequestModel requestModel, Device device);
}
