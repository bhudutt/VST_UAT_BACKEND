/**
 * 
 */
package com.hitech.config;

import java.time.Duration;

import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.filter.factory.FallbackHeadersGatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;

/**
 * @author dinesh.jakhar
 *
 */
//@Configuration
public class Resilience4JConfiguration {

	/**
	 * Default Resilience4j circuit breaker configuration
	 */
//	@Bean
//	public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
////		return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
////				.circuitBreakerConfig(
////						CircuitBreakerConfig.custom().minimumNumberOfCalls(5).failureRateThreshold(20).build())
////				.build());
//		return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
//	            .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
//	            .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(50)).build()).build());
//	}

//	@Bean
	public ReactiveResilience4JCircuitBreakerFactory defaultCustomizer() {

		CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom() //
				.slidingWindowType(CircuitBreakerConfig.SlidingWindowType.TIME_BASED) // The type of sliding window is
																						// time window
				.slidingWindowSize(10) // The size of the time window is 60 second
				.minimumNumberOfCalls(5) // At least... Is required in the unit time window 5 The statistical
											// calculation can only be started after one call
				.failureRateThreshold(50) // The call failure rate in the unit time window reaches 50% The circuit
											// breaker will be activated after
				.enableAutomaticTransitionFromOpenToHalfOpen() // Allow the circuit breaker to automatically change from
																// open state to half open state
				.permittedNumberOfCallsInHalfOpenState(5) // The number of normal calls allowed in the half open state
				.waitDurationInOpenState(Duration.ofSeconds(5)) // It is necessary to wait for the circuit breaker to
																// change from open state to half open state 60 second
				.recordExceptions(Throwable.class) // All exceptions are treated as failures
				.build();
		ReactiveResilience4JCircuitBreakerFactory factory = new ReactiveResilience4JCircuitBreakerFactory();
		factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
				.timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofMillis(600)).build())
				.circuitBreakerConfig(circuitBreakerConfig).build());
		return factory;
	}

//	@Bean
//	public Customizer<ReactiveResilience4JCircuitBreakerFactory> authCircuitBreaker() {
//		return factory -> {
//			factory.configure(builder -> builder.circuitBreakerConfig(CircuitBreakerConfig.ofDefaults()).build()
//					.setCircuitBreakerConfig(
//							CircuitBreakerConfig.custom().minimumNumberOfCalls(10).failureRateThreshold(20).build()),
//					"fallbackCommand");
//		};
//	}
//
//	@Bean
//	public Customizer<ReactiveResilience4JCircuitBreakerFactory> accountCircuitBreaker() {
//		return factory -> {
//			factory.configure(builder -> builder.circuitBreakerConfig(CircuitBreakerConfig.ofDefaults()).build(),
//					"accountCircuitBreaker");
//		};
//	}

	@Bean
	public FallbackHeadersGatewayFilterFactory fallbackHeadersGatewayFilterFactory() {
		return new FallbackHeadersGatewayFilterFactory();
	}
}
