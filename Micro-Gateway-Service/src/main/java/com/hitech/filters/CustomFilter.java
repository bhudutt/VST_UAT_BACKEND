package com.hitech.filters;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.addOriginalRequestUrl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.pattern.PathPattern.PathMatchInfo;

//@Component
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.Config> {
	private static final Logger logger = LoggerFactory.getLogger(CustomFilter.class);

//	@Value("${spring.webflux.base-path}")
	private String basePath;

	public CustomFilter() {
		super(Config.class);
	}
	
	@Override
	public List<String> shortcutFieldOrder() {
	    return Arrays.asList("order");
	}

//   @Override
//   public GatewayFilter apply(Config config) {
//	return (exchange, chain) -> {
//	System.out.println("First pre filter" + exchange.getRequest());
//        //Custom Post Filter.Suppose we can call error response handler based on error code.
//	return chain.filter(exchange).then(Mono.fromRunnable(() -> {
//				System.out.println("First post filter");
//		    }));
//	    };
//    }

//	@Override
//	public GatewayFilter apply(Config config) {
//		return (exchange, chain) -> {
//
//			LinkedHashSet<URI> attr = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
//			
//			Optional<URI> option = attr.stream().findFirst();
//
//			ServerHttpRequest request = exchange.getRequest();
//			if (option.isPresent()) {
//				String originalUrl = option.get().toString();
//				String newPath = originalUrl.replaceFirst(basePath, "");
//				logger.info("Adding original request URL: {}", originalUrl + " : " + newPath);
//				request = request.mutate().header("X-Gateway-Original-Request-Url", originalUrl).build();
//			}
//
//			return chain.filter(exchange.mutate().request(request).build());
//		};
//	}

//	@Override
//    public GatewayFilter apply(Config config) {
//        return (exchange, chain) -> {
//            Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR); 
//            PathMatchInfo variables = exchange.getAttribute(ServerWebExchangeUtils.URI_TEMPLATE_VARIABLES_ATTRIBUTE); // 获取路径变量。
//            if ((variables == null) || (route == null)) {
//                return chain.filter(exchange);
//            }
//            Map<String, String> uriVariables = variables.getUriVariables();
//            URI uri = route.getUri();
//            String host = uri.getHost();
//            if ((host != null) && uriVariables.containsKey(host)) { 
//                host = uriVariables.get(host);
//            }
//            if (host == null) {
//                return chain.filter(exchange);
//            }
//            URI newUri = UriComponentsBuilder.fromUri(uri).host(host).build().toUri();
//            Route newRoute = Route.builder()
//                    .id(route.getId())
//                    .uri(newUri)
//                    .order(route.getOrder())
//                    .predicate(route.getPredicate())
//                    .filters(route.getFilters())
//                    .build(); // 重构路由。
//            exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR, newRoute);
//            return chain.filter(exchange);
//        };
//    }

	@Override
	public GatewayFilter apply(Config config) {
//		return new StatePrinterGatewayFilter(reactiveResilience4JCircuitBreakerFactory);
		return (exchange, chain) -> {
            ServerHttpRequest req = exchange.getRequest();
            String path = req.getURI().getRawPath();
            String newPath = path.replaceFirst(basePath, "");

            ServerHttpRequest request = req.mutate().path(newPath).contextPath("").build();

            return chain.filter(exchange.mutate().request(request).build());
        };
	}

	public static class Config {
		// Put the configuration properties
		public Config() {

		}

		public Config(int order) {
			this.order = order;
		}

		private int order;

		public int getOrder() {
			return order;
		}

		public void setOrder(int order) {
			this.order = order;
		}
	}
}
