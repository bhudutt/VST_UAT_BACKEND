/**
 * 
 */
package com.hitech;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;

/**
 * @author dinesh.jakhar
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableHystrix
//@RibbonClients(defaultConfiguration = RibbonEurekaClientConfig.class)
public class ZuulApiGetWayApplication {
	public static void main(String[] args) {
//		System.setProperty("server.servlet.context-path", "/gateway");
		SpringApplication.run(ZuulApiGetWayApplication.class, args);
		System.out.println("Zuul server is started...");
	}

//	@Bean
//	public WebFluxProperties webFluxProperties(){
//		return new WebFluxProperties(); 
//	}

//	@Bean
//	public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory>
//	  webServerFactoryCustomizer() {
//	    return factory -> factory.setContextPath("/gateway");
//	}

//	@Autowired
//    private TokenRelayGatewayFilterFactory filterFactory;
//
//    @Bean
//    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
//        return builder.routes()
//                .route("resource2", r -> r.path("/calledForm")115.113.186.122
//                        .filters(f -> f.filters(filterFactory.apply())
//                                .removeRequestHeader("Cookie")) // Prevents cookie being sent downstream
//                        .uri("http://resource:9000"))
//                .build();
//    }

//	@Bean
//	public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
//		return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
////				.circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
//				.circuitBreakerConfig(
//						CircuitBreakerConfig.custom().slidingWindowSize(5).permittedNumberOfCallsInHalfOpenState(5)
//								.failureRateThreshold(50.0F).waitDurationInOpenState(Duration.ofMillis(30))
////                        .slowCallDurationThreshold(Duration.ofMillis(200))
////                        .slowCallRateThreshold(50.0F)
//								.build())
//				.timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofMillis(2000)).build())
//				.build());
//	}

	@Bean
	public ReactiveResilience4JCircuitBreakerFactory reactiveResilience4JCircuitBreakerFactory(
			final CircuitBreakerRegistry circuitBreakerRegistry, final TimeLimiterRegistry timeLimiterRegistry) {
		ReactiveResilience4JCircuitBreakerFactory reactiveResilience4JCircuitBreakerFactory = new ReactiveResilience4JCircuitBreakerFactory();
		reactiveResilience4JCircuitBreakerFactory.configureCircuitBreakerRegistry(circuitBreakerRegistry);
		reactiveResilience4JCircuitBreakerFactory.configureDefault(id -> {
			CircuitBreakerConfig circuitBreakerConfig = circuitBreakerRegistry.find(id).isPresent()
					? circuitBreakerRegistry.find(id).get().getCircuitBreakerConfig()
					: circuitBreakerRegistry.getDefaultConfig();
			TimeLimiterConfig timeLimiterConfig = timeLimiterRegistry.find(id).isPresent()
					? timeLimiterRegistry.find(id).get().getTimeLimiterConfig()
					: timeLimiterRegistry.getDefaultConfig();

			return new Resilience4JConfigBuilder(id).circuitBreakerConfig(circuitBreakerConfig)
					.timeLimiterConfig(timeLimiterConfig).build();
		});
		return reactiveResilience4JCircuitBreakerFactory;
	}

	@Bean
	public CorsWebFilter corsWebFilter() {

		final CorsConfiguration corsConfig = new CorsConfiguration();
		corsConfig.setAllowedOrigins(Collections.singletonList("*"));
		corsConfig.setMaxAge(3600L);
		corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE"));
		corsConfig.addAllowedHeader("*");

		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfig);

		return new CorsWebFilter(source);
	}

}
