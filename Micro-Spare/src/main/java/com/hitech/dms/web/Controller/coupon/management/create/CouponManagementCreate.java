package com.hitech.dms.web.Controller.coupon.management.create;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

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
import com.hitech.dms.web.model.coupon.management.CouponDetailSaveResponse;
import com.hitech.dms.web.model.coupon.management.CouponExcelReportRequest;
import com.hitech.dms.web.model.coupon.management.CouponReportRequest;
import com.hitech.dms.web.service.coupon.management.CouponManagementService;

import net.sf.jasperreports.engine.JasperPrint;
@RestController
@RequestMapping("coupon/create")
public class CouponManagementCreate {
	
	
	@Autowired
	CouponManagementService service;
	
	
	@Value("${file.upload-dir.CouponManagementReport}")
	private String CouponManagementReport;
	
	
	
	
	
	
	
	@PostMapping("/saveCouponDetails")
	public ResponseEntity<?> saveCouponDetails(@RequestBody CouponDetailSaveResponse couponDetails,
			OAuth2Authentication authentication, Device device, HttpServletRequest request)
	{
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
			
		}
		CouponDetailSaveResponse response=service.saveCouponDetail(couponDetails,userCode);
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		
		if (response != null && response.getStatusCode() == 200) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("" + new Date());
			codeResponse.setMessage(response.getStatusMessage());
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + new Date());
			codeResponse.setMessage(response.getStatusMessage());
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
		
		
	}
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("yyyy/MM/dd");
	}

	
	
	// printExcel
	@PostMapping("/exportCpnExcelReport1")
	public void exportENQExcelReport(@RequestBody CouponExcelReportRequest requestModel,
			@RequestParam(defaultValue = "xls") String format, @RequestParam(required = false) String printStatus,
			OAuth2Authentication authentication, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		
		printStatus="true";
	   System.out.println("request at controller "+requestModel +"printStatus "+printStatus);
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		
		String reportName = "Coupon Management";
		
		SimpleDateFormat smdf = getSimpleDateFormat();
		//String fromDate = smdf.format(requestModel.getFromDate());
		//String toDate = smdf.format(requestModel.getToDate());
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		
		jasperParameter.put("FromDate",requestModel.getFromDate()!=null?requestModel.getFromDate():null);
		jasperParameter.put("ToDate", requestModel.getToDate()!=null?requestModel.getToDate():null);
		jasperParameter.put("UserCode", userCode!=null?userCode:null);
		jasperParameter.put("DocNo", requestModel.getDocNo()!=null?requestModel.getDocNo():null);
		jasperParameter.put("DealerCode", requestModel.getDealerCode()!=null?requestModel.getDealerCode():null);
		jasperParameter.put("page", requestModel.getPage());
		jasperParameter.put("Size", requestModel.getSize());
		
		String filePath = "";
		String property = System.getProperty("os.name");
		if (property.contains("Windows")) {
			filePath = CouponManagementReport;
		} else {
			filePath = "/var/VST-DMS-APPS/FILES/REPORTS/CouponManagement/";
		}
		System.out.println("at excel generate ");
		JasperPrint jasperPrint = service.ExcelGeneratorReport(request, "Couponapproval.jasper", jasperParameter,
				filePath);
		System.out.println("after excel generate");
		try {

			if (format != null && format.equalsIgnoreCase("xls")) {
				

				response.setContentType("application/xlsx");
				response.setHeader("Content-Disposition", "inline; filename=" + reportName + ".xlsx");
				response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			}

			outputStream = response.getOutputStream();

			service.printReport1(jasperPrint, format, printStatus,outputStream, reportName);

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
	
	
	//Print JsperReport for Coupon Management Detail
	 @PostMapping("/couponManagementPdfReport")
	 public void printCouponMgmtReport(@RequestBody CouponReportRequest couponRequest,
			@RequestParam(defaultValue = "pdf") String format, @RequestParam(required = false) String printStatus,
			OAuth2Authentication authentication, HttpServletRequest request, HttpServletResponse response)
			throws IOException
			{	
				
		    OutputStream outputStream = null;
			String userCode = null;
			if (authentication != null) {
				userCode = authentication.getUserAuthentication().getName();
			}
			printStatus="true";
			System.out.println("request at controller "+couponRequest +" format is "+format);
			String reportName = "CouponManagement";
			HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
			jasperParameter.put("DocumentNo",couponRequest.getDocumentNo()!=null?couponRequest.getDocumentNo():null);
			jasperParameter.put("DealerCode", couponRequest.getDealerCode()!=null?couponRequest.getDealerCode():null);
			jasperParameter.put("flag",couponRequest.getFlag()!=null?couponRequest.getFlag():null);
			jasperParameter.put("UserCode",userCode!=null?userCode:null);


			String filePath = "";
			String property = System.getProperty("os.name");
			if (property.contains("Windows")) {
				 filePath = CouponManagementReport;
			} else {
				filePath = "/var/VST-DMS-APPS/FILES/REPORTS/CouponManagement/";
			}
		try {

			JasperPrint jasperPrint = service.PdfGeneratorReportForJobCardOpen(request, "Coupon_Management_FrontPage.jasper",
					jasperParameter, filePath);
			
			  System.out.println("after jasperPrint line ");
			
			if (format != null && format.equalsIgnoreCase("pdf")) {
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "inline; filename=" + reportName + ".pdf");
			}

			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();
			System.out.println("after response outputStream  ");
			
			System.out.println("before send data for"+format);

			service.printReport(jasperPrint,format,printStatus,outputStream, reportName);
			System.out.println("after printReport");


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
