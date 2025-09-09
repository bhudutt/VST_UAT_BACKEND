package com.hitech.dms.web.Controller.search;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.model.SpareModel.SparePoCreateResponseModel;
import com.hitech.dms.web.model.SpareModel.SparePoHDRAndPartDetailsModel;
import com.hitech.dms.web.model.spare.search.response.sparePoSearchListResponse;
import com.hitech.dms.web.model.spare.search.resquest.SparePoCancelRequestModel;
import com.hitech.dms.web.model.spare.search.resquest.SparePoUpdateRequestModel;
import com.hitech.dms.web.model.spare.search.resquest.partSerachRequest;
import com.hitech.dms.web.service.sparePo.createSparePoService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import net.sf.jasperreports.engine.JasperPrint;

@RestController
@RequestMapping("/api/v1/searchSparePo")
@SecurityRequirement(name = "hitechApis")
public class searchSparePoController {
	private static final Logger logger = LoggerFactory.getLogger(searchSparePoController.class);
	@Autowired
	private createSparePoService spareService;
	@Value("${file.upload-dir.SpareTemplate:C:\\VST-DMS-APPS\\template\\template\\}")
	private String spareSearchDownloadPath;
	
	@Value("${file.upload-dir.SparePoPrintReport}")
	private String sparePoPrintReport;
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	/**
	 * Create Api for the search part details
	 * 
	 * @param requestModel
	 * @param authentication
	 * @return
	 */
	@PostMapping("/fetchSparePOSearchList")
	public ResponseEntity<?> fetchSparePOSearchList(@RequestBody partSerachRequest requestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<sparePoSearchListResponse> responseModel = spareService.sparePoDataList(userCode, requestModel);
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Spare PO Search List on " + formatter.format(new Date()));
			codeResponse.setMessage("Spare PO Search List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Spare PO Search List Not Fetched");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}

