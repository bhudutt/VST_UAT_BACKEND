package com.hitech.dms.web.dao.allotment.deallot;

import com.hitech.dms.web.model.allot.deallot.request.MachineDeAllotRequestModel;
import com.hitech.dms.web.model.allot.deallot.response.MachineDeAllotResponseModel;

public interface MachineDeAllotDao {
	public MachineDeAllotResponseModel deAllotMachine(String authorizationHeader, String userCode,
			MachineDeAllotRequestModel requestModel);
}
