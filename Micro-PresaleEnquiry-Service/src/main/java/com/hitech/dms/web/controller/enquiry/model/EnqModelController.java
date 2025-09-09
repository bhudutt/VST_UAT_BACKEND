/**
 * 
 */
package com.hitech.dms.web.controller.enquiry.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.enquiry.model.EnqModelDao;
import com.hitech.dms.web.model.enquiry.model.request.BrandListRequestModel;
import com.hitech.dms.web.model.enquiry.model.request.EnqModelRequestModel;
import com.hitech.dms.web.model.enquiry.model.request.ModelItemDTLRequestModel;
import com.hitech.dms.web.model.enquiry.model.request.ModelItemListRequestModel;
import com.hitech.dms.web.model.enquiry.model.request.ModelVariantListRequestModel;
import com.hitech.dms.web.model.enquiry.model.response.BrandListResponseModel;
import com.hitech.dms.web.model.enquiry.model.response.EnqModelResponseModel;
import com.hitech.dms.web.model.enquiry.model.response.ModelItemDTLResponseModel;
import com.hitech.dms.web.model.enquiry.model.response.ModelItemListResponseModel;
import com.hitech.dms.web.model.enquiry.model.response.ModelVariantListResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
@Validated
@RestController
@RequestMapping("/model")
@SecurityRequirement(name = "hitechApis")
public class EnqModelController {
	private static final Logger logger = LoggerFactory.getLogger(EnqModelController.class);

	@Autowired
	private EnqModelDao enqModelDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@GetMapping("/fetchEnqModelList/{pcId}/{activityPlanID}/{searchText}/{activityId}/{dealerId}")
	public ResponseEntity<?> fetchEnqPlanActivityList(@PathVariable Integer pcId, @PathVariable Long activityPlanID,
			@PathVariable String searchText, @PathVariable Long activityId, @PathVariable Long dealerId,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<EnqModelResponseModel> responseModelList = enqModelDao.fetchEnqModelList(userCode, pcId, activityPlanID,
				searchText, activityId, dealerId);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Enquiry Plan Activity List on " + formatter.format(new Date()));
			codeResponse.setMessage("Enquiry Plan Activity List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Enquiry Plan Activity List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/fetchEnqModelList")
	public ResponseEntity<?> fetchEnqPlanActivityList(@RequestBody EnqModelRequestModel enqModelRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<EnqModelResponseModel> responseModelList = enqModelDao.fetchEnqModelList(userCode, enqModelRequestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Enquiry Plan Activity List on " + formatter.format(new Date()));
			codeResponse.setMessage("Enquiry Plan Activity List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Enquiry Plan Activity List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/fetchEnqVariantModelList/{modelID}")
	public ResponseEntity<?> fetchEnqVariantModelList(@PathVariable Long modelID, OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<ModelVariantListResponseModel> responseModelList = enqModelDao.fetchEnqVariantModelList(userCode, modelID);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Enquiry Variant Model List on " + formatter.format(new Date()));
			codeResponse.setMessage("Enquiry Variant Model List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Enquiry Variant Model List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/fetchEnqVariantModelList")
	public ResponseEntity<?> fetchEnqVariantModelList(
			@RequestBody ModelVariantListRequestModel modelVariantListRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<ModelVariantListResponseModel> responseModelList = enqModelDao.fetchEnqVariantModelList(userCode,
				modelVariantListRequestModel.getModelID());
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Enquiry Variant Model List on " + formatter.format(new Date()));
			codeResponse.setMessage("Enquiry Variant Model List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Enquiry Variant Model List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/fetchEnqModelItemList/{pcID}/{activityPlanID}/{modelID}/{searchValue}/{activityId}/{dealerId}/{isFor}")
	public ResponseEntity<?> fetchEnqModelItemList(@PathVariable Integer pcID, @PathVariable String searchValue,
			@PathVariable Long activityPlanID, @PathVariable Long modelID, @PathVariable Long activityId,
			@PathVariable Long dealerId, @PathVariable String isFor, OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<ModelItemListResponseModel> responseModelList = enqModelDao.fetchEnqModelItemList(userCode, pcID,
				activityPlanID, modelID, searchValue, activityId, dealerId, isFor);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Enquiry Model Item List on " + formatter.format(new Date()));
			codeResponse.setMessage("Enquiry Model Item Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Enquiry Model Item List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/fetchEnqModelItemList")
	public ResponseEntity<?> fetchEnqModelItemList(@RequestBody ModelItemListRequestModel modelItemListRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<ModelItemListResponseModel> responseModelList = enqModelDao.fetchEnqModelItemList(userCode,
				modelItemListRequestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Enquiry Model Item List on " + formatter.format(new Date()));
			codeResponse.setMessage("Enquiry Model Item Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Enquiry Model Item List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/fetchEnqModelItemDTL/{itemID}")
	public ResponseEntity<?> fetchEnqModelItemDTL(@PathVariable Long itemID, OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		ModelItemDTLResponseModel responseModel = enqModelDao.fetchEnqModelItemDTL(userCode, itemID);
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Enquiry Model Item Detail on " + formatter.format(new Date()));
			codeResponse.setMessage("Enquiry Model Item Detail Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Enquiry Model Item Detail Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/fetchEnqModelItemDTL")
	public ResponseEntity<?> fetchEnqModelItemDTL(@RequestBody ModelItemDTLRequestModel modelItemDTLRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		ModelItemDTLResponseModel responseModel = enqModelDao.fetchEnqModelItemDTL(userCode, modelItemDTLRequestModel);
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Enquiry Model Item Detail on " + formatter.format(new Date()));
			codeResponse.setMessage("Enquiry Model Item Detail Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Enquiry Model Item Detail Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/fetchEnqBrandList/{isFor}")
	public ResponseEntity<?> fetchEnqBrandList(@PathVariable String isFor, OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<BrandListResponseModel> responseModelList = enqModelDao.fetchEnqBrandList(userCode, isFor);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Enquiry Brand List Detail on " + formatter.format(new Date()));
			codeResponse.setMessage("Enquiry Model Item Detail Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("EnquiryBrand List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/fetchEnqBrandList")
	public ResponseEntity<?> fetchEnqBrandList(@RequestBody BrandListRequestModel brandListRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<BrandListResponseModel> responseModelList = enqModelDao.fetchEnqBrandList(userCode, brandListRequestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Enquiry Brand List Detail on " + formatter.format(new Date()));
			codeResponse.setMessage("Enquiry Model Item Detail Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("EnquiryBrand List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
}
