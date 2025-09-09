package com.hitech.dms.web.Controller.spare.party.mapping;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.spare.party.mapping.DealerDistributorMappingDao;
import com.hitech.dms.web.model.spare.party.mapping.request.DealerDistributorMappingRequest;
import com.hitech.dms.web.model.spare.party.mapping.request.PartyMappingRequest;
import com.hitech.dms.web.model.spare.party.mapping.response.DealerCodeSearchResponse;
import com.hitech.dms.web.model.spare.party.mapping.response.DealerDistributorMappingResponse;
import com.hitech.dms.web.model.spare.party.mapping.response.DealerMappingHeaderResponse;
import com.hitech.dms.web.model.spare.party.mapping.response.DistributorDetailResponse;
import com.hitech.dms.web.model.spare.party.mapping.response.DistributorMappingResponse;
import com.hitech.dms.web.model.spare.party.mapping.response.PartyCodeSearchResponse;
import com.hitech.dms.web.model.spare.party.mapping.response.PartyDetailResponse;
import com.hitech.dms.web.model.spare.party.mapping.response.PartyDistributorMappingResponse;
import com.hitech.dms.web.model.spare.party.mapping.response.PartyMappingResponse;

@RestController
@RequestMapping("/api/v1/dealer/distributor")
public class DealerDistributorMappingController {

	@Autowired
	DealerDistributorMappingDao dealerDistributorMappingDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@GetMapping("/fetchHeaderList")
	public ResponseEntity<?> fetchHeaderList(OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		String lookupTypeCode = "DI-DS-MAP";
		List<DealerMappingHeaderResponse> dealerMappingHeaderResponseList = dealerDistributorMappingDao
				.fetchHeaderList(lookupTypeCode);
		if (dealerMappingHeaderResponseList != null && !dealerMappingHeaderResponseList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Header List on " + formatter.format(new Date()));
			codeResponse.setMessage("Header List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Header List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(dealerMappingHeaderResponseList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/searchDealerDistributor")
	public ResponseEntity<?> searchDealerDistributor(@RequestParam() String isFor, @RequestParam() String searchText,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<DealerCodeSearchResponse> dealerCodeSearchResponseList = dealerDistributorMappingDao
				.searchDealerDistributor(isFor, searchText, userCode);
		if (dealerCodeSearchResponseList != null && !dealerCodeSearchResponseList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Dealer Code List on " + formatter.format(new Date()));
			codeResponse.setMessage("Dealer Code List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Dealer Code List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(dealerCodeSearchResponseList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/fetchDistributorDetails")
	public ResponseEntity<?> fetchDistributorDetails(@RequestParam() Integer distributorId,
			@RequestParam(required = false) BigInteger headerId, @RequestParam(required = false) Integer parentDealerId,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		DistributorDetailResponse distributorDetailResponse = null;
		if (headerId != null && parentDealerId != null) {
			BigInteger id = dealerDistributorMappingDao
					.checkIfDealerDistributorMappingAlreadyExist(headerId, parentDealerId);
			
			boolean isExist = dealerDistributorMappingDao
					.checkIfDealerDistributorDtlExist(id, distributorId);
			
			if (isExist) {
				codeResponse.setCode("EC500");
				codeResponse.setMessage("Dealer Distributor mapping already exist");
				userAuthResponse.setResponseCode(codeResponse);
				userAuthResponse.setResponseData(distributorDetailResponse);
				return ResponseEntity.ok(userAuthResponse);
			} else {
				distributorDetailResponse = dealerDistributorMappingDao.fetchDistributorDetails(distributorId);
			}
		} else {
			distributorDetailResponse = dealerDistributorMappingDao.fetchDistributorDetails(distributorId);
		}
		if (distributorDetailResponse != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Party Code List on " + formatter.format(new Date()));
			codeResponse.setMessage("Party Code List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Party Code List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(distributorDetailResponse);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/searchDistributorList")
	public ResponseEntity<?> searchDistributorList(@RequestParam() Integer parentDealerId,
			@RequestParam() String searchText, @RequestParam() String isFor, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<DealerCodeSearchResponse> dealerCodeSearchResponseList = dealerDistributorMappingDao
				.searchDistributorList(parentDealerId, searchText, isFor, userCode);
		if (dealerCodeSearchResponseList != null && !dealerCodeSearchResponseList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Dealer Code List on " + formatter.format(new Date()));
			codeResponse.setMessage("Dealer Code List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Dealer Code List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(dealerCodeSearchResponseList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/createDealerDistributorMapping")
	public ResponseEntity<?> createDealerDistributorMapping(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody DealerDistributorMappingRequest dealerDistributorMappingRequest,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}

		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		DealerDistributorMappingResponse dealerDistributorMappingResponse = dealerDistributorMappingDao
				.createDealerDistributorMapping(userCode, dealerDistributorMappingRequest);
		if (dealerDistributorMappingResponse.getStatusCode() == 201) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Dealer Distributor Mapping added on " + formatter.format(new Date()));
			codeResponse.setMessage(dealerDistributorMappingResponse.getMsg());
		} else if (dealerDistributorMappingResponse.getStatusCode() == 500) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Dealer Distributor Mapping Added or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(dealerDistributorMappingResponse);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/deleteDealerDistributorMapping")
	public ResponseEntity<?> deleteDealerDistributorMapping(@RequestBody List<Integer> idList,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}

		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		DealerDistributorMappingResponse dealerDistributorMappingResponse = dealerDistributorMappingDao
				.deleteDealerDistributorMapping(idList);
		if (dealerDistributorMappingResponse.getStatusCode() == 201) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Party Mapping deleted on " + formatter.format(new Date()));
			codeResponse.setMessage(dealerDistributorMappingResponse.getMsg());
		} else if (dealerDistributorMappingResponse.getStatusCode() == 500) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Party Mapping deleted or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(dealerDistributorMappingResponse);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/fetchDealerDistributorMapping")
	public ResponseEntity<?> fetchDealerDistributorMappingList(@RequestParam() Integer parentDealerId,
			@RequestParam() Integer mappingToId, OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<DistributorMappingResponse> distributorMappingResponseList = dealerDistributorMappingDao
				.fetchDealerDistributorMappingList(parentDealerId, mappingToId);
		if (distributorMappingResponseList != null && !distributorMappingResponseList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Party Category List on " + formatter.format(new Date()));
			codeResponse.setMessage("Party Category List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Party Category List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(distributorMappingResponseList);
		return ResponseEntity.ok(userAuthResponse);
	}

}
