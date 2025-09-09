/**
 * 
 */
package com.hitech.dms.web.controller.grn.invoice.autosearch;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.grn.invoice.autosearch.InvoicesForGrnDao;
import com.hitech.dms.web.model.grn.invoice.autosearch.request.InvoicesForGrnRequestModel;
import com.hitech.dms.web.model.grn.invoice.autosearch.response.InvoicesForGrnResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
@Validated
@RestController
@RequestMapping("/grn")
@SecurityRequirement(name = "hitechApis")
public class InvoicesForGrnController {
	private static final Logger logger = LoggerFactory.getLogger(InvoicesForGrnController.class);

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@Autowired
	private InvoicesForGrnDao invoicesForGrnDao;

	@PostMapping("/fetchInvoiceListForGrn")
	public ResponseEntity<?> fetchInvoiceListForGrn(@RequestBody InvoicesForGrnRequestModel requestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<InvoicesForGrnResponseModel> responseModelList = invoicesForGrnDao.fetchInvoiceListForGrn(userCode,
				requestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Invoice List For Grn on " + formatter.format(new Date()));
			codeResponse.setMessage("Invoice List For Grn Successfully fetched");
		} else {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Invoice List For Grn Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
}
