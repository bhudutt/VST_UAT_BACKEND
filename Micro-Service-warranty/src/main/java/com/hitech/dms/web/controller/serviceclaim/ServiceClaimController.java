package com.hitech.dms.web.controller.serviceclaim;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.validation.BindingResult;
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
import com.hitech.dms.web.entity.serviceclaim.ServiceClaimHdr;
import com.hitech.dms.web.model.serviceclaim.ServiceCLaimApprovalRequestDto;
import com.hitech.dms.web.model.serviceclaim.ServiceClaimJCListRequest;
import com.hitech.dms.web.model.serviceclaim.ServiceClaimRejectApproveStatus;
import com.hitech.dms.web.model.serviceclaim.ServiceClaimSearchRequestDto;
import com.hitech.dms.web.service.serviceclaim.ServiceClaimService;
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
@RequestMapping("/warranty/serviceClaim")
@SecurityRequirement(name = "hitechApis")
public class ServiceClaimController {
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	@Autowired
	private ServiceClaimService claimService;
	
	@Autowired
	private ConnectionConfiguration dataSourceConnection;
	
	@Value("${file.upload-dir.Service-Claim:C:\\VST-DMS-APPS\\FILES\\Template\\service-claim\\}")
    private String uploadDir;
	
	
	@Value("${file.upload-dir.InstallationImg}")
	private String InstallationImg;
	
	/**
	 * @author suraj.gaur
	 * @param requestModel
	 * @param bindingResult
	 * @param authentication
	 * @param device
	 * @return ResponseEntity<?>
	 */
	@PostMapping("/saveServiceClaim")
	public ResponseEntity<?> saveServiceClaim(
			@RequestBody ServiceClaimHdr requestModel,
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
			codeResponse.setMessage("Service Claim not created or server side error.");
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
		
		ApiResponse<?> response = claimService.saveServiceClaim(userCode, requestModel);
		
		if (response != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Successful on " + formatter.format(new Date()));
			codeResponse.setMessage("Service Claim created Successfully");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Error While Creating Service Claim.");
		}
		
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		
		return ResponseEntity.ok(userAuthResponse);
	}
	
