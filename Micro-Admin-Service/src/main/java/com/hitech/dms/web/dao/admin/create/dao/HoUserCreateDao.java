package com.hitech.dms.web.dao.admin.create.dao;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.admin.create.request.HoUserCreateRequestModel;
import com.hitech.dms.web.model.admin.create.response.HoUserCreateResponseModel;

public interface HoUserCreateDao {
	public HoUserCreateResponseModel createUpdateHoUser(String userCode, HoUserCreateRequestModel requestModel,
			Device device);

	public HoUserCreateResponseModel updateOneTimePasswordForAllUsers(String userCode,
			HoUserCreateRequestModel requestModel, Device device);
	
	
	
	
}

