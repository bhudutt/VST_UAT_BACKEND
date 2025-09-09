package com.hitech.dms.web.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.config.CustomFeignConfig;
import com.hitech.dms.web.model.activity.source.request.ActivitySourceListRequestModel;
import com.hitech.dms.web.model.productModels.ModelsForSeriesSegmentRequestModel;

import feign.Headers;

@FeignClient(name = "common-services", configuration = CustomFeignConfig.class, fallback = PostFeignClientFallback.class)
public interface CommonServiceClient {
	@PostMapping(value = "/common/activity/fetchActivitySourceListByPcId")
	@Headers("Content-Type: application/json")
	public HeaderResponse fetchActivitySourceListByPcId(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody ActivitySourceListRequestModel requestFormModel);

	@GetMapping(value = "/common/activity/fetchActivitySourceListByPcId/{pcId}/{isFor}/{isIncludeInActive}")
	@Headers("Content-Type: application/json")
	public HeaderResponse fetchActivitySourceListByPcId(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@PathVariable(name = "pcId") Integer pcId, @PathVariable(name = "isFor") String isFor,
			@PathVariable(name = "isIncludeInActive") String isIncludeInActive);

	@GetMapping(value = "/common/userbranch/branchesUnderUserList/{isInactiveInclude}")
	@Headers("Content-Type: application/json")
	public HeaderResponse branchesUnderUserList(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@PathVariable(name = "isInactiveInclude") String isInactiveInclude);

	@GetMapping(value = "/common/userdealer/dealersUnderUserList/{isInactiveInclude}/{isFor}")
	@Headers("Content-Type: application/json")
	public HeaderResponse dealersUnderUserList(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@PathVariable(name = "isInactiveInclude") String isInactiveInclude,
			@PathVariable(name = "isFor") String isFor);

	@PostMapping(value = "/common/models/fetchModelsForSeriesSegment")
	@Headers("Content-Type: application/json")
	public HeaderResponse fetchModelsForSeriesSegment(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody ModelsForSeriesSegmentRequestModel requestFormModel);
}
