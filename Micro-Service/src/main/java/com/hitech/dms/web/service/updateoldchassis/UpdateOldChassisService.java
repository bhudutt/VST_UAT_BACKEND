package com.hitech.dms.web.service.updateoldchassis;

import com.hitech.dms.app.utils.ApiResponse;
import com.hitech.dms.web.model.updateoldchassis.UpdateOldChassisRequest;

public interface UpdateOldChassisService {
	
	ApiResponse<?> autoSearchChassisNo(String chassisNo);
	
	ApiResponse<?>  fetchVinAndCustDetails(String chassisNo);
	
	ApiResponse<?> updateOldChassis(String userCode, UpdateOldChassisRequest requestModel);

}
