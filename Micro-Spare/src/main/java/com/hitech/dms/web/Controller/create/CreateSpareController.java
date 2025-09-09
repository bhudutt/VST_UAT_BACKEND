package com.hitech.dms.web.Controller.create;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.app.exceptions.ErrorDetails;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.model.SpareModel.SparePoCreateResponseModel;
import com.hitech.dms.web.model.SpareModel.partSearchResponseModel;
import com.hitech.dms.web.model.spare.create.response.SearchPartsByCategoryRequest;
import com.hitech.dms.web.model.spare.create.response.SpareJobCardResponse;
import com.hitech.dms.web.model.spare.create.response.SparePOTcsTotalAmntResponse;
import com.hitech.dms.web.model.spare.create.response.SparePoCalculationResponse;
import com.hitech.dms.web.model.spare.create.response.SparePoCategoryResponse;
import com.hitech.dms.web.model.spare.create.response.SparePoDealerAndDistributerSearchResponse;
import com.hitech.dms.web.model.spare.create.response.SparePoPartUploadResponse;
import com.hitech.dms.web.model.spare.create.response.SparePoStatusResponse;
import com.hitech.dms.web.model.spare.create.response.SparePoTypesResponse;
import com.hitech.dms.web.model.spare.create.response.SubProductCategoryResponse;
import com.hitech.dms.web.model.spare.create.resquest.SparePoCalculationRequest;
import com.hitech.dms.web.model.spare.create.resquest.SparePoDealerAndDistributorRequest;
import com.hitech.dms.web.model.spare.create.resquest.SparePoHeaderRequest;
import com.hitech.dms.web.model.spare.create.resquest.SparePoTcsCalculationRequest;
import com.hitech.dms.web.model.spare.search.response.partSearchDetailsResponse;
import com.hitech.dms.web.service.sparePo.createSparePoService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/create")
@Slf4j
@Validated
public class CreateSpareController {
	@Autowired
	private createSparePoService spareService;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	/**
	 * 
	 * @param authorizationHeader
	 * @param requestModel
	 * @param bindingResult
	 * @param authentication
	 * @param device
	 * @param request
	 * @return
	 */
	@PostMapping(value = "/createSparePO")
	public ResponseEntity<?> createSparePO(@RequestBody SparePoHeaderRequest requestModel, BindingResult bindingResult,
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		SparePoCreateResponseModel responseModel = null;
		System.out.println("requestModel::::::::::::::Controller" + requestModel);
		if (bindingResult.hasErrors()) {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Spare PO not created or server side error.");

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
		responseModel = spareService.createSparePODetails(authorizationHeader, userCode, requestModel, device);
		if (responseModel != null && responseModel.getStatusCode().compareTo(WebConstants.STATUS_CREATED_201) == 0) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Successful on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Error While Creating Spare PO.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}

	/**
	 * 
	 * @param authorizationHeader
	 * @param requestModel
	 * @param bindingResult
	 * @param authentication
	 * @param device
	 * @param request
	 * @return
	 */
	@PostMapping(value = "/saveSparePO")
	public ResponseEntity<?> saveSparePO(@RequestBody SparePoHeaderRequest requestModel, BindingResult bindingResult,
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		SparePoCreateResponseModel responseModel = null;
		if (bindingResult.hasErrors()) {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Spare PO not saved or server side error.");
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
		responseModel = spareService.saveSparePODetails(authorizationHeader, userCode, requestModel, device);
		if (responseModel != null && responseModel.getStatusCode().compareTo(WebConstants.STATUS_CREATED_201) == 0) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Successful on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Error While saveing Spare PO.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}

	/**
	 * this api used for fetch spare Po types
	 * 
	 * @param authentication
	 * @param device
	 * @param request
	 * @return
	 */
	@GetMapping("/getSparePoTypes")
	public ResponseEntity<?> getTypes(OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<SparePoTypesResponse> responseModelList = spareService.getAllTypes();
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Spare PO Type List on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Spare PO Type List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Spare PO Type List Not Fetched ");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	/**
	 * # for the fetch Spare Po Status
	 * 
	 * @param authentication
	 * @param device
	 * @param request
	 * @return [DRAFT, CANCELLED , REALASE, PARTIAL INVOICE, FULLY INVOICE,ORDER
	 *         ACKNOWLEDGE]
	 */
	@GetMapping("/getSparePoStatus")
	public ResponseEntity<?> getStatus(OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<SparePoStatusResponse> responseModelList = spareService.getAllSparePoStatus();
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Spare PO Status List on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Spare PO Status List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Spare PO Status List Not Fetched ");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	/**
	 * this api used for fetch all category of spare
	 * 
	 * @param authentication
	 * @param device
	 * @param request
	 * @return
	 */
	@GetMapping("/getSparePoAllCategory")
	public ResponseEntity<?> getAllCategory(OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<SparePoCategoryResponse> responseModelList = spareService.getAllSparePoCategory();
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Spare PO Category List on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Spare PO Category List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Spare PO Category List Not Fetched ");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/getSpareJobCardList")
	public ResponseEntity<?> getAllSpareJobCardList(@RequestParam(name = "BranchId") int BranchId,
			OAuth2Authentication authentication, Device device, HttpServletRequest httpServlet) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAutResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = new SimpleDateFormat();
		List<SpareJobCardResponse> jobCardResponse = spareService.getAllJobCardByBranchId(BranchId);
		if (jobCardResponse != null && !jobCardResponse.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Spare PO JobCard List on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Spare PO JobCard List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Spare PO JobCard List Not Fetched ");
		}
		userAutResponse.setResponseCode(codeResponse);
		userAutResponse.setResponseData(jobCardResponse);
		return ResponseEntity.ok(userAutResponse);
	}

	/**
	 * 
	 * @param authorizationHeader
	 * @param partSearchRequestModel
	 * @param authentication
	 * @param device
	 * @param request
	 * @return
	 */
	@PostMapping("/fetchPartNumberByCategory")
	public ResponseEntity<?> fetchPartNumberList(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody SearchPartsByCategoryRequest searchRequest, OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<partSearchResponseModel> responseModelList = spareService.fetchPartNumberByCategory(userCode,
				searchRequest);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Part No List on " + formatter.format(new Date()));
			codeResponse.setMessage("Part Number Fetched Successfully fetched");
		} else {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Part Number Not Fetched ");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	/**
	 * 
	 * @param authorizationHeader
	 * @param dealerCode
	 * @param authentication
	 * @param device
	 * @param request
	 * @return
	 */

	@PostMapping("/getDealerAndDistributor")
	public ResponseEntity<?> getDealerAndDistributor(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody SparePoDealerAndDistributorRequest sparePoDealerAndDistributorRequest,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<SparePoDealerAndDistributerSearchResponse> responseModelList = spareService
				.getDealerAndDistributor(userCode, sparePoDealerAndDistributorRequest);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("dealer and distributor mapping List on " + formatter.format(new Date()));
			codeResponse.setMessage("dealer and distributor mapping Fetched Successfully fetched");
		} else {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("dealer and distributor mapping Not Fetched");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	/**
	 * api used for fetch part number
	 * 
	 * @param authorizationHeader
	 * @param partSearchRequestModel
	 * @param authentication
	 * @param device
	 * @param request
	 * @return
	 */

	@GetMapping("/fetchPartDetails/branchId/{branchId}/partId/{partId}/poCategoryId/{poCategoryId}")
	public ResponseEntity<?> fetchPartNumberList(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@PathVariable int partId, @PathVariable BigInteger branchId,@PathVariable int poCategoryId, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<partSearchDetailsResponse> responseModelList = spareService.fetchPartDetails(userCode, partId, branchId,poCategoryId);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Part details No List on " + formatter.format(new Date()));
			codeResponse.setMessage("Part details Fetched Successfully fetched");
		}else if(responseModelList.isEmpty()) {
			codeResponse.setCode("EC204");
			codeResponse.setDescription("Fetch Part No List on " + formatter.format(new Date()));
			codeResponse.setMessage("Price not available for selected part");
		}else {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Part details Not Fetched");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	/**
	 * 
	 * @param authorizationHeader
	 * @param partId
	 * @param authentication
	 * @param device
	 * @param request
	 * @return
	 */
//	@GetMapping("/fetchPartDetailsByPartNo/{partNumber}")
//	public ResponseEntity<?> fetchPartNumberListByPartNumber(
//			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
//			@PathVariable String partNumber, OAuth2Authentication authentication, Device device,
//			HttpServletRequest request) {
//		String userCode = null;
//		if (authentication != null) {
//			userCode = authentication.getUserAuthentication().getName();
//		}
//		HeaderResponse userAuthResponse = new HeaderResponse();
//		MessageCodeResponse codeResponse = new MessageCodeResponse();
//		SimpleDateFormat formatter = getSimpleDateFormat();
//		List<partSearchDetailsResponse> responseModelList = spareService.fetchPartDetailsByPartNumber(userCode,
//				partNumber);
//		if (responseModelList != null && !responseModelList.isEmpty()) {
//			codeResponse.setCode("EC200");
//			codeResponse.setDescription("Fetch Part details No List on " + formatter.format(new Date()));
//			codeResponse.setMessage("Part details Fetched Successfully fetched");
//		} else {
//			codeResponse.setCode("EC500");
//			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
//			codeResponse.setMessage("Part details Not Fetched or server side error.");
//		}
//		userAuthResponse.setResponseCode(codeResponse);
//		userAuthResponse.setResponseData(responseModelList);
//		return ResponseEntity.ok(userAuthResponse);
//	}

	/**
	 * 
	 * @param authorizationHeader
	 * @param branch
	 * @param file
	 * @param authentication
	 * @param device
	 * @param request
	 * @return
	 */
	@PostMapping("/uploadSparePo")
	public ResponseEntity<?> uploadMslSpare(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestParam() Integer dealer, @RequestParam() BigInteger branch, @RequestParam (value ="productCategoryId") Integer productCategoryId, 
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
		SparePoPartUploadResponse responseModel = null;
		if (isFileExist) {
			responseModel = spareService.validateUploadedFile(authorizationHeader, userCode, branch, dealer,productCategoryId, file);
			if (responseModel != null
					&& responseModel.getStatusCode().compareTo(WebConstants.STATUS_CREATED_201) == 0) {
				codeResponse.setCode("EC200");
				codeResponse.setDescription("Successful on " + formatter.format(new Date()));
				codeResponse.setMessage(responseModel.getMsg());
			} else {
				codeResponse.setCode("EC500");
				codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
				codeResponse.setMessage("Error While Uploading Spare Part .xlsx file.");
			}
		} else {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Please Upload Spare Part .xlsx file.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}

	/**
	 * 
	 * @param requestModel
	 * @param authentication
	 * @return
	 */
	@PostMapping("/calculateSparePoItemAmount")
	public ResponseEntity<?> calculateSparePOItemAmount(@RequestBody SparePoCalculationRequest requestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<SparePoCalculationResponse> responseModelList = spareService.calculateSparePOItemAmount(userCode,
				requestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Spare Po item amount on " + formatter.format(new Date()));
			codeResponse.setMessage("SparePo Item Amount Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("SparePo Item Amount Not Fetched ");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	/**
	 * 
	 * @param requestModel
	 * @param authentication
	 * @return
	 */
	@PostMapping("/calculateSparePoTcsTotalAmount")
	public ResponseEntity<?> calculateMachinePOTotalAmount(@RequestBody SparePoTcsCalculationRequest requestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<SparePOTcsTotalAmntResponse> responseModelList = spareService.calculateSparePoTcsTotalAmount(userCode,
				requestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Geo List By Pin Id on " + formatter.format(new Date()));
			codeResponse.setMessage("Spare TCS Total PO Amount Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Spare TCS Total PO Amount Not Fetched ");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/getSubProductCategoryList/{category_id}")
	public ResponseEntity<?> getSubProductCategoryList(OAuth2Authentication authentication, Device device,
		   @PathVariable Integer  category_id, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<SubProductCategoryResponse> responseModelList = spareService.getSubProductCategoryList(category_id);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch sub product category List on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch  sub product category List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch  sub product category List Not Fetched ");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

}
