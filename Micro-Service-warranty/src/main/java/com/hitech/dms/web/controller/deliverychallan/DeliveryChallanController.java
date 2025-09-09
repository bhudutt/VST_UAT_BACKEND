package com.hitech.dms.web.controller.deliverychallan;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.SessionFactory;
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
import com.hitech.dms.app.config.ConnectionConfiguration;
import com.hitech.dms.app.exceptions.ErrorDetails;
import com.hitech.dms.web.common.ApiResponse;
import com.hitech.dms.web.entity.deliverychallan.DeliveryChallanHdr;
import com.hitech.dms.web.model.deliverychallan.WarrantyPartsDCRequestDto;
import com.hitech.dms.web.model.deliverychallan.WcrDispatchRequestDto;
import com.hitech.dms.web.service.deliverychallan.DeliveryChallanService;
import com.hitech.dms.web.service.deliverychallan.DeliveryChallanServiceImpl;
import com.lowagie.text.pdf.PdfWriter;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

/**
 * @author suraj.gaur
 */
@RestController
@RequestMapping("/warranty/deliverychallan")
@SecurityRequirement(name = "hitechApis")
public class DeliveryChallanController {
	
private static final Logger logger = LoggerFactory.getLogger(DeliveryChallanServiceImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private DeliveryChallanService challanService;
	
	@Autowired
	private ConnectionConfiguration dataSourceConnection;
	
	@Value("${file.upload-dir.Dispatch:C:\\VST-DMS-APPS\\FILES\\Template\\dispatch\\}")
    private String uploadDir;
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	@PostMapping(value = "/wcrDispatchSearchList")
	public ResponseEntity<?> wcrDispatchSearchList(@RequestBody WcrDispatchRequestDto requestModel,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) 
	{		
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		
		ApiResponse<?> response = challanService.wcrDispatchSearchList(userCode, requestModel);
		
		if (response != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Dispatch Search List on " + formatter.format(new Date()));
			codeResponse.setMessage("Dispatch Search Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Dispatch Search Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@PostMapping(value = "/saveDeliveryChallan")
	public ResponseEntity<?> saveDeliveryChallan(@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody DeliveryChallanHdr requestModel,
			BindingResult bindingResult,
			OAuth2Authentication authentication, 
			Device device) 
	{
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		
		if (bindingResult.hasErrors()) {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Dispatch not created or server side error.");
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
		
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		
		ApiResponse<?> response = challanService.saveDeliveryChallan(authorizationHeader, userCode, requestModel);
		
		if (response.getResult() != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Successful on " + formatter.format(new Date()));
			codeResponse.setMessage("Dispatch created Successfully");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Error While Creating Dispatch.");
		}
		
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping(value = "/wcrItemList")
	public ResponseEntity<?> wcrItemList(@RequestParam String wcrIds, OAuth2Authentication authentication, 
			Device device, HttpServletRequest request) 
	{		
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		
		ApiResponse<?> response = challanService.wcrItemList(userCode, wcrIds);
		
		if (response != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("GOODWILL Search List on " + formatter.format(new Date()));
			codeResponse.setMessage("Dispatch Search Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Dispatch Search Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}	
	
	
	
	@GetMapping("/fetchAllTranspoter")
	public ResponseEntity<?> fetchAllFailureType(OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		ApiResponse<?> response = challanService.fetchAllTranspoter(userCode);
		if (response != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Transpoter Name List on " + formatter.format(new Date()));
			codeResponse.setMessage("Transpoter Name List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Transpoter Name List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	/**
	 * @author mahesh.kumar
	 * @param dcNo
	 * @return ResponseEntity
	 */
	@GetMapping("/autoSearchDcNo")
	public ResponseEntity<?> autoSearchDcNo(@RequestParam String dcNo,
			OAuth2Authentication authentication) 
	{
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();

		ApiResponse<?> response = challanService.autoSearchDcNo(dcNo);

		if (response != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Auto Search DC Number on " + formatter.format(new Date()));
			codeResponse.setMessage(response.getMessage());
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Auto Search DC Number not Fetched or server side error.");
		}

		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	/**
	 * @author mahesh.kumar
	 * @param lrNo
	 * @return ResponseEntity
	 */
	@GetMapping("/autoSearchLrNo")
	public ResponseEntity<?> autoSearchLrNo(@RequestParam String lrNo,
			OAuth2Authentication authentication) 
	{
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();

		ApiResponse<?> response = challanService.autoSearchLrNo(lrNo);

		if (response != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Auto Search LR Number on " + formatter.format(new Date()));
			codeResponse.setMessage(response.getMessage());
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Auto Search LR Number not Fetched or server side error.");
		}

		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@PostMapping(value = "/warrantyPartsDCSearchList")
	public ResponseEntity<?> warrantyPartsDCSearchList(@RequestBody WarrantyPartsDCRequestDto requestModel,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) 
	{		
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		
		ApiResponse<?> response = challanService.warrantyPartsDCSearchList(userCode, requestModel);
		
		if (response != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Warranty Part DC Search List on " + formatter.format(new Date()));
			codeResponse.setMessage("Warranty Part DC Search Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Warranty Part DC Search Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/viewDcList")
	public ResponseEntity<?> viewDcList(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestParam("id") BigInteger id,
			OAuth2Authentication authentication) 
	{
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		
		ApiResponse<?> response = challanService.viewDcList(id);
		
		if (response != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch DC View List on: " + formatter.format(new Date()));
			codeResponse.setMessage("DC View List Successfully Fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on: " + formatter.format(new Date()));
			codeResponse.setMessage("DC View List Not Fetched or Server Side Error.");
		}
		
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@PostMapping("/exportDispatchSearchDetail")
	public void exportDispatchSearchDetail(@RequestBody WarrantyPartsDCRequestDto searchRequest,
			@RequestParam(defaultValue = "xls") String format, @RequestParam(required = false) String printStatus,
			OAuth2Authentication authentication, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName = "Wcr-Part-Dispatch-Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("userCode", userCode);
		jasperParameter.put("dcNo", searchRequest.getDcNo());
		jasperParameter.put("lrNo", searchRequest.getLrNo());
		jasperParameter.put("transporterName", searchRequest.getTransporterName());
		jasperParameter.put("fromDate", searchRequest.getFromDate());
		jasperParameter.put("toDate", searchRequest.getToDate());
		jasperParameter.put("page", searchRequest.getPage());
		jasperParameter.put("size", searchRequest.getSize());

		String filePath = "";
		String property = System.getProperty("os.name");
		if (property.contains("Windows")) {
			filePath = uploadDir;
		} else {
			filePath = "/var/VST-DMS-APPS/FILES/Template/dispatch/";
		}
	

		JasperPrint jasperPrint = pdfGeneratorReport(request, 
				"WCRPARTDISPATCH.jasper", jasperParameter, filePath);
		try {

			if (format != null && format.equalsIgnoreCase("pdf")) {
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "inline; filename=" + reportName + ".pdf");
			} else if (format != null && format.equalsIgnoreCase("xls")) {
				// response.setContentType("application/xlsx");
				response.setHeader("Content-Disposition", "attachment; filename=" + reportName + ".xlsx");
				response.setContentType("application/vnd.ms-excel");
			}

			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();

			printReport(jasperPrint, format, printStatus, outputStream, reportName);

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
	
	public JasperPrint pdfGeneratorReport(HttpServletRequest request, String jaspername,
			HashMap<String, Object> jasperParameter,String downloadPath) {
		//String filePath = request.getServletContext().getRealPath("/reports/" + jaspername);
		JasperPrint jasperPrint = null;
		Connection connection = null;
		try {
			//String filePath = ResourceUtils.getFile("classpath:reports/"+jaspername).getAbsolutePath();
			
			String filePath=downloadPath+jaspername;
			System.out.println("filePath  "+filePath);
			connection = dataSourceConnection.getConnection();

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
	
	public void printReport(JasperPrint jasperPrint, String format, String printStatus, OutputStream outputStream,String reportName)
			throws Exception {

		
		if(format !=null && format.equalsIgnoreCase("pdf")) {
			JRPdfExporter exporter = new JRPdfExporter();

			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
			SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
			if (printStatus != null && printStatus.equals("true")) {
				configuration.setPermissions(PdfWriter.ALLOW_COPY | PdfWriter.ALLOW_PRINTING);
				configuration.setPdfJavaScript("this.print();");
			}
			exporter.setConfiguration(configuration);
			exporter.exportReport();
		}else if(format != null && format.equalsIgnoreCase("xls")) {
			JRXlsxExporter exporter = new JRXlsxExporter();
			SimpleXlsxReportConfiguration reportConfigXLS = new SimpleXlsxReportConfiguration();
	        reportConfigXLS.setSheetNames(new String[] { "sheet1" });
	        exporter.setConfiguration(reportConfigXLS);
	        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
	        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
	       
	        exporter.exportReport();
		}
	}
	
	
	
	@GetMapping("/printDispatch")
	public void pcrPrint(@RequestParam("dcId") BigInteger dcId, 
			@RequestParam(defaultValue = "pdf") String format, @RequestParam(required = false) String printStatus,
			OAuth2Authentication authentication, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName = "DispatchPrint";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("dcId", dcId.intValue());
//     	jasperParameter.put("flag", flag);
		
		String filePath = "";
		String property = System.getProperty("os.name");
		if (property.contains("Windows")) {
			filePath = uploadDir;
		} else {
			filePath = "/var/VST-DMS-APPS/FILES/Template/dispatch/";
		}
	

		JasperPrint jasperPrint = pdfGeneratorReport(request, 
				"WCRPARTDISPATCHfront.jasper", jasperParameter, filePath);
		try {

			if (format != null && format.equalsIgnoreCase("pdf")) {
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "inline; filename=" + reportName + ".pdf");
			} else if (format != null && format.equalsIgnoreCase("xls")) {
				// response.setContentType("application/xlsx");
				response.setHeader("Content-Disposition", "attachment; filename=" + reportName + ".xlsx");
				response.setContentType("application/vnd.ms-excel");
			}

			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();

			printReport(jasperPrint, format, printStatus, outputStream, reportName);

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
