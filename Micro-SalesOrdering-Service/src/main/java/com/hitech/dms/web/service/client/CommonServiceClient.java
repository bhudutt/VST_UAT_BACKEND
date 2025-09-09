package com.hitech.dms.web.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.config.CustomFeignConfig;
import com.hitech.dms.web.model.branchdtl.request.BranchDTLRequestModel;
import com.hitech.dms.web.model.dealerdtl.request.DealerDTLRequestModel;

import feign.Headers;

@FeignClient(name = "common-services", configuration = CustomFeignConfig.class, fallback = PostFeignClientFallback.class)
public interface CommonServiceClient {
	@PostMapping(value = "/common/dealer/fetchDealerDTLByDealerId")
	@Headers("Content-Type: application/json")
	public HeaderResponse fetchDealerDTLByDealerId(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody DealerDTLRequestModel requestModel);

	@PostMapping(value = "/common/branch/fetchMainBranchDtlByDealerId")
	@Headers("Content-Type: application/json")
	public HeaderResponse fetchMainBranchDtlByDealerId(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			BranchDTLRequestModel requestModel);

	@PostMapping(value = "/common/branch/fetchBranchDtlByBranchId")
	@Headers("Content-Type: application/json")
	public HeaderResponse fetchBranchDtlByBranchId(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			BranchDTLRequestModel requestModel);
}
