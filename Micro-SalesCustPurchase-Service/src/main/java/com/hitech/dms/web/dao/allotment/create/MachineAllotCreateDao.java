package com.hitech.dms.web.dao.allotment.create;

import com.hitech.dms.web.model.allot.create.request.MachineAllotCreateRequestModel;
import com.hitech.dms.web.model.allot.create.response.MachineAllotCreateResponseModel;

public interface MachineAllotCreateDao {
	public MachineAllotCreateResponseModel create(String authorizationHeader, String userCode,
			MachineAllotCreateRequestModel requestModel);
}
