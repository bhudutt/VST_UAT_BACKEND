/**
 * 
 */
package com.hitech.dms.web.model.emp.response;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
@ToString
public class EmployeeResponseModel {
	private BigInteger empId;
	private String empCode;
	private String empName;
	private String displayValue;
	private BigInteger reportingId;
	private String isDefaultForBranch;
}
