package com.hitech.dms.web.service.client;

import java.math.BigInteger;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.config.CustomFeignConfig;
import com.hitech.dms.web.model.branchdtl.request.BranchDTLRequestModel;
import com.hitech.dms.web.model.dealerdtl.request.DealerDTLRequestModel;
import com.hitech.dms.web.model.productModels.ModelsForSeriesSegmentRequestModel;

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
	
	@GetMapping(value = "/common/models/fetchModelListByPcId/{pcId}/{isFor}")
	@Headers("Content-Type: application/json")
	public HeaderResponse fetchModelListByPcId(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@PathVariable(name = "pcId") Integer pcId, @PathVariable(name = "isFor") String isFor);
	
	@GetMapping(value = "/common/geo/fetchStateDtlByStateID/{stateId}")
	@Headers("Content-Type: application/json")
	public HeaderResponse fetchStateDtlByStateID(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@PathVariable(name = "stateId") BigInteger stateId);
	
	@PostMapping(value = "/common/models/fetchModelsForSeriesSegment")
	@Headers("Content-Type: application/json")
	public HeaderResponse fetchModelsForSeriesSegment(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody ModelsForSeriesSegmentRequestModel requestFormModel);
}
