/**
 * 
 */
package com.hitech.filters;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
/**
 * @author dinesh.jakhar
 *
 */
//@Component
public class StripBasePathGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

//    @Value("${spring.webflux.base-path}")
    private String basePath;

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            ServerHttpRequest req = exchange.getRequest();
            String path = req.getURI().getRawPath();
            String newPath = path.replaceFirst(basePath, "");

            ServerHttpRequest request = req.mutate().path(newPath).contextPath(null).build();

            return chain.filter(exchange.mutate().request(request).build());
        };
    }
}
