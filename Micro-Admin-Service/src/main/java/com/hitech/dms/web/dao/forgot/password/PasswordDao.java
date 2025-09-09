package com.hitech.dms.web.dao.forgot.password;

import com.hitech.dms.web.model.forgot.password.request.ChangePasswordRequestModel;
import com.hitech.dms.web.model.forgot.password.response.ChangePasswordResponseModel;

public interface PasswordDao {
	public ChangePasswordResponseModel changePassword(ChangePasswordRequestModel requestModel);
}
