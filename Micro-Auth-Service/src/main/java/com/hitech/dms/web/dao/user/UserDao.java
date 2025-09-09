package com.hitech.dms.web.dao.user;

import java.util.List;

import com.hitech.dms.web.entity.user.MenuURLCodeEntity;
import com.hitech.dms.web.model.user.AppMenuModel;
import com.hitech.dms.web.model.user.RoleDtlModel;
import com.hitech.dms.web.model.user.UserMenu;
import com.hitech.dms.web.model.user.accesslog.request.AccessLogRequestModel;
import com.hitech.dms.web.model.user.accesslog.request.AccessLogUpdateRequestModel;
import com.hitech.dms.web.model.user.activitylog.response.UserActivityLogResponseModel;

public interface UserDao {

	public List<UserMenu> fetchUserMenu(String userName);

	public UserActivityLogResponseModel insertIntoAccessLog(String userCode, AccessLogRequestModel requestModel);

	public UserActivityLogResponseModel updateAccessLog(String userCode, AccessLogUpdateRequestModel requestModel);

	public List<MenuURLCodeEntity> fetchUserMenuURLMasterList(String userCode);

	public List<MenuURLCodeEntity> fetchMenuURLMasterList(String userCode);

	public List<UserMenu> fetchPermissionsForUser(String userCode, String pmodule);

	public Boolean updateLastLoginDate(String userCode, String fromWhere);

	public List<AppMenuModel> getUserMenu(String userName);
	
	public List<RoleDtlModel> fetchUserRoleList(String userCode);
}
