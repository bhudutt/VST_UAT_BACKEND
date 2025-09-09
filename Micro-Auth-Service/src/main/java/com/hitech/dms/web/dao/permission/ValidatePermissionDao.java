package com.hitech.dms.web.dao.permission;

public interface ValidatePermissionDao {
	public boolean validateMenuPermission(String userCode, String code);
}
