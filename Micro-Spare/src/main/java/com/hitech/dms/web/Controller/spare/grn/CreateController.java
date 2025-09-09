package com.hitech.dms.web.Controller.spare.grn;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.hitech.dms.app.config.ConnectionConfiguration;
import com.hitech.dms.app.utils.ApiResponse;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.spare.grn.mapping.SpareGRNDao;
import com.hitech.dms.web.model.spare.grn.mapping.request.SpareGrnRequest;
import com.hitech.dms.web.model.spare.grn.mapping.response.BinLocationListResponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.InvoiceNumberResponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.PartNumberDetailResponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.PartyCodeDetailResponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.SpareGrnFromResponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.SpareGrnPODetailsReponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.SpareGrnPONumberReponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.SpareGrnResponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.StoreResponse;
import com.hitech.dms.web.model.spare.party.mapping.response.PartyCodeSearchResponse;

@RestController
@RequestMapping("/api/v1/spare/grn/create")
public class CreateController {


	@Value("${file.upload-dir.SpareGrnSearch:C:\\VST-DMS-APPS\\template\\spare template\\\\}")
    private String spareGrnSearchdownloadPath;
	
	@Autowired
	SpareGRNDao spareGRNDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@GetMapping("/getGrnFrom")
	public ResponseEntity<?> fetchGrnFrom(@RequestParam() int dealerId, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		ApiResponse<?> apiResponse = spareGRNDao.fetchGrnFromList(dealerId, userCode);
		if (apiResponse != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Header List on " + formatter.format(new Date()));
			codeResponse.setMessage(apiResponse.getMessage());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage((apiResponse != null && apiResponse.getMessage() != null) ? apiResponse.getMessage() : "No Data Found");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(apiResponse.getResult());
		return ResponseEntity.ok(userAuthResponse);
	}

	//not 
	@GetMapping("/fetchGrnFromDetails")
	public ResponseEntity<?> fetchGrnFromDetails(@RequestParam() int dealerId,
			@RequestParam() int grnTypeId, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		SpareGrnFromResponse spareGrnFromResponse = spareGRNDao
				.fetchGrnFromDetails(dealerId, grnTypeId, userCode);
		if (spareGrnFromResponse != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Header Details on " + formatter.format(new Date()));
			codeResponse.setMessage("Header Details Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Header Details Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(spareGrnFromResponse);
		return ResponseEntity.ok(userAuthResponse);
	}

	
	@GetMapping("/searchPartyCode")
	public ResponseEntity<?> searchPartyCode(@RequestParam() String searchText, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<PartyCodeSearchResponse> partyCodeSearchResponseList = spareGRNDao.searchPartyCode(searchText, userCode);
		if (partyCodeSearchResponseList != null && !partyCodeSearchResponseList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Party Code List on " + formatter.format(new Date()));
			codeResponse.setMessage("Party Code List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Party Code List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(partyCodeSearchResponseList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/fetchPartyCodeDetails")
	public ResponseEntity<?> fetchPartyCodeDetails(@RequestParam() Integer partyCategoryId,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		PartyCodeDetailResponse partyCodeDetailResponse = spareGRNDao.fetchPartyDetails(partyCategoryId);
		if (partyCodeDetailResponse != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Party Details on " + formatter.format(new Date()));
			codeResponse.setMessage("Party Details Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Party Details Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(partyCodeDetailResponse);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/searchInvoiceNumber")
	public ResponseEntity<?> searchInvoiceNumber(@RequestParam() String searchText, 
			@RequestParam() String grnType, @RequestParam(required = false) String partyCode,
			@RequestParam() String dealerCode, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<InvoiceNumberResponse> invoiceNumberResponseList = spareGRNDao
				.searchInvoiceNumber(searchText, grnType, userCode, dealerCode, partyCode);
		if (invoiceNumberResponseList != null && !invoiceNumberResponseList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Invoice Number List on " + formatter.format(new Date()));
			codeResponse.setMessage("Invoice Number List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Invoice Number List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(invoiceNumberResponseList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/fetchInvoiceDetails")
	public ResponseEntity<?> fetchInvoiceDetails(@RequestParam() String InvoiceNo,
			@RequestParam() String grnType, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		String lookupTypeCode = "GRN";
		InvoiceNumberResponse invoiceNumberResponse = spareGRNDao.fetchInvoiceDetails(InvoiceNo, grnType);
		if (invoiceNumberResponse != null) {
			codeResponse.setCode(invoiceNumberResponse.getStatusCode().toString());
			codeResponse.setDescription("Invoice Details on " + formatter.format(new Date()));
			codeResponse.setMessage(invoiceNumberResponse.getMsg());
		} else {
			codeResponse.setCode(invoiceNumberResponse.getStatusCode().toString());
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage((invoiceNumberResponse.getStatusCode() != WebConstants.STATUS_INTERNAL_SERVER_ERROR_500) ?
					invoiceNumberResponse.getMsg()
					: "Invoice Details Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(invoiceNumberResponse);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/fetchStoreList")
	public ResponseEntity<?> fetchStoreList(@RequestParam Integer partId, 
			OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<StoreResponse> storeResponseList = spareGRNDao.fetchStoreListByPartId(userCode, partId);
		if (storeResponseList != null && !storeResponseList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Header List on " + formatter.format(new Date()));
			codeResponse.setMessage("Header List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Header List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(storeResponseList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/fetchBinByStore")
	public ResponseEntity<?> fetchBinByStore(@RequestParam() Integer branchStoreId, @RequestParam() Integer partId,
			OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		HashMap<BigInteger, String> binNameDtl = spareGRNDao.fetchBinByStore(branchStoreId, partId);
		if (binNameDtl != null && !binNameDtl.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Bin on " + formatter.format(new Date()));
			codeResponse.setMessage("Bin Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Bin Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(binNameDtl);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/searchPoNumber")
	public ResponseEntity<?> searchPoNumber(@RequestParam() String searchText, 
			@RequestParam() String grnType, @RequestParam() String categoryCode,
			@RequestParam() String partyCode, @RequestParam(required = false) Integer branchId,
			@RequestParam() String isFor,
			OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<SpareGrnPONumberReponse> spareGrnPONumberReponseList = 
				spareGRNDao.searchPONumber(searchText, grnType, categoryCode, partyCode, userCode, branchId, isFor);
		if (spareGrnPONumberReponseList != null && !spareGrnPONumberReponseList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("PO Number List on " + formatter.format(new Date()));
			codeResponse.setMessage("PO Number List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("PO Number List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(spareGrnPONumberReponseList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/fetchPoDetails")
	public ResponseEntity<?> fetchPoDetails(@RequestParam() BigInteger poHdrId, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<SpareGrnPODetailsReponse> spareGrnPODetailsReponse = spareGRNDao.fetchPODetails(poHdrId);
		if (spareGrnPODetailsReponse != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Invoice Details on " + formatter.format(new Date()));
			codeResponse.setMessage("Invoice Details Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Invoice Details Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(spareGrnPODetailsReponse);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/searchBinLocation")
	public ResponseEntity<?> searchBinLocation(@RequestParam() String searchText, @RequestParam() int branchStoreId,
			@RequestParam() String invoiceNo, OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<BinLocationListResponse> binLocationListResponseList = spareGRNDao.searchBinLocation(searchText, userCode,
				branchStoreId, invoiceNo);
		if (binLocationListResponseList != null && !binLocationListResponseList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setMessage("Part Number List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Part Number List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(binLocationListResponseList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/getBinDetails")
	public ResponseEntity<?> fetchBinDetails(@RequestParam() String invoiceNo, int grnTypeId,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<PartNumberDetailResponse> partNumberDetailResponse = spareGRNDao.fetchBinDetails(invoiceNo, grnTypeId, userCode);
		if (partNumberDetailResponse != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Part Details on " + formatter.format(new Date()));
			codeResponse.setMessage("Part Details Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Part Details Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(partNumberDetailResponse);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/createSpareGrn")
	public ResponseEntity<?> createSpareGrn(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody SpareGrnRequest spareGrnRequest, OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}

		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		SpareGrnResponse spareGrnResponse = spareGRNDao.createSpareGrn(userCode, spareGrnRequest);
		if (spareGrnResponse.getStatusCode() == 201) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Spare GRN added on " + formatter.format(new Date()));
			codeResponse.setMessage(spareGrnResponse.getMsg());
		} else if (spareGrnResponse.getStatusCode() == 500) {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Spare GRN Added ");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(spareGrnResponse);
		return ResponseEntity.ok(userAuthResponse);
	}
}
