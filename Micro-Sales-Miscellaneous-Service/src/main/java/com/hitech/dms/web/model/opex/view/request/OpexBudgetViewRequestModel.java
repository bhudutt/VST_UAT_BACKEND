/**
 * 
 */
package com.hitech.dms.web.model.opex.view.request;

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
public class OpexBudgetViewRequestModel {
	private BigInteger opexId;
	private String opexNumber;
	private int flag;
}
