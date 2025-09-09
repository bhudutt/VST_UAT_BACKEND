package com.hitech.dms.web.dao.activity.permission;

import java.util.Map;

import com.hitech.dms.web.model.activity.permission.request.ActivityPermissionRequestModel;

public interface ActivityPermissionCheckDao {
	public Map<String, Object> checkActivityPermissions(String userCode, ActivityPermissionRequestModel requestModel);
}
