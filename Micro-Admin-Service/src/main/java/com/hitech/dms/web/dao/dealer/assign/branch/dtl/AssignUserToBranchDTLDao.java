/**
 * 
 */
package com.hitech.dms.web.dao.dealer.assign.branch.dtl;

import com.hitech.dms.web.model.dealer.branchassign.view.request.AssignUserToBranchDTLRequestModel;
import com.hitech.dms.web.model.dealer.branchassign.view.response.AssignUserToBranchDTLResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
public interface AssignUserToBranchDTLDao {
	public AssignUserToBranchDTLResponseModel fetchAssignUserToBranchDTL(String userCode,
			AssignUserToBranchDTLRequestModel requestModel);
}
