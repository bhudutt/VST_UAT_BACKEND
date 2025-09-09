package com.hitech.dms.web.dao.pr.search;

import com.hitech.dms.web.model.pr.search.request.PurchaseReturnSearchRequestModel;
import com.hitech.dms.web.model.pr.search.response.PurchaseReturnSearchMainResponseModel;

public interface PurchaseReturnSearchDao {
	public PurchaseReturnSearchMainResponseModel searchSalesPurchaseReturnList(String userCode, PurchaseReturnSearchRequestModel requestModel);
}
