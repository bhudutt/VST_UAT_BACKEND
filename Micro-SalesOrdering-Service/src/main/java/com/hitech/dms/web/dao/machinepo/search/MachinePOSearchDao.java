package com.hitech.dms.web.dao.machinepo.search;

import com.hitech.dms.web.model.machinepo.search.request.MachinePOSearchRequestModel;
import com.hitech.dms.web.model.machinepo.search.response.MachinePOSearchListResponseModel;

public interface MachinePOSearchDao {
	public MachinePOSearchListResponseModel fetchMachinePOSearchList(String userCode, MachinePOSearchRequestModel requestModel);
}
