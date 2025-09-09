/**
 * 
 */
package com.hitech.dms.web.dao.adminReport;

import java.io.OutputStream;
import java.sql.Connection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.hitech.dms.app.config.persistence.ConnectionConfiguration;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

/**
 * @author santosh.kumar
 *
 */
@Repository
public class AdminReportDaoImpl implements AdminReportDao {
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private ConnectionConfiguration dataSourceConnection;

	@Override
	public void printReport(JasperPrint jasperPrint, String format, String printStatus, OutputStream outputStream,
			String reportName) throws JRException {
		if (format != null && format.equalsIgnoreCase("xls")) {
			JRXlsxExporter exporter = new JRXlsxExporter();
			SimpleXlsxReportConfiguration reportConfigXLS = new SimpleXlsxReportConfiguration();
			reportConfigXLS.setSheetNames(new String[] { "sheet1" });
			exporter.setConfiguration(reportConfigXLS);
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
			exporter.exportReport();
		}

	}

	@Override
	public JasperPrint ExcelGeneratorReport(HttpServletRequest request, String jaspername,
			HashMap<String, Object> jasperParameter, String filePath) {
		JasperPrint jasperPrint = null;
		Connection connection = null;
		try {

			String filePaths = filePath + jaspername;

			connection = dataSourceConnection.getConnection();

			if (connection != null) {

				jasperPrint = JasperFillManager.fillReport(filePaths, jasperParameter, connection);

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
