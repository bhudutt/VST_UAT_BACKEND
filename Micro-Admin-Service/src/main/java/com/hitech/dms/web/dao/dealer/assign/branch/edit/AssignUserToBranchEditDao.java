package com.hitech.dms.web.dao.dealer.assign.branch.edit;

import com.hitech.dms.web.model.dealer.branchassign.dtl.request.AssignUserToBranchFormModel;
import com.hitech.dms.web.model.dealer.branchassign.dtl.response.AssignUserToBranchResponseModel;

public interface AssignUserToBranchEditDao {
	public AssignUserToBranchResponseModel editAssignBranchesToUser(String userCode,
			AssignUserToBranchFormModel requestModel);
}
