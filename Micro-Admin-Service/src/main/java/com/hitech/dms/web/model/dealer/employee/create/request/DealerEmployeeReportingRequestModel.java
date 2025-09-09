package com.hitech.dms.web.model.dealer.employee.create.request;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author vinay.gautam
 *
 */
@Setter
@Getter
public class DealerEmployeeReportingRequestModel {
	
	private BigInteger dealerId;
	private String searchText;
	private String empCode;

}
