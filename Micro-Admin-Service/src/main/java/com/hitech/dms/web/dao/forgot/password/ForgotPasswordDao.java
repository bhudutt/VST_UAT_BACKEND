package com.hitech.dms.web.dao.forgot.password;

import com.hitech.dms.web.model.forgot.password.request.ForgotPasswordRequestModel;
import com.hitech.dms.web.model.forgot.password.response.ForgotPasswordResponseModel;

public interface ForgotPasswordDao {
	public ForgotPasswordResponseModel resetPassword(ForgotPasswordRequestModel requestModel);
}
