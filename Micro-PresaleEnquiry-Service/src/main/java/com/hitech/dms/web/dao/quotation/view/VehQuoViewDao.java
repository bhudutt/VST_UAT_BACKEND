package com.hitech.dms.web.dao.quotation.view;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.quotation.view.request.VehQuoViewRequestModel;
import com.hitech.dms.web.model.quotation.view.response.VehQuoViewResponseModel;

public interface VehQuoViewDao {
	public VehQuoViewResponseModel fetchQuotationDTL(String userCode, VehQuoViewRequestModel quoViewRequestModel,
			Device device);
}
