/**
 * 
 */
package com.hitech.dms.web.controller.dealer.employee.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.dealer.employee.view.EmployeeViewDao;
import com.hitech.dms.web.model.dealer.employee.view.request.EmployeeViewRequestModel;
import com.hitech.dms.web.model.dealer.employee.view.response.EmployeeViewResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
@RestController
@RequestMapping("/dealer")
@SecurityRequirement(name = "hitechApis")
public class EmployeeViewController {
	private static final Logger logger = LoggerFactory.getLogger(EmployeeViewController.class);

	@Autowired
	private EmployeeViewDao employeeViewDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@PostMapping("/fetchEmployeeDetailById")
	public ResponseEntity<?> fetchEmployeeDetailById(@Valid @RequestBody EmployeeViewRequestModel requestedData,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		String userCode = null;
		userCode = authentication != null ? authentication.getUserAuthentication().getName() : null;
		SimpleDateFormat formatter = getSimpleDateFormat();
		EmployeeViewResponseModel responseModel = null;
		try {
			responseModel = employeeViewDao.fetchEmployeeDetailById(userCode, requestedData);
			if (responseModel != null) {
				codeResponse.setCode("EC200");
				codeResponse.setDescription("Dealer Employee Detail fetched on " + formatter.format(new Date()));
				codeResponse.setMessage("Dealer Employee Detail fetched Successfully.");
			} else {
				codeResponse.setCode("EC500");
				codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
				codeResponse.setMessage("Dealer Employee Detail Not Fetched or server side error.");
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
			codeResponse.setMessage(e.getMessage());
			codeResponse.setCode("500");
		}

		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
}
