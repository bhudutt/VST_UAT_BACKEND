/**
 * 
 */
package com.hitech.dms.web.model.dealer.user.dtl.request;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class DealerEmpDtlRequestModel {
	private String empCode;
	private BigInteger empId;
}
