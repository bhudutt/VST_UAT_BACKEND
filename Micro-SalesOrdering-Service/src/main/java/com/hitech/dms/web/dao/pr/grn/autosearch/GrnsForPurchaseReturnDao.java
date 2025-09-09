package com.hitech.dms.web.dao.pr.grn.autosearch;

import java.util.List;

import com.hitech.dms.web.model.pr.grn.autosearch.request.GrnsForPurchaseReturnRequestModel;
import com.hitech.dms.web.model.pr.grn.autosearch.response.GrnsForPurchaseReturnResponseModel;

public interface GrnsForPurchaseReturnDao {
	public List<GrnsForPurchaseReturnResponseModel> fetchGrnListForPurchaseReturn(String userCode,
			GrnsForPurchaseReturnRequestModel requestModel);
}
