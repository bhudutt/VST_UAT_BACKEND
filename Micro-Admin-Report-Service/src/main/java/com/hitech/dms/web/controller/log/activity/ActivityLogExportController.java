/**
 * 
 */
package com.hitech.dms.web.controller.log.activity;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.config.persistence.ConnectionConfiguration;
import com.hitech.dms.web.dao.adminReport.AdminReportDao;
import com.hitech.dms.web.model.log.activity.list.request.ActivityLogListRequestModel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

/**
 * @author dinesh.jakhar
 *
 */
@RestController
@RequestMapping("/export")
@SecurityRequirement(name = "hitechApis")
public class ActivityLogExportController {
	private static final Logger logger = LoggerFactory.getLogger(ActivityLogExportController.class);

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
    private ConnectionConfiguration dataSourceConnection;
    
    @Autowired
    private AdminReportDao adminDao;
	
	@Value("${file.upload-dir.userActivityLogTemp:C:\\VST-DMS-APPS\\REPORTS\\activity log\\}")
    private String userActivityLogPath;
	
//	@Value("${file.upload-dir.userActivityLogTemp}")
//    private String userActivityLogPath;
	

//	@Autowired
//	private ActivityLogDao activityLogDao;
//	@Autowired
//	private MessageSource messageSource;
//	@Autowired
//	private FileStorageProperties fileStorageProperties;

//	@PostMapping("/fetchAcivityLogExport")
//	public ResponseEntity<?> fetchAcivityLogExport(@RequestBody ActivityLogListRequestModel requestModel,
//			OAuth2Authentication authentication) {
//		String userCode = null;
//		if (authentication != null) {
//			userCode = authentication.getUserAuthentication().getName();
//		}
//		HeaderResponse userAuthResponse = new HeaderResponse();
//		MessageCodeResponse codeResponse = new MessageCodeResponse();
//		SimpleDateFormat formatter = getSimpleDateFormat();
//		ExportResponseModel responseModel = null;
//		String fileName = null;
//		ActivityLogListMainResponseModel responseMainModel = activityLogDao.fetchAcivityLogExport(userCode,
//				requestModel);
//		if (responseMainModel != null) {
//			List<ActivityLogListResponseModel> exportList = responseMainModel.getSearchList();
//
//			Date currDate = new Date();
//
//			fileName = messageSource.getMessage("app.temp.file.name",
//					new Object[] { "ACTIVITYLOGLIST" + "_" + currDate.getTime() }, LocaleContextHolder.getLocale());
//			String filePath = messageSource.getMessage("label.temp.activity.log.path", new Object[] { userCode },
//					LocaleContextHolder.getLocale());
//			String limit = messageSource.getMessage("label.rep.activity.log.excelLimit", new Object[] { 1 },
//					LocaleContextHolder.getLocale());
//			if (exportList == null || exportList.isEmpty()) {
//				exportList = new ArrayList<ActivityLogListResponseModel>();
//				ActivityLogListResponseModel exportModel = new ActivityLogListResponseModel();
//				exportList.add(exportModel);
//			}
//			filePath = fileStorageProperties.getUploadDir() + filePath;
//			File file = new File(filePath + fileName);
//			if (file.exists()) {
//				file.delete();
//			}
//			if (!exportList.isEmpty()) {
//				Integer listSize = exportList.size();
//				logger.debug(listSize + " : limit : " + limit);
//				Double target = (listSize.doubleValue()) / Integer.valueOf(limit);
//				target = Math.ceil(target);
//				logger.debug("target : " + target);
//				List<List<ActivityLogListResponseModel>> parts = CommonUtils.chopIntoParts(exportList,
//						target.intValue());
//				logger.debug("parts : " + parts.size());
//				for (int i = 1; i <= parts.size(); i++) {
//					responseModel = ExcelUtils.createExcelInMultiSheets(filePath, fileName,
//							messageSource.getMessage("label.rep.access.log.list.sheet", new Object[] { i },
//									LocaleContextHolder.getLocale()),
//							"", messageSource.getMessage("label.rep.access.log.list.header", null,
//									LocaleContextHolder.getLocale()),
//							parts.get(i - 1), "EXPORTREPACTLOGLIST");
//				}
//			} else {
//				responseModel = ExcelUtils.createExcelInMultiSheets(filePath, fileName,
//						messageSource.getMessage("label.rep.access.log.list.sheet", new Object[] { 1 },
//								LocaleContextHolder.getLocale()),
//						"", messageSource.getMessage("label.rep.access.log.list.header", null,
//								LocaleContextHolder.getLocale()),
//						exportList, "EXPORTREPACTLOGLIST");
//			}
//			logger.info("fileName : " + fileName);
//		}
//		if (responseModel != null && responseModel.isFileCreated()) {
//			responseModel.setFileName(fileName);
//			codeResponse.setCode("EC200");
//			codeResponse.setDescription("Successful on " + formatter.format(new Date()));
//			codeResponse.setMessage(responseModel.getMsg());
//		} else {
//			codeResponse.setCode("EC500");
//			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
//			codeResponse.setMessage("User Activity Log List Not Fetched or server side error.");
//		}
//		userAuthResponse.setResponseCode(codeResponse);
//		userAuthResponse.setResponseData(responseModel);
//		return ResponseEntity.ok(userAuthResponse);
//	}
	

	
	@PostMapping("/fetchAcivityLogExport")
	public void fetchAcivityLogExport(@RequestBody ActivityLogListRequestModel requestModel,
			HttpServletRequest request, HttpServletResponse response,
			OAuth2Authentication authentication) throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
	    HashMap<String, Object> jasperParameter = new HashMap<String, Object>();

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		jasperParameter.put("PcID", requestModel.getPcID()!=null?requestModel.getPcID().intValue():null);
//		jasperParameter.put("orgHierID", requestModel.getOrgHierID());
		jasperParameter.put("dealerId", requestModel.getDealerID()!=null?requestModel.getDealerID().intValue():null);
		jasperParameter.put("BranchId", requestModel.getBranchID()!=null?requestModel.getBranchID().intValue():null);	
		jasperParameter.put("userCode", userCode);
		jasperParameter.put("FromDate", formatter.format(requestModel.getFromDate()));
		jasperParameter.put("ToDate", formatter.format(requestModel.getToDate()));
//		jasperParameter.put("includeInActive", requestModel.getIncludeInActive());
		jasperParameter.put("page", requestModel.getPage());
		jasperParameter.put("size", requestModel.getSize());
	       
