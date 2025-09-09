/**
 * 
 */
package com.hitech.dms.web.controller.models;

import java.math.BigDecimal;
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
import com.hitech.dms.web.dao.models.ProductListDao;
import com.hitech.dms.web.model.models.request.MachineItemDTLRequestModel;
import com.hitech.dms.web.model.models.request.MachineItemRequestModel;
import com.hitech.dms.web.model.models.request.ModelItemListRequestModel;
import com.hitech.dms.web.model.models.request.ModelsForSeriesSegmentRequestModel;
import com.hitech.dms.web.model.models.request.ProductListRequestModel;
import com.hitech.dms.web.model.models.request.SeriesSegmentRequestModel;
import com.hitech.dms.web.model.models.response.MachineItemResponseModel;
import com.hitech.dms.web.model.models.response.ModelByPcIdResponseModel;
import com.hitech.dms.web.model.models.response.ModelItemListResponseModel;
import com.hitech.dms.web.model.models.response.ModelsForSeriesSegmentResponseModel;
import com.hitech.dms.web.model.models.response.ProductListResponseModel;
import com.hitech.dms.web.model.models.response.SeriesSegmentResponseModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author dinesh.jakhar
 *
 */
@Validated
@RestController
@RequestMapping("/models")
@SecurityRequirement(name = "hitechApis")
public class ProductListController {
	private static final Logger logger = LoggerFactory.getLogger(ProductListController.class);

	@Autowired
	private ProductListDao productListDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@GetMapping("/fetchProductList/{productLevelName}/{searchParentText}/{pcID}")
	public ResponseEntity<?> fetchProductList(@PathVariable String productLevelName, @PathVariable Long pcID,
			@PathVariable String searchParentText, OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<ProductListResponseModel> responseModelList = productListDao.fetchProductList(userCode, productLevelName,
				searchParentText, pcID);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Product List on " + formatter.format(new Date()));
			codeResponse.setMessage("Product List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Product List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/fetchProductList")
	public ResponseEntity<?> fetchProductList(@RequestBody ProductListRequestModel productListRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<ProductListResponseModel> responseModelList = productListDao.fetchProductList(userCode,
				productListRequestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Product List on " + formatter.format(new Date()));
			codeResponse.setMessage("Product List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Product List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/fetchSeriesSegmentList")
	public ResponseEntity<?> fetchSeriesSegmentList(@RequestBody SeriesSegmentRequestModel ssRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<SeriesSegmentResponseModel> responseModelList = productListDao.fetchSeriesSegmentList(userCode,
				ssRequestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Product List on " + formatter.format(new Date()));
			codeResponse.setMessage("Series Segment List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Series Segment List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/fetchModelsForSeriesSegment")
	public ResponseEntity<?> fetchModelsForSeriesSegment(@RequestBody ModelsForSeriesSegmentRequestModel ssRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<ModelsForSeriesSegmentResponseModel> responseModelList = productListDao
				.fetchModelsForSeriesSegment(userCode, ssRequestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Model List For Series Segment on " + formatter.format(new Date()));
			codeResponse.setMessage("Model List For Series Segment Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Model List For Series Segment Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/fetchItemDTLList")
	public ResponseEntity<?> fetchItemDTLList(@RequestBody MachineItemRequestModel ssRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<MachineItemResponseModel> responseModelList = productListDao.fetchItemDTLList(userCode, ssRequestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Machine Item List on " + formatter.format(new Date()));
			codeResponse.setMessage("Machine Item List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Machine Item List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/fetchItemDTL")
	public ResponseEntity<?> fetchItemDTL(@RequestBody MachineItemDTLRequestModel ssRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		MachineItemResponseModel responseModel = productListDao.fetchItemDTL(userCode, ssRequestModel);
		if (responseModel != null) {
			
			if(responseModel.getNdp() !=null && responseModel.getNdp().compareTo(BigDecimal.ZERO) != 0) {
				codeResponse.setCode("EC200");
				codeResponse.setDescription("Fetch Machine Item on " + formatter.format(new Date()));
				codeResponse.setMessage("Machine Item Detail Successfully fetched");
			}else {
				codeResponse.setCode("EC200");
				codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
				codeResponse.setMessage("Machine Item Detail Not Fetched or server side error. ndp value not set");
			}
			
			
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Machine Item Detail Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@PostMapping("/fetchModelItemList")
	public ResponseEntity<?> fetchModelItemList(@RequestBody ModelItemListRequestModel ssRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<ModelItemListResponseModel> responseModelList = productListDao.fetchModelItemList(userCode, ssRequestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Machine Model Item List on " + formatter.format(new Date()));
			codeResponse.setMessage("Machine Item List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Machine Model Item List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/fetchModelListByPcId/{pcId}/{isFor}")
	public ResponseEntity<?> fetchModelListByPcId(@PathVariable Integer pcId,
			@PathVariable(required = false) String isFor, OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<ModelByPcIdResponseModel> responseModelList = productListDao.fetchModelListByPcId(userCode, pcId, isFor);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Model List on " + formatter.format(new Date()));
			codeResponse.setMessage("Model List based on PC Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Model List based on PC Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
}
