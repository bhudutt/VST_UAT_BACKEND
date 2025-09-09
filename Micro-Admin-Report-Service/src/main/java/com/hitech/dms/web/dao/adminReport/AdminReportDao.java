/**
 * 
 */
package com.hitech.dms.web.dao.adminReport;

import java.io.OutputStream;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

/**
 * @author santosh.kumar
 *
 */
public interface AdminReportDao {

	/**
	 * @param jasperPrint
	 * @param format
	 * @param printStatus
	 * @param outputStream
	 * @param reportName
	 * @throws JRException 
	 */
	void printReport(JasperPrint jasperPrint, String format, String printStatus, OutputStream outputStream,
			String reportName) throws JRException;

	/**
	 * @param request
	 * @param string
	 * @param jasperParameter
	 * @param filePath
	 * @return
	 */
	JasperPrint ExcelGeneratorReport(HttpServletRequest request, String string, HashMap<String, Object> jasperParameter,
			String filePath);

}
