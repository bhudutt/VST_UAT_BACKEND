package com.hitech.dms.web.model.service.report.request;

import java.util.List;

import lombok.Data;

@Data
public class HistoryCardResponseList {

	private String msg;
	private String status;
	private HistoryCardResponse response;
	private List<HistoryCardDtlResponse> searchList; 
}
