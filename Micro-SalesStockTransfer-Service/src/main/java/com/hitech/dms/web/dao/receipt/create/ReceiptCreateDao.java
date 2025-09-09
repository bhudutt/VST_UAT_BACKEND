package com.hitech.dms.web.dao.receipt.create;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.stock.receipt.request.ReceiptCreateRequestModel;
import com.hitech.dms.web.model.stock.receipt.response.ReceiptCreateResponseModel;

public interface ReceiptCreateDao {
	public ReceiptCreateResponseModel createReceipt(String authorizationHeader, String userCode,
			ReceiptCreateRequestModel requestModel, Device device);
}
