/**
 * @author vinay.gautam
 *
 */
package com.hitech.dms.web.model.dealer.employee.create.request;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */
@Data
public class DealerEmployeeAddressRequestModel {
	
	private BigInteger empAddressId;

	private BigInteger employeeId;

	private String custAddress1;
	
	private String custAddLine2;

	private String custAddLine3;

	private BigInteger cityId;

	private BigInteger pinId;

	private String pinCode;

}
