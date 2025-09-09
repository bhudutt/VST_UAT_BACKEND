/**
 * 
 */
package com.hitech.dms.web.controller.permission;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.web.entity.user.UserEntity;
import com.hitech.dms.web.service.permission.ValidatePermissionService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author dinesh.jakhar
 *
 */
@RestController
@RequestMapping("/api/user")
@Slf4j
@Validated
public class ValidatePermissionController {
	private static final Logger logger = LoggerFactory.getLogger(ValidatePermissionController.class);

	@Autowired
	private ValidatePermissionService validatePermissionService;

	@GetMapping("/validateMenuPermission/{code}")
	public ResponseEntity<?> fetchAggregateList(@PathVariable(name = "code") String code,
			OAuth2Authentication authentication) {
		String userCode = null;
		UserEntity user = null;
		if (authentication != null) {
			user = (UserEntity) authentication.getUserAuthentication().getPrincipal();
			userCode = user.getUsername();
		}
		boolean isAccess = validatePermissionService.validateMenuPermission(userCode, code);
		return ResponseEntity.ok(isAccess);
	}

}
