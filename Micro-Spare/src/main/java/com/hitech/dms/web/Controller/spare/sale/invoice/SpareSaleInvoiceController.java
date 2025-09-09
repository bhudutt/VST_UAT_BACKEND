package com.hitech.dms.web.Controller.spare.sale.invoice;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
import com.hitech.dms.app.utils.ApiResponse;
import com.hitech.dms.web.model.SpareModel.SparePoHDRAndPartDetailsModel;
import com.hitech.dms.web.model.spara.customer.order.picklist.request.PicklistRequest;
import com.hitech.dms.web.model.spara.delivery.challan.response.COpartDetailResponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.SpareGrnResponse;
import com.hitech.dms.web.model.spare.sale.invoice.SalesInvoiceDCResponse;
import com.hitech.dms.web.model.spare.sale.invoice.request.SpareSalesInvoiceRequest;
import com.hitech.dms.web.model.spare.sale.invoice.request.TaxDetailsRequest;
import com.hitech.dms.web.model.spare.sale.invoice.response.PartTaxCalCulationResponse;
import com.hitech.dms.web.model.spare.sale.invoice.response.SpareSalesInvoiceResponse;
import com.hitech.dms.web.service.sale.invoice.SpareSaleInvoiceService;

@RestController
@RequestMapping("api/v1/spareSaleInvoice")
public class SpareSaleInvoiceController {
	
	@Autowired
	SpareSaleInvoiceService spareSaleInvoiceService;
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	@GetMapping("/fetchReferenceDocList")
	public ResponseEntity<?> fetchReferenceDocList(@RequestParam() Integer dealerId,
			OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		HashMap<BigInteger, String> indentNumberList = spareSaleInvoiceService
				.fetchReferenceDocList(dealerId, userCode);
		if (indentNumberList != null && !indentNumberList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Customer Order Number List on " + formatter.format(new Date()));
			codeResponse.setMessage("Customer Order Number List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Customer Order Number List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(indentNumberList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping(value = "/searchDcNumber")
	public ResponseEntity searchDCNumber(@RequestParam(name = "searchText") String searchText, 
			@RequestParam() String categoryCode,
			OAuth2Authentication authentication, Device device,HttpServletRequest request) {
		HttpHeaders headers = new HttpHeaders();
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		HashMap<BigInteger, String> responseModelList = spareSaleInvoiceService.searchDCNumber(searchText, categoryCode, userCode);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Spare PO Category List on " + LocalDate.now());
			codeResponse.setMessage("Fetch Spare PO Category List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Fetch Spare PO Category List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping(value = "/deliveryChallanDtl")
	public ResponseEntity deliveryChallanDtl(@RequestParam(name = "partyBranchId") Integer partyBranchId, 
			OAuth2Authentication authentication, Device device,HttpServletRequest request) {
		HttpHeaders headers = new HttpHeaders();
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reqType = "Create";
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		List<SalesInvoiceDCResponse> responseModelList = spareSaleInvoiceService.deliveryChallanDtl(partyBranchId, reqType);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Spare PO Category List on " + LocalDate.now());
			codeResponse.setMessage("Fetch Spare PO Category List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Fetch Spare PO Category List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping(value="/getPartDetailsByDCNumber")
	public ResponseEntity getDcPartDetail(@RequestParam() String deliveryChallanNumber, 
			OAuth2Authentication authentication, Device device,HttpServletRequest request) {
		
		HttpHeaders headers = new HttpHeaders();
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		List<COpartDetailResponse> responseModelList = spareSaleInvoiceService.getDcPartDetail(deliveryChallanNumber);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Spare PO Category List on " + LocalDate.now());
			codeResponse.setMessage("Fetch Spare PO Category List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Fetch Spare PO Category List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@GetMapping("/fetchPoHdrAndPartDetails")
	public ResponseEntity<?> fetchPoHdrAndPartDetails(@RequestParam(value= "poHdrId") Integer poHdrId,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		
		ApiResponse<?> responseModel = spareSaleInvoiceService.fetchPoHdrAndPartDetails(userCode,poHdrId );
		if (responseModel != null) {
			codeResponse.setCode(String.valueOf(responseModel.getStatus()));
			codeResponse.setDescription("Spare HDR details List on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMessage());
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Spare HDR details NOT Found ");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@PostMapping("/fetchTaxDetails")
	public ResponseEntity<?> fetchTaxDetails(@RequestBody TaxDetailsRequest taxDetailRequest,
			OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		PartTaxCalCulationResponse partTaxCalCulationResponse = spareSaleInvoiceService
				.fetchTaxDetails(taxDetailRequest, userCode);
		if (partTaxCalCulationResponse != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Customer Order Number List on " + formatter.format(new Date()));
			codeResponse.setMessage("Customer Order Number List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Customer Order Number List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(partTaxCalCulationResponse);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@PostMapping("/createSpareSaleInvoice")
	public ResponseEntity<?> createSpareSaleInvoice(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody SpareSalesInvoiceRequest salesInvoiceRequest, OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}

		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		SpareSalesInvoiceResponse spareSalesInvoiceResponse = spareSaleInvoiceService.createSpareSaleInvoice(userCode, salesInvoiceRequest);
		if (spareSalesInvoiceResponse.getStatusCode() == 201) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Spare Sales Invoice Created on " + formatter.format(new Date()));
			codeResponse.setMessage(spareSalesInvoiceResponse.getMsg());
		} else if (spareSalesInvoiceResponse.getStatusCode() == 500) {
			codeResponse.setCode(String.valueOf(spareSalesInvoiceResponse.getStatusCode()));
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage(spareSalesInvoiceResponse.getMsg());
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(spareSalesInvoiceResponse);
		return ResponseEntity.ok(userAuthResponse);
	}

}
