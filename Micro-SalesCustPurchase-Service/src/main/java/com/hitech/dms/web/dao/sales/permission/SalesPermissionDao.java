package com.hitech.dms.web.dao.sales.permission;

import java.util.Map;

import com.hitech.dms.web.model.sales.permission.request.SalesPermissionRequestModel;

public interface SalesPermissionDao {
	Map<String, Object> checkSalesPermissions(String userCode, SalesPermissionRequestModel requestModel);
}
