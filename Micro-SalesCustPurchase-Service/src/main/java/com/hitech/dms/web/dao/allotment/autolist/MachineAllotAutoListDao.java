package com.hitech.dms.web.dao.allotment.autolist;

import java.util.List;

import com.hitech.dms.web.model.allot.autolist.request.MachineEnqAllotAutoListRequestModel;
import com.hitech.dms.web.model.allot.autolist.response.MachineEnqAllotAutoListResponseModel;

public interface MachineAllotAutoListDao {
	public List<MachineEnqAllotAutoListResponseModel> fetchEnqListForAllot(String userCode,
			MachineEnqAllotAutoListRequestModel requestModel);
}
