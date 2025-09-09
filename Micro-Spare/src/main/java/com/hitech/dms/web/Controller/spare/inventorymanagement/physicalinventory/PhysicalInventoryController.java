package com.hitech.dms.web.Controller.spare.inventorymanagement.physicalinventory;

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
import com.hitech.dms.app.utils.ApiResponse;
import com.hitech.dms.web.entity.spare.inventorymanagement.physicalinventory.PhysicalInventoryHdr;
import com.hitech.dms.web.model.spare.inventorymanagement.physicalinventory.SearchPhysicalInventoryRequestDto;
import com.hitech.dms.web.model.spare.inventorymanagement.physicalinventory.StockAdjForPhyInvDto;
import com.hitech.dms.web.model.spare.inventorymanagement.physicalinventory.exportPhyInvDto;
import com.hitech.dms.web.service.spare.inventorymanagement.physicalinventory.PhysicalInventoryService;
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
 * @author Mahesh.Kumar
 */
@RestController
@RequestMapping("/inventoryManagement/physicalInventory")
@SecurityRequirement(name = "hitechApis")
public class PhysicalInventoryController {
	
	@Autowired
	private PhysicalInventoryService PhysicalInvService;
	
	@Autowired
	private ConnectionConfiguration dataSourceConnection;
	
