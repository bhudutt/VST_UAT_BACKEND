package com.hitech.dms.web.controller.goods.transit.searchReport;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response.Status;

import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.app.config.FileStorageProperties;
import com.hitech.dms.app.config.persistence.ConnectionConfiguration;
import com.hitech.dms.app.exceptions.ErrorDetails;
import com.hitech.dms.web.model.goodsInTransitReport.request.AdvanceTrackingReportRequestModel;
import com.hitech.dms.web.model.goodsInTransitReport.request.GoodsInTransitReportRequest;
import com.hitech.dms.web.model.goodsInTransitReport.response.GoodsInTransitReportRepList;
import com.hitech.dms.web.model.goodsInTransitReport.response.ModelItemList;
import com.hitech.dms.web.service.goodsInTransitReport.GoodsInTransitReportService;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;


/**
 * 
 * @author vivek.gupta
 *
 */
@RestController
@RequestMapping("/goodsInTransit")
public class GoodsInTransitReportController {
	
	private static final Logger logger = LoggerFactory.getLogger(GoodsInTransitReportController.class);
	
	
	@Autowired
	private GoodsInTransitReportService goodsInTransitReportService;
	
	@Autowired
	private ConnectionConfiguration dataSourceConnection;
	
	@Value("${file.upload-dir.AdvanceTrackingReport}")
	private String advanceTrackingReportPath;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	@Autowired
	private FileStorageProperties fileStorageProperties;
	
	@Value("${file.upload-dir.GoodsTransit:C:\\VST-DMS-APPS\\REPORTS\\Goods-Transit\\Excel\\\\}")
    private String dcExcelPath;
	
	
	@PostMapping(value="/search")
	public ResponseEntity goodsInTransitSearch(@RequestBody GoodsInTransitReportRequest requestModel, BindingResult bindingResult,
			OAuth2Authentication authentication, Device device,HttpServletRequest request) {
		
		HttpHeaders headers = new HttpHeaders();
		String userCode = null;
		GoodsInTransitReportRepList responseModel = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		if (bindingResult.hasErrors()) {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Spare delivery challan not search or server side error.");

			ErrorDetails errorDetails = new ErrorDetails(HttpStatus.BAD_REQUEST, "Validation failed");
			errorDetails.setCount(bindingResult.getErrorCount());
			errorDetails.setStatus(HttpStatus.BAD_REQUEST);
			List<String> errors = new ArrayList<>();
			bindingResult.getAllErrors().forEach(error -> errors.add(error.getDefaultMessage()));
			errorDetails.setErrors(errors);

			userAuthResponse.setResponseCode(codeResponse);
			userAuthResponse.setResponseData(errorDetails);

			return ResponseEntity.ok(userAuthResponse);
		}
		responseModel = goodsInTransitReportService.goodsInTransitSearch(userCode, requestModel,device);
		if (responseModel != null && responseModel.getStatus().equals(Status.OK)) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Successful on " + LocalDate.now());
			codeResponse.setMessage(responseModel.getMessage());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage(responseModel.getMessage());
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);

	 }
	
	@PostMapping("/exportGoodsInTransitReports")
	public void generateReport(@RequestBody GoodsInTransitReportRequest requestModel, @RequestParam(defaultValue = "xlsx") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		String userCodes = null;
		if (authentication != null) {
			userCodes = authentication.getUserAuthentication().getName();
		}
		String reportName="Goods In Transit Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();

		SimpleDateFormat dmyFormat = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDateStr = dmyFormat.format(requestModel.getAsOnDate());
	
		
		jasperParameter.put("userCode", userCodes);
		jasperParameter.put("stateId", requestModel.getStateId());
		jasperParameter.put("dealerId", requestModel.getDealerId());
		jasperParameter.put("branchId", requestModel.getBranchId());
		jasperParameter.put("asOnDate", formattedDateStr);
		jasperParameter.put("profitCenterId", requestModel.getProfitCenterId());
		jasperParameter.put("orgHierID", requestModel.getOrgHierID());
		jasperParameter.put("modelId", requestModel.getModelId());
		jasperParameter.put("itemNumber", requestModel.getItemNumber());
		jasperParameter.put("includeInactive", requestModel.getIncludeInActive());
//		jasperParameter.put("zone", requestModel.getZone());
//		jasperParameter.put("area", requestModel.getArea());
//		jasperParameter.put("territory", requestModel.getTerritory());
		jasperParameter.put("page", requestModel.getPage());
		jasperParameter.put("Size", requestModel.getSize());
		
		String filePath="";
		String property = System.getProperty("os.name");
		
		if(property.contains("Windows")) {
			filePath=dcExcelPath;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/goods-transit/Excel/";
		}
		
		JasperPrint jasperPrint = xlsxGeneratorReport(request, "InTransitReport.jasper" , jasperParameter, filePath);
		try {
			response.setContentType("application/xlsx");
			response.setHeader("Content-Disposition", "inline; filename=InTransitReport.xlsx");
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
	
	
	@SuppressWarnings("unchecked")
	@GetMapping("/getItemNumberByModelId")
	public ResponseEntity<?> itemDetailList(@RequestParam(name = "modelId") BigInteger modelId, OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
//		SimpleDateFormat formatter = getSimpleDateFormat();
		List<ModelItemList> responseModel = goodsInTransitReportService.itemDetailList(modelId,userCode);
		
		if (responseModel != null && !responseModel.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch party Name Search List on " + LocalDate.now());
			codeResponse.setMessage("Party Name Search List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Party Name Search List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	

	@PostMapping("/exportAdvanceTrackingReports")
	public void generateReport(@RequestBody AdvanceTrackingReportRequestModel requestModel, @RequestParam(defaultValue = "xlsx") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		String userCodes = null;
		if (authentication != null) {
			userCodes = authentication.getUserAuthentication().getName();
		}
		
		String reportName="Advance Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		
		jasperParameter.put("pcId", requestModel.getPcId());
		jasperParameter.put("orgHierID",requestModel.getOrgHierID());
		jasperParameter.put("dealerId",requestModel.getDealerId());
		jasperParameter.put("branchId", requestModel.getBranchId());
		jasperParameter.put("Stateid", requestModel.getStateid());
		jasperParameter.put("Model", requestModel.getModel());
		jasperParameter.put("LoanCash", requestModel.getLoanCash());
		jasperParameter.put("FinancialInstitute", requestModel.getFinancialInstitute());
		jasperParameter.put("fromDate", requestModel.getFromDate());
		jasperParameter.put("toDate", requestModel.getToDate());
		jasperParameter.put("UserCode", userCodes);
//		jasperParameter.put("Page", requestModel.getPage());
//		jasperParameter.put("Size", requestModel.getSize());
				
		String filePath="";
		String property = System.getProperty("os.name");
		if(property.contains("Windows")) {
			filePath=advanceTrackingReportPath;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/advanceTrackingReport/TrackingReport/";
		}
		
		JasperPrint jasperPrint = xlsxGeneratorReport(request, "advancetrackingreport.jasper" , jasperParameter,filePath);
		try {
			response.setContentType("application/xlsx");
			response.setHeader("Content-Disposition", "inline; filename=AdvanceTrackingReport.xlsx");
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
	
	
	
}
