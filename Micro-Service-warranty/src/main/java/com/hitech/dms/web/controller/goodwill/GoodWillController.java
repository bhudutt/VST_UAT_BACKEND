package com.hitech.dms.web.controller.goodwill;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mobile.device.Device;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.app.config.ConnectionConfiguration;
import com.hitech.dms.app.exceptions.ErrorDetails;
import com.hitech.dms.web.common.ApiResponse;
import com.hitech.dms.web.entity.goodwill.WarrantyGoodwill;
import com.hitech.dms.web.model.goodwill.GoodwillApprovalRequestDto;
import com.hitech.dms.web.model.goodwill.GoodwillSearchRequestDto;
import com.hitech.dms.web.model.pcr.PcrSearchRequestDto;
import com.hitech.dms.web.service.goodwill.GoodWillService;
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
@RequestMapping("/warranty/goodwill")
@SecurityRequirement(name = "hitechApis")
public class GoodWillController {
	
	@Autowired
	GoodWillService goodWillService;
	
	@Value("${file.upload-dir.C:\\VST-DMS-APPS\\FILES\\Template\\wgc\\}")
    private String uploadDir;
	
	@Autowired
	private ConnectionConfiguration dataSourceConnection;
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	/**
	 * @author suraj.gaur
	 * @param authorizationHeader
	 * @param requestModel
	 * @param files
	 * @param bindingResult
	 * @param authentication
	 * @param device
	 * @return ResponseEntity<?>
	 */
	@PostMapping("/saveGoodwill")
	public ResponseEntity<?> saveGoodwill(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestPart(value="requestModel") WarrantyGoodwill requestModel,
			@RequestPart(required = false) List<MultipartFile> files,
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
			codeResponse.setMessage("Goodwill not created or server side error.");
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
		
		ApiResponse<?> response = goodWillService.saveGoodwill(authorizationHeader, userCode, requestModel, files);
		
		if (response != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Successful on " + formatter.format(new Date()));
			codeResponse.setMessage("Goodwill created Successfully");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Error While Creating Goodwill.");
		}
		
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/autoSearchGoodwillNo")
	public ResponseEntity<?> autoSearchGoodwillNo(@RequestParam("goodwillNo") String goodwillNo,
			OAuth2Authentication authentication) {
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		ApiResponse<?>  response = goodWillService.autoSearchGoodwillNo(goodwillNo);
		if (response != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Goodwill Number on" + formatter.format(new Date()));
			codeResponse.setMessage("Goodwill Number Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Goodwill Number Not Fetched or server side error.");
		}
		
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@GetMapping("/autoSearchJcNo")
	public ResponseEntity<?> autoSearchJC(@RequestParam("roNo") String roNo,
			OAuth2Authentication authentication) {
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		ApiResponse<?>  response = goodWillService.autoSearchJcNo(roNo);
		if (response != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch JC Number on" + formatter.format(new Date()));
			codeResponse.setMessage("JC Number Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("JC Number Not Fetched or server side error.");
		}
		
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@GetMapping("/autoSearchPcrNo")
	public ResponseEntity<?> autoSearchPcrNo(@RequestParam("pcrNo") String pcrNo,
			OAuth2Authentication authentication) {
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		ApiResponse<?>  response = goodWillService.autoSearchPcrNo(pcrNo);
		if (response != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch PCR Number on" + formatter.format(new Date()));
			codeResponse.setMessage("PCR Number Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("PCR Number Not Fetched or server side error.");
		}
		
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/autoSearchChassisNo")
	public ResponseEntity<?> autoSearchChassisNo(@RequestParam("chassisNo") String chassisNo,
			OAuth2Authentication authentication) {
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		ApiResponse<?>  response = goodWillService.autoSearchChassisNo(chassisNo);
		if (response != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch PCR Number on" + formatter.format(new Date()));
			codeResponse.setMessage("Chassis Number Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Chassis Number Not Fetched or server side error.");
		}
		
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@PostMapping(value = "/goodwillSearchList")
	public ResponseEntity<?> goodwillSearchList(@RequestBody GoodwillSearchRequestDto requestModel,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		
		ApiResponse<?> response = goodWillService.goodwillSearchList(userCode, requestModel);
		if (response != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("GOODWILL Search List on " + formatter.format(new Date()));
			codeResponse.setMessage("GOODWILL Search Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("GOODWILL Search Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@PostMapping("/approveRejectGoodwill")
	public ResponseEntity<?> approveRejectGoodwill(@RequestBody GoodwillApprovalRequestDto requestModel,
			OAuth2Authentication authentication) {

		String userCode = null;
		userCode = authentication != null ? authentication.getUserAuthentication().getName() : null;
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();

		ApiResponse<?> response = goodWillService.approveRejectGoodwill(userCode, requestModel);

		if (response != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Goodwill Approval on " + formatter.format(new Date()));
			codeResponse.setMessage(response.getMessage());
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Goodwill Approval  Not Fetched or server side error.");
		}

		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/viewGoodwill")
	public ResponseEntity<?> viewGoodwill(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestParam("goodwillId") BigInteger goodwillId,
			OAuth2Authentication authentication) 
	{
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		
		ApiResponse<?> response = goodWillService.viewGoodwill(goodwillId);
		
		if (response != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Goodwill View List on: " + formatter.format(new Date()));
			codeResponse.setMessage("Goodwill View List Successfully Fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on: " + formatter.format(new Date()));
			codeResponse.setMessage("Goodwill View List Not Fetched or Server Side Error.");
		}
		
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@PostMapping("/exportGoodWillSearchDetail")
	public void exportGoodWillSearchDetail(@RequestBody GoodwillSearchRequestDto searchRequest,
			@RequestParam(defaultValue = "xls") String format, @RequestParam(required = false) String printStatus,
			OAuth2Authentication authentication, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName = "Goodwill-Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("UserCode", userCode);
		jasperParameter.put("goodwillNo", searchRequest.getGoodwillNo());
		jasperParameter.put("pcrNo", searchRequest.getPcrNo());
		jasperParameter.put("jobcardNo", searchRequest.getJobCardNo());
		jasperParameter.put("chassisNo", searchRequest.getChassisNo());
		jasperParameter.put("status", searchRequest.getStatus());
		jasperParameter.put("fromDate", searchRequest.getFromDate());
		jasperParameter.put("toDate", searchRequest.getToDate());
		jasperParameter.put("page", searchRequest.getPage());
		jasperParameter.put("size", searchRequest.getSize());

		String filePath = "";
		String property = System.getProperty("os.name");
		if (property.contains("Windows")) {
			filePath = uploadDir;
		} else {
			filePath = "/var/VST-DMS-APPS/FILES/Template/wgc/";
		}
	

		JasperPrint jasperPrint = pdfGeneratorReport(request, 
				"GoodWill.jasper", jasperParameter, filePath);
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
	
	
	
	@GetMapping("/goodwillPrint")
	public void pcrPrint(@RequestParam("goodwillId") Integer goodwillId, 
			@RequestParam(defaultValue = "pdf") String format, @RequestParam(required = false) String printStatus,
			OAuth2Authentication authentication, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName = "goodwillPrint";
		String filePath = "";
		String imagePath = "";
		String property = System.getProperty("os.name");
		if (property.contains("Windows")) {
			filePath = uploadDir;
			imagePath = "C:\\VST-DMS-APPS\\FILES\\Template\\wgc\\Image\\";
		} else {
			filePath = "/var/VST-DMS-APPS/FILES/Template/wgc/";
			imagePath = "/var/VST-DMS-APPS/FILES/Template/wgc/Image/";
		}
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("goodwillId", goodwillId);
		jasperParameter.put("imagePath", imagePath);
	

		JasperPrint jasperPrint = pdfGeneratorReport(request, 
				"goodwillfront.jasper", jasperParameter, filePath);
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
