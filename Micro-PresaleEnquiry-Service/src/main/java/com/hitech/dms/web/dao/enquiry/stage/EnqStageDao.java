package com.hitech.dms.web.dao.enquiry.stage;

import java.util.List;

import com.hitech.dms.web.model.enquiry.stages.request.EnqStageRequestModel;
import com.hitech.dms.web.model.enquiry.stages.response.EnqStageResponseModel;

public interface EnqStageDao {
	public List<EnqStageResponseModel> fetchEnqStageList(String userCode, String isFor);

	public List<EnqStageResponseModel> fetchEnqStageList(String userCode, EnqStageRequestModel enqStageRequestModel);
}
