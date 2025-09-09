package com.hitech.dms.web.controller.dealer.user.search;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.dealer.user.search.DealerUserSearchDaoImpl;
import com.hitech.dms.web.model.dealer.employee.search.request.DealerEmployeeSearchRequest;
import com.hitech.dms.web.model.dealer.employee.search.response.DealerEmployeeSearchMainResponse;
import com.hitech.dms.web.model.dealer.user.search.request.DealerUserSearchRequest;
import com.hitech.dms.web.model.dealer.user.search.response.DealerUserSearchMainResponse;

/**
 * @author vinay.gautam
 *
 */

@RestController
@RequestMapping("/dealer")
public class DealerUserSearchController {
	
	
private static final Logger logger = LoggerFactory.getLogger(DealerUserSearchController.class);
	
	@Autowired
	private DealerUserSearchDaoImpl dealerUserSearchDaoImpl;
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	@PostMapping("/fetchDealerUserSearch")
	public ResponseEntity<?> fetchDealerEmployeeSearch(@RequestBody DealerUserSearchRequest requestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		
		DealerUserSearchMainResponse responseModel = dealerUserSearchDaoImpl.fetchDealerUserSearch(userCode,
				requestModel);
		if (responseModel != null && !responseModel.getSearch().isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Dealer User Serach List on " + formatter.format(new Date()));
			codeResponse.setMessage("Dealer User Serach List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Dealer User Serach List Not Fetched or server side error.");
		}
		
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	

}
