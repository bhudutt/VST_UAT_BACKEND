package com.hitech.dms.web.controller.dealer.employee.search;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.dealer.employee.search.DealerEmployeeSearchDaoImpl;
import com.hitech.dms.web.model.dealer.employee.search.request.DealerEmployeeSearchRequest;
import com.hitech.dms.web.model.dealer.employee.search.response.DealerEmployeeSearchMainResponse;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author vinay.gautam
 *
 */

@RestController
@RequestMapping("/dealer")
@Validated
@SecurityRequirement(name = "hitechApis")
public class DealerEmployeeRestSearchController {
	
private static final Logger logger = LoggerFactory.getLogger(DealerEmployeeRestSearchController.class);
	
	@Autowired
	private DealerEmployeeSearchDaoImpl dealerEmployeeSearchDaoImpl;
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	
	@PostMapping("/fetchDealerEmployeeSearch")
	public ResponseEntity<?> fetchDealerEmployeeSearch(@RequestBody DealerEmployeeSearchRequest requestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		
		DealerEmployeeSearchMainResponse responseModel = dealerEmployeeSearchDaoImpl.fetchDealerEmployeeSearch(userCode,
				requestModel);
		if (responseModel != null && !responseModel.getSearch().isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Dealer Employee Serach List on " + formatter.format(new Date()));
			codeResponse.setMessage("Dealer Employee Serach List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Dealer Employee Serach List Not Fetched or server side error.");
		}
		
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}

}
