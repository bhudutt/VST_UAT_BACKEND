/**
 * 
 */
package com.hitech.dms.web.controller.quotation.search.export;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.config.FileStorageProperties;
import com.hitech.dms.app.config.persistence.ConnectionConfiguration;
import com.lowagie.text.pdf.PdfWriter;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

/**
 * @author dinesh.jakhar
 *
 */
@Validated
@RestController
@RequestMapping("/export")
@SecurityRequirement(name = "hitechApis")
public class QuotationPrintController {
	private static final Logger logger = LoggerFactory.getLogger(QuotationPrintController.class);

	@Autowired
	private ConnectionConfiguration dataSourceConnection;
	@Autowired
	private FileStorageProperties fileStorageProperties;
	
	@Value("${file.upload-dir.Report}")
    private String downloadPath;
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@GetMapping("/exportPresalesQuotaion")
	public void generateReport(@RequestParam String quotationNumber, @RequestParam(defaultValue = "pdf") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		Decoder decoder = Base64.getDecoder();
		byte[] decodedByte = decoder.decode(quotationNumber);
		quotationNumber = new String(decodedByte);
		
		
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("QuotationNumber", quotationNumber);

		JasperPrint jasperPrint = pdfGeneratorReport(quotationNumber, request, "Quotation.jasper", jasperParameter);
		try {
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "inline; filename=" + quotationNumber + ".pdf");
			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();
			printReport(jasperPrint, format, printStatus, outputStream);

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

	public JasperPrint pdfGeneratorReport(String docNumber, HttpServletRequest request, String jaspername,
			HashMap<String, Object> jasperParameter) {
		//String filePath = request.getServletContext().getRealPath("/reports/" + jaspername);
		JasperPrint jasperPrint = null;
		Connection connection = null;
		try {
			//String filePath = ResourceUtils.getFile("classpath:reports/"+jaspername).getAbsolutePath();
			
			String filePath="";
			String property = System.getProperty("os.name");
			if(property.contains("Windows")) {
				filePath=downloadPath+jaspername;
			}else {
				filePath="/var/VST-DMS-APPS/FILES/REPORTS/"+jaspername;
			}
			
			
			System.out.println("filePath  "+filePath);
			
			//final InputStream stream = new ClassPathResource("/reports/"+jaspername).getInputStream();
			
			//String filePath = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "reports/"+jaspername).getAbsolutePath();
			
			
			
			
			/*
			 * ClassPathResource resource = new ClassPathResource("reports/"+jaspername);
			 * byte[] dataArr = FileCopyUtils.copyToByteArray(resource.getInputStream());
			 * String filePath = new String(dataArr, StandardCharsets.UTF_8);
			 */
			
			//final InputStream stream = this.getClass().getResourceAsStream("classpath:reports/"+jaspername);
			
			// Compile the Jasper report from .jrxml to .japser
	      //  final JasperReport report = JasperCompileManager.compileReport(stream);
			
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

	public void printReport(JasperPrint jasperPrint, String format, String printStatus, OutputStream outputStream)
			throws Exception {

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
	}

//	public String exportReport(String reportFormat) throws FileNotFoundException, JRException {
//		String path = "C:\\Users\\basan\\Desktop\\Report";
//		List<Employee> employees = repository.findAll();
//		// load file and compile it
//		File file = ResourceUtils.getFile("classpath:employees.jrxml");
//		JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
//		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(employees);
//		Map<String, Object> parameters = new HashMap<>();
//		parameters.put("createdBy", "Java Techie");
//		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
//		if (reportFormat.equalsIgnoreCase("html")) {
//			JasperExportManager.exportReportToHtmlFile(jasperPrint, path + "\\employees.html");
//		}
//		if (reportFormat.equalsIgnoreCase("pdf")) {
//			JasperExportManager.exportReportToPdfFile(jasperPrint, path + "\\employees.pdf");
//		}
//
//		return "report generated in path : " + path;
//	}
}
