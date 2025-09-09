package com.hitech.dms.web.dao.activityClaim.create;

import java.math.BigInteger;
import java.util.List;

import org.springframework.mobile.device.Device;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.web.entity.activityClaim.ActivityClaimHdrEntity;
import com.hitech.dms.web.model.activityClaim.create.response.ActivityClaimCreateResponseModel;
import com.hitech.dms.web.model.activityClaim.create.response.ActivityClaimHdrDtlResponseModel;
import com.hitech.dms.web.model.activityClaim.create.response.ActivityPlanAutoResponseModel;

/**
 * @author vinay.gautam
 *
 */
public interface ActivityClaimCreateDao {
	
	public List<ActivityPlanAutoResponseModel> activityPlanAuto(String userCode,BigInteger dealerId, String isFor, String searchText);
	public ActivityClaimHdrDtlResponseModel fetchActivityHdrAndDtl(String userCode,BigInteger dealerId, BigInteger activityPlanHdrId, String isFor, Device device);
	public ActivityClaimCreateResponseModel createActivityClaim(String userCode, ActivityClaimHdrEntity requestData, List<MultipartFile> files, Device device);
}
