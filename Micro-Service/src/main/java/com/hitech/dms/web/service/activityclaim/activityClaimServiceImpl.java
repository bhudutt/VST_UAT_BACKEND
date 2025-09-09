/**
 * 
 */
package com.hitech.dms.web.service.activityclaim;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import java.lang.String;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.web.dao.activity.claim.ActivityClaimDao;
import com.hitech.dms.web.dao.activity.claim.ActivityClaimRepository;
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

@Service
public class activityClaimServiceImpl implements activityClaimService {

	@Autowired
	private ActivityClaimDao activityClaimdao;

	@Override
	public Map<String, Object> getActivityDetails(String userCode, Device device, Integer activityPlanId) {

		return activityClaimdao.getActivityDetails(userCode, activityPlanId);
	}
	
	@Override
	public ActivityClaimCreateResponseModel createActivityClaim(String userCode,
	        @Valid ActivityClaimHdrEntity requestedData, List<MultipartFile> files, Device device) {
	    ActivityClaimCreateResponseModel response = new ActivityClaimCreateResponseModel();

	    try {
	        Map<String, Object> activityClaimMap = activityClaimdao.fetchActivityClaimByPlanId(requestedData.getActivityPlanHdrId());

	        if (activityClaimMap.isEmpty()) {
	            return activityClaimdao.saveActivityClaim(userCode, requestedData, files, device);
	        } else {
	            Object activityClaimNumber = activityClaimMap.values().iterator().next();
	           
	            if (activityClaimNumber != null) {
	                response.setActivityClaimNumber(activityClaimNumber.toString());
	                response.setStatusCode(500);
	                response.setMsg("claim already generated for this plan number");
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.setStatusCode(500); // Example: Internal Server Error
	        response.setMsg("Failed to create activity claim due to an unexpected error.");
	    }

	    return response;
	}


//	@Override
//	public ActivityClaimCreateResponseModel createActivityClaim(String userCode,
//	        @Valid ActivityClaimHdrEntity requestedData, List<MultipartFile> files, Device device) {
//	    ActivityClaimCreateResponseModel response = new ActivityClaimCreateResponseModel();
//
//	    try {
//	        Map<String, Object> res = activityClaimdao.fetchActivityClaimByPlanId(requestedData.getActivityPlanHdrId());
//
//	        Collection<Object> values = res.values();
//	        // Convert the collection to a List
//	        List<Object> valueList = new ArrayList<>(values);
//
//	        if (valueList == null && valueList.isEmpty()) {
//	            return activityClaimdao.saveActivityClaim(userCode, requestedData, files, device);
//	        } else {
//	            if (valueList.get(0) != null) {
//	                response.setActivityClaimNumber(valueList.get(0).toString());
//	                response.setStatusCode(500);
//	                response.setMsg(valueList.get(0).toString() + " claim already generated for this plan number");
//	            }
//	        }
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	    }
//
//	    return response;
//	}


	@Override
	public ActivityClaimSearchMainResponseModel fetchActivityClaimSearchList(String userCode,
			ActivityClaimSearchRequestModel acRequestModel) {
		return activityClaimdao.fetchActivityClaim(userCode, acRequestModel);
	}

	@Override
	public Map<String, Object> getActivityClaimDetailsById(String userCode, Device device, Integer activityClaimId) {
		Map<String, Object> finalResponse = new HashMap<>();
		Map<String, Object> headerResponse = activityClaimdao.fetchActivityHeader(userCode, activityClaimId);
		Map<String, Object> detailsResponse = activityClaimdao.fetchActivityClaimDetails(userCode, activityClaimId);
		finalResponse.put("header", headerResponse.get("Header"));
		finalResponse.put("details", detailsResponse.get("Details"));
		return finalResponse;
	}

	@Override
	public Map<String, Object> checkActivityPermissions(String userCode, ActivityPermissionRequestModel requestModel) {

		return activityClaimdao.fetchActivityPermissions(userCode, requestModel);
	}

	@Override
	public ActivityClaimApproveResponse approveRejectActivityClaim(String userCode,
			ActivityClaimApproveRequestModel requestModel, Device device) {

	//	activityClaimdao.approveAndRejectActivityClaimDetail(userCode, requestModel.getActivityList());
		return activityClaimdao.approveAndRejectActivityClaim(userCode, requestModel);
	}

	@Override
	public List<ActivityResponseModel> activityPlanAuto(String userCode,
			ActivityRequestModel activityPlanNoAutoRequestModel) {
		return activityClaimdao.fetchPlanNumberForClaim(userCode,activityPlanNoAutoRequestModel);
	}

}
