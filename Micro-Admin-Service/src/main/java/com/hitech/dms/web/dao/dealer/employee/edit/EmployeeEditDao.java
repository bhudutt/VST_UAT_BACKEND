package com.hitech.dms.web.dao.dealer.employee.edit;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.dealer.employee.create.request.DealerEmployeeHdrRequestModel;
import com.hitech.dms.web.model.dealer.employee.create.response.DealerEmployeeCreateResponseModel;

public interface EmployeeEditDao {
	public DealerEmployeeCreateResponseModel updateDealerEmployee(String userCode,
			DealerEmployeeHdrRequestModel requestModel, Device device);
}
