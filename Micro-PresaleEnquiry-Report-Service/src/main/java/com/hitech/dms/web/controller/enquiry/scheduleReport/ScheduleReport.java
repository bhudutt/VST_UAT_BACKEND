package com.hitech.dms.web.controller.enquiry.scheduleReport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.hitech.dms.app.config.persistence.ConnectionConfiguration;
import com.hitech.dms.web.service.sftp.FtpIntegrationService;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

@Component
public class ScheduleReport {
	private static final Logger logger = LoggerFactory.getLogger(ScheduleReport.class);

	@Value("${file.upload-dir.TocReport}")
	private String downloadPath;

	@Autowired
	private ConnectionConfiguration dataSourceConnection;

	@Value("${cron.expression.schedule}")
	private String cronExpression;

	@Autowired
	FtpIntegrationService ftpIntegrationService;

//	@Scheduled(fixedRate = 10000)
	@Scheduled(cron = "${cron.expression.schedule}")
	public void generateTOCStockCsvReport() throws IOException {
		Date date = new Date();
		String prodFilePath = "/var/VST-DMS-APPS/FILES/REPORTS/TOC/";

		String timeStamp = getTimeStamp(date);
//		String timestamp = new java.text.SimpleDateFormat("yyyy_MM_dd_hhmmss").format(new Date());
		String fileName = "TOC_STOCK_REPORT_" + timeStamp + ".csv";

		String filePath = "";
		String property = System.getProperty("os.name");
		if (property.contains("Windows")) {
			filePath = downloadPath;
		} else {
			filePath = prodFilePath;
		}

		String fileCreationPath = filePath + fileName;
		logger.info(fileCreationPath);
		File file = new File(fileCreationPath);

		// Create the file if it doesn't exist
		if (file.createNewFile()) {
			logger.info("File created: " + file.getAbsolutePath());
		} else {
			logger.info("File already exists.");
		}

		OutputStream outputStream = new FileOutputStream(filePath + fileName);
		String format = "csv";
		String reportName = "Report";
		String printStatus = "";

		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		String jasperFileName = "TOC_STOCK_REPORT.jasper";
		String sheetName = "TOC_Stock_Dealer_" + timeStamp;
		
		JasperPrint jasperPrint = pdfGeneratorReport(jasperFileName, jasperParameter, filePath);
        
		try {
			printReport(jasperPrint, sheetName, format, printStatus, outputStream, reportName, fileName);

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

	private String getTimeStamp(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_hhmmss");
		sdf.setTimeZone(TimeZone.getTimeZone("IST"));
		logger.info(sdf.format(date));
		
		String timeStamp = sdf.format(date);
		return timeStamp;
	}

	public JasperPrint pdfGeneratorReport(String jaspername, HashMap<String, Object> jasperParameter,
			String downloadPath) {
		JasperPrint jasperPrint = null;
		Connection connection = null;
		try {
			String filePath = downloadPath + jaspername;
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

	public void printReport(JasperPrint jasperPrint, String sheetName, String format, String printStatus,
			OutputStream outputStream, String reportName, String fileName) throws Exception {

		JRXlsxExporter exporter = new JRXlsxExporter();
		SimpleXlsxReportConfiguration reportConfigXLS = new SimpleXlsxReportConfiguration();
		reportConfigXLS.setSheetNames(new String[] { sheetName });
		exporter.setConfiguration(reportConfigXLS);
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));

		exporter.exportReport();
		
		   boolean hasData = hasData(jasperPrint);

	        if (hasData) {
	    		boolean ftpUpload = ftpIntegrationService.connectWithFtp(fileName);
	            logger.info("The JasperPrint has data.");
	        } else {
	            logger.info("The JasperPrint has no data.");
	        }
	        
	}

	@Scheduled(cron = "${cron.expression.schedule}")
	public void generateTOCGitCsvReport() throws IOException {
		Date date = new Date();
		String prodFilePath = "/var/VST-DMS-APPS/FILES/REPORTS/TOC/";

		String timeStamp = getTimeStamp(date);
//		String timestamp = new SimpleDateFormat("yyyy_MM_dd_hhmmss").format(new Date());
		String fileName = "TOC_GIT_REPORT_" + timeStamp + ".csv";

		String filePath = "";
		String property = System.getProperty("os.name");
		if (property.contains("Windows")) {
			filePath = downloadPath;

		} else {
			filePath = prodFilePath;
		}

		String fileCreationPath = filePath + fileName;
		logger.info(fileCreationPath);
		File file = new File(fileCreationPath);

		// Create the file if it doesn't exist
		if (file.createNewFile()) {
			logger.info("File created: " + file.getAbsolutePath());
		} else {
			logger.info("File already exists.");
		}

		OutputStream outputStream = new FileOutputStream(filePath + fileName);
		String format = "csv";
		String reportName = "Report";
		String printStatus = "";

		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();

		String jasperFileName = "TOC_GIT_REPORT.jasper";
		String sheetName = "TOC_GIT_Dealer_" + timeStamp;

		JasperPrint jasperPrint = pdfGeneratorReport(jasperFileName, jasperParameter, filePath);

		try {
			printReport(jasperPrint, sheetName, format, printStatus, outputStream, reportName, fileName);

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
	
	
	@Scheduled(cron = "${cron.expression.schedule}")
	public void generateTOCConsumptionDistributorCsvReport() throws IOException {
		Date date = new Date();
		String prodFilePath = "/var/VST-DMS-APPS/FILES/REPORTS/TOC/";

		String timeStamp = getTimeStamp(date);
//		String timestamp = new SimpleDateFormat("yyyy_MM_dd_hhmmss").format(new Date());
		String fileName = "TOC_CONSUMPTION_DISTRIBUTOR_REPORT_" + timeStamp + ".csv";

		String filePath = "";
		String property = System.getProperty("os.name");
		if (property.contains("Windows")) {
			filePath = downloadPath;

		} else {
			filePath = prodFilePath;
		}

		String fileCreationPath = filePath + fileName;
		logger.info(fileCreationPath);
		File file = new File(fileCreationPath);

		// Create the file if it doesn't exist
		if (file.createNewFile()) {
			logger.info("File created: " + file.getAbsolutePath());
		} else {
			logger.info("File already exists.");
		}

		OutputStream outputStream = new FileOutputStream(filePath + fileName);
		String format = "csv";
		String reportName = "Report";
		String printStatus = "";

		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();

		String jasperFileName = "TOC_Consumption_Distributor_Report.jasper";
		String sheetName = "TOC_CONSUMPTION_DISTRIBUTORr_" + timeStamp;

		JasperPrint jasperPrint = pdfGeneratorReport(jasperFileName, jasperParameter, filePath);

		try {
			printReport(jasperPrint, sheetName, format, printStatus, outputStream, reportName, fileName);

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
	
	@Scheduled(cron = "${cron.expression.schedule}")
	public void generateTOCReceiptDistributorCsvReport() throws IOException {
		Date date = new Date();
		String prodFilePath = "/var/VST-DMS-APPS/FILES/REPORTS/TOC/";

		String timeStamp = getTimeStamp(date);
//		String timestamp = new SimpleDateFormat("yyyy_MM_dd_hhmmss").format(new Date());
		String fileName = "TOC_RECEIPT_DISTRIBUTOR_REPORT_" + timeStamp + ".csv";

		String filePath = "";
		String property = System.getProperty("os.name");
		if (property.contains("Windows")) {
			filePath = downloadPath;

		} else {
			filePath = prodFilePath;
		}

		String fileCreationPath = filePath + fileName;
		logger.info(fileCreationPath);
		File file = new File(fileCreationPath);

		// Create the file if it doesn't exist
		if (file.createNewFile()) {
			logger.info("File created: " + file.getAbsolutePath());
		} else {
			logger.info("File already exists.");
		}

		OutputStream outputStream = new FileOutputStream(filePath + fileName);
		String format = "csv";
		String reportName = "Report";
		String printStatus = "";

		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();

		String jasperFileName = "TOC_Receipt_Distributor_Report.jasper";
		String sheetName = "TOC_RECEIPT_DISTRIBUTOR_" + timeStamp;

		JasperPrint jasperPrint = pdfGeneratorReport(jasperFileName, jasperParameter, filePath);

		try {
			printReport(jasperPrint, sheetName, format, printStatus, outputStream, reportName, fileName);

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
	
	@Scheduled(cron = "${cron.expression.schedule}")
	public void generateTOCGitDistributorCsvReport() throws IOException {
		Date date = new Date();
		String prodFilePath = "/var/VST-DMS-APPS/FILES/REPORTS/TOC/";

		String timeStamp = getTimeStamp(date);
//		String timestamp = new SimpleDateFormat("yyyy_MM_dd_hhmmss").format(new Date());
		String fileName = "TOC_GIT_DISTRIBUTOR_REPORT_" + timeStamp + ".csv";

		String filePath = "";
		String property = System.getProperty("os.name");
		if (property.contains("Windows")) {
			filePath = downloadPath;

		} else {
			filePath = prodFilePath;
		}

		String fileCreationPath = filePath + fileName;
		logger.info(fileCreationPath);
		File file = new File(fileCreationPath);

		// Create the file if it doesn't exist
		if (file.createNewFile()) {
			logger.info("File created: " + file.getAbsolutePath());
		} else {
			logger.info("File already exists.");
		}

		OutputStream outputStream = new FileOutputStream(filePath + fileName);
		String format = "csv";
		String reportName = "Report";
		String printStatus = "";

		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();

		String jasperFileName = "TOC_GIT_Distributore_Report.jasper";
		String sheetName = "TOC_GIT_DISTRIBUTOR_" + timeStamp;

		JasperPrint jasperPrint = pdfGeneratorReport(jasperFileName, jasperParameter, filePath);

		try {
			printReport(jasperPrint, sheetName, format, printStatus, outputStream, reportName, fileName);

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
	
	
	@Scheduled(cron = "${cron.expression.schedule}")
	public void generateTOCStockDistributorCsvReport() throws IOException {
		Date date = new Date();
		String prodFilePath = "/var/VST-DMS-APPS/FILES/REPORTS/TOC/";

		String timeStamp = getTimeStamp(date);
//		String timestamp = new SimpleDateFormat("yyyy_MM_dd_hhmmss").format(new Date());
		String fileName = "TOC_STOCK_DISTRIBUTOR_REPORT_" + timeStamp + ".csv";

		String filePath = "";
		String property = System.getProperty("os.name");
		if (property.contains("Windows")) {
			filePath = downloadPath;

		} else {
			filePath = prodFilePath;
		}

		String fileCreationPath = filePath + fileName;
		logger.info(fileCreationPath);
		File file = new File(fileCreationPath);

		// Create the file if it doesn't exist
		if (file.createNewFile()) {
			logger.info("File created: " + file.getAbsolutePath());
		} else {
			logger.info("File already exists.");
		}

		OutputStream outputStream = new FileOutputStream(filePath + fileName);
		String format = "csv";
		String reportName = "Report";
		String printStatus = "";

		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();

		String jasperFileName = "TOC_Stock_Distributore_Report.jasper";
		String sheetName = "TOC_STOCK_DISTRIBUTOR_" + timeStamp;

		JasperPrint jasperPrint = pdfGeneratorReport(jasperFileName, jasperParameter, filePath);

		try {
			printReport(jasperPrint, sheetName, format, printStatus, outputStream, reportName, fileName);

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
	

	
	
	 public static boolean hasData(JasperPrint jasperPrint) {
	        // Check if there are any pages in the JasperPrint
	        if (jasperPrint.getPages() == null || jasperPrint.getPages().isEmpty()) {
	            return false;
	        }

	        // Check if each page has at least one column
	        for (int i = 0; i < jasperPrint.getPages().size(); i++) {
	            if (jasperPrint.getPages().get(i).getElements() == null ||
	                    jasperPrint.getPages().get(i).getElements().isEmpty()) {
	                return false;
	            }
	        }

	        return true;
	    }

}
