/**
 * 
 */
package com.hitech.dms.web.controller.quotation.search;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.quotation.search.QuotationSearchDao;
import com.hitech.dms.web.model.quotation.search.request.EnquiryDTLForQUORequestModel;
import com.hitech.dms.web.model.quotation.search.request.VehQuoSearchRequestModel;
import com.hitech.dms.web.model.quotation.search.response.EnquiryDTLForQUOResponseModel;
import com.hitech.dms.web.model.quotation.search.response.VehQuoSearchMainResponseModel;
import com.hitech.dms.web.model.quotation.search.response.VehQuoSearchResponse;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
@Validated
@RestController
@RequestMapping("/quotation")
@SecurityRequirement(name = "hitechApis")
public class QuotationSearchController {
	private static final Logger logger = LoggerFactory.getLogger(QuotationSearchController.class);

	@Autowired
	private QuotationSearchDao quotationSearchDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@PostMapping("/fetchEnquiryListForQuotation")
	public ResponseEntity<?> fetchEnquiryListForQuotation(
			@RequestBody EnquiryDTLForQUORequestModel enquiryListRequestModel, OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<EnquiryDTLForQUOResponseModel> responseModelList = quotationSearchDao
				.fetchEnquiryListForQuotation(userCode, enquiryListRequestModel);
		if (responseModelList != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Enquiry List For Quotation on " + formatter.format(new Date()));
			codeResponse.setMessage("Enquiry List For Quotation Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Enquiry List For Quotation Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/searchQuotationList")
	public ResponseEntity<?> searchQuotationList(@RequestBody VehQuoSearchRequestModel requestModel, Device device,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		VehQuoSearchMainResponseModel responseModel = quotationSearchDao.searchQuotationList(userCode, requestModel,
				device);
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Quotation Search List " + formatter.format(new Date()));
			codeResponse.setMessage("Quotation Search List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Quotation Search List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@PostMapping("/searchQuoList")
	public ResponseEntity<?> searchQuoList(@RequestBody VehQuoSearchRequestModel requestModel, Device device,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<VehQuoSearchResponse> responseModelList = null;
		VehQuoSearchMainResponseModel responseModel = quotationSearchDao.searchQuotationList(userCode, requestModel,
				device);
		if (responseModel != null) {
			responseModelList = responseModel.getResponseModelList();
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Quotation Search List " + formatter.format(new Date()));
			codeResponse.setMessage("Quotation Search List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Quotation Search List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
}
