package com.hitech.dms.app.config;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.hitech.dms.app.filter.request.HttpRequestResponseUtils;
import com.hitech.dms.web.model.user.RoleDtlModel;
import com.hitech.dms.web.model.user.accesslog.request.AccessLogRequestModel;
import com.hitech.dms.web.model.user.activitylog.response.UserActivityLogResponseModel;
import com.hitech.dms.web.service.user.UserService;

@Component
public class CustomTokenEnhancer implements TokenEnhancer {

	@Autowired
	private UserService userService;

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		final Map<String, Object> additionalInfo = new HashMap<>();
		additionalInfo.put("organization", authentication.getName() + randomAlphabetic(4));
		try {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
					.getRequest();
			final String ip = HttpRequestResponseUtils.getClientIpAddress();
			UserActivityLogResponseModel response = insertAccessLogs(authentication.getName(), ip, request);
			if (response != null && response.getId() != null) {
				additionalInfo.put("userLoginAccessId", response.getId());
			}
			additionalInfo.put("user_ip", ip);
			additionalInfo.put("user_code", authentication.getName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<RoleDtlModel> userRoleList = userService.fetchUserRoleList(authentication.getName());
		additionalInfo.put("userRoleList", userRoleList.toArray());
		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
		return accessToken;
	}

//	@Async
	UserActivityLogResponseModel insertAccessLogs(String userCode, String ip, HttpServletRequest request)
			throws Exception {
		final String userAgent = HttpRequestResponseUtils.getUserAgent();
		final String latitude = HttpRequestResponseUtils.getLatitude();
		final String longitude = HttpRequestResponseUtils.getLongitude();
		final Date currDate = new Date();
		AccessLogRequestModel requestModel = new AccessLogRequestModel();
		requestModel.setUserCode(userCode);
		requestModel.setIpAddress(ip);
		requestModel.setLastAccessTime(currDate);
		requestModel.setUserAgent(userAgent);
		requestModel.setLoginTime(currDate);
		requestModel.setLatitude(latitude);
		requestModel.setLongitude(longitude);
//		return CompletableFuture.completedFuture(userService.insertIntoAccessLog(userCode, requestModel));
		return userService.insertIntoAccessLog(userCode, requestModel);
	}
}
