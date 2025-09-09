/**
 * 
 */
package com.hitech.dms.app.interceptor;

import java.util.concurrent.CompletableFuture;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.filter.request.HttpRequestResponseUtils;
import com.hitech.dms.web.model.user.activitylog.request.UserActivityLogRequestModel;
import com.hitech.dms.web.service.client.AuthServiceClient;

/**
 * @author dinesh.jakhar
 *
 */
@Component
public class AppLogger implements HandlerInterceptor {
	private static final Logger logger = LoggerFactory.getLogger(AppLogger.class);

	@Autowired
	private AuthServiceClient authServiceClient;
	private static final String TOKEN_PREFIX = "Bearer ";
	private static final String HEADER_STRING = "Authorization";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		try {
			final String eventType = HttpRequestResponseUtils.getEventType();
			final String ip = HttpRequestResponseUtils.getClientIpAddress();
			final String url = HttpRequestResponseUtils.getRequestUrl();
			final String page = HttpRequestResponseUtils.getPageName();
			final String refererPage = HttpRequestResponseUtils.getRefererPage();
			final String queryString = HttpRequestResponseUtils.getPageQueryString();
			final String userAgent = HttpRequestResponseUtils.getUserAgent();
			final String requestMethod = HttpRequestResponseUtils.getRequestMethod();
			final String latitude = HttpRequestResponseUtils.getLatitude();
			final String longitude = HttpRequestResponseUtils.getLongitude();

			if (eventType != null && !eventType.equals("")) {
				UserActivityLogRequestModel visitor = new UserActivityLogRequestModel();
				// visitor.setUser(HttpRequestResponseUtils.getLoggedInUser());
				visitor.setIp(ip);
				visitor.setEvent(eventType);
				visitor.setMethod(requestMethod);
				visitor.setUrl(url);
				visitor.setPage(page);
				visitor.setQueryString(queryString);
				visitor.setRefererPage(refererPage);
				visitor.setUserAgent(userAgent);
				visitor.setUniqueVisit(true);
				visitor.setLatitude(latitude);
				visitor.setLongitude(longitude);
				logger.info(visitor != null ? visitor.toString() : null);
				String authToken = request.getHeader(HEADER_STRING);
				if (authToken != null) {
					authToken = authToken.replace(TOKEN_PREFIX, "");
					visitor.setUserCode(HttpRequestResponseUtils.getLoggedInUser());
					updateActivityLogs(authToken, visitor);
				}
			}
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		}

		return true;
	}

	@Async
	CompletableFuture<HeaderResponse> updateActivityLogs(String authToken, UserActivityLogRequestModel visitor) {
		return CompletableFuture.completedFuture(authServiceClient.updateActivityLogs(authToken, visitor));
	}
}
