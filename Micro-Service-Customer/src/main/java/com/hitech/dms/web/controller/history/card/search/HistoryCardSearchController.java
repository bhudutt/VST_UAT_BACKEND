package com.hitech.dms.web.controller.history.card.search;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.app.config.persistence.ConnectionConfiguration;
import com.hitech.dms.app.utils.DateToStringParserUtils;
import com.hitech.dms.web.model.service.report.request.HistoryCardResponseList;
import com.hitech.dms.web.model.service.report.request.RemiderSearchBean;
import com.hitech.dms.web.service.history.card.HistoryCardService;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

@RestController
@RequestMapping("historyCard")
public class HistoryCardSearchController {
	
	
	@Autowired
	private ConnectionConfiguration dataSourceConnection;
	

	@Value("${file.upload-dir.historyCardServiceTemplate}")
    private String historyCardExcelPath;
	
	
	
	@Autowired
	private HistoryCardService historyCardService;
	
	
	/**
	 * 
	 * @param authorizationHeader
	 * @param authentication
	 * @param device
	 * @param request
	 * @return
	 */
	@GetMapping(value = "/search")
	public ResponseEntity<?> search(@RequestParam(name = "chassisNo") String chassisNo,
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		MessageCodeResponse responseModel = null;
		HistoryCardResponseList responseModelList = historyCardService.getsearchDetails(authorizationHeader,
				userCode,chassisNo);
		if (responseModelList != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch JobCard search List " + LocalDate.now());
			codeResponse.setMessage("Fetch History Card Search List Successfully");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("History Card Seach List Not Fetched !!!.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@PostMapping("/excel")
	public void generateReport(@RequestParam(name = "chassisNo") String chassisNo, @RequestParam(defaultValue = "xlsx") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		String userCodes = null;
		if (authentication != null) {
			userCodes = authentication.getUserAuthentication().getName();
		}
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
			
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		jasperParameter.put("chassis", chassisNo);
		jasperParameter.put("flag", 1);
		
	
		
		String filePath="";
		String property = System.getProperty("os.name");
		
		if(property.contains("Windows")) {
			filePath=historyCardExcelPath;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/History-Card/Excel/";
		}
		
		JasperPrint jasperPrint = xlsxGeneratorReport(request, "ServiceHistoryCard.jasper" , jasperParameter,filePath);
		try {
			response.setContentType("application/xlsx");
			response.setHeader("Content-Disposition", "inline; filename=ServiceHistoryCard.xlsx");
			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();
			printReport(jasperPrint, format, printStatus, outputStream,null);

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
	
	
	public JasperPrint xlsxGeneratorReport( 
			  HttpServletRequest request, String jaspername,
			  HashMap<String, Object> jasperParameter,String downloadPath) {
		
//		String filePath = request.getServletContext().getRealPath("/reports/" + jaspername);
		JasperPrint jasperPrint = null;
		Connection connection = null;
		try {
			String filePath=downloadPath+jaspername;
			System.out.println("filePath  "+filePath);
			connection = dataSourceConnection.getConnection();
        System.out.println("jasperParameter "+jasperParameter);

        if (connection != null) {
				jasperPrint = JasperFillManager.fillReport(filePath, jasperParameter, connection);
			}
		} catch (Exception e) {
			jasperPrint = null;
			e.printStackTrace();
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (Exception e) {
				jasperPrint = null;
				e.printStackTrace();
			}
		}
		return jasperPrint;
	}
	
	public void printReport(JasperPrint jasperPrint, String format, String printStatus, OutputStream outputStream, String reportName)
			throws Exception {

		JRXlsxExporter exporter = new JRXlsxExporter();
		SimpleXlsxReportConfiguration reportConfigXLS = new SimpleXlsxReportConfiguration();
        reportConfigXLS.setSheetNames(new String[] { "sheet1" });
        exporter.setConfiguration(reportConfigXLS);
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
       
        exporter.exportReport();
	}
	

	
	
//	/**
//	 * 
//	 * @param authorizationHeader
//	 * @param authentication
//	 * @param device
//	 * @param request
//	 * @return
//	 */
//	@PostMapping(value = "/getchassisNoList")
//	public ResponseEntity<?> getchassisNoList(@RequestParam(name = "chassisNo") String chassisNo,
//			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
//			OAuth2Authentication authentication, Device device,
//			HttpServletRequest request) {
//		String userCode = null;
//		if (authentication != null) {
//			userCode = authentication.getUserAuthentication().getName();
//		}
//		HeaderResponse userAuthResponse = new HeaderResponse();
//		MessageCodeResponse codeResponse = new MessageCodeResponse();
//		MessageCodeResponse responseModel = null;
//		List<HistoryCardResponseList> responseModelList = historyCardService.getsearchDetails(authorizationHeader,
//				userCode,chassisNo);
//		if (responseModelList != null && !responseModelList.isEmpty()) {
//			codeResponse.setCode("EC200");
//			codeResponse.setDescription("Fetch JobCard search List " + LocalDate.now());
//			codeResponse.setMessage("Fetch JobCard Search List Successfully");
//		} else {
//			codeResponse.setCode("EC500");
//			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
//			codeResponse.setMessage("Service reminder seach List Not Fetched !!!.");
//		}
//		userAuthResponse.setResponseCode(codeResponse);
//		userAuthResponse.setResponseData(responseModelList);
//		return ResponseEntity.ok(userAuthResponse);
//	}
	
	

}
