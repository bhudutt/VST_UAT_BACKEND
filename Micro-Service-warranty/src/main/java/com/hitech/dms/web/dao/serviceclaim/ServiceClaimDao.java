package com.hitech.dms.web.dao.serviceclaim;

import java.math.BigInteger;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;

import com.hitech.dms.web.model.serviceclaim.InstaillationStatusBean;
import com.hitech.dms.web.model.serviceclaim.JobCardStatusBean;
import com.hitech.dms.web.model.serviceclaim.PDIStatusBean;
import com.hitech.dms.web.model.serviceclaim.ServiceCLaimApprovalRequestDto;
import com.hitech.dms.web.model.serviceclaim.ServiceClaimJCListRequest;
import com.hitech.dms.web.model.serviceclaim.ServiceClaimSearchRequestDto;

/**
 * @author suraj.gaur
 */
@Transactional
public interface ServiceClaimDao {
	
	List<?> getJobcardClaimList(Session session, String userCode, ServiceClaimJCListRequest requestModel);

	List<?> viewServiceClaim(Session session, BigInteger claimId, Integer flag);

	List<?> getClaimTypes(Session session);
	
	List<?> autoSearchClaimNo(Session session, String claimNo);
	
	List<?> getClaimStatus(Session session);
	
	List<?> serviceClaimSearch(Session session, String userCode, ServiceClaimSearchRequestDto requestModel);
	
	List<?> getApprovalHierarchy(Session session);
	
	List<?> approveRejectSVClaim(Session session, String userCode, BigInteger hoUserId, ServiceCLaimApprovalRequestDto requestModel);

	Integer installationAppAndRej(Session session, String userCode, BigInteger hoUserId, Integer installationId,
			String statusApproveReject);

	void approveRejectStatusClaim(Session session, String userCode, BigInteger hoUserId,
			List<InstaillationStatusBean> instaillationStatus);

	void approveRejectPDIStatusClaim(Session session, String userCode, BigInteger hoUserId,
			List<PDIStatusBean> pdiStatus);

	void approveRejectJCStatusClaim(Session session, String userCode, BigInteger hoUserId,
			List<JobCardStatusBean> jobCardStatus);

}
