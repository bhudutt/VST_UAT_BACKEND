package com.hitech.dms.app.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.mobile.device.DeviceHandlerMethodArgumentResolver;
import org.springframework.mobile.device.DeviceResolverHandlerInterceptor;
import org.springframework.util.unit.DataSize;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.hitech.dms.app.interceptor.AppLogger;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	
	@Autowired
	private AppLogger appLogger;

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
		registry.addInterceptor(appLogger);
		
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

	@Value("${spring.servlet.multipart.max-file-size}")
	String maxFileSize;
	@Value("${spring.servlet.multipart.max-request-size}")
	String maxRequestSize;

	@Bean
	public CommonsMultipartResolver commonsMultipartResolver() {
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
		commonsMultipartResolver.setMaxUploadSize(DataSize.parse(maxRequestSize).toBytes());
		commonsMultipartResolver.setMaxUploadSizePerFile(DataSize.parse(maxFileSize).toBytes());
		return commonsMultipartResolver;
	}
	
//	@Override
//	public MessageCodesResolver getMessageCodesResolver() {
//		return new DefaultMessageCodesResolver() {
//			/**
//			 * 
//			 */
//			private static final long serialVersionUID = -3061483283551916629L;
//
//			@Override
//			public String[] resolveMessageCodes(String errorCode, String objectName) {
//				return new String[] {"custom." + errorCode};
//			}
//		};
//	}
	
}
