package com.hitech.dms.web.dao.log.activity;

import java.util.Map;

import com.hitech.dms.web.model.log.activity.list.request.ActivityLogListRequestModel;
import com.hitech.dms.web.model.log.activity.list.response.ActivityLogListMainResponseModel;

public interface ActivityLogDao {
	public ActivityLogListMainResponseModel fetchAcivityLogExport(String userCode,
			ActivityLogListRequestModel requestModel);
	
	public Map<String, Object> exportAcivityLog(String userCode,
			ActivityLogListRequestModel requestModel);
}
