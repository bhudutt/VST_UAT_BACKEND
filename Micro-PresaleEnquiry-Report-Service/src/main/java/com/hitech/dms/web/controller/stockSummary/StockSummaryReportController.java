package com.hitech.dms.web.controller.stockSummary;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.web.dao.stockSummary.StockSummaryDao;
import com.hitech.dms.web.model.stockSummary.DeliverySummaryRequest;
import com.hitech.dms.web.model.stockSummary.DeliverySummaryResponse;
import com.hitech.dms.web.model.stockSummary.StockSummaryRequest;
import com.hitech.dms.web.model.stockSummary.StockSummaryResponse;

import net.sf.jasperreports.engine.JasperPrint;

@RestController
@RequestMapping("/summary")
public class StockSummaryReportController {
	
	
	@Autowired
	StockSummaryDao dao;
	
	@Value("${file.upload-dir.EnquiryReport}")
	private String downloadPath;
	
	private static final Logger logger=LoggerFactory.getLogger(StockSummaryReportController.class);
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	@PostMapping("/stockSummaryReport")
	public ResponseEntity<?> deliverySummaryReport(OAuth2Authentication authentication,
			@RequestBody DeliverySummaryRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		System.out.println("at controller "+request);
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		DeliverySummaryResponse response=dao.deliverySummaryReportDao(request,userCode);
		//List<ProfitCenterUnderUserResponseModel> responseModelList = profitCenterDao.fetchPcUnderUserList(userCode);
		if (response != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Summary Report " + formatter.format(new Date()));
			codeResponse.setMessage("SummaryReport  Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Summary Report Fetched or "+response.getStatusMessage());
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@PostMapping("/stockSummaryReport1")
	public ResponseEntity<?> stockSummaryReport(OAuth2Authentication authentication,
			@RequestBody DeliverySummaryRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		System.out.println("at controller "+request);
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		StockSummaryResponse response=dao.stockSummaryReportDao(request,userCode);
		if (response != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Summary Report " + formatter.format(new Date()));
			codeResponse.setMessage("Stock  Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Stock Report Fetched or "+response.getStatusMessage());
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	
	
	
	
	
	@PostMapping("/deliverySummaryReportExcel")
	public void exportENQExcelReport(@RequestBody DeliverySummaryRequest requestModel,
			@RequestParam(defaultValue = "xls") String format, @RequestParam(required = false) String printStatus,
			OAuth2Authentication authentication, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		System.out.println("Request at controller "+requestModel);
		Integer dealerCode=requestModel.getDealerId();
		Integer branchId=requestModel.getBranchId();
		Integer stateId=requestModel.getStateId();
		Integer orgHierarchyId=requestModel.getOrgHierId();
		Integer dealerCode1=null;
		Integer branchId1=null;
		Integer stateId1=null;
		Integer orgHierarchyId1=null;

		
		
		
		String reportName = "Delivery_Summary_Report";
		SimpleDateFormat smdf = getSimpleDateFormat();
	
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		
		jasperParameter.put("userCode",userCode);
		jasperParameter.put("stateId", requestModel.getStateId());
		jasperParameter.put("dealerId", requestModel.getDealerId());
		jasperParameter.put("branchId", requestModel.getBranchId());

		jasperParameter.put("asOnDate", requestModel.getAsOnDate() != null ? requestModel.getAsOnDate() : null);
		jasperParameter.put("profitCenterId", requestModel.getPcId() != null ? requestModel.getPcId() : null);
		jasperParameter.put("orgHierID",
				requestModel.getOrgHierId() != null ? requestModel.getOrgHierId() : null);
		jasperParameter.put("modelId",
				requestModel.getModelId() != null ? requestModel.getModelId() : null);
		jasperParameter.put("itemNumber",
				requestModel.getItemNo() != null ? requestModel.getItemNo() : null);
		jasperParameter.put("page", requestModel.getPage() != null ? requestModel.getPage() : null);
		jasperParameter.put("size", requestModel.getSize() != null ? requestModel.getSize() : null);
		
      System.out.println("after data ");
		String filePath = "";
		String property = System.getProperty("os.name");
		if (property.contains("Windows")) {
			filePath = downloadPath;
		} else {
			filePath = "/var/VST-DMS-APPS/FILES/REPORTS/ENQ/";
		}
		JasperPrint jasperPrint = dao.ExcelGeneratorReport(request, "Delivery_Summary_Report.jasper", jasperParameter,
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

			dao.printReport(jasperPrint, format, printStatus, outputStream, reportName);

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
	// stockSummaryReportExcel
	@PostMapping("/stockSummaryReportExcel")
	public void exportStockSummaryExcel(@RequestBody StockSummaryRequest requestModel,
			@RequestParam(defaultValue = "xls") String format, @RequestParam(required = false) String printStatus,
			OAuth2Authentication authentication, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		System.out.println("Request at controller "+requestModel);
		Integer dealerCode=requestModel.getDealerId();
		Integer branchId=requestModel.getBranchId();
		Integer stateId=requestModel.getStateId();
		Integer orgHierarchyId=requestModel.getOrgHierId();
		Integer dealerCode1=null;
		Integer branchId1=null;
		Integer stateId1=null;
		Integer orgHierarchyId1=null;

		
		
		
		String reportName = "Stock_Summary_Report";
		SimpleDateFormat smdf = getSimpleDateFormat();
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		
		jasperParameter.put("userCode",userCode);
		jasperParameter.put("stateId", requestModel.getStateId());
		jasperParameter.put("dealerId", requestModel.getDealerId());
		jasperParameter.put("branchId", requestModel.getBranchId());

		jasperParameter.put("asOnDate", requestModel.getAsOnDate() != null ? requestModel.getAsOnDate() : null);
		jasperParameter.put("profitCenterId", requestModel.getPcId() != null ? requestModel.getPcId() : null);
		jasperParameter.put("orgHierID",
				requestModel.getOrgHierId() != null ? requestModel.getOrgHierId() : null);
		jasperParameter.put("modelId",
				requestModel.getModelId() != null ? requestModel.getModelId() : null);
		jasperParameter.put("itemNumber",
				requestModel.getItemNo() != null ? requestModel.getItemNo() : null);
		jasperParameter.put("page", requestModel.getPage() != null ? requestModel.getPage() : null);
		jasperParameter.put("size", requestModel.getSize() != null ? requestModel.getSize() : null);
		
      System.out.println("after data ");
		String filePath = "";
		String property = System.getProperty("os.name");
		if (property.contains("Windows")) {
			filePath = downloadPath;
		} else {
			filePath = "/var/VST-DMS-APPS/FILES/REPORTS/ENQ/";
		}
		JasperPrint jasperPrint = dao.ExcelGeneratorReportStockSummary(request, "Stock_Summary_Report.jasper", jasperParameter,
				filePath);
		logger.info("after excel generate");
		try {

			if (format != null && format.equalsIgnoreCase("xls")) {
				response.setHeader("Content-Disposition", "attachment; filename=" + reportName + ".xlsx");
				response.setContentType("application/vnd.ms-excel");
			}

			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();

			dao.printReport(jasperPrint, format, printStatus, outputStream, reportName);

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
