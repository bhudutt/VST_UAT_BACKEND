package com.hitech.dms.web.model.attendance.search.request;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hitech.dms.app.utils.DateHandler;

import lombok.Data;
@Data
public class AttendanceSearchRequestModel {


	private Integer pcId;
	private String includeInActive;
	private int page;
	private int size;
	private String zone;
	private String area;
	
	private String fromDate1;
	private String toDate1;
	private Integer dealerId1;
	private Integer branchId1;
	private Integer orgHierID1;
	

}
