/**
 * 
 */
package com.hitech.dms.web.service.activityclaim;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.mobile.device.Device;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.web.entity.activityclaim.ActivityClaimHdrEntity;
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
public interface activityClaimService {

	/**
	 * @param userCode
	 * @param device
	 * @param acIntegertivityPlanId 
	 * @return
	 */
	Map<String, Object> getActivityDetails(String userCode, Device device, Integer activityPlanId);

	/**
	 * @param userCode
	 * @param requestedData
	 * @param files
	 * @param device
	 * @return
	 */
	ActivityClaimCreateResponseModel createActivityClaim(String userCode, @Valid ActivityClaimHdrEntity requestedData,
			List<MultipartFile> files, Device device);

	/**
	 * @param userCode
	 * @param acRequestModel
	 * @return
	 */
	ActivityClaimSearchMainResponseModel fetchActivityClaimSearchList(String userCode,
			ActivityClaimSearchRequestModel acRequestModel);

	/**
	 * @param userCode
	 * @param device
	 * @param activityClaimId
	 * @return
	 */
	Map<String, Object> getActivityClaimDetailsById(String userCode, Device device, Integer activityClaimId);

	/**
	 * @param userCode
	 * @param requestModel
	 * @return
	 */
	Map<String, Object> checkActivityPermissions(String userCode, ActivityPermissionRequestModel requestModel);

	/**
	 * @param userCode
	 * @param requestModel
	 * @param device
	 * @return
	 */
	ActivityClaimApproveResponse approveRejectActivityClaim(String userCode,
			ActivityClaimApproveRequestModel requestModel, Device device);

	/**
	 * @param userCode
	 * @param activityPlanNoAutoRequestModel
	 * @return
	 */
	List<ActivityResponseModel> activityPlanAuto(String userCode, ActivityRequestModel activityPlanNoAutoRequestModel);

}
