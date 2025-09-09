package com.hitech.dms.web.Controller.spare.customer.order;

import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.hitech.dms.web.model.spara.customer.order.request.CustomerOrderDownloadTemplate;
import com.hitech.dms.web.model.spara.customer.order.request.CustomerOrderPartDetailsRequest;
import com.hitech.dms.web.model.spara.customer.order.request.CustomerOrderPartNoListRequest;
import com.hitech.dms.web.model.spara.customer.order.request.CustomerOrderPartNoRequest;
import com.hitech.dms.web.model.spara.customer.order.request.SpareCustomerOrderCancelRequest;
import com.hitech.dms.web.model.spara.customer.order.request.SpareCustomerOrderRequest;
import com.hitech.dms.web.model.spara.customer.order.request.SpareCustomerOrderUpdateListRequest;
import com.hitech.dms.web.model.spara.customer.order.response.SpareCustOrderCreateResponseModel;
import com.hitech.dms.web.model.spara.customer.order.response.SpareCustOrderPartDetailResponse;
import com.hitech.dms.web.model.spara.customer.order.response.SpareCustomerOrderPartUploadResponse;
import com.hitech.dms.web.model.spare.create.response.SparePoCategoryResponse;
import com.hitech.dms.web.service.spare.customer.order.SpareCustomerOrderService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Vivek.Gupta
 *
 */

@RestController
@RequestMapping("/api/v1/customerOrder")
@Slf4j
@Validated
public class CreateSpareCustomerOrderController {

	@Value("${file.upload-dir.customerTemplate}")
	private String templateDownloadPath;

