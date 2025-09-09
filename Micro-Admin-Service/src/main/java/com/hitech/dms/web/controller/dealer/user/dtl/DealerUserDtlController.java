/**
 * 
 */
package com.hitech.dms.web.controller.dealer.user.dtl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import com.hitech.dms.web.dao.dealer.user.dtl.DealerUserDtlDao;
import com.hitech.dms.web.model.dealer.user.dtl.request.DealerEmpAutoSearchRequestModel;
import com.hitech.dms.web.model.dealer.user.dtl.request.DealerEmpDtlRequestModel;
import com.hitech.dms.web.model.dealer.user.dtl.request.DealerEmpUserDtlRequestModel;
import com.hitech.dms.web.model.dealer.user.dtl.response.DealerEmpAutoSearchResponseModel;
import com.hitech.dms.web.model.dealer.user.dtl.response.DealerEmpDtlResponseModel;
import com.hitech.dms.web.model.dealer.user.dtl.response.DealerEmpUserDtlResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@RestController
@RequestMapping("/dealer")
@Validated
public class DealerUserDtlController {
	private static final Logger logger = LoggerFactory.getLogger(DealerUserDtlController.class);

	@Autowired
	private DealerUserDtlDao dealerUserDtlDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@PostMapping("/fetchEmployeeDTLForUserById")
	public ResponseEntity<?> fetchEmployeeDTLForUserById(@RequestBody DealerEmpDtlRequestModel requestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();

		DealerEmpDtlResponseModel responseModel = dealerUserDtlDao.fetchEmployeeDTLForUserById(userCode, requestModel);
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Dealer Employee Detail on " + formatter.format(new Date()));
			codeResponse.setMessage("Dealer Employee Detail Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Dealer Employee Detail Not Fetched or server side error.");
		}

		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/fetchEmployeeAutoList")
	public ResponseEntity<?> fetchEmployeeAutoList(@RequestBody DealerEmpAutoSearchRequestModel requestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();

		List<DealerEmpAutoSearchResponseModel> responseModelList = null;
		Map<String, Object> mapData = dealerUserDtlDao.fetchEmployeeAutoList(userCode, requestModel);
		if (mapData != null && mapData.get("msg").toString().equals("SUCCESS")) {
			responseModelList = (List<DealerEmpAutoSearchResponseModel>) mapData.get("responseModelList");
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Dealer Employee Search Auto List on " + formatter.format(new Date()));
			codeResponse.setMessage("Dealer Employee List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Dealer Employee List Not Fetched or server side error.");
		}

		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/fetchEmpUserDetail")
	public ResponseEntity<?> fetchEmpUserDetail(@RequestBody DealerEmpUserDtlRequestModel requestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();

		DealerEmpUserDtlResponseModel responseModel = dealerUserDtlDao.fetchEmpUserDetail(userCode, requestModel);
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Dealer Employee User Detail on " + formatter.format(new Date()));
			codeResponse.setMessage("Dealer Employee User Detail Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Dealer Employee User Detail Not Fetched or server side error.");
		}

		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
}
