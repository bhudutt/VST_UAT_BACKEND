package com.hitech.dms.web.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.config.CustomFeignConfig;
import com.hitech.dms.web.model.user.accesslog.request.AccessLogUpdateRequestModel;
import com.hitech.dms.web.model.user.activitylog.request.UserActivityLogRequestModel;

import feign.Headers;

@FeignClient(name = "auth-server", configuration = CustomFeignConfig.class, fallback = AuthFeignClientFallback.class)
public interface AuthServiceClient {
	@PostMapping(value = "/auth-api/log/updateActivityLogs")
	@Headers("Content-Type: application/json")
	public HeaderResponse updateActivityLogs(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody UserActivityLogRequestModel requestModel);

	@PostMapping(value = "/auth-api/log/updateAppAccessLog")
	@Headers("Content-Type: application/json")
	public HeaderResponse updateAppAccessLog(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			AccessLogUpdateRequestModel requestModel);
}
