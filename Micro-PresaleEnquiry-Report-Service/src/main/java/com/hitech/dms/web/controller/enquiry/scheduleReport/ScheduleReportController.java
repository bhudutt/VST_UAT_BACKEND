package com.hitech.dms.web.controller.enquiry.scheduleReport;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@Validated
@RestController
@RequestMapping("/toc")
@SecurityRequirement(name = "hitechApis")
public class ScheduleReportController {

	private static final Logger logger = LoggerFactory.getLogger(ScheduleReportController.class);
	
	@Autowired
	ScheduleReport scheduleReport;

	
	@GetMapping("/stockReport")
	public void loginAndCreateStockCsv() throws IOException {
		logger.info("API call for stock download");
			scheduleReport.generateTOCStockCsvReport();
			scheduleReport.generateTOCGitCsvReport();
			scheduleReport.generateTOCConsumptionDistributorCsvReport();
			scheduleReport.generateTOCReceiptDistributorCsvReport();
			scheduleReport.generateTOCGitDistributorCsvReport();
			scheduleReport.generateTOCStockDistributorCsvReport();
	}
}
