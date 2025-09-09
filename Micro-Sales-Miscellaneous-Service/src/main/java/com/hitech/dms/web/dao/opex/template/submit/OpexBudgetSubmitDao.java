package com.hitech.dms.web.dao.opex.template.submit;

import com.hitech.dms.web.model.opex.template.submit.request.OpexBudgetSubmitRequestModel;
import com.hitech.dms.web.model.opex.template.submit.response.OpexBudgetSubmitResponseModel;

public interface OpexBudgetSubmitDao {
	public OpexBudgetSubmitResponseModel submitOpexBudget(String authorizationHeader, String userCode,
			OpexBudgetSubmitRequestModel requestModel);
}
