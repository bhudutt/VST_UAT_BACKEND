package com.hitech.dms.web.Controller.spare.grn;

import java.io.IOException;
import java.io.OutputStream;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.app.config.ConnectionConfiguration;
import com.hitech.dms.app.utils.ApiResponse;
import com.hitech.dms.web.dao.spare.grn.mapping.SpareGRNDao;
import com.hitech.dms.web.model.spare.grn.mapping.request.SpareGrnSearchRequest;
import com.hitech.dms.web.model.spare.grn.mapping.response.SpareGrnDetailsResponse;
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
@RequestMapping("/api/v1/spare/grn/search")
public class SearchController {

	@Value("${file.upload-dir.Grn}")
    private String spareGrnSearchdownloadPath;
	
	@Autowired
	private ConnectionConfiguration dataSourceConnection;
	
	@Autowired
	SpareGRNDao spareGRNDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	@GetMapping("/searchGrnOrInvoiceNumber")
	public ResponseEntity<?> searchGrnOrInvoiceNumber(@RequestParam() String searchType,
			@RequestParam() String searchText, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		System.out.println("at controller aaa "+searchText +" "+searchType);
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		String lookupTypeCode = "GRN";
		List<String> grnNumberList = spareGRNDao.searchGrnOrInvoiceNumber(searchType, searchText, userCode);
		if (grnNumberList != null && !grnNumberList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Grn Number List on " + formatter.format(new Date()));
			codeResponse.setMessage("Grn Number List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Grn Number List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(grnNumberList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/searchClaimOrInvoiceNumber")
	public ResponseEntity<?> searchClaimOrInvoiceNumber(@RequestParam() String searchType,
			@RequestParam() String searchText, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		//searchText="D";
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		String lookupTypeCode = "CLAIM ";
		List<String> grnNumberList = spareGRNDao.searchClaimOrInvoiceNumber(searchType, searchText, userCode);
		System.out.println("at controller grnNlist  "+grnNumberList);
		if (grnNumberList != null && !grnNumberList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Claim Number List on " + formatter.format(new Date()));
			codeResponse.setMessage("Claim Number List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Grn Number List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(grnNumberList);
		return ResponseEntity.ok(userAuthResponse);
	}
	

	@GetMapping("/searchInvoiceNumberByGrn")
	public ResponseEntity<?> searchInvoiceNumberByGrn(@RequestParam() String grnNumber,
			@RequestParam() String searchText, OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		String lookupTypeCode = "GRN";
		List<String> invoiceNumberList = spareGRNDao.searchInvoiceNumberByGrn(grnNumber, searchText);
		if (grnNumber != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Grn Number List on " + formatter.format(new Date()));
			codeResponse.setMessage("Grn Number List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Grn Number List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(invoiceNumberList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/fetchGrnDetails")
	public ResponseEntity<?> fetchGrnDetails(@RequestParam() String grnNumber,
			@RequestParam() String invoiceNo, @RequestParam() String poNumber, @RequestParam() Date fromDate, 
			@RequestParam() Date toDate, @RequestParam() Integer page, 
			@RequestParam() Integer size, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) throws ParseException {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		String lookupTypeCode = "GRN";
		ApiResponse<?> response = spareGRNDao
				.fetchGrnDetailsList(grnNumber, invoiceNo, poNumber, fromDate, toDate, userCode, page, size);
		if (response != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Search List on " + formatter.format(new Date()));
			codeResponse.setMessage("Search List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Search List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/exportSpareGrnSearchDetail")
	public void exportSpareGrnSearchDetail(@RequestBody SpareGrnSearchRequest spareGrnSearchRequest,
			@RequestParam(defaultValue = "xls") String format, @RequestParam(required = false) String printStatus,
			OAuth2Authentication authentication, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName = "Spare GRN Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("grnNumber", spareGrnSearchRequest.getGrnNumber());
		jasperParameter.put("invoiceNo", spareGrnSearchRequest.getInvoiceNo());
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
			filePath = "/var/VST-DMS-APPS/FILES/REPORTS/spare/Excel/Grn/";
		}
	

		JasperPrint jasperPrint = pdfGeneratorReport(request, 
				"SpareGrnDetail.jasper", jasperParameter, filePath);
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
