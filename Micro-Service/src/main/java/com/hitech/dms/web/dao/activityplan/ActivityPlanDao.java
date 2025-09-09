package com.hitech.dms.web.dao.activityplan;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.activity.create.request.ActivityPlanApprovalRequestModel;
import com.hitech.dms.web.model.activitymaster.response.ActivityNumberSearchResponseModel;
import com.hitech.dms.web.model.activityplan.request.ActivityPlanEditRequestModel;
import com.hitech.dms.web.model.activityplan.request.ActivityPlanRequestModel;
import com.hitech.dms.web.model.activityplan.request.ActivityPlanSearchRequestModel;
import com.hitech.dms.web.model.activityplan.response.ActivityPlanApprovalResponse;
import com.hitech.dms.web.model.activityplan.response.ActivityPlanResponseModel;
import com.hitech.dms.web.model.activityplan.response.ActivityPlanSearchResultResponseModel;
import com.hitech.dms.web.model.activityplan.response.ActivityPlanServiceTypeListResponseModel;
import com.hitech.dms.web.model.activityplan.response.ActivityPlanStateDealerWiseListResponseModel;
import com.hitech.dms.web.model.activityplan.response.ActivityPlanStateListResponseModel;
import com.hitech.dms.web.model.activityplan.response.ActivityPlanStatusResponseModel;
import com.hitech.dms.web.model.activityplan.response.ActivityPlanViewResponseModel;

public interface ActivityPlanDao {
	
	ActivityPlanResponseModel createActivityPlan(String userCode, Device device, ActivityPlanRequestModel requestModel);
	
	public Map<String, Object> fetchUserDTLByUserCode(Session session, String userCode);
	
	List<ActivityPlanStateListResponseModel> fetchStateList(String userCode, Device device);

	List<ActivityPlanStateDealerWiseListResponseModel> fetchStateDealerWiseList(String userCode, Device device,
			Integer stateId, String dealerCode);

	List<ActivityPlanServiceTypeListResponseModel> fetchServiceActivityTypeList(String userCode, Device device);

	ActivityPlanSearchResultResponseModel fetchActivityPlanSearchList(String userCode, Device device,
			ActivityPlanSearchRequestModel requestModel);

	List<ActivityPlanViewResponseModel> fetchActivityPlanViewList(String userCode, Device device, Integer activityId);

	List<ActivityPlanStatusResponseModel> fetchActivityPlanStatusList(String userCode, Device device);

	ActivityPlanResponseModel ActivityPlanStatusUpdate(String userCode, Device device, ActivityPlanApprovalRequestModel requestModel);

	List<ActivityNumberSearchResponseModel> fetchActivityPlanNumberBySearchList(String userCode, Device device,
			String activityPlanNo);

	List<ActivityPlanStatusResponseModel> fetchActivityPlanStatusListId(String userCode, Device device);

	ActivityPlanApprovalResponse approveRejectActivityPlan(String userCode,
			com.hitech.dms.web.model.activityplan.request.ActivityPlanApprovalRequestModel requestModel);

	ActivityPlanResponseModel ActivityPlanEdit(String authorizationHeader, String userCode,
			ActivityPlanEditRequestModel requestModel, Device device);

	

	

}
