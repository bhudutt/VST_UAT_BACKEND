package com.hitech.dms.web.Controller.spare.claim.inventory;

import java.math.BigInteger;
import java.text.ParseException;
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
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.spare.inventorytransfer.InventoryTransferForClaimDao;
import com.hitech.dms.web.model.spare.inventory.response.SpareGrnInventoryResponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.PartNumberDetailResponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.SpareGrnResponse;
import com.hitech.dms.web.model.spare.inventory.request.SpareInventoryRequest;

@RestController
@RequestMapping("/api/v1/inventoryTransfer")
public class InventoryTransferController {

	@Value("${file.upload-dir.SpareGrnSearch:C:\\VST-DMS-APPS\\template\\spare template\\\\}")
    private String spareGrnSearchdownloadPath;
	
	@Autowired
	private ConnectionConfiguration dataSourceConnection;
	
	@Autowired
	private InventoryTransferForClaimDao inventoryTransferForClaimDao;
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	@GetMapping("/searchGrnNumber")
	public ResponseEntity<?> searchGrnOrInvoiceNumber(@RequestParam() String searchType,
			@RequestParam() String searchText, @RequestParam() String page,
			OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		HashMap<BigInteger, String> grnNumberList = inventoryTransferForClaimDao.searchGrnNumber(searchType, searchText, page, userCode);
		if (grnNumberList != null && !grnNumberList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Grn Number List on " + formatter.format(new Date()));
			codeResponse.setMessage("Grn Number List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Grn Number List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(grnNumberList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/fetchGrnDetails")
	public ResponseEntity<?> fetchGrnDetails(@RequestParam() int grnHdrId,
			@RequestParam() String pageName,
			OAuth2Authentication authentication,
			Device device, HttpServletRequest request) throws ParseException {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		SpareGrnInventoryResponse spareGrnInventoryResponse = 
				inventoryTransferForClaimDao.fetchGrnDetails(grnHdrId, pageName);
		if (spareGrnInventoryResponse != null) {
			codeResponse.setCode(spareGrnInventoryResponse.getStatusCode().toString());
			codeResponse.setDescription("GRN List on " + formatter.format(new Date()));
			codeResponse.setMessage(spareGrnInventoryResponse.getMsg());
		} else {
			codeResponse.setCode(spareGrnInventoryResponse.getStatusCode().toString());
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage((spareGrnInventoryResponse.getStatusCode() != WebConstants.STATUS_INTERNAL_SERVER_ERROR_500) ?
					spareGrnInventoryResponse.getMsg()
					: "GRN List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(spareGrnInventoryResponse);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/fetchGrnPartDetails")
	public ResponseEntity<?> fetchGrnPartDetails(@RequestParam() int grnHdrId, 
			@RequestParam(required = false) String claimType,
			@RequestParam(required = false) String page,
			OAuth2Authentication authentication,
			Device device, HttpServletRequest request) throws ParseException {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<PartNumberDetailResponse> partNumberDetailResponseList = 
				inventoryTransferForClaimDao.fetchGrnPartDetails(grnHdrId, page, claimType);
		if (partNumberDetailResponseList != null && !partNumberDetailResponseList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("GRN List on " + formatter.format(new Date()));
			codeResponse.setMessage("GRN List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("No Part Details found for the given GRN Number");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(partNumberDetailResponseList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@PostMapping("/createSpareInventory")
	public ResponseEntity<?> createSpareInventory(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody SpareInventoryRequest spareInventoryRequest, OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}

		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		SpareGrnResponse spareGrnResponse = inventoryTransferForClaimDao
				.createSpareInventory(userCode, spareInventoryRequest);
		if (spareGrnResponse.getStatusCode() == 201) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Spare Inventory added on " + formatter.format(new Date()));
			codeResponse.setMessage(spareGrnResponse.getMsg());
		} else if (spareGrnResponse.getStatusCode() == 500) {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Spare Inventory Added or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(spareGrnResponse);
		return ResponseEntity.ok(userAuthResponse);
	}	

}
