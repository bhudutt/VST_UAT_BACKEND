/**
 * 
 */
package com.hitech.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

/**
 * @author dinesh.jakhar
 *
 */
@Component
public class PrePostGlobalFilter implements GlobalFilter, Ordered {
	final Logger logger = LoggerFactory.getLogger(PrePostGlobalFilter.class);

	static final String TOKEN_PREFIX = "Bearer ";
	static final String HEADER_STRING = "Authorization";
	private static final String EVENT_TYPE = "Event-Type";
	private static final String PAGE_NAME = "Page-Name";
	private static final String[] EVENT_TYPE_CANDIDATES = { "Submit", "Update", "Approve", "Reject", "Search" };

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//		logger.info("First Pre Global Filter");
//		final String ip = HttpRequestResponseUtils.getClientIpAddress();
//		final String url = HttpRequestResponseUtils.getRequestUrl();
//		final String page = HttpRequestResponseUtils.getRequestUri();
//		final String refererPage = HttpRequestResponseUtils.getRefererPage();
//		final String queryString = HttpRequestResponseUtils.getPageQueryString();
//		final String userAgent = HttpRequestResponseUtils.getUserAgent();
//		final String requestMethod = HttpRequestResponseUtils.getRequestMethod();
//		final LocalDateTime timestamp = LocalDateTime.now();

//		logger.info(exchange.getRequest().getMethodValue());
//		logger.info(exchange.getRequest().getPath().toString());
//		logger.info(exchange.getRequest().getRemoteAddress().toString());
//		try {
//			if (exchange.getRequest().getHeaders().get("Authorization") != null) {
//				UserActivityLogRequestModel logRequestModel = createUserActivityLogObject(exchange);
//				logger.info(logRequestModel != null ? logRequestModel.toString() : null);
////				CompletableFuture<HeaderResponse> activityResponse = updateActivityLogs(
////						exchange.getRequest().getHeaders().get("Authorization").get(0).replace(TOKEN_PREFIX, ""),
////						logRequestModel);
//				HeaderResponse activityResponse = serviceClient.updateActivityLogs(exchange.getRequest().getHeaders().get("Authorization").get(0).replace(TOKEN_PREFIX, ""),
//						logRequestModel);
//				if(activityResponse != null) {
//					logger.info(activityResponse.toString());
//				}
//			}
//		} catch (Exception exp) {
//			logger.error(this.getClass().getName(), exp);
//		}
		return chain.filter(exchange).then(Mono.fromRunnable(() -> {
			logger.info("Last Post Global Filter");
		}));
	}

	@Override
	public int getOrder() {
		return -1;
	}

//	@Async
//	CompletableFuture<HeaderResponse> updateActivityLogs(String authorizationHeader,
//			UserActivityLogRequestModel logRequestModel) {
//		return CompletableFuture
//				.completedFuture(serviceClient.updateActivityLogs(authorizationHeader, logRequestModel));
//	}

//	private UserActivityLogRequestModel createUserActivityLogObject(ServerWebExchange exchange) {
//		UserActivityLogRequestModel visitor = null;
//		final LocalDateTime timestamp = LocalDateTime.now();
////		visitor.setUserCode(HttpRequestResponseUtils.getLoggedInUser());
//		HttpHeaders headers = exchange.getRequest().getHeaders();
//		for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
//			System.out.println(entry.getKey() + " : " + entry.getValue());
//			String key = entry.getKey();
//			if (key != null && key.equals(EVENT_TYPE)) {
//				visitor = new UserActivityLogRequestModel();
//				visitor.setRefererPage(headers.get("Referer").get(0));
//				visitor.setUserAgent(headers.get("User-Agent").get(0));
//				if(headers.get(PAGE_NAME) != null) {
//					visitor.setPage(headers.get(PAGE_NAME).get(0));
//				}else {
//					visitor.setPage(exchange.getRequest().getPath().toString());
//				}
//				visitor.setIp(exchange.getRequest().getRemoteAddress().toString());
//				visitor.setMethod(exchange.getRequest().getMethodValue());
//				visitor.setUrl(exchange.getRequest().getPath().toString());
//				visitor.setLoggedTime(timestamp);
//				visitor.setUniqueVisit(true);
//			}
//		}
//		if(visitor != null) {
//			logger.info(visitor.toString());
//		}
//		return visitor;
//	}
}
