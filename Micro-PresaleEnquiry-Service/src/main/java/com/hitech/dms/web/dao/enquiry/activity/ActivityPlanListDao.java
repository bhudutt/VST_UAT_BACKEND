package com.hitech.dms.web.dao.enquiry.activity;

import java.util.Date;
import java.util.List;

import com.hitech.dms.web.model.enquiry.activity.request.ActivityPlanListRequestModel;
import com.hitech.dms.web.model.enquiry.activity.response.ActivityPlanListResponseModel;

public interface ActivityPlanListDao {
	public List<ActivityPlanListResponseModel> fetchEnqActivityPlanList(String userCode, Integer pcId,
			Integer activityId, Long dealerId, Date fieldActivityDate);

	public List<ActivityPlanListResponseModel> fetchEnqActivityPlanList(String userCode,
			ActivityPlanListRequestModel planListRequestModel);
}
