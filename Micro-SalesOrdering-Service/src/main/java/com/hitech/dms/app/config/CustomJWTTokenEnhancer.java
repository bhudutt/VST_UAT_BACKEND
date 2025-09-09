/**
 * 
 */
package com.hitech.dms.app.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import com.hitech.dms.web.entity.user.UserEntity;

/**
 * @author dnsh87
 *
 */
public class CustomJWTTokenEnhancer extends JwtAccessTokenConverter {
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		UserEntity user = (UserEntity) authentication.getPrincipal();

		Map<String, Object> info = new LinkedHashMap<String, Object>(accessToken.getAdditionalInformation());

		info.put("Authorities", user.getAuthorities());

		DefaultOAuth2AccessToken customAccessToken = new DefaultOAuth2AccessToken(accessToken);
		customAccessToken.setAdditionalInformation(info);

		return super.enhance(customAccessToken, authentication);
	}
}
