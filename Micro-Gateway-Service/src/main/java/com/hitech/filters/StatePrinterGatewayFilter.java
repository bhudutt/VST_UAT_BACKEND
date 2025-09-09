/**
 * 
 */
package com.hitech.filters;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.vavr.collection.Seq;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;

/**
 * @author dinesh.jakhar
 *
 */
public class StatePrinterGatewayFilter implements GatewayFilter {

	private ReactiveResilience4JCircuitBreakerFactory reactiveResilience4JCircuitBreakerFactory;

	// Get an instance of reactiveResilience4JCircuitBreakerFactory through the
	// construction method
	public StatePrinterGatewayFilter(
			ReactiveResilience4JCircuitBreakerFactory reactiveResilience4JCircuitBreakerFactory) {
		this.reactiveResilience4JCircuitBreakerFactory = reactiveResilience4JCircuitBreakerFactory;
	}

	private CircuitBreaker circuitBreaker = null;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		// Concurrency is not considered here. If it is a production environment, please
		// add the locking logic by yourself
		if (null == circuitBreaker) {
			CircuitBreakerRegistry circuitBreakerRegistry = null;
			try {
				Method method = reactiveResilience4JCircuitBreakerFactory.getClass()
						.getDeclaredMethod("getCircuitBreakerRegistry", (Class[]) null);
				// Make the getCircuitBreakerRegistry method accessible using reflection
				method.setAccessible(true);
				// Execute the getCircuitBreakerRegistry method with reflection to get
				// circuitBreakerRegistry
				circuitBreakerRegistry = (CircuitBreakerRegistry) method
						.invoke(reactiveResilience4JCircuitBreakerFactory);
			} catch (Exception exception) {
				exception.printStackTrace();
			}

			// get all circuit breaker instances
			Seq<CircuitBreaker> seq = circuitBreakerRegistry.getAllCircuitBreakers();
			// Filter by name, authFallbackCommand comes from the routing configuration
			circuitBreaker = seq.filter(breaker -> breaker.getName().equals("authFallbackCommand")).getOrNull();
		}

		// Get the circuit breaker status, and then judge it empty again, because the
		// above operation may not be able to get the circuitBreaker
		String state = (null == circuitBreaker) ? "unknown" : circuitBreaker.getState().name();

		System.out.println("state : " + state);

		// Continue to execute the following logic
		return chain.filter(exchange);
	}

//    @Override
//    public int getOrder() {
//        return 10;
//    }
}
