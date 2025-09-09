package com.hitech.dms.web.service.client;

import org.springframework.stereotype.Component;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.web.model.enquiry.list.request.EnquiryListRequestModel;
import com.hitech.dms.web.model.quotation.search.request.VehQuoSearchRequestModel;

@Component
public class PostFeignClientFallback implements EnquiryServiceClient {

	@Override
	public HeaderResponse fetchEnquiryList(String authorizationHeader, EnquiryListRequestModel requestModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HeaderResponse searchQuoList(String authorizationHeader,
			VehQuoSearchRequestModel requestModel) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
