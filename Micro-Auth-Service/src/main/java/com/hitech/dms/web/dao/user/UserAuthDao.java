package com.hitech.dms.web.dao.user;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.hitech.dms.web.entity.response.user.UserRestEntity;
import com.hitech.dms.web.entity.user.UserEntity;
import com.hitech.dms.web.entity.user.UserTypeEntity;
import com.hitech.dms.web.model.user.User;

public interface UserAuthDao {
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
}
