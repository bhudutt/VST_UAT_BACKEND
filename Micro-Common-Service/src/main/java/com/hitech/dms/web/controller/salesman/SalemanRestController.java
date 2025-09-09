/**
 * 
 */
package com.hitech.dms.web.controller.salesman;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.salesman.SalesmanDao;
import com.hitech.dms.web.model.salesman.request.SalesmanListFormModel;
import com.hitech.dms.web.model.salesman.response.SalesmanListModel;

/**
 * @author dinesh.jakhar
 *
 */
@Validated
@RestController
@RequestMapping("/salesman")
public class SalemanRestController {
	private static final Logger logger = LoggerFactory.getLogger(SalemanRestController.class);
	@Autowired
	private SalesmanDao salesmanDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@GetMapping("/salesmanList/{dealerOrBranch}/{dealerOrBranchID}")
	public ResponseEntity<?> fetchSalesmanList(@PathVariable String dealerOrBranch, @PathVariable Long dealerOrBranchID,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<SalesmanListModel> responseModelList = salesmanDao.fetchSalesmanList(userCode, dealerOrBranch,
				dealerOrBranchID);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Salesman List By Dealer/Branch on " + formatter.format(new Date()));
			codeResponse.setMessage("Salesman List By Dealer/Branch List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Salesman List By Dealer/Branch Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/salesmanList")
	public ResponseEntity<?> fetchSalesmanList(@RequestBody SalesmanListFormModel salesmanListFormModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
			salesmanListFormModel.setUserCode(userCode);
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<SalesmanListModel> responseModelList = salesmanDao.fetchSalesmanList(salesmanListFormModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Salesman List By Dealer/Branch on " + formatter.format(new Date()));
			codeResponse.setMessage("Salesman List By Dealer/Branch List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Salesman List By Dealer/Branch Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
}
