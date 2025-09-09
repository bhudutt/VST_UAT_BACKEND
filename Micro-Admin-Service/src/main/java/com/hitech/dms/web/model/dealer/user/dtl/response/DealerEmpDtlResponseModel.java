/**
 * 
 */
package com.hitech.dms.web.model.dealer.user.dtl.response;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class DealerEmpDtlResponseModel {
	private String empCode;
	private BigInteger empId;
	private String mobileNumber;
	private String displayValue;
}
