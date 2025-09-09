/**
 * 
 */
package com.hitech.dms.app.security.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author dinesh.jakhar
 *
 */
@Component
@ConfigurationProperties(prefix = "security.user")
public class MsecretKey {
	 private String secretKey;

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
}
