package com.hitech.dms.web.dao.activityplan.approval;

import com.hitech.dms.web.model.activityplan.approval.request.ActivityPlanApprovalRequestModel;
import com.hitech.dms.web.model.activityplan.approval.response.ActivityPlanApprovalResponse;

public interface ActivityPlanApprovalDao {
	public ActivityPlanApprovalResponse approveRejectActivityPlan(String userCode,
			ActivityPlanApprovalRequestModel requestModel);
}
