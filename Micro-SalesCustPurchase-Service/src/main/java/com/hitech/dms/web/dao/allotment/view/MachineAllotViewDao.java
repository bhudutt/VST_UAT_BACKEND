package com.hitech.dms.web.dao.allotment.view;

import com.hitech.dms.web.model.allot.view.request.MachineAllotViewRequestModel;
import com.hitech.dms.web.model.allot.view.response.MachineAllotViewResponseModel;

public interface MachineAllotViewDao {
	public MachineAllotViewResponseModel fetchMachineAllotDtl(String userCode,
			MachineAllotViewRequestModel requestModel);
}
