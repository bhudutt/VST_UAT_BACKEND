package com.hitech.dms.web.controller.updateoldchassis;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.app.utils.ApiResponse;
import com.hitech.dms.web.model.updateoldchassis.UpdateOldChassisRequest;
import com.hitech.dms.web.service.updateoldchassis.UpdateOldChassisService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/updateOldChassis")
@SecurityRequirement(name = "hitechApis")
public class UpdateOldChassisController {
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	@Autowired
	private UpdateOldChassisService updateOldChassisService;
	
	
	
	/**
	 * @author mahesh.kumar
	 * @param chassisNo
	 * @return ResponseEntity
	 */
	@GetMapping("/autoSearchChassisNo")
	public ResponseEntity<?> autoSearchChassisNo(@RequestParam String chassisNo,
			OAuth2Authentication authentication) 
	{
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();

		ApiResponse<?> response = updateOldChassisService.autoSearchChassisNo(chassisNo);

		if (response != null && response.getResult()!= null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Auto Search Chassis Number on " + formatter.format(new Date()));
			codeResponse.setMessage(response.getMessage());
		} else {
			codeResponse.setCode("EC204");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Auto Search Chassis Number not Fetched or server side error.");
		}

		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	/**
	 * @author mahesh.kumar
	 * @param authentication
	 * @param chassisNo
	 * @return ResponseEntity
	 */
	
	@GetMapping("/fetchVinAndCustDetails")
	public ResponseEntity<?> fetchVinAndCustDetails(@RequestParam("chassisNo") String chassisNo,OAuth2Authentication authentication) {
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		ApiResponse<?>  response = updateOldChassisService.fetchVinAndCustDetails(chassisNo);
		if (response.getResult() != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Vin and Customer Details on" + formatter.format(new Date()));
			codeResponse.setMessage("Vin and Customer Details fetched");
		} else {
			codeResponse.setCode("EC204");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Vin and Customer Details Not Fetched or server side error.");
		}
		
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	
	/**
	 * @author mahesh.kumar
	 * @param requestModel
	 * @param bindingResult
	 * @param authentication
	 * @return ResponseEntity<?>
	 */
	@PostMapping("/updateOldChassis")
	public ResponseEntity<?> updateOldChassis(@RequestBody UpdateOldChassisRequest requestModel,
			BindingResult bindingResult, OAuth2Authentication authentication) 
	{	
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		
		ApiResponse<?> response = updateOldChassisService.updateOldChassis(userCode, requestModel);
		
		if (response != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Successful on " + formatter.format(new Date()));
			codeResponse.setMessage("Old Chassis Updated Successfully");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Error While Updating Old Chassis .");
		}
		
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		
		return ResponseEntity.ok(userAuthResponse);
	}

}
