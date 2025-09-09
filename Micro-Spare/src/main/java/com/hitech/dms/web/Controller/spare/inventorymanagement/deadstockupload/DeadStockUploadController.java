package com.hitech.dms.web.Controller.spare.inventorymanagement.deadstockupload;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.app.config.ConnectionConfiguration;
import com.hitech.dms.app.exceptions.ErrorDetails;
import com.hitech.dms.app.utils.ApiResponse;
import com.hitech.dms.web.entity.spare.inventorymanagement.deadstockupload.DeadStockUploadEntity;
import com.hitech.dms.web.model.spare.inventorymanagement.deadstockupload.DeadStockSearchRequest;
import com.hitech.dms.web.model.spare.inventorymanagement.physicalinventory.exportPhyInvDto;
import com.hitech.dms.web.service.spare.inventorymanagement.deadstockupload.DeadStockUploadService;
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
 * @author mahesh.kumar
 */
@RestController
@RequestMapping("/inventoryManagement/deadStockUpload")
@SecurityRequirement(name = "hitechApis")
public class DeadStockUploadController {
	
	@Autowired
	private DeadStockUploadService deadStockUploadService;
	
	@Autowired
	private ConnectionConfiguration dataSourceConnection;
	
//	@Value("${file.upload-dir.Physical-Inventory:C:\\\\VST-DMS-APPS\\FILES\\Template\\\\physical-inventory\\\\\\\\}")
//    private String uploadDir;
	
	@Value("${file.upload-dir.deadStockUpload:C:\\VST-DMS-APPS\\FILES\\Template\\dead-stock\\\\}")
	private String templateDownloadPath;
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	/**
	 * @author mahesh.kumar
	 * @param filename
	 * @param response
	 * @param request
	 * @return
	 */
	@GetMapping(value = "/downloadTempForDeadStock")
	public ResponseEntity<?> downloadTemplate(@RequestParam("filename") String filename, 
			HttpServletResponse response, HttpServletRequest request) 
	{
		HttpHeaders headers = new HttpHeaders();
		Resource resource = null;
		try {
			String filePath = "";
			String property = System.getProperty("os.name");
			
			if (property.contains("Windows")) {
				filePath = templateDownloadPath;
			} else {
				filePath = "/var/VST-DMS-APPS/FILES/Template/dead-stock/";
			}

			Path path = Paths.get(filePath + filename);
			resource = new UrlResource(path.toUri());
			response.setContentType("application/octet-stream");
			headers.add("Content-Disposition", "attachment ; filename =" + filename);
			headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok().headers(headers).body(resource);
	}
	
	
	
	/**
	 * @author mahesh.kumar
	 * @param authorizationHeader
	 * @param branchId
	 * @param file
	 * @param authentication
	 * @return
	 */
	@PostMapping("/uploadDeadStock")
	public ResponseEntity<?> uploadDeadStock(
			@RequestParam(name = "branchId") BigInteger branchId, 
			@RequestPart(required = true) MultipartFile deadStockFile, 
			OAuth2Authentication authentication) 
	{
		@SuppressWarnings("unused")
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		
		if (deadStockFile == null || deadStockFile.isEmpty()) {
			codeResponse.setCode("EC401");
			codeResponse.setDescription("Dead Stock Items " + formatter.format(new Date()));
			codeResponse.setMessage("File is not provided or empty");
			userAuthResponse.setResponseCode(codeResponse);
			return ResponseEntity.ok(userAuthResponse);
		}
		
		if(!deadStockFile.getOriginalFilename().equalsIgnoreCase("DeadStockUpload.xlsx")) {
			codeResponse.setCode("EC401");
			codeResponse.setDescription("Dead Stock Items " + formatter.format(new Date()));
			codeResponse.setMessage("File should be uploaded which is downloaded from given template!");
			userAuthResponse.setResponseCode(codeResponse);
			return ResponseEntity.ok(userAuthResponse);
		}
		
		ApiResponse<?> response = deadStockUploadService.uploadDeadStock(branchId,deadStockFile);
		
		if (response.getResult() != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Dead Stock items fetched successfull on: " + formatter.format(new Date()));
			codeResponse.setMessage(response.getMessage());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Dead Stock items not fetched or server side error.");
		}
		
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	/**
	 * @author mahesh.kumar
	 * @param requestModel
	 * @param bindingResult
	 * @param authentication
	 * @param device
	 * @return ResponseEntity<?>
	 */
	@PostMapping("/saveDeadStockUpload")
	public ResponseEntity<?> saveDeadStockUpload(@RequestPart(value = "requestModels")List<DeadStockUploadEntity> requestModels,
			@RequestPart(required = false) List<MultipartFile> files,
			BindingResult bindingResult, OAuth2Authentication authentication, Device device) 
	{	
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		
		if (bindingResult.hasErrors()) {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Dead Stock not Saved or server side error.");
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
		
		ApiResponse<?> response = deadStockUploadService.saveDeadStockUpload(userCode, requestModels, files);
		
		if (response != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Successful on " + formatter.format(new Date()));
			codeResponse.setMessage("Dead Stock Saved Successfully");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Error While Saving Dead Stock.");
		}
		
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	/**
	 * @author mahesh.kumar
	 * @param authentication
	 * @param partNo
	 * @return ResponseEntity
	 */
	
	@GetMapping("/autoCompletePartNo")
	public ResponseEntity<?> autoCompletePartNo(@RequestParam("partNo") String partNo,
			@RequestParam("branchId") BigInteger branchId,OAuth2Authentication authentication) {
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		ApiResponse<?>  response = deadStockUploadService.autoCompletePartNo(partNo, branchId);
		if (response != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Part Number and Description on" + formatter.format(new Date()));
			codeResponse.setMessage("Part Number Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Part Number and Description Not Fetched or server side error.");
		}
		
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@PostMapping("/searchDeadStockUpload")
	public ResponseEntity<?> searchDeadStockUpload(OAuth2Authentication authentication,
			HttpServletRequest request,@RequestBody DeadStockSearchRequest requestModel) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		ApiResponse<?> response = deadStockUploadService.searchDeadStockUpload(userCode, requestModel);
		if (response != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Dead Stock List on " + formatter.format(new Date()));
			codeResponse.setMessage("Dead Stock List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Dead Stock List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	/**
	 * @author mahesh.kumar
	 * @param requestModel
	 * @param response
	 * @param request
	 * @return
	 */
	@PostMapping("/exportDeadStockReport")
	public void exportDeadStockReport(@RequestBody DeadStockSearchRequest requestModel,
			@RequestParam(defaultValue = "xls") String format, @RequestParam(required = false) String printStatus,
			OAuth2Authentication authentication, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName = "Dead-Stock-Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("userCode", userCode);
		jasperParameter.put("partId", requestModel.getPartId());
		jasperParameter.put("page", requestModel.getPage());
		jasperParameter.put("size", requestModel.getSize());

		String filePath = "";
		String property = System.getProperty("os.name");
		if (property.contains("Windows")) {
			filePath = templateDownloadPath;
		} else {
			filePath = "/var/VST-DMS-APPS/FILES/Template/dead-stock/";
		}
	

		JasperPrint jasperPrint = pdfGeneratorReport(request, 
				"DeadStock.jasper", jasperParameter, filePath);
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

}
