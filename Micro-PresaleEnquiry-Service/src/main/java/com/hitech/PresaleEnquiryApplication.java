/**
 * 
 */
package com.hitech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import com.hitech.dms.app.config.FileStorageProperties;
import com.hitech.dms.app.validatior.AppBindingResultValidator;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

/**
 * @author dinesh.jakhar
 *
 */
@EnableFeignClients
@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigurationProperties({
	FileStorageProperties.class
})
@OpenAPIDefinition(info =
@Info(title = "The Hi-tech Robotics Systemz ltd. API", version = "${springdoc.version}", description = "Documentation The Hi-tech Robotics Systemz ltd. API v1.0")
)
@SecurityScheme(name = "hitechApis", scheme = "bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
public class PresaleEnquiryApplication {
	public static void main(String[] args) {
		SpringApplication.run(PresaleEnquiryApplication.class, args);
		System.out.println("Sales Enquiry service ruuning....!");
	}

	@Bean
	public AppBindingResultValidator getBindingResultValidator() {
		return new AppBindingResultValidator();
	}
}
