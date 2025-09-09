package com.hitech.dms.web.dao.activity.create;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.entity.activity.create.request.ActivityRequestEntity;
import com.hitech.dms.web.model.activity.create.request.ActivityRequestDtlModel;
import com.hitech.dms.web.model.activity.create.request.ActivityRequestModel;
import com.hitech.dms.web.model.activity.create.response.ActivityResponseModel;
import com.hitech.dms.web.model.activity.create.response.ActivityViewAfterSubmitResponseModel;
import com.hitech.dms.web.model.activity.create.response.ActivityViewResponseModel;
import com.hitech.dms.web.model.activity.create.response.ServiceActivitySearchListResultResponse;

public interface ActivityCreateDao {

	public List<ActivityResponseModel> activityPlanAuto(String userCode,ActivityRequestModel activityPlanNoAutoRequestModel);
	public ActivityResponseModel fetchActivityPlanDetailsById(String userCode,Integer activityPlanHdrId);
	public List<ActivityRequestDtlModel> fetchJobcardDetailsByActivityId(String userCode,BigInteger activityPlanHdrId);
	public ActivityResponseModel createServiceActivity(String authorizationHeader,String userCode,ActivityRequestEntity requestModel);
	public ServiceActivitySearchListResultResponse viewSubmitActivity(String userCode,ActivityRequestModel activityPlanNoAutoRequestModel);
	/**
	 * @param userCode
	 * @param searchText
	 * @return
	 */
	public Map<String, Object> getJobCardNumberList(String userCode, String searchText,String planNoId);
	List<ActivityViewAfterSubmitResponseModel> fetchActivityServiceViewList(String userCode, Device device, Integer activityId);
}
