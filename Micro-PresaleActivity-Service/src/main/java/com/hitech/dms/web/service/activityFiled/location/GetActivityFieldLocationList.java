package com.hitech.dms.web.service.activityFiled.location;

import java.util.List;

import com.hitech.dms.web.model.activity.create.response.PoPlantResponseModel;

public interface GetActivityFieldLocationList {

	
	public  List<PoPlantResponseModel> getAllLocationList(String userCode, Integer dealerId);
}
