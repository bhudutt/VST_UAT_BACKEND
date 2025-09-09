package com.hitech.dms.web.dao.dealer.assign.branch;

import com.hitech.dms.web.model.dealer.branchassign.dtl.request.AssignUserToBranchFormModel;
import com.hitech.dms.web.model.dealer.branchassign.dtl.response.AssignUserToBranchResponseModel;

public interface AssignUserToBranchDao {
	public AssignUserToBranchResponseModel assignBranchesToUser(String userCode,
			AssignUserToBranchFormModel requestModel);
}
