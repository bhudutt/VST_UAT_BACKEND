/**
 * 
 */
package com.hitech.dms.app.config.report;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @author dinesh.jakhar
 *
 */
@Configuration
public class JasperRerportsConfig {
	
	@Bean
	@Scope(value="prototype")
	public ReportFillerConfig getReportFillerConfig() {
		return new ReportFillerConfig();
	}
	
	@Bean
	@Scope(value="prototype")
	public ReportExporterConfig getReportExporterConfig() {
		return new ReportExporterConfig();
	}
}
