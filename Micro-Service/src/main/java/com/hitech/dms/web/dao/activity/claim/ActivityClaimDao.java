/**
 * 
 */
package com.hitech.dms.web.dao.activity.claim;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.mobile.device.Device;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.web.entity.activityclaim.ActivityClaimHdrEntity;
import com.hitech.dms.web.model.activity.claim.request.ActivityClaim;
import com.hitech.dms.web.model.activity.claim.request.ActivityClaimApproveRequestModel;
import com.hitech.dms.web.model.activity.claim.request.ActivityClaimSearchRequestModel;
import com.hitech.dms.web.model.activity.claim.response.ActivityClaimApproveResponse;
import com.hitech.dms.web.model.activity.claim.response.ActivityClaimCreateResponseModel;
import com.hitech.dms.web.model.activity.claim.response.ActivityClaimSearchMainResponseModel;
import com.hitech.dms.web.model.activity.claim.response.ActivityPermissionRequestModel;
import com.hitech.dms.web.model.activity.create.request.ActivityRequestModel;
import com.hitech.dms.web.model.activity.create.response.ActivityResponseModel;

/**
 * @author santosh.kumar
 *
 */
public interface ActivityClaimDao {

	/**
	 * @param userCode
	 * @param acIntegertivityPlanId
	 * @return
	 */
	Map<String, Object> getActivityDetails(String userCode, Integer activityPlanId);

	/**
	 * @param userCode
	 * @param requestedData
	 * @param files
	 * @param device
	 * @return
	 */
	ActivityClaimCreateResponseModel saveActivityClaim(String userCode, @Valid ActivityClaimHdrEntity requestedData,
			List<MultipartFile> files, Device device);

	/**
	 * @param userCode
	 * @param acRequestModel
	 * @return
	 */
	ActivityClaimSearchMainResponseModel fetchActivityClaim(String userCode,
			ActivityClaimSearchRequestModel acRequestModel);

	/**
	 * @param userCode
	 * @param activityClaimId
	 * @return
	 */
	Map<String, Object> fetchActivityHeader(String userCode, Integer activityClaimId);

	/**
	 * @param userCode
	 * @param activityClaimId
	 * @return
	 */
	Map<String, Object> fetchActivityClaimDetails(String userCode, Integer activityClaimId);

	/**
	 * @param userCode
	 * @param requestModel
	 * @return
	 */
	Map<String, Object> fetchActivityPermissions(String userCode, ActivityPermissionRequestModel requestModel);

	/**
	 * @param userCode
	 * @param requestModel
	 * @return
	 */
	ActivityClaimApproveResponse approveAndRejectActivityClaim(String userCode,
			ActivityClaimApproveRequestModel requestModel);

	/**
	 * @param activityPlanHdrId
	 * @return
	 */
	Map<String, Object> fetchActivityClaimByPlanId(BigInteger activityPlanHdrId);

	/**
	 * @param userCode
	 * @param activityPlanNoAutoRequestModel
	 * @return
	 */
	List<ActivityResponseModel> fetchPlanNumberForClaim(String userCode,
			ActivityRequestModel activityPlanNoAutoRequestModel);

	void approveAndRejectActivityClaimDetail(String userCode, List<ActivityClaim> activityList);

}
