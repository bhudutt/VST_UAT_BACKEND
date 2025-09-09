/**
 * 
 */
package com.hitech.dms.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * @author dinesh.jakhar
 *
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

	private static final String RESOURCE_ID = "resource-server-ecat-api";
	private static final String SECURED_READ_SCOPE = "#oauth2.hasScope('READ')";
	private static final String SECURED_WRITE_SCOPE = "#oauth2.hasScope('WRITE')";
	private static final String SECURED_PATTERN = "/**";

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		resources.resourceId(RESOURCE_ID);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable()
//                .sessionManagement().disable()
//                .authorizeRequests()
////                .antMatchers("/enquiry/list").permitAll()
//                .and()
//                .requestMatchers()                
//                .antMatchers(SECURED_PATTERN).and().authorizeRequests()
//                .antMatchers(HttpMethod.POST, SECURED_PATTERN)
//                .access(SECURED_WRITE_SCOPE)
//                .anyRequest().access(SECURED_READ_SCOPE);
		http.authorizeRequests()
		.antMatchers("/swagger-uicustom.html","/swagger-resources/**", "/swagger-ui/**", "/v3/api-docs/**", "/user/forgotPassword/**").permitAll();
//        .antMatchers("/swagger-ui/**", "/presaleActivity-openapi/**").permitAll();
		http.authorizeRequests().antMatchers(HttpMethod.GET, SECURED_PATTERN).access("#oauth2.hasScope('read')")
				.antMatchers(HttpMethod.POST, SECURED_PATTERN).access("#oauth2.hasScope('write')")
				.antMatchers(HttpMethod.PATCH, SECURED_PATTERN).access("#oauth2.hasScope('write')")
				.antMatchers(HttpMethod.PUT, SECURED_PATTERN).access("#oauth2.hasScope('write')")
				.antMatchers(HttpMethod.DELETE, SECURED_PATTERN).access("#oauth2.hasScope('write')");
	}
}
