package com.hitech.dms.web.dao.enquiry.exchange.search;

import java.util.List;

import com.hitech.dms.web.model.enquiry.exchange.request.ExchangeVehicleSearchRequestModel;
import com.hitech.dms.web.model.enquiry.exchange.response.ExchangeEnquiryRes;
import com.hitech.dms.web.model.enquiry.exchange.response.ExchangeVehicleSearchMainResponseModel;

public interface ExchangeVehicleSearchDao {
	public ExchangeVehicleSearchMainResponseModel fetchExchangeVehicleEnquiryList(String userCode,
			ExchangeVehicleSearchRequestModel requestModel);

	public List<ExchangeEnquiryRes> getEnquiryNumbers(String userCode);
}
