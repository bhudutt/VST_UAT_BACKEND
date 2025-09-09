package com.hitech.dms.web.model.goodwill;

import java.sql.Date;

import lombok.Data;

@Data
public class GoodwillSearchRequestDto {
	private String goodwillNo;
	private String pcrNo;
	private String jobCardNo;
	private String chassisNo;
	private String status;
	private Date fromDate;
	private Date toDate;
	private Integer page;
	private Integer size; 
	

}
