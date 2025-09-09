package com.hitech.dms.web.dao.activity.create;

import com.hitech.dms.web.model.activity.create.request.ActualActivityRequestModel;
import com.hitech.dms.web.model.activity.create.response.ActualActivityResponeModel;

public interface ActualActivityCreateDao {
	public ActualActivityResponeModel createActualActivity(String userCode, ActualActivityRequestModel requestModel);
}
