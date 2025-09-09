package com.hitech.dms.web.dao.enquiry.exchange.update;

import com.hitech.dms.web.model.enquiry.exchange.request.ExchangeVehicleUpdateRequestModel;
import com.hitech.dms.web.model.enquiry.exchange.response.ExchangeVehicleUpdateResponseModel;

public interface ExchangeVehicleUpdateDao {
	public ExchangeVehicleUpdateResponseModel updateExchangeVeh(String userCode,
			ExchangeVehicleUpdateRequestModel exchangeVehicleUpdateRequestModel);
}
