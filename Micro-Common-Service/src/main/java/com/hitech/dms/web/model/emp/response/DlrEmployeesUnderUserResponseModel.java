/**
 * 
 */
package com.hitech.dms.web.model.emp.response;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class DlrEmployeesUnderUserResponseModel {
	private BigInteger empId;
	private String empCode;
	private String empName;
	private BigInteger reportingId;
}
