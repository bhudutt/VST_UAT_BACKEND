package com.hitech.dms.app.config;

import java.util.Arrays;
import java.util.Map;

import org.dozer.inject.Inject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.hitech.dms.app.security.provider.CustomAuthenticationProvider;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class UserSecurityConfig extends WebSecurityConfigurerAdapter {

//	@Autowired
//	DataSource ds;
	@Inject
	private ApplicationEventPublisher applicationEventPublisher;
	@Autowired
	private UserDetailsService userDetailsService;

	@Value("${security.signing-key}")
	private String signingKey;

	@Value("${security.encoding-strength}")
	private Integer encodingStrength;

	@Value("${security.security-realm}")
	private String securityRealm;

	@Autowired
	private JdbcTemplate jdbcTemplate;

//	@Override
//	@Bean(BeanIds.USER_DETAILS_SERVICE)
//	public UserDetailsService userDetailsServiceBean() throws Exception {
//		return super.userDetailsServiceBean();
//	}
//
//	@Override
//	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
//	public AuthenticationManager authenticationManagerBean() throws Exception {
//		return super.authenticationManagerBean();
//	}

	@Bean("userPasswordEncoder")
	@Primary
	PasswordEncoder userPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().httpBasic()
				.realmName(securityRealm).and().csrf().disable();

	}

//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//
//		// BCryptPasswordEncoder(4) is used for users.password column
////    JdbcUserDetailsManagerConfigurer<AuthenticationManagerBuilder> cfg = auth.jdbcAuthentication()
////        .passwordEncoder(userPasswordEncoder()).dataSource(ds);
////
////    cfg.getUserDetailsService().setEnableGroups(true);
////    cfg.getUserDetailsService().setEnableAuthorities(false);
//
//	}
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authProvider());
	}

	@Override
	public void configure(final WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**", "/css/**", "/js/**", "/images/**", "/auth/**", "/notification/**");
	}

	@Bean
	@Lazy
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public DaoAuthenticationProvider authProvider() {
		final CustomAuthenticationProvider authProvider = new CustomAuthenticationProvider();
		authProvider.setUserDetailsService(this.userDetailsService);
		authProvider.setPasswordEncoder(userPasswordEncoder());
		return authProvider;
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setSigningKey(signingKey);
		converter.setAccessTokenConverter(authExtractor());
		return converter;
	}
	
	@Bean
    public DefaultAccessTokenConverter authExtractor() {
        return new DefaultAccessTokenConverter() {
            @Override
            public OAuth2Authentication extractAuthentication(Map<String, ?> claims) {
                OAuth2Authentication authentication = super.extractAuthentication(claims);
                authentication.setDetails(claims);
                return authentication;
            }
        };
    }

	@Bean
	public TokenStore tokenStore() {
		return new JdbcTokenStore(jdbcTemplate.getDataSource());
	}

	@Bean
	@Primary
	// Making this primary to avoid any accidental duplication with another token
	// service instance of the same name
	public DefaultTokenServices tokenServices() {
		DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
		defaultTokenServices.setTokenStore(tokenStore());
		defaultTokenServices.setSupportRefreshToken(true);
		return defaultTokenServices;
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE"));
		configuration.setAllowedHeaders(Arrays.asList("Accept", "Origin", "Content-Type", "Depth", "User-Agent",
				"If-Modified-Since,", "Cache-Control", "Authorization", "X-Req", "X-File-Size", "X-Requested-With",
				"X-File-Name", "X-CROSSORIGIN-AUTH-USR", "X-CROSSORIGIN-AUTH-PSW", "X-CROSSORIGIN-CNHI"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
