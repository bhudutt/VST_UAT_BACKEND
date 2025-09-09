package com.hitech.dms.web.partsStockController.create;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.partsStock.Service.PartsStockUploadService;
import com.hitech.dms.web.partsStockController.create.response.PartsUploadCreateResponse;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/create/partsStock")
@Slf4j
@Validated
public class PartsStockUploadController {

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@Autowired
	PartsStockUploadService partsUploadService;

	/**
	 * 
	 * @param authorizationHeader
	 * @param branch
	 * @param file
	 * @param authentication
	 * @param device
	 * @param request
	 * @return
	 * 
	 */

	@PostMapping("/uploadPartsStock")
	public ResponseEntity<?> uploadPartsStock(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestParam() Integer dealer, @RequestParam() Integer dealerCode, @RequestParam() Integer branchCode,
			@RequestPart(required = false) MultipartFile file, OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {

		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}

		boolean isFileExist = true;
		if (file == null || file.isEmpty()) {

			isFileExist = false;
		}

		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		PartsUploadCreateResponse responseModel = null;
		if (isFileExist) {

			responseModel = partsUploadService.partsUploadService(authorizationHeader, userCode, dealer, branchCode,
					file);
			//System.out.println("responseModel at controlller {} with message  " +responseModel.getMessage()+"code is "+responseModel.getStatusCode()+ responseModel);
			if (responseModel != null
					&& responseModel.getStatusCode().compareTo(WebConstants.STATUS_CREATED_201) == 0) {
				codeResponse.setCode("EC200");
				codeResponse.setDescription("Successful on " + formatter.format(new Date()));
				codeResponse.setMessage(responseModel.getMessage());
			} 
			else
			{
				codeResponse.setCode(responseModel.getStatusCode().toString());
				codeResponse.setMessage(responseModel.getMessage());
			}
		} 
		
		
		else {
			codeResponse.setCode("EC404");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Please Fill Upload parts Stock .xlsx file.");
		}

		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}

}
