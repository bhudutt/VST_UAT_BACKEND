/**
 * 
 */
package com.hitech.dms.web.controller.jobcard.search;

import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.model.jobcard.request.JobCardExcelReportRequest;
import com.hitech.dms.web.model.jobcard.search.request.JobCardSearchRequest;
import com.hitech.dms.web.model.jobcard.search.response.JobCardDetailsResponse;
import com.hitech.dms.web.model.jobcard.search.response.JobCardSearchResponse;
import com.hitech.dms.web.model.jobcard.search.response.JobCardSearchTypeWiseResponse;
import com.hitech.dms.web.service.jobcard.JobCardService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import net.sf.jasperreports.engine.JasperPrint;

/**
 * @author santosh.kumar
 *
 */
@RestController
@RequestMapping("/api/v1/search/jobCard")
@SecurityRequirement(name = "hitechApis")
public class JobCardSearchCantroller {

	@Autowired
	private JobCardService jobCardService;
	@Value("${file.upload-dir.JobCardReport}")
	private String downloadPathPreInvoice;
	
	@Value("${file.upload-dir.JobCardOpenReport}")
	private String downloadPathOpenJobCard;
	
	@Value("${file.upload-dir.jobCardReports}")
	private String downloadJobCardExcelPath;
	
	@Value("${file.upload-dir.serviceJobcardImageDownload}")
	private String downloadJobCardImagePath;
	private static final Logger logger = LoggerFactory.getLogger(JobCardSearchCantroller.class);
	
	

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
	@PostMapping(value = "/searchJobCard")
	public ResponseEntity<?> searchJobCard(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			OAuth2Authentication authentication, @RequestBody JobCardSearchRequest jobCardSearchRequest, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		MessageCodeResponse responseModel = null;
		System.out.println("jobCardSearchRequest" + jobCardSearchRequest);
		List<JobCardSearchResponse> responseModelList = jobCardService.getsearchJobCardDetails(authorizationHeader,
				userCode, jobCardSearchRequest);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch JobCard search List " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch JobCard Search List Successfully");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("JobCard seach List Not Fetched !!!.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	/**
	 * 
	 * @param authorizationHeader
	 * @param authentication
	 * @param type
	 * @param engineNumber
	 * @param vinNumber
	 * @param mobileNumber
	 * @param customerName
	 * @param bookingNumber
	 * @param quotatioNumber
	 * @param invoiceNumber
	 * @param device
	 * @param request
	 * @return
	 */
	@GetMapping("serachJobCard/type/{type}")
	public ResponseEntity<?> searchJobCardaTypeWise(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			OAuth2Authentication authentication, @PathVariable String type,
			@RequestParam(required = false) String chassisNo,
			@RequestParam(required = false) String engineNumber, @RequestParam(required = false) String vinNumber,
			@RequestParam(required = false) String mobileNumber, @RequestParam(required = false) String customerName,
			@RequestParam(required = false) String bookingNumber, @RequestParam(required = false) String quotatioNumber,
			@RequestParam(required = false) String invoiceNumber,

			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		MessageCodeResponse responseModel = null;
		JobCardSearchTypeWiseResponse responseModelList = jobCardService.getsearchJobCardaTypeWise(authorizationHeader,
				userCode, type,chassisNo,engineNumber, vinNumber, mobileNumber, customerName, bookingNumber, quotatioNumber,
				invoiceNumber);
		if (responseModelList != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch JobCard search List " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch JobCard search List Successfully");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("JobCard search List Not Fetched !!!.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);

	}

	/**
	 * 
	 * @param authorizationHeader
	 * @param roId
	 * @param authentication
	 * @param device
	 * @param request
	 * @return
	 */
	@GetMapping(value = "/getJobCardDetailsByRoId/{roId}")
	public ResponseEntity<?> getJobCardDetailsByRoId(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@PathVariable Integer roId, OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		MessageCodeResponse responseModel = null;

		JobCardDetailsResponse responseModelList = jobCardService.getJobCardDetailsByRoId(authorizationHeader, userCode,
				roId);
		if (responseModelList != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch JobCard  Status List " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch JobCard Details List Successfully");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("JobCard Details List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/getQuotationSearch")
	public ResponseEntity<?> getQuotationSearch(OAuth2Authentication authentication,
			@RequestParam(value = "quotationText") String quotationText,@RequestParam(value = "categoryId") int categoryId, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		Map<String, Object> responseModelList = jobCardService.getQuotationSearchList(userCode, device,
				quotationText,categoryId);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Quotation List on " + formatter.format(new Date()));
			codeResponse.setMessage("Quotation List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Quotation Not Fetched !!!.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	
	
	
	
	/**
	 * 
	 * @param authentication
	 * @param jobCardId
	 * @param device
	 * @param request
	 * @return
	 */
	
	@GetMapping("/validatePcrButton")
	public ResponseEntity<?> validatePcrButton(OAuth2Authentication authentication,
			@RequestParam(value = "jobCardId") String jobCardId, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		Boolean responseModelList = jobCardService.getValidatePcrButton(userCode, device,
				jobCardId);
		if (responseModelList != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("PCR button validate on " + formatter.format(new Date()));
			codeResponse.setMessage("PCR button validate Successfully");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("PCR button validate Unsuccessfully !!!.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	
	@GetMapping("/getJobCardStatusList")
	public ResponseEntity<?> getJobCardStatusList(OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		Map<String, Object> responseModelList = jobCardService.getJobCardStatusList(userCode);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("JobCard Status  List on " + formatter.format(new Date()));
			codeResponse.setMessage("JobCard Status List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("JobCard Status Not Fetched !!!.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	
	
	
	
	
/**
 * 
 * @param roId
 * @param format
 * @param printStatus
 * @param authentication
 * @param request
 * @param response
 * @throws IOException
 */
	@GetMapping("/exportJobCardPreInvoiceReport")
	public void exportJobCardPreInvoiceReport(@RequestParam(value = "roId") String roId,
			@RequestParam(defaultValue = "pdf") String format, @RequestParam(required = false) String printStatus,
			OAuth2Authentication authentication, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName = "JobCardPreInvoice";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("ROID", roId);
		jasperParameter.put("FLAG", 2);

		String filePath = "";
		String property = System.getProperty("os.name");
		if (property.contains("Windows")) {
		    filePath = downloadPathPreInvoice;
		
		} else {
			filePath = "/var/VST-DMS-APPS/FILES/REPORTS/jobcard invoice/";
			
		}
		JasperPrint jasperPrint = jobCardService.PdfGeneratorReport(request, "JobCardPreInvoice.jasper",
				jasperParameter, filePath);
		try {

			if (format != null && format.equalsIgnoreCase("pdf")) {
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "inline; filename=" + reportName + ".pdf");
			}

			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();

			jobCardService.printReport(jasperPrint, format, printStatus, outputStream, reportName);

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
	/**
	 * 
	 * @param roId
	 * @param format
	 * @param printStatus
	 * @param authentication
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@GetMapping("/exportJobCardOpenReport")
	public void exportJobCardOpenReport(@RequestParam(value = "roId") String roId,
			@RequestParam(defaultValue = "pdf") String format, @RequestParam(required = false) String printStatus,
			OAuth2Authentication authentication, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName = "JobCardPreInvoice";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("ROID", roId);
		jasperParameter.put("FLAG", 1);

		String filePath = "";
		String property = System.getProperty("os.name");
		
		  if (property.contains("Windows")) { 
			  filePath = downloadPathOpenJobCard;
		  }
		  else { filePath = "/var/VST-DMS-APPS/FILES/REPORTS/JobCard Open/";
		  }
		 
		
		
		
		JasperPrint jasperPrint = jobCardService.PdfGeneratorReportForJobCardOpen(request, "JobCardOpen.jasper",
				jasperParameter, filePath);
		try {

			if (format != null && format.equalsIgnoreCase("pdf")) {
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "inline; filename=" + reportName + ".pdf");
			}

			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();

			jobCardService.printReport(jasperPrint, format, printStatus, outputStream, reportName);

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

	@PostMapping("/exportJobcardReport")
	public void generateReport(@RequestBody JobCardExcelReportRequest requestModel,
			@RequestParam(defaultValue = "xlsx") String format, @RequestParam(required = false) String printStatus,
			OAuth2Authentication authentication, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		OutputStream outputStream = null;
		String userCodes = null;
		if (authentication != null) {
			userCodes = authentication.getUserAuthentication().getName();
		}

		String reportName = "jobCard Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();


        jasperParameter.put("BranchId", requestModel.getBranchId());
        jasperParameter.put("UserCode", userCodes);
        jasperParameter.put("RONumber", requestModel.getRoNumber());
        jasperParameter.put("ChasisNumber", requestModel.getChasisNumber());
        jasperParameter.put("EngineNumber", requestModel.getEngineNumber());
        jasperParameter.put("VinNo", requestModel.getVinNo());
        jasperParameter.put("CustomerName", requestModel.getCustomerName());
        jasperParameter.put("MobileNo", requestModel.getMobileNo());
        jasperParameter.put("ServiceTypeId", requestModel.getServiceTypeId());
        jasperParameter.put("RepairTypeId", requestModel.getRepairTypeId());
        jasperParameter.put("JobCardCategoryId", requestModel.getJobCardCategoryId());
        jasperParameter.put("InvoiceNo", requestModel.getInvoiceNo());
        jasperParameter.put("ServcieBookingNumber", requestModel.getServiceBookingNumber());
        jasperParameter.put("QuotationNumber", requestModel.getQuotationNumber());
        jasperParameter.put("Fromdate", requestModel.getFromDate());
        jasperParameter.put("Todate", requestModel.getToDate());
        jasperParameter.put("page", requestModel.getPage());
        jasperParameter.put("size", requestModel.getSize());
        jasperParameter.put("pcId", requestModel.getPcId());
        jasperParameter.put("hoId", requestModel.getHoId());
        jasperParameter.put("zoneId", requestModel.getZoneId());
        jasperParameter.put("stateId", requestModel.getStateId());
        jasperParameter.put("territoryId", requestModel.getTerritoryId());
		String filePath = "";
		String property = System.getProperty("os.name");
		if (property.contains("Windows")) {
			filePath = downloadJobCardExcelPath;
		} else {
			filePath = "/var/VST-DMS-APPS/FILES/REPORTS/jobCardExcel/";
		}

		JasperPrint jasperPrint = jobCardService.jobcasexlsxGeneratorReport(request, "jobcard.jasper", jasperParameter, filePath);
		try {
			response.setContentType("application/xlsx");
			response.setHeader("Content-Disposition", "inline; filename=JobCardExcelReport.xlsx");
			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();
			jobCardService.printReportExcel(jasperPrint, format, printStatus, outputStream);

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

	@GetMapping("/downloadFile/{docPath}/{fileName:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, @PathVariable("docPath") String docPath,
			@RequestParam(required = false) Long id, HttpServletRequest request) throws MalformedURLException {
		// Load file as Resource
		String filePath=null;
		String property = System.getProperty("os.name");
		if (property.contains("Windows")) {
			filePath = downloadJobCardImagePath;
		} else {
			filePath = "/var/VST-DMS-APPS/FILES/dms-service-customer/images/";
		}
		
		
		
		Resource resource = jobCardService.loadFileAsResource(fileName, docPath, id,filePath);
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
