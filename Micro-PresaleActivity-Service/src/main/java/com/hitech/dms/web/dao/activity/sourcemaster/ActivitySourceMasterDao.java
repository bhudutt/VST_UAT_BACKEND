package com.hitech.dms.web.dao.activity.sourcemaster;

import java.util.List;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.entity.activity.sourcemaster.ActivitySourceMasterEntity;
import com.hitech.dms.web.model.activity.sourcemaster.response.ActivityGlCodeListModel;
import com.hitech.dms.web.model.activity.sourcemaster.response.ActivitySourceMasterListResponseModel;
import com.hitech.dms.web.model.activity.sourcemaster.response.ActivitySourceMasterResponseModel;



public interface ActivitySourceMasterDao {

	
	public ActivitySourceMasterResponseModel createActivitySourceMaster(String userCode,
			ActivitySourceMasterEntity activitySourceMasterEntity, Device device);
	public List<ActivitySourceMasterListResponseModel> fetchActivitySourceMasterList(String userCode, Integer Activity_ID);
	public List<ActivityGlCodeListModel> fetchActivityGLCodeList(String userCode, Object object1, int pcId);
	public ActivitySourceMasterResponseModel changeActiveStatus(String userCode, Integer activityId,
			Character isActive);
	public List<ActivitySourceMasterResponseModel> fetchActivityNameList(String userCode, String searchFor,
			String activityName);
}
