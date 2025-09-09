package com.hitech.dms.web.model.dealer.employee.create.response;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */

@Data
public class DealerEmployeeCreateResponseModel {
	
	private String msg;
	private Integer statusCode;
	private String empCode;
	private BigInteger employeeId;

}
