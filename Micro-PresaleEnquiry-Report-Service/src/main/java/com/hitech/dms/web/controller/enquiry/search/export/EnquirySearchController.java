/**
 * 
 */
package com.hitech.dms.web.controller.enquiry.search.export;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.app.config.FileStorageProperties;
import com.hitech.dms.app.config.persistence.ConnectionConfiguration;
import com.hitech.dms.app.utils.CommonUtils;
import com.hitech.dms.app.utils.ExcelUtils;
import com.hitech.dms.web.dao.enquiry.search.export.EnquirySearchExportDao;
import com.hitech.dms.web.model.enquiry.list.request.EnquiryListRequestModel;
import com.hitech.dms.web.model.enquiry.list.response.EnquiryListResponseModel;
import com.hitech.dms.web.model.enquiry.response.EnquiryExportResponseModel;
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
 * @author dinesh.jakhar
 *
 */
@Validated
@RestController
@RequestMapping("/export")
@SecurityRequirement(name = "hitechApis")
public class EnquirySearchController {
	private static final Logger logger = LoggerFactory.getLogger(EnquirySearchController.class);

	@Autowired
	private EnquirySearchExportDao enquirySearchExportDao;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private FileStorageProperties fileStorageProperties;

	@Value("${file.upload-dir.EnquiryReport:C:\\VST-DMS-APPS\\REPORTS\\ENQ\\\\}")
	private String enqListdownloadPath;

