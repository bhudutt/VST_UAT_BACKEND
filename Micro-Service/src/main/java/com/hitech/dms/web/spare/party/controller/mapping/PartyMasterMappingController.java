package com.hitech.dms.web.spare.party.controller.mapping;

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
import com.hitech.dms.web.spare.party.model.mapping.request.PartyMappingRequest;
import com.hitech.dms.web.spare.party.model.mapping.response.*;

import com.hitech.dms.web.model.baymaster.create.response.BayMasterResponse;
import com.hitech.dms.web.model.baymaster.responselist.BayTypeModel;
import com.hitech.dms.web.spare.party.dao.mapping.PartyMappingDao;

@RestController
@RequestMapping("/partyMaster/mapping")
public class PartyMasterMappingController {

	@Autowired
	private PartyMappingDao partyMappingDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@PostMapping("/createMapping")
	public ResponseEntity<?> createPartyMapping(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody PartyMappingRequest partyMappingRequest, OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}

		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		PartyMappingResponse partyMappingResponse = partyMappingDao.createPartyMapping(userCode, partyMappingRequest);
		if (partyMappingResponse.getStatusCode() == 201) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Party Mapping added on " + formatter.format(new Date()));
			codeResponse.setMessage(partyMappingResponse.getMsg());
		} else if (partyMappingResponse.getStatusCode() == 500) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Party Mapping Added or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(partyMappingResponse);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/deletePartyMapping")
	public ResponseEntity<?> deletePartyMapping(@RequestParam() String isActive, @RequestBody List<Integer> partyIdList,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}

		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		PartyMappingResponse partyMappingResponse = partyMappingDao.deletePartyMapping(partyIdList, isActive);
		if (partyMappingResponse.getStatusCode() == 201) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Party Mapping deleted on " + formatter.format(new Date()));
			codeResponse.setMessage(partyMappingResponse.getMsg());
		} else if (partyMappingResponse.getStatusCode() == 500) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Party Mapping deleted or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(partyMappingResponse);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/fetchPartyMapping")
	public ResponseEntity<?> fetchPartyMappingList(@RequestParam() Integer partyCategoryId,
			@RequestParam() Integer partyBranchId, OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<PartyDistributorMappingResponse> partyDistributorMappingResponseList = partyMappingDao
				.fetchPartyMappingList(partyCategoryId, partyBranchId);
		if (partyDistributorMappingResponseList != null && !partyDistributorMappingResponseList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Party Category List on " + formatter.format(new Date()));
			codeResponse.setMessage("Party Category List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Party Category List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(partyDistributorMappingResponseList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/fetchPartyCategory")
	public ResponseEntity<?> fetchPartyCategoryMasterList(OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<PartyCategoryResponse> partyCategoryResponseList = partyMappingDao.fetchPartyCategoryMaster(userCode);
		if (partyCategoryResponseList != null && !partyCategoryResponseList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Party Category List on " + formatter.format(new Date()));
			codeResponse.setMessage("Party Category List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Party Category List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(partyCategoryResponseList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/searchPartyCode")
	public ResponseEntity<?> searchPartyCode(@RequestParam() int partyCategoryId, @RequestParam() String searchText,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<PartyCodeSearchResponse> partyCodeSearchResponseList = partyMappingDao
				.searchPartyCodeByPartyCategory(partyCategoryId, searchText, userCode);
		if (partyCodeSearchResponseList != null && !partyCodeSearchResponseList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Party Code List on " + formatter.format(new Date()));
			codeResponse.setMessage("Party Code List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Party Code List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(partyCodeSearchResponseList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/fetchPartyDetails")
	public ResponseEntity<?> fetchPartyDetails(@RequestParam() int partyBranchId, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		PartyDetailResponse partyDetailResponse = partyMappingDao.fetchPartyDetailsByPartyCode(partyBranchId);
		
		if (partyDetailResponse != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Party Code List on " + formatter.format(new Date()));
			codeResponse.setMessage("Party Details Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Party Details Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(partyDetailResponse);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/searchDealer")
	public ResponseEntity<?> searchDealer(@RequestParam() String dealer, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<DealerCodeSearchResponse> dealerCodeSearchResponseList = partyMappingDao.searchDealers(dealer, userCode);
		if (dealerCodeSearchResponseList != null && !dealerCodeSearchResponseList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Dealer List on " + formatter.format(new Date()));
			codeResponse.setMessage("Dealer List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Dealer List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(dealerCodeSearchResponseList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/fetchDealerDetails")
	public ResponseEntity<?> fetchDealerDetails(@RequestParam() Integer parentDealerId,
			@RequestParam(required = false) Integer partyCategoryId,
			@RequestParam(required = false) Integer partyBranchId,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		DistributorDetailResponse distributorDetailResponse = null;
		if (partyBranchId != null && partyCategoryId!= null) {
			boolean isExist = partyMappingDao.checkIfPartyMappingAlreadyExist(
					partyBranchId, partyCategoryId, parentDealerId);

			if (isExist) {
				codeResponse.setCode("EC500");
				codeResponse.setMessage("Party mapping already exist");
				userAuthResponse.setResponseCode(codeResponse);
				userAuthResponse.setResponseData(distributorDetailResponse);
				return ResponseEntity.ok(userAuthResponse);
			} else {
				distributorDetailResponse = partyMappingDao
						.fetchDealerDetailsBydealer(parentDealerId);			}
		} else {
			distributorDetailResponse = partyMappingDao
					.fetchDealerDetailsBydealer(parentDealerId);		}
		 
		if (distributorDetailResponse != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Dealer Details on " + formatter.format(new Date()));
			codeResponse.setMessage("Dealer Details Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Dealer Details Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(distributorDetailResponse);
		return ResponseEntity.ok(userAuthResponse);
	}

}
