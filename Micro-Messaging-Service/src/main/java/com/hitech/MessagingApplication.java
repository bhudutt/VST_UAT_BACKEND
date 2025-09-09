/**
 * 
 */
package com.hitech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

/**
 * @author dinesh.jakhar
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
//@EnableScheduling
@EnableAsync
public class MessagingApplication {
	public static void main(String[] args) {
		SpringApplication.run(MessagingApplication.class, args);
		System.out.println("Messaging service ruunning...!");
	}
	
	@Bean
    public FreeMarkerConfigurationFactoryBean getFreeMarkerConfiguration() {
        FreeMarkerConfigurationFactoryBean fmConfigFactoryBean = new FreeMarkerConfigurationFactoryBean();
        fmConfigFactoryBean.setTemplateLoaderPath("classpath:templates");
        return fmConfigFactoryBean;
    }
}
