package com.hitech.dms.web.controller.jobcard.billing.create;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.config.persistence.ConnectionConfiguration;
import com.hitech.dms.web.model.jobcard.billing.request.JobBillingSearchRequestModel;
import com.hitech.dms.web.service.jobcard.billing.JobCardBillingPrintService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@RestController
@RequestMapping("/Billing")
@SecurityRequirement(name = "hitechApis")
public class JobCardBillingPrintPdfController {
	
	@Autowired
	private ConnectionConfiguration dataSourceConnection;
	
	@Autowired
	private JobCardBillingPrintService jobCardBillingPringService;
	
	@Autowired
	@Value("${file.upload-dir.JobCardBillingReport}")
    private String downloadPathJobCardBilling;
	
	@Autowired
	@Value("${file.upload-dir.BillingReports}")
    private String downloadBillingPath;
	
//	@GetMapping("/exportJobCardBillingReport")
//	public void exportJobCardBillingReport(@RequestParam(value = "roBillId") Integer roBillId,@RequestParam(value = "flag") Integer flag,
//			@RequestParam(defaultValue = "pdf") String format, @RequestParam(required = false) String printStatus,
//			OAuth2Authentication authentication, HttpServletRequest request, HttpServletResponse response)
//			throws IOException {
//		OutputStream outputStream = null;
//		String userCode = null;
//		if (authentication != null) {
//			userCode = authentication.getUserAuthentication().getName();
//		}
//		String reportName = "JobCardBilling";
//		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
//		jasperParameter.put("RollBillId", roBillId);
//		jasperParameter.put("Flag",flag);
//
//		String filePath = "";
//		String property = System.getProperty("os.name");
//		if (property.contains("Windows")) {
//			filePath=downloadPathJobCardBilling;
//		}else {
//			filePath="/var/VST-DMS-APPS/FILES/REPORTS/JobCard Billing/";
//		}
//		JasperPrint jasperPrint = jobCardBillingPringService.PdfGeneratorReport(request, "JobCardBillFront.jasper",
//				jasperParameter, filePath);
//		try {
//
//			if (format != null && format.equalsIgnoreCase("pdf")) {
//				response.setContentType("application/pdf");
//				response.setHeader("Content-Disposition", "inline; filename=" + reportName + ".pdf");
//			}
//
//			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
//			outputStream = response.getOutputStream();
//
//			jobCardBillingPringService.printReport(jasperPrint, format, printStatus, outputStream, reportName);
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if (outputStream != null) {
//				outputStream.flush();
//				outputStream.close();
//			}
//		}
//	}
	
	// excel export 
	
	@PostMapping("/exportBillingReports")
	public void generateReport(@RequestBody JobBillingSearchRequestModel requestModel, @RequestParam(defaultValue = "xlsx") String format,
			@RequestParam(required = false) String printStatus, OAuth2Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
		String userCodes = null;
		if (authentication != null) {
			userCodes = authentication.getUserAuthentication().getName();
		}
		
		String reportName="Billing Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		
		jasperParameter.put("UserCode", userCodes);
		jasperParameter.put("JobcardNo", requestModel.getJobCardNo());
		jasperParameter.put("JobCardFormDate",requestModel.getJobCardFormDate());
		jasperParameter.put("JobCardToDate", requestModel.getJobCardToDate());
		jasperParameter.put("JobCardBillNo", requestModel.getBillingNumber());
		jasperParameter.put("JobCardBillFormDate", requestModel.getBillingFormDate());	
		jasperParameter.put("JobCardBillToDate", requestModel.getBillingToDate());	
		jasperParameter.put("Billstatus", requestModel.getBillingStatus());	
		jasperParameter.put("SaleType", requestModel.getSaleType());	
		jasperParameter.put("PaymentMode", requestModel.getPaymentMode());	
		jasperParameter.put("Engine", requestModel.getEngineNo());	
		jasperParameter.put("Chassis", requestModel.getChassisNo());	
		jasperParameter.put("CustomerType", requestModel.getCustomerType());	
		jasperParameter.put("CustomerName", requestModel.getCustomerName());	
		String filePath="";
		String property = System.getProperty("os.name");
		if(property.contains("Windows")) {
			filePath=downloadBillingPath;
		}else {
			filePath="/var/VST-DMS-APPS/FILES/REPORTS/Billing/";
		}
		
		JasperPrint jasperPrint = xlsxGeneratorReport(request, "JobCardBill.jasper" , jasperParameter,filePath);
		try {
			response.setContentType("application/xlsx");
			response.setHeader("Content-Disposition", "inline; filename=JobCardBillingReport.xlsx");
			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();
			jobCardBillingPringService.printReport(jasperPrint, format, printStatus, outputStream);

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
	
	/**
	 * 
	 * @param roId
	 * @param format
	 * @param printStatus
	 * @param authentication
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@GetMapping("/exportJobCardBillingReport")
	public void exportJobCardOpenReport(@RequestParam(value = "roBillId") Integer roBillId,
			@RequestParam(defaultValue = "pdf") String format, @RequestParam(required = false) String printStatus,
			OAuth2Authentication authentication, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName = "JobCardBilling";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("RollBillId", roBillId);
	//	jasperParameter.put("Flag", 0);

		String filePath = "";
		String property = System.getProperty("os.name");
		
		  if (property.contains("Windows")) {
			  filePath = downloadPathJobCardBilling;
			  } else { 
			  filePath="/var/VST-DMS-APPS/FILES/REPORTS/JobCard Billing/";
			  }
	
		
		JasperPrint jasperPrint = jobCardBillingPringService.PdfGeneratorReportForJobCardBill(request, "JobCardBillFront.jasper",
				jasperParameter, filePath);
		try {

			if (format != null && format.equalsIgnoreCase("pdf")) {
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "inline; filename=" + reportName + ".pdf");
			}

			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();

			jobCardBillingPringService.printReport(jasperPrint, format, printStatus, outputStream, reportName);

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
