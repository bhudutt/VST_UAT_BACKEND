package com.hitech.dms.web.dao.branch;

import java.util.List;

import com.hitech.dms.web.model.branchdtl.request.BranchDTLRequestModel;
import com.hitech.dms.web.model.branchdtl.response.BranchDTLResponseModel;

public interface BranchDao {
	public List<BranchDTLResponseModel> fetchBranchListByDealerId(String userCode, BranchDTLRequestModel requestModel);

	public BranchDTLResponseModel fetchMainBranchDtlByDealerId(String userCode, BranchDTLRequestModel requestModel);
	
	public BranchDTLResponseModel fetchBranchDtlByBranchId(String userCode, BranchDTLRequestModel requestModel);

}
