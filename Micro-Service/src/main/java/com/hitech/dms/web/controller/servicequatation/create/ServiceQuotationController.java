package com.hitech.dms.web.controller.servicequatation.create;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.app.exceptions.ErrorDetails;
import com.hitech.dms.app.utils.ApiResponse;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.controller.partmaster.create.SparePartMasterController;
import com.hitech.dms.web.dao.partmaster.create.PartMasterCreateDao;
import com.hitech.dms.web.dao.servicequotation.create.ServiceQuatationCreateDao;
import com.hitech.dms.web.model.masterdata.response.QuotationSearchResponse;
import com.hitech.dms.web.model.partmaster.create.request.PartMasterFormRequestModel;
import com.hitech.dms.web.model.partmaster.create.response.PartMasterCreateResponseModel;
import com.hitech.dms.web.model.partmaster.create.response.ServiceQutationCreateResponseModel;
import com.hitech.dms.web.model.service.bookingview.response.ServiceBookingViewResponseModel;
import com.hitech.dms.web.model.servicequotation.create.request.QuotationExcelRequestModel;
import com.hitech.dms.web.model.servicequotation.create.request.ServiceQuotationCloseResponse;
import com.hitech.dms.web.model.servicequotation.create.request.ServiceQuotationModel;
import com.hitech.dms.web.repo.dao.masterdata.search.QutationRepo;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import net.sf.jasperreports.engine.JasperPrint;

@RestController
@RequestMapping("/servicequotation")
@SecurityRequirement(name = "hitechApis")
public class ServiceQuotationController {

	private static final Logger logger = LoggerFactory.getLogger(SparePartMasterController.class);

	@Autowired
	private ServiceQuatationCreateDao serviceQuatationCreateDao;
	
	
	@Value("${file.upload-dir.QuotationReports}")
	private String downloadPath;
	

