package com.hitech.dms.web.dao.machinepo.calculation;

import java.util.List;

import com.hitech.dms.web.model.machinepo.calculation.request.MachinePOItemAmntRequestModel;
import com.hitech.dms.web.model.machinepo.calculation.request.MachinePOTotalAmntRequestModel;
import com.hitech.dms.web.model.machinepo.calculation.response.MachinePOItemAmntResponseModel;
import com.hitech.dms.web.model.machinepo.calculation.response.MachinePOTotalAmntResponseModel;

public interface MachinePOCalculationDao {
	public List<MachinePOItemAmntResponseModel> calculateMachineItemAmount(String userCode,
			MachinePOItemAmntRequestModel requestModel);
	public List<MachinePOTotalAmntResponseModel> calculateMachinePOTotalAmount(String userCode,
			MachinePOTotalAmntRequestModel requestModel);
}
