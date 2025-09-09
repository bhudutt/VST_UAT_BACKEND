package com.hitech.dms.web.dao.incentive.template.billing;

import java.io.ByteArrayInputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.web.model.scheme.template.billing.download.request.SchemeIncentiveTemplateRequestModel;
import com.hitech.dms.web.model.scheme.template.billing.upload.request.IncentiveBillingRequestModel;
import com.hitech.dms.web.model.scheme.template.billing.upload.response.IncentiveBillingMainResponseModel;

public interface IncentiveTemplateDao {
	public ByteArrayInputStream createTemplate(HttpServletResponse response, String userCode,
			String authorizationHeader, SchemeIncentiveTemplateRequestModel requestModel);
	
	public IncentiveBillingMainResponseModel validateIncentiveTemplate(String authorizationHeader, String userCode,
			IncentiveBillingRequestModel requestModel, List<MultipartFile> files);
}
