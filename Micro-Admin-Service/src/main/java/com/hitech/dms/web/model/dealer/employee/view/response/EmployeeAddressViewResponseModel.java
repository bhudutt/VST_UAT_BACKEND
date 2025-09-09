/**
 * 
 */
package com.hitech.dms.web.model.dealer.employee.view.response;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class EmployeeAddressViewResponseModel {
	private BigInteger empAddressId;

	private BigInteger employeeId;

	private String custAddress1;
	
	private String custAddLine2;

	private String custAddLine3;

	private BigInteger cityId;
	private String city;

	private BigInteger pinId;
	private String locality;
	private String tehsil;
	private BigInteger tehsilId;	
	private String district;
	private BigInteger districtId;
	private String state;
	private BigInteger stateId;
	private String country;
	private BigInteger countryId;

	private String pinCode;
}
