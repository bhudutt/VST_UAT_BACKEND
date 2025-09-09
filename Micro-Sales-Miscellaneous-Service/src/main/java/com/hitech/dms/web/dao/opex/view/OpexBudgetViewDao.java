package com.hitech.dms.web.dao.opex.view;

import com.hitech.dms.web.model.opex.view.request.OpexBudgetViewRequestModel;
import com.hitech.dms.web.model.opex.view.response.OpexBudgetViewResponseModel;

public interface OpexBudgetViewDao {
	public OpexBudgetViewResponseModel fetchOpexBudgetView(String userCode, OpexBudgetViewRequestModel requestModel);
}