	/**
	 * create api for export spare po search data in excel and download
	 * 
	 * @param format
	 * @param printStatus
	 * @param authentication
	 * @param request
	 * @param response
	 * @throws IOException
	 * @Return ---- Excel data
	 */
	@PostMapping("/exportAllSparePO")
	public void generateAllSparePOReport(@RequestBody partSerachRequest requestModel,
			@RequestParam(defaultValue = "xls") String format, @RequestParam(required = false) String printStatus,
			OAuth2Authentication authentication, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName = "SparePO Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("FromDate", requestModel.getFromDate());
		jasperParameter.put("Todate", requestModel.getToDate());
		jasperParameter.put("PoType", requestModel.getPoType());
		jasperParameter.put("Postatus", requestModel.getPoStatus());
		jasperParameter.put("PoCategory", requestModel.getProductCategory());
		jasperParameter.put("poon", requestModel.getPoOn());
		jasperParameter.put("Partycode", requestModel.getPartyCode());
		jasperParameter.put("ProductSubCategory", requestModel.getProductSubCategory());
		jasperParameter.put("PoNumber", requestModel.getPoNumber());
		jasperParameter.put("partyName", requestModel.getPartyName());
		jasperParameter.put("UserCode", userCode);
		jasperParameter.put("orgId", 1);
		jasperParameter.put("ho", requestModel.getHo());
		jasperParameter.put("Zone", requestModel.getZone());
		jasperParameter.put("state", requestModel.getState());
		jasperParameter.put("territory",requestModel.getTerritory());
		jasperParameter.put("pooncheckboxflag", requestModel.getPoOnCheckBoxFlag());
		String filePath = "";
		String property = System.getProperty("os.name");
		if (property.contains("Windows")) {
			filePath = spareSearchDownloadPath;
		} else {
			filePath = "/var/VST-DMS-APPS/FILES/Template/template/";
		}
		JasperPrint jasperPrint = spareService.ExcelGeneratorReport(request, "Spare_PO_Report.jasper", jasperParameter,
				filePath);
		try {

			if (format != null && format.equalsIgnoreCase("xls")) {
				// response.setContentType("application/xls");
				response.setHeader("Content-Disposition", "attachment; filename=" + reportName + ".xlsx");
				response.setContentType("application/vnd.ms-excel");
			}

			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();

			spareService.printReport(jasperPrint, format, printStatus, outputStream, reportName);

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
	 * fetch spareHdrDetails and sparePartsDetails
	 * @param poHdrId
	 * @param authentication
	 * @return
	 */
	@GetMapping("/fetchSpareHdrAndPartDetails")
	public ResponseEntity<?> fetchSpareHdrDetails(@RequestParam(value= "poHdrId") Integer poHdrId,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		SparePoHDRAndPartDetailsModel responseModel = spareService.sparePoHDRAndPartDetails(userCode,poHdrId );
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Spare HDR details List on " + formatter.format(new Date()));
			codeResponse.setMessage("Spare HDR details List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Spare HDR details NOT Found ");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	/**
	 * s
	 * @param authorizationHeader
	 * @param requestModel
	 * @param bindingResult
	 * @param authentication
	 * @param device
	 * @param request
	 * @return
	 */
	
	@PostMapping(value = "/updateSparePO")
	public ResponseEntity<?> updateSparePO(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody SparePoUpdateRequestModel requestModel, BindingResult bindingResult,
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
		responseModel = spareService.updateSparePO(authorizationHeader, userCode, requestModel, device);
		if (responseModel != null && responseModel.getStatusCode().compareTo(WebConstants.STATUS_CREATED_201) == 0) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Successful on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Error While Updating Spare PO.");
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
	@PostMapping(value = "/cancelSparePO")
	public ResponseEntity<?> cancelSparePO(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody SparePoCancelRequestModel requestModel, OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		SparePoCreateResponseModel responseModel = null;
		responseModel = spareService.cancelSparePO(authorizationHeader, userCode, requestModel, device);
		if (responseModel != null && responseModel.getStatusCode().compareTo(WebConstants.STATUS_CREATED_201) == 0) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Successful on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Error While Canceling Spare PO.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	/**
	 * 
	 * @param poHdrId
	 * @param format
	 * @param printStatus
	 * @param authentication
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@GetMapping("/exportSparePoReport")
	public void exportSparePoReport(@RequestParam(value = "poHdrId") String poHdrId,
			@RequestParam(defaultValue = "pdf") String format, @RequestParam(required = false) String printStatus,
			OAuth2Authentication authentication, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName = "SparePoReport";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("POHDRID", poHdrId);
		jasperParameter.put("FLAG", 1);

		String filePath = "";
		String property = System.getProperty("os.name");
		if (property.contains("Windows")) {
			 filePath = sparePoPrintReport;
		} else {
			filePath = "/var/VST-DMS-APPS/FILES/REPORTS/SparePo/Print/";
		}
		
		JasperPrint jasperPrint = spareService.PdfGeneratorReportForSparePo(request, "SpareOrdering.jasper",
				jasperParameter, filePath);
		try {

			if (format != null && format.equalsIgnoreCase("pdf")) {
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "inline; filename=" + reportName + ".pdf");
			}

			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();

			spareService.printReport(jasperPrint, format, printStatus, outputStream, reportName);

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
	 * @param poHdrId
	 * @param format
	 * @param printStatus
	 * @param authentication
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	
	@GetMapping("/exportSpareViewExcelReport")
	public void exportSpareViewExcelReport(@RequestParam(value = "poHdrId") int poHdrId,
			@RequestParam(defaultValue = "xls") String format, @RequestParam(required = false) String printStatus,
			OAuth2Authentication authentication, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName = "SparePoReport";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("POHDRID", poHdrId);
		jasperParameter.put("Flag", 2);

		String filePath = "";
		String property = System.getProperty("os.name");
		if (property.contains("Windows")) {
			 filePath = sparePoPrintReport;
	
		} else {
			 filePath = "/var/VST-DMS-APPS/FILES/REPORTS/SparePo/Print/";
			
		}
		
		JasperPrint jasperPrint = spareService.PdfGeneratorReportForSparePo(request, "SPAREORDEREXCELPARTDETAIL.jasper",
				jasperParameter, filePath);
		try {

			if (format != null && format.equalsIgnoreCase("xls")) {
				// response.setContentType("application/xls");
				response.setHeader("Content-Disposition", "attachment; filename=" + reportName + ".xlsx");
				response.setContentType("application/vnd.ms-excel");
			}

			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();

			spareService.printReport(jasperPrint, format, printStatus, outputStream, reportName);

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
	
	
	
	@GetMapping("/searchPartyCode")
	public ResponseEntity<?> getPartyCode(OAuth2Authentication authentication,
			@RequestParam(value = "searchText") String searchText, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		Map<String, Object> responseModelList = spareService.getPartyCodeList(userCode, device,
				searchText);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("PartyCode List on " + formatter.format(new Date()));
			codeResponse.setMessage("PartyCode List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("PartyCode Not Fetched !!!.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/searchPartyName")
	public ResponseEntity<?> getSearchPartyName(OAuth2Authentication authentication,
			@RequestParam(value = "searchText") String searchText, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		Map<String, Object> responseModelList = spareService.getPartyNameList(userCode, device,
				searchText);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("PartyName List on " + formatter.format(new Date()));
			codeResponse.setMessage("PartyName List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("PartyName Not Fetched !!!.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

}
