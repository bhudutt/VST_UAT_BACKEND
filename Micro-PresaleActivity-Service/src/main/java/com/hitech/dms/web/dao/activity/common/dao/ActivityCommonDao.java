package com.hitech.dms.web.dao.activity.common.dao;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.activityClaim.approval.response.ActivityClaimDtlListResponseModel;
import com.hitech.dms.web.model.activityClaim.create.response.ActivityClaimHdrResponseModel;
import com.hitech.dms.web.model.activityplan.dtl.response.ActivityPlanHDRResponseModel;

public interface ActivityCommonDao {
	public ActivityClaimHdrResponseModel fetchActivityPlanHdr(String userCode,
			BigInteger activityPlanHdrId, String isFor, Device device);
	public ActivityClaimHdrResponseModel fetchActivityPlanHdr(Session session, String userCode,
			BigInteger activityPlanHdrId, String isFor);
	
	public ActivityPlanHDRResponseModel fetchActivityPlanHDRDTL(String userCode,
			BigInteger activityPlanHdrId, String isFor);
	public ActivityPlanHDRResponseModel fetchActivityPlanHDRDTL(Session session, String userCode,
			BigInteger activityPlanHdrId, String isFor);
	
	public List<ActivityClaimDtlListResponseModel> fetchActivityClaimDtlList(Session session, String userCode,
			BigInteger dealerId, BigInteger activityPlanHdrId, String isFor);
	
	public List<?> fetchApprovalData(Session session, String approvalCode);
	
	public ActivityClaimHdrResponseModel fetchActivityClaimHdr(Session session, String userCode,
			BigInteger activityPlanHdrId, String isFor);
	
	public Map<String, Object> fetchHOUserDTLByUserCode(Session session, String userCode);
	
	public Map<String, Object> fetchUserDTLByUserCode(Session session, String userCode);
	public Map<String,Object> fetchBranchDetials(Session sesssion,String userCode);
}