	@Autowired
	private SpareCustomerOrderService spareCustomerOrderService;
	
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
	@PostMapping(value = "/create")
	public ResponseEntity<?> createSpareCustomerOrder(@Valid @RequestBody SpareCustomerOrderRequest requestModel,
			BindingResult bindingResult,
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {

		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SpareCustOrderCreateResponseModel responseModel = null;

		if (bindingResult.hasErrors()) {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Spare customer order not created or server side error.");

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
		responseModel = spareCustomerOrderService.createCustomerOrder(authorizationHeader, userCode, requestModel,
				device);
		if (responseModel != null && responseModel.getStatusCode().compareTo(WebConstants.STATUS_OK_200) == 0) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Successful on " + LocalDate.now());
			codeResponse.setMessage(responseModel.getMsg());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Error While Creating Spare Customer Order.");
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
	@PostMapping(value = "/edit")
	public ResponseEntity<?> editSpareCustomerOrder(@Valid @RequestBody SpareCustomerOrderRequest requestModel,
			BindingResult bindingResult,
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {

		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SpareCustOrderCreateResponseModel responseModel = null;

		if (bindingResult.hasErrors()) {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Spare customer order not created or server side error.");

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
		responseModel = spareCustomerOrderService.editSpareCustomerOrder(authorizationHeader, userCode, requestModel,
				device);
		if (responseModel != null && responseModel.getStatusCode().compareTo(WebConstants.STATUS_OK_200) == 0) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Successful on " + LocalDate.now());
			codeResponse.setMessage(responseModel.getMsg());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Error While Creating Spare Customer Order.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping(value = "/downloadTemplateOfUploadMsl")
	public ResponseEntity downloadTemplate(@RequestParam("filename") String filename, HttpServletResponse response,
			HttpServletRequest request) {
		HttpHeaders headers = new HttpHeaders();
		Resource resource = null;
		try {
			String filePath = "";
			String property = System.getProperty("os.name");
			if (property.contains("Windows")) {
				filePath = templateDownloadPath;
			} else {
				filePath = "/var/VST-DMS-APPS/FILES/Template/customer-order/";
			}

			Path path = Paths.get(filePath + filename);
			resource = new UrlResource(path.toUri());
			response.setContentType("application/octet-stream");
			headers.add("Content-Disposition", "attachment ; filename =" + filename);
			headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok().headers(headers).body(resource);

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
	@PostMapping("/fetchPartNumber")
	public ResponseEntity<?> fetchPartNumberList(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody CustomerOrderPartNoRequest requestModel, OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<partSearchResponseModel> responseModelList = spareCustomerOrderService.fetchPartNumber(userCode, requestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Part No List on " + formatter.format(new Date()));
			codeResponse.setMessage("Part Number Fetched Successfully fetched");
		}else if(responseModelList.isEmpty()) {
			codeResponse.setCode("EC204");
			codeResponse.setDescription("Fetch Part No List on " + formatter.format(new Date()));
			codeResponse.setMessage("Either Part not available or Price not available");
		}
		else {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Part Number Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
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
	@PostMapping("/customerOrderPartDetail")
	public ResponseEntity<?> customerOrderPartDetail(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody CustomerOrderPartNoRequest requestModel, OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<SpareCustOrderPartDetailResponse> responseModelList = spareCustomerOrderService.customerOrderPartDetail(userCode, requestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Part No List on " + formatter.format(new Date()));
			codeResponse.setMessage("Part Number Fetched Successfully fetched");
		} else {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Part Number Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	
	
	
	

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
	@PostMapping("/uploadSpareCO")
	public ResponseEntity<?> uploadMslSpare(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestParam(name = "branchId") BigInteger branchId, 
			@RequestParam(name = "productCategoryId") Integer productCategoryId,
			@RequestParam(name = "dealerId") Integer dealerId,
			@RequestParam(name = "partyTypeId") Integer partyTypeId,
			@RequestParam(name= "partyCodeId") Integer partyBranchId,
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
		SpareCustomerOrderPartUploadResponse responseModel = null;
		if (isFileExist) {
			responseModel = spareCustomerOrderService.validateUploadedFile(authorizationHeader, userCode, branchId,
					productCategoryId, dealerId, partyTypeId,partyBranchId, file);
			if (responseModel != null
					&& responseModel.getStatusCode().compareTo(WebConstants.STATUS_CREATED_201) == 0) {
				codeResponse.setCode("EC200");
				codeResponse.setDescription("Successful on " + formatter.format(new Date()));
				codeResponse.setMessage(responseModel.getMsg());
			}else if(responseModel.getStatusCode().compareTo(WebConstants.STATUS_EXPECTATION_FAILED_417) == 0) {
				codeResponse.setCode("EC417");
				codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
				codeResponse.setMessage(responseModel.getMsg());
			  }
				else {
				codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
				codeResponse.setMessage("Invalid data in excel sheet .xlsx file.");
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
	
//	
//	@PostMapping("/customerOrderNumber")
//	public ResponseEntity<?> createPartRequisition(@RequestBody CustOrderProductCtgRequestModel requestModel,
//			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
//		String userCode = null;
//		if (authentication != null) {
//			userCode = authentication.getUserAuthentication().getName();
//		}
//		HeaderResponse userAuthResponse = new HeaderResponse();
//		MessageCodeResponse codeResponse = new MessageCodeResponse();
//		SimpleDateFormat formatter = getSimpleDateFormat();
//		CustOrderProductCtgResponseModel responseModel = spareCustomerOrderService.getCustOrderProductCtg(userCode,requestModel,device);
//		
//		if (responseModel.getStatusCode() == 200) {
//			codeResponse.setCode("EC200");
//			codeResponse.setDescription("Created Customer order Number" + formatter.format(new Date()));
//			codeResponse.setMessage(responseModel.getMsg());
//		} else if (responseModel.getStatusCode() == 500) {
//			codeResponse.setCode("EC200");
//			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
//			codeResponse.setMessage("Requisition Not Added or server side error.");
//		}
//		userAuthResponse.setResponseCode(codeResponse);
//		userAuthResponse.setResponseData(responseModel);
//		return ResponseEntity.ok(userAuthResponse);
//
//	}
	
	
	/**
	 * this api used for fetch all category of spare
	 * 
	 * @param authentication
	 * @param device
	 * @param request
	 * @return
	 */
	@GetMapping("/getProductCategory")
	public ResponseEntity<?> getAllCategory(OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<SparePoCategoryResponse> responseModelList = spareCustomerOrderService.getAllCustOrderPoCategory();
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Spare PO Category List on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Spare PO Category List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Spare PO Category List Not Fetched or server side error.");
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

	@PostMapping("/fetchCOPartDetailsList")
	public ResponseEntity<?> fetchPartDetailsList(@RequestBody CustomerOrderPartDetailsRequest bean,
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			 OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<SpareCustOrderPartDetailResponse> responseModelList = spareCustomerOrderService.fetchCOPartDetails(userCode, bean);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Part details No List on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModelList.get(0).getMsg());
		} else {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Part details Not Fetched or server side error.");
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
	@GetMapping("/getProductSubCategory")
	public ResponseEntity<?> getAllSubCategory(OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<SparePoCategoryResponse> responseModelList = spareCustomerOrderService.getAllSubCategory();
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Spare PO Category List on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Spare PO Category List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Spare PO Category List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
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
	@PostMapping(value = "/update")
	public ResponseEntity<?> updateSpareCustomerOrder(@RequestBody SpareCustomerOrderUpdateListRequest requestModel,
			BindingResult bindingResult,
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {

		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SpareCustOrderCreateResponseModel responseModel = null;

		if (bindingResult.hasErrors()) {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Spare customer order not created or server side error.");

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
		responseModel = spareCustomerOrderService.updateCustomerOrder(authorizationHeader, userCode, requestModel.getPartDetailList(),
				device);
		if (responseModel != null && responseModel.getStatusCode().compareTo(WebConstants.STATUS_OK_200) == 0) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Successful on " + LocalDate.now());
			codeResponse.setMessage(responseModel.getMsg());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Error While Creating Spare Customer Order.");
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
	@PostMapping(value = "/cancel")
	public ResponseEntity<?> cancelSpareCustomerOrder(@RequestBody SpareCustomerOrderCancelRequest requestModel,
			BindingResult bindingResult,
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {

		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SpareCustOrderCreateResponseModel responseModel = null;

		if (bindingResult.hasErrors()) {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Spare customer order not cancel or server side error.");

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
		responseModel = spareCustomerOrderService.cancelCustomerOrder(authorizationHeader, userCode, requestModel,device);
		if (responseModel != null && responseModel.getStatusCode().compareTo(WebConstants.STATUS_OK_200) == 0) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Successful on " + LocalDate.now());
			codeResponse.setMessage(responseModel.getMsg());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Error While Creating Spare Customer Order.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@PostMapping(value = "/delete")
	public ResponseEntity<?> deletePartNofromDTL(@RequestBody CustomerOrderPartNoListRequest requestModel,
			BindingResult bindingResult,
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {

		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SpareCustOrderCreateResponseModel responseModel = null;

		if (bindingResult.hasErrors()) {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Spare customer order not delete or server side error.");

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
		responseModel = spareCustomerOrderService.deletePartNofromDTL(authorizationHeader, userCode, requestModel,device);
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Successful on " + LocalDate.now());
			codeResponse.setMessage(responseModel.getMsg());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Error While Creating Spare Customer Order.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	

}
