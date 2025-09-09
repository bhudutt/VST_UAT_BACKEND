/**
 * 
 */
package com.hitech.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author dinesh.jakhar
 *
 */
@RestController
public class FallbackController {
	@RequestMapping("/authFailed")
    public Mono<String> authFailed() {
        return Mono.just("Auth Service is taking too long to respond or is down. Please try again later");
    }
	@RequestMapping("/orderFallBack")
    public Mono<String> orderServiceFallBack() {
        return Mono.just("Order Service is taking too long to respond or is down. Please try again later");
    }
    @RequestMapping("/paymentFallback")
    public Mono<String> paymentServiceFallBack() {
        return Mono.just("Payment Service is taking too long to respond or is down. Please try again later");
    }
}
