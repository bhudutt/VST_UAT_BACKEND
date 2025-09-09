package com.hitech.dms.web.dao.allotment.search;

import com.hitech.dms.web.model.allot.search.request.MachineAllotSearchRequestModel;
import com.hitech.dms.web.model.allot.search.response.MachineAllotSearchMainResponseModel;

public interface MachineAllotSearchDao {
	public MachineAllotSearchMainResponseModel searchAllotList(String userCode,
			MachineAllotSearchRequestModel requestModel);
}
