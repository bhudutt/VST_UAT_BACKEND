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
import com.hitech.dms.web.dao.enquiry.export.EnquirySearchDao;
import com.hitech.dms.web.model.enquiry.list.request.SalesEnquiryReportRequest;
import com.hitech.dms.web.model.enquiry.list.response.SalesEnquiryReportResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import net.sf.jasperreports.engine.JasperPrint;

/**
 * @author santosh.kumar
 *
 */
@Validated
@RestController
@RequestMapping("/search/SalesReport")
@SecurityRequirement(name = "hitechApis")
public class SalesEnquiryReportController {
	private static final Logger logger = LoggerFactory.getLogger(SalesEnquiryReportController.class);
	@Autowired
	EnquirySearchDao enquiryDao;

	@Value("${file.upload-dir.EnquiryReport}")
	private String downloadPath;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("yyyy/MM/dd");
	}

	/**
	 * 
	 * @param requestModel
	 * @param authentication
	 * @return
	 */
	@PostMapping("/fetchSalesEnquiryList")
	public ResponseEntity<?> fetchSalesEnquiryList(@RequestBody SalesEnquiryReportRequest requestModel,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<SalesEnquiryReportResponse> responseModel = enquiryDao.fetchSalesEnquiryList(userCode, requestModel);
		if (responseModel != null && !responseModel.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch  Sales Enquiry List on " + formatter.format(new Date()));
			codeResponse.setMessage(" Sales Enquiry Serach List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Sales Enquiry Serach List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@PostMapping("/exportEnquirySalesExcelReport")
	public void exportEnquirySalesExcelReport(@RequestBody SalesEnquiryReportRequest requestModel,
			@RequestParam(defaultValue = "xls") String format, @RequestParam(required = false) String printStatus,
			OAuth2Authentication authentication, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName = "SalesEnquiry Report";
		HashMap<String, Object> jasperParameter = new HashMap<>();
		jasperParameter.put("USERCODE",userCode);
		jasperParameter.put("FROMDATE", requestModel.getFromDate() != null ? requestModel.getFromDate() : null);
		jasperParameter.put("TODATE", requestModel.getToDate() != null ? requestModel.getToDate() : null);
		jasperParameter.put("DEALERID", requestModel.getDealerId() != null ? requestModel.getDealerId() : null);
		jasperParameter.put("BRANCHID", requestModel.getBranchId() != null ? requestModel.getBranchId() : null);
		jasperParameter.put("MODEL", requestModel.getModelIds() != null ? requestModel.getModelIds() : null);
		jasperParameter.put("STATEID", requestModel.getStateId() != null ? requestModel.getStateId() : null);
		jasperParameter.put("PCID", requestModel.getProfitCenterId() != null ? requestModel.getProfitCenterId() : null);
		jasperParameter.put("CLUSTER", requestModel.getClusterId() != null ? requestModel.getClusterId() : null);

		String filePath = "";
		String property = System.getProperty("os.name");
		if (property.contains("Windows")) {
			filePath = downloadPath;
		} else {
			filePath = "/var/VST-DMS-APPS/FILES/REPORTS/ENQ/";
		}
		JasperPrint jasperPrint = enquiryDao.ExcelGeneratorReport(request, "SaleReport.jasper", jasperParameter,
				filePath);
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
	
	
	@GetMapping("/unauthorized")
    public ResponseEntity<String> getUnauthorized() {
        return ResponseEntity.status(401).body("Unauthorized");
    }
	
	
	
}
