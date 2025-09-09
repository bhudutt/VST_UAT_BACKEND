/**
 * 
 */
package com.hitech.dms.web.controller.party;

import java.math.BigInteger;
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
import com.hitech.dms.web.dao.party.PartyDao;
import com.hitech.dms.web.model.party.request.FinancePartyByBranchRequestModel;
import com.hitech.dms.web.model.party.request.FinancePatyByPartyCodeRequestModel;
import com.hitech.dms.web.model.party.request.PartyDTLRequestModel;
import com.hitech.dms.web.model.party.request.PartyForInvoiceRequestModel;
import com.hitech.dms.web.model.party.request.PartyListRequestModel;
import com.hitech.dms.web.model.party.response.FinancePartyByBranchResponseModel;
import com.hitech.dms.web.model.party.response.FinancePatyByPartyCodeResponseModel;
import com.hitech.dms.web.model.party.response.PartyDTLResponseModel;
import com.hitech.dms.web.model.party.response.PartyListResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Validated
@RestController
@RequestMapping("/party")
public class PartyController {
	private static final Logger logger = LoggerFactory.getLogger(PartyController.class);

	@Autowired
	private PartyDao partyDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@GetMapping("/fetchFinancePartyList/{branchID}/{code}")
	public ResponseEntity<?> fetchFinancePartyList(@PathVariable BigInteger branchID,
			@PathVariable(value = "FIN") String code, OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<FinancePartyByBranchResponseModel> responseModelList = partyDao.fetchFinancePatyList(userCode, branchID,
				code);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Finance Party List By Branch Id on " + formatter.format(new Date()));
			codeResponse.setMessage("Finance Party List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Finance Party List By Branch Id Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/fetchFinancePartyList")
	public ResponseEntity<?> fetchFinancePartyList(
			@RequestBody FinancePartyByBranchRequestModel financePartyByBranchRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<FinancePartyByBranchResponseModel> responseModelList = partyDao.fetchFinancePatyList(userCode,
				financePartyByBranchRequestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Finance Party List By Branch Id on " + formatter.format(new Date()));
			codeResponse.setMessage("Finance Party List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Finance Party List By Branch Id Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/fetchPatyListForInvoice")
	public ResponseEntity<?> fetchPatyListForInvoice(@RequestBody PartyForInvoiceRequestModel requestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<PartyListResponseModel> responseModelList = partyDao.fetchPatyListForInvoice(userCode, requestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription(
					"Fetch Dealer/CoDealer/Distributor Party List By PO Id on " + formatter.format(new Date()));
			codeResponse.setMessage("Dealer/CoDealer/Distributor Party List By PO Id List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse
					.setMessage("Dealer/CoDealer/Distributor Party List By PO Id Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/fetchPartyListByCategoryCode")
	public ResponseEntity<?> fetchPartyListByCategoryCode(
			@RequestBody FinancePartyByBranchRequestModel financePartyByBranchRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<FinancePartyByBranchResponseModel> responseModelList = partyDao.fetchFinancePatyList(userCode,
				financePartyByBranchRequestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Party List By Branch Id on " + formatter.format(new Date()));
			codeResponse.setMessage("Party List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Party List By Branch Id Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/fetchPartyList")
	public ResponseEntity<?> fetchPartyList(@RequestBody PartyListRequestModel requestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<PartyListResponseModel> responseModelList = partyDao.fetchPatyList(userCode, requestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Financer Party List By Branch Id on " + formatter.format(new Date()));
			codeResponse.setMessage("Financer Party List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Financer Party List By Branch Id Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/fetchPartyDTL")
	public ResponseEntity<?> fetchPartyList(@RequestBody PartyDTLRequestModel requestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		PartyDTLResponseModel responseModel = partyDao.fetchPatyDTL(userCode, requestModel);
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Financer Party Detail By Branch Id on " + formatter.format(new Date()));
			codeResponse.setMessage("Financer Party Detail Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Financer Party Detail By Branch Id Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/fetchFinancePatyDTL")
	public ResponseEntity<?> fetchFinancePatyDTL(@RequestBody FinancePatyByPartyCodeRequestModel requestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		FinancePatyByPartyCodeResponseModel responseModel = partyDao.fetchFinancePatyDTL(userCode, requestModel);
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse
					.setDescription("Fetch Financer Party Detail By Party Id/Code on " + formatter.format(new Date()));
			codeResponse.setMessage("Financer Party Detail Successfully fetched.");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Financer Party Detail By Party Id/Code Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
}
