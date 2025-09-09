package com.hitech.dms.web.dao.userbranch;

import java.util.List;

import com.hitech.dms.web.model.userbranch.request.UserBranchRequestModel;
import com.hitech.dms.web.model.userbranch.response.UserBranchResponseModel;

public interface UserBranchDao {
	public List<UserBranchResponseModel> fetchUserBranchList(String userCode, String isInactiveInclude);
	public List<UserBranchResponseModel> fetchUserBranchList(UserBranchRequestModel branchRequestModel);
	public List<UserBranchResponseModel> brancheByUserCode(String userCode, String isInactiveInclude);
}
