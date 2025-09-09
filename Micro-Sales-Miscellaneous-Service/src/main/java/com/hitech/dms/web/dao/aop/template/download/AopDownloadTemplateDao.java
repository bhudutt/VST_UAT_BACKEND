package com.hitech.dms.web.dao.aop.template.download;

import java.io.ByteArrayInputStream;

import javax.servlet.http.HttpServletResponse;

import com.hitech.dms.web.model.aop.template.request.AopTargetRequestModel;

public interface AopDownloadTemplateDao {
	public ByteArrayInputStream createTemplate(HttpServletResponse response, String userCode,
			String authorizationHeader, AopTargetRequestModel requestModel);
}
