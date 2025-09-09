package com.hitech.dms.web.dao.activityplan.search;

import com.hitech.dms.web.model.activityplan.search.request.ActivityPlanSearchRequestModel;
import com.hitech.dms.web.model.activityplan.search.response.ActivityPlanSearchResultResponseModel;

public interface ActivityPlanSearchDao {
	public ActivityPlanSearchResultResponseModel fetchActivityPlanList(String userCode,
			ActivityPlanSearchRequestModel activityPlanSearchRequestModel);
}
