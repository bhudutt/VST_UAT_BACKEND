package com.hitech.dms.web.dao.oldchassis.search;

import java.util.List;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.oldchassis.search.request.OldChassisSearchListRequestModel;
import com.hitech.dms.web.model.oldchassis.search.response.OldChassisSearchListResultResponseModel;
import com.hitech.dms.web.model.oldchassis.search.response.OldChassisValidateEVRListResponseModel;

public interface OldChassisSearchDao {

	OldChassisSearchListResultResponseModel OldChassisSearchList(String userCode,
			OldChassisSearchListRequestModel requestModel);

	List<OldChassisValidateEVRListResponseModel> fetchValidateEVRList(String userCode, Device device);

}
