/**
 * 
 */
package com.hitech.dms.web.controller.enquiry.search;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.enquiry.search.EnquirySearchDao;
import com.hitech.dms.web.entity.enquiry.ColumnEntity;
import com.hitech.dms.web.model.advancereport.ListPreference;
import com.hitech.dms.web.model.enquiry.create.response.EnquiryCreateResponseModel;
import com.hitech.dms.web.model.enquiry.list.request.EnquiryListRequestModel;
import com.hitech.dms.web.model.enquiry.list.response.EnquiryListResponseModel;
import com.hitech.dms.web.model.enquiry.list.response.EnquiryListResultResponseModel;

import com.hitech.dms.web.model.enquiry.request.EnquiryReportRequest;
import com.hitech.dms.web.model.enquiry.response.EnquiryReportResponse;
import com.hitech.dms.web.model.enquiry.response.EnquiryReportWithDealerWiseResponse;
import com.hitech.dms.web.model.enquiry.response.EnquriyReportByStateResponse;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import net.sf.jasperreports.engine.JasperPrint;

@Validated
@RestController
@RequestMapping("/search")
@SecurityRequirement(name = "hitechApis")
public class EnquirySearchController {
	private static final Logger logger = LoggerFactory.getLogger(EnquirySearchController.class);

	@Autowired
	private EnquirySearchDao enquirySearchDao;
	@Value("${file.upload-dir.EnquiryReport}")
	private String downloadPath;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@PostMapping("/fetchENQList")
	public ResponseEntity<?> fetchENQList(@RequestBody EnquiryListRequestModel enquiryListRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		EnquiryListResultResponseModel responseModel = enquirySearchDao.fetchEnquiryList(userCode,
				enquiryListRequestModel);
		if (responseModel != null && responseModel.getEnquiryList() != null
				&& !responseModel.getEnquiryList().isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Enquiry Serach List on " + formatter.format(new Date()));
			codeResponse.setMessage("Enquiry Serach List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Enquiry Serach List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
		 
	}

	@PostMapping("/fetchEnquiryList")
	public ResponseEntity<?> fetchEnquiryList(@RequestBody EnquiryListRequestModel enquiryListRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		try {
		List<EnquiryListResponseModel> responseModelList = enquirySearchDao.fetchENQList(userCode,
				enquiryListRequestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Enquiry Serach List on " + formatter.format(new Date()));
			codeResponse.setMessage("Enquiry Serach List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Enquiry Serach List Not Fetched or server side error.");
		}
		
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(userAuthResponse);

	}

	@PostMapping("/fetchENQForReport")
	public ResponseEntity<?> fetchENQForReport(@RequestBody EnquiryReportRequest reportEnquiryListRequest,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<EnquiryReportResponse> responseModelList = enquirySearchDao.getENQListForReport(userCode,
				reportEnquiryListRequest);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch ReportEnquiry Serach on " + formatter.format(new Date()));
			codeResponse.setMessage("ReportEnquiry List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("ReportEnquiry Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/fetchENQWithStateAndDealerWiseForReport")
	public ResponseEntity<?> fetchENQWithStateAndDealerWiseForReport(
			@RequestBody EnquiryReportRequest reportEnquiryListRequest, OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<EnquiryReportWithDealerWiseResponse> responseModelList = enquirySearchDao
				.fetchENQWithStateAndDealerWiseForReport(userCode, reportEnquiryListRequest);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch ReportEnquiry Serach on " + formatter.format(new Date()));
			codeResponse.setMessage("ReportEnquiry List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("ReportEnquiry Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/fetchENQWithStateWiseReport")
	public ResponseEntity<?> fetchENQWithStateWiseReport(@RequestBody EnquiryReportRequest reportEnquiryListRequest,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<EnquriyReportByStateResponse> responseModelList = enquirySearchDao.fetchENQWithStateWiseReport(userCode,
				reportEnquiryListRequest);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch ReportEnquiry Serach on " + formatter.format(new Date()));
			codeResponse.setMessage("ReportEnquiry List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("ReportEnquiry Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/fetchENQWithStateAndDealerWiseForSearch")
	public ResponseEntity<?> fetchENQWithStateAndDealerWiseForSearch(
			@RequestBody EnquiryReportRequest reportEnquiryListRequest, OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<EnquiryReportWithDealerWiseResponse> responseModelList = enquirySearchDao
				.fetchENQWithStateAndDealerWiseForSearch(userCode, reportEnquiryListRequest);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch ReportEnquiry Serach on " + formatter.format(new Date()));
			codeResponse.setMessage("ReportEnquiry List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("ReportEnquiry Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/exportENQExcelWithStateWiseReport")
	public void exportENQExcelWithStateWiseReport(@RequestBody EnquiryReportRequest requestModel,
			@RequestParam(defaultValue = "xls") String format, @RequestParam(required = false) String printStatus,
			OAuth2Authentication authentication, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName = "EnquiryWithStateWise Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
//		
//		jasperParameter.put("MODEL", 0);
//		jasperParameter.put("ENQUIRYTYPE", 0);
//		jasperParameter.put("ENQUIRYLIST", 0);
//		jasperParameter.put("ENQUIRYSOURCE", 0);
//		jasperParameter.put("CLUSTER", null);
		jasperParameter.put("STATEID", requestModel.getStateId());
		jasperParameter.put("FROMDATE", requestModel.getFromDate());
		jasperParameter.put("TODATE", requestModel.getToDate());

		String filePath = "";
		String property = System.getProperty("os.name");
		if (property.contains("Windows")) {
			filePath = downloadPath;
		} else {
			filePath = "/var/VST-DMS-APPS/FILES/REPORTS/ENQ/";
		}
		JasperPrint jasperPrint = enquirySearchDao.ExcelGeneratorReport(request, "EnquiryStatusStatewise.jasper",
				jasperParameter, filePath);
		try {

			if (format != null && format.equalsIgnoreCase("xls")) {
				// response.setContentType("application/xls");
				response.setHeader("Content-Disposition", "attachment; filename=" + reportName + ".xlsx");
				response.setContentType("application/vnd.ms-excel");
			}

			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();

			enquirySearchDao.printReport(jasperPrint, format, printStatus, outputStream, reportName);

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

	@PostMapping("/exportENQExcelwithDealerWiseReport")
	public void exportENQExcelwithDealerWiseReport(@RequestBody EnquiryReportRequest requestModel,
			@RequestParam(defaultValue = "xls") String format, @RequestParam(required = false) String printStatus,
			OAuth2Authentication authentication, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName = "EnquiryStatusDealerwise Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();

//		jasperParameter.put("ENQUIRYTYPE", 0);
//		jasperParameter.put("ENQUIRYLIST", 0);
//		jasperParameter.put("ENQUIRYSOURCE", 0);
//		jasperParameter.put("CLUSTER", null);
		jasperParameter.put("STATEID", requestModel.getStateId());
		jasperParameter.put("FROMDATE", requestModel.getFromDate());
		jasperParameter.put("TODATE", requestModel.getToDate());
		jasperParameter.put("DealerId", requestModel.getDealerId());
		String filePath = "";
		String property = System.getProperty("os.name");
		if (property.contains("Windows")) {
			filePath = downloadPath;
		} else {
			filePath = "/var/VST-DMS-APPS/FILES/REPORTS/ENQ/";
		}
		JasperPrint jasperPrint = enquirySearchDao.ExcelGeneratorReport(request, "EnquiryStatusDealerwise.jasper",
				jasperParameter, filePath);
		try {

			if (format != null && format.equalsIgnoreCase("xls")) {
				// response.setContentType("application/xls");
				response.setHeader("Content-Disposition", "attachment; filename=" + reportName + ".xlsx");
				response.setContentType("application/vnd.ms-excel");
			}

			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();

			enquirySearchDao.printReport(jasperPrint, format, printStatus, outputStream, reportName);

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

	@PostMapping("/exportENQExcelwithDealerDetailsReport")
	public void exportENQExcelwithDealerDetailsReport(@RequestBody EnquiryReportRequest requestModel,
			@RequestParam(defaultValue = "xls") String format, @RequestParam(required = false) String printStatus,
			OAuth2Authentication authentication, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName = "Enquiry Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("FROMDATE", null);
		jasperParameter.put("TODATE", null);
		jasperParameter.put("MODEL", null);
		jasperParameter.put("ENQUIRYTYPE", null);
		jasperParameter.put("ENQUIRYLIST", null);
		jasperParameter.put("ENQUIRYSOURCE", null);
		jasperParameter.put("CLUSTER", null);
		jasperParameter.put("STATEID", requestModel.getStateId());
		jasperParameter.put("DEALERID", requestModel.getDealerId().intValue());

		String filePath = "";
		String property = System.getProperty("os.name");
		if (property.contains("Windows")) {
			filePath = downloadPath;
		} else {
			filePath = "/var/VST-DMS-APPS/FILES/REPORTS/ENQ/";
		}
		JasperPrint jasperPrint = enquirySearchDao.ExcelGeneratorReport(request, "EnquiryStatusDealerDetails.jasper",
				jasperParameter, filePath);
		try {

			if (format != null && format.equalsIgnoreCase("xls")) {
				// response.setContentType("application/xls");
				response.setHeader("Content-Disposition", "attachment; filename=" + reportName + ".xlsx");
				response.setContentType("application/vnd.ms-excel");
			}

			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();

			enquirySearchDao.printReport(jasperPrint, format, printStatus, outputStream, reportName);

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
	
	
	@GetMapping("/exportEnquiryListPreferences")
	public ResponseEntity<?> exportEnquiryListPreferences(OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
//		SimpleDateFormat formatter = getSimpleDateFormat();
		List<ListPreference> responseModel = enquirySearchDao.exportEnquiryListPreferences(userCode);
		
		
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	@PostMapping("/updateEnqColumnList")
	public ResponseEntity<?> updateEnqColumnList(@RequestBody List<ColumnEntity> requestPayload,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		EnquiryCreateResponseModel responseModel = enquirySearchDao.createColumnSetting(userCode,
				requestPayload);
		if (responseModel.getStatusCode() == 201) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Column added on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else if (responseModel.getStatusCode() == 500) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Column not saved");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
		 
	}
}
