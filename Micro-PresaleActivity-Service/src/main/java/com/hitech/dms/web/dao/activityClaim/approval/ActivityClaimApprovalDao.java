package com.hitech.dms.web.dao.activityClaim.approval;

import java.math.BigInteger;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.activityClaim.approval.request.ActivityClaimApproveRequestModel;
import com.hitech.dms.web.model.activityClaim.approval.response.ActivityClaimApproveResponse;
import com.hitech.dms.web.model.activityClaim.approval.response.ActivityClaimHdrDtlViewResponseModel;

/**
 * @author vinay.gautam
 *
 */
public interface ActivityClaimApprovalDao {
	public ActivityClaimHdrDtlViewResponseModel fetchActivityHdrAndDtlView(String userCode,BigInteger dealerId, BigInteger id,String isFor, Device device);
	
	public ActivityClaimApproveResponse approveRejectActivityClaim(String userCode,ActivityClaimApproveRequestModel acaRequestModel, Device device);

}
