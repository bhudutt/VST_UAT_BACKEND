/**
 * 
 */
package com.hitech.dms.web.controller.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.web.service.email.EmailService;

/**
 * @author santosh.kumar
 *
 */
@RestController
@RequestMapping("vst/email")
public class EmailController {

	@Autowired
	private EmailService emailService;
	

	/**
	 * 
	 * @param files
	 * @param to
	 * @param cc
	 * @param bcc
	 * @param subject
	 * @param body
	 * @return
	 */
	@PostMapping("/send")
	public String sendMail(@RequestParam(value = "file", required = false) MultipartFile[] files,
			@RequestParam String from,
			@RequestParam String[] to, @RequestParam(required = false) String[] cc,
			@RequestParam(required = false) String[] bcc, @RequestParam String subject, @RequestParam String body) {
		return emailService.sendMail(files,from, to, cc, bcc, subject, body);
	}

}
