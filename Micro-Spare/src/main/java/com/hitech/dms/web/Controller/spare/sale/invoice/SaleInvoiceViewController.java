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
@RequestMapping("api/v1/spareSaleInvoice/view")
public class SaleInvoiceViewController {
	
	@Value("${file.upload-dir.SaleInvoicePrint}")
    private String downloadPath;
	
	@Autowired
	private ConnectionConfiguration dataSourceConnection;
	
	@Autowired
	SpareSaleInvoiceService spareSaleInvoiceService;
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	@GetMapping("/exportInvoicePrint")
	public void exportSpareSaleInvoicePrint(@RequestParam Integer invoiceSaleId,
			@RequestParam(defaultValue = "pdf") String format, @RequestParam(required = false) String printStatus,
			OAuth2Authentication authentication, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		
		String reportName = "Spare Invoice Print";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("invoiceSaleId", invoiceSaleId);
//		jasperParameter.put("flag", "");
		
		String filePath = "";
		String property = System.getProperty("os.name");
		if (property.contains("Windows")) {
			filePath = downloadPath;
		} else {
			filePath = "/var/VST-DMS-APPS/FILES/REPORTS/spare/Print/SaleInvoice/";
		}
	

		JasperPrint jasperPrint = pdfGeneratorReport(request, 
				"SalesInvoice.jasper", jasperParameter, filePath);
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
				System.out.println("connection  "+filePath);

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
			
			System.out.println("configuration  "+ configuration);

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
