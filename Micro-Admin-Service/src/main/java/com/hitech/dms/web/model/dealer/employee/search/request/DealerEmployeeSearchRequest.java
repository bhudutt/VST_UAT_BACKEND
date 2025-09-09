package com.hitech.dms.web.model.dealer.employee.search.request;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hitech.dms.app.utils.DateHandler;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */
@Data
public class DealerEmployeeSearchRequest {
	private BigInteger dealerId;
	private BigInteger branchId;
	private String employeeCode;
	private String mobileNo;
	@JsonDeserialize(using = DateHandler.class)
	private Date fromDate;
	@JsonDeserialize(using = DateHandler.class)
	private Date toDate;
	private BigInteger orgHierId;
	private Integer page;
	private Integer size;
	private String includeInactive;
	private String status;
	
	private String fromDate1;
	private String toDate1;
	
	


}
