package com.hitech.dms.web.Controller.spare.customer.order;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
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
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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
import com.hitech.dms.app.exceptions.ErrorDetails;
import com.hitech.dms.app.utils.DateToStringParserUtils;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.model.spara.customer.order.request.CustomerOrderSearchRequest;
import com.hitech.dms.web.model.spara.customer.order.response.SpareCustOrderSearchResponseModel;
import com.hitech.dms.web.model.spara.customer.order.response.SpareCustOrderViewListResponseModel;
import com.hitech.dms.web.model.spara.delivery.challan.request.DcSearchRequest;
import com.hitech.dms.web.service.spare.customer.order.SpareCustomerOrderViewService;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

/**
 * @author Vivek.Gupta
 *
 */

@RestController
@RequestMapping("/api/v1/custOrderView")
@Slf4j
@Validated
public class ViewSpareCustomerOrderController {
	
	
	
	@Autowired
	private SpareCustomerOrderViewService spareCustomerOrderViewService;
	
	@Value("${file.upload-dir.CoTemplate}")
    private String dcExcelPath;
	
	@Autowired
	private ConnectionConfiguration dataSourceConnection;
	
	 private SimpleDateFormat getSimpleDateFormat() {
			return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		}
	
	
	@PostMapping("/search")
	public ResponseEntity<?> customerOrderSearch(@RequestBody CustomerOrderSearchRequest requestMap, BindingResult bindingResult,
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			OAuth2Authentication authentication, Device device, HttpServletRequest request)throws ParseException{
		
		
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SpareCustOrderSearchResponseModel responseModel = null;

		if (bindingResult.hasErrors()) {
			codeResponse.setCode("EC400");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Spare customer order not created or server side error.");

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
		responseModel = spareCustomerOrderViewService.searchCODetailByStartAndEndDate(authorizationHeader, userCode, requestMap, device);
		if (responseModel != null && responseModel.getStatusCode().compareTo(WebConstants.STATUS_OK_200) == 0) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Successful on " + LocalDate.now());
			codeResponse.setMessage(responseModel.getMsg());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + LocalDate.now());
			codeResponse.setMessage("Error While Creating Spare Customer Order.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
		
	}
	
	
	@SuppressWarnings("unchecked")
	@GetMapping("/view")
	public ResponseEntity<?> getCustomerOrderById(@RequestParam(name = "customerOrderId") Integer customerOrderId,
			OAuth2Authentication authentication) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SpareCustOrderViewListResponseModel responseModel = spareCustomerOrderViewService.getCustomerOrderById(userCode, customerOrderId);
		if (responseModel != null ) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Fetch party Name Search List on " );
			codeResponse.setMessage("Party Name Search List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " );
			codeResponse.setMessage("Party Name Search List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(responseModel);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	
	@PostMapping("/exportCOReports")
	public void generateReport(@RequestBody CustomerOrderSearchRequest  requestModel, @RequestParam(defaultValue = "xlsx") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		String userCodes = null;
		if (authentication != null) {
			userCodes = authentication.getUserAuthentication().getName();
		}
		String reportName="Customer Order Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
					
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		jasperParameter.put("DEALERID", requestModel.getDealerId());
		jasperParameter.put("BRANCHID", requestModel.getBranchId());
		jasperParameter.put("PCID", requestModel.getPcId());
		jasperParameter.put("STATE", requestModel.getStateId());
		jasperParameter.put("startDate", formatter.format(requestModel.getStartDate()));
		jasperParameter.put("endDate", formatter.format(DateToStringParserUtils.addEndTimeOFTheDay(requestModel.getEndDate())));
		jasperParameter.put("Status", requestModel.getCustomerOrderStatus()); 
		jasperParameter.put("CustomerOrderNumber", requestModel.getCustomerOrderNumber());
		jasperParameter.put("PartyTypeId", requestModel.getPartyTypeId());
		jasperParameter.put("ProductCategoryId", requestModel.getProductCategoryId());
		jasperParameter.put("ProductSubCategoryId", requestModel.getProductSubCategoryId());
		jasperParameter.put("PartyCodeId", requestModel.getPartyCodeId());
		jasperParameter.put("userCode", userCodes);
		jasperParameter.put("page", requestModel.getPage());
		jasperParameter.put("size", requestModel.getSize());
		
		
		String filePath="";
		String property = System.getProperty("os.name");
		
		if(property.contains("Windows")) {
			filePath=dcExcelPath;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/Customer-Order/Excel/";
		}
		
		JasperPrint jasperPrint = xlsxGeneratorReport(request, "Customer_Order.jasper" , jasperParameter,filePath);
		try {
			response.setContentType("application/xlsx");
			response.setHeader("Content-Disposition", "inline; filename=Customer_Order.xlsx");
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