	@Autowired
	private ConnectionConfiguration dataSourceConnection;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@PostMapping("/exportEnquiryList")
	public ResponseEntity<?> exportEnquiryList(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody EnquiryListRequestModel enquiryListRequestModel, OAuth2Authentication authentication,
			HttpServletResponse response) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		EnquiryExportResponseModel responseModel = null;
		try {
		List<EnquiryListResponseModel> exportList = enquirySearchExportDao.exportEnquiryList(authorizationHeader,
				userCode, enquiryListRequestModel);
		if (exportList != null && !exportList.isEmpty()) {
//			HttpHeaders headers = new HttpHeaders();

//			response.setContentType("application/octet-stream");
//			SimpleDateFormat dateFormatter = getSimpleDateFormat();
			Date currDate = new Date();
//			String currentDateTime = dateFormatter.format(currDate);

			String fileName = messageSource.getMessage("app.temp.file.name",
					new Object[] { "ENQUIRYLIST" + "_" + currDate.getTime() }, LocaleContextHolder.getLocale());
			String filePath = messageSource.getMessage("label.temp.enq.path", new Object[] { userCode },
					LocaleContextHolder.getLocale());
			String limit = messageSource.getMessage("label.rep.enq.excelLimit", new Object[] { 1 },
					LocaleContextHolder.getLocale());
			if (exportList == null || exportList.isEmpty()) {
				exportList = new ArrayList<EnquiryListResponseModel>();
				EnquiryListResponseModel exportModel = new EnquiryListResponseModel();
				exportList.add(exportModel);
			}
			filePath = fileStorageProperties.getUploadDir() + filePath;
			File file = new File(filePath + fileName);
			if (file.exists()) {
				file.delete();
			}
			if (!exportList.isEmpty()) {
				Integer listSize = exportList.size();
				logger.debug(listSize + " : limit : " + limit);
				Double target = (listSize.doubleValue()) / Integer.valueOf(limit);
				target = Math.ceil(target);
				logger.debug("target : " + target);
				List<List<EnquiryListResponseModel>> parts = CommonUtils.chopIntoParts(exportList, target.intValue());
				logger.debug("parts : " + parts.size());
				for (int i = 1; i <= parts.size(); i++) {
					responseModel = ExcelUtils.createExcelInMultiSheets(filePath, fileName,
							messageSource.getMessage("label.rep.enq.list.sheet", new Object[] { i },
									LocaleContextHolder.getLocale()),
							"", messageSource.getMessage("label.rep.enq.list.header", null,
									LocaleContextHolder.getLocale()),
							parts.get(i - 1), "EXPORTREPENQLIST");
				}
			} else {
				responseModel = ExcelUtils.createExcelInMultiSheets(filePath, fileName,
						messageSource.getMessage("label.rep.enq.list.header", new Object[] { 1 },
								LocaleContextHolder.getLocale()),
						"",
						messageSource.getMessage("label.rep.enq.list.header", null, LocaleContextHolder.getLocale()),
						exportList, "EXPORTREPENQLIST");
			}
			
		
			logger.info("fileName : " + fileName);
			if (responseModel != null && responseModel.isFileCreated()) {
				responseModel.setFileName(fileName);
				codeResponse.setCode("EC200");
				codeResponse.setDescription("Successful on " + formatter.format(new Date()));
				codeResponse.setMessage(responseModel.getMsg());
			} else {
				codeResponse.setCode("EC500");
				codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
				codeResponse.setMessage("Enquiry Serach List Not Fetched or server side error.");
			}
		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@PostMapping("/exportENQList")
	public void exportSpareGrnSearchDetail(@RequestBody EnquiryListRequestModel enquiryListRequestModel,
			@RequestParam(defaultValue = "xls") String format, @RequestParam(required = false) String printStatus,
			OAuth2Authentication authentication, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName = "Enquiry List Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();

		jasperParameter.put("dealerId", enquiryListRequestModel.getDealerID());
		jasperParameter.put("branchID", enquiryListRequestModel.getBranchID());
		jasperParameter.put("UserCode", userCode);
		jasperParameter.put("PCID", enquiryListRequestModel.getPcID());
		jasperParameter.put("enquiryNo", enquiryListRequestModel.getEnqNumber());
		jasperParameter.put("enquiryFrom", enquiryListRequestModel.getEnqFrom());
		jasperParameter.put("enquiryStage", enquiryListRequestModel.getEnqStageId());
		jasperParameter.put("enquiryStatus", enquiryListRequestModel.getEnqStatus());
		jasperParameter.put("enquiryFromDate", enquiryListRequestModel.getEnqFromDate());
		jasperParameter.put("enquiryToDate", enquiryListRequestModel.getEnqToDate());
		jasperParameter.put("series", enquiryListRequestModel.getSeries());
		jasperParameter.put("segmant", enquiryListRequestModel.getSegmant());
		jasperParameter.put("variant", enquiryListRequestModel.getVariant());
		jasperParameter.put("modelID", enquiryListRequestModel.getModelID());
		jasperParameter.put("salesmanID", enquiryListRequestModel.getSalesPerson());
		jasperParameter.put("enquirySourceID", enquiryListRequestModel.getEnqSourceID());
		jasperParameter.put("prospectType", enquiryListRequestModel.getProspectType());
		jasperParameter.put("orgHierID", enquiryListRequestModel.getOrgHierID());
		jasperParameter.put("includeInactive", enquiryListRequestModel.getIncludeInActive());
		jasperParameter.put("page", enquiryListRequestModel.getPage());
		jasperParameter.put("size", enquiryListRequestModel.getSize());
		jasperParameter.put("enqFlpFromDate", enquiryListRequestModel.getEnqFlpFromDate());
		jasperParameter.put("enqFlpToDate", enquiryListRequestModel.getEnqFlpToDate());

		String filePath = "";
		String property = System.getProperty("os.name");
		if (property.contains("Windows")) {
			filePath = enqListdownloadPath;
		} else {
			filePath = "/var/VST-DMS-APPS/FILES/REPORTS/Enquiry List";
		}

		JasperPrint jasperPrint = pdfGeneratorReport(request, "Enquiry List.jasper", jasperParameter, filePath);
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
			HashMap<String, Object> jasperParameter, String downloadPath) {
		// String filePath = request.getServletContext().getRealPath("/reports/" +
		// jaspername);
		JasperPrint jasperPrint = null;
		Connection connection = null;
		try {
			// String filePath =
			// ResourceUtils.getFile("classpath:reports/"+jaspername).getAbsolutePath();

			String filePath = downloadPath + jaspername;
			System.out.println("filePath  " + filePath);
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

	public void printReport(JasperPrint jasperPrint, String format, String printStatus, OutputStream outputStream,
			String reportName) throws Exception {

		if (format != null && format.equalsIgnoreCase("pdf")) {
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
		} else if (format != null && format.equalsIgnoreCase("xls")) {

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
