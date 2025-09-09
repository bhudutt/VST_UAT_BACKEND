package com.hitech.dms.web.service.salesman.request;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.config.CustomFeignConfig;
import com.hitech.dms.web.model.enquiry.salesman.request.SalesmanListFormModel;
import com.hitech.dms.web.model.enquiry.salesman.response.SalesmanListResponseModel;

import feign.Headers;

@FeignClient(name = "common-services", configuration = CustomFeignConfig.class, fallback = PostFeignClientFallback.class)
public interface SalesmanServiceClient {
	@PostMapping(value = "/salesman/salesmanList")
	@Headers("Content-Type: application/json")
	public SalesmanListResponseModel fetchSalesmanList(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody SalesmanListFormModel salesmanListFormModel);

	@GetMapping(value = "/common/salesman/salesmanList/{dealerOrBranch}/{dealerOrBranchID}")
	@Headers("Content-Type: application/json")
	public HeaderResponse getSalesmanList(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@PathVariable(name = "dealerOrBranch") String dealerOrBranch,
			@PathVariable(name = "dealerOrBranchID") Long dealerOrBranchID);
}
