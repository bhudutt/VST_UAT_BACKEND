package com.hitech.dms.web.Controller.spare.sale.invoice;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
import com.hitech.dms.web.model.spare.sale.invoice.request.SaleInvoiceCancelRequest;
import com.hitech.dms.web.model.spare.sale.invoice.request.SalesInvoiceReportRequest;
import com.hitech.dms.web.model.spare.sale.invoice.response.SpareSalesInvoiceResponse;
import com.hitech.dms.web.service.sale.invoice.SpareSaleInvoiceService;
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
@RequestMapping("api/v1/spareSaleInvoice/search")
public class SaleInvoiceSearchController {
	
	@Value("${file.upload-dir.SaleInvoice}")
    private String downloadPath;
	
	@Autowired
	private ConnectionConfiguration dataSourceConnection;
	
	@Autowired
	SpareSaleInvoiceService spareSaleInvoiceService;
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	@GetMapping("/searchInvoiceNumber")
	public ResponseEntity<?> searchInvoiceList(@RequestParam() String searchText,
			OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		HashMap<BigInteger, String> list = spareSaleInvoiceService
				.searchInvoiceList(searchText, userCode);
		if (list != null && !list.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Invoice Number List on " + formatter.format(new Date()));
			codeResponse.setMessage("Invoice Number List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Invoice Number List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(list);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/fetchInvoiceList")
	public ResponseEntity<?> fetchInvoiceList(@RequestParam() String invoiceNumber,
			@RequestParam() Date fromDate, @RequestParam() Date toDate,
			@RequestParam() Integer page, @RequestParam() Integer size,
			OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<SpareSalesInvoiceResponse> list = spareSaleInvoiceService
				.fetchInvoiceList(invoiceNumber, fromDate, toDate, userCode,
						page, size);
		if (list != null && !list.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Invoice Number List on " + formatter.format(new Date()));
			codeResponse.setMessage("Invoice Number List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Invoice Number List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(list);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/hdrAndDtl")
	public ResponseEntity<?> fetchHdrAndDtl(@RequestParam() BigInteger invoiceId,
			OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		SpareSalesInvoiceResponse salesInvoiceResponse = spareSaleInvoiceService.fetchHdrAndDtl(invoiceId, userCode);
		if (salesInvoiceResponse != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Sale Invoice on " + formatter.format(new Date()));
			codeResponse.setMessage("Sale Invoice List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Sale invoice Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(salesInvoiceResponse);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@PostMapping("/cancel")
	public ResponseEntity<?> cancelInvoice(@RequestBody() SaleInvoiceCancelRequest saleInvoiceCancelRequest,
			OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		SpareSalesInvoiceResponse spareSalesInvoiceResponse = spareSaleInvoiceService
				.cancelInvoice(saleInvoiceCancelRequest, userCode);
		if (spareSalesInvoiceResponse != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Invoice Number Cancelled on " + formatter.format(new Date()));
			codeResponse.setMessage("Invoice Number Cancelled Successfully");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Invoice Number Cancelled or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(spareSalesInvoiceResponse);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@PostMapping("/exportSpareSaleInvoiceDetail")
	public void exportSpareSaleInvoiceDetail(@RequestBody SalesInvoiceReportRequest salesInvoiceReportRequest,
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
		jasperParameter.put("invoiceNumber", salesInvoiceReportRequest.getInvoiceNumber());
		jasperParameter.put("FromDate", salesInvoiceReportRequest.getFromDate());
		jasperParameter.put("ToDate", salesInvoiceReportRequest.getToDate());
		jasperParameter.put("UserCode", userCode);
		jasperParameter.put("page", salesInvoiceReportRequest.getPage());
		jasperParameter.put("size", salesInvoiceReportRequest.getSize());
		jasperParameter.put("pcId", salesInvoiceReportRequest.getPcId());
		jasperParameter.put("hoId", salesInvoiceReportRequest.getHoId());
		jasperParameter.put("zoneId", salesInvoiceReportRequest.getZoneId());
		jasperParameter.put("stateId", salesInvoiceReportRequest.getStateId());
		jasperParameter.put("territoryId", salesInvoiceReportRequest.getTerritoryId());

		String filePath = "";
		String property = System.getProperty("os.name");
		if (property.contains("Windows")) {
			filePath = downloadPath;
		} else {
			filePath = "/var/VST-DMS-APPS/FILES/REPORTS/spare/Excel/SaleInvoice/";
		}
	

		JasperPrint jasperPrint = pdfGeneratorReport(request, 
				"SPARESALESINVOICE.jasper", jasperParameter, filePath);
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
