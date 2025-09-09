package com.hitech.dms.web.model.partybybranch.create.request;

import lombok.Data;

@Data
public class PartySearchExcelRequst {
	
	private String categoryCode;
	private String partyCode;
	private String partyName;
	private String status;
	private Integer page;
	private Integer size;

}
