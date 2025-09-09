package com.hitech.dms.web.service.serviceclaim;

import java.math.BigInteger;

import javax.transaction.Transactional;

import com.hitech.dms.web.common.ApiResponse;
import com.hitech.dms.web.entity.serviceclaim.ServiceClaimHdr;
import com.hitech.dms.web.model.serviceclaim.ServiceCLaimApprovalRequestDto;
import com.hitech.dms.web.model.serviceclaim.ServiceClaimJCListRequest;
import com.hitech.dms.web.model.serviceclaim.ServiceClaimSearchRequestDto;

/**
 * @author suraj.gaur
 */
@Transactional
public interface ServiceClaimService {
	
	ApiResponse<?> saveServiceClaim(String userCode, ServiceClaimHdr requestModel);
	
	ApiResponse<?> getJobcardClaimList(String userCode, ServiceClaimJCListRequest requestModel);

	ApiResponse<?> viewServiceClaim(BigInteger claimId);

	ApiResponse<?> getClaimTypes();
	
	ApiResponse<?>  autoSearchClaimNo(String claimNo);
	
	ApiResponse<?> getClaimStatus();
	
	ApiResponse<?>  serviceClaimSearch(String userCode, ServiceClaimSearchRequestDto requestModel);

	ApiResponse<?> approveRejectSVClaim(String userCode, ServiceCLaimApprovalRequestDto requestModel);

	ApiResponse<?> installationAppAndRej(String userCode, Integer installationId, String statusApproveReject);
	
}
