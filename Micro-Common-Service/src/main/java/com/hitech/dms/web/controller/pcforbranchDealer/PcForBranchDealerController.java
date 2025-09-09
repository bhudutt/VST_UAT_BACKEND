package com.hitech.dms.web.controller.pcforbranchDealer;

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
import com.hitech.dms.web.dao.pcforbranchDealer.PcForBranchDealerDao;
import com.hitech.dms.web.model.pcforbranchDealer.request.PcForBranchDealerRequestModel;
import com.hitech.dms.web.model.pcforbranchDealer.response.PcForBranchDealerResponseModel;

@Validated
@RestController
@RequestMapping("/pcfordealerbranch")
public class PcForBranchDealerController {
	private static final Logger logger = LoggerFactory.getLogger(PcForBranchDealerController.class);

	@Autowired
	private PcForBranchDealerDao pcForBranchDealerDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@GetMapping("/fetchPcForBranchDealerList/{isApplicableFor}/{dealerOrBranchId}/{forSalesFlag}")
	public ResponseEntity<?> fetchSalesmanList(@PathVariable String isApplicableFor,
			@PathVariable Long dealerOrBranchId, @PathVariable String forSalesFlag,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<PcForBranchDealerResponseModel> responseModelList = pcForBranchDealerDao
				.fetchPcForBranchDealerList(userCode, isApplicableFor, dealerOrBranchId, forSalesFlag);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch PC List By Dealer/Branch on " + formatter.format(new Date()));
			codeResponse.setMessage("PC List By Dealer/Branch List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("PC List By Dealer/Branch Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/fetchPcForBranchDealerList")
	public ResponseEntity<?> fetchSalesmanList(@RequestBody PcForBranchDealerRequestModel pcForBranchDealerRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<PcForBranchDealerResponseModel> responseModelList = pcForBranchDealerDao
				.fetchPcForBranchDealerList(userCode, pcForBranchDealerRequestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch PC List By Dealer/Branch on " + formatter.format(new Date()));
			codeResponse.setMessage("PC List By Dealer/Branch List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("PC List By Dealer/Branch Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
}
