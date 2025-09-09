package com.hitech.dms.web.dao.dealer.assign.branch.search;

import com.hitech.dms.web.model.dealer.branchassign.search.request.AssignUserToBranchSearchRequestModel;
import com.hitech.dms.web.model.dealer.branchassign.search.response.AssignUserToBranchSearchMainResponseModel;

public interface AssignUserToBranchSearchDao {
	public AssignUserToBranchSearchMainResponseModel searchAssignedUserBranchList(String userCode,
			AssignUserToBranchSearchRequestModel requestModel);
}
