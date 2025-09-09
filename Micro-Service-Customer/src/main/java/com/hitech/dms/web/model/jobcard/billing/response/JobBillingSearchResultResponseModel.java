package com.hitech.dms.web.model.jobcard.billing.response;

import java.util.List;

import lombok.Data;

@Data
public class JobBillingSearchResultResponseModel {

	List<JobBillingSearchResponseModel> searchResult;
	private Integer recordCount;
	
}
