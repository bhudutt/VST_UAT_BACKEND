package com.hitech.dms.web.dao.receipt.view;

import com.hitech.dms.web.model.stock.receipt.view.request.ReceiptViewRequestModel;
import com.hitech.dms.web.model.stock.receipt.view.response.ReceiptViewResponseModel;

public interface ReceiptViewDao {
	public ReceiptViewResponseModel fetchReceiptView(String userCode, ReceiptViewRequestModel requestModel);
}
