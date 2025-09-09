package com.hitech.dms.web.dao.activity.source.form.dao;

import com.hitech.dms.web.model.activity.source.form.request.ActivityFormRequestModel;
import com.hitech.dms.web.model.activity.source.form.request.SourceFormRequestModel;
import com.hitech.dms.web.model.activity.source.form.response.ActivitySourceFormResponseModel;

public interface ActivitySourceFormDao {
	public ActivitySourceFormResponseModel saveActivity(String userCode, ActivityFormRequestModel requestModel);
	public ActivitySourceFormResponseModel saveSource(String userCode, SourceFormRequestModel requestModel);
}