		String filePath="";
		String property = System.getProperty("os.name");
		
		if(property.contains("Windows")) {
			filePath=userActivityLogPath;
		}else {
			filePath="/var/VST-DMS-APPS/REPORTS/activity log"; 
		}
		JasperPrint jasperPrint = adminDao.ExcelGeneratorReport(request, "UserActivityLog.jasper" , jasperParameter,filePath);
		try {
			response.setContentType("application/xlsx");
			response.setHeader("Content-Disposition", "inline; filename=UserActivityLog.xlsx");
			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();
			adminDao.printReport(jasperPrint, "xlsx", null, outputStream,null);

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
			
		public JasperPrint xlsxGeneratorReport( 
				  HttpServletRequest request, String jaspername,
				  HashMap<String, Object> jasperParameter,String downloadPath) {
			
//			String filePath = request.getServletContext().getRealPath("/reports/" + jaspername);
			JasperPrint jasperPrint = null;
			Connection connection = null;
			try {
				String filePath=downloadPath+jaspername;
				System.out.println("filePath  "+filePath);
				connection = dataSourceConnection.getConnection();
	            System.out.println("jasperParameter "+jasperParameter);

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
		
		public void printReport(JasperPrint jasperPrint, String format, String printStatus, OutputStream outputStream, String reportName)
				throws Exception {

			JRXlsxExporter exporter = new JRXlsxExporter();
			SimpleXlsxReportConfiguration reportConfigXLS = new SimpleXlsxReportConfiguration();
	        reportConfigXLS.setSheetNames(new String[] { "sheet1" });
	        exporter.setConfiguration(reportConfigXLS);
	        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
	        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
	       
	        exporter.exportReport();
		}
	
}
