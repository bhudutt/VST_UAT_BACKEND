/**
 * 
 */
package com.hitech.dms.web.controller.jobcardreport;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.config.persistence.ConnectionConfiguration;
import com.hitech.dms.web.controller.billingreport.JobCardBillExcelReportController;
import com.hitech.dms.web.dao.common.CommonDao;
import com.hitech.dms.web.model.jobcard.request.JobCardExcelReportRequest;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

/**
 * @author santosh.kumar
 *
 */
@RestController
@RequestMapping("/report")
@SecurityRequirement(name = "hitechApis")
public class jobcardexportexcelcantroller {
	private static final Logger logger = LoggerFactory.getLogger(JobCardBillExcelReportController.class);

	@Autowired
	private ConnectionConfiguration dataSourceConnection;
	@Autowired
	private CommonDao dao;

	@Value("${file.upload-dir.jobCardReports}")
	private String downloadBillingPath;

	@PostMapping("/exportJobcardReport")
	public void generateReport(@RequestBody JobCardExcelReportRequest requestModel,
			@RequestParam(defaultValue = "xlsx") String format, @RequestParam(required = false) String printStatus,
			OAuth2Authentication authentication, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		OutputStream outputStream = null;
		String userCodes = null;
		if (authentication != null) {
			userCodes = authentication.getUserAuthentication().getName();
		}

		String reportName = "jobCard Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();


        jasperParameter.put("BranchId", requestModel.getBranchId());
        jasperParameter.put("UserCode", userCodes);
        jasperParameter.put("RONumber", requestModel.getRoNumber());
        jasperParameter.put("ChasisNumber", requestModel.getChasisNumber());
        jasperParameter.put("EngineNumber", requestModel.getEngineNumber());
        jasperParameter.put("VinNo", requestModel.getVinNo());
        jasperParameter.put("CustomerName", requestModel.getCustomerName());
        jasperParameter.put("MobileNo", requestModel.getMobileNo());
        jasperParameter.put("ServiceTypeId", requestModel.getServiceTypeId());
        jasperParameter.put("RepairTypeId", requestModel.getRepairTypeId());
        jasperParameter.put("JobCardCategoryId", requestModel.getJobCardCategoryId());
        jasperParameter.put("InvoiceNo", requestModel.getInvoiceNo());
        jasperParameter.put("ServcieBookingNumber", requestModel.getServiceBookingNumber());
        jasperParameter.put("QuotationNumber", requestModel.getQuotationNumber());
        jasperParameter.put("Fromdate", requestModel.getFromDate());
        jasperParameter.put("Todate", requestModel.getToDate());
        jasperParameter.put("page", requestModel.getPage());
        jasperParameter.put("size", requestModel.getSize());
        jasperParameter.put("pcId", requestModel.getPcId());
        jasperParameter.put("hoId", requestModel.getHoId());
        jasperParameter.put("zoneId", requestModel.getZoneId());
        jasperParameter.put("stateId", requestModel.getStateId());
        jasperParameter.put("territoryId", requestModel.getTerritoryId());
		String filePath = "";
		String property = System.getProperty("os.name");
		if (property.contains("Windows")) {
			filePath = downloadBillingPath;
		} else {
			filePath = "/var/VST-DMS-APPS/FILES/REPORTS/jobCardExcel/";
		}

		JasperPrint jasperPrint = xlsxGeneratorReport(request, "jobcard.jasper", jasperParameter, filePath);
		try {
			response.setContentType("application/xlsx");
			response.setHeader("Content-Disposition", "inline; filename=JobCardExcelReport.xlsx");
			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();
			dao.printReport(jasperPrint, format, printStatus, outputStream);

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

	public JasperPrint xlsxGeneratorReport(HttpServletRequest request, String jaspername,
			HashMap<String, Object> jasperParameter, String downloadPath) {

//		String filePath = request.getServletContext().getRealPath("/reports/" + jaspername);
		JasperPrint jasperPrint = null;
		Connection connection = null;
		try {
			String filePath = downloadBillingPath + jaspername;
			System.out.println("filePath  " + filePath);
			connection = dataSourceConnection.getConnection();
			System.out.println("jasperParameter " + jasperParameter);

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

}
