/**
 * 
 */
package com.hitech.dms.web.controller.pr.grn.create;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.app.exceptions.ErrorDetails;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.pr.grn.create.PurchaseReturnCreateDao;
import com.hitech.dms.web.model.pr.create.request.PurchaseReturnCreateRequestModel;
import com.hitech.dms.web.model.pr.create.response.PurchaseReturnCreateResponseModel;
import com.hitech.dms.web.validators.pr.grn.create.PurchaseReturnCreateValidator;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
//@Validated
@RestController
@RequestMapping("/purchaseReturn")
@SecurityRequirement(name = "hitechApis")
public class PurchaseReturnCreateController {
	private static final Logger logger = LoggerFactory.getLogger(PurchaseReturnCreateController.class);
	@Autowired
	private PurchaseReturnCreateValidator bindingResultValidator;

	@InitBinder("requestModel")
	public void initMerchantOnlyBinder(WebDataBinder binder) {
		binder.addValidators(bindingResultValidator);
	}

	@Autowired
	private PurchaseReturnCreateDao purchaseReturnCreateDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	}

	@PostMapping(value = "/createMachinePurchaseReturn")
	public ResponseEntity<?> createMachinePurchaseReturn(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody PurchaseReturnCreateRequestModel requestModel, BindingResult bindingResult,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		PurchaseReturnCreateResponseModel responseModel = null;
		if (bindingResult.hasErrors()) {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Machine Purchase Return not created or server side error.");

			ErrorDetails errorDetails = new ErrorDetails(HttpStatus.BAD_REQUEST, "Validation failed");
			errorDetails.setCount(bindingResult.getErrorCount());
			errorDetails.setStatus(HttpStatus.BAD_REQUEST);
			List<String> errors = new ArrayList<>();
			bindingResult.getAllErrors().forEach(error -> errors.add(error.getDefaultMessage()));
			errorDetails.setErrors(errors);

			userAuthResponse.setResponseCode(codeResponse);
			userAuthResponse.setResponseData(errorDetails);

			return ResponseEntity.ok(userAuthResponse);
		}
		responseModel = purchaseReturnCreateDao.createMachinePurchaseReturn(authorizationHeader, userCode,
				requestModel);
		if (responseModel != null && responseModel.getStatusCode().compareTo(WebConstants.STATUS_CREATED_201) == 0) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Successful on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Error While Creating Machine Purchase Return.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/downloadFile/{docPath}/{fileName:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, @PathVariable("docPath") String docPath,
			@RequestParam(required = false) Long id, HttpServletRequest request) throws MalformedURLException {
		// Load file as Resource
		String filePath=null;
		String property = System.getProperty("os.name");
		if (property.contains("Windows")) {
			filePath = "C:\\VST-DMS-APPS\\FILES\\RCUPDATE\\";
		} else {
			filePath = "/var/VST-DMS-APPS/FILES/RCUPDATE/";
		}
		
		
		
		Resource resource = purchaseReturnCreateDao.loadFileAsResource(fileName, docPath, id,filePath);
		System.out.println("resource  "+resource.toString());
		logger.info("resource "+resource.toString());

		// Try to determine file's content type
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			logger.info("Could not determine file type.");
		}

		// Fallback to the default content type if type could not be determined
		if (contentType == null) {
			contentType = "application/octet-stream";
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment ; filename = " + resource.getFilename());
		headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
		
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.headers(headers).body(resource);
		
		/*
		 * return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
		 * .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +
		 * resource.getFilename() + "\"") .body(resource);
		 */
	}
	
}
