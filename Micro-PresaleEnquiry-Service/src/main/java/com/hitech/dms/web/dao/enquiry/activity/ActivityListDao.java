package com.hitech.dms.web.dao.enquiry.activity;

import java.util.List;

import com.hitech.dms.web.model.enquiry.activity.request.ActivityListRequestModel;
import com.hitech.dms.web.model.enquiry.activity.response.ActivityListResponseModel;

public interface ActivityListDao {
	public List<ActivityListResponseModel> fetchEnqActivityList(String userCode, Integer pcId);

	public List<ActivityListResponseModel> fetchEnqActivityList(String userCode,
			ActivityListRequestModel activityListRequestModel);
}
