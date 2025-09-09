package com.hitech.dms.web.Controller.spare.claim.inventory;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.hitech.dms.app.config.ConnectionConfiguration;
import com.hitech.dms.app.utils.ApiResponse;
import com.hitech.dms.web.dao.spare.claim.SpareClaimDao;
import com.hitech.dms.web.dao.spare.inventorytransfer.InventoryTransferForClaimDao;
import com.hitech.dms.web.model.common.GeneratedNumberModel;
import com.hitech.dms.web.model.spare.inventory.response.SpareGrnInventoryResponse;
import com.lowagie.text.pdf.PdfWriter;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

import com.hitech.dms.web.model.spare.grn.mapping.request.SpareGrnRequest;
import com.hitech.dms.web.model.spare.grn.mapping.request.SpareGrnSearchRequest;
import com.hitech.dms.web.model.spare.grn.mapping.response.PartNumberDetailResponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.SpareGrnDetailsResponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.SpareGrnResponse;
import com.hitech.dms.web.model.spare.inventory.request.SpareInventoryRequest;
import com.hitech.dms.web.model.spare.inventory.request.SpareInventorySearchRequest;

@RestController
@RequestMapping("/api/v1/inventoryTransfer/search")
public class InventoryTransferSearchController {

	@Value("${file.upload-dir.InventoryTransfer}")
    private String spareGrnSearchdownloadPath;
	
	@Autowired
	private ConnectionConfiguration dataSourceConnection;
	
