package com.hitech.dms.web.model.activitycreditnote.search.request;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hitech.dms.app.utils.DateHandler;

import lombok.Data;



@Data
public class SearchActivityCreditNoteRequestModel {
	
	@JsonDeserialize(using = DateHandler.class)
	private Date FromDate;
	@JsonDeserialize(using = DateHandler.class)
	private Date ToDate;
	
	private String vendorinvoiceno;
	
	private Integer page;
	
	private Integer size;
}
