package com.hitech.dms.web.service.activitylog;

import com.hitech.dms.web.model.user.activitylog.request.UserActivityLogRequestModel;
import com.hitech.dms.web.model.user.activitylog.response.UserActivityLogResponseModel;

public interface UserActivityLogService {
	public UserActivityLogResponseModel addActivityLog(String userCode, UserActivityLogRequestModel requestModel);
}
