/**
 * 
 */
package com.hitech.dms.web.controller.enquiry.transfer;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.enquiry.transfer.EnquiryTransferDao;
import com.hitech.dms.web.model.enquiry.transfer.request.EnquiryTransferRequestModel;
import com.hitech.dms.web.model.enquiry.transfer.response.EnquiryTransferResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Validated
@RestController
@RequestMapping("/enquiry")
public class EnquiryTransferControler {
	@Autowired
	private EnquiryTransferDao enquiryTransferDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@PostMapping("/salesman/transfer")
	public ResponseEntity<?> updateSalesmanEnquiry(@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@Valid @RequestBody EnquiryTransferRequestModel enquiryTransferRequestModel,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		EnquiryTransferResponseModel responseModel = enquiryTransferDao.transferEnqSalesman(authorizationHeader, userCode,
				enquiryTransferRequestModel, device);
		if (responseModel.getStatusCode() == 201) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Salesman Transfered on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else if (responseModel.getStatusCode() == 500) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Salesman Not Transfered or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);

//		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
//				.buildAndExpand(responseModel.getEnquiryHdrId()).toUri();
//
//		return ResponseEntity.created(location).build();
	}
}
