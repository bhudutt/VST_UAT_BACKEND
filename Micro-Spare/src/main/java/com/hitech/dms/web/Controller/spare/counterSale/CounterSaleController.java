package com.hitech.dms.web.Controller.spare.counterSale;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.app.config.ConnectionConfiguration;
import com.hitech.dms.app.utils.ApiResponse;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.model.spare.counterSale.CounterSaleResponse;
import com.hitech.dms.web.service.spare.counterSale.CounterSaleService;

@RestController
@RequestMapping("api/v1/counterSale")
public class CounterSaleController {

	@Value("${file.upload-dir.PickList}")
    private String downloadPath;
	
	@Autowired
	private ConnectionConfiguration dataSourceConnection;
	
	@Autowired
	CounterSaleService counterSaleService;
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	@GetMapping("/searchCustomerDetails")
	public ResponseEntity<?> searchCustomerOrderNumber(@RequestParam() String searchText,
			OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		ApiResponse<HashMap<BigInteger, String>> customerList = counterSaleService
				.searchCustomerDetails(searchText, userCode);
		if (customerList.getStatus() == WebConstants.STATUS_OK_200) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Customer List on " + formatter.format(new Date()));
			codeResponse.setMessage(customerList.getMessage());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage(customerList.getMessage());
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(customerList.getResult());
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/fetchCounterSale")
	public ResponseEntity<?> fetchCounterSale(@RequestParam() int counterSaleId,
			OAuth2Authentication authentication,
			Device device, HttpServletRequest request) throws ParseException {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		CounterSaleResponse counterSaleResponse = 
				counterSaleService.fetchCounterSale(counterSaleId);
		if (counterSaleResponse != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Counter Sale on " + formatter.format(new Date()));
			codeResponse.setMessage("Counter Sale Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Counter Sale Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(counterSaleResponse);
		return ResponseEntity.ok(userAuthResponse);
	}
}
