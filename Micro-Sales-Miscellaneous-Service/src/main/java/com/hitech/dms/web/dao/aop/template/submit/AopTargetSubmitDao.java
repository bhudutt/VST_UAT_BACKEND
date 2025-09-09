
package com.hitech.dms.web.dao.aop.template.submit;

import javax.validation.Valid;

import com.hitech.dms.web.model.aop.template.submit.request.AopTargetSubmitRequestModel;
import com.hitech.dms.web.model.aop.template.submit.request.AopTargetUpdateRequestModel;
import com.hitech.dms.web.model.aop.template.submit.response.AopTargetSubmitResponseModel;

public interface AopTargetSubmitDao {
	public AopTargetSubmitResponseModel submitAopTarget(String authorizationHeader, String userCode,
			AopTargetSubmitRequestModel requestModel);

	public AopTargetSubmitResponseModel updateAopTarget(String authorizationHeader, String userCode,
			@Valid AopTargetUpdateRequestModel requestModel);
}
