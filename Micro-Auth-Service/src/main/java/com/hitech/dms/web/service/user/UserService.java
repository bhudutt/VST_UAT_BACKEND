package com.hitech.dms.web.service.user;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.hitech.dms.web.entity.response.user.UserRestEntity;
import com.hitech.dms.web.entity.user.MenuURLCodeEntity;
import com.hitech.dms.web.entity.user.UserEntity;
import com.hitech.dms.web.entity.user.UserTypeEntity;
import com.hitech.dms.web.model.user.AppMenuModel;
import com.hitech.dms.web.model.user.RoleDtlModel;
import com.hitech.dms.web.model.user.User;
import com.hitech.dms.web.model.user.UserMenu;
import com.hitech.dms.web.model.user.accesslog.request.AccessLogRequestModel;
import com.hitech.dms.web.model.user.accesslog.request.AccessLogUpdateRequestModel;
import com.hitech.dms.web.model.user.activitylog.response.UserActivityLogResponseModel;

public interface UserService {
	public List<UserMenu> fetchUserMenu(String userName);
	public UserActivityLogResponseModel insertIntoAccessLog(String userCode, AccessLogRequestModel requestModel);

	public UserActivityLogResponseModel updateAccessLog(String userCode, AccessLogUpdateRequestModel requestModel);
	public List<MenuURLCodeEntity> fetchUserMenuURLMasterList(String userCode);
	public List<MenuURLCodeEntity> fetchMenuURLMasterList(String userCode);
	public List<UserMenu> fetchPermissionsForUser(String userCode, String pmodule);
	public Boolean updateLastLoginDate(String userCode, String fromWhere);
	
	public UserEntity findByUser(String userName);
	public UserRestEntity findByUserName(String userName);
	public Map<String, String> updateUserProfile(User user);
	public Map<String, String> validateCurrentPassword(User user);
	public Map<String, String> resetPassword(User user);
	public Map<String, String> changePassword(User user);
	public List<UserTypeEntity> userTypeList();
	public Map<String, Object> validateOTPNumber(User authenticationRequest);
	public Map<String, Object> fetchUserDTL(String userCode, Integer userId);
	public Map<String, Object> fetchUserDTL(Session session, String userCode, Integer userId);
	public Map<String, Object> validateUserByUserCode(User user);
	
	public List<AppMenuModel> getUserMenu(String userName);
	
	public List<RoleDtlModel> fetchUserRoleList(String userCode);
}
