package com.hitech.dms.web.model.dealer.employee.create.response;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author vinay.gautam
 *
 */
@Setter
@Getter
public class DealerEmployeeReportingResponseModel {
	
	private String displayValue;
	private String reportingEmpCode;
	private BigInteger reportingEmpId;
	private String reportingEmpName;

}
