/**
 * 
 */
package com.hitech.dms.web.controller.enquiry.export;

import java.io.IOException;
import java.io.OutputStream;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
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
import com.hitech.dms.web.dao.enquiry.search.EnquirySearchReportDao;
import com.hitech.dms.web.model.enquiry.list.response.ServiceBookingModelDTLResponseModel;
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
public class EnquirySearchReportController {
	private static final Logger logger = LoggerFactory.getLogger(EnquirySearchReportController.class);

	@Autowired
	private EnquirySearchReportDao enquirySearchReportDao;
	@Value("${file.upload-dir.EnquiryReport}")
	private String downloadPath;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
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
		List<EnquiryReportResponse> responseModelList = enquirySearchReportDao.getENQListForReport(userCode,
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
		List<EnquiryReportWithDealerWiseResponse> responseModelList = enquirySearchReportDao
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
		List<EnquriyReportByStateResponse> responseModelList = enquirySearchReportDao
				.fetchENQWithStateWiseReport(userCode, reportEnquiryListRequest);
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
		List<EnquiryReportWithDealerWiseResponse> responseModelList = enquirySearchReportDao
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
		
		jasperParameter.put("FROMDATE", requestModel.getFromDate() != null ? requestModel.getFromDate() : null);
		jasperParameter.put("TODATE", requestModel.getToDate() != null ? requestModel.getToDate() : null);
		jasperParameter.put("MODEL", requestModel.getModelIds() != null ? requestModel.getModelIds() : null);
		jasperParameter.put("ENQUIRYTYPE",
				requestModel.getEnquiryTypeId() != 0 ? requestModel.getEnquiryTypeId() : null);
		jasperParameter.put("ENQUIRYLIST",
				requestModel.getEnquiryStatusId() != 0 ? requestModel.getEnquiryStatusId() : null);
		jasperParameter.put("ENQUIRYSOURCE",
				requestModel.getEnquirySourceId() != 0 ? requestModel.getEnquirySourceId() : null);
		jasperParameter.put("CLUSTER", requestModel.getClusterId() != null ? requestModel.getClusterId() : null);
		jasperParameter.put("STATEID", requestModel.getStateId() != 0 ? requestModel.getStateId() : null);
		jasperParameter.put("PCID", requestModel.getProfitCenterId() != 0 ? requestModel.getProfitCenterId() : null);
		jasperParameter.put("USERCODE", userCode);

		String filePath = "";
		String property = System.getProperty("os.name");
		if (property.contains("Windows")) {
			filePath = downloadPath;
		} else {
			filePath = "/var/VST-DMS-APPS/FILES/REPORTS/ENQ/";
		}
		JasperPrint jasperPrint = enquirySearchReportDao.ExcelGeneratorReport(request, "EnquiryStatusStatewise.jasper",
				jasperParameter, filePath);
		try {

			if (format != null && format.equalsIgnoreCase("xls")) {
				// response.setContentType("application/xls");
				response.setHeader("Content-Disposition", "attachment; filename=" + reportName + ".xlsx");
				response.setContentType("application/vnd.ms-excel");
			}

			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();

			enquirySearchReportDao.printReport(jasperPrint, format, printStatus, outputStream, reportName);

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

		jasperParameter.put("FROMDATE", requestModel.getFromDate() != null ? requestModel.getFromDate() : null);
		jasperParameter.put("TODATE", requestModel.getToDate() != null ? requestModel.getToDate() : null);
		jasperParameter.put("MODEL", requestModel.getModelIds() != null ? requestModel.getModelIds() : null);
		jasperParameter.put("ENQUIRYTYPE",
				requestModel.getEnquiryTypeId() != 0 ? requestModel.getEnquiryTypeId() : null);
		jasperParameter.put("ENQUIRYLIST",
				requestModel.getEnquiryStatusId() != 0 ? requestModel.getEnquiryStatusId() : null);
		jasperParameter.put("ENQUIRYSOURCE",
				requestModel.getEnquirySourceId() != 0 ? requestModel.getEnquirySourceId() : null);
		jasperParameter.put("CLUSTER", requestModel.getClusterId() != null ? requestModel.getClusterId() : null);
		jasperParameter.put("STATEID", requestModel.getStateId() != 0 ? requestModel.getStateId() : null);
		jasperParameter.put("DEALERID",
				requestModel.getDealerId() != null ? requestModel.getDealerId().intValue() : null);
		jasperParameter.put("PCID", requestModel.getProfitCenterId() != 0 ? requestModel.getProfitCenterId() : null);
		jasperParameter.put("USERCODE", userCode);
		
		String filePath = "";
		String property = System.getProperty("os.name");
		if (property.contains("Windows")) {
			filePath = downloadPath;
		} else {
			filePath = "/var/VST-DMS-APPS/FILES/REPORTS/ENQ/";
		}
		JasperPrint jasperPrint = enquirySearchReportDao.ExcelGeneratorReport(request, "EnquiryStatusDealerwise.jasper",
				jasperParameter, filePath);
		try {

			if (format != null && format.equalsIgnoreCase("xls")) {
				// response.setContentType("application/xls");
				response.setHeader("Content-Disposition", "attachment; filename=" + reportName + ".xlsx");
				response.setContentType("application/vnd.ms-excel");
			}

			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();

			enquirySearchReportDao.printReport(jasperPrint, format, printStatus, outputStream, reportName);

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

		jasperParameter.put("FROMDATE", requestModel.getFromDate() != null ? requestModel.getFromDate() : null);
		jasperParameter.put("TODATE", requestModel.getToDate() != null ? requestModel.getToDate() : null);
		jasperParameter.put("MODEL", requestModel.getModelIds() != null ? requestModel.getModelIds() : null);
		jasperParameter.put("ENQUIRYTYPE",
				requestModel.getEnquiryTypeId() != 0 ? requestModel.getEnquiryTypeId() : null);
		jasperParameter.put("ENQUIRYLIST",
				requestModel.getEnquiryStatusId() != 0 ? requestModel.getEnquiryStatusId() : null);
		jasperParameter.put("ENQUIRYSOURCE",
				requestModel.getEnquirySourceId() != 0 ? requestModel.getEnquirySourceId() : null);
		jasperParameter.put("CLUSTER", requestModel.getClusterId() != null ? requestModel.getClusterId() : null);
		jasperParameter.put("STATEID", requestModel.getStateId() != 0 ? requestModel.getStateId() : null);
		jasperParameter.put("DEALERID",
				requestModel.getDealerId() != null ? requestModel.getDealerId().intValue() : null);
		jasperParameter.put("PCID", requestModel.getProfitCenterId() != 0 ? requestModel.getProfitCenterId() : null);
		jasperParameter.put("USERCODE", userCode);

		String filePath = "";
		String property = System.getProperty("os.name");
		if (property.contains("Windows")) {
			filePath = downloadPath;
		} else {
			filePath = "/var/VST-DMS-APPS/FILES/REPORTS/ENQ/";
		}
		JasperPrint jasperPrint = enquirySearchReportDao.ExcelGeneratorReport(request,
				"EnquiryStatusDealerDetails.jasper", jasperParameter, filePath);
		try {

			if (format != null && format.equalsIgnoreCase("xls")) {
				// response.setContentType("application/xls");
				response.setHeader("Content-Disposition", "attachment; filename=" + reportName + ".xlsx");
				response.setContentType("application/vnd.ms-excel");
			}

			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();

			enquirySearchReportDao.printReport(jasperPrint, format, printStatus, outputStream, reportName);

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

	@GetMapping("/fetchModelAllList")
	public ResponseEntity<?> fetchModelAllList(OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<ServiceBookingModelDTLResponseModel> responseModelList = enquirySearchReportDao.fetchModelAllList(userCode,
				device);
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

	@GetMapping("/getStateIdFormStateDesc")
	public ResponseEntity<?> getStateIdFormStateDesc(OAuth2Authentication authentication,
			@RequestParam(value = "stateDesc") String stateDesc, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		Map<String, Object> responseModelList = enquirySearchReportDao.getStateIdFormStateDesc(userCode, stateDesc);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("State Details List on " + formatter.format(new Date()));
			codeResponse.setMessage("State Details List Successfully fetched");
		} else {
			codeResponse.setCode("EC404");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("State Details Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
}
