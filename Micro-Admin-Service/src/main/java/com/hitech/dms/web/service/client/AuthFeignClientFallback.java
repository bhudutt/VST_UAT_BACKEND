package com.hitech.dms.web.service.client;


import org.springframework.stereotype.Component;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.web.model.user.accesslog.request.AccessLogUpdateRequestModel;
import com.hitech.dms.web.model.user.activitylog.request.UserActivityLogRequestModel;

@Component
public class AuthFeignClientFallback implements AuthServiceClient {

	@Override
	public HeaderResponse updateActivityLogs(String authorizationHeader, UserActivityLogRequestModel requestModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HeaderResponse updateAppAccessLog(String authorizationHeader, AccessLogUpdateRequestModel requestModel) {
		// TODO Auto-generated method stub
		return null;
	}

}