	@Autowired
	private InventoryTransferForClaimDao inventoryTransferForClaimDao;
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	@GetMapping("/fetchGrnDetails")
	public ResponseEntity<?> fetchGrnDetails(@RequestParam() int grnHdrId,
			OAuth2Authentication authentication,
			Device device, HttpServletRequest request) throws ParseException {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		SpareGrnInventoryResponse spareGrnInventoryResponse = 
				inventoryTransferForClaimDao.fetchGrnDetails(grnHdrId, "search");
		if (spareGrnInventoryResponse != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("GRN List on " + formatter.format(new Date()));
			codeResponse.setMessage("GRN List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("GRN List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(spareGrnInventoryResponse);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/fetchGrnPartDetails")
	public ResponseEntity<?> fetchGrnPartDetails(@RequestParam() int grnHdrId, 
			@RequestParam(required = false) String claimType,
			OAuth2Authentication authentication,
			Device device, HttpServletRequest request) throws ParseException {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		String lookupTypeCode = "GRN";
		List<PartNumberDetailResponse> partNumberDetailResponseList = 
				inventoryTransferForClaimDao.fetchGrnPartDetails(grnHdrId, null, claimType);
		if (partNumberDetailResponseList != null && !partNumberDetailResponseList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("GRN List on " + formatter.format(new Date()));
			codeResponse.setMessage("GRN List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("No Part Details found for the given GRN Number");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(partNumberDetailResponseList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@PostMapping("/fetchGrnList")
	public ResponseEntity<?> fetchGrnList(@RequestBody() SpareInventorySearchRequest spareSearchInventoryRequest, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) throws ParseException {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		String lookupTypeCode = "GRN";
		ApiResponse<?> response = inventoryTransferForClaimDao
				.fetchGrnList(spareSearchInventoryRequest, userCode);
		if (response != null) {
			codeResponse.setCode(String.valueOf(response.getStatus()));
			codeResponse.setDescription("Search List on " + formatter.format(new Date()));
			codeResponse.setMessage(response.getMessage());
		} else {
			codeResponse.setCode(String.valueOf(response.getStatus()));
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage(response.getMessage());
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}
	
//	@GetMapping("/generateInventoryNumber")
//	public ResponseEntity<?> generateInventoryNumber(@RequestParam() Integer branchId,
//			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
//			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
//		String userCode = null;
//		if (authentication != null) {
//			userCode = authentication.getUserAuthentication().getName();
//		}
//
//		HeaderResponse userAuthResponse = new HeaderResponse();
//		MessageCodeResponse codeResponse = new MessageCodeResponse();
//		SimpleDateFormat formatter = getSimpleDateFormat();
//		GeneratedNumberModel generatedNumberModel = inventoryTransferForClaimDao.generateInventoryNumber(branchId);
//		if (generatedNumberModel != null) {
//			codeResponse.setCode("EC200");
//			codeResponse.setDescription("Inventory number generated on " + formatter.format(new Date()));
//			codeResponse.setMessage(generatedNumberModel.getMsg());
//		} else if (generatedNumberModel.getStatusCode() == 500) {
//			codeResponse.setCode("EC500");
//			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
//			codeResponse.setMessage("Inventory number generated or server side error.");
//		}
//		userAuthResponse.setResponseCode(codeResponse);
//		userAuthResponse.setResponseData(generatedNumberModel);
//		return ResponseEntity.ok(userAuthResponse);
//	}
	
	@PostMapping("/exportInventoryTransferDetail")
	public void exportSpareGrnSearchDetail(@RequestBody SpareGrnSearchRequest spareGrnSearchRequest,
			@RequestParam(defaultValue = "xls") String format, @RequestParam(required = false) String printStatus,
			OAuth2Authentication authentication, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName = "Inventory Transfer For Claim Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("grnHdrId", null);
		jasperParameter.put("grnNumber", spareGrnSearchRequest.getGrnNumber());
		jasperParameter.put("inventoryNumber", spareGrnSearchRequest.getInventoryNumber());
		jasperParameter.put("FromDate", spareGrnSearchRequest.getFromDate());
		jasperParameter.put("ToDate", spareGrnSearchRequest.getToDate());
		jasperParameter.put("UserCode", userCode);
		jasperParameter.put("page", spareGrnSearchRequest.getPage());
		jasperParameter.put("size", spareGrnSearchRequest.getSize());
		jasperParameter.put("pcId", spareGrnSearchRequest.getPcId());
		jasperParameter.put("hoId", spareGrnSearchRequest.getHoId());
		jasperParameter.put("zoneId", spareGrnSearchRequest.getZoneId());
		jasperParameter.put("stateId", spareGrnSearchRequest.getStateId());
		jasperParameter.put("territoryId", spareGrnSearchRequest.getTerritoryId());
		
		String filePath = "";
		String property = System.getProperty("os.name");
		if (property.contains("Windows")) {
			filePath = spareGrnSearchdownloadPath;
		} else {
			filePath = "/var/VST-DMS-APPS/FILES/REPORTS/spare/Excel/InventoryTransfer/";
		}
	

		JasperPrint jasperPrint = pdfGeneratorReport(request, 
				"Inventory_Transfer_For_Claim.jasper", jasperParameter, filePath);
		System.out.println(jasperParameter);
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
			
			
			
			/*
			 * JRXlsExporter xlsExporter = new JRXlsExporter();
			 * jasperPrint.setProperty("net.sf.jasperreports.export.xls.ignore.graphics",
			 * "false"); //
			 * jasperPrint.setProperty("net.sf.jasperreports.export.xls.sheet.name", //
			 * "true");
			 * 
			 * File file = new File(reportName + ".xls");
			 * 
			 * xlsExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			 * 
			 * SimpleXlsReportConfiguration configuration = new
			 * SimpleXlsReportConfiguration(); configuration.setOnePagePerSheet(true);
			 * configuration.setDetectCellType(true);
			 * configuration.setCollapseRowSpan(false);
			 * configuration.setWhitePageBackground(false);
			 * configuration.setRemoveEmptySpaceBetweenColumns(true);
			 * configuration.setRemoveEmptySpaceBetweenRows(true);
			 * configuration.setIgnorePageMargins(true);
			 * 
			 * String sheetName[] = { reportName }; configuration.setSheetNames(sheetName);
			 * 
			 * xlsExporter.setConfiguration(configuration);
			 * xlsExporter.setExporterOutput(new
			 * SimpleOutputStreamExporterOutput(outputStream)); xlsExporter.exportReport();
			 */
		}
	}
}
