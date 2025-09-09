package com.hitech.dms.web.dao.common;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.hitech.dms.web.model.branchdtl.response.BranchDTLResponseModel;
import com.hitech.dms.web.model.dealerdtl.response.DealerDTLResponseModel;

public interface CommonDao {

	public DealerDTLResponseModel fetchDealerDTLByDealerId(String authorizationHeader, BigInteger dealerId);
	
	public BranchDTLResponseModel fetchBranchDtlByBranchId(String authorizationHeader, BigInteger branchId);
	
	public Map<String, Object> fetchHOUserDTLByUserCode(Session session, String userCode);

	public Map<String, Object> fetchUserDTLByUserCode(Session session, String userCode);

	public List<?> fetchApprovalData(Session session, String approvalCode);
}
