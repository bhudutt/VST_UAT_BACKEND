package com.hitech.dms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
@EnableAuthorizationServer
@EnableDiscoveryClient
@EnableScheduling
@EnableAsync
public class VstDmsApplication {
	private static final Logger LOGGER=LoggerFactory.getLogger(VstDmsApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(VstDmsApplication.class, args);
	}

}
