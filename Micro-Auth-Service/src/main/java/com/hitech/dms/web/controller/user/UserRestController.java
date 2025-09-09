package com.hitech.dms.web.controller.user;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.app.errors.EntityNotFoundException;
import com.hitech.dms.web.entity.response.user.UserRestEntity;
import com.hitech.dms.web.service.user.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/users")
@Slf4j
@Validated
public class UserRestController {
	@Autowired
	private Mapper mapper;

	@Autowired
	private UserService userService;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@GetMapping("/findByUsername")
	@PreAuthorize("!hasAuthority('USER') || (authentication.principal == #email)")
	public ResponseEntity<?> findByUsername(@RequestParam String username, OAuth2Authentication authentication) {
		UserRestEntity userEntity = userService.findByUserName(username);
		if (userEntity == null) {
			throw new EntityNotFoundException(UserRestEntity.class, "username", username);
		}
		return ResponseEntity.ok(userEntity);
	}

	@GetMapping("/userDetailByUsername")
	@PreAuthorize("!hasAuthority('USER') || (authentication.principal == #email)")
	public ResponseEntity<?> userDetailByUsername(@RequestParam String username, OAuth2Authentication authentication) {
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		UserRestEntity userEntity = userService.findByUserName(username);
		if (userEntity != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch User Detail By User Name on " + formatter.format(new Date()));
			codeResponse.setMessage("User Detail By User Name Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("User Detail By User Name Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(userEntity);
		return ResponseEntity.ok(userAuthResponse);
	}

}
