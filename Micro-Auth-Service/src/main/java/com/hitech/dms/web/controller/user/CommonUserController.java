/**
 * 
 */
package com.hitech.dms.web.controller.user;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.web.model.user.User;
import com.hitech.dms.web.service.user.UserService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author dinesh.jakhar
 *
 */
@RestController
@RequestMapping("/api/common")
@Slf4j
@Validated
public class CommonUserController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/validateUserByUserCode")
	@PreAuthorize("!hasAuthority('USER') || (authentication.principal == #email)")
	public ResponseEntity<?> validateUserByUserCode(@RequestBody User user, OAuth2Authentication authentication) {
		Map<String, Object> mapData = userService.validateUserByUserCode(user);
		return ResponseEntity.ok(mapData);
	}
}
