package com.hitech.dms.web.controller.enquiry.export;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
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
import com.hitech.dms.web.dao.enquiry.export.EnquirySearchDao;
import com.hitech.dms.web.model.enquiry.export.EnquiryListRequestModel;
import com.hitech.dms.web.model.enquiry.export.EnquiryReport2Response;
import com.hitech.dms.web.model.enquiry.export.EnquiryResponse;
import com.hitech.dms.web.model.enquiry.export.SalesManModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import net.sf.jasperreports.engine.JasperPrint;

@Validated
@RestController
@RequestMapping("/search/report")
@SecurityRequirement(name = "hitechApis")
public class PresalesEnquiryReport {

	private static final Logger logger = LoggerFactory.getLogger(PresalesEnquiryReport.class);

	@Autowired
	EnquirySearchDao enquiryDao;
	
	//ENQUIRY_NO_SEARCH

	@Value("${file.upload-dir.EnquiryReport}")
	private String downloadPath;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("yyyy/MM/dd");
	}

	@PostMapping("/fetchENQListReport1")
	public ResponseEntity<?> fetchENQList(@RequestBody EnquiryListRequestModel enquiryListRequestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		logger.info("at controller request " + enquiryListRequestModel);
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		EnquiryReport2Response responseModel = enquiryDao.fetchEnquiryList(userCode, enquiryListRequestModel);
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch ReportEnquiry Serach on " + formatter.format(new Date()));
			codeResponse.setMessage("ReportEnquiry List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Data Not Available for particular Date");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}

	// search DocumentNo
	@GetMapping("/enquiryNo")
	public ResponseEntity<?> searchEnquiryNo(
			@RequestParam() String searchText,
			OAuth2Authentication authentication, Device device, HttpServletRequest request)
	{
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
			
		}
		logger.info("request at controller"+searchText);
		
		List<String> response=enquiryDao.searchDocumentNo(searchText, userCode);
		logger.info("response at controller "+response);
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		
		if (response != null ) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("" + new Date());
			codeResponse.setMessage("Data Search Successfully ");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + new Date());
			codeResponse.setMessage("Data found Unsuccessful");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
		
		
	}
	
	// get SalesManList
	@GetMapping("/fetchSalesManList")
	public ResponseEntity<?> fetchSalesManList(
			@RequestParam(required = false) String dealerId,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		logger.info("at controller salesMan ");
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<SalesManModel> responseModel = enquiryDao.fetchSalesManList(userCode,dealerId);
		if (responseModel != null && !responseModel.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch ReportEnquiry Serach on " + formatter.format(new Date()));
			codeResponse.setMessage("salesMan List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("salesMan Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/exportENQExcelReport1")
	public void exportENQExcelReport(@RequestBody EnquiryListRequestModel requestModel,
			@RequestParam(defaultValue = "xls") String format, @RequestParam(required = false) String printStatus,
			OAuth2Authentication authentication, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		BigInteger dealerCode=requestModel.getDealerId();
		BigInteger branchId=requestModel.getBranchId();
		BigInteger stateId=requestModel.getStateId();
		BigInteger orgHierarchyId=requestModel.getOrgHierarchyId();
		Integer dealerCode1=null;
		Integer branchId1=null;
		Integer stateId1=null;
		Integer orgHierarchyId1=null;
		if(dealerCode!=null)
		{
			dealerCode1=requestModel.getDealerId().intValue();
		}
		
		if(branchId!=null)
		{
			branchId1=requestModel.getBranchId().intValue();
		}
		
		if(stateId!=null)
		{
			stateId1=requestModel.getStateId().intValue();
		}
		if(orgHierarchyId!=null)
		{
			orgHierarchyId1=requestModel.getOrgHierarchyId().intValue();
		}
		
		
		
		String reportName = "Enquiry Report2";
		SimpleDateFormat smdf = getSimpleDateFormat();
		//String fromDate = smdf.format(requestModel.getFromDate());
		//String toDate = smdf.format(requestModel.getToDate());
		String prospectType = requestModel.getProspectType();
		logger.info("prospectType" + prospectType);
		if (prospectType.equalsIgnoreCase("0")) {
			prospectType = null;
		}
		if (requestModel.getEnquiryStatusId().equalsIgnoreCase("0")) {
			requestModel.setEnquiryStatusId(null);
		}
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		
		jasperParameter.put("Usercode",userCode);
		jasperParameter.put("PCID", requestModel.getProfitCenterId());
		jasperParameter.put("dealerId", dealerCode1 != null ? dealerCode1 : null);
		jasperParameter.put("branchId", branchId1 != null ? branchId1 : null);
		jasperParameter.put("enquiryNo",
				requestModel.getEnquiryTypeId() != null ? requestModel.getEnquiryTypeId() : null);
		jasperParameter.put("enquiryStage",
				requestModel.getEnquiryStage() != null ? requestModel.getEnquiryStage() : null);
		jasperParameter.put("enquiryStatus",
				requestModel.getEnquiryStatusId() != null ? requestModel.getEnquiryStatusId() : null);
		
		jasperParameter.put("enquiryFromDate", requestModel.getFromDate1());
		jasperParameter.put("enquiryToDate", requestModel.getToDate1());
		jasperParameter.put("modelID", requestModel.getModelId() != null ? requestModel.getModelId() : null);
		jasperParameter.put("salesManID", requestModel.getSalesManId() != null ? requestModel.getSalesManId() : null);
		jasperParameter.put("enquirySourceID",
		requestModel.getEnquirySourceId() != null ? requestModel.getEnquirySourceId() : null);
		jasperParameter.put("prospectType", prospectType != null ? prospectType : null);
		jasperParameter.put("stateId",stateId1!=null?stateId1:null);
		jasperParameter.put("orgHierID",orgHierarchyId1!=null?orgHierarchyId1:null);
		
		jasperParameter.put("page", requestModel.getPage() != null ? requestModel.getPage() : null);
		jasperParameter.put("size", requestModel.getSize() != null ? requestModel.getSize() : null);
		jasperParameter.put("zone", requestModel.getZone() != null ? requestModel.getZone() : null);
		jasperParameter.put("territory", requestModel.getTerritory() != null ? requestModel.getTerritory() : null);
System.out.println("jasperParameter "+jasperParameter);
		String filePath = "";
		String property = System.getProperty("os.name");
		if (property.contains("Windows")) {
			filePath = downloadPath;
		} else {
			filePath = "/var/VST-DMS-APPS/FILES/REPORTS/ENQ/";
		}
		logger.info("at excel generate ");
		JasperPrint jasperPrint = enquiryDao.ExcelGeneratorReport(request, "EnquiryList2.jasper", jasperParameter,
				filePath);
		logger.info("after excel generate");
		try {

			if (format != null && format.equalsIgnoreCase("xls")) {
				// response.setContentType("application/xls");
				response.setHeader("Content-Disposition", "attachment; filename=" + reportName + ".xlsx");
				response.setContentType("application/vnd.ms-excel");
			}

			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();

			enquiryDao.printReport(jasperPrint, format, printStatus, outputStream, reportName);

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
