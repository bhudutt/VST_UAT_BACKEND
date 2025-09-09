/**
 * 
 */
package com.hitech.dms.web.controller.jobcard.create;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
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
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.model.jobcard.edit.request.EditFinalModel;
import com.hitech.dms.web.model.jobcard.request.ServiceBookingAndQuoationStatusRequestModel;
import com.hitech.dms.web.model.jobcard.request.jobCardCreateRequest;
import com.hitech.dms.web.model.jobcard.response.AutoSelectSrvCategoryAndTypeResponse;
import com.hitech.dms.web.model.jobcard.response.ChassisDetailsWithFlagResponse;
import com.hitech.dms.web.model.jobcard.response.InvoiceSearchResponse;
import com.hitech.dms.web.model.jobcard.response.JobCardApplicationUsedResponse;
import com.hitech.dms.web.model.jobcard.response.JobCardCategoryResponse;
import com.hitech.dms.web.model.jobcard.response.JobCardCreateResponse;
import com.hitech.dms.web.model.jobcard.response.JobCardImplementTypeResponse;
import com.hitech.dms.web.model.jobcard.response.JobChassisResponse;
import com.hitech.dms.web.model.jobcard.response.JobLabourMasterResponse;
import com.hitech.dms.web.model.jobcard.response.JobRepairCatgResponse;
import com.hitech.dms.web.model.jobcard.response.JobRepresentativeResponse;
import com.hitech.dms.web.model.jobcard.response.JobServiceActivityTypeResponse;
import com.hitech.dms.web.model.jobcard.response.JobServiceBookingResponse;
import com.hitech.dms.web.model.jobcard.response.JobServiceTypeResponse;
import com.hitech.dms.web.model.jobcard.response.JobStatusResponse;
import com.hitech.dms.web.model.jobcard.response.JobTechnicianListResponse;
import com.hitech.dms.web.model.jobcard.response.jobBillableTypeResponse;
import com.hitech.dms.web.model.jobcard.response.jobInventryChecklistReponse;
import com.hitech.dms.web.model.jobcard.response.jobpainscratchedResponse;
import com.hitech.dms.web.model.jobcard.save.request.InstallationUpdateRequest;
import com.hitech.dms.web.model.jobcard.save.request.JobCardCancelRequest;
import com.hitech.dms.web.model.jobcard.save.request.JobCardCloseRequest;
import com.hitech.dms.web.model.jobcard.save.request.JobCardFinalRequest;
import com.hitech.dms.web.model.jobcard.save.request.PdiDataUpdateRequest;
import com.hitech.dms.web.model.jobcard.search.request.SearchChassisRequest;
import com.hitech.dms.web.model.models.response.MasterDataModelResponse;
import com.hitech.dms.web.service.jobcard.JobCardService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author santosh.kumar
 *
 */