	@Value("${file.upload-dir.Physical-Inventory:C:\\\\VST-DMS-APPS\\FILES\\Template\\\\physical-inventory\\\\\\\\}")
    private String uploadDir;
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	/**
	 * @author mahesh.kumar
	 * @param authentication
	 * @return ResponseEntity
	 */
	@GetMapping("/getProductCatgry")
	public ResponseEntity<?> getProductCatgry(OAuth2Authentication authentication) {
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();

		ApiResponse<?> response = PhysicalInvService.getProductCatgry();

		if (response != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Product Category list " + formatter.format(new Date()));
			codeResponse.setMessage(response.getMessage());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Product Category list Not Fetched or server side error.");
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
	@GetMapping("/getPartsDetail")
	public ResponseEntity<?> getPartsDetail(@RequestParam("branchId") BigInteger branchId,
			@RequestParam("prodCatId") BigInteger prodCatId,@RequestParam("isZeroQty") Boolean isZeroQty, OAuth2Authentication authentication) {
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();

		ApiResponse<?> response = PhysicalInvService.getPartsDetail(branchId, prodCatId, isZeroQty);

		if (response != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Part Details list " + formatter.format(new Date()));
			codeResponse.setMessage(response.getMessage());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Part Details list Not Fetched or server side error.");
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
	@GetMapping("/getToStores")
	public ResponseEntity<?> getToStores(@RequestParam("branchId") BigInteger branchId) {
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();

		ApiResponse<?> response = PhysicalInvService.getToStores(branchId);

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
	@GetMapping("/getToBinLocation")
	public ResponseEntity<?> getToBinLocation(@RequestParam("partBranchId") BigInteger partBranchId, 
			@RequestParam("storeId") BigInteger storeId, @RequestParam("binLocation") String binLocation) {
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();

		ApiResponse<?> response = PhysicalInvService.getToBinLocation(partBranchId, storeId, binLocation);

		if (response != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("BinLocation list " + formatter.format(new Date()));
			codeResponse.setMessage(response.getMessage());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("BinLocation list Not Fetched or server side error.");
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
	@GetMapping("/getAllNdp")
	public ResponseEntity<?> getAllNdp(@RequestParam("partBranchId") BigInteger partBranchId, 
			@RequestParam("storeId") BigInteger storeId, @RequestParam("binId") BigInteger binId) {
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();

		ApiResponse<?> response = PhysicalInvService.getAllNdp(partBranchId, storeId, binId);

		if (response != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("NDP list " + formatter.format(new Date()));
			codeResponse.setMessage(response.getMessage());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("NDP list Not Fetched or server side error.");
		}

		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	/**
	 * @author mahesh.kumar
	 * @param exportPhyInv
	 * @param response
	 * @param request
	 * @return
	 */
	@PostMapping("/exportPhyInvReport")
	public void exportPhyInvReport(@RequestBody exportPhyInvDto exportPhyInv,
			@RequestParam(defaultValue = "xls") String format, @RequestParam(required = false) String printStatus,
			OAuth2Authentication authentication, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName = "Physical-Inventory-Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
//		jasperParameter.put("branchId", exportPhyInv.getBranchId());
//		jasperParameter.put("prodCatId", exportPhyInv.getProdCatId());
//		jasperParameter.put("isZeroQty", exportPhyInv.getIsZeroQty());
		
		jasperParameter.put("phyInvId", exportPhyInv.getPhyInvId());

		String filePath = "";
		String property = System.getProperty("os.name");
		if (property.contains("Windows")) {
			filePath = uploadDir;
		} else {
			filePath = "/var/VST-DMS-APPS/FILES/Template/physical-inventory/";
		}
	

		JasperPrint jasperPrint = pdfGeneratorReport(request, 
				"PhysicalInventory.jasper", jasperParameter, filePath);
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
	 * @author Mahesh.Kumar
	 * @param requestModel
	 * @param bindingResult
	 * @param authentication
	 * @param device
	 * @return ResponseEntity<?>
	 */
	@PostMapping("/savePhysicalInventory")
	public ResponseEntity<?> savePhysicalInventory(@RequestBody StockAdjForPhyInvDto requestModel,
			BindingResult bindingResult, OAuth2Authentication authentication, Device device) 
	{	
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		
		if (bindingResult.hasErrors()) {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Physical Inventory not created or server side error.");
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
		
		ApiResponse<?> response = PhysicalInvService.savePhysicalInventory(userCode, requestModel);
		
		if (response.getResult() != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Successful on " + formatter.format(new Date()));
			codeResponse.setMessage("Physical Inventory created Successfully");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Error While Creating Physical Inventory.");
		}
		
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		
		return ResponseEntity.ok(userAuthResponse);
	}
	
	/**
	 * @author mahesh.kumar
	 * @param phyInvNo
	 * @return ResponseEntity
	 */
	@GetMapping("/autoSearchPhyInvNo")
	public ResponseEntity<?> autoSearchPhyInvNo(@RequestParam String phyInvNo,
			OAuth2Authentication authentication) 
	{
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();

		ApiResponse<?> response = PhysicalInvService.autoSearchPhyInvNo(phyInvNo);

		if (response != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Auto Search Physical Inventory Number on " + formatter.format(new Date()));
			codeResponse.setMessage(response.getMessage());
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Auto Search Physical Inventory Number not Fetched or server side error.");
		}

		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@PostMapping(value = "/searchPhysicalInventory")
	public ResponseEntity<?> searchPhysicalInventory(@RequestBody SearchPhysicalInventoryRequestDto requestModel,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		
		ApiResponse<?> response = PhysicalInvService.searchPhysicalInventory(userCode, requestModel);
		if (response != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Physical Inventory Search List on " + formatter.format(new Date()));
			codeResponse.setMessage("Physical Inventory Search Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Physical Inventory Search Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@GetMapping("/viewPhysicalInventory")
	public ResponseEntity<?> viewPhysicalInventory(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestParam("phyInvId") BigInteger phyInvId,
			OAuth2Authentication authentication) 
	{
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		
		ApiResponse<?> response = PhysicalInvService.viewPhysicalInventory(phyInvId);
		
		if (response != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Physical Inventory View List on: " + formatter.format(new Date()));
			codeResponse.setMessage("Stock Physical Inventory List Successfully Fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on: " + formatter.format(new Date()));
			codeResponse.setMessage("Physical Inventory View List Not Fetched or Server Side Error.");
		}
		
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	
	/**
	 * @author mahesh.kumar
	 * @param requestModel
	 * @param format
	 * @param request
	 * @param response
	 * @return
	 */
	@PostMapping("/exportPhyInvSearchReport")
	public void exportPhyInvSearchReport(@RequestBody SearchPhysicalInventoryRequestDto requestModel,
			@RequestParam(defaultValue = "xls") String format, @RequestParam(required = false) String printStatus,
			OAuth2Authentication authentication, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName = "Physical-Inventory-Search-Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("phyInvNo", requestModel.getPhyInvNo());
		jasperParameter.put("userCode", userCode);
		jasperParameter.put("status", requestModel.getStatus());
		jasperParameter.put("phyInvDoneBy", requestModel.getPhyInvDoneBy());
		jasperParameter.put("productCategoryId", requestModel.getProductCategoryId());
		jasperParameter.put("fromDate", requestModel.getFromDate());
		jasperParameter.put("toDate", requestModel.getToDate());
		jasperParameter.put("page", requestModel.getPage());
		jasperParameter.put("size", requestModel.getSize());

		String filePath = "";
		String property = System.getProperty("os.name");
		if (property.contains("Windows")) {
			filePath = uploadDir;
		} else {
			filePath = "/var/VST-DMS-APPS/FILES/Template/physical-inventory/";
		}
	

		JasperPrint jasperPrint = pdfGeneratorReport(request, 
				"PHYSICALINVENTORYSEARCH.jasper", jasperParameter, filePath);
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
