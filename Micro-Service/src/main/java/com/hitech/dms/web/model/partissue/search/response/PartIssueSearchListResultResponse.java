package com.hitech.dms.web.model.partissue.search.response;

import java.util.List;

import lombok.Data;

@Data
public class PartIssueSearchListResultResponse {

	private List<PartIssueSearchResponseModel> searchResult;
	private Integer recordCount;
	
}
