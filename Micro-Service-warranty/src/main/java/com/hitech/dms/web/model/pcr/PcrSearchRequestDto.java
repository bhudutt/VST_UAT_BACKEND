package com.hitech.dms.web.model.pcr;

import java.sql.Date;

import lombok.Data;

@Data
public class PcrSearchRequestDto {
	
//	private String userCode;
//    private String profitCenter;
//    private String dealership;
//    private String branch;
	private String pcrNo;
    private String jobCardNo;
	private String status;
	private Date fromDate;
	private Date toDate;
	private Integer page;
	private Integer size; 
}
