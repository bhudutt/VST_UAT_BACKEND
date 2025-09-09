package com.hitech.dms.web.Controller.spare.inventorymanagement.bintobintransfer;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.app.config.ConnectionConfiguration;
import com.hitech.dms.app.exceptions.ErrorDetails;
import com.hitech.dms.app.utils.ApiResponse;
import com.hitech.dms.web.entity.spare.inventorymanagement.bintobintransfer.BinToBinTransferHdr;
import com.hitech.dms.web.model.spare.inventorymanagement.bintobintransfer.SearchBinToBinTransferRequestDto;
import com.hitech.dms.web.service.spare.inventorymanagement.bintobintransfer.BinToBinTransferService;
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
@RequestMapping("/inventoryManagement/binToBinTransfer")
@SecurityRequirement(name = "hitechApis")
public class BinToBinTransferController {
	
	@Autowired
	private ConnectionConfiguration dataSourceConnection;
	
	@Value("${file.upload-dir.BinToBin:C:\\VST-DMS-APPS\\FILES\\Template\\bin-to-bin\\}")
    private String uploadDir;
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	@Autowired
	private BinToBinTransferService binTransferService;
	
	/**
	 * @author suraj.gaur
	 * @param requestModel
	 * @param bindingResult
	 * @param authentication
	 * @param device
	 * @return ResponseEntity<?>
	 */
	@PostMapping("/saveBinToBinTransfer")
	public ResponseEntity<?> saveBinToBinTransfer(@RequestBody BinToBinTransferHdr requestModel,
			BindingResult bindingResult, OAuth2Authentication authentication, Device device) 
	{	
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		
		if (bindingResult.hasErrors()) {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Bin to Bin Transfer Issue not created or server side error.");
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
		
		ApiResponse<?> response = binTransferService.saveBinToBinTransfer(userCode, requestModel);
		
		if (response != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Successful on " + formatter.format(new Date()));
			codeResponse.setMessage("Bin to Bin Transfer Issue created Successfully");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Error While Creating Bin to Bin Transfer Issue .");
		}
		
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		
		return ResponseEntity.ok(userAuthResponse);
	}	
	
	/**
	 * @author mahesh.kumar
	 * @param authentication
	 * @return ResponseEntity
	 */
	@GetMapping("/getSpareEmployee")
	public ResponseEntity<?> getSpareEmployee(@RequestParam("branchId") BigInteger branchId, OAuth2Authentication authentication) {
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();

		ApiResponse<?> response = binTransferService.getSpareEmployee(branchId);

		if (response != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Spare Employee list " + formatter.format(new Date()));
			codeResponse.setMessage(response.getMessage());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Spare Employee list Not Fetched or server side error.");
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
		ApiResponse<?>  response = binTransferService.autoCompletePartNo(partNo, branchId);
		if (response != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Part Number and Description on" + formatter.format(new Date()));
			codeResponse.setMessage("Claim Number Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Part Number and Description Not Fetched or server side error.");
		}
		
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	/**
	 * @author mahesh.kumar
	 * @param authentication
	 * @return ResponseEntity
	 */
	@GetMapping("/getStorelist")
	public ResponseEntity<?> getStorelist(@RequestParam("partBranchId") BigInteger partBranchId,
			@RequestParam("branchId") BigInteger branchId,OAuth2Authentication authentication) {
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();

		ApiResponse<?> response = binTransferService.getStorelist(partBranchId, branchId);

		if (response != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Store list " + formatter.format(new Date()));
			codeResponse.setMessage(response.getMessage());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Store list Not Fetched or server side error.");
		}

		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	/**
	 * @author mahesh.kumar
	 * @param authentication
	 * @return ResponseEntity
	 */
	@GetMapping("/getStoreBinlist")
	public ResponseEntity<?> getStoreBinlist(@RequestParam("binName") String binName,
			@RequestParam("stockStoreId") BigInteger stockStoreId,
			@RequestParam("branchId") BigInteger branchId,
			@RequestParam("partBranchId") BigInteger partBranchId,
			OAuth2Authentication authentication) {
		
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();

		ApiResponse<?> response = binTransferService.getStoreBinlist(binName,branchId,stockStoreId,partBranchId);

		if (response != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Store Bin list " + formatter.format(new Date()));
			codeResponse.setMessage(response.getMessage());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Store Bin list Not Fetched or server side error.");
		}

		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	/**
	 * @author mahesh.kumar
	 * @param issueNo
	 * @param branchId
	 * @return ResponseEntity
	 */
	@GetMapping("/autoSearchIssueNo")
	public ResponseEntity<?> autoSearchIssueNo(@RequestParam String issueNo,
			OAuth2Authentication authentication) 
	{
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();

		ApiResponse<?> response = binTransferService.autoSearchIssueNo(issueNo);

		if (response != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Auto Search Issue Number on " + formatter.format(new Date()));
			codeResponse.setMessage(response.getMessage());
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Auto Search Issue Number not Fetched or server side error.");
		}

		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	/**
	 * @author mahesh.kumar
	 * @param receiptNo
	 * @param branchId
	 * @return ResponseEntity
	 */
	@GetMapping("/autoSearchReceiptNo")
	public ResponseEntity<?> autoSearchReceiptNo(@RequestParam String receiptNo,
			OAuth2Authentication authentication) 
	{
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();

		ApiResponse<?> response = binTransferService.autoSearchReceiptNo(receiptNo);

		if (response != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Auto Search Receipt Number on " + formatter.format(new Date()));
			codeResponse.setMessage(response.getMessage());
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Auto Search Receipt Number not Fetched or server side error.");
		}

		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@PostMapping(value = "/searchBinToBinTransfer")
	public ResponseEntity<?> searchBinToBinTransfer(@RequestBody SearchBinToBinTransferRequestDto requestModel,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		
		ApiResponse<?> response = binTransferService.searchBinToBinTransfer(userCode, requestModel);
		if (response != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Bin To Bin Transfer Search List on " + formatter.format(new Date()));
			codeResponse.setMessage("Bin To Bin Transfer Search Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Bin To Bin Transfer Search Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@GetMapping("/viewBinToBinTransfer")
	public ResponseEntity<?> viewBinToBinTransfer(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestParam("issueId") BigInteger issueId,
			OAuth2Authentication authentication) 
	{
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		
		ApiResponse<?> response = binTransferService.viewBinToBinTransfer(issueId);
		
		if (response != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Bin To Bin Transfer View List on: " + formatter.format(new Date()));
			codeResponse.setMessage("Bin To Bin Transfer View List Successfully Fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on: " + formatter.format(new Date()));
			codeResponse.setMessage("Bin To Bin Transfer View List Not Fetched or Server Side Error.");
		}
		
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@PostMapping("/exportBinToBinSearchDetail")
	public void exportBinToBinSearchDetail(@RequestBody SearchBinToBinTransferRequestDto requestModel,
			@RequestParam(defaultValue = "xls") String format, @RequestParam(required = false) String printStatus,
			OAuth2Authentication authentication, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName = "BinToBin-Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("issueNo", requestModel.getIssueNo());
		jasperParameter.put("userCode", userCode);
		jasperParameter.put("receiptNo", requestModel.getReceiptNo());
		jasperParameter.put("transferDoneBy", requestModel.getTransferDoneBy());
		jasperParameter.put("fromDate", requestModel.getFromDate());
		jasperParameter.put("toDate", requestModel.getToDate());
		jasperParameter.put("page", requestModel.getPage());
		jasperParameter.put("size", requestModel.getSize());

		String filePath = "";
		String property = System.getProperty("os.name");
		if (property.contains("Windows")) {
			filePath = uploadDir;
		} else {
			filePath = "/var/VST-DMS-APPS/FILES/Template/bin-to-bin/";
		}
	

		JasperPrint jasperPrint = pdfGeneratorReport(request, 
				"BinToBin.jasper", jasperParameter, filePath);
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
	 * @author mahesh.kumar
	 * @param authentication
	 * @return ResponseEntity
	 */
	@GetMapping("/getStockStores")
	public ResponseEntity<?> getStockStores(@RequestParam("branchId") BigInteger branchId, 
			@RequestParam("partBranchId") BigInteger partBranchId,@RequestParam("storeId") BigInteger storeId) {
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();

		ApiResponse<?> response = binTransferService.getStockStores(branchId, partBranchId, storeId);

		if (response != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("getStockStores list " + formatter.format(new Date()));
			codeResponse.setMessage(response.getMessage());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Stock Store list Not Fetched or server side error.");
		}

		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}

}
