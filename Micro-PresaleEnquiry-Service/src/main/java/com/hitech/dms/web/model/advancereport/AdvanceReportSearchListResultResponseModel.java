package com.hitech.dms.web.model.advancereport;

import java.util.List;

import lombok.Data;

@Data
public class AdvanceReportSearchListResultResponseModel {

	private List<AdvanceTrackingReportResponseModel> searchResult;
	private Integer recordCount;
}
