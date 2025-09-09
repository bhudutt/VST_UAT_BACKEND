package com.hitech.dms.web.model.activitycreditnote.search.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchActivityCreditNoteResultResponseModel {

	
	private List<SearchActivityCreditNoteResponseModel> searchResult;
	private Integer recordCount;
}
