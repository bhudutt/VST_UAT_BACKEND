package com.hitech.dms.web.controller.remider.search;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.app.utils.ApiResponse;
import com.hitech.dms.app.utils.DateToStringParserUtils;
import com.hitech.dms.web.model.service.report.request.RemiderSearchBean;
import com.hitech.dms.web.model.service.report.request.ReminderReportReq;
import com.hitech.dms.web.model.service.report.request.ServicePlanStatusBean;
import com.hitech.dms.web.model.service.report.request.ServiceSearchReportBean;
import com.hitech.dms.web.model.service.report.response.CustomerDtlResponse;
import com.hitech.dms.web.model.service.report.response.ModelResponse;
import com.hitech.dms.web.model.service.report.response.ServiceTypeResponse;
import com.hitech.dms.web.model.service.report.response.ZoneResponse;
import com.hitech.dms.web.remider.report.service.SearchRemiderReportService;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import com.hitech.dms.app.config.persistence.ConnectionConfiguration;

@RestController
@RequestMapping("service/reminder")
public class RemiderSearchController {
	
	
	@Autowired
	private ConnectionConfiguration dataSourceConnection;
	

	@Value("${file.upload-dir.reminderServiceTemplate}")
    private String reminderServiceExcelPath;
	
	
	@Value("${file.upload-dir.servicePlanStatusTemplate}")
    private String servicePlanStatusExcelPath;
	
	
	@Autowired
	private SearchRemiderReportService searchRemiderReportService;
	
	
	/**
	 * 
	 * @param authorizationHeader
	 * @param authentication
	 * @param device
	 * @param request
	 * @return
	 */
	@PostMapping(value = "/search")
	public ResponseEntity<?> search(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader, @RequestBody RemiderSearchBean bean,
			OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		MessageCodeResponse responseModel = null;
		List<ServiceSearchReportBean> responseModelList = searchRemiderReportService.getsearchDetails(authorizationHeader,
				userCode,bean);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch JobCard search List " + LocalDate.now());
			codeResponse.setMessage("Fetch JobCard Search List Successfully");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Service reminder seach List Not Fetched !!!.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	/**
	 * 
	 * @param authorizationHeader
	 * @param authentication
	 * @param device
	 * @param request
	 * @return
	 */
	@PostMapping(value = "/searchPlanStatus")
	public ResponseEntity<?> searchPlanStatus(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader, @RequestBody RemiderSearchBean bean,
			OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		MessageCodeResponse responseModel = null;
		List<ServicePlanStatusBean> responseModelList = searchRemiderReportService.searchPlanStatus(authorizationHeader,
				userCode,bean);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch JobCard search List " + LocalDate.now());
			codeResponse.setMessage("Fetch JobCard Search List Successfully");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Service plan status seach List Not Fetched !!!.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	
	@GetMapping("/fetchZoneList")
	public ResponseEntity<?> zoneList(@RequestHeader(value = "Authorization", required = true) String authorizationHeader, OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		List<ZoneResponse> responseModel = searchRemiderReportService.getZoneList(userCode);
		if (responseModel != null ) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Zone Name Search List on " );
			codeResponse.setMessage("Zone Name Search List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " );
			codeResponse.setMessage("Zone Name Search List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@PostMapping("/customerName")
	public ResponseEntity<?> fetchCustomerNameList(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody ReminderReportReq requestModel, OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
	
		List<CustomerDtlResponse> responseModelList = searchRemiderReportService.fetchCustomerName(userCode, requestModel);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Part No List on " + LocalDate.now());
			codeResponse.setMessage("Customer Name Fetched Successfully fetched");
		} else {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Customer Name Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	@PostMapping("/getDealerList")
	public ResponseEntity<?> kpdList(@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody ReminderReportReq requestModel, OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		List<CustomerDtlResponse> responseModel = searchRemiderReportService.getDealerList(userCode,requestModel);
		if (responseModel != null ) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch party Name Search List on " );
			codeResponse.setMessage("KDP Name Search List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " );
			codeResponse.setMessage("KDP Name Search List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping(value = "/vinDetailsByChassisNo")
	public ResponseEntity<?> vinDetailsByChassisNo(@RequestParam("chassisNo") String chassisNo,

			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			OAuth2Authentication authentication) {
		ApiResponse apiResponse = new ApiResponse();
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
			apiResponse.setMessage("Details get successfully.");
			apiResponse.setStatus(HttpStatus.OK.value());
			System.out.println("chassisNo--" + chassisNo + "----");
			apiResponse.setResult(searchRemiderReportService.vinDetailsByChassisNo(chassisNo,userCode));
		return ResponseEntity.ok(apiResponse);
	}
	
	
	
	
	@GetMapping("/getModelList")
	public ResponseEntity<?> fetchModelAllList(OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		List<ModelResponse> responseModelList = searchRemiderReportService.getModelList(userCode,device);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Machine Model Item List on " + LocalDate.now());
			codeResponse.setMessage("Machine Item List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Machine Model Item List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	@GetMapping("/getServiceTypeList")
	public ResponseEntity<?> getServiceTypeList(OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		List<ServiceTypeResponse> responseModelList = searchRemiderReportService.getServiceTypeList(userCode,device);
		if (responseModelList != null && !responseModelList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch Service Type List on " + LocalDate.now());
			codeResponse.setMessage("Service Type List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Service Type List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModelList);
		return ResponseEntity.ok(userAuthResponse);
	}
	

	
	@PostMapping("/excel")
	public void generateReport(@RequestBody RemiderSearchBean bean, @RequestParam(defaultValue = "xlsx") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		String userCodes = null;
		if (authentication != null) {
			userCodes = authentication.getUserAuthentication().getName();
		}
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
			
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		jasperParameter.put("Usercode", userCodes);
		jasperParameter.put("dealerId", bean.getDealerCode());
		jasperParameter.put("CustomerId", bean.getCustomerCode());
		jasperParameter.put("stateId", bean.getStateId());
		jasperParameter.put("ModelId", bean.getModel());
		jasperParameter.put("ChassisNo", bean.getChassisNo());
		jasperParameter.put("FromDate", formatter.format(bean.getFromDate()));
		jasperParameter.put("ToDate", formatter.format(DateToStringParserUtils.addDayByOne(bean.getToDate())));
		jasperParameter.put("ServiceName", bean.getServiceName());
		jasperParameter.put("branchId", bean.getBranchId());
		jasperParameter.put("page", bean.getPage());
		jasperParameter.put("size", bean.getSize());
		
		
		String filePath="";
		String property = System.getProperty("os.name");
		
		if(property.contains("Windows")) {
			filePath=reminderServiceExcelPath;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/Reminder-Service/Excel/";
		}
		
		JasperPrint jasperPrint = xlsxGeneratorReport(request, "ServiceReminderReport.jasper" , jasperParameter,filePath);
		try {
			response.setContentType("application/xlsx");
			response.setHeader("Content-Disposition", "inline; filename=ServiceReminderReport.xlsx");
			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();
			printReport(jasperPrint, format, printStatus, outputStream,null);

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
	
	
	@PostMapping("/planStatusExcel")
	public void generatePlanStatusReport(@RequestBody RemiderSearchBean bean, @RequestParam(defaultValue = "xlsx") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		String userCodes = null;
		if (authentication != null) {
			userCodes = authentication.getUserAuthentication().getName();
		}
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");	
		
		jasperParameter.put("Usercode", userCodes);
		jasperParameter.put("dealerId", bean.getDealerCode());
		jasperParameter.put("CustomerId", bean.getCustomerCode());
		jasperParameter.put("stateId", bean.getStateId());
		jasperParameter.put("ModelId", bean.getModel());
		jasperParameter.put("ChassisNo", bean.getChassisNo());
		jasperParameter.put("FromDate", formatter.format(bean.getFromDate()));
		jasperParameter.put("ToDate", formatter.format(DateToStringParserUtils.addDayByOne(bean.getToDate())));
		jasperParameter.put("ServiceName", bean.getServiceName());
		jasperParameter.put("branchId", bean.getBranchId());
		jasperParameter.put("page", bean.getPage());
		jasperParameter.put("size", bean.getSize());
			
		
		String filePath="";
		String property = System.getProperty("os.name");
		
		if(property.contains("Windows")) {
			filePath=servicePlanStatusExcelPath;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/Plan-Status/Excel/";
		}
		
		JasperPrint jasperPrint = xlsxGeneratorReport(request, "ServicePlanStatusReport.jasper" , jasperParameter,filePath);
		try {
			response.setContentType("application/xlsx");
			response.setHeader("Content-Disposition", "inline; filename=ServicePlanStatusReport.xlsx");
			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();
			printReport(jasperPrint, format, printStatus, outputStream,null);

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
		
//		String filePath = request.getServletContext().getRealPath("/reports/" + jaspername);
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
