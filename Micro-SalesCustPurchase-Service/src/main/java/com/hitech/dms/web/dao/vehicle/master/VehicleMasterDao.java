package com.hitech.dms.web.dao.vehicle.master;

import java.util.List;

import com.hitech.dms.web.model.vehicle.master.VehicleMasterDetailsModel;
import com.hitech.dms.web.model.vehicle.master.VehicleMasterRequestModel;
import com.hitech.dms.web.model.vehicle.master.VehicleMasterSearchListResultResponseModel;

public interface VehicleMasterDao {

	VehicleMasterDetailsModel vehicleMasterDTL(String userCode, VehicleMasterRequestModel requestModel);

	VehicleMasterSearchListResultResponseModel VehicleMasterSearchList(String userCode,
			VehicleMasterRequestModel requestModel);

	
}
