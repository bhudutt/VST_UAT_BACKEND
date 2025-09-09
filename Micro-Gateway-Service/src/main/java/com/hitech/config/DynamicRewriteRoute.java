package com.hitech.config;

import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;

//@Configuration
public class DynamicRewriteRoute {
    
    @Value("${rewrite.backend.uri}")
    private String backendUri;
    private static Random rnd = new Random();
    
    @Bean
    public RouteLocator dynamicZipCodeRoute(RouteLocatorBuilder builder) {
        return builder.routes()
          .route("dynamicRewrite", r ->
             r.path("/auth-api/**")
              .filters(f -> f.filter((exchange, chain) -> {
                  ServerHttpRequest req = exchange.getRequest();
                  ServerWebExchangeUtils.addOriginalRequestUrl(exchange, req.getURI());
                  String path = req.getURI().getRawPath();
                  String newPath = path.replaceAll(
                    "/auth-api/(?.*)", 
                    "/auth-api/oauth/token");
                  ServerHttpRequest request = req.mutate().path(newPath).build();
                  exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR, request.getURI());
                  return chain.filter(exchange.mutate().request(request).build());
              }))
              .uri(backendUri))
          .build();
    }
}
