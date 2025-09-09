package com.hitech.dms.app.config;

import java.util.Arrays;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

//@Configuration
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	DataSource ds;

	@Autowired
	AuthenticationManager authMgr;

	@Autowired
	private UserDetailsService usrSvc;

	@Bean
	public TokenStore tokenStore() {
//		return new JdbcTokenStore(ds);
		return new JwtTokenStore(accessTokenConverter());
	}

	@Bean("clientPasswordEncoder")
	PasswordEncoder clientPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public ApprovalStore approvalStore() {
		return new JdbcApprovalStore(ds);
	}

	@Bean
	public TokenEnhancer tokenEnhancer() {
		return new CustomTokenEnhancer();
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer cfg) throws Exception {

		// This will enable /oauth/check_token access
		cfg.checkTokenAccess("permitAll").checkTokenAccess("isAuthenticated()");

		// BCryptPasswordEncoder(4) is used for oauth_client_details.user_secret
		cfg.passwordEncoder(clientPasswordEncoder());
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.jdbc(ds);
//		clients.jdbc(ds).passwordEncoder(clientPasswordEncoder());
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		final TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer()));
		endpoints.tokenStore(tokenStore()).tokenEnhancer(accessTokenConverter());//.accessTokenConverter(accessTokenConverter());
		endpoints.authenticationManager(authMgr);
		endpoints.userDetailsService(usrSvc);
	}

	@Bean
	@Primary
	public DefaultTokenServices tokenServices() {
		final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
		defaultTokenServices.setTokenStore(tokenStore());
		defaultTokenServices.setSupportRefreshToken(true);
		return defaultTokenServices;
	}
	
	@Bean
    JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("123rft");
        return converter;
    }
}