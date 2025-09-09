package com.hitech.dms.app.controller.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.annotation.AccessLimit;
import com.hitech.dms.app.annotation.ApiIdempotent;
import com.hitech.dms.app.common.ServerResponse;
import com.hitech.dms.app.entity.Mail;
import com.hitech.dms.app.service.mail.MailService;
import com.hitech.dms.app.util.MailUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/test")
@Slf4j
public class MailController {

	@Autowired
	private MailService mailService;

	@Autowired
	private MailUtil mailUtil;

	@ApiIdempotent
	@PostMapping("testIdempotence")
	public ServerResponse testIdempotence() {
		return mailService.testIdempotence();
	}

	@AccessLimit(maxCount = 5, seconds = 5)
	@PostMapping("accessLimit")
	public ServerResponse accessLimit() {
		return mailService.accessLimit();
	}

	@PostMapping("send")
	public ServerResponse sendMail(@Validated Mail mail, Errors errors) {
		if (errors.hasErrors()) {
			String msg = errors.getFieldError().getDefaultMessage();
			return ServerResponse.error(msg);
		}

		return mailService.send(mail, null);
	}

	@PostMapping("sendMail")
	public ServerResponse sendMail(@RequestBody Mail mail) {
		Mail build = Mail.builder().to(mail.getTo()).title(mail.getTitle()).content(mail.getContent()).build();
		boolean send = mailUtil.send(build);
		return ServerResponse.success(send);
	}

//	public static ThreadLocal<UserDto> userDtoThreadLocal = new ThreadLocal<>();
//
//	public static void main(String[] args) {
//		userDtoThreadLocal.set(UserDto.builder().build());
//	}

}
