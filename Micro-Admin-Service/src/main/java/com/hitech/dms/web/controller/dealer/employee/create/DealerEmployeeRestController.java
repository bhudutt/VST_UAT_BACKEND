package com.hitech.dms.web.controller.dealer.employee.create;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.dealer.employee.create.DealerEmployeeDaoImpl;
import com.hitech.dms.web.model.dealer.employee.create.request.DealerEmployeeHdrRequestModel;
import com.hitech.dms.web.model.dealer.employee.create.request.DealerEmployeeReportingRequestModel;
import com.hitech.dms.web.model.dealer.employee.create.response.DealerEmployeeCreateResponseModel;
import com.hitech.dms.web.model.dealer.employee.create.response.DealerEmployeeReportingResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author vinay.gautam
 *
 */

@RestController
@RequestMapping("/dealer")
@Validated
@SecurityRequirement(name = "hitechApis")
public class DealerEmployeeRestController {
	
	private static final Logger logger = LoggerFactory.getLogger(DealerEmployeeRestController.class);

	@Autowired
	private DealerEmployeeDaoImpl dealerEmployeeDaoImpl;
	
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	@PostMapping("/createDealerEmployee")
	public ResponseEntity<?> createDealerEmployee(@Valid @RequestBody DealerEmployeeHdrRequestModel requestedData, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		String userCode = null;
		userCode = authentication != null ? authentication.getUserAuthentication().getName() : null;
		SimpleDateFormat formatter = getSimpleDateFormat();
		DealerEmployeeCreateResponseModel responseModel = null;
		try {
			responseModel = dealerEmployeeDaoImpl.createDealerEmployee(userCode, requestedData,device);
			if (responseModel != null && responseModel.getStatusCode() == 201) {
				codeResponse.setCode("EC200");
				codeResponse.setDescription("Dealer Employee Create on " + formatter.format(new Date()));
				codeResponse.setMessage(responseModel.getMsg());
			} else if (responseModel.getStatusCode() == 500) {
				codeResponse.setCode("EC500");
				codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
				codeResponse.setMessage("Dealer Employee Not Create or server side error.");
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
	
	
	@PostMapping(value = "/dealerReportingEmployee")
	public ResponseEntity<?> dealerReportingEmployee(@RequestBody DealerEmployeeReportingRequestModel requestModel, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<DealerEmployeeReportingResponseModel> responseList = dealerEmployeeDaoImpl.dealerReportingEmployee(userCode, requestModel, device);
		if (responseList != null && !responseList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Dealer Reporting Employee Detail List on " + formatter.format(new Date()));
			codeResponse.setMessage("Dealer Reporting Employee Detail List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Dealer Reporting Employee Detail List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseList);
		return ResponseEntity.ok(userAuthResponse);
	}
	

}
