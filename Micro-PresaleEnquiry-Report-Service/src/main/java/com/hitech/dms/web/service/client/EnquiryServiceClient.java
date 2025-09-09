package com.hitech.dms.web.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.config.CustomFeignConfig;
import com.hitech.dms.web.model.enquiry.list.request.EnquiryListRequestModel;
import com.hitech.dms.web.model.quotation.search.request.VehQuoSearchRequestModel;

import feign.Headers;

@FeignClient(name = "presales-enquiry")
public interface EnquiryServiceClient {
	@PostMapping(value = "/presaleEnquiry/search/fetchEnquiryList",consumes = MediaType.APPLICATION_JSON_VALUE)
	@Headers("Content-Type: application/json")
	public HeaderResponse fetchEnquiryList(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody EnquiryListRequestModel requestModel);
	
	@PostMapping(value = "/presaleEnquiry/quotation/searchQuoList")
	@Headers("Content-Type: application/json")
	public HeaderResponse searchQuoList(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody VehQuoSearchRequestModel requestModel);
}
