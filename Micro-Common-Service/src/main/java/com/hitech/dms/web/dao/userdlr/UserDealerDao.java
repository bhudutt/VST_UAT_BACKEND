package com.hitech.dms.web.dao.userdlr;

import java.util.List;

import com.hitech.dms.web.model.userdlr.request.UserDealerRequestModel;
import com.hitech.dms.web.model.userdlr.response.UserDealerResponseModel;

public interface UserDealerDao {
	public List<UserDealerResponseModel> fetchUserDealerList(String userCode, String includeInactive, String isFor);

	public List<UserDealerResponseModel> fetchUserDealerList(String userCode, UserDealerRequestModel requestModel);
}