@RestController
@RequestMapping("/api/v1/jobcard")
@SecurityRequirement(name = "hitechApis")
public class JobCardCreateCantroller {
	@Autowired
	private JobCardService jobCardService;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	}

	/**
	 * 
	 * @param authorizationHeader
	 * @param authentication
	 * @param device
	 * @param request
	 * @return
	 */
	@GetMapping(value = "/getcategoryList")
	public ResponseEntity<?> getJobCardCategory(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		MessageCodeResponse responseModel = null;

		List<JobCardCategoryResponse> responseModelList = jobCardService.getJobCardCategoryList(authorizationHeader,
				userCode);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch JobCard  Category List " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch JobCard Category List Successfully");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("JobCard Category List Not Fetched !!!.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	/**
	 * 
	 * @param authorizationHeader
	 * @param authentication
	 * @param device
	 * @param request
	 * @return
	 */
	@GetMapping(value = "/getJobSourceList")
	public ResponseEntity<?> getJobSourceList(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		MessageCodeResponse responseModel = null;

		List<JobStatusResponse> responseModelList = jobCardService.getJobSourceList(authorizationHeader, userCode);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch JobCard  Status List " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch JobCard Status List Successfully");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("JobCard Status List Not Fetched !!!.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	/**
	 * 
	 * @param authorizationHeader
	 * @param authentication
	 * @param device
	 * @param request
	 * @return
	 */
	@GetMapping(value = "/getJobPlaceOfService")
	public ResponseEntity<?> getJobPlaceOfService(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		MessageCodeResponse responseModel = null;

		List<JobStatusResponse> responseModelList = jobCardService.getJobPlaceOfService(authorizationHeader, userCode);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch JobCard  Place of service List " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch JobCard Place of service List Successfully");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("JobCard Place of service List Not Fetched !!!.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	/**
	 * 
	 * @param authorizationHeader
	 * @param authentication
	 * @param device
	 * @param request
	 * @return
	 */

	@GetMapping(value = "/getJobServiceTypeList")
	public ResponseEntity<?> getJobServiceTypeList(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		MessageCodeResponse responseModel = null;

		List<JobServiceTypeResponse> responseModelList = jobCardService.getJobServiceTypeList(authorizationHeader,
				userCode);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch JobCard service type List " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch JobCard service type List Successfully");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("JobCard service type List Not Fetched !!!.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	/**
	 * 
	 * @param authorizationHeader
	 * @param authentication
	 * @param device
	 * @param request
	 * @return
	 */
	@GetMapping(value = "/getJobRepairType")
	public ResponseEntity<?> getJobRepairType(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		MessageCodeResponse responseModel = null;

		List<JobRepairCatgResponse> responseModelList = jobCardService.getJobRepairType(authorizationHeader, userCode);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch JobCard Repair Order Type List " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch JobCard Repair Type List Successfully");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("JobCard Repair Order Type List Not Fetched !!!.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	/**
	 * 
	 * @param authorizationHeader
	 * @param bookingNo
	 * @param authentication
	 * @param device
	 * @param request
	 * @return
	 */
	@GetMapping(value = "/getJobServiceBookingSearch")
	public ResponseEntity<?> getJobServiceBookingSearch(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestParam(value = "bookingNo") String bookingNo, OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		MessageCodeResponse responseModel = null;
		List<JobServiceBookingResponse> responseModelList = jobCardService
				.getJobServiceBookingSearch(authorizationHeader, userCode, bookingNo);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Job service booking serach List " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Job service booking serach List Successfully");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("JobCard service booking serach List Not Fetched !!!.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	/**
	 * 
	 * @param authorizationHeader
	 * @param invoiceTxt
	 * @param authentication
	 * @param device
	 * @param request
	 * @return
	 */

	@GetMapping(value = "/getSearchInvoice")
	public ResponseEntity<?> getSearchInvoice(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestParam(value = "invoiceTxt") String invoiceTxt, OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		MessageCodeResponse responseModel = null;
		List<InvoiceSearchResponse> responseModelList = jobCardService.getSearchInvoice(authorizationHeader, userCode,
				invoiceTxt);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Job invoice serach List " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Job invoice serach List Successfully");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("JobCard invoice serach List Not Fetched !!!.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	/**
	 * 
	 * @param authorizationHeader
	 * @param ChassisText
	 * @param authentication
	 * @param device
	 * @param request
	 * @return
	 */
	@PostMapping(value = "/getSearchChassisOrMobileNo")
	public ResponseEntity<?> getSearchChassisOrMobileNo(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody SearchChassisRequest requestModel, OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		MessageCodeResponse responseModel = null;
		List<JobChassisResponse> responseModelList = jobCardService.getSearchChassisOrMobileNo(authorizationHeader,
				userCode, requestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Job chassisOrMobile serach List " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Job chassisOrMobile serach List Successfully");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("JobCard chassisOrMobile List Not Fetched !!!.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	/**
	 * 
	 * @param authorizationHeader
	 * @param vinId
	 * @param authentication
	 * @param device
	 * @param request
	 * @return
	 */

	@GetMapping(value = "/getSearchChassisDetailsByVinId")
	public ResponseEntity<?> getSearchChassisDetailsByVinId(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestParam(value = "vinId") BigInteger vinId, OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		MessageCodeResponse responseModel = null;
		ChassisDetailsWithFlagResponse responseModelList = jobCardService
				.getSearchChassisDetailsByVinId(authorizationHeader, userCode, vinId);
		if (responseModelList.getJobCardNumber() == null) {
			if (responseModelList.getJobChassisDetailsResponse() != null
					&& !responseModelList.getJobChassisDetailsResponse().isEmpty()) {
				codeResponse.setCode("EC200");
				codeResponse.setDescription("Fetch Job chassis details List " + formatter.format(new Date()));
				codeResponse.setMessage("Fetch Job chassis details List Successfully");
			} else {
				codeResponse.setCode("EC500");
				codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
				codeResponse.setMessage("JobCard chassis details List Not Fetched !!!.");
			}
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModelList.getMessage());
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	/**
	 * 
	 * @param authorizationHeader
	 * @param authentication
	 * @param device
	 * @param request
	 * @return
	 */
	@GetMapping(value = "/getJobCardImplementTypeList")
	public ResponseEntity<?> getJobCardImplementTypeList(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		MessageCodeResponse responseModel = null;

		List<JobCardImplementTypeResponse> responseModelList = jobCardService
				.getJobCardImplementTypeList(authorizationHeader, userCode);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch JobCard implement  type List " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch JobCard implement type   List Successfully");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("JobCard implement type List Not Fetched !!!.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	/**
	 * 
	 * @param authorizationHeader
	 * @param authentication
	 * @param device
	 * @param request
	 * @return
	 */
	@GetMapping(value = "/getJobCardApplicationUsedList")
	public ResponseEntity<?> getJobCardApplicationUsedList(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		MessageCodeResponse responseModel = null;

		List<JobCardApplicationUsedResponse> responseModelList = jobCardService
				.getJobCardApplicationUsedList(authorizationHeader, userCode);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch JobCard application Used List " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch JobCard application Used List Successfully");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("JobCard application Used List Not Fetched !!!.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	/**
	 * 
	 * @param authorizationHeader
	 * @param authentication
	 * @param device
	 * @param request
	 * @return
	 */
	@GetMapping(value = "/getRepresentativeList")
	public ResponseEntity<?> getRepresentativeList(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		MessageCodeResponse responseModel = null;

		List<JobRepresentativeResponse> responseModelList = jobCardService.getRepresentativeList(authorizationHeader,
				userCode);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch JobCard Representative List " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch JobCard Representative List Successfully");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("JobCard Representative List Not Fetched !!!.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	/**
	 * 
	 * @param authorizationHeader
	 * @param authentication
	 * @param device
	 * @param request
	 * @return
	 */
	@GetMapping(value = "/getTechnicianList")
	public ResponseEntity<?> getTechnicianList(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		MessageCodeResponse responseModel = null;

		List<JobTechnicianListResponse> responseModelList = jobCardService.getTechnicianList(authorizationHeader,
				userCode);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch JobCard technician  List " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch JobCard technician List Successfully");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("JobCard technician List Not Fetched !!!.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	/**
	 * 
	 * @param authorizationHeader
	 * @param jobcardRequest
	 * @param authentication
	 * @param device
	 * @param request
	 * @return
	 */

//	@PostMapping(value = "/Create/jobCard")
//	public ResponseEntity<?> getJobCardCreation(
//			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
//			@RequestBody jobCardCreateRequest jobcardRequest, OAuth2Authentication authentication, Device device,
//			HttpServletRequest request) {
//		String userCode = null;
//		if (authentication != null) {
//			userCode = authentication.getUserAuthentication().getName();
//		}
//		HeaderResponse userAuthResponse = new HeaderResponse();
//		MessageCodeResponse codeResponse = new MessageCodeResponse();
//		SimpleDateFormat formatter = getSimpleDateFormat();
//		// JobCardCreateResponse responseModel=null;
//		JobCardCreateResponse responseModel = jobCardService.getJobCardCreation(authorizationHeader, userCode,
//				jobcardRequest);
//		if (responseModel != null && responseModel.getStatusCode().compareTo(WebConstants.STATUS_CREATED_201) == 0) {
//			codeResponse.setCode("EC200");
//			codeResponse.setDescription("Successful on Createing Job Cardss " + formatter.format(new Date()));
//			codeResponse.setMessage(responseModel.getMsg());
//		} else {
//			codeResponse.setCode("EC500");
//			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
//			codeResponse.setMessage("Error While createing job Card.");
//		}
//		userAuthResponse.setResponseCode(codeResponse);
//		userAuthResponse.setResponseData(responseModel);
//		return ResponseEntity.ok(userAuthResponse);
//	}

	/**
	 * 
	 * @param authorizationHeader
	 * @param jobCardData
	 * @param authentication
	 * @param device
	 * @param request
	 * @return
	 */

	@PostMapping(value = "/saveJobCardData")
	public ResponseEntity<?> saveJobCardData(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody JobCardFinalRequest jobCardData, OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		JobCardCreateResponse responseModel = jobCardService.saveJobCardData(authorizationHeader, userCode,
				jobCardData);
		if (responseModel != null && responseModel.getStatusCode().compareTo(WebConstants.STATUS_CREATED_201) == 0) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Successful on Createing Job Cardss " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Error While createing job Card.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}

	/**
	 * 
	 * @param authorizationHeader
	 * @param authentication
	 * @param device
	 * @param request
	 * @return
	 */
	@GetMapping(value = "/getServiceActvityType")
	public ResponseEntity<?> getServiceActvityType(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		MessageCodeResponse responseModel = null;

		List<JobServiceActivityTypeResponse> responseModelList = jobCardService
				.getServiceActvityTypeList(authorizationHeader, userCode);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Service activity Type List " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Service activity Type List Successfully");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("JobCard Service activity Type List Not Fetched !!!.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	/**
	 * 
	 * @param authorizationHeader
	 * @param authentication
	 * @param device
	 * @param request
	 * @return
	 */

	@GetMapping(value = "/getBillableTypeList")
	public ResponseEntity<?> getBillableTypeList(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestParam int categoryId, OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		MessageCodeResponse responseModel = null;

		List<jobBillableTypeResponse> responseModelList = jobCardService.getBillableTypeList(authorizationHeader,
				userCode, categoryId);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch JobCard billable Type List " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch JobCard billable Type List Successfully");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("JobCard billable Type List Not Fetched !!!.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	/**
	 * 
	 * @param authorizationHeader
	 * @param authentication
	 * @param device
	 * @param request
	 * @return
	 */
	@GetMapping(value = "/getinventryCheckList")
	public ResponseEntity<?> getinventryCheckList(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		MessageCodeResponse responseModel = null;

		List<jobInventryChecklistReponse> responseModelList = jobCardService.getinventryCheckList(authorizationHeader,
				userCode);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch JobCard inventry checklist List " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch JobCard inventry checklist List Successfully");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("JobCard inventry checklist List Not Fetched !!!.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	/**
	 * 
	 * @param authorizationHeader
	 * @param authentication
	 * @param device
	 * @param request
	 * @return
	 */
	@GetMapping(value = "/getPaintScracthedList")
	public ResponseEntity<?> getPaintScracthedList(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		MessageCodeResponse responseModel = null;

		List<jobpainscratchedResponse> responseModelList = jobCardService.getPaintScracthedList(authorizationHeader,
				userCode);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch JobCard paint scratched List " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch JobCard paint scratched List Successfully");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("JobCard paint scratched List Not Fetched !!!.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	/**
	 * 
	 * @param authorizationHeader
	 * @param authentication
	 * @param device
	 * @param request
	 * @return
	 */
	@GetMapping(value = "/getLobourDetails")
	public ResponseEntity<?> getLobourDetails(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		MessageCodeResponse responseModel = null;

		List<JobLabourMasterResponse> responseModelList = jobCardService.getLobourDetails(authorizationHeader,
				userCode);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Job Labour Details List " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch JobCard Labour Details List Successfully");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("JobCard Labour Details Not Fetched !!!.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	/**
	 * 
	 * @param authorizationHeader
	 * @param authentication
	 * @param device
	 * @param request
	 * @return
	 */
	@GetMapping(value = "/getOutSiderLobourDetails")
	public ResponseEntity<?> getOutSiderLobourDetails(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		MessageCodeResponse responseModel = null;

		List<JobLabourMasterResponse> responseModelList = jobCardService.getOutSiderLobourDetails(authorizationHeader,
				userCode);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Job OutSider Labour Details List " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch JobCard  OutSider Labour Details List Successfully");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("JobCard OutSider Labour Details Not Fetched !!!.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	/**
	 * 
	 * @param authorizationHeader
	 * @param authentication
	 * @param device
	 * @param request
	 * @param roId
	 * @param chassisNoPic
	 * @param hourMeterPic
	 * @param pic1
	 * @param pic2
	 * @return
	 */
	@PostMapping("/upload/images")
	public ResponseEntity<?> uploadImage(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			OAuth2Authentication authentication, Device device, HttpServletRequest request,
			@RequestParam("roId") Integer roId,
			@RequestParam(name = "chassisNoPic", required = false) MultipartFile chassisNoPic,
			@RequestParam(name = "hourMeterPic", required = false) MultipartFile hourMeterPic,
			@RequestParam(name = "pic1", required = false) MultipartFile pic1,
			@RequestParam(name = "pic2", required = false) MultipartFile pic2) {

		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		MessageCodeResponse responseModel = null;

		responseModel = jobCardService.uploadDocuments(authorizationHeader, userCode, roId, chassisNoPic, hourMeterPic,
				pic1, pic2);
		if (responseModel != null && responseModel.getCode() == "201") {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Image upload sucessfully " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMessage());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Error While uploading image.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}

	/**
	 * 
	 * @param authorizationHeader
	 * @param requestModel
	 * @param authentication
	 * @param device
	 * @param request
	 * @return
	 */
	@PostMapping(value = "/closeJobCard")
	public ResponseEntity<?> closeJobCard(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody JobCardCloseRequest requestModel, OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		JobCardCreateResponse responseModel = null;
		responseModel = jobCardService.closeJobCard(authorizationHeader, userCode, requestModel, device);
		if (responseModel != null && responseModel.getStatusCode().compareTo(WebConstants.STATUS_CREATED_201) == 0) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Successful on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Error While Closeing job card.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}

	/**
	 * 
	 * @param authorizationHeader
	 * @param requestModel
	 * @param authentication
	 * @param device
	 * @param request
	 * @return
	 */
	@PostMapping(value = "/cancelJobCard")
	public ResponseEntity<?> cancelJobCard(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody JobCardCancelRequest requestModel, OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		JobCardCreateResponse responseModel = null;
		responseModel = jobCardService.cancelJobCard(authorizationHeader, userCode, requestModel, device);
		if (responseModel != null && responseModel.getStatusCode().compareTo(WebConstants.STATUS_CREATED_201) == 0) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Successful on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Error While Closeing job card.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}

	/**
	 * 
	 * @param requestModel
	 * @param files
	 * @param authentication
	 * @param device
	 * @param request
	 * @return
	 */
	@PostMapping("/upload/pdiFiles")
	public ResponseEntity<?> uploadPdiFiles(@RequestPart(value = "requestModel") PdiDataUpdateRequest requestModel,
			@RequestPart(value = "files", required = false) MultipartFile files, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		JobCardCreateResponse responseModel = null;

		responseModel = jobCardService.uploadPdiFiles(userCode, requestModel, files);
		if (responseModel != null && responseModel.getStatusCode().compareTo(WebConstants.STATUS_CREATED_201) == 0) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Successful on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Error While Uploading Pdi.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}

	/**
	 * 
	 * @param requestModel
	 * @param files
	 * @param authentication
	 * @param device
	 * @param request
	 * @return
	 */
	@PostMapping("/upload/installationFiles")
	public ResponseEntity<?> uploadInstallationFiles(
			@RequestPart(value = "requestModel") InstallationUpdateRequest requestModel,
			@RequestPart(value = "files", required = false) MultipartFile files, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		JobCardCreateResponse responseModel = null;

		responseModel = jobCardService.uploadInstallationFiles(userCode, requestModel, files);
		if (responseModel != null && responseModel.getStatusCode().compareTo(WebConstants.STATUS_CREATED_201) == 0) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Successful on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Error While Uploading Installation.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}

	/**
	 * 
	 * @param authorizationHeader
	 * @param requestModel
	 * @param authentication
	 * @param device
	 * @param request
	 * @return
	 */
	@PostMapping("/edit/jobCard")
	public ResponseEntity<?> editJobCard(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody EditFinalModel requestModel, OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		JobCardCreateResponse responseModel = null;
		responseModel = jobCardService.editJobCard(authorizationHeader, userCode, requestModel, device);
		if (responseModel != null && responseModel.getStatusCode().compareTo(WebConstants.STATUS_CREATED_201) == 0) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Successful on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Error While editing job card.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}

	/**
	 * 
	 * @param authorizationHeader
	 * @param authentication
	 * @param device
	 * @param request
	 * @param roId
	 * @param chassisNoPic
	 * @param hourMeterPic
	 * @param pic1
	 * @param pic2
	 * @return
	 */
	@PostMapping("/edit/upload/images")
	public ResponseEntity<?> editUploadImage(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			OAuth2Authentication authentication, Device device, HttpServletRequest request,
			@RequestParam("roId") Integer roId,
			@RequestParam(name = "chassisNoPic", required = true) MultipartFile chassisNoPic,
			@RequestParam(name = "hourMeterPic", required = true) MultipartFile hourMeterPic,
			@RequestParam(name = "pic1", required = false) MultipartFile pic1,
			@RequestParam(name = "pic2", required = false) MultipartFile pic2) {

		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		MessageCodeResponse responseModel = null;

		responseModel = jobCardService.editUploadImage(authorizationHeader, userCode, roId, chassisNoPic, hourMeterPic,
				pic1, pic2);
		if (responseModel != null && responseModel.getCode() == "201") {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Image upload sucessfully " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMessage());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Error While uploading image.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}

	/**
	 * 
	 * @param authorizationHeader
	 * @param requestModel
	 * @param authentication
	 * @param device
	 * @param request
	 * @return
	 */
	@GetMapping(value = "/updateServieAndQuoationStatus")
	public ResponseEntity<?> updateServieAndQuoationStatus(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody ServiceBookingAndQuoationStatusRequestModel requestModel, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		MessageCodeResponse responseModel = null;
		MessageCodeResponse responseModelList = jobCardService.updateServieAndQuoationStatus(authorizationHeader,
				userCode, requestModel);
		if (responseModel != null && responseModel.getCode() == "201") {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Job  details List " + formatter.format(new Date()));
			codeResponse.setMessage("Service booking and quotiation updated Successfully");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Internel server side error.");
		}

		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	/**
	 * 
	 * @param authorizationHeader
	 * @param chassisNumber
	 * @param categoryId
	 * @param authentication
	 * @param device
	 * @param request
	 * @return
	 */
	@GetMapping(value = "/getSearchChassisDetailsByChassis")
	public ResponseEntity<?> getSearchChassisDetailsByChassis(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestParam(value = "chassisNumber") String chassisNumber,
			@RequestParam(value = "categoryId") Integer categoryId, OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		MessageCodeResponse responseModel = null;
		ChassisDetailsWithFlagResponse responseModelList = jobCardService
				.getSearchChassisDetailsByChassis(authorizationHeader, userCode, chassisNumber, categoryId);
		if (responseModelList.getJobCardNumber() == null) {
			if (responseModelList.getJobChassisDetailsResponse() != null
					&& !responseModelList.getJobChassisDetailsResponse().isEmpty()) {
				codeResponse.setCode("EC200");
				codeResponse.setDescription("Fetch Job chassis details List " + formatter.format(new Date()));
				codeResponse.setMessage("Fetch Job chassis details List Successfully");
			} else {
				codeResponse.setCode("EC500");
				codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
				codeResponse.setMessage("JobCard chassis details List Not Fetched !!!.");
			}
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModelList.getMessage());
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping(value = "/getAutoSelectSrvCategoryAndType")
	public ResponseEntity<?> getAutoSelectSrvCategoryAndType(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestParam(value = "Vin_Id") Integer Vin_Id, @RequestParam(value = "KMReading") Integer KMReading,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		MessageCodeResponse responseModel = null;
		List<AutoSelectSrvCategoryAndTypeResponse> responseModelList = jobCardService
				.getAutoSelectSrvCategoryAndType(authorizationHeader, userCode, Vin_Id, KMReading);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Job  details List " + formatter.format(new Date()));
			codeResponse.setMessage("JObCard Auto select service Fetch Successfully");
		}else if(responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Empty Job  details List " + formatter.format(new Date()));
			codeResponse.setMessage("Service applicability not found, Kindly contact administration...");
		}else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Error while JObCard Auto service Fetch .");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping(value = "/search/activityPlanNo")
	public ResponseEntity<?> getACtivityPlanNumber(
			@RequestParam("profitcenter") String profitcenter,
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader, OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		MessageCodeResponse responseModel = null;
		HashMap<String, Object> responseModelList = new HashMap<>();
		responseModelList=jobCardService.getActivityDetails(authorizationHeader, userCode,profitcenter);

		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Job  details List " + formatter.format(new Date()));
			codeResponse.setMessage("Service Activity Type Fetch Successfully");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Service Activity Type Not Fetched !!!.");
		}

		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/viewVehicleDetails")
	public ResponseEntity<?> viewVehicleDetails(OAuth2Authentication authentication,
			@RequestParam(value="chassisNumber") String chassisNo,@RequestParam(value="falg") String flag) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		MasterDataModelResponse resultMap = jobCardService.getChassisDetails(userCode,chassisNo,flag);

		if (resultMap != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Chassis Details on " + formatter.format(new Date()));
			codeResponse.setMessage("Chassis Details Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Chassis Details Not Fetched !!!.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(resultMap);
		return ResponseEntity.ok(userAuthResponse);

	}
	
	@GetMapping("/serviceHistoryByChassis")
	public ResponseEntity<?> getServiceHistoryByChassis(OAuth2Authentication authentication,
			@RequestParam(value = "chassisNumber") String chassisNumber, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		Map<String, Object> responseModelList = jobCardService.getServiceHistoryByChassis(userCode, device,
				chassisNumber);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Service History  List on " + formatter.format(new Date()));
			codeResponse.setMessage("Service History List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Service History Not Fetched !!!.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/checkLubricant")
	public ResponseEntity<?> checkLubricant(OAuth2Authentication authentication,
			@RequestParam(value = "roId") BigInteger roId, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		Integer rowCount = jobCardService.checkLubricant(userCode, device,
				roId);
		if (rowCount != null) {
			codeResponse.setCode("EC200");
		} else {
			codeResponse.setCode("EC500");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(rowCount);
		return ResponseEntity.ok(userAuthResponse);
	}

}
