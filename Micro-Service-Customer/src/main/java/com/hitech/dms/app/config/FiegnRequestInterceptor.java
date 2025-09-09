package com.hitech.dms.app.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * 
 * @author dinesh.jakhar
 *
 */
@Component
public class FiegnRequestInterceptor implements RequestInterceptor {
	private static final String ACCEPT_LANGUAGE_HEADER = "Accept-Language";
	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String TOKEN_TYPE = "Bearer";

	@Override
	public void apply(RequestTemplate requestTemplate) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.getDetails() instanceof OAuth2AuthenticationDetails) {
			OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
			requestTemplate.header(AUTHORIZATION_HEADER, String.format("%s %s", TOKEN_TYPE, details.getTokenValue()));
		}
	}

//    @Override
//    public void apply(RequestTemplate requestTemplate) {
//        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        if (requestAttributes == null) {
//            return;
//        }
//        HttpServletRequest request = requestAttributes.getRequest();
//        if (request == null) {
//            return;
//        }
//        String language = request.getHeader(ACCEPT_LANGUAGE_HEADER);
//        if (language == null) {
//            return;
//        }
//        requestTemplate.header(ACCEPT_LANGUAGE_HEADER, language);
//        String authorization = request.getHeader(AUTHORIZATION_HEADER);
//        if (authorization == null) {
//            return;
//        }
//        requestTemplate.header(AUTHORIZATION_HEADER, authorization);
//    }
}
