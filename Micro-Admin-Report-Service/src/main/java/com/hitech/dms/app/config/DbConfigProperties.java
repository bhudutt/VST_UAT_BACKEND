package com.hitech.dms.app.config;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class DbConfigProperties {
	private static final String DATABASE_RESOUCE_URI = "database.properties";
	
	@Bean
	public PropertyPlaceholderConfigurer getPropertyPlaceholderConfigurer() {
		final PropertyPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer();
		ClassPathResource[] resources = new ClassPathResource[] { new ClassPathResource(DATABASE_RESOUCE_URI) };
		propertyPlaceholderConfigurer.setLocations(resources);
		propertyPlaceholderConfigurer.setIgnoreUnresolvablePlaceholders(true);
		return propertyPlaceholderConfigurer;
	}
}