	/**
	 * @author suraj.gaur
	 * @param requestModel
	 * @param authentication
	 * @return ResponseEntity
	 */
	@PostMapping("/getJobcardClaimList")
	public ResponseEntity<?> getJobcardClaimList(@RequestBody ServiceClaimJCListRequest requestModel,
			OAuth2Authentication authentication) {

		String userCode = null;
		userCode = authentication != null ? authentication.getUserAuthentication().getName() : null;
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();

		ApiResponse<?> response = claimService.getJobcardClaimList(userCode, requestModel);

		if (response != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Job card claim list " + formatter.format(new Date()));
			codeResponse.setMessage(response.getMessage());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Job card claim list Not Fetched or server side error.");
		}

		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/viewServiceClaim")
	public ResponseEntity<?> viewServiceClaim(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestParam("ID") BigInteger claimId,
			OAuth2Authentication authentication) 
	{
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		
		ApiResponse<?> response = claimService.viewServiceClaim(claimId);
		
		if (response != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Service Claim View List on: " + formatter.format(new Date()));
			codeResponse.setMessage("Service Claim View List Successfully Fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on: " + formatter.format(new Date()));
			codeResponse.setMessage("Service Claim View List Not Fetched or Server Side Error.");
		}
		
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		
		return ResponseEntity.ok(userAuthResponse);
	}

	/**
	 * @author suraj.gaur
	 * @param authentication
	 * @return ResponseEntity
	 */
	@GetMapping("/getClaimTypes")
	public ResponseEntity<?> getClaimTypes(OAuth2Authentication authentication) {
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();

		ApiResponse<?> response = claimService.getClaimTypes();

		if (response != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Job card claim list " + formatter.format(new Date()));
			codeResponse.setMessage(response.getMessage());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Job card claim list Not Fetched or server side error.");
		}

		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@GetMapping("/autoSearchClaimNo")
	public ResponseEntity<?> autoSearchClaimNo(@RequestParam("claimNo") String claimNo,
			OAuth2Authentication authentication) {
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		ApiResponse<?>  response = claimService.autoSearchClaimNo(claimNo);
		if (response != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Claim Number on" + formatter.format(new Date()));
			codeResponse.setMessage("Claim Number Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Claim Number Not Fetched or server side error.");
		}
		
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}	
	
	@GetMapping("/getClaimStatus")
	public ResponseEntity<?> getClaimStatus(OAuth2Authentication authentication) {
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		ApiResponse<?>  response = claimService.getClaimStatus();
		if (response != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Claim Type on" + formatter.format(new Date()));
			codeResponse.setMessage("Claim Status Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Claim Status Not Fetched or server side error.");
		}
		
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping(value = "/serviceClaimSearch")
	public ResponseEntity<?> serviceClaimSearch(@RequestBody ServiceClaimSearchRequestDto requestModel,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		
		ApiResponse<?> response = claimService.serviceClaimSearch(userCode, requestModel);
		if (response != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Service Claim Search List on " + formatter.format(new Date()));
			codeResponse.setMessage("Service Claim Search Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Service Claim Search Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	/**
	 * @author suraj.gaur
	 * @param requestModel
	 * @param authentication
	 * @return ResponseEntity
	 */
	@PostMapping("/approveRejectSVClaim")
	public ResponseEntity<?> approveRejectSVClaim(@RequestBody ServiceCLaimApprovalRequestDto requestModel,
			OAuth2Authentication authentication) {

		String userCode = null;
		userCode = authentication != null ? authentication.getUserAuthentication().getName() : null;
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();

		ApiResponse<?> response = claimService.approveRejectSVClaim(userCode, requestModel);

		if (response != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Service Claim Approval on " + formatter.format(new Date()));
			codeResponse.setMessage(response.getMessage());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Service Claim Approval Not Fetched or server side error.");
		}

		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@PostMapping("/exportServiceClaimSearchDetail")
	public void exportServiceClaimSearchDetail(@RequestBody ServiceClaimSearchRequestDto searchRequest,
			@RequestParam(defaultValue = "xls") String format, @RequestParam(required = false) String printStatus,
			OAuth2Authentication authentication, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName = "Service-Claim-Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("userCode", userCode);
		jasperParameter.put("claimNo", searchRequest.getClaimNo());
		jasperParameter.put("claimTypeId", searchRequest.getClaimTypeId());
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
			filePath = "/var/VST-DMS-APPS/FILES/Template/service-claim/";
		}
	

		JasperPrint jasperPrint = pdfGeneratorReport(request, 
				"FSCPDIINSTALLATIONClaim.jasper", jasperParameter, filePath);
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
	
	/**
	 * @author vivek.gupta
	 * @param requestModel
	 * @param authentication
	 * @return ResponseEntity
	 */
	@PostMapping("/installationAppAndRej")
	public ResponseEntity<?> installationAppAndRej(@RequestBody ServiceClaimRejectApproveStatus bean, 
			OAuth2Authentication authentication) {

		String userCode = null;
		userCode = authentication != null ? authentication.getUserAuthentication().getName() : null;
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();

		ApiResponse<?> response = claimService.installationAppAndRej(userCode, bean.getInstallationId(), bean.getStatusApproveReject());

		if (response != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Service Claim Approval on " + formatter.format(new Date()));
			codeResponse.setMessage(response.getMessage());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Service Claim Approval Not Fetched or server side error.");
		}

		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/openInstallationImage")
	public ResponseEntity<?> downloadInstallationImage(
	        @RequestParam("installationId") Integer installationId) {
		
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();


	    try {
	        String basePath = System.getProperty("os.name").contains("Windows") ?
	        		InstallationImg :
	                "/var/VST-DMS-APPS/FILES/installation/";

	        Path folderPath = Paths.get(basePath, String.valueOf(installationId));

	        // Get first file in the folder
	        Optional<Path> firstFile = Files.list(folderPath)
	                                        .filter(Files::isRegularFile)
	                                        .findFirst();

	        if (firstFile.isEmpty()) {
	            return ResponseEntity.notFound().build();
	        }

	        Path filePath = firstFile.get();
	        String filename = filePath.getFileName().toString();
	        Resource resource = new UrlResource(filePath.toUri());

	        HttpHeaders headers = new HttpHeaders();
	        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
	        headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);

	        // Detect MIME type dynamically
	        String mimeType = Files.probeContentType(filePath);
	        if (mimeType == null) {
	            mimeType = "application/octet-stream";
	        }

	        return ResponseEntity.ok()
	                .headers(headers)
	                .contentType(MediaType.parseMediaType(mimeType))
	                .body(resource);

	    } catch (Exception e) {
	        codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Image not available for this Installation");
			userAuthResponse.setResponseCode(codeResponse);
			return ResponseEntity.ok(userAuthResponse);
	    }
	}
	
	

	
	
}
