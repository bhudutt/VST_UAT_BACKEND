/**
 * 
 */
package com.hitech.dms.web.model.opex.template.request;

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
public class OpexBudgetRequestModel {
	private Integer pcId;
	private String finYear;
	private int lastFinYearCount;
	private String isFor;
	private BigInteger opexId;
}
