/**
 * 
 */
package com.hitech.dms.web.controller.inv.autolist;

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
import com.hitech.dms.web.dao.inv.autolist.PoInvoiceAutoListDao;
import com.hitech.dms.web.model.inv.autolist.request.PoInvoiceAutoListRequestModel;
import com.hitech.dms.web.model.inv.autolist.response.PoInvoiceAutoListResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
@Validated
@RestController
@RequestMapping("/invoice")
@SecurityRequirement(name = "hitechApis")
public class PoInvoiceAutoListController {
	private static final Logger logger = LoggerFactory.getLogger(PoInvoiceAutoListController.class);

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@Autowired
	private PoInvoiceAutoListDao autoListDao;

	@PostMapping("/fetchPoListForInvoice")
	public ResponseEntity<?> fetchAllotListForDc(@RequestBody PoInvoiceAutoListRequestModel requestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<PoInvoiceAutoListResponseModel> responseModelList = autoListDao.fetchPoListForInvoice(userCode,
				requestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch PO List For Invoice on " + formatter.format(new Date()));
			codeResponse.setMessage("PO List For Invoice Successfully fetched");
		} else {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("PO List For Invoice Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
}
