package com.hitech.dms.web.dao.receipt.search;

import com.hitech.dms.web.model.stock.receipt.search.request.ReceiptSearchRequestModel;
import com.hitech.dms.web.model.stock.receipt.search.response.ReceiptSearchMainResponseModel;

public interface ReceiptSearchDao {
	public ReceiptSearchMainResponseModel searchReceiptList(String userCode, ReceiptSearchRequestModel requestModel);
}
