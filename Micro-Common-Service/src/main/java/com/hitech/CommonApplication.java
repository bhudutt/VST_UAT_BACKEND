/**
 * 
 */
package com.hitech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import com.hitech.dms.app.config.FileStorageProperties;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

/**
 * @author dinesh.jakhar
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigurationProperties({
	FileStorageProperties.class
})
@OpenAPIDefinition(info =
@Info(title = "The Hi-tech Robotics Systemz ltd. API", version = "${springdoc.version}", description = "Documentation The Hi-tech Robotics Systemz ltd. API v1.0")
)
@SecurityScheme(name = "hitechApis", scheme = "bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
public class CommonApplication {
	public static void main(String[] args) {
		SpringApplication.run(CommonApplication.class, args);
		System.out.println("Common service ruuning....!");
	}
}
