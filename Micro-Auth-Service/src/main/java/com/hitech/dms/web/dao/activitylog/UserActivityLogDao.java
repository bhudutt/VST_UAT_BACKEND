package com.hitech.dms.web.dao.activitylog;

import com.hitech.dms.web.model.user.activitylog.request.UserActivityLogRequestModel;
import com.hitech.dms.web.model.user.activitylog.response.UserActivityLogResponseModel;

public interface UserActivityLogDao {
	public UserActivityLogResponseModel addActivityLog(String userCode, UserActivityLogRequestModel requestModel);
}
