package com.hitech.dms.web.dao.activityplan.upload.template;

import java.io.ByteArrayInputStream;

import javax.servlet.http.HttpServletResponse;

import com.hitech.dms.web.model.activityplan.upload.request.ActivityPlanUploadRequestModel;

public interface ActivityPlanTemplateDao {
	public ByteArrayInputStream createActivityPlanTemplateExcel(HttpServletResponse response, String userCode,
			String authorizationHeader, Integer pcId, String isInactiveInclude);

	public ByteArrayInputStream createActivityPlanTemplateExcel(HttpServletResponse response, String userCode,
			String authorizationHeader, ActivityPlanUploadRequestModel activityPlanUploadRequestModel);
}
