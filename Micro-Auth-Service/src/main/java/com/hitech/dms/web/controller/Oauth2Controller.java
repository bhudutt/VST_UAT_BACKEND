/**
 * 
 */
package com.hitech.dms.web.controller;

import static java.util.Arrays.asList;

import java.math.BigInteger;
import java.security.Principal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.approval.Approval;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.filter.request.HttpRequestResponseUtils;
import com.hitech.dms.web.model.user.accesslog.request.AccessLogUpdateRequestModel;
import com.hitech.dms.web.service.user.UserService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author dinesh.jakhar
 *
 */
@RestController
@RequestMapping("/oauth")
@Slf4j
@Validated
public class Oauth2Controller {

	@Autowired
	private TokenStore tokenStore;
	@Autowired
	private ApprovalStore approvalStore;
	@Autowired
	private UserService userservice;
	@Autowired
	@Qualifier(value = "jdbcClientDtlService")
	private JdbcClientDetailsService clientDetailsService;

	@Resource(name = "tokenServices")
	ConsumerTokenServices tokenServices;

//	@RequestMapping(value = "/revoke-token", method = RequestMethod.DELETE)
//	@ResponseStatus(HttpStatus.OK)
//	public void logout(HttpServletRequest request) {
//		String authHeader = request.getHeader("Authorization");
//		if (authHeader != null) {
//			String tokenValue = authHeader.replace("Bearer", "").trim();
//			OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
//			tokenStore.removeAccessToken(accessToken);
//		}
//	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/revoke-token")
	@ResponseStatus(HttpStatus.OK)
	public void revokeToken(HttpServletRequest request, OAuth2Authentication authentication) {
		String authorization = request.getHeader("Authorization");
		if (authorization != null && authorization.contains("Bearer")) {
			String tokenId = authorization.substring("Bearer".length() + 1);
			OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenId);
			if (accessToken != null) {
				BigInteger userLoginAccessId = (BigInteger) accessToken.getAdditionalInformation()
						.get("userLoginAccessId");
				String userCode = (String) accessToken.getAdditionalInformation().get("user_code");
				tokenServices.revokeToken(tokenId);
				AccessLogUpdateRequestModel requestModel = new AccessLogUpdateRequestModel();
				requestModel.setUserLoginId(userLoginAccessId);
				requestModel.setLogoutType("Normal");
				requestModel.setLogoutIp(HttpRequestResponseUtils.getClientIpAddress());
				boolean isDone = CompletableFuture.completedFuture(userservice.updateAccessLog(userCode, requestModel))
						.isDone();
				if (isDone) {
					requestModel = null;
				}
			} else {
//				throw new InvalidTokenException("Invalid access token: " + tokenId);
			}
		}
	}

	@GetMapping(value = "/fetchAllActiveUserTokens")
	public ResponseEntity<List<String>> allTokens(@RequestParam String clientId) {
		Collection<OAuth2AccessToken> tokens = tokenStore.findTokensByClientId(clientId);
		List<String> tokenValues = tokens.stream().map(OAuth2AccessToken::getValue).collect(Collectors.toList());
		return ResponseEntity.ok(tokenValues);
	}

	@PostMapping(value = "/revokeAllUsers")
	public ResponseEntity<String> revokeAllUsers(@RequestParam String clientId, @RequestParam String isValid) {
		if (isValid != null && isValid.equals("$Dnsh87$")) {
			Collection<OAuth2AccessToken> tokens = tokenStore.findTokensByClientId(clientId);
//			long count = tokens.stream().peek(tokenStore::removeAccessToken).count();
			for(OAuth2AccessToken accessToken : tokens) {
				tokenStore.removeAccessToken(accessToken);
			}
			return ResponseEntity.ok("All Users Revoked.");
		}
		return ResponseEntity.ok("Something Wrong Happened.");
	}

	@PostMapping(value = "/approval/revoke")
	public void revokeApproval(@RequestBody Approval approval) {
		approvalStore.revokeApprovals(asList(approval));
		tokenStore.findTokensByClientIdAndUserName(approval.getClientId(), approval.getUserId())
				.forEach(tokenStore::removeAccessToken);
	}

	@GetMapping(value = "/fetchApprovals")
	public ResponseEntity<Map<String, Object>> fetchApprovals(OAuth2Authentication authentication,
			Principal principal) {
		List<Approval> approvals = clientDetailsService.listClientDetails().stream()
				.map(clientDetail -> approvalStore.getApprovals(principal.getName(), clientDetail.getClientId()))
				.flatMap(Collection::stream).collect(Collectors.toList());
		Map<String, Object> mapData = new HashMap<String, Object>();
		mapData.put("approvals", approvals);
		mapData.put("clientDetails", clientDetailsService.listClientDetails());
		return ResponseEntity.ok(mapData);
	}

	@GetMapping(value = "/longLifeTokenExist")
	public ResponseEntity<Map<String, Object>> longLifeTokenExist(@RequestParam String clientId,
			@RequestParam String userId) {
		Collection<OAuth2AccessToken> existingTokens = tokenStore.findTokensByClientIdAndUserName(clientId, userId);
		Map<String, Object> mapData = new HashMap<String, Object>();
		if (existingTokens == null || existingTokens.isEmpty()) {
			return ResponseEntity.ok(mapData);
		}
		for (OAuth2AccessToken token : existingTokens) {
			mapData.put("Value", token.getValue());
			mapData.put("Info", token.getAdditionalInformation());
			mapData.put("Exp. Time", token.getExpiration().toGMTString());

		}

		return ResponseEntity.ok(mapData);
	}
}
