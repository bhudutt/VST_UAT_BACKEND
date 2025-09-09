package com.hitech.dms.web.controller.pcr;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.hitech.dms.web.entity.pcr.ServiceWarrantyPcr;
import com.hitech.dms.web.model.pcr.ComplaintAggregateDto;
import com.hitech.dms.web.model.pcr.JobCardPcrDto;
import com.hitech.dms.web.model.pcr.JobCardPcrViewDto;
import com.hitech.dms.web.model.pcr.PCRApprovalRequestDto;
import com.hitech.dms.web.model.pcr.PcrApprovalResponseDto;
import com.hitech.dms.web.model.pcr.PcrCreateResponseDto;
import com.hitech.dms.web.model.pcr.PcrSearchRequestDto;
import com.hitech.dms.web.model.pcr.PcrSearchResponseDto;
import com.hitech.dms.web.service.common.Utils;
import com.hitech.dms.web.service.pcr.pcrService;
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

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/warranty/pcr")
@SecurityRequirement(name = "hitechApis")
public class WarrantyPcrController {
	
	@Autowired
	private pcrService pcrService;
	
	@Autowired
	private Utils utils;
	
	@Autowired
	private ConnectionConfiguration dataSourceConnection;
	
	@Value("${file.upload-dir.Temp:C:\\\\VST-DMS-APPS\\FILES\\Template\\\\pcr\\\\\\\\}")
    private String uploadDir;
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	@PostMapping(value = "/pcrSearchList")
	public ResponseEntity<?> pcrSearchList(@RequestBody PcrSearchRequestDto requestModel,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		
		ApiResponse<?> responseModel = pcrService.pcrSearchList(userCode, requestModel);
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("PCR Search List on " + formatter.format(new Date()));
			codeResponse.setMessage("PCR Search Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("PCR Search Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@GetMapping("/fetchAllFailureType")
	public ResponseEntity<?> fetchAllFailureType(OAuth2Authentication authentication,
			HttpServletRequest request,@RequestParam("roId") BigInteger roId) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<Map<String, Object>> responseModelList = pcrService.fetchAllFailureType(userCode, roId);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Failure Type List on " + formatter.format(new Date()));
			codeResponse.setMessage("Failure Type List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Failure Type List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@GetMapping(value ="/getComplaintAggregate")
	public ResponseEntity<?> getComplaintAggregate(@RequestHeader(value="Authorization",required=true)String authorizationHeader,
			OAuth2Authentication authentication,Device device, HttpServletRequest request){
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		MessageCodeResponse responseModel = null;
		
		List<ComplaintAggregateDto> responseModelList = pcrService.getComplaintAggregate(authorizationHeader, userCode);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Complaint Aggregate List " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Complaint Aggregate List Successfully");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Complaint Aggregate List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@GetMapping("/getComplaintCode")
	public ResponseEntity<?> getComplaintCode(OAuth2Authentication authentication,
			HttpServletRequest request,@RequestParam("ID") Integer ID) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<Map<String, Object>> responseModelList = pcrService.getComplaintCode(ID, userCode);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Complaint Code List on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Complaint Code List Successfully");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Complaint Code List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@GetMapping("/fetchJobCardForPcr")
	public ResponseEntity<?> fetchJobCardForPcr(OAuth2Authentication authentication,
			HttpServletRequest request,@RequestParam("roId") BigInteger roId) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		JobCardPcrDto responseModelList = pcrService.fetchJobCardForPcr(userCode, roId);
		if (responseModelList != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Job Card For Pcr List on " + formatter.format(new Date()));
			codeResponse.setMessage("Job Card For Pcr List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Job Card For Pcr List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
    
    @PostMapping("/createPCR")
	public ResponseEntity<?> createPCR(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestPart(value="requestModel") ServiceWarrantyPcr requestModel,
			@RequestPart(required = false) List<MultipartFile> files,
			BindingResult bindingResult,
			OAuth2Authentication authentication, Device device) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
//		List<PcrCreateResponseDto> responseModel = new ArrayList<>();
		System.out.println("requestModel::::::::::::::Controller" + requestModel);
		if (bindingResult.hasErrors()) {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("PCR not created or server side error.");

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
		PcrCreateResponseDto responseModelList = pcrService.createPCR(authorizationHeader, userCode, requestModel, files, device);
		if (responseModelList != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Successful on " + formatter.format(new Date()));
			codeResponse.setMessage("Pcr created Successfully");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Error While Creating PCR.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
    
    
    
    @GetMapping("/autoSearchJcNo")
	public ResponseEntity<?> autoSearchJC(@RequestParam("roNo") String roNo,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<Map<String, Object>> responseModelList = pcrService.autoSearchJcNo(roNo);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch JC Number on" + formatter.format(new Date()));
			codeResponse.setMessage("JC Number Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("JC Number Not Fetched or server side error.");
		}
		
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
    
    
    @GetMapping("/autoSearchPcrNo")
	public ResponseEntity<?> autoSearchPcrNo(@RequestParam("pcrNo") String pcrNo,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<Map<String, Object>> responseModelList = pcrService.autoSearchPcrNo(pcrNo);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch PCR Number on" + formatter.format(new Date()));
			codeResponse.setMessage("PCR Number Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("PCR Number Not Fetched or server side error.");
		}
		
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
    
    
    @PostMapping("/approveRejectPCR")
	public ResponseEntity<?> approveRejectPCR(@RequestBody PCRApprovalRequestDto requestModel,
			OAuth2Authentication authentication) {

		String userCode = null;
		userCode = authentication != null ? authentication.getUserAuthentication().getName() : null;
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();

		PcrApprovalResponseDto responseModel = pcrService.approveRejectPCR(userCode, requestModel);

		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("PCR Approval on " + formatter.format(new Date()));
			codeResponse.setMessage(responseModel.getMsg());
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch PCR Approval  Not Fetched or server side error.");
		}

		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
    
    
    @GetMapping("/viewPCR")
	public ResponseEntity<?> viewPCR(@RequestParam("pcrId") BigInteger pcrId,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		JobCardPcrViewDto responseModel = pcrService.viewPCR(userCode, pcrId);
		if (responseModel != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch PCR View List on" + formatter.format(new Date()));
			codeResponse.setMessage("PCR View List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("PCR View List Not Fetched or server side error.");
		}
		
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
    
    
    
    @PostMapping("/exportPcrSearchDetail")
	public void exportPcrSearchDetail(@RequestBody PcrSearchRequestDto pcrSearchRequest,
			@RequestParam(defaultValue = "xls") String format, @RequestParam(required = false) String printStatus,
			OAuth2Authentication authentication, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName = "PCR-Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("UserCode", userCode);
		jasperParameter.put("pcrNo", pcrSearchRequest.getPcrNo());
		jasperParameter.put("jobcardNo", pcrSearchRequest.getJobCardNo());
		jasperParameter.put("status", pcrSearchRequest.getStatus());
		jasperParameter.put("fromDate", pcrSearchRequest.getFromDate());
		jasperParameter.put("toDate", pcrSearchRequest.getToDate());
		jasperParameter.put("page", pcrSearchRequest.getPage());
		jasperParameter.put("size", pcrSearchRequest.getSize());

		String filePath = "";
		String property = System.getProperty("os.name");
		if (property.contains("Windows")) {
			filePath = uploadDir;
		} else {
			filePath = "/var/VST-DMS-APPS/FILES/Template/pcr/";
		}
	

		JasperPrint jasperPrint = pdfGeneratorReport(request, 
				"pcr.jasper", jasperParameter, filePath);
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
    
    
    @GetMapping("/pcrPrint")
	public void pcrPrint(@RequestParam("pcrId") Integer pcrId, 
			@RequestParam(defaultValue = "pdf") String format, @RequestParam(required = false) String printStatus,
			OAuth2Authentication authentication, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName = "PCRPrint";
		
		String filePath = "";
		String imagePath = "";
		String property = System.getProperty("os.name");
		if (property.contains("Windows")) {
			filePath = uploadDir;
			imagePath = "C:\\VST-DMS-APPS\\FILES\\Template\\pcr\\Image\\";
		} else {
			filePath = "/var/VST-DMS-APPS/FILES/Template/pcr/";
			imagePath = "/var/VST-DMS-APPS/FILES/Template/pcr/Image/";
		}
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("id", pcrId);
     	jasperParameter.put("imagePath", imagePath);
	

		JasperPrint jasperPrint = pdfGeneratorReport(request, 
				"pcrfront.jasper", jasperParameter, filePath);
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
    
    
    @GetMapping("/wcrPrint")
	public void wcrPrint(@RequestParam("pcrId") Integer pcrId, 
			@RequestParam(defaultValue = "pdf") String format, @RequestParam(required = false) String printStatus,
			OAuth2Authentication authentication, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName = "WCRPrint";
		
		String filePath = "";
		String imagePath = "";
		String property = System.getProperty("os.name");
		if (property.contains("Windows")) {
			filePath = uploadDir;
			imagePath = "C:\\VST-DMS-APPS\\FILES\\Template\\pcr\\Image\\";
		} else {
			filePath = "/var/VST-DMS-APPS/FILES/Template/pcr/";
			imagePath = "/var/VST-DMS-APPS/FILES/Template/pcr/Image/";
		}
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		System.out.println("imagePath  "+imagePath);
		jasperParameter.put("id", pcrId);
     	jasperParameter.put("imagePath", imagePath);
	

		JasperPrint jasperPrint = pdfGeneratorReport(request, 
				"WCRReportfront.jasper", jasperParameter, filePath);
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
	
	
	
	@GetMapping("/getRejectedReason")
	public ResponseEntity<?> getRejectedReason(OAuth2Authentication authentication,HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<Map<String, Object>> responseModelList = pcrService.getRejectedReason();
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Rejected Reason List on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Rejected Reason List Successfully");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Rejected Reason List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@GetMapping("/getProductType")
	public ResponseEntity<?> getProductType(OAuth2Authentication authentication,HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<Map<String, Object>> responseModelList = pcrService.getProductType();
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Product Type List on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Product Type List Successfully");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Product Type List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	
	@GetMapping("/getFailureCode")
	public ResponseEntity<?> getDefectCode(OAuth2Authentication authentication,
			HttpServletRequest request,@RequestParam("prodId") Integer prodId) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<Map<String, Object>> responseModelList = pcrService.getDefectCode(prodId, userCode);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Defect Code List on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Defect Code List Successfully");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Defect Code List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@GetMapping("/getFailureDesc")
	public ResponseEntity<?> getDefectDesc(OAuth2Authentication authentication,
			HttpServletRequest request,@RequestParam("defectId") Integer defectId) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<Map<String, Object>> responseModelList = pcrService.getDefectDesc(defectId, userCode);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Defect Description List on " + formatter.format(new Date()));
			codeResponse.setMessage("Fetch Defect Description List Successfully");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Defect Description List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	
	//Created an API for getting an images from stored path
	
	@GetMapping("/getImages/{moduleName}/{id}/{fileName}")
	public ResponseEntity<?> getImages(OAuth2Authentication authentication,@PathVariable("moduleName") String moduleName, 
			@PathVariable("id") BigInteger id,@PathVariable("fileName") String fileName) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		try {
			Resource resource = pcrService.getImages(fileName, id, moduleName, userCode);
			return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentType(MediaType.IMAGE_JPEG) // Set the content type (can be dynamic as per the image type)
                    .body(resource);
        } catch (Exception e) {
            // If the image is not found, return a 404 response
            return ResponseEntity.notFound().build();
        }
	}
	
	
	@GetMapping("/getOsName")
    public Map<String, String> getOsName() {
		Map<String, String> response = new HashMap<>();
		try {
			String osName = utils.getOsName();
			response.put("osName", osName);
		}catch(Exception e) {
			// Log the exception (optional)
            System.err.println("Error occurred while fetching OS name: " + e.getMessage());
            // Return an error message in case of an exception
            response.put("error", "Unable to retrieve OS name");
		}
        return response;
    }
	
	
	@GetMapping("/getDetailedOsInfo")
    public Map<String, Object> getDetailedOsInfo() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Attempt to get the detailed OS information
        	Map<String, String> osInfo = utils.getDetailedOsInfo();
            response.put("osDetails", osInfo);
        } catch (Exception e) {
            // Log the exception (optional)
            System.err.println("Error occurred while fetching OS information: " + e.getMessage());
            // Return an error message in case of an exception
            response.put("error", "Unable to retrieve OS information");
        }

        return response;
    }

}
