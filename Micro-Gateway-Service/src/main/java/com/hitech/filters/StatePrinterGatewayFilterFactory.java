/**
 * 
 */
package com.hitech.filters;

import java.net.URI;
import java.util.LinkedHashSet;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

/**
 * @author dinesh.jakhar
 *
 */
@Component
public class StatePrinterGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {
	private static final Logger logger = LoggerFactory.getLogger(StatePrinterGatewayFilterFactory.class);
	
//	@Value("${spring.webflux.base-path}")
    private String basePath;
	
	@Autowired
	ReactiveResilience4JCircuitBreakerFactory reactiveResilience4JCircuitBreakerFactory;

	@Override
	public String name() {
		return "CircuitBreakerStatePrinter";
	}
	
	@Override
	public GatewayFilter apply(Object config) {
		return new StatePrinterGatewayFilter(reactiveResilience4JCircuitBreakerFactory);
	}

//	@Override
//	public GatewayFilter apply(Object config) {
////		return new StatePrinterGatewayFilter(reactiveResilience4JCircuitBreakerFactory);
//		return (exchange, chain) -> {
//            ServerHttpRequest req = exchange.getRequest();
//            String path = req.getURI().getRawPath();
//            String newPath = path.replaceFirst(basePath, "");
//
//            ServerHttpRequest request = req.mutate().path(newPath).contextPath(null).build();
//
//            return chain.filter(exchange.mutate().request(request).build());
//        };
//	}
}
