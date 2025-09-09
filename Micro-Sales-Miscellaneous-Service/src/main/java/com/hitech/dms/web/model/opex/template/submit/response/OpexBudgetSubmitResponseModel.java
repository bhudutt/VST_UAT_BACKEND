/**
 * 
 */
package com.hitech.dms.web.model.opex.template.submit.response;

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
public class OpexBudgetSubmitResponseModel {
	private BigInteger opexId;
	private String opexNumber;
	private String msg;
	private int statusCode;
}
