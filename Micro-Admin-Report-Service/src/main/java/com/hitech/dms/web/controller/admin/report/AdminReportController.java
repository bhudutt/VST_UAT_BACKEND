/**
 * 
 */
package com.hitech.dms.web.controller.admin.report;

import java.io.IOException;
import java.io.OutputStream;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.web.dao.adminReport.AdminReportDao;
import com.hitech.dms.web.model.admin.request.DealerEmployeeSearchRequest;
import com.hitech.dms.web.model.admin.request.DealerUserSearchRequest;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import net.sf.jasperreports.engine.JasperPrint;

/**
 * @author santosh.kumar
 *
 */
@RestController
@RequestMapping("/export")
@SecurityRequirement(name = "hitechApis")
public class AdminReportController {
	private static final Logger logger = LoggerFactory.getLogger(AdminReportController.class);

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	@Value("${file.upload-dir.Admin}")
	private String downloadPath;
	
	@Autowired
    private AdminReportDao adminDao;
	
	

	@PostMapping("/adminExportExcel")
	public void exportAdminDelearReport(@RequestBody DealerEmployeeSearchRequest requestModel,
			@RequestParam(defaultValue = "xls") String format, @RequestParam(required = false) String printStatus,
			OAuth2Authentication authentication, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName = "EmpReport Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		
		jasperParameter.put("dealerId",requestModel.getDealerId() !=null ? requestModel.getDealerId():null );
		jasperParameter.put("branchID", requestModel.getBranchId() !=null ? requestModel.getBranchId() :null);
		jasperParameter.put("userCode", userCode);
		jasperParameter.put("employeeCode",
				requestModel.getEmployeeCode() != null ? requestModel.getEmployeeCode() : null);
		jasperParameter.put("Mobilenumber",
				requestModel.getMobileNo() !=null ? requestModel.getMobileNo() : null);
		jasperParameter.put("FromDate",
				requestModel.getFromDate1() != null ? requestModel.getFromDate1() : null);
		jasperParameter.put("ToDate", requestModel.getToDate1() != null ? requestModel.getToDate1() : null);
		jasperParameter.put("orgHierID", requestModel.getOrgHierId() != null ? requestModel.getOrgHierId() : null);
		jasperParameter.put("includeInactive", requestModel.getIncludeInactive() != null ? requestModel.getIncludeInactive() : null);
		jasperParameter.put("status", requestModel.getStatus() !=null ? requestModel.getStatus(): 'A');
		jasperParameter.put("Page", requestModel.getPage());
		jasperParameter.put("Size", requestModel.getSize());
		
		logger.info("jasperParameter "+jasperParameter);

		String filePath = "";
		String property = System.getProperty("os.name");
		if (property.contains("Windows")) {
			filePath = downloadPath;
		} else {
			filePath = "/var/VST-DMS-APPS/FILES/REPORTS/Admin/";
		}
		JasperPrint jasperPrint = adminDao.ExcelGeneratorReport(request, "EmpReport.jasper",
				jasperParameter, filePath);
		try {

			if (format != null && format.equalsIgnoreCase("xls")) {
				// response.setContentType("application/xls");
				response.setHeader("Content-Disposition", "attachment; filename=" + reportName + ".xlsx");
				response.setContentType("application/vnd.ms-excel");
			}

			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();

			adminDao.printReport(jasperPrint, format, printStatus, outputStream, reportName);

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
	@PostMapping("/dealerUserExportExcel")
	public void dealerUserExportExcel(@RequestBody DealerUserSearchRequest requestModel,
			@RequestParam(defaultValue = "xls") String format, @RequestParam(required = false) String printStatus,
			OAuth2Authentication authentication, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName = "UserReport Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		
		jasperParameter.put("dealerId",requestModel.getDealerId() !=null ? requestModel.getDealerId().intValue():null );
		jasperParameter.put("branchID", requestModel.getBranchId() !=null ? requestModel.getBranchId().intValue() :null);
		jasperParameter.put("userCode", userCode);
		jasperParameter.put("employeeCode",
				requestModel.getEmpCode() != null ? requestModel.getEmpCode() : null);
		jasperParameter.put("employeeName",
				requestModel.getEmpName() !=null ? requestModel.getEmpName() : null);
		jasperParameter.put("FromDate",
				requestModel.getFromDate() != null ? requestModel.getFromDate() : null);
		jasperParameter.put("ToDate", requestModel.getToDate() != null ? requestModel.getToDate() : null);
		jasperParameter.put("orgHierID", requestModel.getOrgHierId() != null ? requestModel.getOrgHierId().intValue() : null);
		jasperParameter.put("includeInactive", 'N');
		jasperParameter.put("status", requestModel.getStatus() !=null ? requestModel.getStatus(): 'A');
		jasperParameter.put("Page", requestModel.getPage());
		jasperParameter.put("Size", requestModel.getSize());

		String filePath = "";
		String property = System.getProperty("os.name");
		if (property.contains("Windows")) {
			filePath = downloadPath;
		} else {
			filePath = "/var/VST-DMS-APPS/FILES/REPORTS/Admin/";
		}
		JasperPrint jasperPrint = adminDao.ExcelGeneratorReport(request, "UserReport.jasper",
				jasperParameter, filePath);
		try {

			if (format != null && format.equalsIgnoreCase("xls")) {
				// response.setContentType("application/xls");
				response.setHeader("Content-Disposition", "attachment; filename=" + reportName + ".xlsx");
				response.setContentType("application/vnd.ms-excel");
			}

			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();

			adminDao.printReport(jasperPrint, format, printStatus, outputStream, reportName);

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
