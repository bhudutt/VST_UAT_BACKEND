package com.hitech.dms.web.dao.activitymaster.create;

import java.util.List;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.entity.activitymaster.ActivitySourceMasterEntity;
import com.hitech.dms.web.model.activitymaster.response.ActivitySourceCostTypeResponseModel;
import com.hitech.dms.web.model.activitymaster.response.ActivitySourceMasterListResponseModel;
import com.hitech.dms.web.model.activitymaster.response.ActivitySourceMasterResponseModel;

public interface ActivitySourceMasterDao {

	ActivitySourceMasterResponseModel createActivityMaster(String userCode, Device device,
			ActivitySourceMasterEntity requestModel);

	List<ActivitySourceCostTypeResponseModel> fetchActivityCostType(String userCode, Device device);

	ActivitySourceMasterResponseModel changeActiveStatus(String userCode, Integer activityId, Character isActive);

	List<ActivitySourceMasterListResponseModel> fetchActivitySourceMasterList(String userCode);

	List<ActivitySourceMasterResponseModel> fetchActivityNameList(String userCode, String searchFor, String activityName);

	

}