	@Autowired
	private QutationRepo quoRepo;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	}

	@PostMapping(value = "/createServiceQuotation")
	public ResponseEntity<?> createServiceQuotation(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody ServiceQuotationModel requestModel, BindingResult bindingResult,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		ServiceQutationCreateResponseModel responseModel = null;
		if (bindingResult.hasErrors()) {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Service Quotation not created or server side error.");

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
		responseModel = serviceQuatationCreateDao.createServiceQuotation(authorizationHeader, userCode, requestModel);
		if (responseModel != null && responseModel.getStatusCode().compareTo(WebConstants.STATUS_CREATED_201) == 0) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Successful on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Error While Service Quotation GRN.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping(value = "/quoSearch")
	public ResponseEntity<?> rcSearch(@RequestBody ServiceQuotationModel requestModel,
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			OAuth2Authentication authentication) {
		ApiResponse apiResponse = new ApiResponse();
		System.out.println("requestModel "+requestModel);

		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}

		List<QuotationSearchResponse> result = quoRepo.rcSearch(
				requestModel.getQuotationNo(), 
				requestModel.page, 
				requestModel.size,
				requestModel.getFromDate(),
				requestModel.getToDate(),
				requestModel.getIncludeInActive(),
				requestModel.getSegment(),
				requestModel.getSeries(),
				requestModel.getVariant(),
				userCode ,
				requestModel.getStatus());
		  
													 
		apiResponse.setMessage("Quotation search list");
		
		apiResponse.setStatus(HttpStatus.OK.value());
		apiResponse.setResult(result);
		Long count = 0L;
		if (result != null && result.size() > 0) {
			// apiResponse.setCount(result.get(0).getRecordCount());
			apiResponse.setCount(Long.valueOf(result.size()));
			
		}
		return ResponseEntity.ok(apiResponse);
	}
	
	
	
	
	
	@GetMapping("/fetchQuotationViewDetails")
	public ResponseEntity<?> fetchQuotationViewDetails(@RequestParam("quotation_Id")  BigInteger quotation_Id,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		ServiceQuotationModel responseModelList = serviceQuatationCreateDao.fetchQuotationView(userCode, quotation_Id);
		if (responseModelList != null ) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Customer View List on" + formatter.format(new Date()));
			codeResponse.setMessage("Customer View List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Customer View List Not Fetched or server side error.");
		}
		
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@GetMapping("/updateQuotationStatus")
	public ResponseEntity<?> updateQuotationStatus(@RequestParam("quotation_Id")  BigInteger quotation_Id,
			@RequestParam("status") String status,
			@RequestParam("remarks") String remarks,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		ServiceQuotationCloseResponse response = serviceQuatationCreateDao.quotationCloseStatusUpdate(userCode, quotation_Id, remarks, status);
				if (response != null ) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Quotation  Status Updated Successfully" + formatter.format(new Date()));
			codeResponse.setMessage(response.getMessage());
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Staus Not has been updated");
		}
		
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	
	
	@PostMapping("/fetchQuotationNoList")
	public ResponseEntity<?> fetchQuotationNoList(@RequestBody ServiceQuotationModel requestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<ServiceQuotationModel> responseModelList = serviceQuatationCreateDao.fetchQuotationNoList(userCode, requestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Machine Chassis List on " + formatter.format(new Date()));
			codeResponse.setMessage("Model List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Model List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@PostMapping("/quotationExcelReport")
	public void exportENQExcelReport(@RequestBody QuotationExcelRequestModel requestModel,
			@RequestParam(defaultValue = "xls") String format, @RequestParam(required = false) String printStatus,
			OAuth2Authentication authentication, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		System.out.println("Request at controller "+requestModel);
//		Integer dealerCode=requestModel.getDealerId();
//		Integer branchId=requestModel.getBranchId();
//		Integer stateId=requestModel.getStateId();
//		Integer orgHierarchyId=requestModel.getOrgHierId();
//		Integer zoneId=requestModel.getZoneId();
//		String status=requestModel.getStatus();
//		String quotationNo=requestModel.getQuotationNo();
//		Integer dealerCode1=null;
//		Integer branchId1=null;
//		Integer stateId1=null;
//		Integer orgHierarchyId1=null;

		
		
		
		String reportName = "Quotation_service_reports";
		SimpleDateFormat smdf = getSimpleDateFormat();
	
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		
//		jasperParameter.put("userCode",userCode);
//		jasperParameter.put("stateId", requestModel.getStateId());
//		jasperParameter.put("dealerId", requestModel.getDealerId());
//		jasperParameter.put("branchId", requestModel.getBranchId());

		
//		jasperParameter.put("profitCenterId", requestModel.getPcId() != null ? requestModel.getPcId() : null);
//		jasperParameter.put("orgHierID",
//				requestModel.getOrgHierId() != null ? requestModel.getOrgHierId() : null);
//		jasperParameter.put("status",
//				requestModel.getStatus() != null ? requestModel.getStatus() : null);
		jasperParameter.put("quotationNo",
				requestModel.getQuotationNo() != null ? requestModel.getQuotationNo() : null);
		jasperParameter.put("page", requestModel.getPage() != null ? requestModel.getPage() : null);
		jasperParameter.put("size", requestModel.getSize() != null ? requestModel.getSize() : null);
		jasperParameter.put("fromDate", requestModel.getFromDate() != null ? requestModel.getFromDate() : null);
		jasperParameter.put("toDate", requestModel.getToDate() != null ? requestModel.getToDate() : null);
		jasperParameter.put("includeInActive","N");
		jasperParameter.put("segment",null);
		jasperParameter.put("series",null);
		jasperParameter.put("variant",null);
		jasperParameter.put("UserCode",userCode);
		jasperParameter.put("Status",null);

		System.out.println("after data ");
		String filePath = "";
		String property = System.getProperty("os.name");
		if (property.contains("Windows")) {
			filePath = downloadPath;
		} else {
			filePath = "/var/VST-DMS-APPS/FILES/REPORTS/Quotation/";
		}
		JasperPrint jasperPrint = serviceQuatationCreateDao.ExcelGeneratorReport(request, "servicequotation.jasper", jasperParameter,
				filePath);
		logger.info("after excel generate");
		try {

			if (format != null && format.equalsIgnoreCase("xls")) {
				response.setHeader("Content-Disposition", "attachment; filename=" + reportName + ".xlsx");
				response.setContentType("application/vnd.ms-excel");
			}

			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();
			System.out.println(" after outputstream result");
			serviceQuatationCreateDao.printReport(jasperPrint, format, printStatus, outputStream, reportName);
			System.out.println(" after print result");

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				outputStream.flush();
				outputStream.close();
			}
		}
	}

	
	
	
	
}
