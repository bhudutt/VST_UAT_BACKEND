package com.hitech.dms.web.Controller.spare.picklist;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.sql.Connection;
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
import com.hitech.dms.web.model.spara.customer.order.picklist.request.PicklistRequest;
import com.hitech.dms.web.model.spara.customer.order.picklist.response.SpareCustOrderForPickListResponse;
import com.hitech.dms.web.model.spara.customer.order.response.CustomerOrderNumberResponse;
import com.hitech.dms.web.model.spare.branchTransfer.indent.PartNumberDetails;
import com.hitech.dms.web.model.spare.grn.mapping.request.SpareGrnSearchRequest;
import com.hitech.dms.web.service.spare.picklist.PickListService;
import com.lowagie.text.pdf.PdfWriter;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

@RestController
@RequestMapping("api/v1/pickList")
public class CreatePickListController {

	@Value("${file.upload-dir.PickList}")
    private String downloadPath;
	
	@Autowired
	private ConnectionConfiguration dataSourceConnection;
	
	@Autowired
	PickListService pickListService;
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	@GetMapping("/searchCustomerOrderNumber")
	public ResponseEntity<?> searchCustomerOrderNumber(@RequestParam() String searchText, String searchFor,
			OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		HashMap<BigInteger, String> coNumberList = pickListService
				.searchCustomerOrderNumber(searchText, searchFor, userCode);
		if (coNumberList != null && !coNumberList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Customer Order Number List on " + formatter.format(new Date()));
			codeResponse.setMessage("Customer Order Number List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Customer Order Number List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(coNumberList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/fetchCustomerOrder")
	public ResponseEntity<?> fetchIndentTransferHdrAndDtl(@RequestParam() Integer customerOrderId, 
			OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		SpareCustOrderForPickListResponse spareCustOrderForPickListResponse = 
				pickListService.fetchCustomerOrderHdrAndDtl(customerOrderId, userCode);
		if (spareCustOrderForPickListResponse != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("PickList on " + formatter.format(new Date()));
			codeResponse.setMessage("PickList Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("PickList Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(spareCustOrderForPickListResponse);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/fetchPartNumberForPickList")
	public ResponseEntity<?> fetchPartNumberForPickList(@RequestParam(required = false) Integer partId, 
			@RequestParam() Integer branchId,
			@RequestParam(required = false) Integer poHdrId, 
			@RequestParam(required = false) Integer coId, 
			@RequestParam(required = false) Integer refDocId,
			@RequestParam() String flag,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<PartNumberDetails> partNumberDetailsList = pickListService.fetchPartNumberForPickList(partId, userCode, branchId, poHdrId,
				coId, refDocId, flag);
		if (partNumberDetailsList != null && !partNumberDetailsList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Part Number Details on " + formatter.format(new Date()));
			codeResponse.setMessage("Part Number Details Successfully fetched");
		}else if(partNumberDetailsList.isEmpty()) {
			codeResponse.setCode("EC204");
			codeResponse.setDescription("Fetch Part No List on " + formatter.format(new Date()));
			codeResponse.setMessage("Price is not available for this part, kindly contact administration");
		}else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Part Number Details Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(partNumberDetailsList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@PostMapping("/createPicklist")
	public ResponseEntity<?> createPicklist(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody PicklistRequest picklistRequest, OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}

		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		CustomerOrderNumberResponse customerOrderNumberResponse = pickListService.createPicklist(userCode, picklistRequest);
		if (customerOrderNumberResponse.getStatusCode() == 201) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Pick list Created on " + formatter.format(new Date()));
			codeResponse.setMessage(customerOrderNumberResponse.getMsg());
		} else if (customerOrderNumberResponse.getStatusCode() == 500) {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Pick list Created or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(customerOrderNumberResponse);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/exportPickListPrint")
	public void exportSpareGrnSearchDetail(@RequestParam Integer pickListHdrId,
			@RequestParam(defaultValue = "pdf") String format, @RequestParam(required = false) String printStatus,
			OAuth2Authentication authentication, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName = "Pick List";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("pickListHdrId", pickListHdrId);
//		jasperParameter.put("flag", "");
		
		String filePath = "";
		String property = System.getProperty("os.name");
		if (property.contains("Windows")) {
			filePath = downloadPath;
		} else {
			filePath = "/var/VST-DMS-APPS/FILES/REPORTS/spare/Print/Picklist/";
		}
	

		JasperPrint jasperPrint = pdfGeneratorReport(request, 
				"sparepicklist.jasper", jasperParameter, filePath);
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
