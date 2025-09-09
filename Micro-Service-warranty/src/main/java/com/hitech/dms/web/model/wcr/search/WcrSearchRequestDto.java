package com.hitech.dms.web.model.wcr.search;

import java.sql.Date;

import lombok.Data;

@Data
public class WcrSearchRequestDto {
	private String wcrNo;
	private String wcrType;
	private String wcrStatus;
	private String jobCardNo;
	private String pcrNo;
	private String chassisNo;
	private Date fromDate;
	private Date toDate;
	private Integer page;
	private Integer size;

}
