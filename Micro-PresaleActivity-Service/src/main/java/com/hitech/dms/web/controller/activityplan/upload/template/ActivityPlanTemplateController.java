/**
 * 
 */
package com.hitech.dms.web.controller.activityplan.upload.template;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.web.dao.activityplan.upload.template.ActivityPlanTemplateDao;
import com.hitech.dms.web.model.activityplan.upload.request.ActivityPlanUploadRequestModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
@Validated
@RestController
@RequestMapping("/activityPlan")
@SecurityRequirement(name = "hitechApis")
public class ActivityPlanTemplateController {
	private static final Logger logger = LoggerFactory.getLogger(ActivityPlanTemplateController.class);

	@Autowired
	private ActivityPlanTemplateDao activityPlanTemplateDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	}

	@GetMapping("/download/{pcId}/{seriesName}/{segment}/{isInactiveInclude}")
	public ResponseEntity<InputStreamResource> downloadActivityPlanTemplate(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@PathVariable Integer pcId, @PathVariable String seriesName, @PathVariable String segment,
			@PathVariable String isInactiveInclude, OAuth2Authentication authentication, HttpServletResponse response) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HttpHeaders headers = new HttpHeaders();
		response.setContentType("application/octet-stream");
		SimpleDateFormat dateFormatter = getSimpleDateFormat();
		String currentDateTime = dateFormatter.format(new Date());

//		String headerKey = "Content-Disposition";
//		String headerValue = "attachment; filename=activity_plans_" + currentDateTime + ".xlsx";
//		response.setHeader(headerKey, headerValue);
		ByteArrayInputStream in = activityPlanTemplateDao.createActivityPlanTemplateExcel(response, userCode,
				authorizationHeader, pcId, isInactiveInclude);

		String filename = "activity_plans_" + currentDateTime + ".xlsx";
		headers.add("Content-Disposition", "attachment ; filename = " + filename);
		headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
		return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));
	}

	@PostMapping("/download")
	public ResponseEntity<InputStreamResource> downloadActivityPlanTemplate(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody ActivityPlanUploadRequestModel activityPlanUploadRequestModel,
			OAuth2Authentication authentication, HttpServletResponse response) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HttpHeaders headers = new HttpHeaders();

		response.setContentType("application/octet-stream");
		SimpleDateFormat dateFormatter = getSimpleDateFormat();
		String currentDateTime = dateFormatter.format(new Date());

//		String headerKey = "Content-Disposition";
//		String headerValue = "attachment; filename=activity_plans_" + currentDateTime + ".xlsx";
//		response.setHeader(headerKey, headerValue);
		ByteArrayInputStream in = activityPlanTemplateDao.createActivityPlanTemplateExcel(response, userCode,
				authorizationHeader, activityPlanUploadRequestModel);
        System.out.println(userCode);
		String filename = "activity_plans_" + currentDateTime + ".xlsx";
		headers.add("Content-Disposition", "attachment ; filename = " + filename);
		headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
		return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));
	}

	private ResponseEntity<?> createResponseEntity(byte[] report, String fileName) {
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$fileName\"").body(report);
	}
}
