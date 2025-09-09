/**
 * 
 */
package com.hitech.dms.app.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

/**
 * @author dnsh87
 *
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private TokenStore tokenStore;

	@Autowired
	private JwtAccessTokenConverter accessTokenConverter;

	@Autowired
	private AuthenticationManager authenticationManager;

	// @Autowired
	// private UserDetailsService userDetailsService;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Bean("clientPasswordEncoder")
	PasswordEncoder clientPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer configurer) throws Exception {
		configurer.jdbc(jdbcTemplate.getDataSource());
	}
	
	@Bean(name = "jdbcClientDtlService")
    public JdbcClientDetailsService clientDetailsService() {
        return new JdbcClientDetailsService(jdbcTemplate.getDataSource());
    }
	
	@Bean
	public ApprovalStore approvalStore() {
		return new JdbcApprovalStore(jdbcTemplate.getDataSource());
	}
	
	@Bean
	public TokenEnhancer tokenEnhancer() {
		return new CustomTokenEnhancer();
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		TokenEnhancerChain chain = new TokenEnhancerChain();
        chain.setTokenEnhancers(List.of(tokenEnhancer(), accessTokenConverter));
		endpoints.tokenStore(tokenStore).reuseRefreshTokens(false).tokenEnhancer(chain)
				.authenticationManager(authenticationManager);
		
//		endpoints.tokenStore(tokenStore).reuseRefreshTokens(false).accessTokenConverter(accessTokenConverter)
//		.authenticationManager(authenticationManager);

	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
//		oauthServer.tokenKeyAccess("hasAuthority('ROLE_TRUSTED_CLIENT')")
//				.checkTokenAccess("hasAuthority('ROLE_TRUSTED_CLIENT')");
		// This will enable /oauth/check_token access
		oauthServer.checkTokenAccess("permitAll").checkTokenAccess("isAuthenticated()");

		// BCryptPasswordEncoder(4) is used for oauth_client_details.user_secret
		oauthServer.passwordEncoder(clientPasswordEncoder());
	}
}
