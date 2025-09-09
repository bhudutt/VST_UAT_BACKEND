/**
 * 
 */
package com.hitech.dms.web.service.permission.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hitech.dms.web.dao.permission.ValidatePermissionDao;
import com.hitech.dms.web.service.permission.ValidatePermissionService;

/**
 * @author dinesh.jakhar
 *
 */
@Service
public class ValidatePermissionServiceImpl implements ValidatePermissionService {
	
	@Autowired
	private ValidatePermissionDao validatePermissionDao;

	@Override
	public boolean validateMenuPermission(String userCode, String code) {
		// TODO Auto-generated method stub
		return validatePermissionDao.validateMenuPermission(userCode, code);
	}
}
