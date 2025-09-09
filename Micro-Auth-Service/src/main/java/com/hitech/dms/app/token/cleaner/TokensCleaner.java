/**
 * 
 */
package com.hitech.dms.app.token.cleaner;

import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;

/**
 * @author dinesh.jakhar
 *
 */
@Component
@Order(10)
public class TokensCleaner {
	private static final Logger LOGGER = LoggerFactory.getLogger(TokensCleaner.class);

	private TokenStore tokenStore;

	@Inject
	public TokensCleaner(@Named("tokenStore") TokenStore tokenStore) {
		this.tokenStore = tokenStore;
	}

	@Scheduled(cron = "0 0/30 * * * ?")
	public void execute() {
//		Date expirationTime = new Date();
		Collection<OAuth2AccessToken> allTokens = tokenStore.findTokensByClientId("vst-dms-view-client");
		long removedTokens = allTokens.stream().filter(OAuth2AccessToken::isExpired).peek(tokenStore::removeAccessToken)
				.count();
		if(LOGGER.isInfoEnabled()) {
			LOGGER.info("PURGING  ", removedTokens);
		}
	}
}
