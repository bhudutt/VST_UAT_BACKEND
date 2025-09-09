package com.hitech.dms.web.dao.pr.view;

import com.hitech.dms.web.model.pr.view.request.PurchaseReturnViewRequestModel;
import com.hitech.dms.web.model.pr.view.response.PurchaseReturnViewResponseModel;

public interface PurchaseReturnViewDao {
	public PurchaseReturnViewResponseModel fetchpurchaseReturnDtlView(String userCode,
			PurchaseReturnViewRequestModel requestModel);
}
