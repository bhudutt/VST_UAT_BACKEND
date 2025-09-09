package com.hitech.dms.web.dao.activityplan.dtl;

import com.hitech.dms.web.model.activityplan.dtl.request.ActivityPlanDTLRequestModel;
import com.hitech.dms.web.model.activityplan.dtl.response.ActivityPlanDTLResponseModel;

public interface ActivityPlanDTLDao {
	public ActivityPlanDTLResponseModel fetchActivityPlanDTLList(String userCode, ActivityPlanDTLRequestModel activityPlanDTLRequestModel);
}
