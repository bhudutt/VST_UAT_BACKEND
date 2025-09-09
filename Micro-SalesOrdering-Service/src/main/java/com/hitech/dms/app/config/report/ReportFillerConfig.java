/**
 * 
 */
package com.hitech.dms.app.config.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRSaver;

/**
 * @author dinesh.jakhar
 *
 */
//@Component
public class ReportFillerConfig {
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ReportFillerConfig.class);

	private String reportFileName;

	private JasperReport jasperReport;

	private JasperPrint jasperPrint;

	@Autowired
	private DataSource dataSource;

	private Map<String, Object> parameters;

	public ReportFillerConfig() {
		parameters = new HashMap<>();
	}

	public void prepareReport() {
		try {
			compileReport();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fillReport();
	}

	public void compileReport() throws FileNotFoundException {
		try {
			File file = ResourceUtils.getFile("classpath:reports/"+reportFileName);
//			InputStream reportStream = getClass().getResourceAsStream("reports/".concat(reportFileName));
			InputStream reportStream = new FileInputStream(file);
			jasperReport = JasperCompileManager.compileReport(reportStream);
			JRSaver.saveObject(jasperReport, reportFileName.replace(".jrxml", ".jasper"));
		} catch (JRException ex) {
			Logger.getLogger(ReportFillerConfig.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void fillReport() {
		try {
			jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource.getConnection());
		} catch (JRException | SQLException ex) {
			Logger.getLogger(ReportFillerConfig.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public Map<String, Object> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	public String getReportFileName() {
		return reportFileName;
	}

	public void setReportFileName(String reportFileName) {
		this.reportFileName = reportFileName;
	}

	public JasperPrint getJasperPrint() {
		return jasperPrint;
	}
}
