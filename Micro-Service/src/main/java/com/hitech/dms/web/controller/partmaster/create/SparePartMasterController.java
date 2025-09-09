package com.hitech.dms.web.controller.partmaster.create;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.app.exceptions.ErrorDetails;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.controller.storemaster.create.StoreMasterController;
import com.hitech.dms.web.dao.partmaster.create.PartMasterCreateDao;
import com.hitech.dms.web.dao.storemaster.create.StoreMasterCreateDao;
import com.hitech.dms.web.model.partmaster.create.request.PartMasterFormRequestModel;
import com.hitech.dms.web.model.partmaster.create.request.PartStockBinModel;
import com.hitech.dms.web.model.partmaster.create.response.PartMasterCreateResponseModel;
import com.hitech.dms.web.model.storemaster.create.request.StoreMasterFormRequestModel;
import com.hitech.dms.web.model.storemaster.create.response.StoreMasterCreateResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/sparepartmaster")
@SecurityRequirement(name = "hitechApis")
public class SparePartMasterController {

private static final Logger logger = LoggerFactory.getLogger(SparePartMasterController.class);
	
	@Autowired
	private PartMasterCreateDao partMasterCreateDao;
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	}
	
	@PostMapping(value = "/createPartMaster")
	public ResponseEntity<?> createStoreMaster(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody PartMasterFormRequestModel requestModel, BindingResult bindingResult,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		PartMasterCreateResponseModel responseModel = null;
		if (bindingResult.hasErrors()) {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("part Master not created or server side error.");

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
		
		manageGridStocekDetail(requestModel);
		
		
		/*
		 * try { uploadDocuments(requestModel, userCode); } catch (IOException
		 * e) {
		 * 
		 * }
		 */
		
		responseModel = partMasterCreateDao.createPartMaster(authorizationHeader, userCode, requestModel);
		if (responseModel != null && responseModel.getStatusCode().compareTo(WebConstants.STATUS_CREATED_201) == 0) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Successful on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Error While Creating Machine GRN.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	private void manageGridStocekDetail(PartMasterFormRequestModel branchPartMasterModel) {
		List<PartStockBinModel> partStockBinModelList = branchPartMasterModel.getPartStockBinModelList();
		Iterator<?> iterator = partStockBinModelList.iterator();
		while (iterator.hasNext()) {
			PartStockBinModel binModel = (PartStockBinModel) iterator.next();
			if (binModel.getBranchStoreId() == null || binModel.getBranchStoreId() == 0) {
				iterator.remove();
			}
		}
	}
}
