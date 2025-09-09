package com.hitech.dms.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

import com.hitech.dms.app.errors.CustomAccessDeniedHandler;
import com.hitech.dms.app.errors.CustomAuthenticationEntryPoint;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    public ResourceServerConfiguration(CustomAuthenticationEntryPoint customAuthenticationEntryPoint) {
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId("resource-server-ecat-api");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .antMatcher("/api/**")
                .authorizeRequests()
                .antMatchers("/api/menus**").permitAll()
                .antMatchers("/api/menus/**").permitAll()
                .antMatchers("/api/v1/search/jobCard/downloadFile/**").permitAll()
                .antMatchers("/api/signin**").permitAll()
                .antMatchers("/api/signin/**").permitAll()
                .antMatchers("/api/search**").permitAll()
                .antMatchers("/api/search/**").permitAll()
                .antMatchers("/api/common/**").permitAll()
                .antMatchers("/api/**").authenticated()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint).accessDeniedHandler(new CustomAccessDeniedHandler());
    }

}