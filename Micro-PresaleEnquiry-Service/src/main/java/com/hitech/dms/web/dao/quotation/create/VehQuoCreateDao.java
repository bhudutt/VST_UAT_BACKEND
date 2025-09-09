package com.hitech.dms.web.dao.quotation.create;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.quotation.create.request.VehQuoHDRRequestModel;
import com.hitech.dms.web.model.quotation.create.response.VehQuoResponseModel;

public interface VehQuoCreateDao {
	public VehQuoResponseModel saveVehicleQuotation(String userCode, VehQuoHDRRequestModel vehQuoHDRRequestModel, Device device);
}
