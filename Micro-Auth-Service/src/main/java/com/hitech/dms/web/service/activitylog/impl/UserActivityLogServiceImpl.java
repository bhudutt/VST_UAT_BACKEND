/**
 * 
 */
package com.hitech.dms.web.service.activitylog.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hitech.dms.web.dao.activitylog.UserActivityLogDao;
import com.hitech.dms.web.model.user.activitylog.request.UserActivityLogRequestModel;
import com.hitech.dms.web.model.user.activitylog.response.UserActivityLogResponseModel;
import com.hitech.dms.web.service.activitylog.UserActivityLogService;

/**
 * @author dinesh.jakhar
 *
 */
@Service
public class UserActivityLogServiceImpl implements UserActivityLogService {
	private static final Logger logger = LoggerFactory.getLogger(UserActivityLogServiceImpl.class);
	
	@Autowired
	private UserActivityLogDao activityLogDao;

	@Override
	public UserActivityLogResponseModel addActivityLog(String userCode, UserActivityLogRequestModel requestModel) {
		// TODO Auto-generated method stub
		return activityLogDao.addActivityLog(userCode, requestModel);
	}	
	
}
