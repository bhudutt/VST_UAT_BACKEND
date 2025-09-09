package com.hitech.dms.app.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.mobile.device.DeviceHandlerMethodArgumentResolver;
import org.springframework.mobile.device.DeviceResolverHandlerInterceptor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public DeviceResolverHandlerInterceptor deviceResolverHandlerInterceptor() { 
        return new DeviceResolverHandlerInterceptor(); 
    }

    @Bean
    public DeviceHandlerMethodArgumentResolver deviceHandlerMethodArgumentResolver() { 
        return new DeviceHandlerMethodArgumentResolver(); 
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) { 
        registry.addInterceptor(deviceResolverHandlerInterceptor()); 
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(deviceHandlerMethodArgumentResolver()); 
    }

	@Bean
	public ReloadableResourceBundleMessageSource messageSource() {
		ReloadableResourceBundleMessageSource resource = new ReloadableResourceBundleMessageSource();
		resource.setBasenames("classpath:messages", "classpath:validation");
		return resource;
	}
}
