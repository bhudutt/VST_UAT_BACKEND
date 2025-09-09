package com.hitech.dms.web.dao.sales.upload.template;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.springframework.mobile.device.Device;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.web.model.sales.upload.request.StockAdjustmentRequestModel;
import com.hitech.dms.web.model.sales.upload.response.StockAdjustmentResponseModel;


public interface StockAjustmentTemplateDao {

	 //Map<String, StringBuffer> validateUploadedFile(String userCode, String dealerCode,BigInteger profitCenter,Device device, MultipartFile file) throws IOException;


	 public StockAdjustmentResponseModel validateUploadedFile(String authorizationHeader, String userCode,
			 StockAdjustmentRequestModel stockUploadRequestModel,MultipartFile files);
}
