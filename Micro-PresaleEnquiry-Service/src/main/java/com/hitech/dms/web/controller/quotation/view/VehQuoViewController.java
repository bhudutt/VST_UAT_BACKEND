/**
 * 
 */
package com.hitech.dms.web.controller.quotation.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.Valid;

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
import com.hitech.dms.web.dao.quotation.view.VehQuoViewDao;
import com.hitech.dms.web.model.quotation.view.request.VehQuoViewRequestModel;
import com.hitech.dms.web.model.quotation.view.response.VehQuoViewResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
@Validated
@RestController
@RequestMapping("/quotation")
@SecurityRequirement(name = "hitechApis")
public class VehQuoViewController {
	private static final Logger logger = LoggerFactory.getLogger(VehQuoViewController.class);

	@Autowired
	private VehQuoViewDao vehQuoViewDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@PostMapping("/fetchQuotationDTL")
	public ResponseEntity<?> fetchQuotationDTL(@Valid @RequestBody VehQuoViewRequestModel requestModel,
			OAuth2Authentication authentication, Device device) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		VehQuoViewResponseModel responseModel = vehQuoViewDao.fetchQuotationDTL(userCode, requestModel, device);
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Presales Quotation DTL on " + formatter.format(new Date()));
			codeResponse.setMessage("Presales Quotation Detail Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Presales Quotation Detail Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
}
