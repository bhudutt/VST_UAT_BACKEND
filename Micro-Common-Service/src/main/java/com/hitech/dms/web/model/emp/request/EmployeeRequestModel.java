/**
 * 
 */
package com.hitech.dms.web.model.emp.request;

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
public class EmployeeRequestModel {
	private BigInteger dealerId;
	private BigInteger branchId;
	private String isFor;
	private String isIncludeActive;
}
