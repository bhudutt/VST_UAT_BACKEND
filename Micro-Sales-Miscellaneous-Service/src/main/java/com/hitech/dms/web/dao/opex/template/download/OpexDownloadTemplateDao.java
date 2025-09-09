package com.hitech.dms.web.dao.opex.template.download;

import java.io.ByteArrayInputStream;

import javax.servlet.http.HttpServletResponse;

import com.hitech.dms.web.model.opex.template.request.OpexBudgetRequestModel;

public interface OpexDownloadTemplateDao {
	public ByteArrayInputStream createTemplate(HttpServletResponse response, String userCode,
			String authorizationHeader, OpexBudgetRequestModel requestModel);
}
