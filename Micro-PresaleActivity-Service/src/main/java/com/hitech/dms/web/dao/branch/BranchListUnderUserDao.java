package com.hitech.dms.web.dao.branch;

import java.util.List;

import com.hitech.dms.web.model.branch.UserBranchListModel;

public interface BranchListUnderUserDao {
	public List<UserBranchListModel> fetchUserBranchList(String authorizationHeader, String isInactiveInclude);
}
