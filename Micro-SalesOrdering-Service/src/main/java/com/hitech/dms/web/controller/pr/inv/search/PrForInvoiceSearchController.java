/**
 * 
 */
package com.hitech.dms.web.controller.pr.inv.search;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.pr.inv.search.PrForInvoiceSearchDao;
import com.hitech.dms.web.model.pr.inv.search.request.PrForInvoiceSearchRequestModel;
import com.hitech.dms.web.model.pr.inv.search.response.PrForInvoiceSearchMainResponseModel;
import com.hitech.dms.web.model.pr.inv.search.response.PurchaseReturnNo;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
//@Validated
@RestController
@RequestMapping("/purchaseReturn")
@SecurityRequirement(name = "hitechApis")
public class PrForInvoiceSearchController {
	private static final Logger logger = LoggerFactory.getLogger(PrForInvoiceSearchController.class);

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@Autowired
	private PrForInvoiceSearchDao invoiceSearchDao;

	@PostMapping("/searchSalesPurchaseReturnInvList")
	public ResponseEntity<?> searchSalesPurchaseReturnInvList(@RequestBody PrForInvoiceSearchRequestModel requestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		PrForInvoiceSearchMainResponseModel responseModel = invoiceSearchDao.searchSalesPurchaseReturnInvList(userCode,
				requestModel);
		if (responseModel != null && responseModel.getSearchList() != null
				&& !responseModel.getSearchList().isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription(
					"Fetch Machine Purchase Return Invoice Search List on " + formatter.format(new Date()));
			codeResponse.setMessage("Machine Purchase Return Search List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Machine Purchase Return Search Invoice List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@GetMapping("/getPurchaseReturnNoList")
	public ResponseEntity<?> getPurchaseReturnNoList(@RequestParam(name = "searchText") String searchText, OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		
		List<PurchaseReturnNo> responseModel = invoiceSearchDao.getPurchaseReturnNoList(searchText, userCode);
		
		if (responseModel != null 
				&& !responseModel.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Purchase Return Number Search List on " + new Date());
			codeResponse.setMessage(" Purchase Return Number Search List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + new Date());
			codeResponse.setMessage(" Purchase Return Number Search List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	

	
}
