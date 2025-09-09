package com.hitech.dms.web.dao.activityplan.upload;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.web.model.activityplan.approval.response.ActivityPlanDateResponse;
import com.hitech.dms.web.model.activityplan.upload.request.ActivityPlanFormRequestModel;
import com.hitech.dms.web.model.activityplan.upload.request.ActivityPlanUploadRequestModel;
import com.hitech.dms.web.model.activityplan.upload.response.ActivityPlanUploadResponseModel;

public interface ActivityPlanUploadDao {
	public ActivityPlanUploadResponseModel validateUploadedFile(String authorizationHeader, String userCode,
			ActivityPlanUploadRequestModel activityPlanUploadRequestModel, List<MultipartFile> files);
	public ActivityPlanUploadResponseModel submitUploadedPlanActivities(String userCode,
			ActivityPlanFormRequestModel requestModel);
	public ActivityPlanDateResponse userDesignationLevelDesc(String userCode);
}
