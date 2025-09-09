/**
 * 
 */
package com.hitech.dms.web.controller.opex.template.download;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.web.dao.opex.template.download.OpexDownloadTemplateDao;
import com.hitech.dms.web.model.opex.template.request.OpexBudgetRequestModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
//@Validated
@RestController
@RequestMapping("opex")
@SecurityRequirement(name = "hitechApis")
public class OpexDownloadTemplateController {
	private static final Logger logger = LoggerFactory.getLogger(OpexDownloadTemplateController.class);
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@Autowired
	private OpexDownloadTemplateDao dao;
	
	@PostMapping("/createTemplate")
	public ResponseEntity<InputStreamResource> createTemplate(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody OpexBudgetRequestModel requestModel, OAuth2Authentication authentication,
			HttpServletResponse response) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HttpHeaders headers = new HttpHeaders();

		response.setContentType("application/octet-stream");
		SimpleDateFormat dateFormatter = getSimpleDateFormat();
		String currentDateTime = dateFormatter.format(new Date());
		
		ByteArrayInputStream in = dao.createTemplate(response, userCode, authorizationHeader, requestModel);

		String filename = "opex_budget_plan_" + currentDateTime + ".xlsx";
		headers.add("Content-Disposition", "attachment ; filename = " + filename);
		headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
		return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));
	}
}
