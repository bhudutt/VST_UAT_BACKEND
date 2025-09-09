package com.hitech.dms.web.service.user.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hitech.dms.web.dao.user.UserAuthDao;
import com.hitech.dms.web.dao.user.UserDao;
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
import com.hitech.dms.web.service.user.UserService;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDao userDao;
	@Autowired
	private UserAuthDao userAuthDao;

	@Override
	public List<UserMenu> fetchUserMenu(String userName) {
		// TODO Auto-generated method stub
		return userDao.fetchUserMenu(userName);
	}

	@Override
	public UserActivityLogResponseModel insertIntoAccessLog(String userCode, AccessLogRequestModel requestModel) {
		// TODO Auto-generated method stub
		return userDao.insertIntoAccessLog(userCode, requestModel);
	}

	@Override
	public UserActivityLogResponseModel updateAccessLog(String userCode, AccessLogUpdateRequestModel requestModel) {
		// TODO Auto-generated method stub
		return userDao.updateAccessLog(userCode, requestModel);
	}

	@Override
	public List<MenuURLCodeEntity> fetchUserMenuURLMasterList(String userCode) {
		// TODO Auto-generated method stub
		return userDao.fetchUserMenuURLMasterList(userCode);
	}

	@Override
	public List<MenuURLCodeEntity> fetchMenuURLMasterList(String userCode) {
		// TODO Auto-generated method stub
		return userDao.fetchMenuURLMasterList(userCode);
	}

	@Override
	public List<UserMenu> fetchPermissionsForUser(String userCode, String pmodule) {
		// TODO Auto-generated method stub
		return userDao.fetchPermissionsForUser(userCode, pmodule);
	}

	@Override
	public Boolean updateLastLoginDate(String userCode, String fromWhere) {
		// TODO Auto-generated method stub
		return userDao.updateLastLoginDate(userCode, fromWhere);
	}

	@Override
	public UserEntity findByUser(String userName) {
		// TODO Auto-generated method stub
		return userAuthDao.findByUser(userName);
	}

	@Override
	public UserRestEntity findByUserName(String userName) {
		// TODO Auto-generated method stub
		return userAuthDao.findByUserName(userName);
	}

	@Override
	public Map<String, String> updateUserProfile(User user) {
		// TODO Auto-generated method stub
		return userAuthDao.updateUserProfile(user);
	}

	@Override
	public Map<String, String> validateCurrentPassword(User user) {
		// TODO Auto-generated method stub
		return userAuthDao.validateCurrentPassword(user);
	}

	@Override
	public Map<String, String> resetPassword(User user) {
		// TODO Auto-generated method stub
		return userAuthDao.resetPassword(user);
	}

	@Override
	public Map<String, String> changePassword(User user) {
		// TODO Auto-generated method stub
		return userAuthDao.changePassword(user);
	}

	@Override
	public List<UserTypeEntity> userTypeList() {
		// TODO Auto-generated method stub
		return userAuthDao.userTypeList();
	}

	@Override
	public Map<String, Object> validateOTPNumber(User authenticationRequest) {
		// TODO Auto-generated method stub
		return userAuthDao.validateOTPNumber(authenticationRequest);
	}

	@Override
	public Map<String, Object> fetchUserDTL(String userCode, Integer userId) {
		// TODO Auto-generated method stub
		return userAuthDao.fetchUserDTL(userCode, userId);
	}

	@Override
	public Map<String, Object> fetchUserDTL(Session session, String userCode, Integer userId) {
		// TODO Auto-generated method stub
		return userAuthDao.fetchUserDTL(session, userCode, userId);
	}

	@Override
	public List<AppMenuModel> getUserMenu(String userName) {
		// TODO Auto-generated method stub
		return userDao.getUserMenu(userName);
	}

	@Override
	public Map<String, Object> validateUserByUserCode(User user) {
		// TODO Auto-generated method stub
		return userAuthDao.validateUserByUserCode(user);
	}

	@Override
	public List<RoleDtlModel> fetchUserRoleList(String userCode) {
		// TODO Auto-generated method stub
		return userDao.fetchUserRoleList(userCode);
	}

}
