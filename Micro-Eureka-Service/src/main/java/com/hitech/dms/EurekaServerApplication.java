/**
 * 
 */
package com.hitech.dms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author dinesh.jakhar
 *
 */
@SpringBootApplication
//Enable eureka server
@EnableEurekaServer 
public class EurekaServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(EurekaServerApplication.class, args);
		System.out.println("Eureka Server Started....!!");
	}
}
