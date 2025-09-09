package com.hitech.dms.web.controller.enquiry.digitalReport;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.enquiry.digitalReport.DigitalReportDao;
import com.hitech.dms.web.dao.enquiry.digitalReport.DigitalReportDaoImpl;
import com.hitech.dms.web.model.digitalReport.request.DigitalEnquiReportModel;
import com.hitech.dms.web.model.digitalReport.request.DigitalEnquirySearchRequestModel;
import com.hitech.dms.web.model.enquiry.digitalReport.response.DigitalUploadResponseModel;
import com.hitech.dms.web.model.enquiry.list.response.EnquiryListResultResponseModel;
import com.hitech.dms.web.utils.ExcelCellGenerator;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;


/**
 * @author vinay.gautam
 *
 */


@Validated
@RestController
@RequestMapping("/digitalUploadReport")
@SecurityRequirement(name = "hitechApis")
public class DigitalReportController {

	@Autowired
	private DigitalReportDao digitalReportDao;

	@SuppressWarnings("unchecked")
	@GetMapping("/downloadDigitalReport")
	public ResponseEntity<InputStreamResource> downloadDigitalReport(Session session,
			@RequestParam(required = false) BigInteger digitalEnqHrdId, HttpServletResponse response,
			OAuth2Authentication authentication) throws IOException {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		List<DigitalUploadResponseModel> data = (List<DigitalUploadResponseModel>) digitalReportDao
				.downloadDigitalReport(userCode, digitalEnqHrdId);

		ByteArrayInputStream in = ExcelCellGenerator.digitalUploadReport(data);

		response.setContentType("application/vnd.ms-excel");

		HttpHeaders headers = new HttpHeaders();
		String filename = "Digital_Upload_Report_" + (Calendar.getInstance()).getTimeInMillis() + ".xlsx";
		headers.add("Content-Disposition", "attachment ; filename = " + filename);
		headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
		return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));
	}

	@PostMapping("/fetchDigitalEnquirySearch")
	public ResponseEntity<?> fetchDigitalEnquirySearch(@RequestBody() DigitalEnquirySearchRequestModel requestModel,
			OAuth2Authentication authentication, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		List<DigitalEnquiReportModel> responseList = null;
		SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		
		responseList = digitalReportDao.searchDigitalReport(requestModel, userCode);
		if (responseList != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Digital Report Search on  " + formatter.format(new Date()));
			codeResponse.setMessage("Digital Report Search Successfully");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Server Site Error");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseList);
		return ResponseEntity.ok(userAuthResponse);
	}

}
